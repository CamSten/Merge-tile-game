package Infrastructure;
import GUI.MainFrame;
import GameComponents.Game;
import GameComponents.GameSession;
import Server.Database.User;
import java.util.ArrayList;
import java.util.List;

public class GameManager implements Subscriber {
    private Mediator mediator;
    private List<Game> savedGames;
    private MainFrame mainFrame;
    private static GameManager gameManager = new GameManager();
    private List<Game> games = new ArrayList<>();
    private List<GameSession> sessions = new ArrayList<>();
    private int highscore = 0;

    private GameManager () {
    }
    public static GameManager getInstance(){
        return gameManager;
    }
    public void initiateSession(User user, Game game, MainFrame mainFrame){
        this.mainFrame = mainFrame;
        GameSession session = new GameSession(user, highscore, mediator);
        session.subscribe();
        if (game == null) {
            session.start();
        }
        else {
            session.restoreFromGame(game);
        }
    }
    void getHighscore(){
        mediator.update(EventType.REQUEST_HIGHEST_SCORE, null);
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
        mainFrame.showGameBoard(values, highscore);
    }
    private void saveGame(Game game){
        for (Game g : games){
            if (g.getUser().getUsername().equalsIgnoreCase(game.getUser().getUsername())){

            }
        }
        savedGames.add(game);
    }
    public void subscribe(Mediator mediator){
        this.mediator = mediator;
        mediator.subscribe(this);
    }
    @Override
    public void update(EventType e, Object data) {
        switch (e){
            case REQUEST_SAVE_GAME_EXECUTE-> {
                Game game = (Game) data;
                saveGame(game);
            }
            case RETURN_UPDATE_TILES -> {
                List<Integer> values = (List<Integer>) data;
                updateGameBoard(values);
            }
            case RETURN_EMPTY_SCORELIST -> {
                setHighscore(0);
                for (GameSession session : sessions){
                    session.setHighscore(0);
                }
            }
            case RETURN_HIGHEST_SCORE -> {
                int value = (Integer) data;
                setHighscore(value);
                for (GameSession session : sessions){
                    session.setHighscore(value);
                }
            }
            case RETURN_NEW_HIGHSCORE -> {
                int highestScore = (Integer) data;
                if (highestScore > highscore) {
                    this.highscore = highestScore;
                    mainFrame.updateHighscoreDisplay(highscore);
                }
            }
            case RETURN_NEW_SCORE -> {
                int result = (int) data;
                updatePoints(result);
            }
            case REQUEST_CONTINUE_GAME -> {
                List<Integer> values = (List<Integer>) data;
                updateGameBoard(values);
                mainFrame.showWin();
            }
            case RETURN_DISPLAY_SCORE -> {
                getHighscore();
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
                mediator.update(EventType.REQUEST_HIGHEST_SCORE, endedSession.getTotalPoints());
            }
        }
    }
    private void removeGame(Game endedGame){
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
        mainFrame.updateScoreDisplay(points);
    }
    public void setHighscore(int value){
        this.highscore = value;
    }
}