package com.example.piano;

public class WhitePianoKey extends PianoKey {
	protected int whiteKeyBaseHeight = 50;
	protected int whiteKeyBaseWidth = 30; 
	
	public  WhitePianoKey(PianoView piano, int key, float scale) {
		super(piano);
		
		int whiteKeyHeight = whiteKeyBaseHeight; //* (int)scale;
		int whiteKeyWidth = whiteKeyBaseWidth; //* (int)scale;
		rect.top = (( key - 1 ) * whiteKeyHeight);
		rect.bottom = rect.top + whiteKeyHeight;
		rect.left = (( key - 1 ) * whiteKeyWidth);
		rect.right = rect.left + whiteKeyWidth;
	}
}