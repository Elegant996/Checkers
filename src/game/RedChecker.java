package game;

class RedChecker extends Checker {
	  
	//Creates a red checker at specified position.
	public RedChecker(int x, int y) {
		super(x, y);
	}
	
	//Used when cloning checkers.
	public Checker newChecker(int x, int y) {
		RedChecker returnVal = new RedChecker(x, y);
		return returnVal;
	} 

	//Return the color of the checker.
	public String getColor() {
		return "Red";
	}                                                                        
	  
	//Direction for moves.
	public void moveForward() {
		setY(getY() + 1);
	    
		//King me!
		if (endOfBoard()) 
			makeKing();
	}
	
	public void moveBackward() {
		setY(getY() - 1);
	}
	
	public void moveLeft() {
		setX(getX() - 1);
	}
	
	public void moveRight() {
		setX(getX() + 1);
	}
}
