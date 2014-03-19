
package com.example.piano;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class PianoView extends View {
	private PianoKey[] keys;
	//protected int bottom, top, right, left; 
	protected float scale;
	protected int key_total = 0;
	private int whitekey_max = 7;
	private int blackkey_max = 5;

	public PianoView(Context context, AttributeSet attribute_set) {
		super(context, attribute_set);
		keys = new PianoKey[whitekey_max + blackkey_max];
		DisplayMetrics metrics = new DisplayMetrics();    
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);    
	    scale = metrics.densityDpi; 
	    
	    for( int note = 0; note <= whitekey_max; ++note ) {
	    	keys[key_total++] = new WhitePianoKey( this, note, scale);
	    }
	   // for( int note = 0; note <= blackkey_max; ++note ) {
	    //	keys[key_total++] = new BlackPianoKey( this, note, scale);	    
	    //}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		
		for( int drawWhiteKey = 0; drawWhiteKey <= whitekey_max; ++drawWhiteKey ) { 
			paint.setColor(Color.BLACK);
			canvas.drawRect( keys[drawWhiteKey].rect.left, keys[drawWhiteKey].rect.top, keys[drawWhiteKey].rect.right, 
					keys[drawWhiteKey].rect.bottom, paint);
			paint.setColor(Color.RED);
			canvas.drawRect( keys[drawWhiteKey].rect.left-5, keys[drawWhiteKey].rect.top-5, keys[drawWhiteKey].rect.right-5, 
					keys[drawWhiteKey].rect.bottom-5, paint);
		}
	}
}
	