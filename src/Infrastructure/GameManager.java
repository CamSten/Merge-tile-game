package Infrastructure;

import GameComponents.GameSession;
import Server.Database.Highscores;
import Server.Database.User;

public class GameManager{
    User user;
    GameSession game;
    GameMediator mediator = GameMediator.getInstance();

    public GameManager(User user) {
        System.out.println("GameManager constructor is reached");

        game = new GameSession(user);
        startNewGame();
    }

    private void startNewGame() {
        game = new GameSession(user);
        mediator.update(Subscriber.EventType.REQUEST_NEW_GAME, null);
    }

    protected void saveScore(int score) {
        System.out.println("In Game, score is: " + score);
        Highscores.saveScore(user, score);
    }
}
