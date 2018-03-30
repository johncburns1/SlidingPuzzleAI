import java.util.*;

/**
 * This class holds the method that solves a given sliding puzzle problem
 *
 *@author johnc
 * @version 1.0
 */
public class Solver {

	/**
	 * Does the solving of the puzzle passed to it by the user
	 * 
	 * @param A 2D array representing the puzzle
	 * @return A string of step-by-step directions to solving the puzzle
	 */
	public static String solvePuzzle(int[][] puzzle) {
		
		//make the starting node and print it out for the user
		Node first = new Node(puzzle);
		
		System.out.println("Initial Node:");
		first.printStats();
		System.out.print("\n");
		
		//is solvable?
		if(!solvable(first, puzzle)) {
			return "There is no solution to this puzzle";
		}

		//priority queue for A*
		PriorityQueue<Node> stateQueue = new PriorityQueue<Node>();

		//hold visited in a HashSet so that lookups can be constant
		HashSet<Node> visited = new HashSet<Node>();
		
		//initialize the goal state
		int size = first.getSize();
		int[] goalState = new int[size];
		for(int i = 0; i < size-1; i++) {
			goalState[i] = i+1; 
		}
		goalState[size-1] = 0;
		
		//add first element to the queue
		stateQueue.add(first);

		//while the queue is not empty
		while(!stateQueue.isEmpty()) {

			//poll off of the queue
			Node next = stateQueue.poll();

			//check if next is the goal state
			if(reachedGoal(next, goalState)) {
				return next.getModification();
			}

			//dont want to visit again for optimization
			visited.add(next);
			
			//make the 4 new nodes that are going to be possible modifications
			Node nextUp = new Node(next, next.getUp());
			Node nextDown = new Node(next, next.getDown());
			Node nextLeft = new Node(next, next.getLeft());
			Node nextRight = new Node(next, next.getRight());
			
			//if they are not the same node and have not been visited yet then add them to the queue
			if(!nextUp.equals(next) && !visited.contains(nextUp)) {
				stateQueue.add(nextUp);
			}

			if(!nextDown.equals(next) && !visited.contains(nextDown)) {
				stateQueue.add(nextDown);
			}

			if(!nextLeft.equals(next) && !visited.contains(nextLeft)) {
				stateQueue.add(nextLeft);
			}

			if(!nextRight.equals(next) && !visited.contains(nextRight)) {
				stateQueue.add(nextRight);
			}


		}

		return null;
	}

	/**
	 * Executes when goal is reached
	 * 
	 * @param Node current node
	 * @return boolean
	 */
	public static boolean reachedGoal(Node state, int[] goalState) {
		if(isGoal(state.getBlocks(), goalState)) {
			System.out.print("You have reached the goal!!!\n");	
			System.out.println("Your goal state is:");
			state.printStats();
			System.out.println("\n");
			return true;
		}

		return false;
	}

	/**
	 * Checks if the current node is the goal state
	 * 
	 * @param int[] order of blocks in current state
	 * @return boolean if Node is equal to the goal state
	 */
	private static boolean isGoal(int[] currentState, int[] goalState) {
		return Arrays.equals(goalState, currentState);
	}

	/**
	 * Calculates the number of inversions in an array
	 * 
	 * @param int[] array
	 * @param int size
	 * @return int numInversions
	 */
	public static int countInversions(int[] array) {

		//size 
		int n = array.length;
		int inv = 0;

		//use a brute force solution to calculate inversions
		//because it is only going to happen once for not a very
		//large data set
		for(int i = 0; i < n-1; i++) {
			for(int j = i+1; j < n; j++) {
				
				//dont count the blank space
				if(array[i] > array[j] && array[i] != 0 && array[j] != 0)
					inv++;
			}
		}
		return inv;
	}

	/**
	 * Determines whether a puzzle is solvable or not
	 * 
	 * @param int[] puzzle
	 * @return boolean 
	 */
	public static boolean solvable(Node state, int[][] puzzle) {
		
		//number of inversions excluding blank space
		int inversions = countInversions(state.getBlocks());
		
		//for arithmetic
		int inversionsState = inversions%2;
		int width = puzzle[0].length;
		int height = puzzle.length;
		int widthState = width%2;
		int row = 0;

		//find where the blank space is
		for(int i = height - 1; i >= 0; i--) {
			for(int j = width - 1; j >= 0; j--) {
				if(puzzle[i][j] == 0) {
					row = width - i + 1;
				}
			}
		}

		int rowState = row%2;

		//odd width/even inversions
		if(widthState != 0 && inversionsState == 0) {
			return true;
		}

		//even width/even row from botton/odd inversions
		else if(widthState == 0 && rowState == 0 && inversionsState != 0) {
			return true;
		}
		
		//even width/odd row from bottow/even inversions
		else if(widthState == 0 && rowState != 0 && inversionsState == 0) {
			return true;
		}

		else return false;		
	}
}
