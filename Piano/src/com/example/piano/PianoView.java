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
	private PianoKey keys;
	public int bottom, top, right, left; 
	public float scale;
	public int whitekey_max = 7;
	public int blackkey_max = 5;

	public PianoView(Context context) {
		super(context);
		keys = new PianoKey[whitekey_max + blackkey_max];
		DisplayMetrics metrics = new DisplayMetrics();    
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);    
	    scale = metrics.densityDpi; 
	    
	    int whitekey_count;
	    for( whitekey_count=0; whitekey_count<whitekey_max; whitekey_count++ ) {
	    	
	    }
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		
	}
}
	