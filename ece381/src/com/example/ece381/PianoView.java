package com.example.ece381;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PianoView extends View implements View.OnLongClickListener,
		View.OnClickListener {

	static PianoKey keya = new PianoKey();
	static PianoKey keyab = new PianoKey();
	static PianoKey keyb = new PianoKey();
	static PianoKey keybb = new PianoKey();
	static PianoKey keyc = new PianoKey();
	static PianoKey keyd = new PianoKey();
	static PianoKey keydb = new PianoKey();
	static PianoKey keye = new PianoKey();
	static PianoKey keyeb = new PianoKey();
	static PianoKey keyf = new PianoKey();
	static PianoKey keyg = new PianoKey();
	static PianoKey keygb = new PianoKey();
	
	private PianoKey[] keys;
	protected float scale;
	public float screenKeyWidth, screenKeyHeight;
    protected static final int FINGERS = 5; // The number of simultaneous fingers

	public PianoView(Context context, AttributeSet attribute_set) {
		super(context, attribute_set);
		
		eventDataMap = new HashMap<Integer, EventData>();
		
		this.setOnLongClickListener(this);
		this.setOnClickListener(this);
		
		keys = new PianoKey[20];
	    
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
	public void onDraw(Canvas canvas) {
		/*for (EventData event : eventDataMap.values()) {
			paint.setColor(Color.WHITE);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(event.x, event.y, 30, paint);
			paint.setStyle(Paint.Style.STROKE);
			if (event.pressure <= 0.001) {
				paint.setColor(Color.RED);
			}
			canvas.drawCircle(event.x, event.y, 60, paint);
		}*/
		canvas.drawColor(Color.WHITE);
		//draw keys
		for( int drawWhiteKey = 0; drawWhiteKey <= 7; ++drawWhiteKey ) {  // draw white keys
			keys[drawWhiteKey].draw(canvas);
		}
		
		for( int drawBlackKey = 8; drawBlackKey <= 13; ++drawBlackKey ) { //draw black keys
			keys[drawBlackKey].draw(canvas);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (callBaseClass) {
			super.onTouchEvent(event);
		}

		if (!handleOnTouchEvent) {
			return false;
		}

		int action = event.getActionMasked();

		int pointerIndex = event.getActionIndex();
		int pointerId = event.getPointerId(pointerIndex);

		boolean result = false;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_POINTER_DOWN:
			EventData eventData = new EventData();
			eventData.x = event.getX(pointerIndex);
			eventData.y = event.getY(pointerIndex);
			eventData.pressure = event.getPressure(pointerIndex);
			eventDataMap.put(new Integer(pointerId), eventData);
			if (returnValueOnActionDown) {
				result = returnValueOnActionDown;
			}
			playSound(eventData.x, eventData.y);
			break;
		case MotionEvent.ACTION_MOVE:
			for (int i = 0; i < event.getPointerCount(); i++) {
				int curPointerId = event.getPointerId(i);
				if (eventDataMap.containsKey(new Integer(curPointerId))) {
					EventData moveEventData = eventDataMap.get(new Integer(
							curPointerId));
					moveEventData.x = event.getX(i);
					moveEventData.y = event.getY(i);
					moveEventData.pressure = event.getPressure(i);
					playSound(moveEventData.x, moveEventData.y);
				}
			}
			if (returnValueOnActionMove) {
				result = returnValueOnActionMove;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			eventDataMap.remove(new Integer(pointerId));
			if (returnValueOnActionUp) {
				result = returnValueOnActionUp;
			}
			break;
		case MotionEvent.ACTION_OUTSIDE:
			break;
		}
		invalidate();
		return result;
	}

	@Override
	public void onClick(View v) {
		Toast msg = Toast.makeText(PianoView.this.getContext(), "onClick",
				Toast.LENGTH_SHORT);
		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);
		msg.show();
	}

	@Override
	public boolean onLongClick(View v) {
		Toast msg = Toast.makeText(PianoView.this.getContext(), "onLongClick",
				Toast.LENGTH_SHORT);
		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);
		msg.show();
		return returnValueOnLongClick;
	}

	public void playSound(float x, float y) {
		int i = 0;
		int height = PianoActivity.getHeight();
		int width = PianoActivity.getWidth();
		for (i = 0; i < 12; i++) {
			if (y < (height / 7) * (i + 1) & y > (height / 7) * i) {
				if (PianoKey.loaded) {
					switch (i) {
					case 0:
						PianoActivity.getSoundPool().play(keyc.getKeyid(), 1,
								1, 0, 0, keyc.getKeyrate());
						break;
					case 1:
						PianoActivity.getSoundPool().play(keyd.getKeyid(), 1,
								1, 0, 0, keyd.getKeyrate());
						break;
					case 2:
						PianoActivity.getSoundPool().play(keye.getKeyid(), 1,
								1, 0, 0, keye.getKeyrate());
						break;
					case 3:
						PianoActivity.getSoundPool().play(keyf.getKeyid(), 1,
								1, 0, 0, keyf.getKeyrate());
						break;
					case 4:
						PianoActivity.getSoundPool().play(keyg.getKeyid(), 1,
								1, 0, 0, keyg.getKeyrate());
						break;
					case 5:
						PianoActivity.getSoundPool().play(keya.getKeyid(), 1,
								1, 0, 0, keya.getKeyrate());
						break;
					case 6:
						PianoActivity.getSoundPool().play(keyb.getKeyid(), 1,
								1, 0, 0, keyb.getKeyrate());
						break;
					}
				}
			} else if (y < (height / 12) * (i + 1) & y > (height / 12) * i
					& x > 3 * width / 4) {
				if (PianoKey.loaded) {
					switch (i) {
					case 1:
						PianoActivity.getSoundPool().play(keydb.getKeyid(), 1,
								1, 0, 0, keydb.getKeyrate());
						break;
					case 3:
						PianoActivity.getSoundPool().play(keyeb.getKeyid(), 1,
								1, 0, 0, keyeb.getKeyrate());
						break;
					case 5:
						PianoActivity.getSoundPool().play(keygb.getKeyid(), 1,
								1, 0, 0, keygb.getKeyrate());
						break;
					case 8:
						PianoActivity.getSoundPool().play(keyab.getKeyid(), 1,
								1, 0, 0, keyab.getKeyrate());
						break;
					case 10:
						PianoActivity.getSoundPool().play(keybb.getKeyid(), 1,
								1, 0, 0, keybb.getKeyrate());
						break;
					}
				}
			}
		}
	}

	public float getScreenSize(float lengthInMm) {
		// Size_in_mm = Size_in_inches * 25.4;
		// Size_in_inches = Size_in_mm / 25.4;
		// Size_in_dp = Size_in_inches * 160;
		// Size_in_dp = (Size_in_mm / 25.4) * 160;
		// Size_in_inches = Size_in_dp / 160;
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				(float) (lengthInMm * 160 / 25.4), getResources()
						.getDisplayMetrics());
	}

	public void setCallBaseClass(boolean process) {
		callBaseClass = process;
	}

	public boolean getCallBaseClass() {
		return callBaseClass;
	}

	public void setHandleTouchEvent(boolean process) {
		handleOnTouchEvent = process;
	}

	public boolean getHandleTouchEvent() {
		return handleOnTouchEvent;
	}

	public void setReturnValueOnActionDown(boolean value) {
		returnValueOnActionDown = value;
	}

	public boolean getReturnValueOnActionDown() {
		return returnValueOnActionDown;
	}

	public void setReturnValueOnActionMove(boolean value) {
		returnValueOnActionMove = value;
	}

	public boolean getReturnValueOnActionMove() {
		return returnValueOnActionMove;
	}

	public void setReturnValueOnActionUp(boolean value) {
		returnValueOnActionUp = value;
	}

	public boolean getReturnValueOnActionUp() {
		return returnValueOnActionUp;
	}

	public void setReturnValueOnLongClick(boolean value) {
		returnValueOnLongClick = value;
	}

	public boolean getReturnValueOnLongClick() {
		return returnValueOnLongClick;
	}

	private Paint paint = new Paint();

	private boolean callBaseClass = true;
	private boolean handleOnTouchEvent = true;
	private boolean returnValueOnActionDown = true;
	private boolean returnValueOnActionMove = true;
	private boolean returnValueOnActionUp = true;
	private boolean returnValueOnLongClick = false;

	private Map<Integer, EventData> eventDataMap;

	private class EventData {
		public float x;
		public float y;
		public float pressure;
	}
}
