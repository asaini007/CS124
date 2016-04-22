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
		
		maxHeap.add(10);
		maxHeap.add(8);
		maxHeap.add(7);
		maxHeap.add(6);
		maxHeap.add(5);
		System.out.println(karmarkarKarp(maxHeap));
	}
	
	// runs the O(n log n) Karmarkar-Karp approximation algorithm, returning the attainable residue
	public static Integer karmarkarKarp(PriorityQueue<Integer> numbers) {
		while(numbers.size() > 1) {
			Integer largest = numbers.remove();
			Integer secondLargest = numbers.remove();
			Integer difference = largest - secondLargest;
			numbers.add(difference);
		}
		return numbers.remove();
	}

}
