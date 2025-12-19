package GUI.Game;
import GameComponents.Moves.*;
import GameComponents.Score;
import Infrastructure.AppManager;
import Infrastructure.GameManager;
import Infrastructure.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class Board extends JPanel {
    private MoveStrategy strategy;
    boolean win = false;
    boolean continueAfterWin = false;
    JPanel centerPanel;
    JPanel topPanel;
    JTextArea scoreDisplay;
    private Score totalScore;
    private int points;
    List<Tile> tiles;
    List<Subscriber> subscribers;
    int rows = 4;
    int cols = 4;
    AppManager manager;

    public Board(List<List<Integer>> values, AppManager manager) {
        System.out.println("BOARD constructor was reached");
//        this.totalScore = new Score(this);
        this.manager = manager;
        setLayout(new BorderLayout());

        centerPanel = new JPanel();
        topPanel = new JPanel(new FlowLayout());

        centerPanel.setLayout(new GridLayout(rows, cols));
//        centerPanel.setFocusable(true);
//        centerPanel.setEnabled(true);

        centerPanel.setVisible(true);
        topPanel.setVisible(true);
        JLabel scoreLabel = new JLabel("Score: ");
        this.scoreDisplay = new JTextArea(String.valueOf(0));
        topPanel.add(scoreLabel);
        topPanel.add(scoreDisplay);
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
                actionCharacters.add('a');
                actionCharacters.add('s');
                actionCharacters.add('d');
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
        System.out.println("assessKeyAction in Board was reached");
        manager.sendToMediator(Subscriber.EventType.REQUEST_KEY_ACTION, c);
    }

    public void updateTileBoard(List<List<Integer>> values){

        System.out.println("       UPDATE TILEBOARD IN BOARD WAS REACHED");
        List<Integer> allValues = new ArrayList<>();
        for (List<Integer> l : values){
            allValues.addAll(l);
        }
        for (int i = 0; i < allValues.size(); i++) {
            Tile.adjustTile(tiles.get(i), allValues.get(i));
        }
        repaint();
        revalidate();
    }
    public void updateScoreDisplay(int value){
        setScore(value);
        System.out.println("D I S P L A Y S C O R E IS REACHED. points is: " + points);
        scoreDisplay.setText(String.valueOf(points));
    }

    protected void updateContinueGame(){
        continueAfterWin = true;
    }

    public void setScore(int value){
        System.out.println("in setScore, score is: " + points);
        this.points = points + value;
    }
}