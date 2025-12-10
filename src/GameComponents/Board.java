package GameComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board extends JPanel {
    boolean keepRunning = true;
    List<Tile> tiles = new ArrayList<>();
    int rows;
    int cols;
    int points;
    boolean full = false;

    public enum Direction {
        UP(-1), DOWN(+1), LEFT(-1), RIGHT(+1);
        private final int moveFactor;

        Direction(int moveFactor) {
            this.moveFactor = moveFactor;
        }

        public int getMoveFactor() {
            return moveFactor;
        }
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

    private void setCoordinates() {
        List<Tile[]> allRows = getAllTileSubsets(Direction.LEFT);
        for (Tile[] tiles : allRows) {
            for (int i = 0; i < tiles.length; i++) {
                tiles[i].setCol(i);
            }
        }
    }

    private void getStartingTiles() {
        for (int i = 0; i < (rows * cols); i++) {
            Tile emptyTile = new Tile(0, true, Color.lightGray, i / cols, i % cols);
            tiles.add(emptyTile);
        }
        Random random = new Random();
        Tile randomTileOne = tiles.get(random.nextInt(tiles.size()));
        Tile randomTileTwo = tiles.getLast();
        adjustTile(randomTileOne, getStartingValue());
        adjustTile(randomTileTwo, getStartingValue());
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
            return values.get(1);
        }
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<Tile> getSubset(int row, int col, Direction direction) {
        System.out.println("getSubset was reached");
        List<Tile> subset = new ArrayList<>();
        switch (direction) {
            case DOWN, UP -> {
                int index = col;
                for (int i = 0; i < rows; i++) {
                    subset.add(tiles.get(i * cols + index));
                }
                System.out.println("subset is returned in getSubset");
                return subset;
            }
            case LEFT, RIGHT -> {
                int index = row;
                int start = index * cols;
                System.out.println("subset is returned in getSubset");
                return new ArrayList<>(tiles.subList(start, start + cols));
            }
            default -> {
                return null;
            }
        }
    }

    public List<Tile> getRow(int row) {
        int start = row * cols;
        return tiles.subList(start, start + rows);
    }

    public Tile getAdjacent(Tile tile, Direction direction) {
        System.out.println("getAdjacent is reached, direction is: " + direction);
        if (tile != null) {
            switch (direction) {
                case UP, DOWN: {
                    System.out.println("in getAdjacent, case UP/DOWN is reached");
                    List<Tile> subset = getSubset(tile.getRow(), tile.getCol(), direction);
                    subset.remove(tile);
                    int index = tile.getRow() + direction.getMoveFactor();
                    for (Tile t : subset) {
                        System.out.println("in getAdjacent, coordinates row/col for tile and t are: " + tile.getRow() + " " + tile.getCol() + " |" + t.getRow() + " " + t.getCol());
                        System.out.println("THUS; index is: " + index + " and t.getRow() is: " + t.getRow());
                        if (t.getRow() == index ) {
                            System.out.println("in getAdjacent, t was returned");
                            return t;
                        }
                    }
                    break;
                }
                case LEFT, RIGHT: {
                    List<Tile> subset = getSubset(tile.getRow(), tile.getCol(), direction);
                    subset.remove(tile);
                    int index = tile.getCol() + direction.getMoveFactor();
                    System.out.println("in getAdjacent, case LEFT/RIGHT is reached");
                    for (Tile t : getSubset(tile.getRow(), tile.getCol(), direction)) {
                        System.out.println("in getAdjacent, t.getCol is: " + t.getCol());
                        if (t.getCol() == index) {
                            return t;
                        }
                    }
                    break;
                }
            }
        }
        return null;
    }

    protected void assessKeyAction(char c) {
        Direction direction = getDirection(c);
        List<Tile[]> tileSubsets = new ArrayList<>();
        List<Tile> valueTiles = new ArrayList<>();
        if (direction != null) {
            tileSubsets = getAllTileSubsets(direction);

            for (Tile t : tiles) {
                if (t.getValue() > 0) {
                    valueTiles.add(t);
                    System.out.println("value tile in assessKeyAction: " + t.getValue());
                }
            }
            for (Tile t : valueTiles) {
                if (t != null && t.getValue() > 0) {
                    System.out.println("in assessKeyAction, moveTile is called for tile with index" + valueTiles.indexOf(t));
                    calculateMoves(t, direction);
                }
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

    private List<Tile[]> getAllTileSubsets(Direction direction) {
        List<Tile[]> tileSubsets = new ArrayList<>();
        switch (direction) {
            case LEFT, RIGHT: {
                for (int i = 0; i < rows; i++) {
                    Tile[] rowArray = new Tile[cols];
                    List<Tile> arrayPart = getRow(i);
                    for (int j = 0; j < cols; j++) {
                        rowArray[j] = arrayPart.get(j);
                    }
                    tileSubsets.add(rowArray);
                }
                break;
            }

            case UP, DOWN: {
                for (int i = 0; i < cols; i++) {
                    Tile[] colArray = new Tile[rows];
                    List<Tile> arrayPart = getSubset(i, i, direction);
                    for (int j = 0; j < rows; j++) {
                        colArray[j] = arrayPart.get(j);
                    }
                    tileSubsets.add(colArray);
                }
                break;
            }
        }
        return tileSubsets;
    }

    protected boolean checkIfAdjacentIsEmpty(Tile tile, Direction direction) {
        return getAdjacent(tile, direction) != null && getAdjacent(tile, direction).getValue() == 0;
    }

    private boolean checkIfMergeable(Tile tile, Direction direction) {
        List<Tile> subset = getSubset(tile.getRow(), tile.getCol(), direction);
        List<Tile> valueTiles = findValueTiles(subset);
        int value = tile.getValue();
        if (getAdjacent(tile, direction) != null && value > 0 && getAdjacent(tile, direction).getValue() == value) {
            return true;
        } else if (valueTiles.size() == 2 && !isFinalPosition(tile, direction)) {
            Tile tileOne = valueTiles.getFirst();
            Tile tileTwo = valueTiles.getLast();
            return tileOne.getValue() == tileTwo.getValue();
        } else if (valueTiles.size() == 3) {
            valueTiles.remove(tile);
            for (Tile t : valueTiles) {
                if (t.getValue() == tile.getValue() && checkIfAdjacentIsEmpty(tile, direction)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFinalPosition(Tile tile, Direction direction) {
        switch (direction) {
            case LEFT -> {
                if (tile.getCol() == 0) {
                    return true;
                }
            }
            case RIGHT -> {
                if (tile.getCol() == cols) {
                    return true;
                }
            }
            case DOWN -> {
                if (tile.getRow() == rows) {
                    return true;
                }
            }
            case UP -> {
                if (tile.getRow() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void calculateMoves(Tile tile, Direction direction) {
        System.out.println("calculateMoves was reached. Direction is: " + direction);
        boolean mergeable = false;
        int moveDistance = 0;
        int start = 0;
        int stop = 0;
        int change = 0;

        Tile adjacent = getAdjacent(tile, direction);
        if (adjacent != null) {
            System.out.println("- - - in calculateMoves, adjacent isn't null");
            if (checkIfMergeable(tile, direction)) {
                System.out.println("in calculateMoves, mergeable is TRUE");
                mergeable = true;
                moveDistance += 1;
            }
            switch (direction) {
                case LEFT, UP-> {
                    List<Tile> subset = getSubset(tile.getRow(), tile.getCol(), direction);
                    if (direction == Direction.LEFT){
                        start = tile.getCol();
                        change = Direction.LEFT.getMoveFactor();
                    }
                    else {
                        start = tile.getRow();
                        change = Direction.UP.getMoveFactor();
                    }
                    moveDistance = getMoveDistance(direction, moveDistance, start, stop, change, subset);
                    break;
                }

                case DOWN, RIGHT -> {
                    List<Tile> subset = getSubset(tile.getRow(), tile.getCol(), direction).reversed();
                    if (direction == Direction.RIGHT) {
                        start = tile.getCol();
                        stop = cols;
                        change = Direction.RIGHT.getMoveFactor();
                    }
                    else {
                        start = tile.getRow();
                        stop = rows;
                        change = Direction.DOWN.getMoveFactor();
                    }
                    moveDistance = getMoveDistance(direction, moveDistance, start, stop, change, subset);
                }
            }
            System.out.println("   in calculateMoves, start is: " + start + ", stop is: " + stop + " and change is: " + change);

            if (moveDistance > 0) {
                moveTiles(tile, direction, mergeable);
            }
        }
    }

    private int getMoveDistance(Direction direction, int moveDistance, int start, int stop, int change, List<Tile> subset) {
        Tile adjacent = null;
        switch (direction) {
            case UP, LEFT -> {
                for (int i = start; i > stop; i += change) {
                    adjacent = getAdjacent(subset.get(i), direction);
                    if (adjacent != null && adjacent.getValue() == 0) {
                        moveDistance += 1;
                        System.out.println("in calculateMoves, moveDistance is: " + moveDistance);
                    }
                }
            }
            case RIGHT, DOWN -> {
                List<Tile> reverse = subset.reversed();
                for (int i = start; i < stop; i += change) {
                    adjacent = getAdjacent(reverse.get(i), direction);
                    if (adjacent != null && adjacent.getValue() == 0) {
                        moveDistance += 1;
                        System.out.println("in calculateMoves, moveDistance is: " + moveDistance);
                    }
                }
            }
        }
        return moveDistance;
    }

    private void moveTiles(Tile tile, Direction direction, boolean mergeable) {
        System.out.println(". . . . moveTiles was reached");
        List<Tile> subset = new ArrayList<>();
        List<Tile> valueTiles = new ArrayList<>();

        if (mergeable) {
            System.out.println("!  ! in moveTiles, mergeable is TRUE");
            mergeTiles(tile, direction);
        }
        subset = getSubset(tile.getRow(), tile.getCol(), direction);
        valueTiles = findValueTiles(subset);
        int numberOfValueTiles = valueTiles.size();
        if (numberOfValueTiles > 0) {
            System.out.println("in moveTiles, number of value tiles is: " + numberOfValueTiles);

            switch (direction) {
                case DOWN -> {
                    for (Tile v : valueTiles) {
                        List<Tile> reversed = subset.reversed();
                        for (Tile t : reversed) {
                            if ( t.getValue() == 0 && t.getRow() > v.getRow()) {
                                switchValues(t, v);
                            }
                        }
                    }
                }
                case LEFT -> {
                    for (Tile v : valueTiles) {
                        for (Tile t : subset) {
                            if (t.getValue() == 0 && t.getCol() < v.getCol()) {
                                switchValues(t, v);
                            }
                        }
                    }
                }
                case RIGHT -> {
                    for (Tile v : valueTiles) {
                        List<Tile> reversed = subset.reversed();
                        for (Tile t : reversed) {
                            if (t.getValue() == 0 && t.getCol() > v.getCol()) {
                                switchValues(t, v);
                            }
                        }
                    }
                }
                case UP -> {
                    for (Tile v : valueTiles) {
                        for (Tile t : subset) {
                            if (t.getValue() == 0 && t.getRow() < v.getRow()) {
                                switchValues(t, v);
                            }
                        }
                    }
                }
            }
        }
    }

    private void mergeTiles(Tile tile, Direction direction) {
        List <Tile> subset = getSubset(tile.getRow(), tile.getCol(), direction);
        List<Tile> valueTiles = new ArrayList<>();
        valueTiles = findValueTiles(subset);
        System.out.println("mergeTiles was reached, valueTiles.size is: " + valueTiles.size());

        while (valueTiles.size() > 1) {
            Tile v = valueTiles.getFirst();
            subset.remove(v);
            Tile adjacent = getAdjacent(v, direction);
            if (adjacent != null && v.getValue() != 0 && !isFinalPosition(v, direction)) {
                if (valueTiles.size() == 2 && adjacent.getValue() == 0) {
                    for (Tile t : subset) {
                        if (t.getValue() == v.getValue() ) {
                            System.out.println("------------in mergeTiles1, positions for t and adjacent are: " + t.getRow() + " " + t.getCol() + " | " + adjacent.getRow() + " " + adjacent.getCol());

                            System.out.println("In mergeTiles1, old value is: " + v.getValue());
                            int newValue = v.getValue() * 2;
                            System.out.println("new value is: " + newValue);
                            System.out.println("value for t is: " + t.getValue());
                            adjustTile(t, newValue);
                            adjustTile(v, 0);
                            valueTiles.remove(v);

                        }
                    }
                } else if (valueTiles.size() == 3) {
                    for (Tile t : valueTiles) {
                        if (t.getValue() == v.getValue() && checkIfAdjacentIsEmpty(v, direction)) {
                            System.out.println("In mergeTiles2, old value is: " + v.getValue());
                            int newValue = v.getValue() * 2;
                            System.out.println("new value is: " + newValue);
                            System.out.println("value for t is: " + t.getValue());
                            adjustTile(t, newValue);
                            adjustTile(v, 0);
                        }
                    }
                } else if (v.getValue() == adjacent.getValue()) {
                    System.out.println("In mergeTiles3, old value is: " + v.getValue());
                    int newValue = v.getValue() * 2;
                    System.out.println("new value is: " + newValue);
                    System.out.println("value for adjacent is: " + adjacent.getValue());
                    adjustTile(adjacent, newValue);
                    adjustTile(v, 0);
                }
            }
        }
    }

    private void switchValues(Tile t, Tile v) {
        int newValue = t.getValue();
        adjustTile(t, v.getValue());
        adjustTile(v, newValue);
    }

    public void adjustTile(Tile tile, int value) {
        System.out.println("adjustTile was reached, previous value is: " + tile.getValue() + ", new value is: " + value);
        tile.setValue(value);
        if (tile.getValue() > 0) {
            tile.setBackground(Color.BLUE);
            tile.setForeground(Color.white);
            System.out.println("__ValueTile is adjusted, position row/col: " + tile.getRow() + " " + tile.getCol());
            tile.setText(String.valueOf(tile.getValue()));
            tile.repaint();
            tile.revalidate();
        } else if (tile.getValue() == 0){
            tile.setBackground(Color.gray);
            System.out.println("New empty tile is adjusted, position row/col: " + tile.getRow() + " " + tile.getCol());
            tile.setText(String.valueOf(tile.getValue()));
            tile.repaint();
            tile.revalidate();
        }
    }

    private List<Tile> findValueTiles(List<Tile> subset) {
        List<Tile> valueTiles = new ArrayList<>();
        for (Tile t : subset) {
            if (t.getValue() > 0) {
                valueTiles.add(t);
            }
        }
        return valueTiles;
    }
    private Direction getOpposite(Direction direction) {
        Direction d = null;
        switch (direction) {
            case LEFT -> {
                d = Direction.RIGHT;
                break;
            }
            case RIGHT -> {
                d = Direction.LEFT;
                break;
            }
            case UP -> {
                d = Direction.DOWN;
                break;
            }
            case DOWN -> {
                d = Direction.UP;
                break;
            }
            default -> {
                return null;
            }
        }
        return d;
    }
}


