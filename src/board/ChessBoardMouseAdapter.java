package board;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

import pieces.AbstractPiece;
import controller.Controller;

public class ChessBoardMouseAdapter extends MouseAdapter {
	private Controller myController;
	private JLayeredPane myLayeredPane;
	private ChessPanel myClickedPanel;
	private AbstractPiece myPiece;
	//only use for when want to save board before pieces are clicked
	private ChessBoard myBoard;
	
	public ChessBoardMouseAdapter(Controller boardController, JLayeredPane layeredPane) {
		myController = boardController;
		myLayeredPane = layeredPane;
	}
	
	private void reset() {
		if (myPiece != null) {
			myController.highlightLegalMoves(myPiece, false);
			myLayeredPane.remove(myPiece);
			myLayeredPane.revalidate();
			myLayeredPane.repaint();
		}
		myBoard = null;
		myClickedPanel = null;
		myPiece = null;	
	}
	
	private Point translatePoint(Component component, MouseEvent event){
		return SwingUtilities.convertPoint((Component) event.getSource(), event.getPoint(), component); 
	}
	
	@Override
	public void mousePressed(MouseEvent event) {
		ChessBoard board = ChessBoard.getInstance();
		//myBoard = board.clone();
		Component component = board.getComponentAt(translatePoint(board, event));
		if (component instanceof ChessPanel) {
			myClickedPanel = (ChessPanel) component;
			myPiece = myClickedPanel.getPiece();
		}
		if (myPiece == null || (myPiece.isWhite() != myController.getCurrentPlayer().isWhite())) {
			reset();
			return;
		}
		myBoard = board.clone();
		myController.highlightLegalMoves(myPiece, true);
		myClickedPanel.removePiece();
		try {myLayeredPane.add(myPiece, JLayeredPane.DRAG_LAYER);} 
		catch (IllegalArgumentException e) {/*ignore exception*/}	
		mouseDragged(event);
		
	}
	
	@Override
	public void mouseDragged(MouseEvent event) {
		if (myPiece == null) {
			reset();
			return;
		}
	
		//why the odd difference in calculating width/height?
		int x = translatePoint(myLayeredPane, event).x - myPiece.getIcon().getIconWidth() / 2; 
		int y = translatePoint(myLayeredPane, event).y - myPiece.getHeight() / 2;	

		myPiece.setLocation(x, y);
		myLayeredPane.revalidate();
		myLayeredPane.repaint();		
	}
	
	@Override
	public void mouseReleased(MouseEvent event) {
		if (myPiece == null) {
			reset();
			return;
		}
		ChessBoard board = ChessBoard.getInstance();
		myLayeredPane.remove(myPiece);
		
		ChessPanel dropped = (ChessPanel) board.getComponentAt(translatePoint(board, event));
		
		if (dropped == null || !myController.isValidMove(myBoard, myPiece, dropped)) {
			
			myClickedPanel.add(myPiece);
			reset();
			return;
		}

		myController.highlightLegalMoves(myPiece, false);
		myController.makeMove(myPiece, dropped);	
		reset();
	}
}
