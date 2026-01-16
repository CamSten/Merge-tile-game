package Infrastructure;

import GUI.Game.Board;
import GUI.MainFrame;
import GameComponents.Game;
import GameComponents.GameSession;
import Server.Database.GameDatabase;
import Server.Database.HighscoreDatabase;
import Server.Database.User;

import java.util.ArrayList;
import java.util.List;

public class GameManager implements Subscriber {
    private Mediator mediator;
    private List<Game> savedGames;
    private MainFrame mainFrame;
    private static GameManager gameManager = new GameManager();
    private Board board;
    private List<Game> games = new ArrayList<>();

    private GameManager () {
    }
    public static GameManager getInstance(){
        return gameManager;
    }
    public void initiateSession(User user, Game game, MainFrame mainFrame){
        System.out.println("initiatesession i GameDatabase is reached.");
        this.mainFrame = mainFrame;
        GameSession session = new GameSession(user, mediator);
        session.subscribe();
        if (game == null) {
            System.out.println("game is null");
            session.start();
        }
        else {
            System.out.println("game is not null");
            session.restoreFromGame(game);
        }
    }
    public Game getSession(User player){
        Game g = null;
        for (Game gs: games){
            if (gs.getUser().getUsername().equalsIgnoreCase(player.getUsername())){
                g = gs;
            }
        }
        return g;
    }
    public void startNewGame(User user, List<Integer>values) {
        this.board = new Board(values, mediator, mainFrame);
        mainFrame.showGameBoard(values);
    }

    protected void saveScore(int score) {
        System.out.println("In Game, score is: " + score);
//        HighscoreDatabase.saveScore(user, score);
    }
    private void saveGame(Game game){
        for (Game g : games){
            if (g.getUser().getUsername().equalsIgnoreCase(game.getUser().getUsername())){

            }
        }
        savedGames.add(game);
    }
    private void getSavedGame(User user){
        Game savedGame = null;
        if (!savedGames.isEmpty()){
            for (Game g : savedGames){
                if (g.getUser().equals(user.getUsername())){
                    savedGame = g;
                }
            }
        }
        if (savedGame != null) {
            mediator.update(EventType.RETURN_GET_SAVED_GAME_TRUE, savedGame.getAllValues());
        }
        else {
            mediator.update(EventType.RETURN_GET_SAVED_GAME_FALSE, savedGame.getAllValues());
        }
    }

    public void subscribe(Mediator mediator){
        this.mediator = mediator;
        mediator.subscribe(this);
    }
    @Override
    public void update(EventType e, Object data) {
        System.out.println("____ UPDATE in GAME MANAGER IS REACHED, eventType is: " + e);
        if (data != null){
            System.out.println("data is: " + data.getClass());
        }
        switch (e){
            case REQUEST_SAVE_GAME_EXECUTE-> {
                Game game = (Game) data;
                saveGame(game);
            }
            case  RETURN_UPDATE_TILES -> {
                List<Integer> values = (List<Integer>) data;
                updateGameBoard(values);
            }
            case  RETURN_NEW_SCORE -> {
                System.out.println("case return new score in AppManager was reached");
                int result = (int) data;
                updatePoints(result);
            }
            case REQUEST_CONTINUE_GAME -> {
                mainFrame.showWin();
            }
            case RETURN_DISPLAY_SCORE -> {
                int points = (Integer) data;
                mainFrame.updateScoreDisplay(points);
            }
            case REQUEST_REMOVE_GAME->  {
                Game endedGame = (Game) data;
                removeGame(endedGame);
             }
            case CONFIRM_FINISHED_SESSION -> {
                GameSession endedSession = (GameSession) data;
                mediator.unsubscribeLowerGame(endedSession);
            }
        }
    }
    private void removeGame(Game endedGame){
        System.out.println("removeGame in GAME MANAGER is reached");

        Game target = null;
        for (Game g : games){
            if (g.getUser().getUsername().equalsIgnoreCase(endedGame.getUser().getUsername())){
                target = g;
            }
        }
        if (target != null){
            games.remove(target);
        }
        mediator.update(EventType.RETURN_REMOVE_GAME, endedGame);
    }
    public void updateGameBoard(List<Integer> values){
        mainFrame.updateBoard(values);
    }
    public void updatePoints(int points){
        System.out.println("updatePoints in MainPanel was reached, points are: " + points);
        mainFrame.updateScoreDisplay(points);
    }
}
