package pieces;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import board.ChessBoard;

@SuppressWarnings("serial")
public class Knight extends AbstractPiece {
	private static final String WHITE_IMAGE_PATH = "src/resources/White_Knight.png";
	private static final String BLACK_IMAGE_PATH = "src/resources/Black_Knight.png";
	private static final String KNIGHT_NAME = "Knight";
	
	private Knight(int row, int col, boolean white) {
		super(row, col, white);
	}
	
	private Knight(Knight knight) {
		super(knight);
	}
	
	private Knight() {
		super();
	}

	@Override
	public Knight clone() {
		return new Knight(this);
	}

	@Override
	protected String getPieceName() {
		return KNIGHT_NAME;
	}

	@Override
	public ImageIcon getImageIcon(boolean isWhite) {
		if (isWhite) {return new ImageIcon(WHITE_IMAGE_PATH);}
		return new ImageIcon(BLACK_IMAGE_PATH);
	}

	@Override
	public List<AbstractPiece> manufactureInitialPieces() {
		List<AbstractPiece> initialPieces = new ArrayList<AbstractPiece>();
		initialPieces.add(new Knight(0, 1, false));
		initialPieces.add(new Knight(0, ChessBoard.BOARD_SIZE - 2, false));
		initialPieces.add(new Knight(ChessBoard.BOARD_SIZE - 1, 1, true));
		initialPieces.add(new Knight(ChessBoard.BOARD_SIZE - 1, ChessBoard.BOARD_SIZE - 2, true));
		return initialPieces;
	}
	
	public static PieceFactory getFactory() {
		return new PieceFactory(new Knight());
	}
}