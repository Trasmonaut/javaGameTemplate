package main;

import java.awt.*;              // GUI objects
import java.awt.event.*;        // AWT events
import javax.swing.*;           // Swing widgets

/**
 * GameWindow: a simple, readable template window.
 * - Top info bar shows health and points
 * - Center is the GamePanel
 * - Bottom has Start/Exit buttons
 *
 * Keep this minimal so it's easy to reuse for new games.
 */
public class GameWindow extends JFrame implements ActionListener, KeyListener, MouseListener {

    // Icons for info labels
   

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
    private JButton nextB;
    private JButton playMusicB;

    // Layout
    private Container c;
    private JPanel mainPanel;
    private GamePanel gamePanel;

    public GameWindow() {
        // Window basics
       

        setTitle("Java Game Template v1.1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Maximize the frame to fill the screen
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    
        setVisible(true);


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
        nextB = new JButton("Next");
        logoB = new JButton("Show Logo");
        playMusicB = new JButton("Play Music");

        startB.addActionListener(this);
        exitB.addActionListener(this);
        nextB.addActionListener(this);
        logoB.addActionListener(this);
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
        buttonPanel.add(nextB);
        buttonPanel.add(logoB);
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
    }
    
    
    // implement single method in ActionListener interface
    
    @Override
    public void actionPerformed(ActionEvent e) {
    
        String command = e.getActionCommand();
        
        //statusBarTF.setText(command + " button clicked.");
        
        if (command.equals(startB.getText())) {
            gamePanel.startGame();
            mainPanel.requestFocus();
        }

        if (command.equals(nextB.getText())) {
            gamePanel.nextDialogue();
            mainPanel.requestFocus();
        }

        if (command.equals(logoB.getText())) {
            // Load and show the default logo for 3 seconds
            boolean ok = gamePanel.loadLogo("src/logo/logo.png");
            if (ok) {
                gamePanel.showLogo(3000);
            } else {
                System.out.println("Logo button: failed to load src/logo/logo.png");
            }
            mainPanel.requestFocus();
        }

        if (command.equals(exitB.getText())) {
            System.exit(0);
        }
        if (command.equals(playMusicB.getText())) {
            gamePanel.playSound("ping", false);
        }
    
        mainPanel.requestFocus();
    }
    
    
    // implement methods in KeyListener interface
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D ) {
            gamePanel.updateGameEntities(2);
            //gamePanel.drawGameEntities();
        }
    
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A ) {
            gamePanel.updateGameEntities(1);
            //gamePanel.drawGameEntities();
        }

        
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
    
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    
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
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    
    }

    @Override
    public void mouseExited(MouseEvent e) {
    
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}