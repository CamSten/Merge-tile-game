package GUI.Game;
import java.awt.*;

public class TileColor {
    Color color = Color.white;

    public TileColor(int exponent){
        System.out.println("int TileColor, exponent is: " + exponent);
        if (exponent> 0){
            switch (exponent){
                case 1: {
                    this.color = new Color(222, 44, 121);
                    break;
                }
                case 2: {
                    this.color = new Color(135, 14, 62);
                    break;
                }
                case 3: {
                    this.color = new Color(136, 13, 30);
                    break;
                }
                case 4: {
                    this.color = new Color(214, 40, 40);
                    break;
                }
                case 5: {
                    this.color = new Color(209, 96, 61);
                    break;
                }
                case 6: {
                    this.color = new Color(254, 98, 29);
                    break;
                }
                case 7: {
                    this.color = new Color(250, 162, 117);
                    break;
                }
                case 8: {
                    this.color = new Color(255, 202, 158);
                    break;
                }
                case 9: {
                    this.color = new Color(255, 250, 148);
                    break;
                }
                case 10: {
                    this.color = new Color(43, 192, 22);
                    break;
                }
                case 11: {
                    this.color = new Color(38, 169, 108);
                    break;
                }
                case 12: {
                    this.color = new Color(50, 147, 111);
                    break;
                }
                case 13: {
                    this.color =new Color(56, 125, 122);
                    break;
                }
                default: {
                    this.color = new Color(57, 94, 102);
                    break;
                }
            }
        }
    }
    public Color getColor(){
        return color;
    }
}