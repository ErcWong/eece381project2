/*
 * Class for handling keys and touch events
*/
package com.example.piano;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class PianoKey {
	protected Rect rect;
	protected boolean pressed = false; // replace with function later on : ERICS TOUCH FUNCTION
	protected PianoView piano_;
	protected Paint fillpaint, strokepaint;
	
	public PianoKey(PianoView paino) {
		rect = new Rect();
		fillpaint = new Paint();
		strokepaint = new Paint();
	}
	
	abstract public void draw(Canvas canvas);
	
	public boolean contains(int x, int y) {
	    return rect.contains(x, y);
	}
}
