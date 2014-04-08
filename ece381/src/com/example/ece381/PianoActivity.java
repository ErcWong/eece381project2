package com.example.ece381;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import com.example.playback.NotesRecord;
import com.example.playback.Timer;

public class PianoActivity extends ActionBarActivity {
	private PianoView pianoView;
	private static Timer recordTimer;
	private static SoundPool soundPool;
	private static int width;
	private static int height;
	private static int whtKey = 7;
	private static int whtBlkKey = 12;
	private static int keyRatio1 = 2;
	private static int keyRatio2 = 3;
	private static int keyWidth = 107;

	public static List<PianoKey> pianoKeyList = new ArrayList<PianoKey>();
	public static List<NotesRecord> notes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		long startTime = SystemClock.elapsedRealtime();
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		pianoView = new PianoView(this);
		setContentView(pianoView);

		// pianoView = (PianoView) findViewById(R.id.PianoView);
		// setContentView(R.layout.activity_piano);

		// Initiate timer
		recordTimer = new Timer();

		// Set the hardware buttons to control the music
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Load the sound
		setSoundPool(new SoundPool(10, AudioManager.STREAM_MUSIC, 0));
		getSoundPool().setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				PianoKey.loaded = true;
			}
		});

		// Get screen sizes
		screenSize();

		// Add keys to the list
		pianoKeyList.clear();
		addWhiteKeysHorizontal();
		addBlackKeysHorizontal();
		// addWhiteKeys();
		// addBlackKeys();

		// Check key dimensions
		for (PianoKey key : pianoKeyList) {
			System.out.println(key.getKeyName()
					+ key.getKeyShape().getRectLeft()
					+ key.getKeyShape().getRectRight());
		}

		// make sure sounds are loaded
		if (PianoKey.loaded = false) {
			System.out.println("PianoActivity not loaded");
			return;
		}
		long endTime = SystemClock.elapsedRealtime();
		Log.d("PianoActTiming",
				"PianoActivity took " + Long.toString(endTime - startTime));
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// return super.onTouchEvent(event);
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.piano, menu);
		View toggle = LayoutInflater.from(this).inflate(
				R.layout.switch_actionbar, null);
		getSupportActionBar().setCustomView(toggle);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		Switch switchActionBar = (Switch) findViewById(R.id.SwitchForActionBar);
		switchActionBar
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							PianoView.setIdle(false);
						} else {
							PianoView.setIdle(true);
						}
					}
				});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.record:
			if (!recordTimer.isTimerRunning()) {
				notes = new ArrayList<NotesRecord>();
				Toast.makeText(this, "Record", Toast.LENGTH_SHORT).show();
				recordTimer.startTimer();
				NotesRecord.startTime = recordTimer.getStartTime();
				notes.add(new NotesRecord(99, NotesRecord.startTime));
			} else {
				recordTimer.stopTimer();
				Toast.makeText(this, recordTimer.totalElapsedTime(),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.pause:
			if (notes != null) {
				if (!recordTimer.isTimerRunning()) {
					recordTimer.resumeTimer();
				} else {
					recordTimer.pauseTimer();
				}
			}
			break;
		case R.id.playback:
			showMemory("Before Playback");
			if (notes != null) {
				for (NotesRecord note : notes) {
					System.out.println("Notes:" + note.key + " ivl:"
							+ note.interval);
				}
				for (final NotesRecord note : notes) {
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							if (note.key != 99) {
								PianoActivity.getSoundPool().play(note.key, 1,
										1, 0, 0, 1);
								PianoActivity.pianoKeyList.get(note.key - 1)
										.setPlayed(true);
								pianoView.invalidate();
								Handler handler = new Handler();
								handler.postDelayed(new Runnable() {

									@Override
									public void run() {
										PianoActivity.pianoKeyList.get(
												note.key - 1).setPlayed(false);
										pianoView.invalidate();
									}
								}, 150);
							}
						}
					}, note.interval);
				}
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("deprecation")
	public void screenSize() {
		int actionBarHeight;
		TypedValue tv = new TypedValue();
		if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
					getResources().getDisplayMetrics());
		} else {
			actionBarHeight = 0;
		}
		Display display = getWindowManager().getDefaultDisplay();
		setWidth(display.getWidth());
		// setWidth(600);
		setHeight(display.getHeight() - actionBarHeight);
	}

	public void addWhiteKeysHorizontal() {
		pianoKeyList.add(new PianoKey("C", getSoundPool().load(this,
				R.raw.keyc, 1), false, 1,
				new Rect(1, 1, width / whtKey, height), new Rect(1, height
						* keyRatio1 / keyRatio2, width / whtKey, height),
				new Rect(1, 1, width * 1 / whtBlkKey + 5, height * keyRatio1
						/ keyRatio2)));
		pianoKeyList.add(new PianoKey("D", getSoundPool().load(this,
				R.raw.keyd, 1), false, 1, new Rect(width * 1 / whtKey, 1, width
				* 2 / whtKey, height), new Rect(width * 1 / whtKey, height
				* keyRatio1 / keyRatio2, width * 2 / whtKey, height), new Rect(
				width * 2 / whtBlkKey + 5, 1, width * 3 / whtBlkKey + 10,
				height * keyRatio1 / keyRatio2)));
		pianoKeyList.add(new PianoKey("E", getSoundPool().load(this,
				R.raw.keye, 1), false, 1, new Rect(width * 2 / whtKey, 1, width
				* 3 / whtKey, height), new Rect(width * 2 / whtKey, height
				* keyRatio1 / keyRatio2, width * 3 / whtKey, height), new Rect(
				width * 4 / whtBlkKey + 10, 1, width * 5 / whtBlkKey, height
						* keyRatio1 / keyRatio2)));
		pianoKeyList.add(new PianoKey("F", getSoundPool().load(this,
				R.raw.keyf, 1), false, 1, new Rect(width * 3 / whtKey, 1, width
				* 4 / whtKey, height), new Rect(width * 3 / whtKey, height
				* keyRatio1 / keyRatio2, width * 4 / whtKey, height), new Rect(
				width * 5 / whtBlkKey + 10, 1, width * 6 / whtBlkKey + 20,
				height * keyRatio1 / keyRatio2)));
		pianoKeyList.add(new PianoKey("G", getSoundPool().load(this,
				R.raw.keyg, 1), false, 1, new Rect(width * 4 / whtKey, 1, width
				* 5 / whtKey, height), new Rect(width * 4 / whtKey, height
				* keyRatio1 / keyRatio2, width * 5 / whtKey + 3, height),
				new Rect(width * 7 / whtBlkKey + 20, 1, width * 8 / whtBlkKey
						+ 10, height * keyRatio1 / keyRatio2)));
		pianoKeyList.add(new PianoKey("A", getSoundPool().load(this,
				R.raw.keya, 1), false, 1, new Rect(width * 5 / whtKey, 1, width
				* 6 / whtKey, height), new Rect(width * 5 / whtKey + 3, height
				* keyRatio1 / keyRatio2, width * 6 / whtKey, height), new Rect(
				width * 9 / whtBlkKey + 10, 1, width * 10 / whtBlkKey, height
						* keyRatio1 / keyRatio2)));
		pianoKeyList.add(new PianoKey("B", getSoundPool().load(this,
				R.raw.keyb, 1), false, 1, new Rect(width * 6 / whtKey, 1, width
				* 7 / whtKey, height), new Rect(width * 6 / whtKey, height
				* keyRatio1 / keyRatio2, width * 7 / whtKey, height), new Rect(
				width * 11 / whtBlkKey, 1, width * 12 / whtBlkKey, height
						* keyRatio1 / keyRatio2)));
	}

	public void addBlackKeysHorizontal() {
		// Black Keys: Db Eb Gb Ab Bb
		pianoKeyList.add(new PianoKey("Db", getSoundPool().load(this,
				R.raw.keydb, 1), false, 1, new Rect(width / whtBlkKey + 5, 1,
				width * 2 / whtBlkKey + 5, height * 2 / 3), new Rect(width
				/ whtBlkKey + 5, 1, width * 2 / whtBlkKey + 5, height * 2 / 3),
				new Rect()));
		pianoKeyList.add(new PianoKey("Eb", getSoundPool().load(this,
				R.raw.keyeb, 1), false, 1, new Rect(width * 3 / whtBlkKey + 10,
				1, width * 4 / whtBlkKey + 10, height * 2 / 3), new Rect(width
				* 3 / whtBlkKey + 10, 1, width * 4 / whtBlkKey + 10,
				height * 2 / 3), new Rect()));
		pianoKeyList.add(new PianoKey("Gb", getSoundPool().load(this,
				R.raw.keygb, 1), false, 1, new Rect(width * 6 / whtBlkKey + 20,
				1, width * 7 / whtBlkKey + 20, height * 2 / 3), new Rect(width
				* 6 / whtBlkKey + 20, 1, width * 7 / whtBlkKey + 20,
				height * 2 / 3), new Rect()));
		pianoKeyList.add(new PianoKey("Ab", getSoundPool().load(this,
				R.raw.keyab, 1), false, 1, new Rect(width * 8 / whtBlkKey + 10,
				1, width * 9 / whtBlkKey + 10, height * 2 / 3), new Rect(width
				* 8 / whtBlkKey + 10, 1, width * 9 / whtBlkKey + 10,
				height * 2 / 3), new Rect()));
		pianoKeyList.add(new PianoKey("Bb", getSoundPool().load(this,
				R.raw.keybb, 1), false, 1, new Rect(width * 10 / whtBlkKey, 1,
				width * 11 / whtBlkKey, height * 2 / 3), new Rect(width * 10
				/ whtBlkKey, 1, width * 11 / whtBlkKey, height * 2 / 3),
				new Rect()));
	}

	public void addWhiteKeys() {
		// White keys: C D E F G A B
		pianoKeyList.add(new PianoKey("keyC", getSoundPool().load(this,
				R.raw.keyc, 1), false, 1,
				new Rect(1, 1, width, height / whtKey), new Rect(1, 1, width
						* keyRatio1 / keyRatio2, height / whtKey), new Rect(
						width * keyRatio1 / keyRatio2, 1, width, height * 1
								/ whtBlkKey)));

		pianoKeyList.add(new PianoKey("keyD", getSoundPool().load(this,
				R.raw.keyd, 1), false, 1, new Rect(1, height / whtKey, width,
				height * 2 / whtKey), new Rect(1, height / whtKey, width
				* keyRatio1 / keyRatio2, height * 2 / whtKey), new Rect(width
				* keyRatio1 / keyRatio2, height * 2 / whtBlkKey, width, height
				* 3 / whtBlkKey)));

		pianoKeyList.add(new PianoKey("keyE", getSoundPool().load(this,
				R.raw.keye, 1), false, 1, new Rect(1, height * 2 / whtKey,
				width, height * 3 / whtKey), new Rect(1, height * 2 / whtKey,
				width * keyRatio1 / keyRatio2, height * 3 / whtKey), new Rect(
				width * keyRatio1 / keyRatio2, height * 4 / whtBlkKey, width,
				height * 5 / whtBlkKey)));

		pianoKeyList.add(new PianoKey("keyF", getSoundPool().load(this,
				R.raw.keyf, 1), false, 1, new Rect(1, height * 3 / whtKey + 1,
				width, height * 4 / whtKey), new Rect(1, height * 3 / whtKey
				+ 1, width * keyRatio1 / keyRatio2, height * 4 / whtKey),
				new Rect(width * keyRatio1 / keyRatio2, height * 5 / whtBlkKey,
						width, height * 6 / whtBlkKey)));

		pianoKeyList.add(new PianoKey("keyG", getSoundPool().load(this,
				R.raw.keyg, 1), false, 1, new Rect(1, height * 4 / whtKey,
				width, height * 5 / whtKey), new Rect(1, height * 4 / whtKey,
				width * keyRatio1 / keyRatio2, height * 5 / whtKey), new Rect(
				width * keyRatio1 / keyRatio2, height * 7 / whtBlkKey, width,
				height * 8 / whtBlkKey)));

		pianoKeyList.add(new PianoKey("keyA", getSoundPool().load(this,
				R.raw.keya, 1), false, 1, new Rect(1, height * 5 / whtKey,
				width, height * 6 / whtKey), new Rect(1, height * 5 / whtKey,
				width * keyRatio1 / keyRatio2, height * 6 / whtKey), new Rect(
				width * keyRatio1 / keyRatio2, height * 9 / whtBlkKey, width,
				height * 10 / whtBlkKey)));

		pianoKeyList.add(new PianoKey("keyB", getSoundPool().load(this,
				R.raw.keyb, 1), false, 1, new Rect(1, height * 6 / whtKey,
				width, height * 7 / whtKey), new Rect(1, height * 6 / whtKey,
				width * keyRatio1 / keyRatio2, height * 7 / whtKey), new Rect(
				width * keyRatio1 / keyRatio2, height * 11 / whtBlkKey, width,
				height * whtBlkKey / whtBlkKey)));
	}

	public void addBlackKeys() {
		// Black Keys: Db Eb Gb Ab Bb
		pianoKeyList.add(new PianoKey("keyDb", getSoundPool().load(this,
				R.raw.keydb, 1), false, 1,
				new Rect(width * keyRatio1 / keyRatio2, height / whtBlkKey,
						width, height * 2 / whtBlkKey), new Rect(width
						* keyRatio1 / keyRatio2, height / whtBlkKey, width,
						height * 2 / whtBlkKey), new Rect()));
		pianoKeyList.add(new PianoKey("keyEb", getSoundPool().load(this,
				R.raw.keyeb, 1), false, 1, new Rect(width * keyRatio1
				/ keyRatio2, height * 3 / whtBlkKey, width, height * 4
				/ whtBlkKey), new Rect(width * keyRatio1 / keyRatio2, height
				* 3 / whtBlkKey, width, height * 4 / whtBlkKey), new Rect()));
		pianoKeyList.add(new PianoKey("keyGb", getSoundPool().load(this,
				R.raw.keygb, 1), false, 1, new Rect(width * keyRatio1
				/ keyRatio2, height * 6 / whtBlkKey + 13, width, height * 7
				/ whtBlkKey + 13),
				new Rect(width * keyRatio1 / keyRatio2, height * 6 / whtBlkKey
						+ 13, width, height * 7 / whtBlkKey + 13), new Rect()));
		pianoKeyList.add(new PianoKey("keyAb", getSoundPool().load(this,
				R.raw.keyab, 1), false, 1, new Rect(width * keyRatio1
				/ keyRatio2, height * 8 / whtBlkKey + 11, width, height * 9
				/ whtBlkKey + 11),
				new Rect(width * keyRatio1 / keyRatio2, height * 8 / whtBlkKey
						+ 11, width, height * 9 / whtBlkKey + 11), new Rect()));
		pianoKeyList.add(new PianoKey("keyBb", getSoundPool().load(this,
				R.raw.keybb, 1), false, 1, new Rect(width * keyRatio1
				/ keyRatio2, height * 10 / whtBlkKey, width, height * 11
				/ whtBlkKey), new Rect(width * keyRatio1 / keyRatio2, height
				* 10 / whtBlkKey, width, height * 11 / whtBlkKey), new Rect()));
	}

	public static void showMemory(String label) {
		Runtime rt = Runtime.getRuntime();
		long free = rt.freeMemory();
		long total = rt.totalMemory();
		long used = total - free;
		System.out.println(label + "free:" + free + " total:" + total
				+ " used:" + used);
	}

	public static SoundPool getSoundPool() {
		return soundPool;
	}

	public void setSoundPool(SoundPool soundPool) {
		PianoActivity.soundPool = soundPool;
	}

	public static int getHeight() {
		return height;
	}

	public static void setHeight(int height) {
		PianoActivity.height = height;
	}

	public static int getWidth() {
		return width;
	}

	public static void setWidth(int width) {
		PianoActivity.width = width;
	}

	public static Timer getRecordTimer() {
		return recordTimer;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_piano,
					container, false);
			return rootView;
		}
	}

}
