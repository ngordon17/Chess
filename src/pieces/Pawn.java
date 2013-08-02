package pieces;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import board.ChessBoard;
import board.ChessPanel;

@SuppressWarnings("serial")
public class Pawn extends AbstractPiece {
	private static final String WHITE_IMAGE_PATH = "src/resources/White_Pawn.png";
	private static final String BLACK_IMAGE_PATH = "src/resources/Black_Pawn.png";
	private static final String PAWN_NAME = "Pawn";
	
	private Pawn(int row, int col, boolean white) {
		super(row, col, white);
	}
	
	private Pawn(Pawn pawn) {
		super(pawn);
	}
	
	private Pawn() {
		super();
	}

	@Override
	public Pawn clone() {
		return new Pawn(this);
	}

	@Override
	protected String getPieceName() {
		return PAWN_NAME;
	}

	@Override
	public ImageIcon getImageIcon(boolean isWhite) {
		if (isWhite) {return new ImageIcon(WHITE_IMAGE_PATH);}
		return new ImageIcon(BLACK_IMAGE_PATH);
	}

	@Override
	public List<AbstractPiece> manufactureInitialPieces() {
		List<AbstractPiece> initialPieces = new ArrayList<AbstractPiece>();
		for (int col = 0; col < ChessBoard.BOARD_SIZE; col++) {
			initialPieces.add(new Pawn(1, col, false));
			initialPieces.add(new Pawn(ChessBoard.BOARD_SIZE - 2, col, true));
		}
		return initialPieces;
	}
	
	public static PieceFactory getFactory() {
		return new PieceFactory(new Pawn());
	}

	private int negate(int num) {
		if (!isWhite) {return num;}
		return -1 * num;
	}
	
	private boolean hasMoved() {
		if (isWhite) {return myRow != ChessBoard.BOARD_SIZE - 2;}
		return myRow != 1;
	}
	
	@Override
	public List<ChessPanel> getLegalMoves(ChessBoard board) {
		List<ChessPanel> legal = new ArrayList<ChessPanel>();
		ChessPanel panel;
		if ((panel = board.getPanel(myRow + negate(1), myCol)) != null && (panel.getPiece() == null || panel.getPiece().isWhite() != isWhite)) {legal.add(panel);}
		if (panel != null && panel.getPiece() == null && !hasMoved() && ((panel = board.getPanel(myRow + negate(2), myCol)) != null) && (panel.getPiece() == null || panel.getPiece().isWhite() != isWhite)) {legal.add(panel);}
		
		for (int col = -1 ; col <= 1; col += 2) {
			if ((panel = board.getPanel(myRow + negate(1), myCol + col)) != null && panel.getPiece() != null && panel.getPiece().isWhite() != isWhite) {legal.add(panel);}
		}
		
		return legal;
	}
}
