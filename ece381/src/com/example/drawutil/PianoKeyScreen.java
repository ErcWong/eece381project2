package com.example.drawutil;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import com.example.ece381.*;

public class PianoKeyScreen {
	private static PianoKey[] keys;
	//protected int bottom, top, right, left; 
	protected float scale;
	//protected int key_total = 0;
	private int whitekey_max = 7;
	private int blackkey_max = 5;
	public float screenKeyWidth, screenKeyHeight;

	public PianoKeyScreen() {
		keys = new PianoKey[20];
		DisplayMetrics metrics = new DisplayMetrics();    
	    scale = metrics.densityDpi; 
	    
	    screenKeyWidth = (float)(PianoActivity.getWidth()/7);
	    screenKeyHeight = (float)(PianoActivity.getHeight()*0.3333333);
	    
	    int key_total = 0;
	    for( int note = 0; note <= 6; ++note ) {
	    	keys[key_total++] = new WhitePianoKey( keys[note] , note, screenKeyHeight, screenKeyWidth );
	   }
	    for( int note = 7; note <= 11; ++note ) {
	    	keys[key_total++] = new BlackPianoKey( keys[note] , note, screenKeyHeight, screenKeyWidth );	    
	    }
	}
	
	public static void draw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		
		for( int drawWhiteKey = 0; drawWhiteKey <= 6; ++drawWhiteKey ) { 
			paint.setColor(Color.BLACK);
			canvas.drawRect( keys[drawWhiteKey].getRect().left, keys[drawWhiteKey].getRect().top, keys[drawWhiteKey].getRect().right, 
					keys[drawWhiteKey].getRect().bottom, paint);
			paint.setColor(Color.WHITE);
			canvas.drawRect( keys[drawWhiteKey].getRect().left+5, keys[drawWhiteKey].getRect().top+5, keys[drawWhiteKey].getRect().right-5, 
					keys[drawWhiteKey].getRect().bottom-5, paint);
		}
		
		for( int drawBlackKey = 7; drawBlackKey <= 11; ++drawBlackKey ) { 
			//paint.setColor(Color.WHITE);
			//canvas.drawRect( keys[drawBlackKey].rect.left, keys[drawBlackKey].rect.top, keys[drawBlackKey].rect.right, 
				//	keys[drawBlackKey].rect.bottom, paint);
			paint.setColor(Color.BLACK);
			canvas.drawRect( keys[drawBlackKey].getRect().left, keys[drawBlackKey].getRect().top, keys[drawBlackKey].getRect().right, 
					keys[drawBlackKey].getRect().bottom, paint);
		}
	}
}
	