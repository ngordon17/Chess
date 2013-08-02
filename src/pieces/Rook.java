package pieces;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import board.ChessBoard;
import board.ChessPanel;

@SuppressWarnings("serial")
public class Rook extends AbstractPiece {
	private static final String WHITE_IMAGE_PATH = "src/resources/White_Rook.png";
	private static final String BLACK_IMAGE_PATH = "src/resources/Black_Rook.png";
	private static final String ROOK_NAME = "Rook";
	
	private Rook(int row, int col, boolean white) {
		super(row, col, white);
	}
	
	private Rook(Rook rook) {
		super(rook);
	}
	
	private Rook() {
		super();
	}

	@Override
	public Rook clone() {
		return new Rook(this);
	}

	@Override
	protected String getPieceName() {
		return ROOK_NAME;
	}

	@Override
	public ImageIcon getImageIcon(boolean isWhite) {
		if (isWhite) {return new ImageIcon(WHITE_IMAGE_PATH);}
		return new ImageIcon(BLACK_IMAGE_PATH);
	}

	@Override
	public List<AbstractPiece> manufactureInitialPieces() {
		List<AbstractPiece> initialPieces = new ArrayList<AbstractPiece>();
		initialPieces.add(new Rook(0, 0, false));
		initialPieces.add(new Rook(0, ChessBoard.BOARD_SIZE - 1, false));
		initialPieces.add(new Rook(ChessBoard.BOARD_SIZE - 1, 0, true));
		initialPieces.add(new Rook(ChessBoard.BOARD_SIZE - 1, ChessBoard.BOARD_SIZE - 1, true));
		return initialPieces;
	}
	
	public static PieceFactory getFactory() {
		return new PieceFactory(new Rook());
	}

	@Override
	public List<ChessPanel> getLegalMoves(ChessBoard board) {
		List<ChessPanel> legal = new ArrayList<ChessPanel>();
		legal.addAll(getLegalMovesHelper(board, 1,0));
		legal.addAll(getLegalMovesHelper(board, -1,0));
		legal.addAll(getLegalMovesHelper(board, 0,1));
		legal.addAll(getLegalMovesHelper(board, 0,-1));	
		return legal;
	}
}