package pieces;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import board.ChessBoard;
import board.ChessPanel;

@SuppressWarnings("serial")
public class Queen extends AbstractPiece {
	private static final String WHITE_IMAGE_PATH = "src/resources/White_Queen.png";
	private static final String BLACK_IMAGE_PATH = "src/resources/Black_Queen.png";
	private static final String QUEEN_NAME = "Queen";
	
	public Queen(int row, int col, boolean white) {
		super(row, col, white);
	}
	
	private Queen(Queen queen) {
		super(queen);
	}
	
	private Queen() {
		super();
	}

	@Override
	public Queen clone() {
		return new Queen(this);
	}

	@Override
	protected String getPieceName() {
		return QUEEN_NAME;
	}

	@Override
	public ImageIcon getImageIcon(boolean isWhite) {
		if (isWhite) {return new ImageIcon(WHITE_IMAGE_PATH);}
		return new ImageIcon(BLACK_IMAGE_PATH);
	}

	@Override
	public List<AbstractPiece> manufactureInitialPieces() {
		List<AbstractPiece> initialPieces = new ArrayList<AbstractPiece>();
		initialPieces.add(new Queen(0, ChessBoard.BOARD_SIZE / 2 - 1, false));
		initialPieces.add(new Queen(ChessBoard.BOARD_SIZE - 1, ChessBoard.BOARD_SIZE / 2 - 1, true));
		return initialPieces;
	}
	
	
	@Override
	public Queen manufacture(int row, int col, boolean isWhite) {
		return new Queen(row, col, isWhite);
	}
	
	
	public static PieceFactory getFactory() {
		return new PieceFactory(new Queen());
	}

	@Override
	public List<ChessPanel> getLegalMoves(ChessBoard board) {
		List<ChessPanel> legal = new ArrayList<ChessPanel>();
		for (int row = -1; row <= 1; row++) {
			for (int col = -1; col <= 1; col++) {
				if (row == 0 && col == 0) {continue;}
				legal.addAll(getLegalMovesHelper(board, row, col));
			}
		}		
		return legal;
	}
}