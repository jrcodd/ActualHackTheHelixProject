package theOnlyPackage;

import java.awt.Rectangle;

public class Tree {
	int x;
	int y;
	int width;
	int height;
	boolean isSelected;
	Rectangle box;
	boolean firstTime = true;
	int type;
	int widthC;
	int heightC;
	Tree(int x, int y, int width, int height, int widthC, int heightC, int type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		isSelected = false;
		box = new Rectangle(x, y, width, height);
		this.type = type;
		this.widthC = widthC;
		this.heightC = heightC;
	}

}
