package GameComponents;
import GameComponents.Moves.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class Board extends JPanel implements Subscriber{
    private MoveStrategy strategy;
    boolean win = false;
    boolean continueAfterWin = false;
    JPanel centerPanel;
    JPanel topPanel;
    JTextArea scoreDisplay;
    Score score;
    List<Tile> tiles = new ArrayList<>();
    List<Subscriber> subscribers = new ArrayList<>();
    int rows;
    int cols;
    int points;
    private final Game game;

    public Board(int rows, int cols, Game game) {
        this.score = new Score(this);
        this.game = game;
        this.rows = rows;
        this.cols = cols;
        setLayout(new BorderLayout());

        centerPanel = new JPanel();
        topPanel = new JPanel(new FlowLayout());

        centerPanel.setLayout(new GridLayout(rows, cols));
        centerPanel.setFocusable(true);
        centerPanel.setEnabled(true);
        getStartingTiles();
        for (Tile tile : tiles) {
            centerPanel.add(tile);
        }

        centerPanel.setVisible(true);
        topPanel.setVisible(true);
        JLabel scoreLabel = new JLabel("Score: ");
        this.scoreDisplay = new JTextArea("");
        topPanel.add(scoreLabel);
        topPanel.add(scoreDisplay);
        scoreDisplay.setEditable(false);
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        setCoordinates();
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("keyTyped was reached");
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
        System.out.println("assessKeyAction was reached");
        MoveStrategy moveStrategy = getMoveStrategy(c);
        if (moveStrategy != null) {
            System.out.println(". . . . MOVESTRATEGY IS: " + moveStrategy);
            moveStrategy.move(getAllTileValues());
        }
    }

    public void updateBoard(MoveResult result) {
        List<List<Integer>> allAdjustedValues = result.getNewValues();
        List<List<Tile>>allSubsets = getAllTileSubsets();
        if (result.hasValuesChanged()) {
            for (int i = 0; i < allSubsets.size(); i++) {
                List<Integer> adjustedValues = allAdjustedValues.get(i);
                List<Tile> subset = allSubsets.get(i);
                for (int j = 0; j < adjustedValues.size(); j++) {
                    Tile.adjustTile(subset.get(j), adjustedValues.get(j));
                }
            }
            if ((result.hasReached2048() && !result.doContinueAfterWin()) || result.isFull()) {
                gameOverActions(result.hasReached2048());
            } else {
                addTile();
                if (checkIfFull()){
                    gameOverActions(result.hasReached2048());
                }
            }
        }
    }

    private void setCoordinates() {
        for (Tile t : tiles) {
            int index = tiles.indexOf(t);
            t.setRow(index / rows);
            t.setCol(index % cols);
        }
    }

    @Override
    public void update(EventType e, Object o) {
        notifySubscribers(e, o);
    }
    public void subscribe(Subscriber s){
        subscribers.add(s);

    }
    public void unSubscribe(Subscriber s){
        subscribers.remove(s);
    }

    private void notifySubscribers(EventType e, Object o){
        for(Subscriber s: subscribers){
            s.update(e, o);
        }
    }

    private void addTile() {
        Random random = new Random();
        List<Tile> emptyTiles = new ArrayList<>();
        for (Tile t : tiles) {
            if (t.getValue() == 0) {
                emptyTiles.add(t);
            }
        }
        Tile randomTile = emptyTiles.get(random.nextInt(emptyTiles.size()));
        Tile.adjustTile(randomTile, getStartingValue());
    }

    private void getStartingTiles() {
        for (int i = 0; i < (rows * cols); i++) {
            Tile emptyTile = new Tile(0, true, Color.lightGray, i / cols, i % cols);
            tiles.add(emptyTile);
        }
        Random random = new Random();
        Tile randomTileOne = tiles.get(random.nextInt(tiles.size()));
        Tile randomTileTwo = tiles.get(random.nextInt(tiles.size()));
        Tile.adjustTile(randomTileOne, getStartingValue());
        Tile.adjustTile(randomTileTwo, getStartingValue());
    }

    private int getStartingValue() {
        Random random = new Random();
        List<Integer> values = new ArrayList<>();
        values.add(2);
        values.add(4);
        int randomValue = random.nextInt(2);
        if (randomValue == 0) {
            return values.getFirst();
        } else {
            return values.getLast();
        }
    }
    private MoveStrategy getMoveStrategy(char c) {
        return switch (c) {
            case 'a' -> new MoveLeftStrategy(this);
            case 'd' -> new MoveRightStrategy(this);
            case 'w' -> new MoveUpStrategy(this);
            case 's' -> new MoveDownStrategy(this);
            default -> null;
        };
    }
    private void gameOverActions(boolean hasReached2048) {
        if (hasReached2048){
            win = true;
        }
        if (continueAfterWin) {
            win = false;
        }
        game.gameOverActions(win);
    }
    protected void displayScore(EventType e, int score){
        if(e == EventType.DISPLAY_SCORE){
            scoreDisplay.setText(String.valueOf(score));
        }
    }
    protected void updateContinueGame(EventType e){
        if (e == EventType.CONTINUE_GAME){
            continueAfterWin = true;
        }
    }
    private void setMoveStrategy(MoveStrategy strategy){
        this.strategy = strategy;
    }
    public List<Tile> getTiles(){
        return tiles;
    }
    public int getRows(){
        return rows;
    }
    public int getCols(){
        return cols;
    }

    private List<List<Tile>> getAllTileSubsets() {
        int count = rows;
        List<List<Tile>> allTileSubsets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<Tile> subset = getSubset(i);
            allTileSubsets.add(subset);
        }
        return allTileSubsets;
    }

    public List<Tile> getSubset(int index) { //Right
        int start = index * cols;
        return new ArrayList<>(tiles.subList(start, start + cols));
    }

    private List<List<Integer>> getAllTileValues( ) {
        List<List<Integer>> allSubsetValues = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<Tile> subset = getSubset(i);
            List<Integer> subsetValues = new ArrayList<>();
            for (Tile t : subset) {
                subsetValues.add(t.getValue());
            }
            allSubsetValues.add(subsetValues);
        }
        return allSubsetValues;
    }
    private boolean checkIfFull(){
        int emptyTile = 0;
        for(Tile t : tiles){
            if(t.getValue() == 0){
                emptyTile+=1;
            }
        }
        return emptyTile <= 0;
    }
}