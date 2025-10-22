import java.awt.Graphics2D;

public interface ImageFX {
	public void update();
	public abstract void draw(Graphics2D g2, boolean facing, int x);
}