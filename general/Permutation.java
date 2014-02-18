package general;

import graphGenerator.City;

import java.util.Vector;

/**
 * @author Krzysiek
 *
 */
public class Permutation {

	private int[] permutation;
	private double strength;

	/**
	 * @param p		permuatation
	 */
	public Permutation(int[] p) {

		this.permutation = p;

	}

	/**
	 * @param p			permutation
	 * @param strength	strength of permutation
	 */
	public Permutation(int[] p, double strength) {

		this.permutation = p;
		this.strength = strength;

	}

	public int[] getPermutation() {

		return this.permutation;

	}

	/**
	 * draws permutation
	 * 
	 * @return	string with permutation drawed
	 */
	public String draw() {
			String k = "";

			for (int i = 0; i < permutation.length; i++) {
				k += " " + permutation[i] + " ";
			}

			return k + "\n";
		
	}
	
	/**
	 * Draws permutation by names
	 * 
	 * @param c names of the cities in permutation
	 * @return	string with permutation drawed
	 */
	public String draw(Vector<City> c) {
		String k = "";

		for (int i = 0; i < permutation.length; i++) {
			k += " " + c.get(permutation[i]).name + " ";
		}

		return k + "\n";
	
}

	public void setPermutation(int[] p) {
		this.permutation = p;
	}

	public void setStrength(double s) {
		this.strength = s;
	}

	public double getStrength() {
		return this.strength;
	}

}
