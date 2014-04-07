package com.example.playback;

// Class used to record note
public class NotesRecord {

	public int key;
	public long interval;
	public static long startTime;

	public NotesRecord() {
	}

	public NotesRecord(int key, long interval) {
		this.key = key;
		this.interval = interval;
	}

}