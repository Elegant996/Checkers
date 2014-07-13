package game;

import java.util.ArrayList;

//Regular checker type.
class RegularChecker extends CheckerType {

	public ArrayList<Move> getJumpMoves(Board currentBoard, boolean onlyChainable, Checker checker) {
		//List of possible moves.
		ArrayList<Move> moves = new ArrayList<Move>();
		
		//Add all jump moves to the list.
		moves.add(new JumpForwardLeft(currentBoard, checker));
		moves.add(new JumpForwardRight(currentBoard, checker));
 
		//Remove all moves that are not valid.
		for (int i = moves.size() - 1; i >= 0; i--)
			if (!moves.get(i).isLegal() || (onlyChainable && !moves.get(i).isChainable()))
				moves.remove(i);
 
		return moves;
	}
	
	public ArrayList<Move> getRegularMoves(Board currentBoard, boolean onlyChainable, Checker checker) {
		//List of possible moves.
		ArrayList<Move> moves = new ArrayList<Move>();
		
		//Add all regular moves to the list.
		moves.add(new ForwardLeft(currentBoard, checker));
		moves.add(new ForwardRight(currentBoard, checker));
		
		//Remove all moves that are not valid.
		for (int i = moves.size() - 1; i >= 0; i--)
			if (!moves.get(i).isLegal() || (onlyChainable && !moves.get(i).isChainable()))
				moves.remove(i);
 
		return moves;
	}

	//Clone the checker.
	CheckerType copy() {
		return new RegularChecker();
	}

	//Return checker type.
	public String getType() {
		return "RegularChecker";
	}
}
