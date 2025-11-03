package scenes;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import managers.TransitionManager;

/**
 * Renders a full-screen transition overlay using opacity from TransitionManager.
 */
public class TransitionEntity {
    private final TransitionManager manager = TransitionManager.getInstance();
    private Color color = Color.BLACK;

    public TransitionEntity() { }

    public void setColor(Color c) { color = (c == null) ? Color.BLACK : c; }

    public void draw(Graphics2D g2, int panelWidth, int panelHeight) {
        if (!manager.isActive()) return;
        
        float opacity = manager.getOpacity();
        if (opacity <= 0f) return;

        AlphaComposite old = (AlphaComposite) g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2.setColor(color);
        g2.fillRect(0, 0, panelWidth, panelHeight);
        g2.setComposite(old);
    }
}
