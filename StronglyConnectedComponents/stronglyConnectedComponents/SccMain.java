package stronglyConnectedComponents;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class SccMain {
	
	static Scanner input;
	static Hashtable<Integer, List<Integer>> graph;
	static Scc solver;
	static int min;
	static int max;

	/**
	 * Solves the 5 largest strongly connected components in a directed graph represented by Integers
	 * @param args 0 - File with each row representing an adjacency list with first Integer the vertex
	 * 			   1 - minimum vertex
	 * 			   2 - maximum vertex
	 */
	public static void main(String[] args) {
		String fileName = args[0];
		graph = new Hashtable<Integer, List<Integer>>();
		min = Integer.parseInt(args[1]);
		max =  Integer.parseInt(args[2]);
		loadGraph(fileName);
		solver = new Scc(graph, min, max);
		solver.solveAlternate();
	}
	
	private static void loadGraph(String fileName) {
		try {
			input = new Scanner(new File(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while (input.hasNextLine()) {
			String ln = input.nextLine();
			String[] integers = ln.split("\\s+");
			Integer vertex = Integer.parseInt(integers[0]);
			if (vertex < min)
				min = vertex;
			if (vertex > max)
				max = vertex;
			if (!graph.containsKey(vertex))
				graph.put(vertex, new ArrayList<Integer>());
			for (int i = 1; i < integers.length; i++) {
				Integer edge = Integer.parseInt(integers[i]);
				graph.get(vertex).add(edge);
				if (!graph.containsKey(edge)) {
					graph.put(edge, new ArrayList<Integer>()); //ensures that vertices with no outgoing edges are included
				}
			}
		}
	}
}
