package theOnlyPackage;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AppPanel extends JPanel implements ActionListener, KeyListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static int wordMoveSpeed = 10;

	Timer t;
	public static BufferedImage backImg;
	public static BufferedImage backImgC;
	public static BufferedImage comingSoonImg;
	public static BufferedImage comingSoonImgC;
	public static BufferedImage helpTextImg;
	public static BufferedImage helpTextImgC;
	public static BufferedImage levelsImg;
	public static BufferedImage levelsImgC;
	public static BufferedImage optionsTextImg;
	public static BufferedImage optionsTextImgC;
	public static BufferedImage planetaryTextImg;
	public static BufferedImage quitTextImg;
	public static BufferedImage quitTextImgC;
	public static BufferedImage startTextImg;
	public static BufferedImage startTextImgC;
	public static BufferedImage titleImg;
	public static BufferedImage l1Text;
	public static BufferedImage l1TextC;

	enum Transition {
		TITLESTART, STARTTITLE, OPTIONSTITLE, HELPTITLE, NONE, STARTLEVELS, LEVELSL1, LEVELSSTART
	}

	enum State {
		TITLE, START, HELP, OPTIONS, GAME
	}

	int treenum = 1;
	Font menuFont;
	Font others;
	Color planetaryColor = new Color(51, 153, 255);
	State currentState = State.TITLE;
	Transition transitions = Transition.NONE;
	long startTime;
	long elapsedTimeSec;
	long elapsedTime;
	ArrayList<Tree> trees;
	ArrayList<GameButton> titleButtons;
	ArrayList<GameButton> gameSelectionButtons;
	ArrayList<GameButton> levelButtons;
	GameButton backBS;
	GameButton backBL;
	GameButton levelsB;
	GameButton comingSoonB;
	GameButton quitB;
	GameButton helpB;
	GameButton optionsB;
	GameButton startB;
	GameButton l1B;
	double pollution;
	double money;
	double factorynum;
	AppPanel() {

		try {
			titleImg = ImageIO.read(this.getClass().getResourceAsStream("TitlePicture.jpg"));
			planetaryTextImg = ImageIO.read(this.getClass().getResourceAsStream("Planetary.png"));
			helpTextImg = ImageIO.read(this.getClass().getResourceAsStream("Help.png"));
			optionsTextImg = ImageIO.read(this.getClass().getResourceAsStream("Options.png"));
			quitTextImg = ImageIO.read(this.getClass().getResourceAsStream("Quit.png"));
			startTextImg = ImageIO.read(this.getClass().getResourceAsStream("Start.png"));
			startTextImgC = ImageIO.read(this.getClass().getResourceAsStream("StartC.png"));
			helpTextImgC = ImageIO.read(this.getClass().getResourceAsStream("HelpC.png"));
			optionsTextImgC = ImageIO.read(this.getClass().getResourceAsStream("OptionsC.png"));
			quitTextImgC = ImageIO.read(this.getClass().getResourceAsStream("QuitC.png"));
			backImg = ImageIO.read(this.getClass().getResourceAsStream("Back.png"));
			backImgC = ImageIO.read(this.getClass().getResourceAsStream("BackC.png"));
			levelsImg = ImageIO.read(this.getClass().getResourceAsStream("Levels.png"));
			levelsImgC = ImageIO.read(this.getClass().getResourceAsStream("LevelsC.png"));
			comingSoonImg = ImageIO.read(this.getClass().getResourceAsStream("Coming Soon....png"));
			comingSoonImgC = ImageIO.read(this.getClass().getResourceAsStream("Coming Soon...C.png"));
			l1Text = ImageIO.read(this.getClass().getResourceAsStream("Level 1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		t = new Timer(0, this);
		t.start();
		menuFont = new Font("Arial", Font.ITALIC, 80);

		this.startTime = System.currentTimeMillis();
		this.elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
		quitB = new GameButton(quitTextImg, quitTextImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 6, 115, 75, 123, 68);
		helpB = new GameButton(helpTextImg, helpTextImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 4, 123, 75, 139, 85);
		startB = new GameButton(startTextImg, startTextImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 3, 139, 75, 154, 83);
		optionsB = new GameButton(optionsTextImg, optionsTextImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 5, 214, 75, 226,
				75);
		backBL = new GameButton(backImg, backImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 4, 132, 55, 132, 55);
		backBS = new GameButton(backImg, backImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 5, 132, 55, 132, 55);
		levelsB = new GameButton(levelsImg, levelsImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 3, 172, 59, 172, 59);
		comingSoonB = new GameButton(comingSoonImg, comingSoonImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 4, 396, 70,
				396, 70);
		l1B = new GameButton(l1Text, l1TextC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 3, 180, 55, 180, 55);
		titleButtons = new ArrayList<GameButton>();
		titleButtons.add(helpB);
		titleButtons.add(quitB);
		titleButtons.add(startB);
		titleButtons.add(optionsB);
		gameSelectionButtons = new ArrayList<GameButton>();
		gameSelectionButtons.add(levelsB);
		gameSelectionButtons.add(backBS);
		gameSelectionButtons.add(comingSoonB);
		levelButtons = new ArrayList<GameButton>();
		levelButtons.add(l1B);
		levelButtons.add(backBL);
		trees = new ArrayList<Tree>();
		for (GameButton gb : levelButtons) {
			gb.setX(Main.WIDTH + gb.getWidth());
		}
		for (GameButton gb : gameSelectionButtons) {
			gb.setX(Main.WIDTH + gb.getWidth());
		}
		money = 500.0;
		pollution = 0.0;
		factorynum = 0.0;
		repaint();

	}

	@Override
	public void paintComponent(Graphics g) {
		drawScreen(g);
		drawButtons(g);
		repaint();
	}

	private void drawScreen(Graphics g) {
		switch (transitions) {
		case TITLESTART:
			for (GameButton gb : gameSelectionButtons) {
				if (gb.getX() >= Main.WIDTH / 2) {
					gb.setX(gb.getX() - wordMoveSpeed);
				}

			}
			for (GameButton gb : titleButtons) {
				if (gb.getX() >= 0 - gb.getWidth()) {
					gb.setX(gb.getX() - wordMoveSpeed);
				}

			}

			break;
		case STARTTITLE:
			for (GameButton gb : gameSelectionButtons) {
				if (gb.getX() < Main.WIDTH + gb.getWidth()) {
					gb.setX(gb.getX() + wordMoveSpeed);
				}
			}
			for (GameButton gb : titleButtons) {
				if (gb.getX() <= Main.WIDTH / 2) {
					gb.setX(gb.getX() + wordMoveSpeed);
				}
			}

			break;
		case STARTLEVELS:
			for (GameButton gb : gameSelectionButtons) {
				if (gb.getX() > 0 - gb.getWidth()) {
					gb.setX(gb.getX() - wordMoveSpeed);
				}
			}
//			for (GameButton gb : levelButtons) {
//				if (gb.getX() >= Main.WIDTH / 2) {
//					gb.setX(gb.getX() - wordMoveSpeed);
//				}
//			}

			break;
		default:
			break;
		}
		switch (currentState) {
		case TITLE:
			drawTitleMenuState(g);
			break;
		case START:
			drawGameSelectionState(g);
			break;
		case GAME:
			drawGameState(g);
			break;
		default:
			break;
		}
	}

	void drawTitleMenuState(Graphics g) {
		g.drawImage(AppPanel.titleImg, 0, 0, Main.WIDTH, Main.HEIGHT, null);
		g.drawImage(AppPanel.planetaryTextImg, Main.WIDTH / 2, Main.HEIGHT / 10, 411, 109, null);

	}

	void drawGameSelectionState(Graphics g) {

		g.drawImage(AppPanel.titleImg, 0, 0, Main.WIDTH, Main.HEIGHT, null);
		g.drawImage(AppPanel.planetaryTextImg, Main.WIDTH / 2, Main.HEIGHT / 10, 411, 109, null);

	}

	void drawGameState(Graphics g) {
		g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		g.setFont(menuFont);
		String treesS = Integer.toString(treenum);
		g.setColor(Color.green);
		g.drawString("Trees: " + treesS ,20 , 50);

		for (Tree t : trees) {
			g.setColor(Color.green);
			g.fill3DRect(t.x, t.y, t.width, t.height, true);
		}

	}

	private void drawButtons(Graphics g) {
		for (GameButton gb : titleButtons) {
			if (gb.isSelected()) {
				gb.drawSelected(g);
			} else {
				gb.draw(g);
			}
		}
		for (GameButton gb : gameSelectionButtons) {
			if (gb.isSelected()) {
				gb.drawSelected(g);
			} else {
				gb.draw(g);
			}

		}
		for (GameButton gb : levelButtons) {
			if (gb.isSelected()) {
				gb.drawSelected(g);
			} else {
				gb.draw(g);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();

		this.elapsedTimeSec = (System.currentTimeMillis() - startTime) / 1000;
		this.elapsedTime = (System.currentTimeMillis() - startTime);
		for (GameButton gb : titleButtons) {
			if (gb.getButtonBox().contains(MouseInfo.getPointerInfo().getLocation())) {
				gb.setSelected(true);
			} else {
				gb.setSelected(false);
			}
			gb.update();
		}
		for (GameButton gb : gameSelectionButtons) {
			if (gb.getButtonBox().contains(MouseInfo.getPointerInfo().getLocation())) {
				gb.setSelected(true);
			} else {
				gb.setSelected(false);
			}
			gb.update();
		}
		for (GameButton gb : levelButtons) {
			if (gb.getButtonBox().contains(MouseInfo.getPointerInfo().getLocation())) {
				gb.setSelected(true);
			} else {
				gb.setSelected(false);
			}
			gb.update();
		}

		if (currentState == State.GAME) {
			
			if (elapsedTimeSec == treenum *5 ) {
				trees.add(new Tree((Main.WIDTH / 10) * treenum, Main.HEIGHT / 2, 20, 100));
				treenum += 1;


			} 
			

		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (quitB.isSelected()) {
				System.exit(0);
			}
			if (startB.isSelected()) {
				transitions = Transition.TITLESTART;
				currentState = State.START;

			}
			if (backBS.isSelected()) {
				transitions = Transition.STARTTITLE;
				currentState = State.TITLE;
			}

			if (backBL.isSelected()) {
				transitions = Transition.LEVELSSTART;
				currentState = State.START;
			}
			if (levelsB.isSelected()) {
				transitions = Transition.STARTLEVELS;
				currentState = State.GAME;
				
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}