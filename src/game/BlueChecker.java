package game;

class BlueChecker extends Checker {

	//Creates a blue checker at specified position.
	public BlueChecker(int x, int y) {
		super(x, y);
	}
	
	//Used when cloning checkers.
	public Checker newChecker(int x, int y) {
		BlueChecker returnVal = new BlueChecker(x, y);
		return returnVal;
	}                
	
	//Return the color of the checker.
	public String getColor() {
		return "Blue";
	}
	
	//Direction for moves.
	public void moveForward() {
		setY(getY() - 1);
		
		//King me!
		if (endOfBoard()) 
			makeKing();
	}
	
	public void moveBackward() {
		setY(getY() + 1);
	}
	
	public void moveLeft() {
		setX(getX() + 1);
	}
	
	public void moveRight() {
		setX(getX() - 1);
	}
}
