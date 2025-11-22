package main;

import entities.PlayerEntity;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;
import managers.InputManager;
import managers.SceneManager;
import managers.SoundManager;

/**
 * GamePanel: minimal, self-contained game surface for the template.
 * - Owns a PlayerEntity and renders it to an offscreen buffer.
 * - Provides a simple game loop thread with start/stop flags.
 * - Exposes simple actions: updateGameEntities(direction), swung(), shield().
 *
 * Keep it simple so you can copy this into new projects and customize.
 */
public class GamePanel extends JPanel {

    public PlayerEntity player;
    private final int screenwidth = GameWindow.screenWidth;
    private final int screenheight = GameWindow.screenHeight;
    private Timer gameTimer;
    public boolean isStarted;
    public boolean isRunning;
    public boolean isPaused = false;

    // Managers
    public SceneManager sceneManager;
    public SoundManager soundManager;

    private BufferedImage backBuffer; // offscreen buffer to draw into (created to match panel size)

    public GamePanel() {
        setBackground(Color.white);
        player = null;
        isStarted = false;
        isRunning = false;
        soundManager = SoundManager.getInstance();
        sceneManager = SceneManager.getInstance();
        // Back buffer will be created on first render to match the actual panel size
        backBuffer = null;
       
    }

    /** Create initial entities for a new game. */
    public void createGameEntities() {
        // Start player roughly at center
        player = new PlayerEntity(this, screenwidth/2 - PlayerEntity.diameter/2, screenheight/2 - PlayerEntity.diameter/2);
        GameWindow.updatePointChecker(0);
        GameWindow.updatePlayerHealht(player.health);
        System.out.println("Player created at (x=" + player.x + " y=," + player.y + ")");

        InputManager.getInstance().setGamePanel(this);

        // Initialize other game entities here
    }

    public void startDialogueByName(String name){
        sceneManager.showDialogueFromFile("src/dialouge/" + name + ".txt");

    }

    /** Play a sound effect by name. */
    public void playSound(String name, boolean loop) {
        soundManager.playClip(name, loop);
    }

    /** Advance non-input game logic per tick (kept minimal). */
    public void updateGameEntities() {
    
        if (isPaused || sceneManager.isAnyActive())
            return;
    }

    public void receiveInput(int actionCode) {
        if (isPaused || player == null) return;

        if (actionCode != 0) {
            player.action(actionCode);
        }

    }

    // ============================ LOOP ============================ //
    /** Draw current state to the panel from the back buffer. */
    public void gameRender() {
        // Ensure backBuffer matches panel size (panel may not be initialized in constructor)
        int panelW = Math.max(1, getWidth());
        int panelH = Math.max(1, getHeight());
        if (backBuffer == null || backBuffer.getWidth() != panelW || backBuffer.getHeight() != panelH) {
            backBuffer = new BufferedImage(panelW, panelH, BufferedImage.TYPE_INT_RGB);
        }

        Graphics2D g2 = backBuffer.createGraphics();
        // Clear background
        g2.setColor(getBackground());
        g2.fillRect(0, 0, backBuffer.getWidth(), backBuffer.getHeight());

        // Ask SceneManager to draw any active overlays. If a logo is active it will
        // occupy the full screen; otherwise we draw game content first and let the
        // SceneManager draw overlays (dialogue/transition) on top.
        if (sceneManager.isLogoActive()) {
            sceneManager.draw(g2, backBuffer.getWidth(), backBuffer.getHeight());
        } else {
            // Draw player and other game content
            if (player != null) {
                player.draw(g2);
            }
            // Draw overlays (dialogue, transition) on top of game content
            sceneManager.draw(g2, backBuffer.getWidth(), backBuffer.getHeight());
        }

        // Blit buffer to screen (1:1 since buffer matches panel size)
        Graphics2D screen = (Graphics2D) getGraphics();
        if (screen != null) {
            screen.drawImage(backBuffer, 0, 0, null);
            screen.dispose();
        }
        g2.dispose();
    }

    
    public void advanceDialogue() {
        sceneManager.advanceDialogue();
    }

    // -------- Convenience wrappers for SceneManager (used by GameWindow UI) --------
    public boolean loadLogo(String path) {
        return sceneManager.loadLogo(path);
    }

    public void showLogo(long durationMs) {
        sceneManager.showLogo(durationMs);
    }

    public void triggerTransition(long durationMs) {
        sceneManager.showTransition(durationMs);
    }

    public boolean showDialogueFromFile(String path) {
        return sceneManager.showDialogueFromFile(path);
    }

    /** Backward-compatible helper used by GameWindow to load & show the default logo. */
    public void triggerLogo() {
        // default logo path used by the template
        String defaultLogo = "src/logo/logo.png";
        boolean ok = loadLogo(defaultLogo);
        if (ok) showLogo(3000);
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
            InputManager.getInstance().processInputThisFrame();
            gameRender();
        });
        gameTimer.start();
        createGameEntities();
        System.out.println("Number of threads: " + Thread.activeCount());
    }

    public void pauseGame() {  //Toggle pause state
        if (isRunning){
            isPaused = !isPaused;
        }
    }

    public void pauseGameplay(boolean pause){  //Pause gameplay without toggling for events like dialogue or logos 
        if (isRunning){
            isPaused = pause;
        }
    }

}