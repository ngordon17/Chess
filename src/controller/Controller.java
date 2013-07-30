package controller;
import player.Player;
import board.ChessBoard;

public class Controller {
	private static ChessBoard myBoard;
	private Player[] myPlayers;
	private Player myCurrentPlayer;
	
	public Controller() {
		myBoard = ChessBoard.getInstance();
		initializePlayers();
	}
	
	private void initializePlayers() {
		myPlayers = new Player[] {new Player(true), new Player(false)};
		myCurrentPlayer = myPlayers[0];
	}
	
	public Player getCurrentPlayer() {
		return myCurrentPlayer;
	}
	
	private void nextTurn() {
		if (myPlayers[0] == myCurrentPlayer) {myCurrentPlayer = myPlayers[1];}
		else {myCurrentPlayer = myPlayers[0];}
		myBoard.saveState();
	}
	
	public void newGame() {
		initializePlayers();
		myBoard.reset();
	}
	
	public void undoMove() {
		myBoard.undoMove();
	}
}
