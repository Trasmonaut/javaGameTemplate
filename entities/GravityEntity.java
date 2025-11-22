package entities;

// GravityEntity interface extends MoveableEntity to include gravity-related functionalities for game entities.


public interface GravityEntity extends MoveableEntity {
    void applyGravity();
    void calculateFallSpeed();
    void resetFallSpeed();
    void jump();
    void fall();
}
