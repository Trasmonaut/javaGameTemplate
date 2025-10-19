import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * GamePanel: minimal, self-contained game surface for the template.
 * - Owns a PlayerEntity and renders it to an offscreen buffer.
 * - Provides a simple game loop thread with start/stop flags.
 * - Exposes simple actions: updateGameEntities(direction), swung(), shield().
 *
 * Keep it simple so you can copy this into new projects and customize.
 */
public class GamePanel extends JPanel {

    private PlayerEntity player;
    private int screenwidth = GameWindow.screenWidth;
    private int screenheight = GameWindow.screenHeight;

    private Timer gameTimer;
    private boolean isStarted;
    private boolean isRunning;

    private final BufferedImage backBuffer; // offscreen buffer to draw into

    public GamePanel() {
        setBackground(Color.white);
        // Match the size set in GameWindow (1000x700)
        backBuffer = new BufferedImage(screenwidth, screenheight, BufferedImage.TYPE_INT_RGB);
    }

    /** Create initial entities for a new game. */
    public void createGameEntities() {
        // Start player roughly at center
        player = new PlayerEntity(this, screenwidth/2 - PlayerEntity.diameter/2, screenheight/2 - PlayerEntity.diameter/2);
        GameWindow.updatePointChecker(0);
        GameWindow.updatePlayerHealht(player.health);
    }

    /** Update entities based on a direction code (1..8). */
    public void updateGameEntities(int direction) {
        if (!isRunning || player == null) return;
        player.move(direction);
    }

    /** Advance non-input game logic per tick (kept minimal). */
    public void updateGameEntities() {
        // No-op in the template; extend with your own logic/timers.
    }

    /** Simple action called on left-click in GameWindow. */
    public void swung() {
        if (player == null) return;
        // In a real game you'd check collisions using player.HitAreaRectangle().
        // Here we just grant a point to demonstrate updating the UI.
        GameWindow.updatePointChecker(1);
    }

    /** Simple toggle action called on right press/release in GameWindow. */
    public void sheild() {
        // Placeholder for shield toggle; keep minimal as a template.
        // You can wire this into PlayerEntity if desired.
        System.out.println("Shield toggled (template)");
    }

    // ============================ LOOP ============================ //
    /** Draw current state to the panel from the back buffer. */
    public void gameRender() {
        Graphics2D g2 = (Graphics2D) backBuffer.getGraphics();
        // Clear background
        g2.setColor(getBackground());
        g2.fillRect(0, 0, backBuffer.getWidth(), backBuffer.getHeight());

        // Draw player
        if (player != null) {
            player.draw(g2);
        }

        // Blit buffer to screen
        Graphics2D screen = (Graphics2D) getGraphics();
        if (screen != null) {
            screen.drawImage(backBuffer, 0, 0, screenwidth, screenheight, null);
            screen.dispose();
        }
        g2.dispose();
    }

    /** Start the game loop once. */
    public void startGame() {
        if (isStarted || isRunning) {
            System.out.println("Game already started. Reopen application to start again.");
            return;
        }
        isStarted = true;
        isRunning = true;
        // ~60 FPS timer driving update + render
        gameTimer = new Timer(16, e -> {
            // reference event so lambda parameter is used (keeps it simple)
            if (e.getSource() == null) return;
            if (!isRunning) return;
            updateGameEntities();
            gameRender();
        });
        gameTimer.start();
        createGameEntities();
        System.out.println("Number of threads: " + Thread.activeCount());
    }
}