
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
	//protected int bottom, top, right, left; 
	protected float scale;
	//protected int key_total = 0;
	private int whitekey_max = 7;
	private int blackkey_max = 5;
	public float screenKeyWidth, screenKeyHeight;

	public PianoView(Context context, AttributeSet attribute_set) {
		super(context, attribute_set);
		keys = new PianoKey[12];
		DisplayMetrics metrics = new DisplayMetrics();    
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);    
	    scale = metrics.densityDpi; 
	    
	    Point point = new Point();
	    Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay(); 
	    display.getSize( point );
	    screenKeyWidth = (float)(point.x / 7);
	    screenKeyHeight = (float)(point.y * 0.3333333);
	    
	    int key_total = 0;
	    for( int note = 0; note <= 7; ++note ) {
	    	keys[key_total++] = new WhitePianoKey( this, note, screenKeyHeight, screenKeyWidth );
	    }
	    /*for( int note = 0; note <= 5; ++note ) {
	    	keys[key_total++] = new BlackPianoKey( this, note, screenKeyHeight, screenKeyWidth );	    
	    }*/
	}
	
	@Override	
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		
		for( int drawWhiteKey = 0; drawWhiteKey <= 7; ++drawWhiteKey ) { 
			paint.setColor(Color.BLACK);
			canvas.drawRect( keys[drawWhiteKey].rect.left, keys[drawWhiteKey].rect.top, keys[drawWhiteKey].rect.right, 
					keys[drawWhiteKey].rect.bottom, paint);
			paint.setColor(Color.WHITE);
			canvas.drawRect( keys[drawWhiteKey].rect.left+5, keys[drawWhiteKey].rect.top+5, keys[drawWhiteKey].rect.right-5, 
					keys[drawWhiteKey].rect.bottom-5, paint);
		}
		
		/*for( int drawBlackKey = 0; drawBlackKey <= blackkey_max; ++drawBlackKey ) { 
			paint.setColor(Color.GREEN);
			canvas.drawRect( keys[drawBlackKey].rect.left, keys[drawBlackKey].rect.top, keys[drawBlackKey].rect.right, 
					keys[drawBlackKey].rect.bottom, paint);
			paint.setColor(Color.BLACK);
			canvas.drawRect( keys[drawBlackKey].rect.left+5, keys[drawBlackKey].rect.top+5, keys[drawBlackKey].rect.right-5, 
					keys[drawBlackKey].rect.bottom-5, paint);
		}*/
	}
}
	