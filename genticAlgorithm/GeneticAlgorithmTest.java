package genticAlgorithm;

import Chart.Chart;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;
import general.Permutation;
import general.PermutationComparator;
import java.util.Collections;

/**
 * @author Krzysiek
 * 
 */
public class GeneticAlgorithmTest {

	Vector<Permutation> permutations;
	double[][] graph;
	Random rand = new Random();
	PermutationComparator c = new PermutationComparator();

	File f;

	int initialGenerationSize;
	Chart chart = new Chart("Genetic Algorithm Results");

	long count = 0;

	/**
	 * @param graph
	 *            Graph that would be considered
	 * @param perm
	 *            Initial generation for genetic algorithm
	 * @throws IOException
	 */
	public GeneticAlgorithmTest(double[][] graph, Vector<Permutation> perm)
			throws IOException {

		Collections.sort(perm, new PermutationComparator());

		this.permutations = perm;
		Collections.sort(permutations, new PermutationComparator());
		this.graph = graph;
		this.initialGenerationSize = permutations.size();

		File f = new File("geneticAlgorithm.report");

		if (!f.exists()) {
			f.createNewFile();
		}

	}

	/**
	 * Runs Genetic Algorithm
	 * 
	 * @param numOfNodes
	 *            number of nodes in one generation
	 * @param crossOverPossibility
	 *            possibility of crossing over
	 * @param mutationPossibility
	 *            possibility of mutation
	 * @param numOfPossMistakes
	 *            number of possible mistakes
	 * @param trace
	 *            swich that decides if steps should be displayed or not
	 * @param graph
	 *            graph that would be considered
	 * @return best best hamilton course that could be reached by this algorithm
	 * @throws IOException
	 */
	public Permutation runAlgorithm(int numOfNodes, float crossOverPossibility,
			float mutationPossibility, int numOfPossMistakes, boolean trace,
			boolean graph) throws IOException {

		Vector<Permutation> nextGeneration;

		int numOfIterations = 1;
		Permutation best = Collections.min(this.permutations, c);

		@SuppressWarnings("unused")
		int indeks = 0, num = 0;

		while (true) {
			nextGeneration = new Vector<Permutation>(); // CLEAR NEXT GENERATION

			Collections.sort(this.permutations, c);

			this.permutations = tournamentSelection(initialGenerationSize, 10);

			// CROSSING OVER
			for (int i = 0; i < permutations.size(); i++) {

				int startPoint = rand.nextInt(numOfNodes);
				int endPoint = rand.nextInt(numOfNodes);
				int parentA = rand.nextInt(permutations.size());
				int parentB = rand.nextInt(permutations.size());

				nextGeneration
						.add(crossOver(permutations.get(parentA),
								permutations.get(parentB), startPoint,
								endPoint, trace));
				nextGeneration
						.add(crossOver(permutations.get(parentB),
								permutations.get(parentA), startPoint,
								endPoint, trace));
			}

			// MUTATION

			for (int i = 0; i < nextGeneration.size(); i++) {
				if (rand.nextFloat() < mutationPossibility) {
					nextGeneration.set(
							i,
							invertMutation(nextGeneration.get(i),
									rand.nextInt(numOfNodes),
									rand.nextInt(numOfNodes), trace));
				}
			}

			// EVALUATION
			if (Collections.min(nextGeneration, c).getStrength() < best
					.getStrength()) {

				indeks = 0;
				best = Collections.min(nextGeneration, c);
				this.permutations = nextGeneration;

				// SELECTION
				num++;

			} else {

				indeks++;
			}

			if (indeks > numOfPossMistakes) {

				break;
			}

			this.chart.addTuple(numOfIterations, (int) best.getStrength(), 0);
			numOfIterations++;

		}

		if (graph) {
			this.chart.displayChart();
		}

		System.out.print("\n" + count + "\n" + numOfIterations + "\n");

		return best;

	}

	/**
	 * 
	 * Invertion mutation
	 * 
	 * @param permutation
	 *            Permitation taht should be mutated
	 * @param start
	 *            starting point of mutation
	 * @param end
	 *            ending point of mutation
	 * @param trace
	 *            switch if mutation should be traced or not
	 * @return permutation permutation after mutation
	 * @throws IOException
	 */
	private Permutation invertMutation(Permutation permutation, int start,
			int end, boolean trace) throws IOException {

		int[] perm = permutation.getPermutation().clone();

		while (start < end) {

			int tmp = perm[start];
			perm[start] = perm[end];
			perm[end] = tmp;

			start++;
			end--;
			count++;
		}

		Permutation tmpp = new Permutation(perm, offspringStrength(perm));

		return tmpp;

	}

