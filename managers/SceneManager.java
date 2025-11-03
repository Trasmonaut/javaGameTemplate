package managers;

import java.awt.Graphics2D;
import scenes.DialogueBoxEntity;
import scenes.LogoEntity;
import scenes.TransitionEntity;

/**
 * SceneManager: central controller for dialogue, logo and transition scenes.
 *
 * Responsibilities:
 * - Manage DialogueManager + DialogueBoxEntity lifecycle
 * - Manage LogoManager + LogoEntity lifecycle
 * - Manage TransitionManager + TransitionEntity lifecycle
 * - Provide a single draw(...) method to render any active overlays
 */
public class SceneManager {
    private static SceneManager instance = null;

    private final DialogueManager dialogueManager = DialogueManager.getInstance();
    private final DialogueBoxEntity dialogueBox = new DialogueBoxEntity();
    private boolean dialogueActive = false;

    private final LogoManager logoManager = LogoManager.getInstance();
    private final LogoEntity logoEntity = new LogoEntity();

    private final TransitionManager transitionManager = TransitionManager.getInstance();
    private final TransitionEntity transitionEntity = new TransitionEntity();

    private SceneManager() { }

    public static SceneManager getInstance() {
        if (instance == null) instance = new SceneManager();
        return instance;
    }

    // ---------------- Dialogue  ----------------
    public boolean showDialogueFromFile(String path) {  // loads dialogue from file and shows first line
        if (dialogueActive) {
            System.err.println("Dialogue already active; cannot start new dialogue.");
            return false;
        }
        
        boolean ok = dialogueManager.loadFromFile(path);
        if (!ok) return false;
        String first = dialogueManager.hasNext() ? dialogueManager.nextLine() : "";
        dialogueBox.setText(first);
        dialogueActive = (first != null && !first.isEmpty());
        return true;
    }

    public void advanceDialogue() {  // advances to next line or ends dialogue when file is exhausted
        if (!dialogueActive) {
            System.err.println("No active dialogue to advance.");
            return;
        }

        String next = dialogueManager.nextLine();
        if (next == null) {
            dialogueActive = false;
            dialogueBox.setText("");
        } else {
            dialogueBox.setText(next);
            dialogueActive = !next.isEmpty();
        }
    }

    public boolean isDialogueActive() {   // Check if dialogue is currently active
        return dialogueActive; 
    }

    // ---------------- Logo ----------------
    public boolean loadLogo(String path) {  // Load logo from file
        try {
            return logoManager.loadFromFile(path);
        } catch (Exception e) {
            System.err.println("Failed to load logo: " + e.getMessage());
            return false;
        }
    }

    public void showLogo(long durationMs) {  // Show loaded logo for duration
        logoManager.show(durationMs);
    }

    public boolean isLogoActive() { 
        return logoManager.isActive(); 
    }

    // ---------------- Transition ----------------
    public void showTransition(long durationMs) {
        transitionManager.show(durationMs);
    }

    public boolean isTransitionActive() { 
        return transitionManager.isActive(); 
    }

    // Returns true if any scene (dialogue/logo/transition) is active and should pause gameplay
    public boolean isAnyActive() {
        return dialogueActive || logoManager.isActive() || transitionManager.isActive();
    }

    // Central draw method: draws logo OR dialogue (logo covers full screen), then draws transition overlay on top if active
    public void draw(Graphics2D g2, int panelWidth, int panelHeight) {
        // If logo is active, draw it and skip drawing dialogue under it
        if (logoManager.isActive()) {
            logoEntity.draw(g2, panelWidth, panelHeight);
        } else {
            // draw dialogue overlay if active
            if (dialogueActive) {
                dialogueBox.draw(g2, panelWidth, panelHeight);
            }
        }

        // Draw transition overlay on top if active
        if (transitionManager.isActive()) {
            transitionEntity.draw(g2, panelWidth, panelHeight);
        }
    }
}
