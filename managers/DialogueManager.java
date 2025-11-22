package managers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * DialogueManager: loads dialogue lines from a text file and returns them one-by-one.
 * 
 */

public class DialogueManager {
    private final List<String> lines = new ArrayList<>();
    private int index = 0;
    private static DialogueManager instance = null;	// keeps track of Singleton instance

    /** Create an empty manager; call loadFromFile before use. */
    public DialogueManager() { }

    public static DialogueManager getInstance() {	// class method to retrieve instance of Singleton
		if (instance == null)
			instance = new DialogueManager();

		return instance;
	}	

    public boolean loadFromFile(String filePath) {
        lines.clear();
        index = 0;
        File f = new File(filePath);
        if (!f.exists()) {
            System.out.println("Dialogue file not found: " + filePath);
            return false;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            System.out.println("Dialogue loaded.");
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error reading dialogue file: " + e.getMessage());
            return false;
        }
    }

    /** Returns true if there are more lines to read. */
    public boolean hasNext() {
        return index < lines.size();
    }

    /** Returns the next line (or null if none left). */
    public String nextLine() {
        if (!hasNext()) return null;
        String s = lines.get(index);
        
        index++;
        return s;
    }

    /** Reset reading to the start. */
    public void reset() {
        index = 0;
    }
}
