import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class DijkstraHeapTest {


	//distance:		{1, 2, 10, 5, 7, 13, 17, 50, 14, 89, 100 }
	//vertex:	{"1", "2", "10", "5", "7", "13", "17", "50", "14", "89", "100" }
	List<Integer> list; //list of the keys
	DijkstraHeap<Integer, String> testHeap;
	
	
	/**
	 * 		     		  1:3
	 * 		 	  /					\
	 * 		   40:4					  5:5
	 * 		 /	    \              /        \
	 * 	  65:200   71:165       10:19      46:33
	 */
	DijkstraHeap<Integer, Integer> testHeapB;
	List<Integer> listB;
	
	@Before
	public void setup() {
		list = new ArrayList<Integer>();
		testHeap = new DijkstraHeap<Integer, String>();
		Integer[] elements = new Integer[] {1, 2, 10, 5, 7, 13, 17, 50, 14, 89, 100 }; 
		for (int i = 0; i < elements.length; i++) {
			list.add(elements[i]);
			Integer value = elements[i];
			testHeap.put(elements[i], new String(value.toString()));
		}		
		
		//testHeap2
		testHeapB = new DijkstraHeap<Integer, Integer>();
		testHeapB.put(1, 3);
		testHeapB.put(40, 4);
		testHeapB.put(5, 5);
		testHeapB.put(65, 200);
		testHeapB.put(71, 165);
		testHeapB.put(10, 19);
		testHeapB.put(46, 33);
		Integer[] elementsB = new Integer[] { 1, 40, 5, 65, 71, 10, 46};
		listB = new ArrayList<Integer>();
		for (int i = 0; i < elementsB.length; i++) {
			listB.add(elementsB[i]);
		}
	}

	@Test
	public void expectingMins() {
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			DijkstraHeap<Integer, String>.Data data = testHeap.removeMin();
			assertEquals(list.get(i).toString(), data.vertex);
			assertEquals(list.get(i), data.distance);
		}
	}
	
	@Test
	public void allowDuplicates() {
		listB.add(40);
		listB.add(5);
		Collections.sort(listB);
		testHeapB.put(40, 1001);
		testHeapB.put(5, 1002);
		assertEquals(9, listB.size());
		for (int i = 0; i < 9 ; i++) {
			DijkstraHeap<Integer, Integer>.Data data = testHeapB.removeMin();
			assertEquals(listB.get(i), data.distance);
		}
	}
	
	@Test
	public void puttingRandomsAndRemovingMins() {
		Random r = new Random();
		for (int i = 0; i < 10000; i++) {
			Integer distance = r.nextInt(100000);
			String vertex = distance.toString();
			testHeap.put(distance, vertex);
		}
		String previous = "-1000";
		while (testHeap.size() != 0) {
			Integer prevDist = Integer.parseInt(previous);
			DijkstraHeap<Integer, String>.Data data = testHeap.removeMin();
			Integer nextMin = data.distance;
			assertTrue(prevDist <= nextMin);
			prevDist = nextMin;
		}
	}
	
	@Test
	public void removingAtSpecificVertex() {
		testHeapB.remove(5);
		/**
		 * 		     		  1:3
		 * 		 	  /					\
		 * 		   40:4					  10:19
		 * 		 /	    \              /       
		 * 	  65:200   71:165        46:33
		 */
		testHeapB.put(9, 12304);
		testHeapB.put(71, 777);
		/**
		 * 		     		  1:3
		 * 		 	  /					\
		 * 		   40:4					  9:12304
		 * 		 /	    \              /        \
		 * 	  65:200   71:165       10:19      46:33
		 *   /
		 * 71:777  
		 */
		Integer[] distances = new Integer[] { 1, 9, 10, 40, 46, 65, 71, 71 };
		Integer[] vertices = new Integer[] { 3, 12304, 19, 4, 33, 200, 165, 777 };
		for (int i = 0; i < distances.length; i++) {
			DijkstraHeap<Integer, Integer>.Data data = testHeapB.removeMin();
			assertEquals(distances[i], data.distance);
			assertEquals(vertices[i], data.vertex);
		}
		assertTrue(testHeapB.size() == 0);
	}
}
