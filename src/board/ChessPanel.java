package board;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pieces.AbstractPiece;

@SuppressWarnings("serial")
public class ChessPanel extends JPanel {
	public static final Dimension PREFERRED_SIZE = new Dimension(90, 80); 
	private static final String BLACK_TILE_PATH = "src/resources/Black_Tile.png";
	private static final String WHITE_TILE_PATH = "src/resources/White_Tile.png";
	private static final ImageIcon BLACK_TILE_ICON = new ImageIcon(BLACK_TILE_PATH);
	private static final ImageIcon WHITE_TILE_ICON = new ImageIcon(WHITE_TILE_PATH);
	private ImageIcon myIcon;
	private AbstractPiece myPiece;
	private int myRow;
	private int myCol;
	
	public ChessPanel(int row, int col) {
		setLayout(new BorderLayout());
		setPreferredSize(PREFERRED_SIZE);
		setIcon(row, col);
		myRow = row;
		myCol = col;		
	}
	
	private ChessPanel(ChessPanel panel) {
		setLayout(new BorderLayout());
		myIcon = panel.myIcon;
		if (panel.myPiece != null) {myPiece = panel.myPiece.clone();}
		myRow = panel.myRow;
		myCol = panel.myCol;	
	}
	
	private void setIcon(int row, int col) {
		if ((row - col) % 2 == 0) {myIcon = WHITE_TILE_ICON;}
		else {myIcon = BLACK_TILE_ICON;}
	}
	
	@Override
	public ChessPanel clone() {
		return new ChessPanel(this);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(myIcon.getImage(), 0, 0, this);
	}
	
	@Override
	public Component add(Component component) {
		if (component instanceof AbstractPiece) {
			myPiece = (AbstractPiece) component;
		}
		return super.add(component);
	}
	
	public boolean isEmpty() {
		return myPiece == null;
	}	
	
	
	public AbstractPiece getPiece() {
		return myPiece;
	}
	
	public void removePiece() {
		if (myPiece != null) {remove(myPiece);}
		myPiece = null;
		
	}
}