package com.example.ece381;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

public class PianoActivity extends ActionBarActivity {
	PianoView vw;
	private static SoundPool soundPool;
	private static int width;
	private static int height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		vw = new PianoView(this);
		setContentView(vw);

		// if (savedInstanceState == null) {
		// getSupportFragmentManager().beginTransaction()
		// .add(R.id.container, new PlaceholderFragment()).commit();
		// }
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
		PianoView.keya.setKeyid(getSoundPool().load(this, R.raw.keya, 1)); 
		PianoView.keyab.setKeyid(getSoundPool().load(this, R.raw.keyab, 1));
		PianoView.keyb.setKeyid(getSoundPool().load(this, R.raw.keyb, 1));
		PianoView.keybb.setKeyid(getSoundPool().load(this, R.raw.keybb, 1));
		PianoView.keyc.setKeyid(getSoundPool().load(this, R.raw.keyc, 1));
		PianoView.keyd.setKeyid(getSoundPool().load(this, R.raw.keyd, 1));
		PianoView.keydb.setKeyid(getSoundPool().load(this, R.raw.keydb, 1));
		PianoView.keye.setKeyid(getSoundPool().load(this, R.raw.keye, 1));
		PianoView.keyeb.setKeyid(getSoundPool().load(this, R.raw.keyeb, 1));
		PianoView.keyf.setKeyid(getSoundPool().load(this, R.raw.keyf, 1));
		PianoView.keyg.setKeyid(getSoundPool().load(this, R.raw.keyg, 1));
		PianoView.keygb.setKeyid(getSoundPool().load(this, R.raw.keygb, 1));
		screenSize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.piano, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void screenSize(){
		Display display = getWindowManager().getDefaultDisplay();
		setWidth(display.getWidth());
		setHeight(display.getHeight());
		System.out.println("Width" + width + " Height" + getHeight());
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
