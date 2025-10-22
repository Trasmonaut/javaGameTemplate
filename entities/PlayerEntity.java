package entities;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

public class PlayerEntity {
    public int damage = 2;
    public int  health = 25;
    private final JPanel panel;
    public int x;
    public int y;
    public static int diameter = 100;
    private final Color PlayerColor = Color.decode("#58d3ca");

    private final int speed = 5;
  

    private Ellipse2D.Double player;
    private Ellipse2D.Double outline;

    private final Color backgroundColour;
    private Dimension dimension;

   

    public PlayerEntity(JPanel p, int xPos, int yPos) {
        panel = p;
        dimension = panel.getSize();

        backgroundColour = panel.getBackground();
        x = xPos;
        y = yPos;


    }
       

    public void draw() {
        Graphics g = panel.getGraphics();
        Graphics2D g2 = (Graphics2D) g;
        draw(g2); // delegate to Graphics2D-based draw
        g.dispose();
    }

    // Overload: draw onto a provided Graphics2D (e.g., an offscreen buffer)
    public void draw(Graphics2D g2) {
        player = new Ellipse2D.Double(x, y, diameter, diameter);
        outline = new Ellipse2D.Double(x-5, y-5, diameter+10, diameter+10);

        g2.setColor(Color.BLACK);
        g2.fill(outline);

        g2.setColor(PlayerColor);
        g2.fill(player);    
    }

    public void erase() {
        Graphics g = panel.getGraphics();
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(backgroundColour);
        g2.fill(new Ellipse2D.Double(x-3, y-3, diameter + 5, diameter + 5));

        g.dispose();
    }

    
   
    public void move(int direction) {
        if (!panel.isVisible()) return;

        dimension = panel.getSize();

        switch (direction) {
            case 1 -> { x -= speed; if (x < 0) x = 0; } // Left
            case 2 -> { x += speed; if (x + diameter > dimension.width) x = dimension.width - diameter; } // Right
            case 3 -> { y -= speed; if (y < 0) y = 0; } // Up
            case 4 -> { y += speed; if (y + diameter > dimension.height) y = dimension.height - diameter; } // Down
            case 5 -> { x += speed; if (x + diameter > dimension.width) x = dimension.width - diameter; y -= speed; if (y < 0) y = 0; } // Up-right
            case 6 -> { x -= speed; if (x < 0) x = 0; y -= speed; if (y < 0) y = 0; } // Up-left
            case 7 -> { x += speed; if (x + diameter > dimension.width) x = dimension.width - diameter; y += speed; if (y + diameter > dimension.height) y = dimension.height - diameter; } // Down-right
            case 8 -> { x -= speed; if (x < 0) x = 0;  y += speed; if (y + diameter > dimension.height) y = dimension.height - diameter; } // Down-left
        }

       
    }

    
    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double (x, y, diameter, diameter);
     }
  
    
}
