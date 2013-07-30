package pieces;

import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public abstract class AbstractPiece extends JLabel {
	protected boolean isWhite;
	protected int myRow;
	protected int myCol;
	
	protected AbstractPiece(int row, int col, boolean white) {
		myRow = row;
		myCol = col;
		isWhite = white;
		setIcon(getImageIcon(isWhite));
	}
	
	protected AbstractPiece(AbstractPiece piece) {
		myRow = piece.myRow;
		myCol = piece.myCol;
		isWhite = piece.isWhite;
		setIcon(getImageIcon(isWhite));
	}
	
	protected AbstractPiece() {}	
	
	@Override
	public abstract AbstractPiece clone();
	protected abstract String getPieceName();
	public abstract ImageIcon getImageIcon(boolean isWhite);
	public abstract List<AbstractPiece> manufactureInitialPieces();
	
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
}
