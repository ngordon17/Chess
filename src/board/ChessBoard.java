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
	//92, 52, 23
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
			}
		}
		myPanels = tiles;
		myInstance = this;
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
	
	public void undoMove() {
		if (myUndoInstances.isEmpty() || myUndoInstances.size() == 1) {return;}
		myUndoInstances.pop();
		myInstance = myUndoInstances.pop();
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
}
