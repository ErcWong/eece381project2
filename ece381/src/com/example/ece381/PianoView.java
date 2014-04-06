package com.example.ece381;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
	static Context c;
	private final String TAG = "PianoView";
	private int offset = 50;
	Set<Integer> keysPlayed = new HashSet<Integer>();

	public PianoView(Context context) {
		super(context);
		c = context;
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
			canvas.drawRect(key.getRect(), paint);

			paint.setStrokeWidth(2);
			paint.setStyle(Paint.Style.FILL);
			// paint white letters for flats
			if (key.getKeyName().contains("b")) {
				paint.setColor(Color.WHITE);
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
			// eventData.currentKey = keyPress((int) eventData.x,
			// (int) eventData.y);
			// eventData.previousKey = eventData.currentKey;
			// System.out.println("eD key" + eventData.currentKey);
			break;
		case MotionEvent.ACTION_MOVE:
			for (int i = 0; i < event.getPointerCount(); i++) {
				int curPointerId = event.getPointerId(i);
				if (eventDataMap.containsKey(Integer.valueOf(curPointerId))) {
					EventData moveEventData = eventDataMap.get(Integer
							.valueOf(curPointerId));
					moveEventData.x = event.getX(i);
					moveEventData.y = event.getY(i);
					moveEventData = keyMove(moveEventData,
							(int) moveEventData.x, (int) moveEventData.y);
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

	// Key touch
	public int keyPress(int x, int y) {
		int tempKey = 0;
		for (PianoKey key : PianoActivity.pianoKeyList) {
			if (key.getKeyShape().contains(x, y) && !key.isPlayed()) {
				// toastKey(key);
				playSound(key);
				key.setPlayed(true);
				keysPlayed.add(key.getKeyid());
				tempKey = key.getKeyid();
				sendNote(key);
			}
		}
		// printHashSet(keysPlayed);
		return tempKey;
	}

	public EventData keyMove(EventData eD, int x, int y) {
		// int currentKey = eD.currentKey;
		int previousKey = eD.previousKey;
		for (PianoKey key : PianoActivity.pianoKeyList) {
			// if (key.getKeyShape().contains(x, y)) {
			// currentKey = key.getKeyid();
			// keysPlayed.add(currentKey);
			// } else if (!keysPlayed.contains(key.getKeyid())) {
			// key.setPlayed(false);
			// }
			// if (key.getKeyShape().contains(x, y) && !key.isPlayed()) {
			// playSound(key);
			// key.setPlayed(true);
			// // currentKey = key.getKeyid();
			// // previousKey = key.getKeyid();
			// } else if (key.isPlayed() &&
			// !keysPlayed.contains(key.getKeyid())) {
			// key.setPlayed(false);
			// }
		}
		return new EventData(x, y, 0, 0);
	}

	// Reset key played boolean to false
	public void keyLetGo(int x, int y) {
		for (PianoKey key : PianoActivity.pianoKeyList) {
			if (key.getKeyShape().contains(x, y) && key.isPlayed()) {
				// toastKey(key, " let go");
				key.setPlayed(false);
				keysPlayed.remove(key.getKeyid());
			}
		}
		printHashSet(keysPlayed);
	}

	// Plays the sound
	public void playSound(PianoKey key) {
		PianoActivity.getSoundPool().play(key.getKeyid(), 1, 1, 0, 0,
				key.getKeyrate());
	}

	// Toast the key
	public void toastKey(PianoKey key, String addOn) {
		Toast msg = Toast.makeText(PianoView.this.getContext(),
				key.getKeyName() + addOn, Toast.LENGTH_SHORT);
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
			// sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append("=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount()) {
				sb.append(";");
			}
		}
		sb.append("]");
		Log.d(TAG, sb.toString());
	}

	public static void printHashSet(Set<Integer> keysPlayed) {
		System.out.print("HashSet: ");
		// Use an Iterator to print each element of the TreeSet.
		Iterator<Integer> iterator = keysPlayed.iterator();
		while (iterator.hasNext())
			System.out.print(iterator.next() + ", ");
		System.out.println();
	}

	public static void removeFromHashSet(Set<Integer> keysPlayed, int key) {
		// Use an Iterator to print each element of the TreeSet.
		Iterator<Integer> iterator = keysPlayed.iterator();
		while (iterator.hasNext())
			System.out.print(iterator.next() + ", ");
		System.out.println();
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

	public static void sendNote(PianoKey key) {
		if (MainActivity.isDe2Connected()) {
			MyApplication app = (MyApplication) c.getApplicationContext();

			// Get the message from the box

			// EditText et = (EditText) findViewById(R.id.MessageText);
			String msg = key.getKeyName().toString();

			// Create an array of bytes. First byte will be the
			// message length, and the next ones will be the message

			byte buf[] = new byte[msg.length() + 1];
			buf[0] = (byte) msg.length();
			System.arraycopy(msg.getBytes(), 0, buf, 1, msg.length());

			// Now send through the output stream of the socket

			OutputStream out;
			try {
				out = app.sock.getOutputStream();
				try {
					out.write(buf, 0, msg.length() + 1);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Toast msg = Toast.makeText(PianoView.c, "not Connected",
					Toast.LENGTH_SHORT);
			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
					msg.getYOffset() / 2);
			msg.show();
		}
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
		public EventData() {
		}

		public EventData(int x, int y, int currentKey, int previousKey) {
			this.x = x;
			this.y = y;
			this.currentKey = currentKey;
			this.previousKey = previousKey;
		}

		public float x;
		public float y;
		public int currentKey;
		public int previousKey;
	}
}
