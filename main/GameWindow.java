package main;
import java.awt.*;
import java.awt.event.*;                // GUI objects
import javax.swing.*;                   // AWT events
import managers.*;                      // Swing widgets

/**
 * GameWindow: a simple, readable template window.
 * - Top info bar shows health and points
 * - Center is the GamePanel
 * - Bottom has Start/Exit buttons

 * Keep this minimal so it's easy to reuse for new games.
 */
public class GameWindow extends JFrame implements ActionListener, KeyListener, MouseListener {
    private InputManager inputManager; // centralized input handler
    // Info bar
    private JLabel pointsL;
    private JLabel healthL;
    private static JTextField pointsTF;
    private static JTextField healthTF;
    public static int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    // Buttons
    private JButton startB;
    private JButton exitB;
    private JButton logoB;
    private JButton startDialogueB;
    private JButton transitionB;
    private JButton nextB;
    private JButton playMusicB;

    // Layout
    private Container c;
    private JPanel mainPanel;
    private GamePanel gamePanel;

    public GameWindow() {
        // Window basics
        setTitle("Java Game Template v1.1.5");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Maximize the frame to fill the screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);

        ImageIcon icon = new ImageIcon("src/images/icon.png");
        setIconImage(icon.getImage());


        // Info labels + fields
        pointsL = new JLabel();
        
        pointsTF = new JTextField();
        pointsTF.setPreferredSize(new Dimension(150, 16));
        pointsTF.setForeground(Color.white);
        pointsTF.setBorder(BorderFactory.createEmptyBorder());
        pointsTF.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        pointsTF.setEditable(false);
        pointsTF.setBackground(Color.black);

        healthL = new JLabel();
        
        healthTF = new JTextField();
        healthTF.setPreferredSize(new Dimension(150, 16));
        healthTF.setForeground(Color.white);
        healthTF.setBorder(BorderFactory.createEmptyBorder());
        healthTF.setFont(new Font(Font.DIALOG, Font.BOLD, 16));
        healthTF.setEditable(false);
        healthTF.setBackground(Color.black);

        // Buttons
        startB = new JButton("Start Game");
        exitB = new JButton("Exit");
        startDialogueB = new JButton("Start Dialogue");
        nextB = new JButton("Next");
        logoB = new JButton("Show Logo");
        transitionB = new JButton("Transition");
        playMusicB = new JButton("Play Music");

        startB.addActionListener(this);
        exitB.addActionListener(this);
        startDialogueB.addActionListener(this);
        nextB.addActionListener(this);
        logoB.addActionListener(this);
        transitionB.addActionListener(this);
        playMusicB.addActionListener(this);

        // Main panel layout
        mainPanel = new JPanel(new FlowLayout());

        // Game area
        gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(screenWidth, 700));

        // Info bar
        JPanel infoPanel = new JPanel(new GridLayout(1, 4));
        infoPanel.setBackground(Color.black);
        infoPanel.add(healthL);
        infoPanel.add(healthTF);
        infoPanel.add(pointsL);
        infoPanel.add(pointsTF);

        // Buttons row
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(startB);
        buttonPanel.add(startDialogueB);
        buttonPanel.add(nextB);
        buttonPanel.add(logoB);
        buttonPanel.add(transitionB);
        buttonPanel.add(playMusicB);
        buttonPanel.add(exitB);

        // Add to window
        mainPanel.add(infoPanel);
        mainPanel.add(gamePanel);
        mainPanel.add(buttonPanel);
        mainPanel.setBackground(Color.black);

        // Input listeners
        gamePanel.addMouseListener(this);
        mainPanel.addKeyListener(this);

        // Show
        c = getContentPane();
        c.add(mainPanel);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Initialize InputManager and link to GamePanel
        inputManager = InputManager.getInstance();
        inputManager.setGamePanel(gamePanel);
    }
    
    
    // implement single method in ActionListener interface
    
    @Override
    public void actionPerformed(ActionEvent e) {
    
        String command = e.getActionCommand();
        // Delegate button actions to the centralized InputManager
        inputManager.handleActionCommand(command);
        mainPanel.requestFocus();
    }
    
    
    // implement methods in KeyListener interface
    
    @Override
    public void keyPressed(KeyEvent e) {
        inputManager.keyPressed(e);
        mainPanel.requestFocus();
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        inputManager.keyReleased(e);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        inputManager.keyTyped(e);
    }
    
    // implement methods in MouseListener interface
    
    public static void updatePointChecker(int x){
        pointsTF.setText(Integer.toString(x));
    }
    
    // helper to update health text; callable from GamePanel/PlayerEntity
    public static void updatePlayerHealht (int x){
        if (x > 0) {
            healthTF.setText(Integer.toString(x));
        } else {
            healthTF.setText("Player Died.");
        }
    }

    // Backward-compatible alias used by some entities
    public static void updateHealthDisplay(int x) {
        updatePlayerHealht(x);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        inputManager.mouseClicked(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        inputManager.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        inputManager.mouseExited(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        inputManager.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        inputManager.mouseReleased(e);
    }
}