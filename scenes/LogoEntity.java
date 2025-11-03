package scenes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import managers.LogoManager;

/**
 * LogoEntity: lightweight renderer that displays the currently active logo (from LogoManager).
 * When active it clears the screen with a background color and draws the logo centered and
 * scaled so it occupies the screen while preserving aspect ratio.
 */
public class LogoEntity {
    private final LogoManager manager = LogoManager.getInstance();
    private Color backgroundColor = Color.BLACK;
    private double scale = 1.0; // scale factor for logo rendering

    public LogoEntity() { }

    public void setBackgroundColor(Color c) {
        this.backgroundColor = (c == null) ? Color.BLACK : c;
    }

    /**
     * Draw the active logo (if any) onto the provided Graphics2D context. The caller should
     * pass the panel width/height so the logo can be centered and scaled.
     */
    public void draw(Graphics2D g2, int panelWidth, int panelHeight) {
        if (!manager.isActive()) return;
        BufferedImage logo = manager.getLogo();
        if (logo == null) return;

        // fill background
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, panelWidth, panelHeight);

        int iw = logo.getWidth();
        int ih = logo.getHeight();
        if (iw <= 0 || ih <= 0) return;

        // Compute scale to cover the panel while preserving aspect ratio (cover behavior)
       

        int drawW = (int) Math.round(iw * scale);
        int drawH = (int) Math.round(ih * scale);

        int x = (panelWidth - drawW) / 2;
        int y = (panelHeight - drawH) / 2;

        g2.drawImage(logo, x, y, drawW, drawH, null);
    }


    /** Show the logo for the specified duration (milliseconds). */
    public void showLogo(long durationMs) {
        manager.show(durationMs);
    }
}
