package GUI.Game;
import GameComponents.GameSession;
import GameComponents.Moves.*;
import GameComponents.Score;
import Infrastructure.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class Board extends JPanel implements Subscriber {
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
    private final GameSession game;

    public Board(GameSession game) {
        System.out.println("BOARD constructor was reached");
//        this.totalScore = new Score(this);
        this.game = game;
        this.subscribers = new ArrayList<>();
        setLayout(new BorderLayout());
        game.subscribe(this);

        centerPanel = new JPanel();
        topPanel = new JPanel(new FlowLayout());

        centerPanel.setLayout(new GridLayout(rows, cols));
//        centerPanel.setFocusable(true);
//        centerPanel.setEnabled(true);

        centerPanel.setVisible(true);
        topPanel.setVisible(true);
        JLabel scoreLabel = new JLabel("Score: ");
        this.scoreDisplay = new JTextArea("");
        topPanel.add(scoreLabel);
        topPanel.add(scoreDisplay);
        scoreDisplay.setEditable(false);
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        tiles = new ArrayList<>();
        for (int i = 0; i < (rows * cols); i++) {
            Tile emptyTile = new Tile(0, true, Color.lightGray, i / cols, i % cols);
            tiles.add(emptyTile);
            centerPanel.add(emptyTile);
            System.out.println("tile added");
        }


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
        notifySubscribers(EventType.KEY_ACTION, c);
    }
    @Override
    public void update(EventType e, Object o) {
        System.out.println("---- in Board, update was reached. eventtype: " + e + " object: " + o.getClass());
        if (e == EventType.UPDATE_TILES) {
            if (o instanceof List list && list.getFirst() instanceof List sublist && sublist.getFirst() instanceof Integer) {
                System.out.println("in BOARD update, o is integer list");
                updateTileBoard(list);
            }
        }
    }
    protected void updateTileBoard(List<List<Integer>> values){
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
    protected void updateScoreDisplay(EventType e, Object o){
        if(e == EventType.DISPLAY_SCORE && o instanceof Integer i){
            System.out.println("D I S P L A Y S C O R E IS REACHED. values is: " + i);
            setScore(i);
            scoreDisplay.setText(String.valueOf(i));
        }
    }


    public void subscribe(Subscriber s){
        if(subscribers== null)
        {
            this.subscribers = new ArrayList<>();
        }
        subscribers.add(s);
    }
    public void unSubscribe(Subscriber s){
        subscribers.remove(s);
    }

    private void notifySubscribers(EventType e, Object o){
        System.out.println("in notifySubscribers in Board, eventtype is: " + e);
        for (Subscriber s : subscribers) {
            s.update(e, o);
        }
    }

    protected void updateContinueGame(EventType e){
        if (e == EventType.CONTINUE_GAME){
            continueAfterWin = true;
        }
    }

    public void setScore(int points){
        System.out.println("in setScore, score is: " + points);
        this.points = points;
    }
}