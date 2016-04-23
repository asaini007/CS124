package numberPartition;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class Main {
	public enum Set{PLUS, MINUS}
	
	public static void main(String[] args) {
		Long[] array = new Long[] { 10L,8L,7L,6L,5L };
		System.out.println(karmarkarKarp(array));
	}
	
	// runs the O(n log n) Karmarkar-Karp approximation algorithm, returning the attainable residue
	public static Long karmarkarKarp(Long[] numbers) {
		PriorityQueue<Long> maxHeap = new PriorityQueue<Long>(100, new Comparator<Long>() {
			@Override
			public int compare(Long o1, Long o2) {
				if (o2 > o1) {
					return 1;
				} else if (o2 < o1) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		
		maxHeap.addAll(Arrays.asList(numbers));
		
		while(maxHeap.size() > 1) {
			Long largest = maxHeap.remove();
			Long secondLargest = maxHeap.remove();
			Long difference = largest - secondLargest;
			maxHeap.add(difference);
		}
		return maxHeap.remove();
	}
	
	// enforces the given prepartition
	public static Long[] prepartitionToStandard(Long[] numbers, int[] prepartitioning) {
		int n = numbers.length;
		Long[] newNumbers = new Long[n];
		for(int j = 0; j < n; j++) {
			newNumbers[prepartitioning[j]] += numbers[j];
		}
		return null;
	}
	
	// returns a neighboring solution of the given solution (standard form)
	public static Set[] stdRandomMove(Set[] solution) {
		int n = solution.length;
		
		Random random = new Random();
		int i = random.nextInt(n), j;
		do{
			j = random.nextInt(n);
		} while(i == j);
		
		Set[] randomNeighbor = Arrays.copyOf(solution, n);
		randomNeighbor[i] = opposite(solution[i]);
		if(random.nextInt(2) == 0) {
			randomNeighbor[j] = opposite(solution[j]);
		}
		
		return randomNeighbor;
	}
	
	// returns the residue of the given set of numbers and their set solution
	public static Long residue(Long[] numbers, Set[] sets) {
		int n = numbers.length;
		Long residue = 0L;
		for(int i = 0; i < n; i++) {
			if(sets[i] == Set.PLUS) {
				residue += numbers[i];
			} else {
				residue -= numbers[i];
			}
		}
		return residue;
	}
	
	// return the opposite Set as the one given
	public static Set opposite(Set value) {
		return (value == Set.PLUS) ? Set.MINUS : Set.PLUS;
	}
}
