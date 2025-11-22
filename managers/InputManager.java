package managers;

import java.awt.event.*;
import main.GamePanel;

public class InputManager implements KeyListener {

    private static InputManager instance;

    private GamePanel gamePanel;

    // Tracks key states
    private final boolean[] keysDown = new boolean[256];
    private final boolean[] keysPressedThisFrame = new boolean[256];

    // Output actions each frame
    private int movementDirection = 0;
    private boolean jumpPressed = false;

    public static InputManager getInstance() {
        if (instance == null) instance = new InputManager();
        return instance;
    }

    public void setGamePanel(GamePanel panel) {
        this.gamePanel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k < 0 || k >= 256) return;

        if (!keysDown[k]) {
            keysPressedThisFrame[k] = true; // one-shot detection
        }

        keysDown[k] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k < 0 || k >= 256) return;
        keysDown[k] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    // Main input logic run ONCE per frame from GameLoop
    public void processInputThisFrame() {
        if (gamePanel == null && !gamePanel.isPaused) return;

        // Check for dialogue advance
        
        if (gamePanel.sceneManager.isAnyActive()){
            if (wasPressed(KeyEvent.VK_SPACE)) {
                gamePanel.advanceDialogue();
                System.out.println("InputManager: SPACE pressed to advance dialogue.");
                clearPerFrameInputs();
                return;
            }

        }else{

            boolean up    = isDown(KeyEvent.VK_W) || isDown(KeyEvent.VK_UP);
            boolean down  = isDown(KeyEvent.VK_S) || isDown(KeyEvent.VK_DOWN);
            boolean left  = isDown(KeyEvent.VK_A) || isDown(KeyEvent.VK_LEFT);
            boolean right = isDown(KeyEvent.VK_D) || isDown(KeyEvent.VK_RIGHT);

            movementDirection = 0;

            // 8-direction movement encoding
            if (left && up) movementDirection = 6;
            else if (right && up) movementDirection = 5;
            else if (left && down) movementDirection = 8;
            else if (right && down) movementDirection = 7;
            else if (left) movementDirection = 1;
            else if (right) movementDirection = 2;
            else if (up) movementDirection = 3;
            else if (down) movementDirection = 4;

            // One-shot action example (jump)
            jumpPressed = wasPressed(KeyEvent.VK_SPACE);

            // Send actions to game panel
            gamePanel.receiveInput(movementDirection, jumpPressed);

            // Reset one-shot keys
            clearPerFrameInputs();
        }
    }

    private boolean isDown(int key) {
        return key >= 0 && key < 256 && keysDown[key];
    }

    private boolean wasPressed(int key) {
        return key >= 0 && key < 256 && keysPressedThisFrame[key];
    }

    private void clearPerFrameInputs() {
        for (int i = 0; i < keysPressedThisFrame.length; i++) {
            keysPressedThisFrame[i] = false;
        }
    }

    public void handleActionCommand(String command) {
        if (command == null || gamePanel == null) return;

        switch (command) {
            case "Start Game":
                gamePanel.startGame();
                break;
            case "Start Dialogue":
                gamePanel.startDialogueByName("test");
                break;
            case "Next":
                gamePanel.advanceDialogue();
                break;
            case "Show Logo":
                gamePanel.triggerLogo();
                break;
            case "Transition":
                gamePanel.triggerTransition(2000);
                break;
            case "Play Music":
                gamePanel.playSound("ping", false);
                break;
            case "Exit":
                System.exit(0);
                break;
            default:
                // unknown command — ignore
                break;
        }
    }



    // Mouse events forwarded from GameWindow
    
    public void mouseClicked(java.awt.event.MouseEvent e) {}
    public void mousePressed(java.awt.event.MouseEvent e) { }
    public void mouseReleased(java.awt.event.MouseEvent e) { }
    public void mouseEntered(java.awt.event.MouseEvent e) { }
    public void mouseExited(java.awt.event.MouseEvent e) { }
}
