package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.java.math.Complex;
import hr.fer.zemris.java.math.ComplexPolynomial;
import hr.fer.zemris.java.math.ComplexRootedPolynomial;

/**
 * Program stvara Newton-Raphson fraktal zadavanjem nultočaka.
 * Vizualizacija je ostvarena višedretvenošću koristeći ExecutorService.
 * 
 * @author Ana Bagić
 *
 */
public class NewtonP1 {

	public static void main(String[] args) {
		if (args.length > 4)
			throw new IllegalArgumentException("Too many arguments.");

		List<Integer> userParameters = Util.calcWorkersAndTracks(args);

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
		System.out.println("Number of threads: " + userParameters.get(0));
		
		FractalViewer.show(new MyProducer(roots, userParameters.get(0), userParameters.get(1)));
	}

	/**
	 * Posao koji obavlja jedna dretva.
	 * 
	 * @author Ana Bagić
	 *
	 */
	public static class Job implements Runnable {

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

		public Job(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin, int yMax,
				int m, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial pol) {
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
		}

		@Override
		public void run() {

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
	 * Razred višedretveno vizualizira fraktal korištenjem ExecutorService.
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
		 * Broj poslova.
		 */
		private int tracks;
		/**
		 * Broj dretvi.
		 */
		private int workers;
		/**
		 * Bazen dretvi.
		 */
		private ExecutorService pool;

		public MyProducer(List<Complex> roots, int workers, int tracks) {
			pol = new ComplexRootedPolynomial(Complex.ONE, roots.stream().toArray(Complex[]::new));
			this.workers = workers;
			this.tracks = tracks;
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

			int m = 16 * 16 * 16;
			short[] data = new short[width * height];

			if (tracks > height)
				tracks = height;
			System.out.println("Number of tracks: " + tracks);

			int rowsForTrack = height / tracks;
			
			List<Job> jobs = new ArrayList<>();
			for(int i = 0; i < tracks; i++) {
				int yMin = i * rowsForTrack;
				int yMax = (i + 1) * rowsForTrack - 1;

				if (i == tracks - 1)
					yMax = height - 1;
				
				jobs.add(new Job(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel, pol));
			}
			
			List<Future<?>> futures = new ArrayList<>();
			for(Job j : jobs) {
				futures.add(pool.submit(j));
			}
			
			for(Future<?> f : futures) {
				while(true) {
					try {
						f.get();
						break;
					} catch (InterruptedException | ExecutionException e) {
					}
				}
			}

			observer.acceptResult(data, (short) (pol.toComplexPolynom().order() + 1), requestNo);
		}

		@Override
		public void setup() {
			pool = Executors.newFixedThreadPool(workers);
		}
		
		@Override
		public void close() {
			pool.shutdown();
		}
	}
}
