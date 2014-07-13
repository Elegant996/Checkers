package game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class UCT extends AI {
	
	//This class is only to be used with UCT.
	private class Node {
		//Parent of a node.
		public Node parent;
		
		//Who's turn it will be to move next.
		public boolean myNextTurn;
		
		//Node's board.
		public Board board;
		
		//List of children.
		public LinkedList<Node> children;
		
		//Value of the node and number of visits to the node as well as its number of wins.
		public int value, visits, wins;
		
		//Constructor.
		public Node(Board board) {
			this(board, null);
		}
		
		//Constructor with next turn control.
		public Node(Board board, Node parent) {
			this.parent = parent;
			if (this.parent == null)
				this.myNextTurn = true;
			else
				this.myNextTurn = !parent.myNextTurn;
			this.board = board;
			this.value = 0;
			this.visits = 0;
			this.wins = 0;
			this.children = new LinkedList<Node>();
		}
	}

	//Max amount of time UCT has to compute a move.
	private final int maxTime;
	
	//The time in milliseconds before we started the algorithm.
	private double startTime;
  
	//Constructor, takes in color and time limit.
	public UCT(String color, int maxTime) {
		super(color);
		this.maxTime = maxTime;
	}
	
	//Computes the next move.
	public Board computeNextMove(Board board) {
		return MCTS(board);
	}
	
	//Returns the board from best child from the root.
	private Board bestChild(Node root) {
		Node bestChild = root.children.getFirst();
		double winRate, bestWinRate, UCTValue, bestUCTValue;
		for(Node child : root.children) {
			winRate = (double)child.wins / child.visits;
			UCTValue = winRate + Math.sqrt(Math.log(root.visits) / child.visits);
			
			bestWinRate = (double)bestChild.wins / bestChild.visits;
			bestUCTValue = bestWinRate + Math.sqrt(Math.log(root.visits) / bestChild.visits);
			
			if (bestUCTValue < UCTValue)
				bestChild = child;
		}
		
		return bestChild.board;
	}
	
	//Will make a random move.
	private Board randomMove(Board board, boolean myTurn, boolean needChained) {
		Checker curChecker;
		Board pieces;
		
		//Copy the board and get the first piece.
		if (!needChained) {
			pieces = board.copy();
			curChecker = pieces.pop();
	      
		//If the move requires a chain we do not copy the board and continue onward.
		} else {
			pieces = new Board();
			curChecker = board.copy().pop();
		}
		
		//Determine whether we are looking for a regular move or a jump move before we begin.
		boolean jumpOnly = hasJumpMove(board.copy(), myTurn);

		//Look at each piece on the board and store them.
		ArrayList<Move> allMoves = new ArrayList<Move>();
		Board curBoard = board.copy();
		while(curChecker != null) {
			//Determine if we should be picking one of our pieces or one of their pieces.
			if (isMyColor(curChecker) == myTurn) {
				//Get all possible moves.
				curBoard.moveToTop(curChecker.getX(), curChecker.getY());		
				Checker temp = curBoard.pop();
				allMoves.addAll((jumpOnly) ? curChecker.getJumpMoves(curBoard, needChained) :
					curChecker.getRegularMoves(curBoard, needChained));
				curBoard.push(temp);
	        
				//Leave function and return board; no chaining.
				if (needChained && allMoves.size() == 0)				
					return board;
			}
	  
			//Next board.
			curChecker = pieces.pop();
		}
		
		//Randomly select a move.
		Random random = new Random();
		int i = random.nextInt(allMoves.size());
		
		//If the move is chainable go through this function again and update the state until it is correct.
		if (allMoves.get(i).isChainable())
			return randomMove(allMoves.get(i).apply(), myTurn, true);
		else
			return allMoves.get(i).apply();
	}
	
	//Will create children for node.
	private void expand(Node node, Board board, boolean needChained) {
		Checker curChecker;
		Board pieces;
		
		//Copy the board and get the first piece.
		if (!needChained) {
			pieces = board.copy();
			curChecker = pieces.pop();
	      
		//If the move requires a chain we do not copy the board and continue onward.
		} else {
			pieces = new Board();
			curChecker = board.copy().pop();
		}
		
		//Determine whether we are looking for a regular move or a jump move before we begin.
		boolean jumpOnly = hasJumpMove(board.copy(), node.myNextTurn);

		//Look at each piece on the board.
		Board curBoard = board.copy();
		while(curChecker != null) {
			//Determine if we should be picking one of our pieces or one of their pieces.
			if (isMyColor(curChecker) == node.myNextTurn) {
				//Get all possible moves.
				curBoard.moveToTop(curChecker.getX(), curChecker.getY());		
				Checker temp = curBoard.pop();
				ArrayList<Move> allMoves = (jumpOnly) ? curChecker.getJumpMoves(curBoard, needChained) :
					curChecker.getRegularMoves(curBoard, needChained);
				curBoard.push(temp);
	        
				//Leave function; no chaining.
				if (needChained && allMoves.size() == 0) {	
					node.children.add(new Node(board, node));
					
					return;
				}
	        
				//If the move is chainable go through this function again and update the state until it is correct.
				for(int i = 0; i < allMoves.size(); i++)
					if (allMoves.get(i).isChainable())
						expand(node, allMoves.get(i).apply(), true);
					else
						node.children.add(new Node(allMoves.get(i).apply(), node));
			}
	  
			//Next checker piece.
			curChecker = pieces.pop();
		}
	}
	
	//UCT Search, time based.
	private Board MCTS(Board board) {
		//Root node.
		Node root = new Node(board.copy());
		
		//Start the timer.
		startTime = System.currentTimeMillis();
		//While we're within our alotted time.
		while(System.currentTimeMillis() - startTime < maxTime)
		//while(System.currentTimeMillis() - startTime < 1000000)
			search(root, board.copy());
		
		//Return the best board.
		return bestChild(root);
	}
	
	private int search(Node node, Board board) {
		//Randomly make moves until the game is over.
		if (node.children.isEmpty() && node.visits == 0) {
			boolean myTurn = node.myNextTurn;
			while(!board.gameOver()) {
				//We could actually force a player out of moves thus the random would be impossible.
				try {
					board = randomMove(board, myTurn, false);
				} catch (IllegalArgumentException e) {
					break;
				}
				myTurn = !myTurn;
			}
			
			//We've visited this node.
			node.visits++;
			
			//Did we win or did we lose? Since we swap myTurn the negation is a win.
			if (!myTurn)
				return 1;
			else
				return 0;
		}
		
		//Expand the node.
		if (node.children.isEmpty())
			expand(node, board, false);
		
		//If it's still empty this is in fact a terminal node.
		if (node.children.isEmpty())
			return isMyColor(board.pop()) ? 1 : 0;
		
		//Process children to determine best node.
		Node best = node.children.getFirst();
		for(Node child : node.children)
			if (best.value < child.value)
				best = child;
		
		int result = 1 - search(best, best.board.copy());
		
		//Backtrack
		best.visits++;
		best.wins += result;
		return result;
	}

}
