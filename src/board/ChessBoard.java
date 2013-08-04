package board;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import pieces.*;

@SuppressWarnings("serial")
public class ChessBoard extends JPanel {
	//TODO: put some of this into a resource bundle
	private static final Color BORDER_COLOR = new Color(25, 25, 25);
	private static final int BORDER_SIZE = 15;
	public static final int BOARD_SIZE = 8;
	private ChessPanel[][] myPanels;
	private static ChessBoard myInstance;
	private static Stack<ChessBoard> myUndoInstances;
	
	private ChessBoard() {	
		reset();		
	}
	
	private ChessBoard(ChessBoard board) {
		ChessPanel[][] tiles = new ChessPanel[8][8];
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				tiles[row][col] = board.myPanels[row][col].clone();
				add(tiles[row][col]);
			}
		}
		myPanels = tiles;
	}
	
	public static ChessBoard getInstance() {
		if (myInstance == null) {myInstance = new ChessBoard();}
		return myInstance;
	}
	
	@Override
	public ChessBoard clone() {
		return new ChessBoard(this);
	}
	
	private void initializeUndoInstances() {
		myUndoInstances = new Stack<ChessBoard>();
		saveState();
	}
	
	public void saveState() {
		myUndoInstances.add(clone());
	}
	
	public ChessBoard getLastTurn() {
		if (myUndoInstances.isEmpty() || myUndoInstances.size() == 1) {return null;}
		ChessBoard board1 = myUndoInstances.pop();
		ChessBoard board2 = myUndoInstances.peek();
		myUndoInstances.add(board1);
		return board2;
	}
	
	public void undoMove() {
		if (myUndoInstances.isEmpty() || myUndoInstances.size() == 1) {return;}
		myUndoInstances.pop();
		myPanels = myUndoInstances.pop().myPanels; 	
		removeAll();
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				add(myPanels[row][col]);
			}
		}		
		revalidate();
		repaint();
	}
	
	public void reset() {		
		removeAll();
		setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE, 0, 0));
		setPreferredSize(new Dimension(ChessPanel.PREFERRED_SIZE.width * BOARD_SIZE + 2*BORDER_SIZE, ChessPanel.PREFERRED_SIZE.height * BOARD_SIZE + 2*BORDER_SIZE));
		setBorder(BorderFactory.createMatteBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_COLOR));	
		
		myPanels = new ChessPanel[BOARD_SIZE][BOARD_SIZE];
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				myPanels[row][col] = new ChessPanel(row, col);
				add(myPanels[row][col]);
			}
		}
		initializePieces();
		initializeUndoInstances();
		revalidate();
		repaint();
	}	
	
	public ChessPanel getPanel(int row, int col) {
		if (row >= BOARD_SIZE || row < 0 || col >= BOARD_SIZE || col < 0) {return null;}
		return myPanels[row][col];
	}
	
	private List<PieceFactory> getFactoryList() {
		List<PieceFactory> factoryList = new ArrayList<PieceFactory>();
		factoryList.add(Pawn.getFactory());
		factoryList.add(Rook.getFactory());
		factoryList.add(Knight.getFactory());
		factoryList.add(Bishop.getFactory());
		factoryList.add(Queen.getFactory());
		factoryList.add(King.getFactory());
		return factoryList;
	}
	
	private void initializePieces() {
		for (PieceFactory factory : getFactoryList()) {
			List<AbstractPiece> initialPieces = factory.manufactureInitialPieces();
			for (AbstractPiece piece : initialPieces) {
				myPanels[piece.getRow()][piece.getCol()].removeAll();
				myPanels[piece.getRow()][piece.getCol()].add(piece);
			}
		}
	}
	
	public ChessPanel findKing(ChessBoard board, boolean isWhite) {
		for (int row = 0; row < ChessBoard.BOARD_SIZE; row++) {
			for (int col = 0; col < ChessBoard.BOARD_SIZE; col++) {
				ChessPanel panel = board.myPanels[row][col];
				AbstractPiece piece = panel.getPiece();
				if (piece != null && (piece instanceof King) && piece.isWhite() == isWhite) {return panel;}
			}
		}
		return null;		
	}
	
	public List<AbstractPiece> findOpposingPieces(ChessBoard board, boolean isWhite) {
		List<AbstractPiece> pieces = new ArrayList<AbstractPiece>();
		for (int row = 0; row < ChessBoard.BOARD_SIZE; row++) {
			for (int col = 0; col < ChessBoard.BOARD_SIZE; col++) {
				ChessPanel panel = board.myPanels[row][col];
				AbstractPiece piece = panel.getPiece();
				if (piece != null && piece.isWhite() != isWhite) {
					pieces.add(piece);
				}
			}
		}
		return pieces;
	}
	
	public void printBoard(ChessBoard board, String header) {	
		ChessPanel[][] panels = board.myPanels;
		System.out.println("\n" + header + "\n");
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ( panels[i][j].getPiece() == null) {System.out.print(String.format("|%10s|", "Empty"));}
				else {System.out.print(String.format("|%10s|", panels[i][j].getPiece().toString()));}	
			}
			System.out.println();
		}	
	}
	
	public ChessBoard mockMove(ChessPanel start, ChessPanel end) {
		ChessBoard copy = clone();
		ChessPanel startCopy = copy.getPanel(start.getRow(), start.getCol());
		ChessPanel endCopy = copy.getPanel(end.getRow(), end.getCol());
		
		AbstractPiece piece = startCopy.getPiece();
		
		startCopy.removePiece();
		endCopy.removePiece();
		endCopy.add(piece);

		return copy;
	}
	
	public static boolean inCheck(ChessBoard board, boolean currentPlayer) {
		ChessPanel panel = board.findKing(board, currentPlayer);
		List<AbstractPiece> pieces = board.findOpposingPieces(board, currentPlayer);
		for (AbstractPiece piece : pieces) {
			//king can't take other king - do checking for this case in king's legal move.
			if (!(piece instanceof King) && piece.isLegalMove(board, panel)) {return true;}
		}
		return false;
	}
}
