package com.example.ece381;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PianoView extends View implements View.OnLongClickListener,
		View.OnClickListener {

	private final String TAG = "PianoView";
	private int offset = 50;

	public PianoView(Context context) {
		super(context);

		eventDataMap = new HashMap<Integer, EventData>();

		this.setOnLongClickListener(this);
		this.setOnClickListener(this);
	}

	@Override
	public void onDraw(Canvas canvas) {
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawPaint(paint);
		for (PianoKey key : PianoActivity.pianoKeyList) {
			paint.setColor(Color.BLACK);
			if (key.getKeyName().contains("b")) {
				paint.setStyle(Paint.Style.FILL);
			} else {
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(10);
			}
			// canvas.drawRect(key.getKeyShape().getRectLeft(), paint);
			// canvas.drawRect(key.getKeyShape().getRectRight(), paint);
			canvas.drawRect(key.getRect(), paint);

			paint.setStrokeWidth(2);
			paint.setStyle(Paint.Style.FILL);
			// paint white letters for flats
			if (key.getKeyName().contains("b")) {
				paint.setColor(Color.WHITE);
				System.out.print(key.getKeyName());
			} else {
				paint.setColor(Color.BLACK);
			}

			canvas.drawText(key.getKeyName(), key.getRect().centerX() - offset,
					key.getRect().centerY(), paint);
			canvas.drawText(String.valueOf(key.isPlayed()), key.getRect()
					.centerX(), key.getRect().centerY(), paint);
		}
		// for (EventData event : eventDataMap.values()) {
		// paint.setColor(Color.WHITE);
		// paint.setStyle(Paint.Style.FILL);
		// canvas.drawCircle(event.x, event.y, 30, paint);
		// paint.setStyle(Paint.Style.STROKE);
		// if (event.pressure <= 0.001) {
		// paint.setColor(Color.RED);
		// }
		// canvas.drawCircle(event.x, event.y, 60, paint);
		// }
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
			eventDataMap.put(Integer.valueOf(pointerId), eventData);
			if (returnValueOnActionDown) {
				result = returnValueOnActionDown;
			}
			keyPress((int) eventData.x, (int) eventData.y);
			break;
		case MotionEvent.ACTION_MOVE:
			for (int i = 0; i < event.getPointerCount(); i++) {
				int curPointerId = event.getPointerId(i);
				if (eventDataMap.containsKey(Integer.valueOf(curPointerId))) {
					EventData moveEventData = eventDataMap.get(Integer
							.valueOf(curPointerId));
					moveEventData.x = event.getX(i);
					moveEventData.y = event.getY(i);
					keyPress((int) moveEventData.x, (int) moveEventData.y);
					keyLetGo((int) moveEventData.x, (int) moveEventData.y);
				}
			}
			if (returnValueOnActionMove) {
				result = returnValueOnActionMove;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			int x = (int) event.getX(pointerIndex);
			int y = (int) event.getY(pointerIndex);
			keyLetGo(x, y);
			eventDataMap.remove(Integer.valueOf(pointerId));
			if (returnValueOnActionUp) {
				result = returnValueOnActionUp;
			}
			break;
		case MotionEvent.ACTION_OUTSIDE:
			break;
		}
		invalidate();
		dumpEvent(event);
		return result;
	}

	@Override
	public void onClick(View v) {
		// Toast msg = Toast.makeText(PianoView.this.getContext(), "onClick",
		// Toast.LENGTH_SHORT);
		// msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
		// msg.getYOffset() / 2);
		// msg.show();
	}

	@Override
	public boolean onLongClick(View v) {
		// Toast msg = Toast.makeText(PianoView.this.getContext(),
		// "onLongClick",
		// Toast.LENGTH_SHORT);
		// msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
		// msg.getYOffset() / 2);
		// msg.show();
		return returnValueOnLongClick;
	}

	public void keyPress(int x, int y) {
		for (PianoKey key : PianoActivity.pianoKeyList) {
			if (key.getKeyShape().contains(x, y) && !key.isPlayed()) {
				toastKey(key);
				playSound(key);
				key.setPlayed(true);
			}
		}
		// for (i = 0; i < 12; i++) {
		// if (y < (height / 7) * (i + 1) & y > (height / 7) * i) {
		// } else if (y < (height / 12) * (i + 1) & y > (height / 12) * i
		// & x > 3 * width / 4) {
	}

	public void keyMove(int x, int y) {
		for (PianoKey key : PianoActivity.pianoKeyList) {
			if (key.getKeyShape().contains(x, y) && key.isPlayed()) {
				Toast msg = Toast.makeText(PianoView.this.getContext(),
						(key.getKeyName() + "Let go"), Toast.LENGTH_SHORT);
				msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
						msg.getYOffset() / 2);
				msg.show();
				key.setPlayed(false);
			}
		}
	}

	// Reset key played boolean to false
	public void keyLetGo(int x, int y) {
		for (PianoKey key : PianoActivity.pianoKeyList) {
			if (key.getKeyShape().contains(x, y) && key.isPlayed()) {
				Toast msg = Toast.makeText(PianoView.this.getContext(),
						(key.getKeyName() + "Let go"), Toast.LENGTH_SHORT);
				msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
						msg.getYOffset() / 2);
				msg.show();
				key.setPlayed(false);
			}
		}
	}

	// Plays the sound
	public void playSound(PianoKey key) {
		PianoActivity.getSoundPool().play(key.getKeyid(), 1, 1, 0, 0,
				key.getKeyrate());
	}

	public void toastKey(PianoKey key) {
		Toast msg = Toast.makeText(PianoView.this.getContext(),
				key.getKeyName(), Toast.LENGTH_SHORT);
		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);
		msg.show();
	}

	@SuppressWarnings("deprecation")
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
				"POINTER_DOWN", "POINTER_UP" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event Action_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
				|| actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("pid").append(
					action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount()) {
				sb.append(";");
			}
		}
		sb.append("]");
		Log.d(TAG, sb.toString());
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
