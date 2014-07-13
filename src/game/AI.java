package game;

//Base Class for AI alogorithms.
abstract class AI {

	//Player's Color
	private String color;
	
	//Constructor, called when an AI object is created through inheritance.
	protected AI(String color) {
		this.color = color;
	}
	
	//Checks checker piece against AI's color.
	protected boolean isMyColor(Checker piece) {
		return piece.getColor() == color;
	}
	
	// return the player's color
	public String getColor() {
		return color;
	}
	
	//Determines whether board has a jump move for that player.
	protected boolean hasJumpMove(Board jumpBoard, boolean myTurn) {
		Board board = jumpBoard.copy();
		Checker checker = jumpBoard.pop();
		
		while(checker != null) {
			if (isMyColor(checker) && myTurn && !checker.getJumpMoves(board, false).isEmpty())
				return true;
			else if (!isMyColor(checker) && !myTurn && !checker.getJumpMoves(board, false).isEmpty())
				return true;
			else
				checker = jumpBoard.pop();
		}
		
		return false;
	}
	
	//All AI classes will utilize this function so we may have consistency between all algorithms.
	abstract Board computeNextMove(Board board);

}
