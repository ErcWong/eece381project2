package com.example.ece381;

import com.example.drawutil.Polygon;

import android.graphics.Rect;

public class PianoKey {
	private String keyName;
	private int keyID;
	private boolean played = false;
	private int keyRate;
	private Rect rect;
	private String prsntKey;
	private Polygon keyShape;

	public static boolean loaded = false;

	public PianoKey(String keyName, int keyID, boolean played, int keyRate,
			Rect rect, Rect rectLeft, Rect rectRight) {
		this.keyName = keyName;
		this.keyID = keyID;
		this.played = played;
		this.keyRate = keyRate;
		this.rect = rect;
		this.prsntKey = keyName;
		this.keyShape = new Polygon(rectLeft, rectRight);
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public int getKeyid() {
		return keyID;
	}

	public void setKeyid(int keyid) {
		this.keyID = keyid;
	}

	public boolean isPlayed() {
		return played;
	}

	public void setPlayed(boolean played) {
		this.played = played;
	}

	public int getKeyrate() {
		return keyRate;
	}

	public void setKeyrate(int keyRate) {
		this.keyRate = keyRate;
	}

	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}

	public String getPresentKey() {
		return prsntKey;
	}

	public void setPresentKey(String prsntKey) {
		this.prsntKey = prsntKey;
	}

	public Polygon getKeyShape() {
		return keyShape;
	}

	public void setKeyShape(Polygon keyShape) {
		this.keyShape = keyShape;
	}

}