package main;

import view.View;
import controller.Controller;

public class Main {
	
	public static void main(String[] args) {
		Controller controller = new Controller();
		View display = new View(controller);
		controller.setView(display);
	}
}
