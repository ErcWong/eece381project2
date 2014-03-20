package com.example.piano;

public class BlackPianoKey extends PianoKey {
	
	public  BlackPianoKey(PianoView piano, int key, float height, float width) {
		super(piano);
		
		rect.top = (int)height;
		rect.bottom = rect.top + (int)((2 * height) * 0.333333);
		if( key < 3 ) {
			rect.left = (int)(( key - 1 ) * (width * 0.75));
		}
		else {
			rect.left = (int)( (( key - 1 ) * (width * 0.75)) + (3 * width) );	
		}		
		rect.right = rect.left + (int)(width/2);
		
		/*rect.top = (int)height;
		rect.bottom = rect.top + (int)height;
		rect.left = (( key - 1 ) * (int)width);
		rect.right = rect.left + (int)width;*/
	}
}