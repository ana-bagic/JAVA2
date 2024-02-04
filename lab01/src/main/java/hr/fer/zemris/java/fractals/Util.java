package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.math.Complex;

/**
 * Razred za pomoćne metode.
 * 
 * @author Ana Bagić
 *
 */
public class Util {
	
	/**
	 * Parsira broj dretvi i broj traka koji su primljeni kao parametri.
	 * Ako broj dretvi nije zadan, postavlja se na broj procesora trenutno na raspolaganju.
	 * Ako broj traka nije zadan, postavlja se na 4 * broj procesora trenutno na raspolaganju.
	 * 
	 * @param args parametri primljeni od korisnika
	 * @return listu koja sadrži izračunat broj dretvi i traka
	 */
	public static List<Integer> calcWorkersAndTracks(String[] args) {
		int workers = -1;
		int tracks = -1;

		for (int i = 0; i < args.length; i++) {
			try {
				if (args[i].startsWith("--workers=")) {
					if (workers != -1)
						throw new IllegalArgumentException();

					int result = Integer.parseInt(args[i].substring(10));
					if (result < 1)
						throw new IllegalArgumentException();

					workers = result;
				} else if (args[i].startsWith("--tracks=")) {
					if (tracks != -1)
						throw new IllegalArgumentException();

					int result = Integer.parseInt(args[i].substring(9));
					if (result < 1)
						throw new IllegalArgumentException();

					tracks = result;
				} else if (args[i].equals("-w")) {
					if (workers != -1 || i == args.length - 1)
						throw new IllegalArgumentException();

					i++;
					int result = Integer.parseInt(args[i]);
					if (result < 1)
						throw new IllegalArgumentException();

					workers = result;
				} else if (args[i].equals("-t")) {
					if (tracks != -1 || i == args.length - 1)
						throw new IllegalArgumentException();

					i++;
					int result = Integer.parseInt(args[i]);
					if (result < 1)
						throw new IllegalArgumentException();

					tracks = result;
				} else {
					throw new IllegalArgumentException("Illegal argument: " + args[i]);
				}

			} catch (Exception e) {
				throw new IllegalArgumentException("Illegal argument: " + args[i]);
			}
		}

		if (workers <= 0)
			workers = Runtime.getRuntime().availableProcessors();
		if (tracks <= 0)
			tracks = Runtime.getRuntime().availableProcessors() * 4;
		
		List<Integer> result = new ArrayList<>();
		result.add(workers);
		result.add(tracks);
		
		return result;
	}
	

	/**
	 * Parsira minimalni broj traka koji je primljen kao parametar.
	 * Ako broj traka nije zadan, postavlja se na 16.
	 * 
	 * @param args parametar primljen od korisnika
	 * @return minimalni broj traka
	 */
	public static int calcMinTracks(String[] args) {
		if(args.length == 0) {
			return 16;
		} else {
			try {
				if (args[0].startsWith("--mintracks=")) {
					if (args.length != 1)
						throw new IllegalArgumentException();
					
					int result = Integer.parseInt(args[0].substring(12));
					if (result < 1)
						throw new IllegalArgumentException();

					return result;
				} else if (args[0].equals("-m")) {
					if (args.length != 2)
						throw new IllegalArgumentException();

					int result = Integer.parseInt(args[1]);
					if (result < 1)
						throw new IllegalArgumentException();

					return result;
				} else {
					throw new IllegalArgumentException("Illegal argument: " + args[0]);
				}
			} catch (Exception e) {
				throw new IllegalArgumentException("Illegal argument: " + args[0]);
			}
		}
	}

	/**
	 * Parsira ulazni string u kompleksan broj.
	 * 
	 * @param s kompleksan broj u obliku stringa
	 * @return parsirani kompleksan broj
	 */
	public static Complex parseToComplex(String s) {
		if(s == null || s.equals(""))
			throw new IllegalArgumentException("Complex number is not given.");
		if(s.contains("++") || s.contains("+-") || s.contains("-+") || s.contains("--"))
			throw new IllegalArgumentException("Two operators are together.");
		
		boolean firstIsPos = true, secondIsPos = true;
		if(s.startsWith("-"))
			firstIsPos = false;
		if(s.indexOf("-", 1) != -1)
			secondIsPos = false;
		
		String[] numbers = s.split(" ");
		String tmp = String.join("", numbers);
		if(tmp.startsWith("-") || tmp.startsWith("+"))
			numbers = tmp.substring(1).split("[+-]");
		else
			numbers = tmp.split("[+-]");
		
		double real = 0, imaginary = 0;
		switch (numbers.length) {
		case 1 -> {
			if(!numbers[0].startsWith("i"))
				real = parseDoubleOrThrow(numbers[0]);
			else
				imaginary = numbers[0].length() == 1 ? 1
						: parseDoubleOrThrow(numbers[0].substring(1));
			
			if(!firstIsPos) {
				real *= -1;
				imaginary *= -1;
			}
		}
		case 2 -> {
			real = parseDoubleOrThrow(numbers[0]);
			
			if(numbers[1].startsWith("i"))
				imaginary = numbers[1].length() == 1 ? 1
					: parseDoubleOrThrow(numbers[1].substring(1));
			else
				throw new IllegalArgumentException(numbers[1] + " is not properly given real part of a complex number.");
			
			real = real*(firstIsPos ? 1 : -1);
			imaginary = imaginary*(secondIsPos ? 1 : -1);
		}
		default ->
			throw new IllegalArgumentException(s + " is not a complex number.");
		}
		
		real = real == 0 ? 0 : real;
		imaginary = imaginary == 0 ? 0 : imaginary;
		
		return new Complex(real, imaginary);
	}
	
	/**
	 * Pomoćna metoda koja parsira zadani string u double ili baca iznimku {@link IllegalArgumentException} s odgovarajućom porukom.
	 * 
	 * @param d string koji treba parsirati u double
	 * @return parsirani double
	 * @throws IllegalArgumentException ako se string ne može parsirati u double
	 */
	private static double parseDoubleOrThrow(String d) {
		double result;
		
		try {
			result = Double.parseDouble(d);
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException(d + " is not a real number.");
		}
		
		return result;
	}
}
