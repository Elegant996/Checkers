package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;

//Sets up the JFrame and then handles the update and draw calls.
public class Engine extends KeyAdapter {

	//Default updates per second.
	private static int UPDATES_PER_SECOND = 2;
	
	//Fonts used for win messages.
	private static final Font FONT_SMALL = new Font("Times New Roman", Font.BOLD, 20);
	private static final Font FONT_LARGE = new Font("Times New Roman", Font.BOLD, 40);

	//Canvas to be used with JFrame.
	private Canvas canvas;
	
	//The name of the algorithm being used.
	private String playerBlueAlg, playerRedAlg;
	
	//Number of rows of pieces to be used when creating the board.
	private int numRowsOfPieces;
	
	//The board is what we'll be interacting with, staleBoard will the board one move back.
	private Board board, staleBoard;
	
	//Depth to be used with MiniMax
	private int depth, qDepth, time;
	
	//AI players.
	private AI playerBlue, playerRed;
	
	//Winner of the game.
	private AI winner;
	
	//Random to be used with turnSwitcher to randomize who starts the game. Reset will be used to ensure the board looks reset.
	private Random random;
	private boolean turnSwitcher, reset;

	//Constructor.
	public Engine(JFrame frame, Canvas canvas) throws InterruptedException {
		this.canvas = canvas;
		
		JMenuBar menuBar = new JMenuBar();
		addFileMenu(menuBar, frame);
		addPlayerMenu(menuBar);
		addOptionsMenu(menuBar, frame);
		
		frame.setJMenuBar(menuBar);
		
		frame.add(canvas);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		this.numRowsOfPieces = 3;
		this.board = null;
		this.staleBoard = null;
		
		//CHANGEME!
		playerBlueAlg = "Quiescence"; //MiniMax, Quiescence, UCT, Random
		playerRedAlg = "Random";
		
		this.depth = 5;
		this.qDepth = 3;
		this.time = 3000;
		this.playerBlue = null;
		this.playerRed = null;
		
		winner = null;
		
		random = new Random();
		
		resetGame();
		
		canvas.addKeyListener(this);
	}
	
