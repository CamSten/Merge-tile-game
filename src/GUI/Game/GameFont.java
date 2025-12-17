package GUI.Game;

import java.awt.*;

public class GameFont {
    private static final Font DEFAULT_FONT = new Font("DialogInput", Font.PLAIN, 14);
    private static final Font HEADER_FONT = new Font("DialogInput", Font.BOLD, 14);
    private static final Font TOP_HEADER_FONT = new Font("DialogInput", Font.BOLD, 16);

    public static Font defaultFont (){
        return DEFAULT_FONT;
    }
    public static Font headerFont (){
        return HEADER_FONT;
    }
    public static Font topHeaderFont (){
        return TOP_HEADER_FONT;
    }

}
