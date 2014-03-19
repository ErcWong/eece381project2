/*
 * Class for handling keys and touch events
*/
package src.com.example.piano;

import android.graphics.Rect;

public abstract class PianoKey {
	protected Rect rect;
	protected boolean pressed;
	
	public PianoKey(PianoView paino) {
		
	}
}