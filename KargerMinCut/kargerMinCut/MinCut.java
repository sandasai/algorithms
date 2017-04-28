package kargerMinCut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Minimum Cut problem to be solved using Karger's minimum cut
 * @author sandasai
 */
public class MinCut {
	
    private Map<Integer, List<Integer>> adjList; //Adjacency list of Integer vertices connected to other Integer vertices
	
	MinCut() {
		adjList = new HashMap<Integer, List<Integer>>();
	}
	
	/**
	 * Performs a single iteration of karger's minimum cut. Assumes that graph has been built.
	 * @return - The minimum cut returned from a single iteration.
	 */
	public int solve() {
		Random r = new Random();
		
		while (adjList.size() > 2) {
			List<Integer> keys = new ArrayList<Integer>(adjList.keySet());
		
			
			Integer fromVertex = keys.get(r.nextInt(keys.size())); 			//pick a random vertex
			List<Integer> fromEdges = adjList.get(fromVertex);				//edges - get the vertices that are connect to that vertex
			Integer toVertex = fromEdges.get(r.nextInt(fromEdges.size())); 	//pick a random vertex from that list
			List<Integer> toEdges = adjList.get(toVertex);					//store the edges of the other vertex
			addEdges(fromEdges, toEdges); 									//merge the vertices/remove edge by adding the edges from one list to another 
			removeAllInteger(fromEdges, fromVertex); 						//remove all self loops
			removeAllInteger(fromEdges, toVertex); 							//remove all self loops
			
			for (Integer key : adjList.keySet()) {
				List<Integer> listEdges = adjList.get(key);
				//remove all references to the removed toVertex, replace it with fromVertices
				while (listEdges.contains(toVertex) && !key.equals(fromVertex)) { 
					listEdges.remove(toVertex);
					listEdges.add(new Integer(fromVertex));
				}
			}
			adjList.remove(toVertex);
		}
		List<Integer> keys = new ArrayList<Integer>(adjList.keySet());
		if (keys.size() != 2) {
			throw new IllegalStateException("There should only be 2 onlys left");
		}
		int minCount = adjList.get(keys.get(0)).size();
		if (minCount != adjList.get(keys.get(1)).size())
			throw new IllegalStateException("Last two nodes should have edges of the same size");
		return minCount;
	}
	
	/**
	 * Prints the adjacency list with the first element on a line
	 * being the integer Node, and subsequent Integers being edges to other nodes
	 */
	public void printGraph() {
		List<Integer> keys = new ArrayList<Integer>(adjList.keySet());
		for (Integer key: keys) {
			System.out.println(key + " " + adjList.get(key));
		}
	}
	
	/**
	 * Adds all the Integers from a list to another list
	 * @param to - the list that will receive the elements.
	 * @param from - the list that contains the integers that will be copied over
	 */
	private void addEdges(List<Integer> to, List<Integer> from) {
		for (int i = 0; i < from.size(); i++) {
			to.add(new Integer(from.get(i)));
		}
	}
	
	/**
	 * Removes all the elements from the list that equals an integer
	 * @param list - The list
	 * @param number - The number to remove from the list
	 */
	private void removeAllInteger(List<Integer> list, Integer number) {
		while(list.remove(number)) {}
	}
	
	/**
	 * Rebuilds the graph given a list of Strings in the format "<integerVertex> <edgeToVertex> <edgeToVertex> <edgeToVertex>..."
	 * @param inputs - list of strings containing integers separated by whitespace.
	 */
	public void buildGraph(List<String> inputs) {
		for (String line: inputs) {
			String[] row = line.split("\\s+");
			List<Integer> adjacent = new ArrayList<Integer>();
			Integer intVertex = Integer.parseInt(row[0]);
			for (int i = 1; i < row.length; i++) {
				adjacent.add(Integer.parseInt(row[i]));
			}
			adjList.put(intVertex, adjacent);
		}
	}
}
