package com.example.piano;

public class WhitePianoKey extends PianoKey {
	
	public  WhitePianoKey(PianoView piano, int key, float height, float width) {
		super(piano);
		
		rect.top = (int)height;
		rect.bottom = rect.top + (int)(2 *height);
		rect.left = (( key - 1 ) * (int)width);
		rect.right = rect.left + (int)width;
	}
}