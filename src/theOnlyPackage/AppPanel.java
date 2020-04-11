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
import java.util.ConcurrentModificationException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
	public static BufferedImage infoImg;
	public static BufferedImage infoImgC;
	public static BufferedImage levelsImg;
	public static BufferedImage levelsImgC;
	public static BufferedImage donateImg;
	public static BufferedImage donateImgC;
	public static BufferedImage planetaryTextImg;
	public static BufferedImage quitTextImg;
	public static BufferedImage quitTextImgC;
	public static BufferedImage startTextImg;
	public static BufferedImage startTextImgC;
	public static BufferedImage titleImg;
	public static BufferedImage l1Text;
	public static BufferedImage l1TextC;
	public static BufferedImage textBubbleImg;
	public static BufferedImage xImg;
	public static BufferedImage tree1Img;
	public static BufferedImage tree1SelectedImg;
	public static BufferedImage tree2Img;
	public static BufferedImage tree2SelectedImg;
	public static BufferedImage tree3Img;
	public static BufferedImage tree3SelectedImg;
	public static BufferedImage factory1Img;
	public static BufferedImage factory1SelectedImg;
	public static BufferedImage factory2Img;
	public static BufferedImage factory2SelectedImg;
	public static BufferedImage factory3Img;
	public static BufferedImage factory3SelectedImg;
	public static BufferedImage graph1;
	public static BufferedImage aboutImg;
	public static BufferedImage aboutImgC;

	enum Transition {
		TITLESTART, DONATETITLE, INFOTITLE, NONE, STARTGAME, TITLEINFO, TITLEDONATE
	}

	enum State {
		TITLE, START, INFO, OPTIONS, GAME,
	}

	int treenum = 0;
	Font menuFont;
	Font others;
	Color planetaryColor = new Color(51, 153, 255);
	State currentState = State.TITLE;
	Transition transitions = Transition.NONE;
	long startTime;
	long elapsedTimeSec;
	long elapsedTime;
	ArrayList<Tree> trees;
	ArrayList<Factory> factories;
	ArrayList<GameButton> titleButtons;
	ArrayList<GameButton> infoButtons;
	ArrayList<GameButton> levelButtons;
	GameButton backBS;
	GameButton backBL;
	GameButton levelsB;
	GameButton comingSoonB;
	GameButton quitB;
	GameButton infoB;
	GameButton donateB;
	GameButton startB;
	GameButton l1B;
	GameButton xB;
	GameButton aboutB;
	double pollution;
	double money;
	int factorynum;
	int i = 1;
	int tRow = 1;
	int tposW = (Main.WIDTH / 10) * i;
	int tposH = (Main.HEIGHT / 4) * tRow;
	int fRow = 1;
	int fposW = (Main.WIDTH / 10) * i;
	int fposH = (Main.HEIGHT / 4) * tRow;
	Tree toReplaceT;
	Factory toReplaceF;
	boolean canReplaceTree = true;
	boolean isReplacingTree = false;
	boolean drawTextBubble;
	int bubblex;
	boolean treefirsttime = true;
	int makeFactoryDelay = 0;
	int makeTreeDelay = 0;
	boolean canReplaceFactory = true;
	boolean isReplacingFactory = false;
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService factoryMaker = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService treeMaker = Executors.newScheduledThreadPool(1);

	Runnable makeFactory = new Runnable() {
		public void run() {
			System.out.println(toReplaceT);

			makeFactoryDelay++;

			if (makeFactoryDelay >= 1000) {
				makeFactoryDelay = 0;
				money -= 100;
				factorynum++;
				replaceTree(toReplaceT);
				isReplacingTree = false;
				canReplaceTree = true;
				factoryMaker.shutdownNow();
			}
		}

	};
	Runnable makeTree = new Runnable() {
		public void run() {
			System.out.println(toReplaceF);

			makeTreeDelay++;

			if (makeTreeDelay >= 10000) {
				makeTreeDelay = 0;
				money -= 200;

				replaceFactory(toReplaceF);
				isReplacingFactory = false;
				canReplaceFactory = true;
				treeMaker.shutdownNow();
			}
		}

	};
	Runnable runnable = new Runnable() {
		public void run() {
			double pollutionRate = (factories.size() * 0.4) - (trees.size() * 0.1);

			money += (factories.size() * 5.2);
			if (pollution + pollutionRate < 0) {
				pollution = 0;
			} else {
				pollution += pollutionRate;
			}
		}
	};
	int bubbley;

	AppPanel() {

		try {
			titleImg = ImageIO.read(this.getClass().getResourceAsStream("TitlePic.png"));
			planetaryTextImg = ImageIO.read(this.getClass().getResourceAsStream("Planetary.png"));
			infoImg = ImageIO.read(this.getClass().getResourceAsStream("Info.png"));
			infoImgC = ImageIO.read(this.getClass().getResourceAsStream("InfoC.png"));
			quitTextImg = ImageIO.read(this.getClass().getResourceAsStream("Quit.png"));
			startTextImg = ImageIO.read(this.getClass().getResourceAsStream("Game.png"));
			startTextImgC = ImageIO.read(this.getClass().getResourceAsStream("GameC.png"));
			donateImg = ImageIO.read(this.getClass().getResourceAsStream("Donate.png"));
			donateImgC = ImageIO.read(this.getClass().getResourceAsStream("DonateC.png"));
			quitTextImgC = ImageIO.read(this.getClass().getResourceAsStream("QuitC.png"));
			backImg = ImageIO.read(this.getClass().getResourceAsStream("Back.png"));
			backImgC = ImageIO.read(this.getClass().getResourceAsStream("BackC.png"));
			levelsImg = ImageIO.read(this.getClass().getResourceAsStream("Levels.png"));
			levelsImgC = ImageIO.read(this.getClass().getResourceAsStream("LevelsC.png"));
			comingSoonImg = ImageIO.read(this.getClass().getResourceAsStream("Coming Soon....png"));
			comingSoonImgC = ImageIO.read(this.getClass().getResourceAsStream("Coming Soon...C.png"));
			l1Text = ImageIO.read(this.getClass().getResourceAsStream("Level 1.png"));
			textBubbleImg = ImageIO.read(this.getClass().getResourceAsStream("TextBubble.png"));
			xImg = ImageIO.read(this.getClass().getResourceAsStream("X.png"));
			tree1Img = ImageIO.read(this.getClass().getResourceAsStream("Tree1.png"));
			tree2Img = ImageIO.read(this.getClass().getResourceAsStream("Tree2.png"));
			tree3Img = ImageIO.read(this.getClass().getResourceAsStream("Tree3.png"));
			factory1Img = ImageIO.read(this.getClass().getResourceAsStream("Factory1.png"));
			factory2Img = ImageIO.read(this.getClass().getResourceAsStream("Factory2.png"));
			factory3Img = ImageIO.read(this.getClass().getResourceAsStream("Factory3.png"));
			tree1SelectedImg = ImageIO.read(this.getClass().getResourceAsStream("Tree1Glow.png"));
			tree2SelectedImg = ImageIO.read(this.getClass().getResourceAsStream("Tree2Glow.png"));
			tree3SelectedImg = ImageIO.read(this.getClass().getResourceAsStream("Tree3Glow.png"));
			factory1SelectedImg = ImageIO.read(this.getClass().getResourceAsStream("Factory1Glow.png"));
			factory2SelectedImg = ImageIO.read(this.getClass().getResourceAsStream("Factory2Glow.png"));
			factory3SelectedImg = ImageIO.read(this.getClass().getResourceAsStream("Factory3Glow.png"));
			graph1 = ImageIO.read(this.getClass().getResourceAsStream("graph1.png"));
			aboutImg = ImageIO.read(this.getClass().getResourceAsStream("About.png"));
			aboutImgC = ImageIO.read(this.getClass().getResourceAsStream("AboutC.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		t = new Timer(0, this);
		t.start();
		menuFont = new Font("Arial", Font.ITALIC, 30);

		this.startTime = System.currentTimeMillis();
		this.elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
		quitB = new GameButton(quitTextImg, quitTextImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 6, 112, 58, 112, 58);
		donateB = new GameButton(donateImg, donateImgC, Main.WIDTH , (Main.HEIGHT / 10) * 4, 191, 49, 191, 49);
		startB = new GameButton(startTextImg, startTextImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 4, 134, 51, 134, 51);
		infoB = new GameButton(infoImg, infoImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 5, 100, 51, 100, 51);
		backBL = new GameButton(backImg, backImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 4, 127, 51, 127, 51);
		backBS = new GameButton(backImg, backImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 5, 127, 51, 127, 51);
		levelsB = new GameButton(levelsImg, levelsImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 3, 172, 59, 172, 59);
		comingSoonB = new GameButton(comingSoonImg, comingSoonImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 4, 396, 70,
				396, 70);
		l1B = new GameButton(l1Text, l1TextC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 3, 180, 55, 180, 55);
		xB = new GameButton(xImg, xImg, (bubblex + 756 / 3) - (134 / 4), bubbley, 134 / 4, 143 / 4, 134 / 4, 143 / 4);
		aboutB = new GameButton(aboutImg, aboutImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 3, 164, 51, 164, 51);
		titleButtons = new ArrayList<GameButton>();
		titleButtons.add(infoB);
		titleButtons.add(quitB);
		titleButtons.add(startB);
		// titleButtons.add(donateB);
		titleButtons.add(aboutB);
		infoButtons = new ArrayList<GameButton>();
		infoButtons.add(donateB);
		infoButtons.add(backBS);
		levelButtons = new ArrayList<GameButton>();
		levelButtons.add(l1B);
		levelButtons.add(backBL);
		trees = new ArrayList<Tree>();
		factories = new ArrayList<Factory>();

		for (GameButton gb : levelButtons) {
			gb.setX(Main.WIDTH + gb.getWidth());
		}
		for (GameButton gb : infoButtons) {
			gb.setX(Main.WIDTH + gb.getWidth());
		}
		money = 700.00;
		pollution = 0.0;
		factorynum = 0;

		executor.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
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
		case STARTGAME:
			
			for (GameButton gb : titleButtons) {
				if (gb.getX() >= 0 - gb.getWidth()) {
					gb.setX(gb.getX() - wordMoveSpeed);
				}

			}

			break;
		case TITLEINFO:
			for (GameButton gb : titleButtons) {
				if (gb.getX() >= 0 - gb.getWidth()) {
					gb.setX(gb.getX() - wordMoveSpeed);
				}

			}
			for (GameButton gb : infoButtons) {
				if (gb.getX() >= Main.WIDTH/2) {
					gb.setX(gb.getX() - wordMoveSpeed);
				}

			}
//		case INFOTITLE:
//			for (GameButton gb : titleButtons) {
//				if (gb.getX() >= 0 - gb.getWidth()) {
//					gb.setX(gb.getX() - wordMoveSpeed);
//				}
//
//			}for (GameButton gb : infoButtons) {
//				if (gb.getX() >= 0 - gb.getWidth()) {
//					gb.setX(gb.getX() - wordMoveSpeed);
//				}
//
//			}
//			break;
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
		case INFO:
			drawGameSelectionState(g);
		default:
			break;
		}
	}

	void drawTitleMenuState(Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		g.drawImage(AppPanel.titleImg, 0, 0, Main.WIDTH, Main.HEIGHT, null);
		g.drawImage(AppPanel.planetaryTextImg, Main.WIDTH / 2, Main.HEIGHT / 10, 411, 109, null);

	}

	void drawGameSelectionState(Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		g.drawImage(AppPanel.titleImg, 0, 0, Main.WIDTH, Main.HEIGHT, null);
		g.drawImage(AppPanel.planetaryTextImg, Main.WIDTH / 2, Main.HEIGHT / 10, 411, 109, null);
		

	}

	void drawGameState(Graphics g) {
		g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		g.setFont(menuFont);
		g.setColor(Color.green);
		g.drawString("Trees: " + treenum, 40, 60);
		g.drawString("Money: " + money, 40, 30);
		g.drawString("Factories: " + factorynum, 40, 90);
		g.drawString("Pollution: " + pollution, 40, 120);
		try {
			for (Tree t : trees) {
				if (t.isSelected) {
					switch (t.type) {
					case 0:
						g.drawImage(tree1SelectedImg, t.x, t.y, t.width, t.height, null);
						break;
					case 1:
						g.drawImage(tree2SelectedImg, t.x, t.y, t.width, t.height, null);
						break;
					case 2:
						g.drawImage(tree3SelectedImg, t.x, t.y, t.width, t.height, null);
						break;

					default:
						break;
					}
				} else {
					switch (t.type) {
					case 0:
						g.drawImage(tree1Img, t.x, t.y, t.width, t.height, null);
						break;
					case 1:
						g.drawImage(tree2Img, t.x, t.y, t.width, t.height, null);
						break;
					case 2:
						g.drawImage(tree3Img, t.x, t.y, t.width, t.height, null);
						break;

					default:
						break;
					}

				}

			}
		} catch (ConcurrentModificationException h) {
			h.printStackTrace();
		}
		for (Factory f : factories) {
			if (f.isSelected) {

				switch (f.type) {
				case 0:
					f.width = (int) (125);
					f.height = (int) (124);
					g.drawImage(factory1SelectedImg, f.x, f.y, f.width, f.height, null);
					break;
				case 1:
					f.width = (int) (125);
					f.height = (int) (119);
					g.drawImage(factory2SelectedImg, f.x, f.y, f.width, f.height, null);
					break;
				case 2:
					f.width = (int) (139);
					f.height = (int) (143);
					g.drawImage(factory3SelectedImg, f.x, f.y, f.width, f.height, null);
					break;
				default:
					break;
				}
			} else {
				switch (f.type) {
				case 0:
					f.width = (int) (102);
					f.height = (int) (111);
					g.drawImage(factory1Img, f.x, f.y, f.width, f.height, null);
					break;
				case 1:
					f.width = (int) (105);
					f.height = (int) (114);
					g.drawImage(factory2Img, f.x, f.y, f.width, f.height, null);
					break;
				case 2:
					f.width = (int) (122);
					f.height = (int) (138);
					g.drawImage(factory3Img, f.x, f.y, f.width, f.height, null);
					break;
				default:
					break;
				}
			}
		}

		if (drawTextBubble) {
			g.drawImage(textBubbleImg, bubblex, bubbley, 756 / 3, 602 / 3, null);
			xB.draw(g);
		}

		drawTrees();
		if (isReplacingTree) {
			g.setColor(Color.green);

			g.fillRect((toReplaceT.x + (toReplaceT.width / 2)) - (1000 / 10), toReplaceT.y - (toReplaceT.height / 3),
					makeFactoryDelay / 5, 20);
			g.drawRect((toReplaceT.x + (toReplaceT.width / 2)) - (1000 / 10), toReplaceT.y - (toReplaceT.height / 3),
					1000 / 5, 20);
		}
		if (isReplacingFactory) {
			g.setColor(Color.blue);

			g.fillRect((toReplaceF.x + (toReplaceF.width / 2)) - (1000 / 10), toReplaceF.y - (toReplaceF.height / 3),
					makeTreeDelay / 50, 20);
			g.drawRect((toReplaceF.x + (toReplaceF.width / 2)) - (1000 / 10), toReplaceF.y - (toReplaceF.height / 3),
					10000 / 50, 20);
		}

	}

	void replaceTree(Tree t) {
		Random r = new Random();
		int type = r.nextInt(3);
		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).equals(t)) {
				factories.add(new Factory(t.x, t.y, 254 / 2, 278 / 2, type));
				trees.remove(i);
				System.out.println(factories.size());
				break;
			}
		}
	}

	void replaceFactory(Factory f) {
		Random r = new Random();
		int type = r.nextInt(3);
		for (int i = 0; i < factories.size(); i++) {
			if (factories.get(i).equals(f)) {
				switch (type) {
				case 0:
					trees.add(new Tree(f.x, f.y, 144 / 2, 351 / 2, 0));
					break;
				case 1:
					trees.add(new Tree(f.x, f.y, 306 / 2, 304 / 2, 1));
					break;
				case 2:
					trees.add(new Tree(f.x, f.y, 131 / 2, 312 / 2, 2));
					break;
				default:
					break;
				}
				factories.remove(i);
				System.out.println(trees.size());
				break;
			}
		}
	}

	private void drawTrees() {
		if (tRow <= 3) {

			if (i >= 10) {
				i = 1;
				tRow++;
			}
			Random r = new Random();
			int type = r.nextInt(3);
			tposW = (Main.WIDTH / 10) * i;
			tposH = (Main.HEIGHT / 4) * tRow;
			switch (type) {
			case 0:
				trees.add(new Tree(tposW, tposH, 144 / 2, 351 / 2, 0));
				break;
			case 1:
				trees.add(new Tree(tposW, tposH, 306 / 2, 304 / 2, 1));
				break;
			case 2:
				trees.add(new Tree(tposW, tposH, 131 / 2, 312 / 2, 2));
				break;
			default:
				break;
			}
			treenum += 1;
			i++;

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
		for (GameButton gb : infoButtons) {
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
		for (GameButton gb : infoButtons) {
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
		if (xB.getButtonBox().contains(MouseInfo.getPointerInfo().getLocation())) {
			xB.setSelected(true);
		} else {
			xB.setSelected(false);
		}

		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).box.contains(MouseInfo.getPointerInfo().getLocation())) {
				if (treefirsttime) {
					bubblex = (trees.get(i).x - 756 / 6) + (trees.get(i).width / 2);
					bubbley = trees.get(i).y - 602 / 3;
					drawTextBubble = true;
					treefirsttime = false;
					xB = new GameButton(xImg, xImg, (bubblex + 756 / 3) - (134 / 4), bubbley, 134 / 4, 143 / 4, 134 / 4,
							143 / 4);
				}
				trees.get(i).isSelected = true;
			} else {
				trees.get(i).isSelected = false;
			}
			for (int j = 0; j < factories.size(); j++) {
				if (factories.get(j).box.contains(MouseInfo.getPointerInfo().getLocation())) {
					factories.get(j).isSelected = true;
				} else {
					factories.get(j).isSelected = false;
				}
			}
			factorynum = factories.size();
			treenum = trees.size();

		}

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (quitB.isSelected()) {
				System.exit(0);
			}
			if (startB.isSelected()) {
				transitions = Transition.STARTGAME;
				currentState = State.GAME;

			}
			if(infoB.isSelected()) {
				transitions = Transition.TITLEINFO;
				currentState = State.INFO;
			}
			if (backBS.isSelected()) {
				transitions = Transition.INFOTITLE;
				currentState = State.TITLE;
			}
			if(aboutB.isSelected()) {
				transitions = Transition.TITLEINFO;
				currentState = State.INFO;
			}

			if (backBL.isSelected()) {
				// transitions = Transition.LEVELSSTART;
				currentState = State.START;
			}

			if (xB.isSelected()) {
				drawTextBubble = false;
			}
			for (Tree t : trees) {
				if (t.isSelected) {
					if (canReplaceTree) {
						factoryMaker = Executors.newScheduledThreadPool(1);
						toReplaceT = t;
						isReplacingTree = true;
						try {
							factoryMaker.scheduleAtFixedRate(makeFactory, 0, 1, TimeUnit.MILLISECONDS);
						} catch (RejectedExecutionException f) {
							f.printStackTrace();
						}
						canReplaceTree = false;
						break;
					}
				}
			}
			for (Factory f : factories) {
				if (f.isSelected) {
					if (canReplaceFactory) {
						treeMaker = Executors.newScheduledThreadPool(1);
						toReplaceF = f;
						isReplacingFactory = true;
						try {
							treeMaker.scheduleAtFixedRate(makeTree, 0, 1, TimeUnit.MILLISECONDS);
						} catch (RejectedExecutionException g) {
							g.printStackTrace();
						}
						canReplaceFactory = false;
						break;
					}
				}
			}

		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

}