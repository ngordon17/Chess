package pieces;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import board.ChessBoard;
import board.ChessPanel;

@SuppressWarnings("serial")
public abstract class AbstractPiece extends JLabel {
	protected boolean isWhite;
	protected int myRow;
	protected int myCol;
	protected boolean hasMoved = false;
	
	protected AbstractPiece(int row, int col, boolean white) {
		myRow = row;
		myCol = col;
		isWhite = white;
		setIcon(getImageIcon(isWhite));
	}
	
	protected AbstractPiece(AbstractPiece piece) {
		myRow = piece.myRow;
		myCol = piece.myCol;
		hasMoved = piece.hasMoved;
		isWhite = piece.isWhite;
		setIcon(getImageIcon(isWhite));
	}
	
	protected AbstractPiece() {}	
	
	@Override
	public abstract AbstractPiece clone();
	protected abstract String getPieceName();
	public abstract ImageIcon getImageIcon(boolean isWhite);
	public abstract List<AbstractPiece> manufactureInitialPieces();
	public abstract List<ChessPanel> getLegalMoves(ChessBoard board);

	protected List<ChessPanel> getLegalMovesHelper(ChessBoard board, int deltaR, int deltaC) {
		List<ChessPanel> legal = new ArrayList<ChessPanel>();
		int row = myRow + deltaR;
		int col = myCol + deltaC;
		ChessPanel panel;
		
		while ((panel = board.getPanel(row, col)) != null) {
			AbstractPiece piece = panel.getPiece();
			if (piece != null) {
				if (piece.isWhite() != isWhite) {legal.add(panel);}
				break;
			}
			legal.add(panel);
			row += deltaR;
			col += deltaC;
		}
		return legal;
	}
	
	public boolean isLegalMove(ChessBoard board, ChessPanel panel) {
		return getLegalMoves(board).contains(panel);
	}
	
	@Override
	public String toString() {
		if (isWhite) {return "W-" + getPieceName();}
		return "B-" + getPieceName();
	}
	
	public int getRow() {
		return myRow;
	}
	
	public int getCol() {
		return myCol;
	}
	
	public boolean isWhite() {
		return isWhite;
	}	
	
	public void setBoardLocation(int row, int col) {
		if (myRow != row || myCol != col) {
			hasMoved = true;
		}
		
		myRow = row;
		myCol = col;
	}

	public void setHasMoved(boolean moved) {
		hasMoved = moved;	
	}

	public Element savePiece(Document doc) {
		Element piece = doc.createElement("piece");
		piece.setAttribute("row", Integer.toString(myRow));
		piece.setAttribute("col", Integer.toString(myCol));
		piece.setAttribute("hasMoved", Boolean.toString(hasMoved));
		piece.setAttribute("isWhite", Boolean.toString(isWhite));
		piece.setAttribute("name", toString());
		return piece;
	}
}
