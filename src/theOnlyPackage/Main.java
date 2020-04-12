package theOnlyPackage;

import javax.swing.JFrame;

public class Main {
	// change these to your screen size
	static final int HEIGHT = 900;
	static final int WIDTH = 1600;
	// variables here
	static JFrame f = new JFrame();
	static AppPanel p = new AppPanel();

	// main method
	public static void main(String[] args) {
		// put stuff here
		Main m = new Main();
		m.setup();
	}

	// method
	protected void setup() {
		// setup the frame
		// do other stuff idk
		// add the panel to the frame bc u have to
		f.add(p);
		// setup frame to look decent
		f.setUndecorated(true);
		//f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.addMouseListener(p);
		f.addKeyListener(p);
		// set the size of the frame
		f.setSize(WIDTH, HEIGHT);
		// when you press the x on the app it will end the code
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// make it so you can see the app
		f.setVisible(true);
		// you cannot change the size of the frame
		f.setResizable(false);

	}
}
