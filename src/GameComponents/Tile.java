package GameComponents;

import javax.swing.*;
import java.awt.*;

public class Tile extends JButton {
    int value;
    boolean empty;
    Color color;
    int row;
    int col;
    public Tile(int value, boolean empty, Color color, int row, int col){
        this.value = value;
        this.empty = empty;
        this.color = color;
        this.row = row;
        this.col = col;
    }
    public int getValue(){
        return value;
    }
    public boolean isEmpty(){
        return empty;
    }
    public Color color(){
        return color;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public int[] getPosition(){
        return new int[]{row, col};
    }

}
