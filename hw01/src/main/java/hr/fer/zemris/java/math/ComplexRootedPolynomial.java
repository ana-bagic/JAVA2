package hr.fer.zemris.java.math;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Razred modelira polinom nad kompleksnim brojevima
 * u obliku z0*(z-z1)*(z-z2)*...*(z-zn).
 * 
 * @author Ana Bagić
 *
 */
public class ComplexRootedPolynomial {

	/**
	 * Konstanta polinoma.
	 */
	private Complex constant;
	/**
	 * Nultočke polinoma.
	 */
	private List<Complex> roots;
	
	/**
	 * Konstruktor stvara novi polinom zadavanjem konstante i nultočaka.
	 * Polinom je u obliku z0*(z-z1)*(z-z2)*...*(z-zn).
	 * 
	 * @param constant konstanta polinoma - z0
	 * @param roots nultočke polinoma - z1, ..., zn
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.roots = new ArrayList<>(roots.length);
		
		this.constant = constant;
		for(Complex c : roots)
			this.roots.add(c);
	}
	
	/**
	 * Računa vrijednost polinoma za zadani kompleksni broj.
	 * 
	 * @param z kompleksni broj za koji se želi izračunati vrijednost polinoma
	 * @return vrijednost polinoma
	 */
	public Complex apply(Complex z) {
		Complex result = constant;
		
		for(Complex c : roots) {
			result = result.multiply(z.sub(c));
		}
		
		return result;
	}

	/**
	 * Stvara novi polinom oblika zn*z^n+zn-1*z^(n-1)+...+z2*z^2+z1*z+z0 iz trenutnog polinoma.
	 * 
	 * @return novi polinom oblika zn*z^n+zn-1*z^(n-1)+...+z2*z^2+z1*z+z0 jednak trenutnom polinomu
	 */
	public ComplexPolynomial toComplexPolynom() {
		int n = roots.size();
		boolean even = n % 2 == 0;
		List<Complex> polynom = new ArrayList<>(n + 1);
		List<List<Complex>> subsets = rootsSubsets();
		
		for(int i = 0; i < n; i++) {
			Complex factor = Complex.ZERO;
			
			for(List<Complex> set : subsets) {
				if(set.size() == n - i) {
					Complex product = constant;
					for(Complex c : set)
						product = product.multiply(c);
					factor = factor.add(product);
				}
			}
			
			factor.multiply(even ? (i % 2 == 0 ? Complex.ONE : Complex.ONE_NEG)
					: (i % 2 == 0 ? Complex.ONE_NEG : Complex.ONE));
			
			polynom.add(factor);
		}
		
		polynom.add(constant);
		
		return new ComplexPolynomial(polynom.stream().toArray(Complex[]::new));
	}

	/**
	 * Traži index nultočke najbliže poslanom kompleksnom broju unutar zadane vrijednosti.
	 * 
	 * @param z kompleksni broj za kojega se traži najbliža nultočka
	 * @param treshold zadana vrijednost unutar koje treba biti udaljenost između poslanog broja i najbliže nultočke
	 * @return index nultočke najbliže poslanom kompleksnom broju, odnosno -1 ako je udaljenost veća od zadane vrijednosti
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		int closestRoot = 0;
		double minDistance = roots.get(0).sub(z).module();
		
		for(int i = 1; i < roots.size(); i++) {
			double distance = roots.get(i).sub(z).module();
			if(distance < minDistance) {
				minDistance = distance;
				closestRoot = i;
			}
		}
		
		if(minDistance > treshold)
			closestRoot = -1;
		
		return closestRoot;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("(").append(constant).append(")");
		for(Complex c : roots) {
			sb.append(" * (z-(");
			sb.append(c);
			sb.append("))");
		}
		
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ComplexRootedPolynomial))
			return false;
		ComplexRootedPolynomial other = (ComplexRootedPolynomial) obj;
		
		if(this.roots.size() != other.roots.size())
			return false;
		
		if(!this.constant.equals(other.constant))
			return false;
		
		for(int i = 0; i < this.roots.size(); i++) {
			if(!this.roots.get(i).equals(other.roots.get(i)))
				return false;
		}
		return true;
	}
	
	/**
	 * Stvara listu svih podskupova nultočaka.
	 * 
	 * @return lista svih podskupova nultočaka
	 */
	private List<List<Complex>> rootsSubsets() {
		List<List<Complex>> subsets = new LinkedList<>();
		
		for(int i = 0; i < (1 << roots.size()); i++) {
			List<Complex> set = new LinkedList<>();
			
			for(int j = 0; j < roots.size(); j++) {
				if((i & (1 << j)) > 0)
					set.add(roots.get(j));
			}
			
			subsets.add(set);
		}
		
		return subsets;
	}
	
}
