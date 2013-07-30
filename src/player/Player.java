package player;

public class Player {
	private boolean isWhite;
	
	public Player(boolean white) {
		isWhite = white;
	}
	
	public boolean isWhite() {
		return isWhite;
	}
	
	@Override
	public String toString() {
		if (isWhite) {return "White";}
		return "Black";
	}
}
