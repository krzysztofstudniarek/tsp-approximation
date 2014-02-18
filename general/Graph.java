package general;

import graphGenerator.City;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

/**
 * @author Krzysiek
 * 
 */
public class Graph {

	private double[][] graph;
	Random rand = new Random();

	private int numberOfGeneratedPerm;

	private Vector<City> nodes = new Vector<City>();

	private Vector<Permutation> permutations;

	private int size;

	private int startingGenerationSize;

	File f;
	BufferedWriter writer;
	private BufferedReader r;

	/**
	 * @param size
	 *            size of the graph
	 * @param startingGenerationSize
	 *            initial generation size for GA and 2-Opt
	 * @param grid
	 *            size of the space that randomized graph could be generated
	 * @param genMode
	 *            generation mode 0-randomized graph, 1-real, 2-dsj1000, 3- data
	 *            graph
	 * @throws IOException
	 */
	public Graph(int size, int startingGenerationSize, int grid, int genMode)
			throws IOException {

		File f = new File("generate.report");

		if (!f.exists()) {
			f.createNewFile();
		}

		this.startingGenerationSize = startingGenerationSize;

		writer = new BufferedWriter(new FileWriter(f));

		Date d = new Date();

		writer.write("----------TSP TESTING PROGRAM START----------\n"
				+ "Date: " + d.toString() + "\n");
		writer.append("----------INPUT DATA---------- \n");

		if (genMode == 2) {
			this.size = 1000;
		} else {
			this.size = size;
		}

		long timestamp = System.currentTimeMillis();

		graph = new double[size][size];

		permutations = new Vector<Permutation>();

		int i = 0, j = 0;
		City c;

		switch (genMode) {

		case 0:
			for (i = 0; i < size; i++) {
				c = new City();
				c.lat = (double) rand.nextInt(grid);
				c.lon = (double) rand.nextInt(grid);
				c.name = i + "";

				nodes.add(c);
			}

			for (i = 0; i < size; i++) {
				for (j = i; j < size; j++) {
					graph[i][j] = nodes.get(i).getDistanceFrom(nodes.get(j));
					graph[j][i] = graph[i][j];
				}
			}

			break;

		case 1:

			r = new BufferedReader(new FileReader("100UsaCities.matrix"));

			String s = r.readLine();
			String[] cit = s.split(";");
			String[] dist;
			City city;
			for (int k = 0; k < size; k++) {
				city = new City();
				city.lat = 0;
				city.lon = 0;
				city.name = cit[k];
				city.state = false;
				nodes.add(city);
			}

			for (int k = 0; k < size; k++) {
				s = r.readLine();
				dist = s.split(";");

				for (int l = 0; l < size; l++) {
					graph[k][l] = Double.parseDouble(dist[l]);

				}
			}

			break;

		case 2:
			r = new BufferedReader(new FileReader("dsj1000.tsp"));
			String line = r.readLine();
			City ci;
			String[] citData;
			while (line != null) {

				citData = line.split(",");
				ci = new City();
				ci.lat = Double.parseDouble(citData[2]);
				ci.lon = Double.parseDouble(citData[3]);
				ci.name = citData[1];
				ci.state = false;
				nodes.add(ci);

				line = r.readLine();
			}

			for (i = 0; i < size; i++) {
				for (j = i; j < size; j++) {
					graph[i][j] = nodes.get(i).getDistanceFrom(nodes.get(j));
					graph[j][i] = graph[i][j];
				}
			}

			break;

		}

		i = 0;

		while (i < startingGenerationSize) {
			Permutation perm = generatePermutation();
			if (isPermutationUnique(perm)) {
				permutations.add(perm);
				i++;
			}
		}

		System.out.print("Program succesfully generated input data in "
				+ (float) (System.currentTimeMillis() - timestamp) / 1000
				+ " seconds.\nCheck generate.report file to see the details.");

		this.draw(true, true, true, true);
		writer.close();

	}

