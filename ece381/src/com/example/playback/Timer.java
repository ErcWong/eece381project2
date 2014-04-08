package com.example.playback;

import android.os.Handler;
import android.os.SystemClock;

public class Timer {
	private long startTime;
	private long timeInMillies;
	private long timeSwap;
	private long finalTime;
	private boolean timerRunning;
	private Handler myHandler = new Handler();

	public Timer() {
		this.setTimerRunning(false);
		this.setStartTime(0);
		this.setTimeInMillies(0);
		this.setTimeSwap(0);
		this.setFinalTime(0);
	}

	// Start the timer
	public void startTimer() {
		long start = SystemClock.uptimeMillis();
		setStartTime(start);
		setTimeInMillies(0);
		setTimeSwap(0);
		setFinalTime(0);
		setTimerRunning(true);
		myHandler.postDelayed(updateTimerMethod, 0);
	}

	// Pause the timer
	public void pauseTimer(){
		setTimerRunning(false);
		timeSwap += timeInMillies;
		myHandler.removeCallbacks(updateTimerMethod);
	}
	
	// Resume the timer
	public void resumeTimer() {
		long start = SystemClock.uptimeMillis();
		setStartTime(start);
		setTimerRunning(true);
		myHandler.postDelayed(updateTimerMethod, 0);
	}
	
	// Stop the timer
	public void stopTimer() {
		setTimerRunning(false);
		myHandler.removeCallbacks(updateTimerMethod);
	}

	// Return time at any given moment
	public long timestamp() {
		long time = SystemClock.elapsedRealtime();
		return time;
	}

	// Return total elapsed time
	public String totalElapsedTime() {
		String time = String.valueOf(0);
		return time;
	}

	private Runnable updateTimerMethod = new Runnable() {
		@Override
		public void run() {
			timeInMillies = SystemClock.uptimeMillis() - startTime;
			finalTime = timeSwap + timeInMillies;
			myHandler.postDelayed(this, 0);
		}
	};

	public boolean isTimerRunning() {
		return timerRunning;
	}

	public void setTimerRunning(boolean timerRunning) {
		this.timerRunning = timerRunning;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getTimeInMillies() {
		return timeInMillies;
	}

	public void setTimeInMillies(long timeInMillies) {
		this.timeInMillies = timeInMillies;
	}

	public long getTimeSwap() {
		return timeSwap;
	}

	public void setTimeSwap(long timeSwap) {
		this.timeSwap = timeSwap;
	}

	public long getFinalTime() {
		return finalTime;
	}

	public void setFinalTime(long finalTime) {
		this.finalTime = finalTime;
	}

}