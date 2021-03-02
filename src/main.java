import java.util.ArrayList;
import java.util.Arrays;

import processing.core.PApplet;
import processing.event.KeyEvent;

public class main extends PApplet {
	final char compMarker = 'O';
	final char playerMarker = 'X';
	ArrayList<Integer> emptySquares = new ArrayList<Integer>(); //denotes empty selectable squares
	char markers[] = { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' };//denotes what value a square will hold
	int turn; // 0 for computer, 1 for player
	boolean nextTurn; // only redraw when it is the next turn
	boolean gameOver;
	int begin = 1;
	int human = 0;
	int initialReset = 0;
	int winner; // 0 for comp, 1 for player, -1 for tie, 2 or 3 for player 1 and 2
	int c = 0;
	
	public void setup() {
		textAlign(CENTER, CENTER);
		gameOver = false;

		// initlize emptySpaces
		for (int i = 0; i < markers.length; i++) {
			emptySquares.add(i); // all positions in grid start as empty
		}

		// who goes first?
		float coinFlip = random(2); // will be 0 or 1
		turn = (int) coinFlip;

		// Game begins
		nextTurn = true;
	}

	public void settings() {
		size(640, 740);
	}

	public void draw() {
		if (begin == 1) {
			textSize(50);
			background(50);
			stroke(255);
			fill(255);
			text("Tic-Tac-Toe", width / 2, height / 2 - 80);
			rectMode(CENTER);
			rect(width / 2, height / 2 + 80, width / 2, 80);
			rect(width / 2, height / 2 + 230, width / 2, 80);
			textSize(25);
			fill(0);
			text("Play Computer", width / 2, height / 2 + 80);
			text("Play Human", width / 2, height / 2 + 230);
		} else {
			if (nextTurn == true) {
				c++;
				drawGrid();
				if (gameOver) {
					turn = 2;
					nextTurn = false;
					finalScreen();
				}
			}
			if (!gameOver) {
				nextTurn = false;
				if (turn == 0 && human == 0) {
					compTurn();
				}
			}
		}

	}

	public void mousePressed() {
		float x = mouseX;
		float y = mouseY;
		if (begin == 1) {

			// PLAYING AGAINST COMPUTER
			if (x > width / 2 - width / 4 && x < width / 2 + width / 4) {
				if (y > height / 2 + 40 && y < height / 2 + 120) {
					reset();
					begin = 0;
					human = 0;
				}
			}

			// PLAYING AGAINST HUMAN (Yourself)
			if (x > width / 2 - width / 4 && x < width / 2 + width / 4) {
				if (y > height / 2 + 190 && y < height / 2 + 270) {
					reset();
					begin = 0;
					human = 1;
					initialReset = 1;
				}
			}
		}
		if (begin != 1) {
			if (x < width - 40 && x > width - 120) {

				if (y < 45) {
					begin = 1;
					reset();
				}
			}
		}
		if ((turn == 1 && nextTurn == false) || human == 1 && !gameOver) { // It is the players turn
			int square; // can be between 0-8

			int h = height - 100; // removes top menu from calculations

			// 9 squares total
			//0 1 2
			//3 4 5
			//6 7 8
			if (x < width / 3) {
				if (y < h / 3 + 100)
					square = 0;
				else if (y < 2 * h / 3 + 120)
					square = 3;
				else
					square = 6;
			} else if (x < 2 * width / 3) {
				if (y < h / 3 + 100)
					square = 1;
				else if (y < 2 * h / 3 + 120)
					square = 4;
				else
					square = 7;
			} else {
				if (y < h / 3 + 100)
					square = 2;
				else if (y < 2 * h / 3 + 120)
					square = 5;
				else
					square = 8;
			}
			// checks if selected square is valid
			if (!gameOver && emptySquares.contains(square)) {
				placeSymbol(square);
			}

		}

	}

	public void keyPressed(KeyEvent event) {
		if ((key == ' ') && (keyPressed == true)) {
			if (gameOver) {
				reset();
			}
		}
	}

	public void reset() {
		gameOver = false;
		Arrays.fill(markers, ' ');
		initialReset = 0;
		emptySquares.clear();
		for (int i = 0; i < markers.length; i++) {
			emptySquares.add(i); // all positions in grid start as empty
		}
		// who goes first?
		float coinFlip = random(2); // will be 0 or 1
		turn = (int) coinFlip;

		// Game begins
		nextTurn = true;
	}

	public void compTurn() {
		// timer(); //pause befor comp turn
		// choose a random number based on empty spaces
		float choice = random((float) emptySquares.size());

		placeSymbol(emptySquares.get((int) choice)); // pick choice from empty spaces list

	}

	public void placeSymbol(int choice) {
		char mark;
		int nextPlayer;

		// choose marker and choose next player
		if (turn == 0) {
			mark = compMarker;
			nextPlayer = 1;
		} else {
			mark = playerMarker;
			nextPlayer = 0;
		}

		markers[choice] = mark; // place the marker into the array of markers
		emptySquares.remove(Integer.valueOf(choice)); // space is no longer empty

		if (checkWin())
			gameOver = true;

		turn = nextPlayer;
		nextTurn = true;
	}

	public boolean checkWin() {
		char mark;
		if (turn == 0) {
			mark = compMarker;
		} else {
			mark = playerMarker;
		}

		// Check Board for win
		if (emptySquares.size() == 0) {
			winner = -1;
			return true;
		} else {
			// check for diagonal win
			if (markers[0] == mark && markers[4] == mark && markers[8] == mark
					|| markers[2] == mark && markers[4] == mark && markers[6] == mark) {
				winner = turn;
				return true;
			}
			// check for horizontal or vertical win
			else {
				for (int i = 0; i < 3; i++) {
					int j = i * 3; // to get the row number
					if (markers[j] == mark && markers[j + 1] == mark && markers[j + 2] == mark
							|| markers[i] == mark && markers[i + 3] == mark && markers[i + 6] == mark) {
						winner = turn;
						return true;
					}
				}

			}
		}

		return false;
	}

	public void drawGrid() {
		if (initialReset == 1) {
			reset();
		}
		String turnText;
		textSize(20);
		background(50);
		stroke(255);
		fill(255);
		rect(width - 80, 25, 80, 40);
		fill(0);
		text("Back", width - 80, 25);
		fill(255);
		if (gameOver) {
			turn = 2;
		}
		switch (turn) {
		case 1:
			turnText = "Player";
			break;
		case 2:
			turnText = "End";
			break;
		default:
			turnText = "Player";
		}

		if (human == 1 && !gameOver) {
			if (turn == 0) {
				turnText = "Player1";
			} else {
				turnText = "Player2";
			}
		}
		text("Turn: " + turnText, 100, 50);
		textSize(100);
		int t = height - 100;
		
		// draw grid
		line(width / 3, 100, width / 3, height);
		line(2 * width / 3, 100, 2 * width / 3, height);
		line(0, t / 3 + 100, width, t / 3 + 100);
		line(0, 2 * t / 3 + 100, width, 2 * t / 3 + 100);

		// fill in grid
		int h, w;
		int counter = 0;
		int multw = 1; // to set proper width
		int multh = 1; // to set proper height;
		for (int i = 0; i < markers.length / 3; i++) {
			h = ((height - 100) / 6 * multh) + 100;
			multw = 1; // reset multw for next row, so that it starts at the begining of row
			for (int j = 0; j < 3; j++) {
				w = width / 6 * multw;
				text(markers[counter], w, h);
				counter++;
				multw += 2;

			}
			multh += 2;
		}
	}

	public void finalScreen() {
		textSize(20);
		String winText;
		// background(0);
		if (human == 1 && winner != -1) {
			if (turn == 0) {
				winner = 3;
			} else {
				winner = 2;
			}
		}
		switch (winner) {
		case 0:
			winText = "The computer Won, Press space to play again";
			break;
		case 1:
			winText = "You Win!, Press space to play again";
			break;
		case 2:
			winText = "PLayer 1 Wins, Press space to play again";
			break;
		case 3:
			winText = "PLayer 2 Wins, Press space to play again";
			break;
		case -1:
			winText = "TIE GAME, Press space to play again";
			break;
		default:
			winText = "DEBUG";
		}
		text(winText, width / 2 + 70, 60);
	}

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "main" };
		PApplet.main(appletArgs);
	}
}
