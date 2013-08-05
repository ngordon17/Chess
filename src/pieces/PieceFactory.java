package pieces;

import java.util.List;

public class PieceFactory {	
	AbstractPiece myPiece;
	
	public PieceFactory(AbstractPiece piece) {
		myPiece = piece;
	}
	
	public boolean isThisPiece(String name) {
		return (name.split("-")[1]).equals(myPiece.toString().split("-")[1]);
	}
	
	public AbstractPiece manufacturePiece(int row, int col, boolean isWhite) {
		return myPiece.manufacture(row, col, isWhite);
	}
	
	public List<AbstractPiece> manufactureInitialPieces() {
		return myPiece.manufactureInitialPieces();
	}
}
