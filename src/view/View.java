package view;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import board.ChessBoard;
import board.ChessBoardMouseAdapter;
import controller.Controller;

@SuppressWarnings("serial")
public class View extends JFrame {	
	private static final String VIEW_TITLE = "Chess by Nick Gordon";
	private static final String LOGO_PATH = "src/resources/Chess_Logo.png";
	private Controller myController;
	private ChessMenuBar myMenuBar;
	
	public View(Controller controller) {
		myController = controller;
		setTitle(VIEW_TITLE);
		setIconImage(new ImageIcon(LOGO_PATH).getImage());
		setLayout(new BorderLayout());
		
		initMenuBars();
		initPanels();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		pack();	
	}
	
	private void initMenuBars() {
		myMenuBar = new ChessMenuBar(myController);
		setJMenuBar(myMenuBar);
	}
	
	private void initPanels() {
		add(makeBoard(), BorderLayout.CENTER);
	}
	
	private JLayeredPane makeBoard() {
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(ChessBoard.getInstance());
		layeredPane.add(scrollPane);
		
		ChessBoardMouseAdapter adapter = new ChessBoardMouseAdapter(myController, layeredPane);
		layeredPane.addMouseListener(adapter);
		layeredPane.addMouseMotionListener(adapter);
		scrollPane.addMouseListener(adapter);
		scrollPane.addMouseMotionListener(adapter);
		
		return layeredPane;
	}
}
