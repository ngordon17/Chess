package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import controller.Controller;

@SuppressWarnings("serial")
public class ChessMenuBar extends JMenuBar {	
	private Controller myController;
	
	public ChessMenuBar(Controller controller) {
		myController = controller;
		
		initFileMenu();
		initMovesMenu();
		initSettingsMenu();
	}
	
	private void initFileMenu() {
		JMenu fileMenu = new JMenu("File");
		
		JMenuItem newGame = new JMenuItem("New Game");
		newGame.addActionListener(new NewGameAction());
		newGame.setEnabled(true);
		fileMenu.add(newGame);
		
		JMenuItem saveGame = new JMenuItem("Save");
		saveGame.addActionListener(new SaveGameAction());
		saveGame.setEnabled(true);
		fileMenu.add(saveGame);
		
		add(fileMenu);		
	}
	
	private void initMovesMenu() {
		JMenu editMenu = new JMenu("Moves");
		
		JMenuItem undoMove = new JMenuItem ("Undo Move");
		undoMove.addActionListener(new UndoMoveAction());
		undoMove.setEnabled(true);
		editMenu.add(undoMove);
	
		add(editMenu);
	}
	
	private void initSettingsMenu() {
		JMenu settingsMenu = new JMenu("Settings");
		
		JMenu highlightMoves = new JMenu("Highlight Legal Moves");
		
		JMenuItem on = new JMenuItem("On");
		on.addActionListener(new HighlightMovesAction(true));
		highlightMoves.add(on);
		on.setEnabled(true);
		
		JMenuItem off = new JMenuItem("Off");
		off.addActionListener(new HighlightMovesAction(false));
		highlightMoves.add(off);
		off.setEnabled(true);
		
		settingsMenu.add(highlightMoves);
		highlightMoves.setEnabled(true);
		
		add(settingsMenu);	
	}

	private class NewGameAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			myController.newGame();
		}
	}
	
	private class SaveGameAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			myController.saveGame();
		}
	}

	private class UndoMoveAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			myController.undoMove();
		}
	}
	
	private class HighlightMovesAction implements ActionListener {
		private boolean isEnabled = false;
		
		public HighlightMovesAction(boolean enabled) {
			isEnabled = enabled;
		}
		
		public void actionPerformed(ActionEvent e) {
			Controller.highlightLegalMoves = isEnabled;
		}
	}
}
