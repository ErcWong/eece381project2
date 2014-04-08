package com.example.ece381;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.playback.NotesRecord;

public class PianoView extends View implements View.OnLongClickListener,
		View.OnClickListener {
	private static final String TIMINGTAG = "Timing";
	private static Context c;
	private final String TAG = "PianoView";
	private boolean singlePress;
	private static boolean idle;

	// private Set<Integer> keysPlayed = new HashSet<Integer>();
	private Rect filler = new Rect(0, 0, 600, 1280);
	private int offset = 50;

	public PianoView(Context context) {
		super(context);
		c = context;
		eventDataMap = new HashMap<Integer, EventData>();
		this.setOnLongClickListener(this);
		this.setOnClickListener(this);
		PianoView.idle = true;
	}

	public PianoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		c = context;
		eventDataMap = new HashMap<Integer, EventData>();
		this.setOnLongClickListener(this);
		this.setOnClickListener(this);
		PianoView.idle = true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		long startTime = SystemClock.elapsedRealtime();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(filler, paint);
		for (PianoKey key : PianoActivity.pianoKeyList) {
			paint.setColor(Color.BLACK);

			if (key.getKeyName().contains("b")) {
				if (key.isPlayed()) {
					paint.setColor(Color.parseColor("#14C1FF"));
				}
				paint.setStyle(Paint.Style.FILL);
				canvas.drawRect(key.getRect(), paint);
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(10);
				canvas.drawRect(key.getRect(), paint);
			} else {
				if (key.isPlayed()) {
					paint.setColor(Color.parseColor("#14C1FF"));
				} else {
					paint.setColor(Color.WHITE);
				}
				paint.setStyle(Paint.Style.FILL);
				canvas.drawRect(key.getRect(), paint);
				paint.setColor(Color.BLACK);
				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(10);
				canvas.drawRect(key.getRect(), paint);
			}

			paint.setStrokeWidth(2);
			paint.setStyle(Paint.Style.FILL);
			// paint white letters for flats
			if (key.getKeyName().contains("b")) {
				paint.setColor(Color.WHITE);
				offset = 40;
			} else {
				paint.setColor(Color.BLACK);
				offset = 23;
			}
			paint.setTextSize(60);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			canvas.drawText(key.getKeyName(), key.getRect().centerX() - offset,
					key.getRect().height() - 20, paint);
			// canvas.drawText(String.valueOf(key.isPlayed()), key.getRect()
			// .centerX(), key.getRect().centerY(), paint);
		}
		long endTime = SystemClock.elapsedRealtime();
		Log.d(TIMINGTAG, "Drawing took " + Long.toString(endTime - startTime));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		long startTime = SystemClock.elapsedRealtime();
		if (callBaseClass) {
			super.onTouchEvent(event);
		}

		if (!handleOnTouchEvent) {
			return false;
		}

		int action = event.getActionMasked();

		int pointerIndex = event.getActionIndex();
		int pointerId = event.getPointerId(pointerIndex);

		if (event.getPointerCount() > 1) {
			setSinglePress(false);
		} else {
			setSinglePress(true);
		}

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
					keyMove(moveEventData, (int) moveEventData.x,
							(int) moveEventData.y);
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
		// dumpEvent(event);
		long endTime = SystemClock.elapsedRealtime();
		Log.d(TIMINGTAG,
				"Touch method took " + Long.toString(endTime - startTime));
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
	public void keyPress(final int x, final int y) {
		long startTime = SystemClock.elapsedRealtime();
		for (final PianoKey key : PianoActivity.pianoKeyList) {

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (key.getKeyShape().contains(x, y) && !key.isPlayed()) {
						// toastKey(key);
						playSound(key);
						key.setPlayed(true);
						// keysPlayed.add(key.getKeyid());
						sendNote(key);
						if (PianoActivity.getRecordTimer().isTimerRunning()) {
							PianoActivity.notes.add(new NotesRecord(key
									.getKeyid(), PianoActivity.getRecordTimer()
									.getFinalTime()));
						}

					}
				}
			}, 0);
		}
		long endTime = SystemClock.elapsedRealtime();
		Log.d(TIMINGTAG, "KeyPress took " + Long.toString(endTime - startTime));
	}

	public void keyMove(EventData eD, final int x, final int y) {
		// int currentKey = eD.currentKey;
		long startTime = SystemClock.elapsedRealtime();
		for (final PianoKey key : PianoActivity.pianoKeyList) {
			if (isSinglePress()) {
				if (key.getKeyShape().contains(x, y) && !key.isPlayed()) {
					playSound(key);
					key.setPlayed(true);
					sendNote(key);
					if (PianoActivity.getRecordTimer().isTimerRunning()) {
						PianoActivity.notes.add(new NotesRecord(key.getKeyid(),
								PianoActivity.getRecordTimer().getFinalTime()));
					}
				} else if (!key.getKeyShape().contains(x, y)) {
					key.setPlayed(false);
				}
			}
		}
		long endTime = SystemClock.elapsedRealtime();
		Log.d(TIMINGTAG, "KeyMove took " + Long.toString(endTime - startTime));
	}

	// Reset key played boolean to false
	public void keyLetGo(int x, int y) {
		long startTime = SystemClock.elapsedRealtime();
		for (PianoKey key : PianoActivity.pianoKeyList) {
			if (key.getKeyShape().contains(x, y) && key.isPlayed()) {
				// toastKey(key, " let go");
				key.setPlayed(false);
				// keysPlayed.remove(key.getKeyid());
			}
		}
		long endTime = SystemClock.elapsedRealtime();
		Log.d(TIMINGTAG, "KeyLetGo took " + Long.toString(endTime - startTime));
		// printHashSet(keysPlayed);
	}

	// Plays the sound
	public void playSound(PianoKey key) {
		// PianoActivity.getSoundPool().play(key.getKeyid(), 1, 1, 0, 0,
		// key.getKeyrate());
		PianoActivity.getSoundPool().play(key.getKeyid(), 1, 1, 0, 0, 1);
	}

	// Toast the key
	public void toastKey(PianoKey key, String addOn) {
		Toast msg = Toast.makeText(PianoView.this.getContext(),
				key.getKeyName() + addOn, Toast.LENGTH_SHORT);
		msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
				msg.getYOffset() / 2);
		msg.show();
	}

	@SuppressWarnings({ "deprecation", "unused" })
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
		if (MainActivity.isDe2Connected() && !PianoView.isIdle()) {
			MyApplication app = (MyApplication) c.getApplicationContext();

			// Get the message from the box

			String msg = key.getKeyName().toString();
			
			if (msg == "Db") {
				msg = "d";
			} else if (msg == "Eb") {
				msg = "e";
			} else if (msg == "Gb") {
				msg = "g";
			} else if (msg == "Ab") {
				msg = "a";
			} else if (msg == "Bb") {
				msg = "b";
			}

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
			// Toast msg = Toast.makeText(PianoView.c, "not Connected",
			// Toast.LENGTH_SHORT);
			// msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2,
			// msg.getYOffset() / 2);
			// msg.show();
		}
	}

	public boolean isSinglePress() {
		return singlePress;
	}

	public void setSinglePress(boolean singlePress) {
		this.singlePress = singlePress;
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

	public static boolean isIdle() {
		return idle;
	}

	public static void setIdle(boolean idle) {
		PianoView.idle = idle;
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

		public EventData(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public float x;
		public float y;
	}
}
