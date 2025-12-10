package GameComponents;

import javax.swing.*;
import java.awt.*;

public class Tile extends JButton {
    int value;
    boolean empty;
    Color color;
    int row;
    int col;
    Color textColor = Color.WHITE;
    int tileSize = 40;
    public Tile(int value, boolean empty, Color color, int row, int col) {
        setEnabled(false);
        this.value = value;
        this.empty = empty;
        this.color = color;
        this.row = row;
        this.col = col;
        setBackground(color);
        setMinimumSize(new Dimension(tileSize, tileSize));
        setFont(new Font("Arial",Font.BOLD, 20));
        if (value > 0) {
            setText(String.valueOf(value));
            setForeground(textColor);
        }
    }
    public void adjustTile(Tile tile, int value){
        System.out.println("adjustTile in Tile was reached");
        tile.setValue(value);
        System.out.println("in adjustTile in Tile, value is: " + tile.getValue());
        if (tile.getValue() > 0){
            setBackground(Color.BLUE);
            setText(String.valueOf(tile.getValue()));
            setForeground(Color.white);
        }
        else {
            setBackground(Color.gray);
        }
    }
    public int getValue(){
        return value;
    }
    public boolean isEmpty(){
        return empty;
    }
    public Color getColor(){
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
    public void setCol(int newCol){
        col = newCol;
    }
    public void setRow (int newRow){
        row = newRow;
    }
    public void setValue(int newValue){
        this.value = newValue;
    }
}