	//Set up the game cycle, not really going to explain a whole lot here. All you need to know is this runs the board.
	public void startGame() throws InterruptedException {
		canvas.createBufferStrategy(2);
		
		Graphics2D g = (Graphics2D)canvas.getBufferStrategy().getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		long start = 0L;
		long sleepDuration = 0L;
		
		while(true) {
			start = System.currentTimeMillis();

			update();
			render(g);

			canvas.getBufferStrategy().show();

			g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			
			sleepDuration = (1000L / UPDATES_PER_SECOND) - (System.currentTimeMillis() - start);

			if (sleepDuration > 0) {
				try {
					Thread.sleep(sleepDuration);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//Resets the game.
	public void resetGame() throws InterruptedException {
		//Let's make a new board!
		board = new Board(numRowsOfPieces);
		
		//New board so their is no prior position.
		staleBoard = null;
		
		//Players and their algorithms.
		if (playerBlueAlg.equals("MiniMax"))
			playerBlue = new MiniMax("Blue", depth);
		else if (playerBlueAlg.equals("Quiescence"))
			playerBlue = new Quiescence("Blue", depth, qDepth);
		else if (playerBlueAlg.equals("UCT"))
			playerBlue = new UCT("Blue", time);
		else
			playerBlue = new Dummy("Blue");
		
		if (playerRedAlg.equals("MiniMax"))
			playerRed = new MiniMax("Red", depth);
		else if (playerRedAlg.equals("Quiescence"))
			playerRed = new Quiescence("Red", depth, qDepth);
		else if (playerRedAlg.equals("UCT"))
			playerRed = new UCT("Red", time);
		else
			playerRed = new Dummy("Red");
		
		//There is no winner.
		winner = null;
		
		//Random who starts.
		if (random.nextInt(2) == 0)
			turnSwitcher = true;
		else
			turnSwitcher = false;
		
		//Skip an update to show fresh board.
		reset = true;
	}
	
	//Update's the board.
	private void update() {
		//Pause the game if we have a winner or we are not viewing the window.
		if (winner != null)// || !canvas.hasFocus())
			return;
		
		if (reset) {
			reset = false;
			return;
		}
		
		//This is to have a backup board in case a new board cannot be made (no moves = loss).
		staleBoard = board;
		
		//If true Blue goes next.
		if (turnSwitcher) {
			board = playerBlue.computeNextMove(board);
			try {
				if (board.gameOver())
					winner = playerBlue;
				else
					turnSwitcher = false;
			} catch (NullPointerException e) {
				winner = playerRed;
			}
		}
		//If false Red goes next.
		else {
			board = playerRed.computeNextMove(board);
			
			try {
				if (board.gameOver())
					winner = playerRed;
				else
					turnSwitcher = true;
			} catch (NullPointerException e) {
				winner = playerBlue;
			}
			
		}
	}
	
	//Render the board.
	private void render(Graphics2D g) throws InterruptedException {
		//Attempts to draw the board. This CAN fail hence we have a backup scenario where we draw the old board!
		try {
			board.draw(g);
		} catch (NullPointerException e) {
			staleBoard.draw(g);
		}
		
		//We have a winner!
		if (winner != null) {
			g.setColor(Color.BLACK);
			g.setFont(FONT_LARGE);
			String message = new String(winner.getColor() + " wins!");
			g.drawString(message, canvas.getWidth() / 2 - (g.getFontMetrics().stringWidth(message) / 2),
					canvas.getHeight() / 2);
			g.setFont(FONT_SMALL);
			String subMessage = "Press enter to restart!";
			g.drawString(subMessage, canvas.getWidth() / 2 - (g.getFontMetrics().stringWidth(subMessage) / 2),
					canvas.getHeight() / 2 + (g.getFontMetrics().stringWidth(message) / 2));
		}
		
		//Things seem to be moving too fast later on so let's slow things down a tick.
		Thread.sleep(1000);
	}
	
	//Let's add some key bindings!
	public void keyPressed(KeyEvent e) {
		//If 'N' key is pushed reset the game.
		if (e.getKeyCode() == KeyEvent.VK_N) {
			try {
				resetGame();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		//If "Enter" is pushed reset only if the game is over
		if (e.getKeyCode() == KeyEvent.VK_ENTER && winner != null) {
			try {
				resetGame();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	//File Menu, not going to comment beyond this.
	public void addFileMenu(JMenuBar menuBar, final JFrame frame) {
		JMenu fileMenu = new JMenu("File");
	    menuBar.add(fileMenu);
	    
	    JMenuItem newGameAction = new JMenuItem("New Game");
	    JMenuItem exitAction = new JMenuItem("Exit");
	    
	    fileMenu.add(newGameAction);
	    fileMenu.addSeparator();
	    fileMenu.add(exitAction);
	    
	    newGameAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
					resetGame();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
		});
	    
		exitAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                frame.setVisible(false);
                System.exit(0);
            }
		});
	}
	
	//Player Menu, not going to comment beyond this.
	public void addPlayerMenu(JMenuBar menuBar) {
		JMenu playerMenu = new JMenu("Player");
	    menuBar.add(playerMenu);
	    
	  //Blue Player Menu
	    JMenu blueMenu = new JMenu("Blue");
	    
	    JRadioButton blueMiniMaxRadio = new JRadioButton("MiniMax");
	    JRadioButton blueQuiescenceRadio = new JRadioButton("Quiescence");
	    JRadioButton blueUCTRadio = new JRadioButton("UCT");
	    JRadioButton blueRandomRadio = new JRadioButton("Random");
	    
	    ButtonGroup blueButtonGroup = new ButtonGroup();
	    blueButtonGroup.add(blueMiniMaxRadio);
	    blueButtonGroup.add(blueQuiescenceRadio);
	    blueButtonGroup.add(blueUCTRadio);
	    blueButtonGroup.add(blueRandomRadio);
	    blueQuiescenceRadio.setSelected(true);
	    
	    blueMenu.add(blueMiniMaxRadio);
	    blueMenu.add(blueQuiescenceRadio);
	    blueMenu.add(blueUCTRadio);
	    blueMenu.add(blueRandomRadio);
	    
	    playerMenu.add(blueMenu);
	    
	    blueMiniMaxRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	playerBlueAlg = "MiniMax";
                try {
					resetGame();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
		});
	    
	    blueQuiescenceRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	playerBlueAlg = "Quiescence";
                try {
					resetGame();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
		});
	    
	    blueUCTRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	playerBlueAlg = "UCT";
                try {
					resetGame();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
		});
	    
	    blueRandomRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	playerBlueAlg = "Random";
                try {
					resetGame();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
		});
	    
	    //Red Player Menu
	    JMenu redMenu = new JMenu("Red");
	    
	    JRadioButton redMiniMaxRadio = new JRadioButton("MiniMax");
	    JRadioButton redQuiescenceRadio = new JRadioButton("Quiescence");
	    JRadioButton redUCTRadio = new JRadioButton("UCT");
	    JRadioButton redRandomRadio = new JRadioButton("Random");
	    
	    ButtonGroup redButtonGroup = new ButtonGroup();
	    redButtonGroup.add(redMiniMaxRadio);
	    redButtonGroup.add(redQuiescenceRadio);
	    redButtonGroup.add(redUCTRadio);
	    redButtonGroup.add(redRandomRadio);
	    redRandomRadio.setSelected(true);
	    
	    redMenu.add(redMiniMaxRadio);
	    redMenu.add(redQuiescenceRadio);
	    redMenu.add(redUCTRadio);
	    redMenu.add(redRandomRadio);
	    
	    playerMenu.add(redMenu);
	    
	    redMiniMaxRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	playerRedAlg = "MiniMax";
                try {
					resetGame();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
		});
	    
