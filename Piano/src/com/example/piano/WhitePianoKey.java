package src.com.example.piano;

public class WhitePianoKey extends PianoKey {
	protected int whiteKeyBaseHeight = 1;
	protected int whiteKeyBaseWidth = 1; 
	
	public  WhitePianoKey(PianoView piano, int key, float scale) {
		super(piano);
		
		int whiteKeyHeight = whiteKeyBaseHeight * (int)scale;
		int whiteKeyWidth = whiteKeyBaseWidth * (int)scale;
		rect.top = 0;
		rect.bottom = rect.top + whiteKeyHeight;
		rect.left = ( key - 1 ) * whiteKeyWidth;
		rect.right = rect.left + whiteKeyWidth;
		
		
	}
}