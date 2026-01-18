package GUI.Game;
import GUI.MainFrame;
import GameComponents.Moves.*;
import GameComponents.Score;
import Infrastructure.AppManager;
import Infrastructure.GameManager;
import Infrastructure.Mediator;
import Infrastructure.Subscriber;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class Board extends JPanel {
    private MainFrame mainFrame;
    private MoveStrategy strategy;
    private boolean win = false;
    private boolean continueAfterWin = false;
    private JPanel centerPanel;
    private JPanel topPanel;
    private JTextArea scoreDisplay;
    private JTextArea highscoreDisplay;
    private int points;
    private List<Tile> tiles;
    private int rows = 4;
    private int cols = 4;
    private Mediator mediator;
    private Color backgroundColor = Color.darkGray;
    private List<Integer>allValues;

    public Board(List<Integer> values, int highscore, Mediator mediator, MainFrame mainFrame) {
        this.mediator = mediator;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        centerPanel = new JPanel();
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        centerPanel.setLayout(new GridLayout(rows, cols));
        centerPanel.setBackground(backgroundColor);
        centerPanel.setVisible(true);
        centerPanel.setBorder(
                BorderFactory.createLineBorder(Color.lightGray, 7, false));
        topPanel.setVisible(true);
        JLabel scoreLabel = new JLabel("Score: ");
        scoreLabel.setBackground(backgroundColor);
        scoreLabel.setForeground(GUI.Game.GameColors.headerText());
        scoreLabel.setFont(GUI.Game.GameFont.topHeaderFont());
        this.scoreDisplay = new JTextArea(String.valueOf(0));
        scoreDisplay.setBackground(backgroundColor);
        scoreDisplay.setForeground(GUI.Game.GameColors.headerText());
        scoreDisplay.setFont(GUI.Game.GameFont.topHeaderFont());
        JLabel highscoreLabel = new JLabel("Highscore: ");
        highscoreLabel.setForeground(GUI.Game.GameColors.headerText());
        highscoreLabel.setFont(GUI.Game.GameFont.topHeaderFont());
        this.highscoreDisplay = new JTextArea(String.valueOf(highscore));
        highscoreDisplay.setForeground(GUI.Game.GameColors.headerText());
        highscoreDisplay.setFont(GUI.Game.GameFont.topHeaderFont());
        highscoreDisplay.setBackground(backgroundColor);
        topPanel.add(scoreLabel);
        topPanel.setBackground(backgroundColor);
        topPanel.add(scoreDisplay);
        topPanel.add(highscoreLabel);
        topPanel.add(highscoreDisplay);
        scoreDisplay.setEditable(false);
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        this.tiles = new ArrayList<>();
        for (int i = 0; i < (rows * cols); i++) {
            Tile emptyTile = new Tile(0, true, Color.lightGray, i / cols, i % cols);
            tiles.add(emptyTile);
            centerPanel.add(emptyTile);
            System.out.println("tile added");
        }
        updateTileBoard(values);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("keyTyped was reached");            }
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("keyPressed was reached");
                List<Character> actionCharacters = new ArrayList<>();
                actionCharacters.add('w');
                actionCharacters.add('W');
                actionCharacters.add('a');
                actionCharacters.add('A');
                actionCharacters.add('s');
                actionCharacters.add('S');
                actionCharacters.add('d');
                actionCharacters.add('D');
                for (char c : actionCharacters) {
                    if (e.getKeyChar() == c) {
                        assessKeyAction(c);
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("keyReleased was reached");
            }
        });
    }

    protected void assessKeyAction(char c) {
        mainFrame.update(Subscriber.EventType.REQUEST_KEY_ACTION, c);
    }

    public void updateTileBoard(List<Integer> allValues){
        for (int i = 0; i < allValues.size(); i++) {
            Tile.adjustTile(tiles.get(i), allValues.get(i));
        }
        repaint();
        revalidate();
    }
    public void updateScoreDisplay(int value){
        setScore(value);
        scoreDisplay.setText(String.valueOf(value));
    }
    public void updateHighscoreDisplay(int value){
        highscoreDisplay.setText(String.valueOf(value));
    }
    protected void updateContinueGame(){
        continueAfterWin = true;
    }
    public void setScore(int value){
        this.points = points + value;
    }
    public List<Integer>getAllValues(){
        return allValues;
    }
}