package managers;

/**
 * TransitionManager: controls a timed full-screen transition overlay.
 * Call show(totalDurationMs) to activate. The overlay remains fully opaque
 * for (totalDurationMs - fadeDurationMs) then fades out over fadeDurationMs.
 */
public class TransitionManager {
    private static TransitionManager instance = null;

    private boolean active = false;
    private long endTimeMs = 0L;
    private long startTimeMs = 0L;

    // Fade timings (in milliseconds). By default we fade in then out 200ms each.
  
    private long fadeInMs = 200;
    private long fadeOutMs = 200;

    public TransitionManager() { }

    public static TransitionManager getInstance() {
        if (instance == null) instance = new TransitionManager();
        return instance;
    }

    /**
     * Show the transition for totalDurationMs milliseconds (including fade in/out).
     * The timeline is: [fadeIn] -> [hold] -> [fadeOut]. Fade durations are taken from
     * the manager settings (fadeInMs/fadeOutMs). If totalDurationMs is shorter than
     * fadeInMs + fadeOutMs, both fades are scaled down proportionally so they fit.
     *
     * Example: show(2000) with default fades 200/200 => 200ms fadeIn, 1600ms hold, 200ms fadeOut.
     */
    public void show(long totalDurationMs) {
        if (totalDurationMs < 0) totalDurationMs = 0;

        // If total < sum of fades, scale fades proportionally to fit
        long sumFades = fadeInMs + fadeOutMs;
        long scaledFadeIn = fadeInMs;
        long scaledFadeOut = fadeOutMs;
        if (totalDurationMs < sumFades && sumFades > 0) {
            double factor = (double) totalDurationMs / (double) sumFades;
            scaledFadeIn = Math.max(0, (long) Math.round(fadeInMs * factor));
            scaledFadeOut = Math.max(0, (long) Math.round(fadeOutMs * factor));
        }

        startTimeMs = System.currentTimeMillis();
        endTimeMs = startTimeMs + totalDurationMs;
        this._lastFadeInMs = scaledFadeIn;
        this._lastFadeOutMs = scaledFadeOut;
        this._lastTotalMs = totalDurationMs;
        active = true;
    }

    /** Hide immediately. */
    public void hide() {
        active = false;
    }

    /** Returns true while the transition is active (including fade period). */
    public boolean isActive() {
        if (!active) return false;
        if (System.currentTimeMillis() > endTimeMs) {
            active = false;
            return false;
        }
        return true;
    }

    /** Returns the current opacity (0.0 to 1.0) of the transition overlay. */
    public float getOpacity() {
        if (!active) return 0f;
        long now = System.currentTimeMillis();
        long elapsed = now - startTimeMs;
        long total = this._lastTotalMs;
        if (elapsed >= total) {
            active = false;
            return 0f;
        }

        long fi = this._lastFadeInMs;
        long fo = this._lastFadeOutMs;
        long holdStart = fi;
        long holdEnd = total - fo;

        if (elapsed < fi) {
            // fade in: 0 -> 1
            if (fi == 0) return 1f;
            return Math.max(0f, Math.min(1f, (float) elapsed / (float) fi));
        } else if (elapsed >= holdStart && elapsed < holdEnd) {
            // hold (fully opaque)
            return 1.0f;
        } else {
            // fade out: 1 -> 0
            long t = elapsed - holdEnd; // 0..fo
            if (fo == 0) return 0f;
            return Math.max(0f, 1.0f - (float) t / (float) fo);
        }
    }
    // Internal fields set on show() to record the actual fades used during the last show() call
    private long _lastFadeInMs = fadeInMs;
    private long _lastFadeOutMs = fadeOutMs;
    private long _lastTotalMs = 0L;

    // Accessors for default fade durations; change these to adjust how future shows behave
    public long getDefaultFadeInMs() {
        return fadeInMs; 
    }

    public void setDefaultFadeInMs(long d) { 
        fadeInMs = Math.max(0, d); 
    }

    public long getDefaultFadeOutMs() { 
        return fadeOutMs; 
    }
    public void setDefaultFadeOutMs(long d) { 
        fadeOutMs = Math.max(0, d); 
    }

}
