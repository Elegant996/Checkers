package game;

import java.util.ArrayList;
import java.util.Random;

public class Dummy extends AI {

	public Dummy(String color) {
		super(color);
	}
	
	//Computes the next move.
	Board computeNextMove(Board board) {
		return randomMove(board, false);
	}
	
	
	//Returns a completely random move.
	private Board randomMove(Board board, boolean needChained) {
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
		boolean jumpOnly = hasJumpMove(board.copy(), true);

		//Look at each piece on the board and store them.
		ArrayList<Move> allMoves = new ArrayList<Move>();
		Board curBoard = board.copy();
		while(curChecker != null) {
			//Determine if we should be picking one of our pieces or one of their pieces.
			if (isMyColor(curChecker)) {
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
			return randomMove(allMoves.get(i).apply(), true);
		else
			return allMoves.get(i).apply();
	}
}
