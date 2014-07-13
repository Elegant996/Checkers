package game;

import java.util.ArrayList;

class MiniMax extends AI {
  
	//Max number of moves ahead we will look.
	private final int maxDepth;
  
	//Best board found so far.
	private Board bestBoard;
  
	//Board of the first move made.
	private Board firstMove;
  
	//Constructor, takes in color and depth limit.
	public MiniMax(String color, int maxDepth) {
		super(color);
		this.maxDepth = maxDepth;
	}
	
	//Computes the next move.
	public Board computeNextMove(Board board) {
		bestBoard = null;
		miniMax(board, true, false, maxDepth, -2000, 2000);
		return bestBoard;
	}
  
	//Calculates the value of the board.
	private int boardValue(Board board) {
  
		//Take the next checker.
		Checker curChecker = board.pop();
    
		//Exception case: there is no board!
		if (curChecker == null)
			return 0;
    
		//Every piece has an initial value of 2 points.
		int value = 2;
    
		//Having a king is worth another 3 points.
		if (curChecker.getType() == "King")
			value += 3;
    
		//Pieces on the sides have a slight advantage, they cannot be jumped, lets give them another point.
		//if (curChecker.getX() == 0 || curChecker.getX() == Board.SIZE)
			//value++;
    
		//If the piece we've examined up until now is not hours lets negate since it is our opponent's.
		if (!isMyColor(curChecker))
			value = -value;
    
		//Sum up our value and add the checker to our board.
		value += boardValue(board);
		board.push(curChecker);
		
		return value;
	}
  
	//MiniMax with Alpha-Beta Pruning, I would go into detail but I think the name is sufficient.
	//This has been modified from the pseudocode on wikipedia to allow chaining.
	private int miniMax(Board board, boolean myTurn, boolean needChained, int depth, int alpha, int beta) {
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

		//Look at each piece on the board.
		Board curBoard = board.copy();
		while(curChecker != null) {
			//Determine if we should be picking one of our pieces or one of their pieces.
			if (isMyColor(curChecker) == myTurn) {
				//Get all possible moves.
				curBoard.moveToTop(curChecker.getX(), curChecker.getY());		
				Checker temp = curBoard.pop();
				ArrayList<Move> allMoves = (jumpOnly) ? curChecker.getJumpMoves(curBoard, needChained) :
					curChecker.getRegularMoves(curBoard, needChained);
				curBoard.push(temp);
	        
				//If no chain moves are possible we are safe to switch players.
				if (needChained && allMoves.size() == 0) {
					//Record the first move so we know what board it is.
					if (depth == maxDepth)
						firstMove = board.copy();
					
					return miniMax(board, !myTurn, false, depth - 1, alpha, beta);
				}
	        
				//Explore all moves.
				for (int i = 0; i < allMoves.size(); i++) {
					//Caluclate the board's value if we can't recurse anymore or depth is reached.
					int curValue;
					if (board.gameOver() || depth == 0)
						curValue = boardValue(allMoves.get(i).apply());          
					//Chainable move is possible lets keep going! DO NOT TOUCH DEPTH!
					else if (allMoves.get(i).isChainable())
						curValue = miniMax(allMoves.get(i).apply(), myTurn, true, depth, alpha, beta);
					//We've done all we can, let's swap sides.
					else
						curValue = miniMax(allMoves.get(i).apply(), !myTurn, false, depth - 1, alpha, beta);
	          
					//Time to start pruning.
					if (curValue < beta) 
						beta = curValue;
	          
					if (curValue > alpha) {
						alpha = curValue;
	            
						//In case we double jump on the first move let's make sure we store it.
						if (depth == maxDepth)
							if (allMoves.get(i).isChainable())
								bestBoard = firstMove;
							else
								bestBoard = allMoves.get(i).apply();
					}
				}
			}
	  
			//Next checker piece.
			curChecker = pieces.pop();
		}
		
		//Max, return best move I can make.
		if (myTurn)
			return alpha;
		//Min, return worst move my opponent can make for me.
		else
			return beta;
	}
}


