package managers;

import entities.PlayerEntity;
import java.awt.event.*;
import main.GamePanel;


/**
 * InputManager: handles user input and updates game entities accordingly.
 *  This class follows the Singleton pattern to ensure only one instance exists.
 */
public class InputManager {
    private static InputManager instance = null;	// keeps track of Singleton instance
    private GamePanel gamePanel;
    private PlayerEntity player;

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    public void initPlayerEntity(PlayerEntity player) {
        if (this.gamePanel == null) return;
        this.player = gamePanel.player;
    }

    /** Create an empty manager; call loadFromFile before use. */
    public InputManager() { }

    public static InputManager getInstance() {	// class method to retrieve instance of Singleton
		if (instance == null)
			instance = new InputManager();
            

		return instance;
	}    

    // Handle raw KeyEvent from the GameWindow
    public void keyPressed(KeyEvent e) {

        if (gamePanel == null || e == null) return;
        int keyCode = e.getKeyCode();


        if (!gamePanel.isPaused && gamePanel.sceneManager.isAnyActive())
            if (keyCode == KeyEvent.VK_SPACE) {
                gamePanel.advanceDialogue();
                System.out.println("InputManager: SPACE pressed to advance dialogue.");
                return;
            }
        

        else if (!gamePanel.isRunning || player == null || gamePanel.isPaused || gamePanel.sceneManager.isAnyActive())
            return; // freeze player input while dialogue/logo/transition active
        // Update player entity position based on keyCode
        switch (keyCode) {
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                player.move(2);
                break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                player.move(1);
                break;

        // reserved for future use
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                break;
        // reserved for future use
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                break;
            default:
                break;
        }

        
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    // Handle action commands (buttons) from GameWindow
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
