package GameComponents;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class Board extends JPanel implements Subscriber{
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
    boolean full = false;
    private final Game game;
    public enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }

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
        Direction direction = getDirection(c);
        if (direction != null) {
            assessMovement(direction);
        }
    }

    private void setCoordinates() {
        for (Tile t : tiles) {
            int index = tiles.indexOf(t);
            t.setRow(index / rows);
            t.setCol(index % cols);
            System.out.println("IN SET COORDINATES, ROW/COL ARE: " + tiles.indexOf(t) + " : " + t.getRow() + " " + t.getCol());
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
    private boolean completed() {
        System.out.println("in Board, completed is checked");
        int numberOfValueTiles = 0;
        for (Tile t : tiles) {
            if (t.getValue() > 0) {
                numberOfValueTiles += 1;
            }
        }
        System.out.println("in Board completed: numberOfValueTiles is: " + numberOfValueTiles);
        return numberOfValueTiles == tiles.size();
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

    private void assessMovement(Direction direction) {
        List<List<Tile>> allTileSubsets = getAllTileSubsets(direction);
        List<List<Integer>> allSubsetValues = getTileValues(direction);
        List<List<Integer>> allAdjustedValues = new ArrayList<>();
        boolean reversed = direction == Direction.RIGHT || direction == Direction.DOWN;
        for (List<Integer> l : allSubsetValues) {
            List<Integer> line = new ArrayList<>(l);
            if (reversed) {
                Collections.reverse(line);
            }
            List<Integer> adjustedValues = getAdjustedValues(line);
            List<Integer>mergedValues = new ArrayList<>(adjustedValues);
            if (reversed) {
                Collections.reverse(mergedValues);
            }
            allAdjustedValues.add(mergedValues);
        }

        if(hasValuesChanged(allSubsetValues, allAdjustedValues)) {
            for (int i = 0; i < allAdjustedValues.size(); i++) {
                List<Tile> subset = allTileSubsets.get(i);
                List<Integer> adjustedValues = allAdjustedValues.get(i);

                for (int j = 0; j < adjustedValues.size(); j++) {
                    Tile.adjustTile(subset.get(j), adjustedValues.get(j));
                    if (adjustedValues.get(j) == 2048 && continueAfterWin == false){
                        gameOverActions();
                    }
                }
            }

        }
        if(!completed()){
            addTile();
        }
        else {
            System.out.println("in assessMovement, gameOverActions is called");
            gameOverActions();
        }
    }
    private boolean hasValuesChanged(List<List<Integer>> allSubsetValues, List<List<Integer>>  allAdjustedValues){
        List<Integer> initialValues = new ArrayList<>();
        int changedValues = 0;
        for (List<Integer> l : allSubsetValues) {
            initialValues.addAll(l);
        }
        List<Integer>newValues = new ArrayList<>();
        for (List<Integer> l : allAdjustedValues){
            newValues.addAll(l);
        }
        for (int i = 0; i < initialValues.size(); i++){
            if (!Objects.equals(initialValues.get(i), newValues.get(i))){
                changedValues+=1;
            }
        }
        return changedValues > 0;
    }

    private List<List<Integer>> getTileValues(Direction direction) {
        List<List<Tile>> allTileSubsets = getAllTileSubsets(direction);
        List<List<Integer>> allSubsetValues = new ArrayList<>();
        for (List<Tile> subset : allTileSubsets) {
            List<Integer> subsetValues = new ArrayList<>();
            for (Tile t : subset) {
                subsetValues.add(t.getValue());
            }
            allSubsetValues.add(subsetValues);
        }
        return allSubsetValues;
    }

    private List<Integer> getAdjustedValues(List<Integer> subset) {
        List<Integer> adjustedValues = new ArrayList<>();
        List<Integer> nonZeroValues = new ArrayList<>();
        for (int i = 0; i < subset.size(); i++) {
            if (subset.get(i) != 0) {
                nonZeroValues.add(subset.get(i));
            }
        }
        if (nonZeroValues.size() < 2) {
            adjustedValues.addAll(nonZeroValues);
        } else {
            for (int i = 0; i < nonZeroValues.size(); i++) {
                if (i < nonZeroValues.size() -1 && nonZeroValues.get(i).equals(nonZeroValues.get(i + 1))) {
                    adjustedValues.add((nonZeroValues.get(i)) * 2);
                    update(EventType.NEW_SCORE, (nonZeroValues.get(i) * 2));
                    i++;
                } else {
                    adjustedValues.add(nonZeroValues.get(i));
                }
            }
        }
        while (adjustedValues.size() < subset.size()) {
            adjustedValues.add(0);
        }
        return adjustedValues;
    }

    public List<Tile> getSubset(int index, Direction direction) {
        System.out.println("getSubset was reached, direction is: " + direction + "  & index is: " + index);
        List<Tile> subset = new ArrayList<>();
        switch (direction) {
            case DOWN, UP -> {
                for (int i = 0; i < rows; i++) {
                    subset.add(tiles.get(i * cols + index));
                }
                return subset;
            }
            case LEFT, RIGHT -> {
                int start = index * cols;
                return new ArrayList<>(tiles.subList(start, start + cols));
            }
            default -> {
                return null;
            }
        }
    }

    private Direction getDirection(char c) {
        Direction d = null;
        switch (c) {
            case 'a': {
                d = Direction.LEFT;
                break;
            }
            case 'd': {
                d = Direction.RIGHT;
                break;
            }
            case 'w': {
                d = Direction.UP;
                break;
            }
            case 's': {
                d = Direction.DOWN;
                break;
            }
            default: {
                return null;
            }
        }
        return d;
    }

    private List<List<Tile>> getAllTileSubsets(Direction direction) {
        int count = rows;
        if (direction == Direction.LEFT || direction == Direction.RIGHT){
            count = cols;
        }
        System.out.println("getAllTileSubsets was reached");
        List<List<Tile>> tileSubsets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<Tile> subset = getSubset(i, direction);
            tileSubsets.add(subset);
        }
        return tileSubsets;
    }
    private void gameOverActions() {
        System.out.println("gameOverActions in Board was reached");
        if (win == false){
            for (Tile t : tiles) {
                if (t.getValue() == 2048) {
                    win = true;
                }
            }
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
}