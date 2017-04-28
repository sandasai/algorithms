package stronglyConnectedComponents;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Strongly connected component problem. Solved using DFS, Kosaraju's two-pass algorithm
 * Two different ways are implemented to solve it - using iteration or recursive when traversing nodes
 * @author sandasai
 *
 */
public class Scc {
	
	Hashtable<Integer, List<Integer>> graph;
	List<Integer> sizes; 	   	//end result for all the s
	HashSet<Integer> explored; 	//A hashset to keep track of which vertices have been visited
	Stack<Integer> worklist;	
	Stack<Integer> workstack;
	List<Integer> stackOrdering;
	List<Integer> ordering; 	//ordered by shortest running time --> longest running time for dfs
	int min;
	int max;
	
	/**
	 * Creates a new problem for solving strongly connected components with a given graph and known minimum and maximum vertex values
	 * @param graph - adjacency list
	 * @param min - min vertex value
	 * @param max - max vertex value
	 */
	Scc(Hashtable<Integer, List<Integer>> graph, int min, int max) {
		this.graph = graph;
		sizes = new ArrayList<Integer>();
		explored = new HashSet<Integer>();
		worklist = new Stack<Integer>();
		//alt
		ordering = new LinkedList<Integer>();
		this.min = min;
		this.max = max;
	}
	
	//stack overflow implementation. Overflow is possible if given a large graph due to recursive calls
	private void dfs(Hashtable<Integer, List<Integer>> graph, Integer vertex, List<Integer> list) {
		explored.add(vertex);
		List<Integer> edges = graph.get(vertex);
		//add edges to the stack
		for (int i = 0; i < edges.size(); i++) {
			if (!explored.contains(edges.get(i))) {
				dfs(graph, edges.get(i), list);
			}
		}
		list.add(vertex);
	}
	
	//iterative stack implementation
	private void dfsAlt(Map<Integer, List<Integer>> graph, List<Integer> ordering) {
		boolean unexploredNeighbors;
		while (!workstack.isEmpty()) {
			unexploredNeighbors = false;
			Integer top = workstack.peek();
			List<Integer> edges = graph.get(top);
			
			for (Integer edge : edges) {
				if (!explored.contains(edge)) { //there is an unexplored neighbor
					unexploredNeighbors = true;
					explored.add(edge);
					workstack.push(edge);
					break;
				}
			} 
			if (unexploredNeighbors)
				continue;
			workstack.pop();
			ordering.add(top);
		}
	}

	/**
	 * Solves strongly connected component problem using an iterative approach when traversing nodes
	 * Outputs the 5 largest SCCs to Standard output
	 */
	public void solveAlternate() {		
		explored = new HashSet<Integer>();
		workstack = new Stack<Integer>(); 
		ordering = new ArrayList<Integer>();
		for (int i = max; i >= min; i--) {
			//check if it has been explored
			if (explored.contains(i))
				continue;
			workstack.push(i);
			explored.add(i);
			dfsAlt(graph, ordering);
		}
		//reverse the graph
		Hashtable<Integer, List<Integer>> reversedGraph = reverseGraph(graph);
		ListIterator<Integer> itr = ordering.listIterator(ordering.size());	
		
		//reset explored and workstacks
		workstack = new Stack<Integer>();
		explored = new HashSet<Integer>(); 
		
		//scc is the ordering of nodes in an scc. Gets cleared when we exit out of dfs
		List<Integer> scc = new ArrayList<Integer>();

		LinkedList<Integer> sizeTracker = new LinkedList<Integer>();
		for (int j = 0; j < 5; j++) {
			sizeTracker.add(0);
		}

		while(itr.hasPrevious()) {
			Integer vertex = itr.previous();
			if (explored.contains(vertex)) {
				continue;
			}
			workstack.push(vertex);
			explored.add(vertex);
			dfsAlt(reversedGraph, scc);
			//compare to sizes of top 5
			for (int i = 0; i < 5; i++) {
				if (scc.size() > sizeTracker.get(i)) {
					sizeTracker.add(i, new Integer(scc.size()));
					sizeTracker.remove(5);
					break;
				}
			}
			scc = new ArrayList<Integer>();
		}
		for (int i = 0; i < 5; i++) {
			System.out.println(sizeTracker.get(i));
		}	
	}
	
	/**
	 * Solves strongly connected component problem using a recursive approach when traversing nodes
	 * Outputs the 5 largest SCCs to Standard output
	 */
	public void solve() {
		//iterate through vertices. Arbitrarily choose the max index to start from.
		for (int i = max; i >= min; i--) {
			//check if it has been explored
			if (explored.contains(i))
				continue;
			dfs(graph, i, ordering);
		}
		
		Hashtable<Integer, List<Integer>> reversedGraph = reverseGraph(graph);
		ListIterator<Integer> itr = ordering.listIterator(ordering.size());		
		explored = new HashSet<Integer>(); 
		List<Integer> scc = new ArrayList<Integer>();
		while(itr.hasPrevious()) {
			Integer vertex = itr.previous();
			if (explored.contains(vertex)) {
				scc = new ArrayList<Integer>();
				continue;
			}
			dfs(reversedGraph, vertex, scc);
			System.out.println(scc);
		}
	}
	
	void printGraph(Map<Integer, List<Integer>> graph) {
		Set<Integer> keys = graph.keySet();
		for (Integer key : keys) {
			System.out.println("vertex: " + key + " - edges: " + graph.get(key));
		}
	}
	
	private Hashtable<Integer, List<Integer>> reverseGraph(Hashtable<Integer, List<Integer>> graph) {
		Hashtable<Integer, List<Integer>> reversedGraph = new Hashtable<Integer, List<Integer>>();
		Set<Integer> keys = graph.keySet();
		for (Integer key : keys) {
			if (!reversedGraph.containsKey(key)) { //make sure key is in new graph
				reversedGraph.put(key, new LinkedList<Integer>());
			}
			for (Integer edge : graph.get(key)) {
				if (!reversedGraph.containsKey(edge))
					reversedGraph.put(edge, new LinkedList<Integer>());
				reversedGraph.get(edge).add(key);
			}
		}
		return reversedGraph;
	}
}