	@SuppressWarnings("unused")
	private Permutation scrambleMutation(Permutation permutation, int range,
			boolean trace) throws IOException {

		int k1, k2, temp;
		int[] perm = permutation.getPermutation().clone();

		for (int i = 0; i < range; i++) {

			k1 = rand.nextInt(perm.length);
			k2 = rand.nextInt(perm.length);

			temp = perm[k1];
			perm[k1] = perm[k2];
			perm[k2] = temp;

		}

		Permutation p = new Permutation(perm, offspringStrength(perm));

		return p;
	}

	/**
	 * Calculates hamilton cycle strength
	 * 
	 * @param offspring		Hamilton course that should be considered	
	 * @return strength		strength of given hamilton course
	 */
	private double offspringStrength(int[] offspring) {

		double res = 0;

		for (int i = 0; i < offspring.length - 1; i++) {
			res += graph[offspring[i]][offspring[i + 1]];
		}

		res += graph[offspring[offspring.length - 1]][offspring[0]];

		return res;
	}

	/**
	 * Crossing over mechanism
	 * 
	 * @param parent1		first parent
	 * @param parent2		second parent
	 * @param s				starting point of cross over
	 * @param e				ending point of cross over
	 * @param trace			swicht that decides wether flow should be displayed or not
	 * @return permutation	offspring of two parents
	 * @throws IOException
	 */
	private Permutation crossOver(Permutation parent1, Permutation parent2,
			int s, int e, boolean trace) throws IOException {

		int[] child = new int[parent1.getPermutation().length];

		Vector<Integer> done = new Vector<Integer>();

		int start = Math.min(s, e), end = Math.max(s, e);

		// PRZEPISYWANIE Z RODZICA DO DZIECKA
		while (end >= start) {

			child[start] = parent1.getPermutation()[start];
			child[end] = parent1.getPermutation()[end];

			done.add(parent1.getPermutation()[start]);
			done.add(parent1.getPermutation()[end]);

			end--;
			start++;
			count++;
		}

		Permutation c = new Permutation(child);

		int counter = 0, i = 0;

		while (i < child.length) {
			if (!done
					.contains(parent2.getPermutation()[(Math.max(s, e) + i + 1)
							% child.length])) {
				child[(Math.max(s, e) + counter + 1) % child.length] = parent2
						.getPermutation()[(Math.max(s, e) + i + 1)
						% child.length];
				counter++;
				done.add(parent2.getPermutation()[(Math.max(s, e) + i + 1)
						% child.length]);

			}
			i++;
			count++;

		}

		c.setPermutation(child);
		c.setStrength(offspringStrength(child));

		return c;
	}

	@SuppressWarnings("unused")
	private Vector<Permutation> selection(int numbe) {
		System.out.print("Sa\n");

		Vector<Permutation> selected = new Vector<Permutation>();
		float[] probabilities = new float[permutations.size()];
		float sum = 0;

		float t = (float) permutations.get(permutations.size() - 1)
				.getStrength();

		int s = 0;

		int i;

		for (i = 0; i < permutations.size(); i++) {
			// System.out.print("a");
			sum += t - permutations.get(i).getStrength();
		}

		probabilities[0] = (float) ((t - permutations.get(0).getStrength()) / sum);

		// SUMOWANIE PRAWDOPODOBIENSTW W TABLICY
		for (i = 1; i < permutations.size(); i++) {

			probabilities[i] = (float) ((t - permutations.get(i).getStrength()) / sum);
		}

		System.out.print("Sc\n");

		for (i = 1; i < permutations.size(); i++) {

			probabilities[i] += probabilities[i - 1];
		}

		probabilities[probabilities.length - 1] = 1;

		float tmp = rand.nextFloat();

		System.out.print("Sd" + ":" + probabilities[0] + ":"
				+ probabilities[probabilities.length - 1] + ":" + tmp + "\n");

		while (selected.size() < numbe) {
			for (i = 0; i < probabilities.length; i++) {
				if (tmp <= (probabilities[i])) {
					System.out.print(tmp);
					selected.add(permutations.get(i));
					break;
				}
			}
			tmp = rand.nextFloat();
		}

		System.out.print("Se\n");

		s = 0;

		return selected;

	}

	/**
	 * Tournament selection operator
	 * 
	 * @param number 			size of generation after selection
	 * @param tournamentSize	size of the tournament
	 * @return permutations		generation after selection
	 */
	private Vector<Permutation> tournamentSelection(int number,
			int tournamentSize) {
		Vector<Permutation> selected = new Vector<Permutation>();
		Vector<Permutation> tournament;

		while (selected.size() < number) {

			tournament = new Vector<Permutation>();

			for (int i = 0; i < tournamentSize; i++) {
				tournament.add(permutations.get(rand.nextInt(permutations
						.size())));
				count++;
			}

			selected.add(Collections.min(tournament, c));

		}

		return selected;
	}
}
