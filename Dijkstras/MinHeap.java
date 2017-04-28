import java.util.Hashtable;
import java.util.Map;
import java.util.NoSuchElementException;

public class MinHeap<K extends Comparable<? super K>, V> {

	//Mapping of a key to it's location in the heap array
	private Map<K, Integer> keyIndex;
		
	//Node keys
	private K[] keyArr;
	
	//Node values. Key and Value arrays are associated by their array indicies
	private V[] valArr;
	//Index for the position past the last Node in the array 
	private int lastIndex = 1;
	
	@SuppressWarnings("unchecked")
	MinHeap() {
		keyArr = (K[])new Comparable[3];
		valArr = (V[])new Comparable[3];
		keyIndex = new Hashtable<K, Integer>();
	}
	
	/**
	 * Add a key and value to the heap
	 * @param key
	 * @param val
	 */
	public void put(K key, V val) {
		if (key == null || val == null) {
			throw new IllegalArgumentException("Null Values are not allowed");
		}
		if (keyIndex.containsKey(key)) {
			int index = keyIndex.get(key);
			keyArr[index] = key;
			valArr[index] = val;
		} else {
			if (lastIndex >= keyArr.length / 2)
				resize();
			keyArr[lastIndex] = key;
			valArr[lastIndex] = val;
			keyIndex.put(key, lastIndex);
			reheap(lastIndex);
			lastIndex++;
		}
	}
	
	/**
	 * Starts a reheap process of swapping nodes, ensuring that minimum values bubble upwards
	 * @param childIndex the node to start the reheap process on
	 */
	private void reheap(int childIndex) {
		//we reached the top
		if (childIndex == 1)
			return;
		
		int parentIndex;
		//odd / left nodes
		if (childIndex % 2 == 0)
			parentIndex = childIndex / 2; 
		//even / right nodes
		else
			parentIndex = (childIndex - 1) / 2;
		//the child node is "less than" than the parent
		if (keyArr[childIndex].compareTo(keyArr[parentIndex]) < 0) {
			swap(childIndex, parentIndex);
			reheap(parentIndex);
		}
	}
	
	/**
	 * Swaps two nodes in the heap.
	 * @param a - index of a node 
	 * @param b - index of a node
	 */
	private void swap(int a, int b) {
		if (a == b) //do nothing
			return;
		K tempKey = keyArr[a];
		V tempVal = valArr[a];
		if (!keyIndex.containsKey(keyArr[a])) 
			throw new IllegalStateException();
		keyIndex.put(keyArr[a], b);
		keyArr[a] = keyArr[b];
		valArr[a] = valArr[b];
		if (!keyIndex.containsKey(keyArr[b])) 
			throw new IllegalStateException();
		keyIndex.put(keyArr[b], a);
		keyArr[b] = tempKey;
		valArr[b] = tempVal;
	}
	
	public int size() {
		return lastIndex - 1;
	}
	
	/**
	 * Removes the min element
	 * @return
	 */
	public V removeMin() {
		if (size() == 0)
			throw new NoSuchElementException();
		return remove(keyArr[1]);
	}
	
	public V peekMin() {
		if (size() == 0)
			throw new NoSuchElementException();
		return valArr[1];
	}
	
	public V remove(K searchKey) {
		int searchKeyIndex = keyIndex.get(searchKey);
		//store the value we want to return
		V returnValue = valArr[searchKeyIndex];
		//swap the last node with the the node we wish to remove
		swap(--lastIndex, searchKeyIndex);
		//remove pointers
		keyIndex.remove(searchKey);
		keyArr[lastIndex] = null;
		valArr[lastIndex] = null;
		//bubble down starting at the node where we removed the element
		bubbleDown(searchKeyIndex);
		return returnValue;
	}
	
	private void bubbleDown(int parentIndex) {
		int leftChild = parentIndex * 2;
		int rightChild = parentIndex * 2 + 1;
		//the parentIndex is a leaf, there are no children
		if (leftChild >= lastIndex) { 
			return;
		}
		int smallerChild;
		//check if there is a right child
		if (rightChild < lastIndex) {
			//compare which child is smaller
			if (keyArr[leftChild].compareTo(keyArr[rightChild]) < 0)
				smallerChild = leftChild;
			else
				smallerChild = rightChild;
		} 
		else //no right child, just compare to parent to left child
			smallerChild = leftChild;
		//check if child is smaller
		if (keyArr[parentIndex].compareTo(keyArr[smallerChild]) > 0) {
			swap(parentIndex, smallerChild);
			bubbleDown(smallerChild);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void resize() {
		K[] newKeyArr = (K[]) new Comparable[keyArr.length * 2]; 
		V[] newValArr = (V[]) new Comparable[keyArr.length * 2]; 
		for (int i = 0; i < keyArr.length; i++) {
			newKeyArr[i] = keyArr[i];
			newValArr[i] = valArr[i];
		}
		keyArr = newKeyArr;
		valArr = newValArr;
	}
	
	public String toString() {
		String returnString = "";		
		int thresh = 2;
		String level = "";
		for (int i = 1; i < lastIndex; i++) {
			level += " " + keyArr[i];
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
