/*
 * Class to handle whole paino keyboard
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
	protected int bottom, top, right, left; 
	protected float scale;
	protected int whitekey_max = 7;
	protected int blackkey_max = 5;

	public PianoView(Context context) {
		super(context);
		keys = new PianoKey[whitekey_max + blackkey_max];
		DisplayMetrics metrics = new DisplayMetrics();    
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);    
	    scale = metrics.densityDpi; 
	    
	    int whitekey_count;
	    int blackkey_count;
	    for( whitekey_count=0; whitekey_count<whitekey_max; whitekey_count++ ) {
	    	//create white keys
	    }
	    for( blackkey_count=0; blackkey_count<blackkey_max; blackkey_count++ ) {
	    	//create black keys
	    }
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		
	}
}
	