import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {
	static Map<Integer, List<Edge>> graph;
	static DijkstraSolver solver;

	/**
	 * @param args 0 - file integers with each row representing an adjacency list with weights
	 * 			   1+ - which vertices to compute shortest path to from vertex 1
	 */
	public static void main(String args[]) {
		File dijkstraData = new File(args[0]);
		graph = new Hashtable<Integer, List<Edge>>();
		try {
			@SuppressWarnings("resource")
			Scanner input = new Scanner(dijkstraData);
			while (input.hasNextLine()) {
				String line = input.nextLine();
				String[] lineArr = line.split("\\s+");
				
				Integer vertex;
				List<Edge> edges = new ArrayList<Edge>();
				
				vertex = Integer.parseInt(lineArr[0]);
				for (int i = 1; i < lineArr.length; i++) {
					String[] parsedEdge = lineArr[i].split(",");
					edges.add(new Edge(Integer.parseInt(parsedEdge[0]), Integer.parseInt(parsedEdge[1]), vertex));
				}
				graph.put(vertex, edges);
			}
			List<Integer> vertices = new ArrayList<Integer>();
			//Solve shortest paths for these vertices
			for (int i = 1; i < args.length; i++) {
				vertices.add(Integer.parseInt(args[i]));
			}
			solve(vertices);
		} catch(FileNotFoundException exception) {
			System.out.println(exception);
		}
	}
	
	public static void solve(List<Integer> vertices) {
		solver = new DijkstraSolver(graph);
		solver.solve();
		for (int i = 0; i < vertices.size(); i++) {
			System.out.println("Vertex: " + vertices.get(i) + " shortest path length: " + solver.getShortestPath(vertices.get(i)));
		}
	}
}
