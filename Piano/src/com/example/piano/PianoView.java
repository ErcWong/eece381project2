/*
 * Class to handle whole paino keyboard
*/
package src.com.example.piano;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

public class PianoView extends View {
	private Rect drawingRect;
	public int bottom, top, right, left; 
	public float scale;

	public PianoView(Context context) {
		super(context);
		drawingRect = new Rect(); 
		DisplayMetrics metrics = new DisplayMetrics();    
	    getWindowManager().getDefaultDisplay().getMetrics(metrics);    
	    scale = metrics.densityDpi; 
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		
	}
}
	