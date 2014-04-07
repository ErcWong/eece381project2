package com.example.playback;

import android.os.SystemClock;

public class Timer{
	private long start;
	private long finish;
	private boolean timerRunning;
	
	public Timer(){
		this.setTimerRunning(false);
	}

	// Start the timer
	public void startTimer() {
		long start = SystemClock.elapsedRealtime();
		setStart(start);
		setTimerRunning(true);
	}
	
	// Stop the timer
	public void stopTimer() {
		long time = SystemClock.elapsedRealtime();
		setFinish(time);
		setTimerRunning(false);
	}
	
	// Return time at any given moment
	public long timestamp() {
		long time = SystemClock.elapsedRealtime();
		return time;
	}
	
	// Return total elapsed time
	public String totalElapsedTime() {
		String time = String.valueOf(getFinish()-getStart());
		return time;
	}
	
	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getFinish() {
		return finish;
	}

	public void setFinish(long finish) {
		this.finish = finish;
	}

	public boolean isTimerRunning() {
		return timerRunning;
	}

	public void setTimerRunning(boolean timerRunning) {
		this.timerRunning = timerRunning;
	}
	
	
}