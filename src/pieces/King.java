package pieces;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import board.ChessBoard;

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
	
	public static PieceFactory getFactory() {
		return new PieceFactory(new King());
	}
}