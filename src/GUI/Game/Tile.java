package GUI.Game;
import javax.swing.*;
import java.awt.*;

public class Tile extends JLabel{
    int value;
    boolean empty;
    Color color;
    int row;
    int col;
    private static Color backgroundColor = Color.darkGray;
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
        setBorder(
                BorderFactory.createLineBorder(Color.lightGray, 1, false));
        setOpaque(true);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setMinimumSize(new Dimension(tileSize, tileSize));
        setFont(new Font("Arial",Font.BOLD, 26));
        if (value > 0) {
            setText(String.valueOf(value));
            setForeground(textColor);
        }
    }
    public static void adjustTile(Tile tile, int value){
        tile.setValue(value);
        if (tile.getValue() > 0){
            tile.setBackground(getColor(value));
            tile.setForeground(new Color(245, 245, 245));
            tile.setText(String.valueOf(tile.getValue()));
            tile.repaint();
            tile.revalidate();
        }
        else {
            tile.setBackground(backgroundColor);
            tile.setText("");
            tile.repaint();
            tile.revalidate();
        }
    }
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
    public void setValue(int newValue){
        this.value = newValue;
    }
}