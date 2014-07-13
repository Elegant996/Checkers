package game;

//A checker move.
abstract class Move {

	//Board with the checker we are trying to move.
	private Board board;

	//Applies the move to the board and returns the new board.
	public Board apply() {
		return board;
	}

	//Is the move legal?
	public boolean isLegal() {
		return (board.isValid());
	}

	//Set board as a result of the move.
	protected void setBoard(Board board) {
		this.board = board;
	}

	//Double jump?
	public abstract boolean isChainable();
}

//Chainable move(double jump!).
abstract class ChainableMove extends Move {
	public boolean isChainable() {
		return true;
	}
}

//Move is not chainable.
abstract class NonChainableMove extends Move {
	public boolean isChainable() {
		return false;
	}
}