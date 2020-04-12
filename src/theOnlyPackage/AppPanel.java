package theOnlyPackage;

import java.awt.Color;
import java.awt.Desktop;
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
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
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

	// img declaration
	public static BufferedImage backImg;
	public static BufferedImage backImgC;
	public static BufferedImage infoImg;
	public static BufferedImage infoImgC;
	public static BufferedImage donateImg;
	public static BufferedImage donateImgC;
	public static BufferedImage planetaryTextImg;
	public static BufferedImage quitTextImg;
	public static BufferedImage quitTextImgC;
	public static BufferedImage startTextImg;
	public static BufferedImage startTextImgC;
	public static BufferedImage titleImg;
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
	public static BufferedImage teamTreesImg;
	public static BufferedImage teamTreesImgC;
	public static BufferedImage rainforrestWebImg;
	public static BufferedImage rainforrestWebImgC;
	public static BufferedImage oneTreeImg;
	public static BufferedImage oneTreeImgC;
	public static BufferedImage helpMessageImgDonate;
	public static BufferedImage plantForThePlanetImg;
	public static BufferedImage plantForThePlanetImgC;
	public static BufferedImage natureConImg;
	public static BufferedImage natureConImgC;
	public static BufferedImage infoTextImg;
	public static BufferedImage logoImg;
	public static BufferedImage AboutL1Img;
	public static BufferedImage AboutL2Img;
	public static BufferedImage AboutL3Img;
	public static BufferedImage AboutL4Img;
	public static BufferedImage AboutL5Img;
	public static BufferedImage AboutL6Img;
	public static BufferedImage AboutL7Img;
	public static BufferedImage AboutL8Img;
	public static BufferedImage AboutL9Img;
	public static BufferedImage AboutL10Img;
	public static BufferedImage AboutL11Img;
	public static BufferedImage AboutL12Img;
	public static BufferedImage howToPlayImg;
	public static BufferedImage howToPlayImgC;

	// other variables declaration
	Timer t;

	enum Transition {
		TITLESTART, DONATETITLE, INFOTITLE, NONE, STARTGAME, TITLEINFO, TITLEDONATE, GAMEGAMEOVER, TITLEABOUT,
		INFODONATE, DONATEINFO, GAMEGAMEWIN, ABOUTTITLE
	}

	enum State {
		TITLE, START, INFO, OPTIONS, GAME, GAMEOVER, DONATE, ABOUT
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
	ArrayList<GameButton> donateButtons;
	ArrayList<GameButton> aboutButtons;
	GameButton howToPlayB;
	GameButton backBS;
	GameButton backBL;
	GameButton backBA;
	GameButton quitB;
	GameButton infoB;
	GameButton donateB;
	GameButton startB;
	GameButton aboutB;
	GameButton teamtreesB;
	GameButton rainforrestWebB;
	GameButton natureConB;
	GameButton donationMessageB;
	GameButton plantForThePlanetB;
	GameButton oneTreePlantedB;
	GameButton infoTextB;
	GameButton graphB;
	GameButton logoB;
	GameButton aboutB1;
	GameButton aboutB2;
	GameButton aboutB3;
	GameButton aboutB4;
	GameButton aboutB5;
	GameButton aboutB6;
	GameButton aboutB7;
	GameButton aboutB8;
	GameButton aboutB9;
	GameButton aboutB10;
	GameButton aboutB11;
	GameButton aboutB12;
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
	boolean treefirsttime = true;
	int makeFactoryDelay = 0;
	int makeTreeDelay = 0;
	boolean canReplaceFactory = true;
	boolean isReplacingFactory = false;
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService factoryMaker = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService treeMaker = Executors.newScheduledThreadPool(1);
	boolean gameOver = false;
	boolean gameWin = false;
	boolean canPlay = true;
	Runnable makeFactory = new Runnable() {
		public void run() {

			makeFactoryDelay++;

			if (makeFactoryDelay >= 1000) {

				makeFactoryDelay = 0;
				money -= 50700;
				replaceTree(toReplaceT);
				isReplacingTree = false;
				canReplaceTree = true;
				factoryMaker.shutdown();
			}
		}

	};
	Runnable makeTree = new Runnable() {
		public void run() {

			makeTreeDelay++;

			if (makeTreeDelay >= 10000) {

				makeTreeDelay = 0;
				money -= 20050;
				replaceFactory(toReplaceF);
				isReplacingFactory = false;
				canReplaceFactory = true;
				treeMaker.shutdown();
			}
		}
	};
	Runnable runnable = new Runnable() {
		public void run() {
			double pollutionRate = (factories.size() * 5.4) - (trees.size() * 3.4);

			money += (factories.size() * 2500);
			if (pollution + pollutionRate < 0) {
				pollution = 0;
			} else {
				pollution += pollutionRate;
			}
		}
	};

	AppPanel() {
		// initialization of variables
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
			teamTreesImg = ImageIO.read(this.getClass().getResourceAsStream("TeamTrees.org.png"));
			teamTreesImgC = ImageIO.read(this.getClass().getResourceAsStream("TeamTrees.orgC.png"));
			rainforrestWebImg = ImageIO.read(this.getClass().getResourceAsStream("Rainforest Action Network.png"));
			rainforrestWebImgC = ImageIO.read(this.getClass().getResourceAsStream("Rainforest Action NetworkC.png"));
			oneTreeImg = ImageIO.read(this.getClass().getResourceAsStream("One Tree Planted.png"));
			oneTreeImgC = ImageIO.read(this.getClass().getResourceAsStream("One Tree PlantedC.png"));
			helpMessageImgDonate = ImageIO.read(this.getClass().getResourceAsStream("InfoText.png"));
			plantForThePlanetImg = ImageIO.read(this.getClass().getResourceAsStream("Plant For The Planet.png"));
			plantForThePlanetImgC = ImageIO.read(this.getClass().getResourceAsStream("Plant For The PlanetC.png"));
			natureConImg = ImageIO.read(this.getClass().getResourceAsStream("The Nature Conservancy.png"));
			natureConImgC = ImageIO.read(this.getClass().getResourceAsStream("The Nature ConservancyC.png"));
			infoTextImg = ImageIO.read(this.getClass().getResourceAsStream("donationInfo.png"));
			logoImg = ImageIO.read(this.getClass().getResourceAsStream("logo.png"));
			AboutL1Img = ImageIO.read(this.getClass().getResourceAsStream("AboutL1.png"));
			AboutL2Img = ImageIO.read(this.getClass().getResourceAsStream("AboutL2.png"));
			AboutL3Img = ImageIO.read(this.getClass().getResourceAsStream("AboutL3.png"));
			AboutL4Img = ImageIO.read(this.getClass().getResourceAsStream("AboutL4.png"));
			AboutL5Img = ImageIO.read(this.getClass().getResourceAsStream("AboutL5.png"));
			AboutL6Img = ImageIO.read(this.getClass().getResourceAsStream("AboutL6.png"));
			AboutL7Img = ImageIO.read(this.getClass().getResourceAsStream("AboutL7.png"));
			AboutL8Img = ImageIO.read(this.getClass().getResourceAsStream("AboutL8.png"));
			AboutL9Img = ImageIO.read(this.getClass().getResourceAsStream("AboutL9.png"));
			AboutL10Img = ImageIO.read(this.getClass().getResourceAsStream("AboutL10.png"));
			AboutL11Img = ImageIO.read(this.getClass().getResourceAsStream("AboutL11.png"));
			AboutL12Img = ImageIO.read(this.getClass().getResourceAsStream("AboutL12.png"));
			howToPlayImg = ImageIO.read(this.getClass().getResourceAsStream("How To Play.png"));
			howToPlayImgC = ImageIO.read(this.getClass().getResourceAsStream("How To PlayC.png"));

		} catch (IOException e) {
			// if any ioExceptions were found
			e.printStackTrace();
		}
		t = new Timer(0, this);
		t.start();
		menuFont = new Font("Arial", Font.ITALIC, 30);

		this.startTime = System.currentTimeMillis();
		this.elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
		quitB = new GameButton(quitTextImg, quitTextImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 6, 112, 58, 112, 58);
		donateB = new GameButton(donateImg, donateImgC, Main.WIDTH, (Main.HEIGHT / 10) * 7, 191, 49, 191, 49);
		startB = new GameButton(startTextImg, startTextImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 4, 134, 51, 134, 51);
		infoB = new GameButton(infoImg, infoImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 5, 100, 51, 100, 51);
		backBL = new GameButton(backImg, backImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 7, 127, 51, 127, 51);
		backBS = new GameButton(backImg, backImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 8, 127, 51, 127, 51);
		backBL = new GameButton(backImg, backImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 7, 127, 51, 127, 51);
		backBA = new GameButton(backImg, backImgC, (Main.WIDTH / 15) * 10, (Main.HEIGHT / 15) * 14, 127, 51, 127, 51);
		howToPlayB = new GameButton(howToPlayImg, howToPlayImgC, (Main.WIDTH / 2), (Main.HEIGHT / 15), 327, 64, 327,
				64);
		aboutB = new GameButton(aboutImg, aboutImgC, Main.WIDTH / 2, (Main.HEIGHT / 10) * 3, 164, 51, 164, 51);
		teamtreesB = new GameButton(teamTreesImg, teamTreesImgC, Main.WIDTH, Main.HEIGHT / 10 * 4, 387, 64, 387, 64);
		plantForThePlanetB = new GameButton(plantForThePlanetImg, plantForThePlanetImgC, Main.WIDTH,
				Main.HEIGHT / 10 * 6, 545, 52, 545, 52);
		oneTreePlantedB = new GameButton(oneTreeImg, oneTreeImgC, Main.WIDTH, Main.HEIGHT / 10 * 2, 455, 51, 455, 51);
		natureConB = new GameButton(natureConImg, natureConImgC, Main.WIDTH, Main.HEIGHT / 10 * 3, 658, 64, 658, 64);
		rainforrestWebB = new GameButton(rainforrestWebImg, rainforrestWebImgC, Main.WIDTH, Main.HEIGHT / 10 * 5, 708,
				51, 708, 51);
		donationMessageB = new GameButton(helpMessageImgDonate, helpMessageImgDonate, Main.WIDTH, Main.HEIGHT / 10 * 4,
				747, 146, 747, 146);
		infoTextB = new GameButton(infoTextImg, infoTextImg, Main.WIDTH, Main.HEIGHT / 10 * 2, 936, 145, 936, 145);
		graphB = new GameButton(graph1, graph1, ((Main.WIDTH / 10) * 9) - 569, ((Main.HEIGHT / 10) * 6), 569, 339, 569,
				339);
		logoB = new GameButton(logoImg, logoImg, Main.WIDTH - 182, 0, 182, 183, 182, 183);
		aboutB1 = new GameButton(AboutL1Img, AboutL1Img, Main.WIDTH, (Main.HEIGHT / 15) * 3, 1209, 41, 1209, 41);
		aboutB2 = new GameButton(AboutL2Img, AboutL2Img, Main.WIDTH, (Main.HEIGHT / 15) * 4, 1220, 41, 1220, 41);
		aboutB3 = new GameButton(AboutL3Img, AboutL3Img, Main.WIDTH, (Main.HEIGHT / 15) * 5, 1217, 41, 1217, 41);
		aboutB4 = new GameButton(AboutL4Img, AboutL4Img, Main.WIDTH, (Main.HEIGHT / 15) * 6, 1209, 41, 1209, 41);
		aboutB5 = new GameButton(AboutL5Img, AboutL5Img, Main.WIDTH, (Main.HEIGHT / 15) * 7, 1209, 41, 1209, 41);
		aboutB6 = new GameButton(AboutL6Img, AboutL6Img, Main.WIDTH, (Main.HEIGHT / 15) * 8, 1209, 41, 1209, 41);
		aboutB7 = new GameButton(AboutL7Img, AboutL7Img, Main.WIDTH, (Main.HEIGHT / 15) * 9, 1209, 41, 1209, 41);
		aboutB8 = new GameButton(AboutL8Img, AboutL8Img, Main.WIDTH, (Main.HEIGHT / 15) * 10, 1209, 41, 1209, 41);
		aboutB9 = new GameButton(AboutL9Img, AboutL9Img, Main.WIDTH, (Main.HEIGHT / 15) * 11, 1209, 41, 1209, 41);
		aboutB10 = new GameButton(AboutL10Img, AboutL10Img, Main.WIDTH, (Main.HEIGHT / 15) * 12, 1209, 41, 1209, 41);
		aboutB11 = new GameButton(AboutL11Img, AboutL11Img, Main.WIDTH, (Main.HEIGHT / 15) * 13, 1209, 41, 1209, 41);
		aboutB12 = new GameButton(AboutL12Img, AboutL12Img, Main.WIDTH, (Main.HEIGHT / 15) * 14, 428, 41, 428, 41);

		titleButtons = new ArrayList<GameButton>();
		titleButtons.add(infoB);
		titleButtons.add(quitB);
		titleButtons.add(startB);
		titleButtons.add(aboutB);
		donateButtons = new ArrayList<GameButton>();
		donateButtons.add(teamtreesB);
		donateButtons.add(rainforrestWebB);
		donateButtons.add(backBL);
		donateButtons.add(plantForThePlanetB);
		donateButtons.add(natureConB);
		donateButtons.add(oneTreePlantedB);
		infoButtons = new ArrayList<GameButton>();
		infoButtons.add(infoTextB);
		infoButtons.add(donateB);
		infoButtons.add(backBS);
		infoButtons.add(graphB);
		infoButtons.add(donationMessageB);
		aboutButtons = new ArrayList<GameButton>();
		aboutButtons.add(aboutB1);
		aboutButtons.add(aboutB2);
		aboutButtons.add(aboutB3);
		aboutButtons.add(aboutB4);
		aboutButtons.add(aboutB5);
		aboutButtons.add(aboutB6);
		aboutButtons.add(aboutB7);
		aboutButtons.add(aboutB8);
		aboutButtons.add(aboutB9);
		aboutButtons.add(aboutB10);
		aboutButtons.add(aboutB11);
		aboutButtons.add(aboutB12);
		aboutButtons.add(backBA);

		trees = new ArrayList<Tree>();
		factories = new ArrayList<Factory>();
		// setting buttons location to off the screen
		for (GameButton gb : infoButtons) {
			gb.setX(Main.WIDTH + gb.getWidth());
		}
		for (GameButton gb : donateButtons) {
			gb.setX(Main.WIDTH + gb.getWidth());
		}
		for (GameButton gb : aboutButtons) {
			gb.setX(Main.WIDTH + gb.getWidth());
		}

		money = 100000.00;
		pollution = 0.0;
		factorynum = 0;
		// start to repeat the runnable every second
		executor.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.SECONDS);
		repaint();

	}

	// draw stuff
	@Override
	public void paintComponent(Graphics g) {
		drawScreen(g);
		drawButtons(g);
		logoB.draw(g);
		repaint();
	}

	private void drawScreen(Graphics g) {
		// move buttons onto the screen
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
				if (gb.equals(graphB)) {
					if (gb.getX() >= ((Main.WIDTH / 2) + donateB.getWidth() + 35)) {
						gb.setX(gb.getX() - wordMoveSpeed);
					}
				} else {
					if (gb.getX() >= Main.WIDTH / 2) {
						gb.setX(gb.getX() - wordMoveSpeed);
					}
				}

			}

			break;
		case TITLEABOUT:
			for (GameButton gb : titleButtons) {
				if (gb.getX() >= 0 - gb.getWidth()) {
					gb.setX(gb.getX() - wordMoveSpeed);
				}
			}
			for (GameButton gb : aboutButtons) {
				if (gb.equals(backBA)) {
					if (gb.getX() >= (Main.WIDTH / 15) * 13) {
						gb.setX(gb.getX() - wordMoveSpeed);
					}
				} else {
					if (gb.getX() >= (Main.WIDTH / 15) * 5) {
						gb.setX(gb.getX() - wordMoveSpeed);
					}
				}
			}

			break;
		case ABOUTTITLE:
			for (GameButton gb : titleButtons) {

				if (gb.getX() <= Main.WIDTH / 2) {
					gb.setX(gb.getX() + wordMoveSpeed);
				}

			}

			for (GameButton gb : aboutButtons) {

				if (gb.getX() <= Main.WIDTH + gb.getWidth()) {
					gb.setX(gb.getX() + wordMoveSpeed);
				}

			}
			break;
		case INFOTITLE:
			for (GameButton gb : titleButtons) {

				if (gb.getX() <= Main.WIDTH / 2) {
					gb.setX(gb.getX() + wordMoveSpeed);
				}

			}

			for (GameButton gb : infoButtons) {

				if (gb.getX() <= Main.WIDTH + gb.getWidth()) {
					gb.setX(gb.getX() + wordMoveSpeed);
				}

			}

			break;

		case GAMEGAMEOVER:
			for (GameButton gb : titleButtons) {

				if (gb.getX() <= Main.WIDTH / 2) {
					gb.setX(gb.getX() + wordMoveSpeed);
				}
			}

			break;
		case GAMEGAMEWIN:
			for (GameButton gb : titleButtons) {

				if (gb.getX() <= Main.WIDTH / 2) {
					gb.setX(gb.getX() + wordMoveSpeed);
				}
			}

			break;
		case INFODONATE:

			for (GameButton gb : infoButtons) {
				if (gb.getX() >= 0 - gb.getWidth()) {

					gb.setX(gb.getX() - wordMoveSpeed);
				}

			}
			for (GameButton gb : donateButtons) {

				if (gb.getX() >= Main.WIDTH / 2) {
					gb.setX(gb.getX() - wordMoveSpeed);

				}
			}

			break;
		case DONATEINFO:
			for (GameButton gb : donateButtons) {
				if (gb.getX() <= Main.WIDTH + gb.getWidth()) {
					gb.setX(gb.getX() + wordMoveSpeed);
				}

			}
			for (GameButton gb : infoButtons) {
				if (gb.equals(graphB)) {
					if (gb.getX() <= ((Main.WIDTH / 2) + donateB.getWidth() + 35)) {
						gb.setX(gb.getX() + wordMoveSpeed);
					}
				} else {
					if (gb.getX() <= Main.WIDTH / 2) {
						gb.setX(gb.getX() + wordMoveSpeed);
					}

				}
			}
			break;

		default:
			break;
		}
		// draw each state
		switch (currentState) {
		case TITLE:
			drawTitleMenuState(g);
			break;
		case START:
			drawSelectionState(g);
			break;
		case GAME:
			drawGameState(g);
			break;
		case INFO:
			drawSelectionState(g);
			break;
		case DONATE:
			drawSelectionState(g);
			break;
		case ABOUT:
			drawSelectionState(g);
			break;
		default:
			break;
		}
	}

	void drawTitleMenuState(Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		g.drawImage(AppPanel.titleImg, 0, 0, Main.WIDTH, Main.HEIGHT, null);
		g.drawImage(AppPanel.planetaryTextImg, (Main.WIDTH / 2), Main.HEIGHT / 10, 411, 109, null);
		if (!canPlay) {
			if (money >= 1000000) {
				g.setFont(menuFont);
				g.setColor(Color.orange);
				g.drawString("You earned 1,000,000 money while keeping the pollution", Main.WIDTH / 2,
						(Main.HEIGHT / 15) * 12);
				g.drawString("rates low enough to stay healthy. Nice job!", Main.WIDTH / 2, (Main.HEIGHT / 15) * 13);

			}
			if (money < 0) {
				g.setFont(menuFont);
				g.setColor(Color.orange);
				g.drawString("You spent all your money in the game.", Main.WIDTH / 2, (Main.HEIGHT / 15) * 12);
				g.drawString("There is only one planet so try harder to protect it.", Main.WIDTH / 2,
						(Main.HEIGHT / 15) * 13);

			} else if (pollution > 100) {
				g.setFont(menuFont);
				g.setColor(Color.orange);
				g.drawString("You polluted the environment too much in the game.", Main.WIDTH / 2,
						(Main.HEIGHT / 15) * 12);
				g.drawString("There is only one planet so try harder to protect it.", Main.WIDTH / 2,
						(Main.HEIGHT / 15) * 13);

			}
		}

	}

	void drawSelectionState(Graphics g) {

		g.drawImage(AppPanel.titleImg, 0, 0, Main.WIDTH, Main.HEIGHT, null);
		g.drawImage(AppPanel.planetaryTextImg, (Main.WIDTH / 2), Main.HEIGHT / 10, 411, 109, null);

	}

	void drawGameState(Graphics g) {
		g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		g.setFont(menuFont);
		g.setColor(Color.green);
		g.drawString("Trees: " + treenum, 40, 60);
		g.drawString("Money: " + money, 40, 30);
		g.drawString("Fossil Fuel Burning Factories: " + factorynum, 40, 90);
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
			if (howToPlayB.isSelected()) {
				howToPlayB.drawSelected(g);
			} else {
				howToPlayB.draw(g);
			}
		} catch (ConcurrentModificationException h) {
			// modifying the trees arrayList gives an error so i dont want to crash the app
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
	// replace trees/factories

	void replaceTree(Tree t) {
		Random r = new Random();
		int type = r.nextInt(3);
		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).equals(t)) {
				factories.add(new Factory(t.x, t.y, 254 / 2, 278 / 2, type));
				trees.remove(i);
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
					trees.add(new Tree(f.x, f.y, 144 / 2, 351 / 2, 187 / 2, 389 / 2, 0));
					break;
				case 1:
					trees.add(new Tree(f.x, f.y, 306 / 2, 304 / 2, 364 / 2, 367 / 2, 1));
					break;
				case 2:
					trees.add(new Tree(f.x, f.y, 131 / 2, 312 / 2, 197 / 2, 367 / 2, 2));
					break;
				default:
					break;
				}
				factories.remove(i);
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
				trees.add(new Tree(tposW, tposH, 144 / 2, 351 / 2, 187 / 2, 389 / 2, 0));
				break;
			case 1:
				trees.add(new Tree(tposW, tposH, 306 / 2, 304 / 2, 364 / 2, 367 / 2, 1));
				break;
			case 2:
				trees.add(new Tree(tposW, tposH, 131 / 2, 312 / 2, 197 / 2, 367 / 2, 2));
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
		for (GameButton gb : donateButtons) {
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
		for (GameButton gb : aboutButtons) {
			if (gb.isSelected()) {
				gb.drawSelected(g);
			} else {
				gb.draw(g);
			}
		}

	}

	@Override
	// goes every frame bc timer started
	public void actionPerformed(ActionEvent arg0) {
		// call paintComponent
		repaint();
		// get the current time
		this.elapsedTimeSec = (System.currentTimeMillis() - startTime) / 1000;
		this.elapsedTime = (System.currentTimeMillis() - startTime);
		// decide wheather or not the buttons should be highlighted/selected
		for (GameButton gb : titleButtons) {
			if (gb.getButtonBox().contains(MouseInfo.getPointerInfo().getLocation())) {
				gb.setSelected(true);
			} else {
				gb.setSelected(false);
			}
			gb.update();
		}
		if (howToPlayB.getButtonBox().contains(MouseInfo.getPointerInfo().getLocation())) {
			howToPlayB.setSelected(true);
		} else {
			howToPlayB.setSelected(false);
		}
		howToPlayB.update();
		for (GameButton gb : infoButtons) {
			if (gb.getButtonBox().contains(MouseInfo.getPointerInfo().getLocation())) {
				gb.setSelected(true);
			} else {
				gb.setSelected(false);
			}
			gb.update();
		}
		for (GameButton gb : aboutButtons) {
			if (gb.getButtonBox().contains(MouseInfo.getPointerInfo().getLocation())) {
				gb.setSelected(true);
			} else {
				gb.setSelected(false);
			}
			gb.update();
		}
		for (GameButton gb : donateButtons) {
			if (gb.getButtonBox().contains(MouseInfo.getPointerInfo().getLocation())) {
				gb.setSelected(true);
			} else {
				gb.setSelected(false);
			}

			gb.update();
		}
		

		for (int i = 0; i < trees.size(); i++) {
			if (trees.get(i).box.contains(MouseInfo.getPointerInfo().getLocation())) {

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
			// set the variables
			factorynum = factories.size();
			treenum = trees.size() - 1;

		}

		// check if the game is over
		if (canPlay) {
			if (money < 0) {
				gameOver = true;
			}
			if (pollution >= 100) {
				gameOver = true;
			}
			if (money >= 1000000) {
				gameWin = true;
			}
		}
		if (gameWin) {
			transitions = Transition.GAMEGAMEWIN;
			currentState = State.TITLE;
			canPlay = false;
			gameWin = false;
		}
		if (gameOver) {
			executor.shutdown();
			transitions = Transition.GAMEGAMEOVER;
			currentState = State.TITLE;
			canPlay = false;
			gameOver = false;

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
	// when the mouse is pressed
	public void mousePressed(MouseEvent e) {
		// left click
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (quitB.isSelected()) {
				System.exit(0);
			}
			if (canPlay) {
				if (startB.isSelected()) {
					transitions = Transition.STARTGAME;
					currentState = State.GAME;
				}
			}
			if (infoB.isSelected()) {
				transitions = Transition.TITLEINFO;
				currentState = State.INFO;
			}
			if (backBS.isSelected()) {
				transitions = Transition.INFOTITLE;
				currentState = State.TITLE;
			}
			if (aboutB.isSelected()) {
				transitions = Transition.TITLEABOUT;
				currentState = State.ABOUT;
			}
			if (donateB.isSelected()) {

				transitions = Transition.INFODONATE;
				currentState = State.DONATE;
			}
			if (backBA.isSelected()) {
				transitions = Transition.ABOUTTITLE;
				currentState = State.TITLE;
			}
			if (backBL.isSelected()) {
				transitions = Transition.DONATEINFO;
				currentState = State.INFO;
			}
			if (teamtreesB.isSelected()) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URL("http://teamtrees.org").toURI());
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
			if (howToPlayB.isSelected()) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URL(
							"https://docs.google.com/document/d/16XldlwXGYPzk5gAhz3DT7ntUWr7grluN4uuGd8S2RVs/edit?usp=sharing")
									.toURI());
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
			if (rainforrestWebB.isSelected()) {

				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URL("https://www.ran.org/ways-to-give/").toURI());
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}

			}
			if (plantForThePlanetB.isSelected()) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(
							new URL("https://www.plant-for-the-planet.org/en/support/tree-voucher \r\n").toURI());
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}

			}
			if (natureConB.isSelected()) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URL("https://support.nature.org/site/Donation2?df_id=13740&13740.donation=form1")
							.toURI());
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}

			}
			if (oneTreePlantedB.isSelected()) {
				Desktop desktop = Desktop.getDesktop();
				try {
					desktop.browse(new URL("https://onetreeplanted.org/pages/about-us").toURI());
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}

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