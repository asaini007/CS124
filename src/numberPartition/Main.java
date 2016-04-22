package numberPartition;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Main {

	public static void main(String[] args) {
		PriorityQueue<Integer> maxHeap = new PriorityQueue<Integer>(100, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - o1;
			}
		});
		
		maxHeap.add(1);
		maxHeap.add(2);
		maxHeap.add(0);
		maxHeap.add(3);
		maxHeap.add(2);
		System.out.println(maxHeap.remove());
		System.out.println(maxHeap.remove());
		System.out.println(maxHeap.remove());
		System.out.println(maxHeap.remove());
		System.out.println(maxHeap.remove());
	}

}
