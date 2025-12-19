
import GUI.MainPanel;
import GameComponents.GameSession;
import Infrastructure.AppManager;
import Infrastructure.GameManager;
import Server.Database.Highscores;
import Server.Database.User;

public class Main {
    public static void main(String[] args) {
        int rows = 4;
        int cols = 4;
        User user = new User("Cams");
        new Highscores();
        new AppManager(user);
    }
}