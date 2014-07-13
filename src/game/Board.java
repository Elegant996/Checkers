package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

//The game board will be maintained by this class, it will also contain the drawing of the board.
public class Board {
	
	//All buffered images are 50x50 except the board so we'll note this when drawing.
	public static final int TILE_SIZE = 50;

	//North American Checkers is played on an 8x8 board.
	public static final int SIZE = 8;
	
	//Since we will be creating multiple boards it is important that these be static to save time, we'll use them for drawing later.
	private static final BufferedImage boardImage, blueCheckerImage, redCheckerImage, blueCheckerKingImage, redCheckerKingImage;
	static {
		BufferedImage boardImageTemp = null;
		BufferedImage blueCheckerImageTemp = null;
		BufferedImage redCheckerImageTemp = null;
		BufferedImage blueCheckerKingImageTemp = null;
		BufferedImage redCheckerKingImageTemp = null;
		
		try {
			boardImageTemp = ImageIO.read(new File("Board.png"));
			blueCheckerImageTemp = ImageIO.read(new File("Blue Piece.png"));
			redCheckerImageTemp = ImageIO.read(new File("Red Piece.png"));
			blueCheckerKingImageTemp = ImageIO.read(new File("Blue King Piece.png"));
			redCheckerKingImageTemp = ImageIO.read(new File("Red King Piece.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boardImage = boardImageTemp;
		blueCheckerImage = blueCheckerImageTemp;
		redCheckerImage = redCheckerImageTemp;
		blueCheckerKingImage = blueCheckerKingImageTemp;
		redCheckerKingImage = redCheckerKingImageTemp;
	}
	
	//List of all checker pieces.
	private ArrayList<Checker> pieces = new ArrayList<Checker>();

	//Board is valid, will be utilized later.
	private boolean valid = true;
	
	//New board for exploring moves, we set the amount of new rows to 0 and pass it on.
	public Board() {
		this(0);
	}

	//Creates a new board, usually used when (re)starting a game.
	public Board(int numRowsOfPieces) {
		init(numRowsOfPieces);
	}

	//Moves the checker (if it exists) to the top of the stack so it will be the next one used.
	public boolean moveToTop(int x, int y) {
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).getX() == x && pieces.get(i).getY() == y) {
				pieces.add(pieces.remove(i));
				return true;
			}
		}
		return false;
	}
	  
	//Adds 4 checkers to each row.
	private void init(int numRowsOfPieces) {
		for (int i = 0; i < SIZE; i++) {      
			//Adds red checkers.
			for (int j = 0; j < numRowsOfPieces; j++)
				if ((i + j) % 2 == 1)
					pieces.add(new RedChecker(i, j));
			
			//Adds blue checkers
			for (int j = SIZE - numRowsOfPieces; j < SIZE; j++)
				if ((i + j) % 2 == 1)
					pieces.add(new BlueChecker(i, j));
		}
	}
	  
	//Jump a checker at its location. Will make board invalid if it fails.
	public void jumpChecker(Checker marked) {
		//Find piece to jump.
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).getX() == marked.getX() &&
					pieces.get(i).getY() == marked.getY() &&
					!pieces.get(i).sameColor(marked)) {
				pieces.remove(i);
				return;
			}
		}
	    
		//Board is invalid.
		valid = false;
	}
	  
	//Returns the validity of the board.
	public boolean isValid() {
		return valid;
	}
	  
	//Adds a checker to the stack.
	public void push(Checker checker) {    
		//Make sure move is valid (on the board) and that hte space is not occupied.
		if (!checker.isValid() || moveToTop(checker.getX(), checker.getY())) { 
			valid = false;
		} else {
			pieces.add(checker);
		}
	}
	  
	//Deep copy the board (FIXED FROM ASSIGNMENT 2!)
	public Board copy() {
		Board returnVal = new Board();
		returnVal.valid = valid;
		returnVal.pieces = new ArrayList<Checker>();
		for (int i = 0; i < pieces.size(); i++)
			returnVal.pieces.add(pieces.get(i).copy());

		return returnVal;
	}
	  
	//Returns the checker at the "top" (bottom) of the "stack".
	public Checker pop() {
		if (pieces.size() == 0)
			return null;
		return (pieces.remove(pieces.size() - 1));
	}
	  
	//Determine whether the game is over.
	public boolean gameOver() {
	    
		//Checker counter for each color.
		int numReds = 0;
		int numBlues = 0;
	    
		//Let's count!
		for (int i = 0; i < pieces.size(); i++) {
			if (pieces.get(i).getColor() == "Red")
				numReds++;
			else
				numBlues++;
	    }
	    
		//If either counter is 0 return true.
		return (numBlues == 0 || numReds == 0);
	}

	//Draws the board
	public void draw(Graphics2D g) {
		//Draw the background image so the board looks pretty.
		g.drawImage(boardImage, 0, 0, null);
		
		//We're gonna mess with the board so lets copy it.
		Board temp = copy();
	    
	    //Determine where each piece is.
		String checkerColor, checkerType;
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++)
				if (temp.moveToTop(i, j)) {
					checkerColor = temp.pieces.get(temp.pieces.size() - 1).getColor();
					checkerType = temp.pieces.get(temp.pieces.size() - 1).getType();
					
					//Checker is regular.
					if (checkerType.equals("RegularChecker")) {
						//Color is blue.
						if (checkerColor.equals("Blue")) 
							g.drawImage(blueCheckerImage, i * TILE_SIZE, j * TILE_SIZE, null);
						//Color is red.
						else if (checkerColor.equals("Red"))
							g.drawImage(redCheckerImage, i * TILE_SIZE, j * TILE_SIZE, null);      
					//Checker is a king.
					} else if (checkerType.equals ("King")) {
						//Color is blue.
						if (checkerColor.equals("Blue")) 
							g.drawImage(blueCheckerKingImage, i * TILE_SIZE, j * TILE_SIZE, null);
						//Color is red.
						else if (checkerColor.equals("Red"))
							g.drawImage(redCheckerKingImage, i * TILE_SIZE, j * TILE_SIZE, null);
					}
				}
	}
}