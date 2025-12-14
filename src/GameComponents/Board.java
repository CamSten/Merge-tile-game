package GameComponents;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

public class Board extends JPanel {
    List<Tile> tiles = new ArrayList<>();
    int rows;
    int cols;
    int points;
    boolean full = false;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT;
    }

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        setLayout(new GridLayout(rows, cols));
        setFocusable(true);
        getStartingTiles();
        for (Tile tile : tiles) {
            add(tile);
        }
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

    private boolean completed() {
        int numberOfValueTiles = 0;
        for (Tile t : tiles) {
            if (t.getValue() == 0) {
                numberOfValueTiles += 1;
            }
        }
        return numberOfValueTiles == tiles.size();
    }

    private void addTile() {
        Random random = new Random();
        List<Tile> emptyTiles = new ArrayList<>();
        if (!completed()) {
            for (Tile t : tiles) {
                if (t.getValue() == 0) {
                    emptyTiles.add(t);
                }
            }
            Tile randomTile = emptyTiles.get(random.nextInt(emptyTiles.size()));
            Tile.adjustTile(randomTile, getStartingValue());
        }
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
                }
            }
            addTile();
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
}