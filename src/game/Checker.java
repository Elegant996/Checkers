package game;

import java.util.ArrayList;

//Base class for all checkers.
abstract class Checker {

	//Position.
	private int x, y;
	
	//New checkers are always regular.
	private CheckerType myType = new RegularChecker();
	
	//Constructor.
	protected Checker(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//Color checker.
	public boolean sameColor(Checker checker) {
		return getColor().equals(checker.getColor());
	}
	
	//Type checker.
	public boolean sameType(Checker checker) {
		return getType().equals(checker.getType());
	}
	
	//Get and Set for position.
	protected int getX() {
		return x;
	}
	protected int getY() {
		return y;
	}
	protected void setX(int x) {
		this.x = x;
	}
	protected void setY(int y) {
		this.y = y;
	}
	
	//King me!
	protected void makeKing() {
		myType = new King();
	}
	
	//Clone a checker.
	protected Checker copy() {
		Checker returnVal = newChecker(x, y);
		returnVal.myType = myType.copy();
		return returnVal;
	}
	
	//Used to create a new checker of that checker's color.
	abstract Checker newChecker(int x, int y);
	
	//Will return a color defined by derived class.
	abstract public String getColor();
	
	//Returns type.
	public String getType() {
		return myType.getType();
	}
	
	//Abstract functions for use by the derived class as direction of the board varies.
	abstract public void moveForward();
	abstract public void moveBackward();
	abstract public void moveLeft();
	abstract public void moveRight();
	
	//Ensure we're within the board.
	public boolean isValid() {
		return (x >= 0 && x < Board.SIZE && y >= 0 && y < Board.SIZE);
	}
	
	//Used to help with kings.
	public boolean endOfBoard() {
	 return (y == 0 || y == Board.SIZE - 1);
	}
	
	//Get all possible jump moves; second parameter to only allow chainable moves (used when double jumping).
	public ArrayList<Move> getJumpMoves(Board curBoard, boolean onlyChainable) {
		return myType.getJumpMoves(curBoard, onlyChainable, this);
	}
	
	//Get all possible regular moves; second parameter to only allow chainable moves (used when double jumping).
		public ArrayList<Move> getRegularMoves(Board curBoard, boolean onlyChainable) {
			return myType.getRegularMoves(curBoard, onlyChainable, this);
		}
}
