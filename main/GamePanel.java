package main;

import entities.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.Timer;
import managers.DialogueManager;
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

    private PlayerEntity player;
    private int screenwidth = GameWindow.screenWidth;
    private int screenheight = GameWindow.screenHeight;

    // Dialogue components
    private DialogueManager dialogueManager;
    private DialogueBoxEntity dialogueBox;

    // Sound manager 
    private SoundManager soundManager;
    private boolean dialogueActive = false;

    // Logo components
    private managers.LogoManager logoManager;
    private entities.LogoEntity logoEntity;

    private Timer gameTimer;
    private boolean isStarted;
    private boolean isRunning;

    private BufferedImage backBuffer; // offscreen buffer to draw into (created to match panel size)

    public GamePanel() {
        setBackground(Color.white);
        player = null;
        isStarted = false;
        isRunning = false;
        soundManager = SoundManager.getInstance();
        dialogueBox = new DialogueBoxEntity();
        dialogueManager = DialogueManager.getInstance();
        logoManager = managers.LogoManager.getInstance();
        logoEntity = new LogoEntity();
    // Back buffer will be created on first render to match the actual panel size
    backBuffer = null;
    }

    /** Create initial entities for a new game. */
    public void createGameEntities() {
        // Start player roughly at center
        player = new PlayerEntity(this, screenwidth/2 - PlayerEntity.diameter/2, screenheight/2 - PlayerEntity.diameter/2);
        GameWindow.updatePointChecker(0);
        GameWindow.updatePlayerHealht(player.health);
        
        System.out.println("Player created at (" + player.x + "," + player.y + ")");

        // Dialogue setup (reads from src/dialouge/test.txt)
    
    initDialogueBoxEntity();
    }

    public DialogueBoxEntity initDialogueBoxEntity(){
        dialogueManager.loadFromFile("src/dialouge/test.txt");
    
        String first = (dialogueManager.hasNext()) ? dialogueManager.nextLine() : "";
        dialogueBox.setText(first);
        dialogueActive = (first != null && !first.isEmpty());
        System.out.println("Dialogue loaded: " + first + " active=" + dialogueActive);
        return dialogueBox;
    }

    /** Play a sound effect by name. */
    public void playSound(String name, boolean loop) {
        soundManager.playClip(name, loop);
    }

    /** Update entities based on a direction code (1..8). */
    public void updateGameEntities(int direction) {
        if (!isRunning || player == null) 
            return;
        if (isPaused()) 
            return; // freeze player input while dialogue or logo is active
        player.move(direction);
    }

    /** Advance non-input game logic per tick (kept minimal). */
    public void updateGameEntities() {
        // Pause non-input updates while dialogue or logo is active
        if (isPaused()) return;
        // No-op in the template; extend with your own logic/timers.
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

        // If a logo is active it should occupy the entire screen; draw it first and skip other renders
        if (logoManager != null && logoManager.isActive()) {
            logoEntity.draw(g2, backBuffer.getWidth(), backBuffer.getHeight());

        } else {
            // Draw player
            if (player != null) {
                player.draw(g2);
            }

            // Draw dialogue box overlay (if any)
            if (dialogueBox != null) {
                dialogueBox.draw(g2, backBuffer.getWidth(), backBuffer.getHeight());
    
            }
        }

        // Blit buffer to screen (1:1 since buffer matches panel size)
        Graphics2D screen = (Graphics2D) getGraphics();
        if (screen != null) {
            screen.drawImage(backBuffer, 0, 0, null);
            screen.dispose();
        }
        g2.dispose();
    }

    public void triggerLogo() {
        boolean logoLoaded = initLogo("src/logo/logo.png");
        if (logoLoaded) {
            showLogo(3000);
        } else {
            System.out.println("Logo button: failed to load src/logo/logo.png");
        }
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

    // Advance to the next dialogue line (called by the window Next button)
    public void nextDialogue() {
        if (dialogueManager == null || dialogueBox == null) {
            System.err.println("DialogueManager or DialogueBoxEntity not initialized.");
            return;
        }
        String next = dialogueManager.nextLine();

        if (next == null) {
            // no more lines - remove box
            dialogueBox = null;
            dialogueActive = false;
            System.out.println("End of dialogue.");
        }
        else{
            dialogueBox.setText(next);
            dialogueActive = !next.isEmpty();
            System.out.println("Dialogue loaded: " + next);
        }
    }

    public boolean initLogo(String path){
        logoManager = managers.LogoManager.getInstance();
        logoEntity = new LogoEntity();

        if (path != null && !path.isEmpty()) {
            boolean loaded = logoManager.loadFromFile(path);
            if (!loaded) {
                System.err.println("Failed to load logo from path: " + path);
                return false;
            }
        }

        System.out.println("Logo initialized and shown");
        return (logoManager != null && logoEntity != null);
    }

    /** Load a logo image into the LogoManager. Returns true on success. */
    public boolean loadLogo(String path) {
        if (logoManager == null) return false;
        return logoManager.loadFromFile(path);
    }

    /** Show loaded logo for the specified duration (milliseconds). */
    public void showLogo(long durationMs) {
        if (logoManager == null) return;
        logoManager.show(durationMs);
    }

    /** Return true when either dialogue or logo is active and gameplay should be paused. */
    public boolean isPaused() {
        if (dialogueActive) 
            return true;

        if (logoManager != null && logoManager.isActive()) 
            return true;

        return false;
    }
}