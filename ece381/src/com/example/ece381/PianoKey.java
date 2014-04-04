package com.example.ece381;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


public abstract class PianoKey {
	private int keyid;
	private boolean played = false;
	static boolean loaded = false;
	private int keyrate = 1;
	protected PianoView piano_;
	protected Paint fillpaint, strokepaint;
	protected boolean pressed = false;
	
	protected Rect rect;

	
	public PianoKey(PianoView paino) {
		rect = new Rect();
		fillpaint = new Paint();
		strokepaint = new Paint();
	}
	
	abstract public void draw(Canvas canvas);
	
	public boolean contains(int x, int y) {
	    return rect.contains(x, y);
	}

	public int getKeyid() {
		return keyid;
	}

	public void setKeyid(int keyid) {
		this.keyid = keyid;
	}

	public boolean isPlayed() {
		return played;
	}

	public void setPlayed(boolean played) {
		this.played = played;
	}

	public int getKeyrate() {
		return keyrate;
	}

	public void setKeyrate(int keyrate) {
		this.keyrate = keyrate;
	}

	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}

}