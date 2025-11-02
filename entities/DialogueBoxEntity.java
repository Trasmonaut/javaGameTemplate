package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * DialogueBoxEntity: minimal text box overlay.
 * Call setText(...) then draw(g2) from your panel's render method.
 */
public class DialogueBoxEntity{
    
    private String text = "";
    private final int padding = 12;
    private final Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 18);

    // Where to draw (relative to panel): bottom bar height
    private final int boxHeight = 100;

    public void setText(String text) {
        this.text = (text == null) ? "" : text;
    }

    /** Draw a semi-transparent box at the bottom with the current text. */
    public void draw(Graphics2D g2, int panelWidth, int panelHeight) {
        // Box background
        int x = 0;
        int y = panelHeight - boxHeight;
        int w = panelWidth;
        int h = boxHeight;

        g2.setColor(Color.black);
        g2.fillRect(x, y, w, h);

        // Text
        g2.setFont(font);
        g2.setColor(Color.WHITE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics fm = g2.getFontMetrics();

        // Simple single-line render; keep it minimal
        int textY = y + padding + fm.getAscent();
        g2.drawString(text, x + padding, textY);
    }

    /** Returns the current text. For debugging purposes. 
     * 
    */
    public String getText() {
        return text;
    }
}
