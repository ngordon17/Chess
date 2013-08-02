package controller;
import java.util.List;
import pieces.AbstractPiece;
import player.Player;
import board.ChessBoard;
import board.ChessPanel;

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
		
		System.out.println("Check: " + inCheck(myBoard, myCurrentPlayer.isWhite()));
		System.out.println("Mate: " + inCheckMate());
	}
	
	public void newGame() {
		initializePlayers();
		myBoard.reset();
	}
	
	public void undoMove() {
		myBoard.undoMove();
		nextTurn();
	}

	public boolean isValidMove(ChessBoard board, AbstractPiece piece, ChessPanel dropped) {
		//TODO: castle, corner cases.
		//if (dropped == null || piece == null) {return false;}
		ChessBoard copy = board.mockMove(board.getPanel(piece.getRow(), piece.getCol()), dropped);
		if (inCheck(copy, myCurrentPlayer.isWhite())) {return false;}
		return piece.isLegalMove(myBoard,dropped);
	}

	public void makeMove(AbstractPiece piece, ChessPanel clickedPanel) {
		clickedPanel.add(piece);	
		nextTurn();
	}
	
	public boolean inCheck(ChessBoard board, boolean currentPlayer) {
		ChessPanel panel = board.findKing(board, currentPlayer);
		List<AbstractPiece> pieces = board.findOpposingPieces(board, currentPlayer);
		for (AbstractPiece piece : pieces) {
			if (piece.isLegalMove(board, panel)) {return true;}
		}
		return false;
	}
	
	public boolean inCheckMate() {
		if (!inCheck(myBoard, myCurrentPlayer.isWhite())) {return false;}
		List<AbstractPiece> pieces = myBoard.findOpposingPieces(myBoard, !myCurrentPlayer.isWhite());
		
		for (AbstractPiece piece : pieces) {
			List<ChessPanel> legal = piece.getLegalMoves(myBoard);
			for (ChessPanel move : legal) {
				ChessBoard copy = myBoard.mockMove(myBoard.getPanel(piece.getRow(), piece.getCol()), move);
				if (!inCheck(copy, myCurrentPlayer.isWhite())) {
					return false;
				}
			}
		}
		return true;	
	}
	
	
	public void highlightLegalMoves(AbstractPiece piece) {
		//TODO: implement me!
	}

}
