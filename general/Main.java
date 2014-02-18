package general;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import approximationAlgorithm.ApproximationAlgorithmTest;
import Chart.TotalChart;
import TwoOptimalAlgorithm.TwoOptimalAlgorithmTest;
//import GUI.Application;
import genticAlgorithm.GeneticAlgorithmTest;


/**
 * @author Krzysiek
 *
 */
public class Main {

	private static Graph g = null;

	/**
	 * Generates graph
	 * 
	 * @throws IOException
	 */
	private static void generateGraph() throws IOException {

		Scanner scanner = new Scanner(System.in);
		
		System.out.println("\n--------------------\n"
				+ "First of all you need to choose graph. Chose: \n"
				+ "1 - randomized graph\n"
				+ "2 - real data graph (up to 100 american Cities)\n"
				+ "3 - dsj1000 graph\n");
		System.out.print("Your choice:\n");
		int c = scanner.nextInt();

		switch (c) {
		case 1:
			System.out.print("\n--------------------\n"
					+ "Now choose numberOfNodes\n"
					+ "Number of nodes in a graph: ");
			g = new Graph(scanner.nextInt(), 0, 1000, 0);
			break;

		case 2:
			System.out.print("\n--------------------\n"
					+ "Now choose numberOfNodes (no more than 100)\n"
					+ "Number of nodes in a graph: ");
			int num;
			if ((num = scanner.nextInt()) <= 100) {
				g = new Graph(num, 0, 1000, 1);
			} else {
				System.out.print("Wrong Input!!!");
				System.exit(-1);
			}
			break;

		case 3:
			g = new Graph(1000, 0, 1000, 2);
		}

	}

	/**
	 * Runs 2-Opt Algorithm
	 * 
	 * @return 				returns best result after 2-Opt run
	 * @throws IOException
	 */
	private static Permutation run2OptAlgorithm() throws IOException {

		TwoOptimalAlgorithmTest twoOptimalTest = new TwoOptimalAlgorithmTest(
				g.getGraph(), g.generateNewPermutations(1).get(0));

		return twoOptimalTest.runAlgorithm(false);

	}

	/**
	 * Runs Genetic Algorithm
	 * 
	 * @param numOfPerms	number of permutations in one cycle
	 * @return				best result after GA run
	 * @throws IOException
	 */
	private static Permutation runGeneticAlgorithm(int numOfPerms)
			throws IOException {

		GeneticAlgorithmTest geneticTest = new GeneticAlgorithmTest(
				g.getGraph(), g.generateNewPermutations(numOfPerms));

		return geneticTest
				.runAlgorithm(g.getSize(), 1f, 0.7f, 100, false, false);

	}

	/**
	 * Run Approximation Algorithm
	 * 
	 * @return		best result after Approximation Algorithm run
	 */
	private static Permutation runApproximationAlgorithm() {

		ApproximationAlgorithmTest appTest = new ApproximationAlgorithmTest(g);

		return appTest.runAlgorithm();

	}