	    redQuiescenceRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	playerRedAlg = "Quiescence";
                try {
					resetGame();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
		});
	    
	    redUCTRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	playerRedAlg = "UCT";
                try {
					resetGame();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
		});
	    
	    redRandomRadio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	playerRedAlg = "Random";
                try {
					resetGame();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
		});
	}
	
	//Options Menu, not going to comment beyond this.
	public void addOptionsMenu(JMenuBar menuBar, final JFrame frame) {
		JMenu optionsMenu = new JMenu("Options");
	    menuBar.add(optionsMenu);
	    
	    JMenuItem MMDepthAction = new JMenuItem("M.M. Depth");
	    optionsMenu.add(MMDepthAction);
	    
	    JMenuItem QDepthAction = new JMenuItem("Q. Depth");
	    optionsMenu.add(QDepthAction);	
	    
	    JMenuItem UCTTimeAction = new JMenuItem("UCT Time");
	    optionsMenu.add(UCTTimeAction);
	    
	    optionsMenu.addSeparator();
	    
	    JMenuItem updateRateAction = new JMenuItem("Updates Per Second");
	    optionsMenu.add(updateRateAction);
	    
	   
		
	    MMDepthAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	String s = (String)JOptionPane.showInputDialog(
                        frame,
                        "Enter the depth for use with MiniMax:",
                        "MiniMax Depth",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "5");
            	
            	if (s != null)
            		depth = Integer.parseInt(s);
            	
            	try {
					resetGame();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
		});
	    
	    QDepthAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	String s = (String)JOptionPane.showInputDialog(
                        frame,
                        "Enter the depth for use with Quiescence Search:",
                        "Quiescence Depth",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "3");
            	
            	if (s != null)
            		qDepth = Integer.parseInt(s);
            	
            	try {
					resetGame();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
		});
	    
	    UCTTimeAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	String s = (String)JOptionPane.showInputDialog(
                        frame,
                        "Enter the time limit for UCT in milliseconds:",
                        "UCT Time Limit",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "3000");
            	
            	if (s != null)
            		time = Integer.parseInt(s);
            	
            	try {
					resetGame();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
		});
		
		updateRateAction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            	String s = (String)JOptionPane.showInputDialog(
                        frame,
                        "Enter the number of Updates Per Second you wish to have:",
                        "Updates Per Second",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        "2");
            	
            	if (s != null)
            		UPDATES_PER_SECOND = Integer.parseInt(s);
            }
		});
		
    }
}
