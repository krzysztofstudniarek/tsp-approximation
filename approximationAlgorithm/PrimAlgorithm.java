package approximationAlgorithm;

import general.Graph;

/**
 * @author Krzysiek
 *
 */
public class PrimAlgorithm {
	double[][] weight;
	int visited[];
	double d[];
	int p[];
	int v, e;

	/**
	 * @param g 	Graph that should be considered
	 */
	public PrimAlgorithm(Graph g) {
		this.weight = g.getGraph();
		this.visited = new int[g.getSize()];
		// this.reachable = new int[g.getSize()][g.getSize()];
		this.p = new int[g.getSize()];
		this.d = new double[g.getSize()];
		this.v = g.getSize();
		this.e = g.getSize() * (g.getSize() - 1) / 2;
		for(int i=0; i<v; i++){
			d[i] = Integer.MAX_VALUE;
			p[i] = visited[i] = 0;
			//System.out.print(d[i] + "   ");
		}

		
	}

	/**
	 * Runs the algorithm
	 * 
	 * @return	returns the matrix of nodes that are taken to the MST
	 */
	public int[][] algorithm() {

		int current, total, i;
		double mincost = 0;
		current = 0;
		d[current] = 0;
		total = 1;
		visited[current] = 1;
		while (total != v) {
			for (i = 0; i < v; i++) {
				if (weight[current][i] != 0)
					if (visited[i] == 0)
						if (d[i] > weight[current][i]) {
							d[i] = weight[current][i];
							p[i] = current;
							//System.out.print("I'M TAKEN "+current+" : " +i+"\n");
						}
			}

			mincost = Integer.MAX_VALUE;
			
			for (i = 0; i < v; i++) {
				if (visited[i] != 1)
					if (d[i] < mincost) {
						mincost = d[i];
						current = i;
					}
			}
			visited[current] = 1;
			total++;
		}

		mincost = 0;

		for (i = 0; i < v; i++)
			mincost += d[i];

		//System.out.print("\n Minimum cost=" + mincost);
		//System.out.print("\n Minimum Spanning tree is");
		
		int result[][] = new int[p.length][p.length];

		for (i = 0; i < v; i++){
			//System.out.print("\n vertex" + i + "is connected to" + p[i]);
			result[i][p[i]] = 1;
			result[p[i]][i] = 1;
		}
		
		return result;
	}

}
