package theOnlyPackage;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class GameButton {
	private BufferedImage img;
	private BufferedImage imgC;
	private int x;
	private int y;
	private int width;
	private int height;
	private int widthC;
	private int heightC;
	private Rectangle buttonBox;
	private boolean selected;
	private boolean show;

	GameButton(BufferedImage img, BufferedImage imgC, int x, int y, int width, int height, int widthC, int heightC) {
		this.setButtonBox(new Rectangle(x, y, width, height));
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.heightC = heightC;
		this.widthC = widthC;
		this.img = img;
		this.imgC = imgC;
		show = true;
	}

	void draw(Graphics g) {
		if (show) {
			g.drawImage(img, x, y, width, height, null);
		}
	}

	void drawSelected(Graphics g) {
		if (show) {
			g.drawImage(imgC, x, y, width, height, null);
		}
	}
	
	void kill() {
		show = false;
	}
	void unKill() {
		show = true;
	}
	public boolean isSelected() {
		return selected;
	}
	void update() {
		
		buttonBox.setBounds(x, y, width, height);
		if(x<0-width || x>Main.WIDTH || y<0-height || y>Main.HEIGHT) {
			kill();
		}
		else {
			unKill();
		}
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public Rectangle getButtonBox() {
		return buttonBox;
	}

	public void setButtonBox(Rectangle buttonBox) {
		this.buttonBox = buttonBox;
	}

	public int getHeightC() {
		return heightC;
	}

	public void setHeightC(int heightC) {
		this.heightC = heightC;
	}

	public int getWidthC() {
		return widthC;
	}

	public void setWidthC(int widthC) {
		this.widthC = widthC;
	}

}
