package Infrastructure;

import GUI.Game.Board;
import GUI.MainPanel;
import GameComponents.GameSession;
import Server.Database.Highscores;
import Server.Database.User;

import javax.swing.*;
import java.awt.*;

import static Infrastructure.Subscriber.EventType.DISPLAY_SCORE;

public class GameManager implements Subscriber {
    User user;
    GameSession game;
    GameMediator mediator;

    public GameManager(User user) {
        System.out.println("GameManager constructor is reached");
        setMediator();

        game = new GameSession(user);
        startNewGame();
    }

    private void startNewGame() {
        game = new GameSession(user);
        mediator.subscribe(game);
        mediator.update(EventType.REQUEST_NEW_GAME, game.getBoard());
    }

    protected void saveScore(int score) {
        System.out.println("In Game, score is: " + score);
        Highscores.saveScore(user, score);
    }

    private void setMediator() {
        GameMediator mediator = GameMediator.getInstance();
        this.mediator = mediator;
        mediator.subscribe(this);

    }

    @Override
    public void update(EventType e, Object data) {
        System.out.println("update in gameManager is reached. eventtype is: " + e);
        switch (e) {
            case ADD_GAME_PANEL -> {

            }

        }
    }
}
