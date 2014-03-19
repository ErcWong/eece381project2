/*
 * Class for handling keys and touch events
*/
package com.example.piano;

import android.graphics.Rect;

public abstract class PianoKey {
	protected Rect rect;
	protected boolean pressed;
	protected PianoView piano_;
	
	public PianoKey(PianoView paino) {
		rect = new Rect();
	}
}