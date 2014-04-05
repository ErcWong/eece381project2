package com.example.piano;

import android.graphics.Canvas;
import android.graphics.Color;

public class BlackPianoKey extends PianoKey {
	
	public  BlackPianoKey(PianoView piano, int key, float height, float width) {
		super(piano);
		
		rect.top = (int)height;
		rect.bottom = rect.top + (int)((2 * height) * 0.666666);
		if( key == 1 ) {
			rect.right = (int)( width + width * 0.25 );
			rect.left = rect.right - (int)(width * 0.6);
		}
		else if( key == 2 ) {
			rect.left = (int)( width + width * 0.75 );
			rect.right = rect.left + (int)(width * 0.6);
		}
		else if( key == 3 )	{
			rect.right = (int)( (4 * width) + width * 0.25 );
			rect.left = rect.right - (int)(width * 0.6);
		}
		else if( key == 4 ) {
			rect.left = (int)( (5 * width) - (width * 0.3) );
			rect.right = rect.left + (int)(width * 0.6);
		}
		else {
			rect.left = (int)( (5 * width) + width * 0.75 );
			rect.right = rect.left + (int)(width * 0.6);
		}
	}
	
	public void draw( Canvas canvas ) {
		if( pressed ) {
			fillpaint.setColor(Color.GREEN);
		} else {
			fillpaint.setColor(Color.BLACK);
		}
		canvas.drawRect(rect,fillpaint);
	}
}