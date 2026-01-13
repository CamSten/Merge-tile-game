
import Infrastructure.AppManager;
import Infrastructure.GameManager;
import Server.Database.Highscores;
import Server.Database.User;
import Server.Database.UserDatabase;

public class Main {
    public static void main(String[] args) {
        int rows = 4;
        int cols = 4;
        startProgram();

    }
    private static void startProgram(){
        new AppManager();

    }
}