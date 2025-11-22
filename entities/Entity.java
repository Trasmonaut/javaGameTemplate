package entities;

import java.awt.geom.Rectangle2D;

// Entity interface defines the basic functionalities for all game entities.
// This can be used for all entities, and for static entities as well.

public interface Entity { 
    void setPosition(int x, int y);
    void draw();  
    Rectangle2D.Double getBoundingRectangle();
    boolean collidesWith(Entity other);
}
