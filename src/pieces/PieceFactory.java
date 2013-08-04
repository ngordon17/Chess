package pieces;

import java.util.List;

public class PieceFactory {	
	AbstractPiece myPiece;
	
	public PieceFactory(AbstractPiece piece) {
		myPiece = piece;
	}
	
	public List<AbstractPiece> manufactureInitialPieces() {
		return myPiece.manufactureInitialPieces();
	}
}
