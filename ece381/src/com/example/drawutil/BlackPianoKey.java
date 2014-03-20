package com.example.drawutil;

import com.example.ece381.PianoKey;

public class BlackPianoKey extends PianoKey {
	public  BlackPianoKey(PianoKey pianoKey, int key, float height, float width) {
		
		pianoKey.getRect().top = (int)height;
		pianoKey.getRect().bottom = pianoKey.getRect().top + (int)((2 * height) * 0.666666);
		if( key == 1 ) {
			pianoKey.getRect().right = (int)( width + width * 0.25 );
			pianoKey.getRect().left = pianoKey.getRect().right - (int)(width * 0.6);
		}
		else if( key == 2 ) {
			pianoKey.getRect().left = (int)( width + width * 0.75 );
			pianoKey.getRect().right = pianoKey.getRect().left + (int)(width * 0.6);
		}
		else if( key == 3 )	{
			pianoKey.getRect().right = (int)( (4 * width) + width * 0.25 );
			pianoKey.getRect().left = pianoKey.getRect().right - (int)(width * 0.6);
		}
		else if( key == 4 ) {
			pianoKey.getRect().left = (int)( (5 * width) - (width * 0.3) );
			pianoKey.getRect().right = pianoKey.getRect().left + (int)(width * 0.6);
		}
		else {
			pianoKey.getRect().left = (int)( (5 * width) + width * 0.75 );
			pianoKey.getRect().right = pianoKey.getRect().left + (int)(width * 0.6);
		}
	}
}
