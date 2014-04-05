package com.example.drawutil;

import android.graphics.Rect;

public class Polygon {
	private Rect rectLeft;
	private Rect rectRight;

	public Polygon(Rect rectLeft, Rect rectRight) {
		this.rectLeft = rectLeft;
		this.rectRight = rectRight;
	}

	public boolean contains(int x, int y) {
		if (rectLeft.contains(x, y) || rectRight.contains(x, y)) {
			return true;
		}
		return false;
	}

	public Rect getRectLeft() {
		return rectLeft;
	}

	public void setRectLeft(Rect rectLeft) {
		this.rectLeft = rectLeft;
	}

	public Rect getRectRight() {
		return rectRight;
	}

	public void setRectRight(Rect rectRight) {
		this.rectRight = rectRight;
	}
}