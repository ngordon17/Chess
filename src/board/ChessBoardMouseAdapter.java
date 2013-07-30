package board;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import pieces.AbstractPiece;
import controller.Controller;

public class ChessBoardMouseAdapter extends MouseAdapter {
	private Controller myController;
	private JScrollPane myScrollPane;
	private JLayeredPane myLayeredPane;
	private ChessPanel myClickedPanel;
	private AbstractPiece myPiece;
	
	public ChessBoardMouseAdapter(Controller boardController, JScrollPane scrollPane, JLayeredPane layeredPane) {
		myController = boardController;
		myScrollPane = scrollPane;
		myLayeredPane = layeredPane;
	}
	
	private void reset() {
		if (myPiece != null) {
			myLayeredPane.remove(myPiece);
			myLayeredPane.revalidate();
			myLayeredPane.repaint();
		}
		myClickedPanel = null;
		myPiece = null;	
	}
	
	private Point translatePoint(Component component, MouseEvent event){
		return SwingUtilities.convertPoint((Component) event.getSource(), event.getPoint(), component); 
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		
	}
	
	@Override
	public void mousePressed(MouseEvent event) {
		ChessBoard board = ChessBoard.getInstance();
		Component component = board.getComponentAt(translatePoint(board, event));
		if (component instanceof ChessPanel) {
			myClickedPanel = (ChessPanel) component;
			myPiece = myClickedPanel.getPiece();
		}
		if (myPiece == null || (myPiece.isWhite() != myController.getCurrentPlayer().isWhite())) {
			reset();
			return;
		}
		
		myClickedPanel.removePiece();
		try {myLayeredPane.add(myPiece, JLayeredPane.DRAG_LAYER);} 
		catch (IllegalArgumentException e) {/*ignore exception*/}	
		mouseDragged(event);
	}
	
	@Override
	public void mouseDragged(MouseEvent event) 
		if (myPiece == null) {
			reset();
			return;
		}
	
		int x = translatePoint(myLayeredPane, event).x - myPiece.getWidth() / 2;
		int y = translatePoint(myLayeredPane, event).y - myPiece.getHeight() / 2;		
		myPiece.setLocation(x, y);
		myLayeredPane.revalidate();
		myLayeredPane.repaint();		
	}
	
	@Override
	public void mouseReleased(MouseEvent event) {
		
	}

}
