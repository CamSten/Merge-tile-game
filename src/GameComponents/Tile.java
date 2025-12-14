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

    public static void adjustTile(Tile tile, int value){
        tile.setValue(value);
        if (tile.getValue() > 0){
            tile.setBackground(getColor(value));
            tile.setForeground(Color.WHITE);
            tile.setText(String.valueOf(tile.getValue()));
            tile.repaint();
            tile.revalidate();
        }
        else {
            tile.setBackground(Color.lightGray);
            tile.repaint();
            tile.revalidate();
        }
    }
//    public static void adjustTile(Tile tile, int value) {
//        System.out.println("adjustTile was reached, previous value is: " + tile.getValue() + ", new value is: " + value);
//        tile.setValue(value);
//        if (tile.getValue() > 0) {
//            tile.setBackground(Color.BLUE);
//            tile.setForeground(Color.white);
//            System.out.println("__ValueTile is adjusted, position row/col: " + tile.getRow() + " " + tile.getCol());
//            tile.setText(String.valueOf(tile.getValue()));
//            tile.repaint();
//            tile.revalidate();
//        } else if (tile.getValue() == 0){
//            tile.setBackground(Color.lightGray);
//            System.out.println("New empty tile is adjusted, position row/col: " + tile.getRow() + " " + tile.getCol());
//            tile.setText(String.valueOf(tile.getValue()));
//            tile.repaint();
//            tile.revalidate();
//        }
//    }
    private static Color getColor(int value){
        double base = 2;
        int exponent = 0;
        double newvalue = 0;

        double newValue = Math.sqrt(value);


            for (int i = 0; i < value; i++){
                double number = Math.pow (base, i);
                if (number == value){
                    exponent = i;
                }
            }


        TileColor tileColor = new TileColor(exponent);
        return tileColor.getColor();
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
