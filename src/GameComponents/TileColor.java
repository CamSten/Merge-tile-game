package GameComponents;

import java.awt.*;

public class TileColor {
    Color color = Color.white;

    public TileColor(int exponent){
        System.out.println("int TileColor, exponent is: " + exponent);
        if (exponent> 0){
            switch (exponent){
                case 1: {
                    this.color = Color.cyan;
                    break;
                }
                case 2: {
                    this.color = Color.CYAN;
                    break;
                }
                case 3: {
                    this.color = Color.GREEN;
                    break;
                }
                case 4: {
                    this.color = Color.green;
                    break;
                }
                case 5: {
                    this.color = Color.yellow;
                    break;
                }
                case 6: {
                    this.color = Color.YELLOW;
                    break;
                }
                case 7: {
                    this.color = Color.RED;
                    break;
                }
                case 8: {
                    this.color = Color.red;
                    break;
                }
                case 9: {
                    this.color = Color.MAGENTA;
                    break;
                }
                case 10: {
                    this.color = Color.magenta;
                    break;
                }
                case 11: {
                    this.color = Color.blue;
                    break;
                }
                case 12: {
                    this.color = Color.BLUE;
                    break;
                }
                case 13: {
                    this.color = Color.black;
                    break;
                }
                default: {
                    this.color = Color.darkGray;
                }
            }
        }
    }
    public Color getColor(){
        return color;
    }
}
