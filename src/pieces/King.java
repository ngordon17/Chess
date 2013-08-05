package pieces;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import board.ChessBoard;
import board.ChessPanel;

@SuppressWarnings("serial")
public class King extends AbstractPiece {
	private static final String WHITE_IMAGE_PATH = "src/resources/White_King.png";
	private static final String BLACK_IMAGE_PATH = "src/resources/Black_King.png";
	private static final String KING_NAME = "King";
	
	private King(int row, int col, boolean white) {
		super(row, col, white);
	}
	
	private King(King king) {
		super(king);
	}
	
	private King() {
		super();
	}

	@Override
	public King clone() {
		return new King(this);
	}

	@Override
	protected String getPieceName() {
		return KING_NAME;
	}

	@Override
	public ImageIcon getImageIcon(boolean isWhite) {
		if (isWhite) {return new ImageIcon(WHITE_IMAGE_PATH);}
		return new ImageIcon(BLACK_IMAGE_PATH);
	}

	@Override
	public List<AbstractPiece> manufactureInitialPieces() {
		List<AbstractPiece> initialPieces = new ArrayList<AbstractPiece>();
		initialPieces.add(new King(0, ChessBoard.BOARD_SIZE / 2, false));
		initialPieces.add(new King(ChessBoard.BOARD_SIZE - 1, ChessBoard.BOARD_SIZE / 2, true));
		return initialPieces;
	}
	
	@Override
	public King manufacture(int row, int col, boolean isWhite) {
		return new King(row, col, isWhite);
	}
	
	
	public static PieceFactory getFactory() {
		return new PieceFactory(new King());
	}
	
	//assume correct input for rook -> TODO: fix this so that input does not need to be assumed.
	private boolean canCastle(ChessPanel rook, ChessBoard board) {
		if (hasMoved) {return false;}
		if (rook.getPiece() == null || rook.getPiece().hasMoved || !(rook.getPiece() instanceof Rook)) {return false;}
		
		for (int col = Math.min(myCol, rook.getCol()); col <= Math.max(myCol, rook.getCol()); col++) {
			//make sure positions in between rook and king are not occupied. Ignore king (col == 4) and rook itself.
			if (board.getPanel(myRow, col).getPiece() != null && col != 4 && board.getPanel(myRow, col) != rook) {
				return false;
			}
		
			//make sure king isn't already in check and that we don't move king through/into check when castling...
			if (Math.abs(myCol - col) < 3) {	
				ChessPanel panel = board.getPanel(myRow, myCol);
				panel.getPiece().hasMoved = true;
				
				ChessBoard copy = board.mockMove(panel, board.getPanel(myRow, col));
				if (ChessBoard.inCheck(copy, isWhite)) {
					return false;
				}
			}
		}		
		return true;	
	}
	
	//we took king out of in check function so make up for that here. 
	public boolean nextToOpposingKing(ChessBoard board, int row, int col) {
		for (int i = row - 1; i <= row + 1; i++) {
			for (int j = col - 1; j <= col + 1; j++) {
				//king cannot move next to opposing king
				if (board.getPanel(i, j) != null && board.getPanel(i,j).getPiece() instanceof King && (i != myRow || j != myCol)) {
					return true;
				}
			}
		}	
		return false;
	}
	
	@Override
	public List<ChessPanel> getLegalMoves(ChessBoard board) {		
		List<ChessPanel> legal = new ArrayList<ChessPanel>();
		ChessPanel panel;
		for (int row = myRow - 1; row <= myRow + 1; row++) {
			for (int col = myCol - 1; col <= myCol + 1; col++) {
				if (row == myRow && col == myCol) {continue;} //not moving is not a valid move
				if (nextToOpposingKing(board, row, col)) {continue;}
				
				if ((panel = board.getPanel(row, col)) != null && (panel.getPiece() == null || panel.getPiece().isWhite() != isWhite)) {legal.add(panel);}
			}
		}	
		if (canCastle(board.getPanel(myRow, 0), board)) {legal.add(board.getPanel(myRow, myCol - 2));}
		if (canCastle(board.getPanel(myRow, ChessBoard.BOARD_SIZE - 1), board)) {legal.add(board.getPanel(myRow, myCol + 2));}	
		return legal;
	}
}