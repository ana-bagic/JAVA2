package hr.fer.zemris.java.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Razred modelira polinom nad kompleksnim brojevima
 * u obliku zn*z^n+zn-1*z^(n-1)+...+z2*z^2+z1*z+z0.
 * 
 * @author Ana Bagić
 *
 */
public class ComplexPolynomial {
	
	/**
	 * Koeficijenti polinoma.
	 */
	private List<Complex> factors;

	/**
	 * Konstruktor stvara novi polinom zadavanjem faktora.
	 * Polinom je u obliku zn*z^n+zn-1*z^(n-1)+...+z2*z^2+z1*z+z0.
	 * 
	 * @param factors koeficijenti polinoma
	 */
	public ComplexPolynomial(Complex ... factors) {
		this.factors = new ArrayList<>(factors.length);
		
		for(Complex c : factors)
			this.factors.add(c);
	}
	
	/**
	 * Vraća red polinoma.
	 * 
	 * @return red polinoma
	 */
	public short order() {
		return (short) (factors.size() - 1);
	}

	/**
	 * Vraća umnožak trenutnog polinoma i poslanog.
	 * 
	 * @param p polinom koji se želi pomnožiti s trenutnim
	 * @return umnožak trenutnog polinoma i poslanog
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		List<Complex> result = new ArrayList<>();
		
		for(int i = 0; i < this.order() + p.order() + 1; i++) {
			result.add(Complex.ZERO);
		}
		
		for(int i = 0; i < this.order() + 1; i++) {
			for(int j = 0; j < p.order() + 1; j++) {
				Complex c = result.get(i+j).add(this.factors.get(i).multiply(p.factors.get(j)));
				result.set(i+j, c);
			}
		}
		
		return new ComplexPolynomial(result.stream().toArray(Complex[]::new));
	}
	
	/**
	 * Računa prvu derivaciju polinoma.
	 * 
	 * @return prva derivacija polinoma
	 */
	public ComplexPolynomial derive() {
		List<Complex> newFactors = new ArrayList<>(factors.size() - 1);
		
		for(int i = 1; i < factors.size(); i++) {
			newFactors.add(factors.get(i).multiply(new Complex(i, 0)));
		}
		
		return new ComplexPolynomial(newFactors.stream().toArray(Complex[]::new));
	}
	
	/**
	 * Računa vrijednost polinoma za zadani kompleksni broj.
	 * 
	 * @param z kompleksni broj za koji se želi izračunati vrijednost polinoma
	 * @return vrijednost polinoma
	 */
	public Complex apply(Complex z) {
		int size = factors.size();
		Complex result = factors.get(0);
		
		for(int i = 1; i < size; i++) {
			result = result.add(factors.get(i).multiply(z.power(i)));
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = factors.size() - 1; i > 0; i--) {
			sb.append("(");
			sb.append(factors.get(i));
			sb.append(")*z^").append(i).append(" + ");
		}
		
		sb.append("(").append(factors.get(0)).append(")");
		
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ComplexPolynomial))
			return false;
		ComplexPolynomial other = (ComplexPolynomial) obj;
		
		if(this.factors.size() != other.factors.size())
			return false;
		
		for(int i = 0; i < this.factors.size(); i++) {
			if(!this.factors.get(i).equals(other.factors.get(i)))
				return false;
		}
		return true;
	}
	
}
