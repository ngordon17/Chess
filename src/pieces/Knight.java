package pieces;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import board.ChessBoard;
import board.ChessPanel;

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
	
	
	@Override
	public Knight manufacture(int row, int col, boolean isWhite) {
		return new Knight(row, col, isWhite);
	}
	
	public static PieceFactory getFactory() {
		return new PieceFactory(new Knight());
	}

	@Override
	public List<ChessPanel> getLegalMoves(ChessBoard board) {
		List<ChessPanel> legal = new ArrayList<ChessPanel>();
		ChessPanel panel;
		for (int row = -1; row <= 1; row+=2) {
			for (int col = -1; col <= 1; col +=2) {
				if ((panel = board.getPanel(myRow + 2*row, myCol + col)) != null && (panel.getPiece() == null || panel.getPiece().isWhite() != isWhite)) {legal.add(panel);}
				if ((panel = board.getPanel(myRow + row, myCol + 2*col)) != null && (panel.getPiece() == null || panel.getPiece().isWhite() != isWhite)) {legal.add(panel);}
			}
		}
		
		
		return legal;
	}
}