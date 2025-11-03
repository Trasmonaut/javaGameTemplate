package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import managers.TransitionManager;

public class ScreenTransitionEntity {
    private final TransitionManager manager = TransitionManager.getInstance();
    private long durationMs;
    private boolean active;
    private Color transitionColor = Color.BLACK;

    /**
     * Draw the transition effect on the given Graphics2D context.
     * The caller should provide the panel width/height.
     */

    public ScreenTransitionEntity() { }


    public void draw(Graphics2D g2, int panelWidth, int panelHeight) {
        if (!manager.isActive()) return;
        // fill background
        g2.setColor(transitionColor);
        g2.fillRect(0, 0, panelWidth, panelHeight);

    }
    
}
