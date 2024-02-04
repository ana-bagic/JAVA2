package hr.fer.zemris.java.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Razred modelira nepromjenjivi kompleksni broj.
 * 
 * @author Ana Bagić
 *
 */
public class Complex {
	
	/**
	 * Kompleksni broj 0.
	 */
	public static final Complex ZERO = new Complex(0,0);
	/**
	 * Kompleksni broj 1.
	 */
	public static final Complex ONE = new Complex(1,0);
	/**
	 * Kompleksni broj -1.
	 */
	public static final Complex ONE_NEG = new Complex(-1,0);
	/**
	 * Kompleksni broj i.
	 */
	public static final Complex IM = new Complex(0,1);
	/**
	 * Kompleksni broj -i.
	 */
	public static final Complex IM_NEG = new Complex(0,-1);
	
	/**
	 * Realni dio kompleksnog broja.
	 */
	private double real;
	/**
	 * Imaginarni dio kompleksnog broja.
	 */
	private double imaginary;
	
	/**
	 * Defaultni konstruktor stvara kompleksni broj (0, 0).
	 */
	public Complex() {
		real = 0;
		imaginary = 0;
	}
	
	/**
	 * Konstruktor stvara novi kompleksni broj zadavanjem realnog i imaginarnog dijela.
	 * 
	 * @param re realni dio kompleksnog broja
	 * @param im imaginarni dio kompleksnog broja
	 */
	public Complex(double re, double im) {
		real = re;
		imaginary = im;
	}
	
	/**
	 * Vraća modul kompleksnog broja.
	 * 
	 * @return modul kompleksnog broja
	 */
	public double module() {
		return Math.sqrt(real*real + imaginary*imaginary);
	}
	
	/**
	 * Množi kompleksni broj nad kojim pozivamo metodu s onim kojega šaljemo.
	 * 
	 * @param c kompleksni broj kojeg množimo
	 * @return umnožak kompleksnih brojeva u obliku kompleksnog broja
	 * @throws NullPointerException ako je poslani kompleksni broj <code>null</code>
	 */
	public Complex multiply(Complex c) {
		Objects.requireNonNull(c, "Argument is null.");
		
		double newReal = this.real*c.real - this.imaginary*c.imaginary;
		double newImaginary = this.real*c.imaginary + c.real*this.imaginary;
		return new Complex(newReal, newImaginary);
	}
	
	/**
	 * Dijeli kompleksni broj nad kojim pozivamo metodu s onim kojega šaljemo.
	 * 
	 * @param c kompleksni broj s kojim dijelimo
	 * @return količnik kompleksnih brojeva u obliku kompleksnog broja
	 * @throws NullPointerException ako je poslani kompleksni broj <code>null</code>
	 * @throws IllegalArgumentException ako je poslani kompleksni broj 0.0
	 */
	public Complex divide(Complex c) {
		Objects.requireNonNull(c, "Argument is null.");
		if(c.real == 0.0 && c.imaginary == 0.0)
			throw new IllegalArgumentException("Argument is 0.");
		
		double realN = this.real*c.real + this.imaginary*c.imaginary;
		double denominator = c.real*c.real + c.imaginary*c.imaginary;
		double imaginaryN = this.imaginary*c.real - this.real*c.imaginary;
		
		return new Complex(realN/denominator, imaginaryN/denominator);
	}
	
	/**
	 * Zbraja kompleksni broj nad koji pozivamo metodu s onim kojega šaljemo.
	 * 
	 * @param c kompleksni broj kojeg zbrajamo
	 * @return zbroj kompleksnih brojeva u obliku kompleksnog broja
	 * @throws NullPointerException ako je poslani kompleksni broj <code>null</code>
	 */
	public Complex add(Complex c) {
		Objects.requireNonNull(c, "Argument is null.");
		
		return new Complex(this.real + c.real, this.imaginary + c.imaginary);
	}
	
	/**
	 * Od kompleksnog broja nad kojim pozivamo metodu oduzima onog kojega šaljemo.
	 * 
	 * @param c kompleksni broj kojeg oduzimamo
	 * @return razlika kompleksnih brojeva u obliku kompleksnog broja
	 * @throws NullPointerException ako je poslani kompleksni broj <code>null</code>
	 */
	public Complex sub(Complex c) {
		Objects.requireNonNull(c, "Argument is null.");
		
		return new Complex(this.real - c.real, this.imaginary - c.imaginary);
	}
	
	/**
	 * Negira kompleksni broj.
	 * 
	 * @return negirani kompleksni broj
	 */
	public Complex negate() {
		return multiply(ONE_NEG);
	}
	
	/**
	 * Potencira kompleksni broj na odabranu potenciju.
	 * 
	 * @param n potencija na koju potenciramo
	 * @return kompleksni broj na n-tu potenciju
	 * @throws IllegalArgumentException ako je parametar negativan broj
	 */
	public Complex power(int n) {
		if(n < 0)
			throw new IllegalArgumentException("Argument is a negative number.");
		
		return fromMagnitudeAndAngle(Math.pow(module(), n), n*angle());
	}
	
	/**
	 * Vraća listu kompleksnih brojeva koji su rezultati n-tog korijena kompleksnog broja.
	 * 
	 * @param n korijen koji tražimo
	 * @return lista kompleksnih brojeva na n-tu potenciju
	 * @throws IllegalArgumentException ako parametar nije pozitivan broj
	 */
	public List<Complex> root(int n) {
		if(n <= 0)
			throw new IllegalArgumentException("Argument is not a positive number.");
		
		List<Complex> numbers = new ArrayList<>(n);
		double rootMagnitude = Math.pow(module(), 1.0/n);
		
		for(int i=0; i<n; i++)
			numbers.add(fromMagnitudeAndAngle(rootMagnitude, (angle()+2*i*Math.PI)/n));
		
		return numbers;
	}
	
	@Override
	public String toString() {
		StringBuilder sb =  new StringBuilder();
		
		sb.append(real);
		sb.append(imaginary >= 0 ? "+" : "-");
		sb.append("i").append(Math.abs(imaginary));
		
		return sb.toString();
	}
	
	/**
	 * Vraća kut (u radijanima) kompleksnog broja u odnosu na realnu os.
	 * 
	 * @return kut (u radijanima) kompleksnog broja u odnosu na realnu os
	 */
	private double angle() {
		double arctg = Math.atan2(imaginary, real);
		return arctg + (arctg < 0 ? Math.PI*2 : 0);
	}
	
	/**
	 * Stvara novi kompleksni broj iz polarnih koordinata.
	 * 
	 * @param module udaljenost od ishodišta
	 * @param angle kut (u radijanima) u odnosu na realnu os
	 * @return stvoreni kompleksni broj
	 */
	public static Complex fromMagnitudeAndAngle(double module, double angle) {
		return new Complex(module*Math.cos(angle), module*Math.sin(angle));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Complex))
			return false;
		Complex other = (Complex) obj;
		
		if (round(real) != round(other.real))
			return false;
		if (round(imaginary) != round(other.imaginary))
			return false;
		return true;
	}
	
	/**
	 * Metoda za zaokruživanje na 4 decimalna mjesta kako bi se mogli uspoređivati doubleovi.
	 * 
	 * @param value double koji želimo zaokružiti
	 * @return zaokruženi double
	 */
	private double round(double value) {
		return (double) Math.round(value * 10000d) / 10000d;
	}
}
