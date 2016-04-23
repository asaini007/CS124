package numberPartition;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class Main {
	public enum Set{PLUS, MINUS}
	
	public static void main(String[] args) {
		Long[] numbers = new Long[] { 10L,8L,7L,6L,5L };
		int[] prepartitioning = prepartitioningSimulatedAnnealing(numbers, 10);
	}
	
	// returns the prepartitioning solution using the repeated random algorithm, for a given array of numbers and number of iterations
	public static int[] prepartitioningRepeatedRandom(Long[] numbers, int iterations) {
		int n = numbers.length;
		int[] lowestPrepartitioning = randomPrepartitioning(n);
		Long lowestResidue = prepartitioningResidue(numbers, lowestPrepartitioning);
		int[] currentPrepartitioning;
		Long currentResidue;
		for(int i = 0; i < iterations; i++) {
			System.out.println(i);
			currentPrepartitioning = randomPrepartitioning(n);
			currentResidue = prepartitioningResidue(numbers, currentPrepartitioning);
			System.out.println("Lowest Prepartitioning:  " + Arrays.toString(lowestPrepartitioning) + ": Residue = " + lowestResidue.longValue());
			System.out.println("Current Prepartitioning: " + Arrays.toString(currentPrepartitioning) + ": Residue = " + currentResidue.longValue());
			if(currentResidue < lowestResidue) {
				lowestPrepartitioning = Arrays.copyOf(currentPrepartitioning, n);
				lowestResidue = currentResidue;
			}
		}
		return lowestPrepartitioning;
	}
	
	// returns the prepartitioning solution using the hill climbing algorithm, for a given array of numbers and number of iterations
	public static int[] prepartitioningHillClimbing(Long[] numbers, int iterations) {
		int n = numbers.length;
		int[] lowestPrepartitioning = randomPrepartitioning(n);
		Long lowestResidue = prepartitioningResidue(numbers, lowestPrepartitioning);
		int[] neighborPrepartitioning;
		Long neighborResidue;
		for(int i = 0; i < iterations; i++) {
			System.out.println(i);
			neighborPrepartitioning = prepartitioningRandomMove(lowestPrepartitioning);
			neighborResidue = prepartitioningResidue(numbers, neighborPrepartitioning);
			System.out.println("Lowest Prepartitioning:   " + Arrays.toString(lowestPrepartitioning) + ": Residue = " + lowestResidue.longValue());
			System.out.println("Neighbor Prepartitioning: " + Arrays.toString(neighborPrepartitioning) + ": Residue = " + neighborResidue.longValue());
			if(neighborResidue < lowestResidue) {
				lowestPrepartitioning = Arrays.copyOf(neighborPrepartitioning, n);
				lowestResidue = neighborResidue;
			}
		}
		return lowestPrepartitioning;
	}
	
	// returns the prepartitioning solution using the simulated annealing algorithm, for a given array of numbers and number of iterations
	public static int[] prepartitioningSimulatedAnnealing(Long[] numbers, int iterations) {
		int n = numbers.length;
		int[] currentPrepartitioning = randomPrepartitioning(n);
		Long currentResidue = prepartitioningResidue(numbers, currentPrepartitioning);
		int[] lowestPrepartitioning = Arrays.copyOf(currentPrepartitioning, n);
		Long lowestResidue = currentResidue;
		int[] neighborPrepartitioning;
		Long neighborResidue;
		for(int i = 0; i < iterations; i++) {
			System.out.println(i);
			neighborPrepartitioning = prepartitioningRandomMove(currentPrepartitioning);
			neighborResidue = prepartitioningResidue(numbers, neighborPrepartitioning);
			System.out.println("Lowest Prepartitioning:   " + Arrays.toString(lowestPrepartitioning) + ": Residue = " + lowestResidue.longValue());
			System.out.println("Current Prepartitioning:  " + Arrays.toString(currentPrepartitioning) + ": Residue = " + currentResidue.longValue());
			System.out.println("Neighbor Prepartitioning: " + Arrays.toString(neighborPrepartitioning) + ": Residue = " + neighborResidue.longValue());
			if(neighborResidue < currentResidue) {
				currentPrepartitioning = Arrays.copyOf(neighborPrepartitioning, n);
				currentResidue = neighborResidue;
			} else {
				Random random = new Random();
				double randomDouble = random.nextDouble();
				double probability = Math.pow(Math.E, (currentResidue - neighborResidue) / t(i));
				if(randomDouble < probability) {
					currentPrepartitioning = Arrays.copyOf(neighborPrepartitioning, n);
					currentResidue = neighborResidue;
				}
			}
			if(currentResidue < lowestResidue) {
				lowestPrepartitioning = Arrays.copyOf(currentPrepartitioning, n);
				lowestResidue = currentResidue;
			}
		}
		return lowestPrepartitioning;
	}
	
	// cooling schedule
	private static double t(int i) {
		return Math.pow(10, 10) * Math.pow(0.8, Math.floor(i/300));
	}

	// returns an array of length n of random numbers on [0,n)
	public static int[] randomPrepartitioning(int n) {
		int[] prepartitioning = new int[n];
		Random random = new Random();
		for(int i = 0; i < n; i++) {
			prepartitioning[i] = random.nextInt(n);
		}
		return prepartitioning;
	}
	
	// returns the residue of the given set of numbers and their set solution
	public static Long prepartitioningResidue(Long[] numbers, int[] prepartitioning) {
		Long[] stdNumbers = prepartitionToStandard(numbers, prepartitioning);
		return karmarkarKarp(stdNumbers);
	}
	
	// enforces the given prepartition on the given numbers
	public static Long[] prepartitionToStandard(Long[] numbers, int[] prepartitioning) {
		int n = numbers.length;
		Long[] newNumbers = new Long[n];
		Arrays.fill(newNumbers, 0L);
		for(int j = 0; j < n; j++) {
			newNumbers[prepartitioning[j]] += numbers[j];
		}
		return newNumbers;
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
	
	// returns a neighboring solution of the given solution (prepartition form)
	public static int[] prepartitioningRandomMove(int[] prepartitioning) {
		int n = prepartitioning.length;
		
		Random random = new Random();
		int i = random.nextInt(n), j;
		do{
			j = random.nextInt(n);
		} while(prepartitioning[i] == j);
		
		int[] randomNeighbor = Arrays.copyOf(prepartitioning, n);
		randomNeighbor[i] = j;
		
		return randomNeighbor;
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
	public static Long stdResidue(Long[] numbers, Set[] sets) {
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
