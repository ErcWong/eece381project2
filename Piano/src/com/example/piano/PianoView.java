
package com.example.piano;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

public class PianoView extends View {
	private PianoKey[] keys;
	protected float scale;
	public float screenKeyWidth, screenKeyHeight;

	public PianoView(Context context, AttributeSet attribute_set) {
		super(context, attribute_set);
		keys = new PianoKey[20];
		DisplayMetrics metrics = new DisplayMetrics();    
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);    
	    scale = metrics.densityDpi; 
	    
	    Point point = new Point();
	    Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay(); 
	    display.getSize( point );
	    screenKeyWidth = (float)(point.x / 7);
	    screenKeyHeight = (float)(point.y * 0.3333333);
	    
	    int key_total = 0;
	    for( int note = 0; note <= 7; ++note ) { // create white keys
	    	keys[key_total++] = new WhitePianoKey( this, note, screenKeyHeight, screenKeyWidth );
	   }
	    for( int note = 0; note <= 5; ++note ) { // create black keys
	    	keys[key_total++] = new BlackPianoKey( this, note, screenKeyHeight, screenKeyWidth );	    
	    }
	}
	
	@Override	
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		
		for( int drawWhiteKey = 0; drawWhiteKey <= 7; ++drawWhiteKey ) {  // draw white keys
			paint.setColor(Color.BLACK);
			canvas.drawRect( keys[drawWhiteKey].rect.left, keys[drawWhiteKey].rect.top, keys[drawWhiteKey].rect.right, 
					keys[drawWhiteKey].rect.bottom, paint);
			paint.setColor(Color.WHITE);
			canvas.drawRect( keys[drawWhiteKey].rect.left+5, keys[drawWhiteKey].rect.top+5, keys[drawWhiteKey].rect.right-5, 
					keys[drawWhiteKey].rect.bottom-5, paint);
		}
		
		for( int drawBlackKey = 8; drawBlackKey <= 13; ++drawBlackKey ) { //draw black keys
			paint.setColor(Color.BLACK);
			canvas.drawRect( keys[drawBlackKey].rect.left, keys[drawBlackKey].rect.top, keys[drawBlackKey].rect.right, 
					keys[drawBlackKey].rect.bottom, paint);
		}
	}
}
	