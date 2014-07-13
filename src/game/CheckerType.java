package game;

import java.util.ArrayList;

//Helps distinguish between regular and king checkers.
abstract class CheckerType {

	//Get all possible jump moves; second parameter to only allow chainable moves (used when double jumping).
	abstract ArrayList<Move> getJumpMoves(Board currentBoard, boolean onlyChainable, Checker checker);
	
	//Get all possible regular moves; second parameter to only allow chainable moves (used when double jumping).
	abstract ArrayList<Move> getRegularMoves(Board currentBoard, boolean onlyChainable, Checker checker);
	
	//Deep cloning a checker type.
	abstract CheckerType copy();
	
	//Type of checker define by derived class.
	abstract public String getType();
}
