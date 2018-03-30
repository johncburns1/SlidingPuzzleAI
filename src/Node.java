import java.util.*;

/**
 * Implements the node data structure used in the SlidingPuzzle class 
 *
 * @author johnc
 * @version 1.0
 */
public class Node implements Comparable<Node> {
	
	//need each node to hold an array of tiles, the heuristic value, the previous state, steps
	//already taken, and the index of the blank spot
	private int[] blocks;
	private int hstic;
	private int steps; 
	private int blank;
	private Node last;
	private int sizeOfRoot;
	private int n;
	private int sizeOfCol;
	private String modifier;
	
	/**
	 * Constructs a state of the puzzle given a 2D array of integers
	 * 
	 */
	public Node(int[][] puzzle) {
		
		//use for looping and creating new arrays
		sizeOfRoot = puzzle[0].length;
		sizeOfCol = puzzle.length;
		n = sizeOfRoot * sizeOfCol;
		blocks = new int[n];
		
		//first node
		steps = 0;
		last = null;
		modifier = modificationToString(0);
		
		//pass into a 1D array
		int k = 0;
		for(int i = 0; i < sizeOfCol; i++) {
			for(int j = 0; j < sizeOfRoot; j++) {
				blocks[k] = puzzle[i][j];
				
				if(blocks[k] == 0) {
					blank = k;
				}
				k++;
			}
		}
		
		//get the heuristic for this state using manhattan distance
		hstic = manhattan(blocks, sizeOfRoot);	
	}
	
	/**
	 * Constructs a state of the puzzle given a previous state
	 * 
	 * @param Node prevNode
	 * @param int modification
	 */
	public Node(Node prevNode, int modification) {
		
		//bring in values from previous Node and alter them accordingly
		blocks = Arrays.copyOf(prevNode.blocks, prevNode.blocks.length);
		steps = prevNode.steps + 1;
		sizeOfRoot = prevNode.sizeOfRoot;
		
		//swap
		blocks[prevNode.blank] = blocks[modification];
		blocks[modification] = 0;
		
		//set blank and last and hstic
		blank = modification;
		last = prevNode;
		modifier = last.modifier +modificationToString(modification);
		hstic = manhattan(blocks, sizeOfRoot);
		n = prevNode.n;
		
		
	}
	/**
	 * Calculates the maximum manhattan distance for each state
	 * 
	 * @param int[] blocks
	 * @param int width
	 * @return int distance
	 */
	private int manhattan(int[] blocks, int width) {
		int dist = 0;
		int x, y;
		
		for(int i = 0; i < blocks.length; i++) {
			
			//do not calculate for the blank tile
			if(blocks[i] != 0) {
				
				x = i;
				y = blocks[i] - 1;
				
				//number of steps from current position to expected position
				//because of 1D array, need an offset
				dist += Math.abs(x / width - y / width) + Math.abs(x % width - y % width);
			}
		}
		return dist;
	}
	
	/**
	 * Changes the modifier int into a string direction
	 * 
	 * @param int modification
	 * @return String representing the move made in the previous step
	 */
	private String modificationToString(int modification) {
		
		//tracing the modifications back and turning them into a string
		if(this.last != null) {
			if(modification == this.last.blank + 3) {
				return "'D' ";
			}
			
			else if(modification == this.last.blank - 3) {
				return "'U' ";
			}
			
			else if(modification == this.last.blank + 1) {
				return "'R' ";
			}
			
			else if(modification == this.last.blank - 1) {
				return "'L' ";
			}
			
			else return "'N/A'";
		}
		
		return "";
	}
	
	/**
	 * Gets the modification as a string from a given state
	 * 
	 * @return String modification
	 */
	public String getModification() {
		return this.modifier;
	}
	
	/**
	 * Gets the size of the puzzle
	 * 
	 * @return int size
	 */
	public int getSize() {
		return this.n;
	}
	
	/**
	 * Gets the heuristic of a given state
	 * 
	 * @return int heuristic
	 */
	public int getHstic() {
		return this.hstic;
	}
	
	/**
	 * Gets the steps of a state
	 * 
	 * @return int steps
	 */
	public int getSteps() {
		return this.steps;
	}
	
	/**
	 * Gets the blank of a state
	 * 
	 * @return int blank
	 */
	public int getBlank() {
		return this.blank;
	}
	
	/**
	 * Gets the array of tiles for a given node
	 * 
	 * @return int[] blocks
	 */
	public int[] getBlocks() {
		return this.blocks;
	}
	
	/**
	 * Prints the states and stats of a given node
	 * 
	 */
	public void printStats() {
		System.out.println("Tiles: " +Arrays.toString(this.blocks));
		System.out.println("Steps: " + this.steps);
		System.out.println("Heuristic: " + this.hstic);
		System.out.println("Cost: " + this.steps);
	}
	
	/**
	 * Gets the index of a manipulation up
	 * 
	 * @return int index of up
	 */
	public int getUp() {
		if((this.blank < sizeOfRoot) == false) {
			return this.blank - 3;
		}
		
		return this.blank;
	}
	
	/**
	 * Gets the index of a manipulation down
	 * 
	 * @return in index of down
	 */
	public int getDown() {
		if(this.blank <= this.blocks.length - this.sizeOfRoot - 1) {
			return this.blank + 3;
		}
		
		return this.blank;
	}
	
	/**
	 * Gets the index of a manipulation to the right
	 * 
	 * @return int index of a manipulation to the right
	 */
	public int getRight() {
		if((this.blank + 1)%this.sizeOfRoot != 0) {
			return this.blank + 1;
		}
		
		return this.blank;
	}
	
	/**
	 * Gets the index of a manipulation to the left
	 * 
	 * @return int index of a manipulation to the left
	 */
	public int getLeft() {
		if((this.blank + 1)%this.sizeOfRoot != 1) {
			return this.blank - 1;
		}
		
		return this.blank;
	}
	
	/**
	 * Gets the g+h of a state
	 * 
	 * @return int g+h
	 */
	public int getGH() {
		return this.hstic + this.steps;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Node) {
			int[] blocks = ((Node) obj).getBlocks();
			if(Arrays.equals(this.blocks, blocks)) {
				return true;
			}
			else return false;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int code = Arrays.hashCode(this.blocks);
		return code;
	}	
	
	@Override
	public int compareTo(Node state) {
		return this.getGH() - state.getGH();
	}
}
