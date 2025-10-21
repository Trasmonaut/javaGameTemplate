import java.io.*;		// for playing sound clips
import java.util.HashMap;
import java.util.Random;
import javax.sound.sampled.*;

public class SoundManager {				// a Singleton class
	HashMap<String, Clip> clips;


	private Random random;

	private static SoundManager instance = null;	// keeps track of Singleton instance

	private float volume;

	private SoundManager () {
		clips = new HashMap<String, Clip>();
		random = new Random();

		// Clip clip = loadClip("sounds/background.wav");	// played from start of the game
		// clips.put("background", clip);

		
	}


	public static SoundManager getInstance() {	// class method to retrieve instance of Singleton
		if (instance == null)
			instance = new SoundManager();
		
		return instance;
	}		


    	public Clip loadClip (String fileName) {	// gets clip from the specified file
 		AudioInputStream audioIn;
		Clip clip = null;

		try {
    			File file = new File(fileName);
    			audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL()); 
    			clip = AudioSystem.getClip();
    			clip.open(audioIn);
		}
		catch (Exception e) {
 			System.out.println ("Error opening sound files: " + e);
		}
    		return clip;
    	}


	public Clip getClip (String title) {

		return clips.get(title);
	}


    	public void playClip(String title, boolean looping) { // plays the specified clip, optionally looping if looping is true
		Clip clip = getClip(title);
		if (clip != null) {
			clip.setFramePosition(0);
			if (looping)
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			else
				clip.start();
		}
    	}


    	public void stopClip(String title) {  // stops the specified clip
		Clip clip = getClip(title);
		if (clip != null) {
			clip.stop();
		}
    	}

	public void setVolume (String title, float volume) {
		Clip clip = getClip(title);

		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * volume) + gainControl.getMinimum();

		gainControl.setValue(gain);
	}


	public void playRandomClip(String baseName, int count, boolean looping) {  // plays a random clip from a set of clips with names baseName1, baseName2, ..., baseNameN
        int randomIndex = random.nextInt(count) + 1;
        String randomClipName = baseName + randomIndex;
		System.out.println(randomClipName);
        playClip(randomClipName, looping);
    }
	
}