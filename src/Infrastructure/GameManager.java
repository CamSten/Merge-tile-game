package Infrastructure;

import GUI.Game.Board;
import GUI.MainFrame;
import GameComponents.Game;
import GameComponents.GameSession;
import Server.Database.Highscores;
import Server.Database.User;

import java.util.List;

public class GameManager implements Subscriber {
    User user;
    GameSession session;
    private Mediator mediator = Mediator.getInstance();
    private List<Game> savedGames;
    private MainFrame mainFrame;
    private static GameManager gameManager = new GameManager();
    private Board board;

    private GameManager () {
    }
    public static GameManager getInstance(){
        return gameManager;
    }
    public void initiate (User user, MainFrame mainFrame){
        this.mainFrame = mainFrame;
        this.session = new GameSession(user);
        session.subscribe();
        session.start();
    }
    public void startNewGame(User user, List<Integer>values) {
        this.board = new Board(values, mediator, mainFrame);
        mainFrame.showGameBoard(values);
    }

    protected void saveScore(int score) {
        System.out.println("In Game, score is: " + score);
        Highscores.saveScore(user, score);
    }
    private void saveGame(Game game){
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
        mediator.update(EventType.RETURN_SAVED_GAME, savedGame.getAllValues());
    }

    public void subscribe(){
        mediator.subscribe(this);
    }
    @Override
    public void update(EventType e, Object data) {
        System.out.println("____ UPDATE in GAME MANAGER IS REACHED, eventType is: " + e);
        switch (e){
            case REQUEST_SAVE_GAME -> {
               Game game = (Game) data;
                saveGame(game);
            }
            case REQUEST_GET_SAVED_GAME -> {
                User u = (User) data;
                getSavedGame(user);
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
            case  REQUEST_CONTINUE_GAME -> {
                mainFrame.showWin();
            }
            case RETURN_DISPLAY_SCORE -> {
                int points = (Integer) data;
                mainFrame.updateScoreDisplay(points);
            }
//            case NEW_UNCHECKED_VALUES -> {
//                session.
//            }
        }
    }
    public void updateGameBoard(List<Integer> values){
        mainFrame.updateBoard(values);
    }
    public void updatePoints(int points){
        System.out.println("updatePoints in MainPanel was reached, points are: " + points);
        mainFrame.updateScoreDisplay(points);
    }
}
