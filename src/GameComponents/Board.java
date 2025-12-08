package GameComponents;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Board extends JPanel {
    List<Tile> tiles = new ArrayList<>();
    int rows;
    int cols;
    int points;
    boolean full = false;

    public Board (int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        setLayout(new GridLayout(cols, rows));
        getStartingTiles();
        for (Tile tile : tiles){
            add(tile);
        }
    }
    private void getStartingTiles(){
        Tile tileOne = new Tile(getStartingValue(), false, Color.BLUE, 1, 1);
        Tile tileTwo = new Tile(getStartingValue(), false, Color.BLUE, 1, 2);

        for (int i = 0; i < (rows*cols)-2; i++){
            Tile emptyTile = new Tile(0, true, Color.lightGray, i/rows, i%cols);
            tiles.add(emptyTile);
        }
        tiles.add(tileOne);
        tiles.add(tileTwo);
        Collections.shuffle(tiles);
    }
    private int getStartingValue(){
        Random random = new Random();
        List <Integer>values = new ArrayList<>();
        values.add(2);
        values.add(4);
        int randomValue = random.nextInt(2);
        if (randomValue == 0){
            return values.getFirst();
        }
        else {
            return values.get(1);
        }
    }
}
