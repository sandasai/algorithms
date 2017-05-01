import java.util.Hashtable;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Custom heap/PQ implementation that supports log n time deletion of K-V pair within the heap
 * @author sandasai
 *
 * @param <D>
 * @param <V>
 */
public class DijkstraHeap<D extends Comparable<? super D>, V> {
	
	/**
	 * Used for retrieving both key and value data from a remove operation
	 */
	public class Data {
		D distance;
		V vertex;
		Data(D distance, V vertex) {
			this.distance = distance;
			this.vertex = vertex;
		}
	}
	
	//key - contain the total distance of edges from the origin to vertex
	private D[] distances;
	
	//value - contains the edges crossing vertices that have been processed by algorithm to vertices not yet processed yet
	private V[] vertices;
	
	//indices - we need to reference index locations of edges that lead to specific vertex
	private Map<V, Integer> indices;
	
	//keeping track of array position
	private int lastIndex = 1;

	/**
	 * Create a new heap to used where minimums are distances associated with a vertex
	 */
	@SuppressWarnings("unchecked")
	DijkstraHeap() {
		distances = (D[])new Comparable[3];
		vertices = (V[])new Object[3];
		indices = new Hashtable<V, Integer>();
	}
	
	/**
	 * Adds a vertex with a given distance to th heap
	 * @param distance
	 * @param vertex
	 */
	public void put(D distance, V vertex) {
		if (distance == null || vertex == null)
			throw new IllegalArgumentException("Null Values are not allowed");
		//replacing distances to vertex
		if (indices.containsKey(vertex)) {
			int i = indices.get(vertex);
			distances[i] = distance;
		}
		//new node in the heap
		else {
			if (lastIndex >= distances.length / 2)
				resize(); 
			distances[lastIndex] = distance;
			vertices[lastIndex] = vertex;
			indices.put(vertex, lastIndex);
			bubbleUp(lastIndex);
			lastIndex++;
		}		
	}
	
	/**
	 * Removes the shortest distance element
	 * @return the data for the minimum node
	 */
	public Data removeMin() {
		if (size() == 0)
			throw new NoSuchElementException();
		return remove(vertices[1]);
	}
	
	/**
	 * Remove a node associated with a vertex
	 * @param v Vertex to remove
	 * @return the data for the node associated with the vertex, or null if the vertex doesn't exist
	 */
	public Data remove(V v) {
		Integer i = indices.get(v);
		if (i == null)
			return null;
		Data returnData = new Data(distances[i], vertices[i]); 
		//swap the last node with the node to remove
		swap(--lastIndex, i);
		//remove pointers
		indices.remove(v);
		distances[lastIndex] = null;
		vertices[lastIndex] = null;
		//bubble down starting at the the root
		bubbleDown(i);
		return returnData;
	}
	
	/**
	 * Returns the number of nodes in the heap
	 * @return the number of nodes in the heap
	 */
	public int size() {
		return lastIndex - 1;
	}
	
	/**
	 * Resizes the arrays to accommodate more data
	 */
	@SuppressWarnings("unchecked")
	private void resize() {
		D[] newDistancesArr = (D[]) new Comparable[distances.length * 2];
		V[] newVerticesArr = (V[]) new Object[distances.length * 2];
		for (int i = 0; i < distances.length; i++) {
			newDistancesArr[i] = distances[i];
			newVerticesArr[i] = vertices[i];
		}
		distances = newDistancesArr;
		vertices = newVerticesArr;
	}
	
	/**
	 * Heapify moving from children -> parent
	 * @param index 
	 */
	private void bubbleUp(int index) {
		if (index == 1)
			return;
		int parentIndex;
		//odd / left nodes
		if (index % 2== 0)
			parentIndex = index / 2;
		//even / right nodes
		else 
			parentIndex = (index - 1) / 2;
		//check if the child node is "less than" the parent
		if (distances[index].compareTo(distances[parentIndex]) < 0) {
			swap(index, parentIndex);
			bubbleUp(parentIndex);
		}
	}
	
	/**
	 * Heapify moving from parent -> children
	 * @param index
	 */
	private void bubbleDown(int index) {
		int leftChild = index * 2;
		int rightChild = index * 2 + 1;
		//the parentIndex is a leaf, there are no children
		if (leftChild >= lastIndex) { 
			return;
		}
		int smallerChild;
		//check if there is a right child
		if (rightChild < lastIndex) {
			//compare which child is smaller
			if (distances[leftChild].compareTo(distances[rightChild]) < 0)
				smallerChild = leftChild;
			else
				smallerChild = rightChild;
		} 
		else //no right child, just compare to parent to left child
			smallerChild = leftChild;
		//check if child is smaller
		if (distances[index].compareTo(distances[smallerChild]) > 0) {
			swap(index, smallerChild);
			bubbleDown(smallerChild);
		}
	}
	
	/**
	 * Performs a swap on two node given indices of the internal arrays
	 * @param a
	 * @param b
	 */
	private void swap(int a, int b) {
		if (a == b)
			return;
		D tempDistance = distances[a];
		V tempVertex = vertices[a];
		indices.put(tempVertex, b); //updating the indices on vertex mapping
		distances[a] = distances[b];
		vertices[a] = vertices[b];
		indices.put(vertices[b], a); //updating the indices on vertex mapping
		distances[b] = tempDistance;
		vertices[b] = tempVertex;
	}

	public String toString() {
		String returnString = "";		
		int thresh = 2;
		String level = "";
		for (int i = 1; i < lastIndex; i++) {
			level += " " + distances[i];
			if (i == thresh - 1) {	//flush
				returnString += level + "\n";
				level = "";
				thresh = thresh * 2;
			}
		}
		//flush remaining
		returnString += level + "\n";
		level = "";
		thresh = thresh * 2;
		return returnString;
	}
}
