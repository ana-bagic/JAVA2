package hr.fer.zemris.java.fractals;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.java.math.Complex;
import hr.fer.zemris.java.math.ComplexPolynomial;
import hr.fer.zemris.java.math.ComplexRootedPolynomial;

/**
 * Program stvara Newton-Raphson fraktal zadavanjem nultočaka.
 * Vizualizacija je ostvarena višedretvenošću koristeći ForkJoinPool.
 * 
 * @author Ana Bagić
 *
 */
public class NewtonP2 {

	public static void main(String[] args) {
		if (args.length > 2)
			throw new IllegalArgumentException("Too many arguments.");

		int minTracks = Util.calcMinTracks(args);

		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

		List<Complex> roots = new LinkedList<>();
		int counter = 1;
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.print("Root " + counter + "> ");
			String nextLine = sc.nextLine().trim();

			if (nextLine.equals("done"))
				break;

			try {
				roots.add(Util.parseToComplex(nextLine));
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
				continue;
			}

			counter++;
		}

		sc.close();
		System.out.println("Image od fractal will appear shortly. Thank you.");
		System.out.println("Minimal number of tracks calculated sequentially: " + minTracks);
		
		FractalViewer.show(new MyProducer(roots, minTracks));
	}

	/**
	 * Posao koji obavlja jedna dretva.
	 * 
	 * @author Ana Bagić
	 *
	 */
	public static class Job extends RecursiveAction {
		private static final long serialVersionUID = 1L;
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		int m;
		short[] data;
		AtomicBoolean cancel;
		ComplexRootedPolynomial pol;
		int minTracks;

		public Job(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin, int yMax,
				int m, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial pol, int minTracks) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.cancel = cancel;
			this.pol = pol;
			this.minTracks = minTracks;
		}

		@Override
		protected void compute() {
			int h = yMax - yMin + 1;
			if(h <= minTracks) {
				computeDirect();
				return;
			}
			
			Job j1 = new Job(reMin, reMax, imMin, imMax, width, height, yMin, yMin + h/2 - 1, m, data, cancel, pol, minTracks);
			Job j2 = new Job(reMin, reMax, imMin, imMax, width, height, yMin + h/2, yMax, m, data, cancel, pol, minTracks);
			invokeAll(j1, j2);
		}

		/**
		 * Pomoćna metoda koja računa poslove koji se više ne dekomponiraju.
		 */
		private void computeDirect() {
			ComplexPolynomial polynomial = pol.toComplexPolynom();
			ComplexPolynomial derived = polynomial.derive();

			int offset = width * yMin;
			double convTreshold = 1E-3;
			double rootTreshold = 2E-3;
			for (int y = yMin; y <= yMax; y++) {
				if (cancel.get())
					break;

				for (int x = 0; x < width; x++) {
					double zRe = x / (width - 1.0) * (reMax - reMin) + reMin;
					double zIm = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;
					Complex zn = new Complex(zRe, zIm);

					double module = 0;
					int iters = 0;
					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex fraction = numerator.divide(denominator);
						Complex znold = zn;
						Complex znSub = zn.sub(fraction);
						zn = znSub;
						module = znold.sub(zn).module();

						iters++;
					} while (iters < m && module > convTreshold);
					int index = pol.indexOfClosestRootFor(zn, rootTreshold);
					data[offset++] = (short) (index + 1);
				}
			}
		}
	}

	/**
	 * Razred višedretveno vizualizira fraktal korištenjem ForkJoinPool.
	 * 
	 * @author Ana Bagić
	 *
	 */
	public static class MyProducer implements IFractalProducer {

		/**
		 * Polinom korišten za vizualizaciju fraktala.
		 */
		private ComplexRootedPolynomial pol;
		/**
		 * Minimalni broj redaka koji će se slijedno izvoditi.
		 */
		private int minTracks;
		/**
		 * Bazen dretvi.
		 */
		private ForkJoinPool pool;

		public MyProducer(List<Complex> roots, int minTracks) {
			pol = new ComplexRootedPolynomial(Complex.ONE, roots.stream().toArray(Complex[]::new));
			this.minTracks = minTracks;
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

			int m = 16 * 16 * 16;
			short[] data = new short[width * height];

			Job job = new Job(reMin, reMax, imMin, imMax, width, height, 0, height-1, m, data, cancel, pol, minTracks);
			pool.invoke(job);

			observer.acceptResult(data, (short) (pol.toComplexPolynom().order() + 1), requestNo);
		}

		@Override
		public void setup() {
			pool = new ForkJoinPool();
		}
		
		@Override
		public void close() {
			pool.shutdown();
		}
	}
}
