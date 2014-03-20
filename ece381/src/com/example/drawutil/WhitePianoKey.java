package com.example.drawutil;

import com.example.ece381.PianoKey;

public class WhitePianoKey extends PianoKey {
	public  WhitePianoKey(PianoKey pianoKey, int key, float height, float width) {
		pianoKey.getRect().top = (int)height;
		pianoKey.getRect().bottom = pianoKey.getRect().top + (int)(2 *height);
		pianoKey.getRect().left = (( key - 1 ) * (int)width);
		pianoKey.getRect().right = pianoKey.getRect().left + (int)width;
	}
}
