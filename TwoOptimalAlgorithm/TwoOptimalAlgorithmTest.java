package TwoOptimalAlgorithm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import Chart.Chart;

import general.Permutation;

/**
 * @author Krzysiek
 *
 */
public class TwoOptimalAlgorithmTest {

	private Permutation hamiltonCycle;
	private double[][] graph;
	Random rand = new Random();

	Chart c = new Chart("2-Opt Algorithm Results");

	File f;
	BufferedWriter writer;

	int numOfGens = 0;

	long counter = 0;

	public TwoOptimalAlgorithmTest(double[][] graph, Permutation perm)
			throws IOException {

		hamiltonCycle = perm;
		this.graph = graph;

	}

	/**
	 * 
	 * Runs 2-Opt Algorithm
	 * 
	 * @param displayChart	switch that decides wether chart should be displayed or not
	 * @return bestResult	best hamilton route that was reached by algorithm
	 * @throws IOException
	 */
	public Permutation runAlgorithm(boolean displayChart) throws IOException {
		Permutation tmp = hamiltonCycle;
		Permutation result;
		Permutation bestResult = tmp;
		Permutation lastBestResult = tmp;

		while (true) {

			for (int i = 0; i < tmp.getPermutation().length; i++) {
				for (int j = i + 1; j < tmp.getPermutation().length; j++) {
					result = swap(tmp, i, j);

					counter++;

					if (result.getStrength() < bestResult.getStrength()) {

						bestResult = result;
					}

				}
			}

			if (lastBestResult.getStrength() <= bestResult.getStrength()) {

				break;
			} else {
				lastBestResult = bestResult;
				tmp = bestResult;
				c.addTuple(numOfGens, (int) tmp.getStrength(), 0);
				numOfGens++;
			}

		}

		
		
		if (displayChart)
			c.displayChart();
		
		return bestResult;
	}

	/**
	 * Swapping the part of permutation (Swapping Mechanism)
	 * 
	 * @param permutation
	 * @param start
	 * @param end
	 * @return
	 */
	private Permutation swap(Permutation permutation, int start, int end) {

		int[] perm = permutation.getPermutation().clone();

		while (start < end) {

			int tmp = perm[start];
			perm[start] = perm[end];
			perm[end] = tmp;

			start++;
			end--;

		}

		Permutation p = new Permutation(perm);
		p.setStrength(strength(perm));

		return p;

	}

	/**
	 * Calculates strength of the given permutation
	 * 
	 * @param offspring 	Hamilto cycle that should be considered
	 * @return				strength of given hamilton cycle
	 */
	private double strength(int[] offspring) {

		double res = 0;

		for (int i = 0; i < offspring.length - 1; i++) {
			res += graph[offspring[i]][offspring[i + 1]];
		}

		res += graph[offspring[offspring.length - 1]][offspring[0]];

		return res;
	}

}
