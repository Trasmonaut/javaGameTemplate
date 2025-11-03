package managers;

import java.awt.image.BufferedImage;
// ImageManager is in the same package; no import needed

/**
 * LogoManager: singleton that loads a logo image and controls showing it for a timed duration.
 * Similar to DialogueManager but for a single logo image.
 */
public class LogoManager {
    private static LogoManager instance = null;

    private BufferedImage logo = null;
    private boolean active = false;
    private long endTimeMs = 0L;

    public LogoManager() { }

    public static LogoManager getInstance() {
        if (instance == null) instance = new LogoManager();
        return instance;
    }

    /**
     * Load a logo image from the given filesystem path. Returns true on success.
     */
    public boolean loadFromFile(String path) {
        BufferedImage img = ImageManager.loadBufferedImage(path);
        if (img == null) {
            System.out.println("LogoManager: failed to load image: " + path);
            logo = null;
            return false;
        }
        logo = img;
        return true;
    }

    /** Show the currently loaded logo for the given duration in milliseconds. */
    public void show(long durationMs) {
        if (logo == null) return;
        if (active) {
            return;
        }
        active = true;
        endTimeMs = System.currentTimeMillis() + Math.max(0, durationMs);
        System.out.println("LogoManager: showing logo for " + durationMs + "ms");

        if (System.currentTimeMillis() > endTimeMs) {
            reset();
        }

    }

    /** Hide the logo immediately. */
    public void hide() {
        active = false;
        endTimeMs = 0L;
    }

    /** Returns true if the logo should currently be displayed. This also updates the internal timer. */
    public boolean isActive() {
        if (!active) return false;
        if (System.currentTimeMillis() > endTimeMs) {
            active = false;
            return false;
        }
        return true;
    }

    public BufferedImage getLogo() {
        return logo;
    }

    /** Reset manager state and unload image. */
    public void reset() {
        logo = null;
        active = false;
        endTimeMs = 0L;
    }
}

