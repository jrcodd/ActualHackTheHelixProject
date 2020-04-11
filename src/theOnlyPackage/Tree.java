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

	Tree(int x, int y, int width, int height, int type) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		isSelected = false;
		box = new Rectangle(x, y, width, height);
		this.type = type;
	}

}
