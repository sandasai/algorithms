import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class MinHeapTest {

	//keys:		{1, 2, 10, 5, 7, 13, 17, 50, 14, 89, 100 }
	//values:	{"1", "2", "10", "5", "7", "13", "17", "50", "14", "89", "100" }
	List<Integer> list; //list of the keys
	MinHeap<Integer, String> testHeap;
	
	
	@Before
	public void setup() {
		list = new ArrayList<Integer>();
		testHeap = new MinHeap<Integer, String>();
		Integer[] elements = new Integer[] {1, 2, 10, 5, 7, 13, 17, 50, 14, 89, 100 }; 
		for (int i = 0; i < elements.length; i++) {
			list.add(elements[i]);
			Integer value = elements[i];
			testHeap.put(elements[i], new String(value.toString()));
		}
	}
	
	@Test
	public void test() {
	}

	@Test
	public void puttingAndExpectingMins() {
		assertEquals(11, testHeap.size());
		testHeap.put(-1, new String("-1"));
		list.add(new Integer(-1));
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			assertEquals(new String(list.get(i).toString()), testHeap.removeMin());
		}
	}
	
	@Test
	public void puttingRandomsAndRemovingMins() {
		Random r = new Random();
		for (int i = 0; i < 10000; i++) {
			Integer key = r.nextInt(1000000);
			String val = key.toString();
			testHeap.put(key, val);
		}
		String previous = "-1000";
		while (testHeap.size() != 0) {
			Integer prev = Integer.parseInt(previous);
			Integer nextMin = Integer.parseInt(testHeap.removeMin());
			assertTrue(prev < nextMin);
		}
	}
}