	/**
	 * Writing statistics to file "generate.report"
	 * 
	 * @param nod
	 *            switch for writing nodes
	 * @param matrix
	 *            switch for writing distance matrix
	 * @param cycles
	 *            switch for writing generated cycles
	 * @param stats
	 *            swithc for writing stats
	 * @return
	 * @throws IOException
	 */
	public String draw(boolean nod, boolean matrix, boolean cycles,
			boolean stats) throws IOException {

		int i = 0, j = 0;

		String s = "";
		if (nod) {

			writer.append("----------Randomized Nodes---------\n\n");
			for (i = 0; i < nodes.size(); i++) {

				writer.append("Node " + i + " - x : " + nodes.get(i).lat
						+ " y : " + nodes.get(i).lon + "\n");
			}
		}

		if (matrix) {

			writer.append("\n----------Distance Matrix---------\n\n");
			for (i = 0; i < nodes.size(); i++) {
				for (j = 0; j < nodes.size(); j++) {

					writer.append(graph[i][j] + " ");
				}

				writer.append("\n");
			}
		}

		if (cycles) {

			writer.append("\n----------Hamilton Cycles Generation Result---------\n\n");

			for (i = 0; i < permutations.size(); i++) {

				writer.append(permutations.get(i).draw());

				writer.append("STRENGTH: "
						+ offspringStrength(permutations.get(i)
								.getPermutation())
						+ "\n------------------------------\n");
			}
		}

		if (stats) {

			s += "\n----------Stats---------\n\n";
			writer.append("\n----------Stats---------\n\n");
			s += "perm num: " + permutations.size() + "   gen num: "
					+ numberOfGeneratedPerm + "\n";
			writer.append("perm num: " + permutations.size() + "   gen num: "
					+ numberOfGeneratedPerm + "\n");
		}

		return s;

	}

	/**
	 * Generates one permutation
	 * 
	 * @return generated permutation
	 */
	private Permutation generatePermutation() {

		int[] result = new int[nodes.size()];

		numberOfGeneratedPerm++;

		for (int i = 0; i < nodes.size(); i++) {
			nodes.get(i).state = false;
		}

		int counter = 1;

		int rn = 0;

		nodes.get(rn).state = true;
		result[0] = 0;

		while (counter < nodes.size()) {

			rn = rand.nextInt(nodes.size());

			if (!nodes.get(rn).state) {
				result[counter] = rn;
				nodes.get(rn).state = true;
				counter++;
			}
		}

		Permutation perm = new Permutation(result);
		perm.setStrength(offspringStrength(result));

		return perm;
	}

	/**
	 * Checks wether permutation was already generated or not
	 * 
	 * @param perm
	 *            Permutation that should be checked
	 * @return
	 */
	private boolean isPermutationUnique(Permutation perm) {

		boolean tmp = true;

		int[] reverted = new int[perm.getPermutation().length];

		for (int i = 0; i < perm.getPermutation().length; i++) {
			reverted[perm.getPermutation()[i]] = i;
		}

		for (int i = 0; i < permutations.size(); i++) {
			Permutation tmpp = permutations.get(i);

			if (Arrays.equals(perm.getPermutation(), tmpp.getPermutation())
					|| Arrays.equals(reverted, tmpp.getPermutation())) {
				tmp = false;
				break;
			}

		}

		return tmp;
	}

	/**
	 * Calculates strength of hamilton course
	 * 
	 * @param offspring
	 *            Hamilton cycle that should be considered
	 * @return
	 */
	private double offspringStrength(int[] offspring) {
		double res = graph[offspring[0]][offspring[1]];

		for (int i = 1; i < offspring.length - 1; i++) {
			res += graph[offspring[i]][offspring[i + 1]];
		}

		res += graph[offspring[offspring.length - 1]][0];

		return res;
	}

	@SuppressWarnings("unchecked")
	public Vector<Permutation> getPermutations() {

		Vector<Permutation> p;

		p = (Vector<Permutation>) this.permutations.clone();

		return p;
	}

	/**
	 * Generate new set of permutations
	 * 
	 * @param generationSize 	size of the set of permutations
	 * @return					generated set of permutations
	 */
	public Vector<Permutation> generateNewPermutations(int generationSize) {
		Vector<Permutation> p = new Vector<Permutation>();

		int i = 0;
		this.startingGenerationSize = generationSize;

		while (i < startingGenerationSize) {
			Permutation perm = generatePermutation();
			if (isPermutationUnique(perm)) {
				p.add(perm);
				i++;
			}
		}

		return p;
	}

	public double[][] getGraph() {
		return this.graph;
	}

	public int getSize() {
		return this.size;
	}

	public Vector<City> getCities() {
		return this.nodes;
	}

}
