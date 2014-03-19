/*
 * Class to handle whole piano keyboard
*/
package src.com.example.piano;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

public class PianoView extends View {
	private PianoKey[] keys;
	//protected int bottom, top, right, left; 
	protected float scale;
	protected int key_total = 0;
	private int whitekey_max = 7;
	private int blackkey_max = 5;

	public PianoView(Context context) {
		super(context);
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
	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		
	}
}
	