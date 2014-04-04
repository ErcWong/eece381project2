package com.example.ece381;

import android.graphics.Canvas;
import android.graphics.Color;

public class WhitePianoKey extends PianoKey {
	
	public  WhitePianoKey(PianoView piano, int key, float height, float width) {
		super(piano);
		
		rect.top = (int)height;
		rect.bottom = rect.top + (int)(2 *height);
		rect.left = (( key - 1 ) * (int)width);
		rect.right = rect.left + (int)width;
	}
	
	public void draw( Canvas canvas ) {
		strokepaint.setColor(Color.BLACK);
		if( pressed ) {
			fillpaint.setColor(Color.MAGENTA);
		} else {
			fillpaint.setColor(Color.WHITE);
		}
		canvas.drawRect(rect,strokepaint);
		canvas.drawRect( rect.left+5, rect.top+5, rect.right-5, 
				rect.bottom-5, fillpaint);
	}
}
