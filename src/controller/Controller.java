package controller;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import pieces.AbstractPiece;
import pieces.King;
import pieces.Pawn;
import pieces.Queen;
import player.Player;
import view.View;
import board.ChessBoard;
import board.ChessPanel;

public class Controller {
	private static final String SAVE_DIRECTORY = "/Users/yankeenjg/Documents/workspace/Chess/src/saves";
	private static ChessBoard myBoard;
	private View myView;
	private Player[] myPlayers;
	private Player myCurrentPlayer;
	
	//game settings
	public static boolean highlightLegalMoves = true;
	
	public Controller() {
		myBoard = ChessBoard.getInstance();
		initializePlayers();
	}
	
	public void setView(View view) {
		myView = view;
	}
	
	private void initializePlayers() {
		myPlayers = new Player[] {new Player(true), new Player(false)};
		myCurrentPlayer = myPlayers[0];
	}
	
	public Player getCurrentPlayer() {
		return myCurrentPlayer;
	}
	
	private void nextTurn() {
		if (myPlayers[0] == myCurrentPlayer) {myCurrentPlayer = myPlayers[1];}
		else {myCurrentPlayer = myPlayers[0];}
		myBoard.saveState();
		
		if (inCheckMate()) {
			if (JOptionPane.showConfirmDialog(myView, "Check Mate! Play again?", "Game Over!", JOptionPane.YES_NO_OPTION) == 0) {
				newGame();
			}
			else {
				myView.dispose();
			}
		}
		else if (ChessBoard.inCheck(myBoard, myCurrentPlayer.isWhite())) {
			JOptionPane.showMessageDialog(myView, "Check!", "Check!", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	public void newGame() {
		initializePlayers();
		myBoard.reset();
	}
	
	public void undoMove() {
		myBoard.undoMove();
		nextTurn();
	}

	public boolean isValidMove(ChessBoard board, AbstractPiece piece, ChessPanel dropped) {
		ChessBoard copy = board.mockMove(board.getPanel(piece.getRow(), piece.getCol()), dropped);
		if (ChessBoard.inCheck(copy, myCurrentPlayer.isWhite())) {return false;}
		
		return piece.isLegalMove(board, board.getPanel(dropped.getRow(), dropped.getCol()));
	}

	public void makeMove(AbstractPiece piece, ChessPanel clickedPanel) {
		//initial pawn move of 2 panels
		if ((piece instanceof Pawn) && (clickedPanel.getRow() == 0 && piece.isWhite()) || (clickedPanel.getRow() == ChessBoard.BOARD_SIZE - 1 && !piece.isWhite())) {
			piece = new Queen(clickedPanel.getRow(), clickedPanel.getCol(), piece.isWhite());
		}
		//en passant
		else if ((piece instanceof Pawn) && ((Pawn) piece).isLegalEnPassantMove(myBoard, clickedPanel.getCol() - piece.getCol())) {
			myBoard.getPanel(piece.getRow(), clickedPanel.getCol()).removePiece();
		}	
		//castling
		else if (piece instanceof King && Math.abs(clickedPanel.getCol() - piece.getCol()) == 2) {
			ChessPanel rookPanel;
			AbstractPiece rook;
			if (clickedPanel.getCol() - piece.getCol() < 0) {
				rookPanel = myBoard.getPanel(piece.getRow(), 0);
				rook = rookPanel.getPiece();
				rookPanel.removePiece();
				myBoard.getPanel(piece.getRow(), clickedPanel.getCol() + 1).add(rook);
			}
			else {
				rookPanel = myBoard.getPanel(piece.getRow(), ChessBoard.BOARD_SIZE - 1);
				rook = rookPanel.getPiece();
				rookPanel.removePiece();
				myBoard.getPanel(piece.getRow(), clickedPanel.getCol() - 1).add(rook);
			}		
		}
		clickedPanel.add(piece);	
		nextTurn();		
	}

	public boolean inCheckMate() {
		if (!ChessBoard.inCheck(myBoard, myCurrentPlayer.isWhite())) {return false;}
		List<AbstractPiece> pieces = myBoard.findOpposingPieces(myBoard, !myCurrentPlayer.isWhite());
		
		for (AbstractPiece piece : pieces) {
			List<ChessPanel> legal = piece.getLegalMoves(myBoard);
			for (ChessPanel move : legal) {
				ChessBoard copy = myBoard.mockMove(myBoard.getPanel(piece.getRow(), piece.getCol()), move);
				if (!ChessBoard.inCheck(copy, myCurrentPlayer.isWhite())) {
					return false;
				}
			}
		}
		return true;	
	}
	
	public void highlightLegalMoves(AbstractPiece piece, boolean highlight) {
		ChessBoard copy = myBoard.clone();
		copy.getPanel(piece.getRow(), piece.getCol()).add(piece.clone());
		
		for (ChessPanel move : piece.getLegalMoves(myBoard)) {
			if (isValidMove(copy, piece, move)) {move.setHighlighted(highlight);}
		}
	}
	
	public void loadGame() {
		try {
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(SAVE_DIRECTORY));
			int load = chooser.showOpenDialog(myView);
			if (load != JFileChooser.APPROVE_OPTION) {return;}

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			System.out.println(chooser.getSelectedFile());
			Document doc = docBuilder.parse(chooser.getSelectedFile());
	
			
			doc.getDocumentElement().normalize();
			
			loadSettings(doc);
			myBoard.loadBoard(doc);
			
		
			
			
			JOptionPane.showMessageDialog(myView, "Load Successful!", "Load Successful!", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(myView, "Load Failed!", "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void loadSettings(Document doc) {
		Element root = doc.getDocumentElement();
		System.out.println(root);
		Element settings = (Element) root.getFirstChild();
		
		String highlight = settings.getAttribute("highlight");
		highlightLegalMoves = Boolean.parseBoolean(highlight);
		return;
	}
	
	public void saveGame() {
		try {
			
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(SAVE_DIRECTORY));
			int save = chooser.showSaveDialog(myView);
			
			if (save != JFileChooser.APPROVE_OPTION) {return;}
			String file = chooser.getSelectedFile() + ".xml";
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("game");
			
			doc.appendChild(root);
			root.appendChild(saveSettings(doc));
				
			root.appendChild(myBoard.saveBoard(doc));		
				
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(file));
			transformer.transform(source,  result);
			JOptionPane.showMessageDialog(myView, "File Saved!", "File Saved!", JOptionPane.INFORMATION_MESSAGE);
	
		} catch (Exception e) {
			JOptionPane.showMessageDialog(myView, "Save Failed!", "Error!", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();}
	}
	
	private Element saveSettings(Document doc) {
		Element settings = doc.createElement("settings");	
		settings.setAttribute("highlight", "true");

		settings.setAttribute("ID", "settings");
		settings.setIdAttribute("ID", true);
		return settings;
	}
	

}
