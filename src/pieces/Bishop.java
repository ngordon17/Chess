package pieces;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import board.ChessBoard;

@SuppressWarnings("serial")
public class Bishop extends AbstractPiece {
	private static final String WHITE_IMAGE_PATH = "src/resources/White_Bishop.png";
	private static final String BLACK_IMAGE_PATH = "src/resources/Black_Bishop.png";
	private static final String BISHOP_NAME = "Bishop";
	
	private Bishop(int row, int col, boolean white) {
		super(row, col, white);
	}
	
	private Bishop(Bishop bishop) {
		super(bishop);
	}
	
	private Bishop() {
		super();
	}

	@Override
	public Bishop clone() {
		return new Bishop(this);
	}

	@Override
	protected String getPieceName() {
		return BISHOP_NAME;
	}

	@Override
	public ImageIcon getImageIcon(boolean isWhite) {
		if (isWhite) {return new ImageIcon(WHITE_IMAGE_PATH);}
		return new ImageIcon(BLACK_IMAGE_PATH);
	}

	@Override
	public List<AbstractPiece> manufactureInitialPieces() {
		List<AbstractPiece> initialPieces = new ArrayList<AbstractPiece>();
		initialPieces.add(new Bishop(0, 2, false));
		initialPieces.add(new Bishop(0, ChessBoard.BOARD_SIZE - 3, false));
		initialPieces.add(new Bishop(ChessBoard.BOARD_SIZE - 1, 2, true));
		initialPieces.add(new Bishop(ChessBoard.BOARD_SIZE - 1, ChessBoard.BOARD_SIZE - 3, true));
		return initialPieces;
	}
	
	public static PieceFactory getFactory() {
		return new PieceFactory(new Bishop());
	}
}