package entities;


// MoveableEntity interface extends Entity to include movement capabilities for game entities.

public interface  MoveableEntity extends Entity {
    void move(int deltaX, int deltaY);
    void update();
    
    
}
