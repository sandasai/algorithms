import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * DijkstraSolver for vertices and weights represented by Integers
 * @author sandasai
 *
 */
public class DijkstraSolver {
	
	//shortest paths to vertices not yet explored {vertex:totalDistance}
	Map<Integer, Integer> shortestPaths;
	
	//edges that lead to a specific vertex;
	Map<Integer, List<Edge>> edgesToVertex;
	
	//Original graph
	Map<Integer, List<Edge>> graph;
		
	//shortest paths to vertices not yet explored
	DijkstraHeap<Integer, Integer> heap;
	
	DijkstraSolver(Map<Integer, List<Edge>> graph) {
		this.graph = graph;
		this.heap = new DijkstraHeap<Integer, Integer>();
		this.shortestPaths = new Hashtable<Integer, Integer>();
		this.edgesToVertex = new Hashtable<Integer, List<Edge>>();
	}
	
	/**
	 * Solve shortest paths to all vertices from vertex 1
	 */
	public void solve() {
		Integer lastVertex = 1;
		shortestPaths.put(lastVertex, 0);
		List<Edge> edges = graph.get(lastVertex);
		
		//add to heap
		for (Edge edge : edges) {
			heap.put(edge.l, edge.v);
			List<Edge> newEdgeList = new ArrayList<Edge>();
			newEdgeList.add(edge);
			edgesToVertex.put(edge.v, newEdgeList);
		}
		
		while(heap.size() > 0) {
			//get shortest path
			DijkstraHeap<Integer, Integer>.Data data = heap.removeMin();
			
			//record shortest distance to that vertex
			Integer nextVertex = data.vertex;
			shortestPaths.put(nextVertex, shortestPaths.get(lastVertex) + data.distance);
			
			//update shortest distances in the heap
			List<Edge> newEdges = graph.get(nextVertex);
			
			//for each newly discovered edge from the vertex that was just incorporated
			for (Edge newEdge : newEdges) {
				if (shortestPaths.get(newEdge.v) != null)
					continue;
				Integer endVertex = newEdge.v;
				List<Edge> existingEdgesToVertex;
				//if one of the new edges leads to a vertex that already has atleast one crossing edge
				if (edgesToVertex.containsKey(endVertex)) {
					existingEdgesToVertex = edgesToVertex.get(endVertex);
					existingEdgesToVertex.add(newEdge);
				} 
				//this is a newly undiscovered vertex, no mapping in edgesToVertex exists
				else {					
					//include the mapping between edges from nextVertex and the newly discovered vertex
					existingEdgesToVertex = new ArrayList<Edge>();
					existingEdgesToVertex.add(newEdge);
					edgesToVertex.put(endVertex, existingEdgesToVertex);
				}
				recalculateShortestPath(endVertex);
			}
		}
	}
	
	/**
	 * Returns the shortest Path from vertex 1 to vertex v 
	 * @param v
	 * @return the shortest Path from vertex 1 to vertex v
	 */
	public Integer getShortestPath(Integer v) {
		return shortestPaths.get(v);
	}
	
	/**
	 * Recalculate the shortest path to a vertex and updates the heap
	 * @param toVertex
	 */
	private void recalculateShortestPath(Integer toVertex) {
		List<Edge> existingEdges = edgesToVertex.get(toVertex);
		heap.remove(toVertex);
		Integer minDistance = Integer.MAX_VALUE;
		Integer distance;
		for (Edge edge : existingEdges) {
			distance = edge.l + shortestPaths.get(edge.origin);
			if (distance < minDistance)
				minDistance = distance;
		}
		heap.put(minDistance, toVertex);
	}
}