	public static void main(String[] args) {

		try {

			int ind = 0;

			File f = new File("output.results");
			if (!f.exists()) {
				f.createNewFile();
			}

			BufferedWriter w = new BufferedWriter(new FileWriter(f));

			Scanner scanner = new Scanner(System.in);

			while (true) {

				ind++;

				System.out
						.print("Program made by Krzysztof Studniarek to compare \n2-Opt, Genetic and Approximation algorithms for TSP");
				System.out.print("\n\n--------------------------------\n\n");
				System.out
						.print("Choose: \n"
								+ "1 - run 2-Opt Algorithm \n"
								+ "2 - run Genetic Algorithm \n"
								+ "3 - run Approximation Algorithm\n"
								+ "12 - run 2-Opt and Genetic Algorithms\n"
								+ "13 - run 2-Opt and Approximation Algorithms\n"
								+ "23 - run Genetic and Approximation Algorithms\n"
								+ "123 - run 2-Opt, Genetic and Approximation Algorithms");

				System.out.print("What is your choice ?\n");
				int choice = scanner.nextInt();

				System.out
						.print("How many times you want to proceed this algorithm/s?\n");
				int times = scanner.nextInt();
				
				TotalChart chart = new TotalChart("Comparision Chart");

				Permutation p1, p2, p3;

				generateGraph();

				int numOfPerms = 1;

				w.append("\n\n\n---------- EXECUTION NO: " + ind
						+ "----------\n\n\n");

				if (choice == 2 || choice == 12 || choice == 123
						|| choice == 23) {
					System.out
							.print("\n--------------------\n"
									+ "How many permutations should be in one generation of Genetic Algorithm?");

					numOfPerms = scanner.nextInt();
				}

				long timeInMills;

				int i = 0;
				while (i < times) {

					switch (choice) {
					case 1:

						timeInMills = System.currentTimeMillis();

						p1 = run2OptAlgorithm();

						w.append("2-Opt Algorithm result: " + p1.getStrength()
								+ "\nMade in: "
								+ (System.currentTimeMillis() - timeInMills)
								+ "ms.\n");

						chart.addTuple(i, p1.getStrength(), 0, 0);

						break;

					case 2:

						timeInMills = System.currentTimeMillis();

						p2 = runGeneticAlgorithm(numOfPerms);

						w.append("Genetic Algorithm result: "
								+ p2.getStrength() + "\nMade in: "
								+ (System.currentTimeMillis() - timeInMills)
								+ "ms.\n");

						chart.addTuple(i, 0, p2.getStrength(), 0);

						break;

					case 3:

						timeInMills = System.currentTimeMillis();

						p3 = runApproximationAlgorithm();

						w.append("Approximation Algorithm result: "
								+ p3.getStrength() + "\nMade in: "
								+ (System.currentTimeMillis() - timeInMills)
								+ "ms.\n");

						chart.addTuple(i, 0, 0, p3.getStrength());

						break;

					case 12:

						timeInMills = System.currentTimeMillis();

						p1 = run2OptAlgorithm();

						w.append("2-Opt Algorithm result: " + p1.getStrength()
								+ "\nMade in: "
								+ (System.currentTimeMillis() - timeInMills)
								+ "ms.\n");

						timeInMills = System.currentTimeMillis();

						p2 = runGeneticAlgorithm(numOfPerms);

						w.append("Genetic Algorithm result: "
								+ p2.getStrength() + "\nMade in: "
								+ (System.currentTimeMillis() - timeInMills)
								+ "ms.\n");

						chart.addTuple(i, p1.getStrength(), p2.getStrength(), 0);

						break;

					case 13:

						timeInMills = System.currentTimeMillis();

						p1 = run2OptAlgorithm();

						w.append("2-Opt Algorithm result: " + p1.getStrength()
								+ "\nMade in: "
								+ (System.currentTimeMillis() - timeInMills)
								+ "ms.\n");

						timeInMills = System.currentTimeMillis();

						p3 = runApproximationAlgorithm();

						w.append("Approximation Algorithm result: "
								+ p3.getStrength() + "\nMade in: "
								+ (System.currentTimeMillis() - timeInMills)
								+ "ms.\n");

						chart.addTuple(i, p1.getStrength(), 0, p3.getStrength());

						break;

					case 23:

						timeInMills = System.currentTimeMillis();

						p2 = runGeneticAlgorithm(numOfPerms);

						w.append("Genetic Algorithm result: "
								+ p2.getStrength() + "\nMade in: "
								+ (System.currentTimeMillis() - timeInMills)
								+ "ms.\n");

						timeInMills = System.currentTimeMillis();

						p3 = runApproximationAlgorithm();

						w.append("Approximation Algorithm result: "
								+ p3.getStrength() + "\nMade in: "
								+ (System.currentTimeMillis() - timeInMills)
								+ "ms.\n");

						chart.addTuple(i, 0, p2.getStrength(), p3.getStrength());

						break;

					case 123:

						timeInMills = System.currentTimeMillis();

						p1 = run2OptAlgorithm();

						w.append("2-Opt Algorithm result: " + p1.getStrength()
								+ "\nMade in: "
								+ (System.currentTimeMillis() - timeInMills)
								+ "ms.\n");

						timeInMills = System.currentTimeMillis();

						p2 = runGeneticAlgorithm(numOfPerms);

						w.append("Genetic Algorithm result: "
								+ p2.getStrength() + "\nMade in: "
								+ (System.currentTimeMillis() - timeInMills)
								+ "ms.\n");

						timeInMills = System.currentTimeMillis();

						p3 = runApproximationAlgorithm();

						w.append("Approximation Algorithm result: "
								+ p3.getStrength() + "\nMade in: "
								+ (System.currentTimeMillis() - timeInMills)
								+ "ms.\n");

						chart.addTuple(i, p1.getStrength(), p2.getStrength(),
								p3.getStrength());

						break;

					default:
						break;
					}

					i++;
				}

				chart.displayChart();

				System.out.print("\n\n-----------------------------\n\n");

				System.out
						.print("Program finished it's work, check results.output file\nto check results and execution times");

				System.out.print("\n\n-----------------------------\n\n");
				System.out
						.print("Type 1 to close, or 0 to start from scratch :)\n");

				if (scanner.nextInt() == 1) {
					w.close();
					System.exit(0);
				}

			}
		} catch (IOException e) {
			System.out.print(e.getMessage());
		}

	}
}
