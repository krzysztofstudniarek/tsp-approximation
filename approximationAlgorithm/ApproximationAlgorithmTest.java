/**
 * 
 */
package approximationAlgorithm;

import general.Graph;
import general.Permutation;

/**
 * @author Krzysiek
 * 
 */
public class ApproximationAlgorithmTest {

	private Graph g;
	private int[] state;
	private int[] partial;
	int ind = 0;
	@SuppressWarnings("unused")
	private String s;

	/**
	 * @param g		Graph taken to consideration
	 */
	public ApproximationAlgorithmTest(Graph g) {
		this.g = g;
		this.state = new int[g.getSize()];
		for(int i =0; i<state.length; i++){
			state[i] = 0;
		}
		this.partial = new int[g.getSize()];
	}

	/**	
	 * Depth First search algorithm run
	 * 
	 * @param graph	graph taken to consideration
	 * @param vertex	actual vertex
	 */
	private void dfs(int[][] graph, int vertex) {
		state[vertex] = 1;
		partial[ind] = vertex; 
		//System.out.print(vertex+"->");
		ind++;
        for (int v = 0; v < state.length; v++)
              if (graph[vertex][v] == 1 && state[v] == 0)
                    dfs(graph ,v);
        state[vertex] = 2;
        //partial.add(vertex);
	}

	
	
	/**
	 * Run Approximation Algorithm
	 * 
	 * @return		returns best Permutation reached by this algorithm
	 */
	public Permutation runAlgorithm() {
		PrimAlgorithm p = new PrimAlgorithm(g);
		int[][] newGraph = p.algorithm();

		int[] visited = new int[g.getSize()];

		for (int i = 0; i < visited.length; i++) {
			visited[i] = 0;
		}

		this.dfs(newGraph, 0);

		Permutation pe = new Permutation(partial);
		pe.setStrength(offspringStrength(partial));
		
		//System.out.print("\n\n" + pe.getStrength() + "\n\n");
		
		return pe;
	}
	
	/**
	 * Calculates strength of the given Permutation
	 * 
	 * @param offspring		Permutation that should have the strength calculated
	 * @return				strength
	 */
	private double offspringStrength(int[] offspring) {

		double res = 0;

		for (int i = 0; i < offspring.length - 1; i++) {
			res += g.getGraph()[offspring[i]][offspring[i + 1]];
		}

		res += g.getGraph()[offspring[offspring.length - 1]][offspring[0]];
		
		return res;
	}

}
