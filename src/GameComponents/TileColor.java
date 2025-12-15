package GameComponents;

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
                    //Fuchsia flame
                }
                case 2: {
                    this.color = new Color(135, 14, 62);
                    break;
                    //Dark amaranth
                }
                case 3: {
                    this.color = new Color(136, 13, 30);
                    break;
                    //Crushed berry
                }
                case 4: {
                    this.color = new Color(214, 40, 40);
                    break;
                    //Flag red
                }
                case 5: {
                    this.color = new Color(209, 96, 61);
                    break;
                    //Fiery terracotta
                }
                case 6: {
                    this.color = new Color(254, 98, 29);
                    break;
                    //Blaze orange
                }
                case 7: {
                    this.color = new Color(250, 162, 117);
                    break;
                    //Tangerine dream
                }
                case 8: {
                    this.color = new Color(255, 202, 158);
                    break;
                    //peach glow
                }
                case 9: {
                    this.color = new Color(255, 250, 148);
                    break;
                    //Canary yellow
                }
                case 10: {
                    this.color = new Color(43, 192, 22);
                    break;
                    //bright fern
                }
                case 11: {
                    this.color = new Color(38, 169, 108);
                    break;
                    //jungle green
                }
                case 12: {
                    this.color = new Color(50, 147, 111);
                    break;
                    //Sea green
                }
                case 13: {
                    this.color =new Color(56, 125, 122);
                    break;
                    //pine blue
                }
                default: {
                    this.color = new Color(57, 94, 102);
                    break;
                    //dark slate gray
                }
            }
        }
    }
    public Color getColor(){
        return color;
    }

}
