
import GUI.MainPanel;
import Server.Database.User;

public class Main {
    public static void main(String[] args) {
        int rows = 4;
        int cols = 4;
        User user = new User("Cams");
    new MainPanel(user, rows, cols);
    }
}