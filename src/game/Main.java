package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Main {
	
	//Main function that starts it all!
	public static void main(String[] args) throws InterruptedException {
		
		//Let's set the name of the frame to the name of the game!
		final JFrame frame = new JFrame("Checkers");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		//Let's set up our canvas.
		Canvas canvas = new Canvas();
		canvas.setBackground(Color.WHITE);
		canvas.setPreferredSize(new Dimension(Board.SIZE * Board.TILE_SIZE, Board.SIZE * Board.TILE_SIZE));
		
		//Time to get the ball rolling!
		new Engine(frame, canvas).startGame();
	}
}
