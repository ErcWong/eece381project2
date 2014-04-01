
package com.example.piano;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

public class PianoView extends View {
	private PianoKey[] keys;
	protected float scale;
	public float screenKeyWidth, screenKeyHeight;
    protected static final int FINGERS = 5; // The number of simultaneous fingers

	public PianoView(Context context, AttributeSet attribute_set) {
		super(context, attribute_set);
		keys = new PianoKey[20];
		//DisplayMetrics metrics = new DisplayMetrics();    
		//((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);    
	    //scale = metrics.densityDpi; 
	    
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
		
		for( int drawWhiteKey = 0; drawWhiteKey <= 7; ++drawWhiteKey ) {  // draw white keys
			keys[drawWhiteKey].draw(canvas);
		}
		
		for( int drawBlackKey = 8; drawBlackKey <= 13; ++drawBlackKey ) { //draw black keys
			keys[drawBlackKey].draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    // get pointer index from the event object
	    int pointerIndex = event.getActionIndex();
	    // get pointer ID
	    int pointerId = event.getPointerId(pointerIndex);
	    // get masked (not specific to a pointer) action
	    int action = event.getActionMasked();
	    boolean redraw = false;

	    switch (action) {
	    case MotionEvent.ACTION_DOWN:
	    case MotionEvent.ACTION_POINTER_DOWN:
	    	if( pointerId < FINGERS ) {
	    		int x = (int)event.getX();
	            int y = (int)event.getY();
	            float pressure = event.getPressure();
	             onTouchDown(pointerId, x, y, pressure);
	    	}
	    	break;
	    case MotionEvent.ACTION_MOVE:  // a pointer was moved
	    	for (int index = 0; index < event.getPointerCount(); ++index) {
	    		if( pointerId >= FINGERS )
	    			continue;
		    	if( index >= 0 ) {
		    		int x = (int)event.getX(index);
		            int y = (int)event.getY(index);
		            float pressure = event.getPressure(index);
		            onTouchDown(pointerId, x, y, pressure);
		    	}
	    	}
	    	break;
	    case MotionEvent.ACTION_UP:
	    case MotionEvent.ACTION_POINTER_UP: 
	    	if (pointerId < FINGERS) {
	    		 onTouchUp(pointerId);
	        }
	    	break;
	    }
	 
	    	invalidate();

	    return true;
	} 
	
	 protected void onTouchDown(int finger, int x, int y, float pressure) {
		    // Look through keys from top to bottom, and set the first one found as down, the rest as up.
		    boolean redraw = false;
		    for (int i = 13; i >= 0; --i) {
		        if (keys[i].contains(x, y)) {
		            // This key is being touched.
		        	keys[i].pressed = true;
		        	break;
		        } else {
		            // This key is not being touched.
		        	keys[i].pressed = false;
		        }
		    }
	 }
	 
	 protected void onTouchUp(int finger) {
	    // Set all keys as up.
	    boolean redraw = false;
	    for (int i = 0; i <= 13; ++i) {
	    	keys[i].pressed = false;
	    }
	}

}
	