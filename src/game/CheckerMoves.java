package game;

class JumpForwardLeft extends ChainableMove {

	public JumpForwardLeft(Board board, Checker checker) {
		//Clone the board and the checker.
		Board newBoard = board.copy();
		Checker newChecker = checker.copy();
 
		//Move where the checker to be removed is.
		newChecker.moveForward();
		newChecker.moveLeft();
 
		//Jump the checker!
		newBoard.jumpChecker(newChecker);
		
		//Move again to go past it simulating the jump.
		newChecker.moveForward();
		newChecker.moveLeft(); 
 
		//Add the checker to the board.
		newBoard.push(newChecker); 
 
		//Store the board the move type.
		setBoard(newBoard);
	}
}

class JumpForwardRight extends ChainableMove {
	public JumpForwardRight(Board board, Checker checker) {
		//Clone the board and the checker.
		Board newBoard = board.copy();
		Checker newChecker = checker.copy();
		
		//Move where the checker to be removed is.
		newChecker.moveForward();
		newChecker.moveRight();
		
		//Jump the checker
		newBoard.jumpChecker(newChecker);
		
		//Move again to go past it simulating the jump.
		newChecker.moveForward();
		newChecker.moveRight();
		
		//Add the checker to the board.
		newBoard.push(newChecker);
		
		//Store the board the move type.
		setBoard(newBoard);
	}
}

class JumpBackwardLeft extends ChainableMove {
	public JumpBackwardLeft(Board board, Checker checker) {
		//Clone the board and the checker.
		Board newBoard = board.copy();
		Checker newChecker = checker.copy();
		
		//Move where the checker to be removed is.
		newChecker.moveBackward();
		newChecker.moveLeft();
		
		//Jump the checker
		newBoard.jumpChecker(newChecker);
		
		//Move again to go past it simulating the jump.
		newChecker.moveBackward();
		newChecker.moveLeft();
		
		//Add the checker to the board.
		newBoard.push(newChecker);
		
		//Store the board the move type.
		setBoard(newBoard);
	}
}

class JumpBackwardRight extends ChainableMove {
	public JumpBackwardRight(Board board, Checker checker) {
		//Clone the board and the checker.
		Board newBoard = board.copy();
		Checker newChecker = checker.copy();
		
		//Move where the checker to be removed is.
		newChecker.moveBackward();
		newChecker.moveRight();
		
		//Jump the checker
		newBoard.jumpChecker(newChecker);
		
		//Move again to go past it simulating the jump.
		newChecker.moveBackward();
		newChecker.moveRight();
		
		//Add the checker to the board.
		newBoard.push(newChecker);
		
		//Store the board the move type.
		setBoard(newBoard);
	}
}

class ForwardLeft extends NonChainableMove {
	public ForwardLeft(Board board, Checker checker) {
		//Clone the board and the checker.
		Board newBoard = board.copy();
		Checker newChecker = checker.copy();
		
		//Move the checker.
		newChecker.moveForward();
		newChecker.moveLeft();
		
		//Add the checker to the board.
		newBoard.push(newChecker);
		
		//Store the board the move type.
		setBoard(newBoard);
	}
}

class BackwardLeft extends NonChainableMove {
	public BackwardLeft (Board board, Checker checker) {
		//Clone the board and the checker.
		Board newBoard = board.copy();
		Checker newChecker = checker.copy();
		
		//Move the checker.
		newChecker.moveBackward();
		newChecker.moveLeft();
		
		//Add the checker to the board.
		newBoard.push(newChecker);
		
		//Store the board the move type.
		setBoard(newBoard);
	}
}

class ForwardRight extends NonChainableMove {
	public ForwardRight (Board board, Checker checker) {
		//Clone the board and the checker.
		Board newBoard = board.copy();
		Checker newChecker = checker.copy();
		
		//Move the checker.
		newChecker.moveForward();
		newChecker.moveRight();
		
		//Add the checker to the board.
		newBoard.push(newChecker);
		
		//Store the board the move type.
		setBoard(newBoard);
	}
}

class BackwardRight extends NonChainableMove {
	public BackwardRight(Board board, Checker checker) {
		//Clone the board and the checker.
		Board newBoard = board.copy();
		Checker newChecker = checker.copy();
		
		//Move the checker.
		newChecker.moveBackward();
		newChecker.moveRight();
		
		//Add the checker to the board.
		newBoard.push(newChecker);
		
		//Store the board the move type.
		setBoard(newBoard);
	}
}

