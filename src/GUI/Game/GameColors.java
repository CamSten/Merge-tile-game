package GUI.Game;
import java.awt.*;
public class GameColors {
        private static final Color backgroundColor(int value) {
            if (value == 0) {
                return Color.white;
            } else {
                return Color.pink;
            }
        }
        private static final Color foregroundColor (int value) {
            if (value == 0){
                return Color.white;
            }
            else {
                return Color.magenta;
            }
        }
        private static final Color headerTextColor() {
            return Color.magenta;
        }
        private static final Color defaultTextColor (){
            return Color.darkGray;
        }
        private static final Color defaultBackgroundColor(){
            return Color.pink;
        }
        public static Color foreground (int value){
            return foregroundColor(value);
        }
        public static Color background (int value){
            return backgroundColor(value);
        }
        public static Color headerText (){
            return headerTextColor();
        }
        public static Color defaultText(){
            return defaultTextColor();
        }
        public static Color defaultBackground(){
            return defaultBackgroundColor();
        }

}
