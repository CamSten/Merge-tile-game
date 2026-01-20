package GameComponents;
import Server.Database.User;

public class Score  {
    private int totalScore = 0;
    private GameSession game;
    User user;

    public Score(GameSession game){
        this.game = game;
        this.user = game.getUser();
        this.totalScore = game.getTotalPoints();
    }
    public int getTotalScore() {
        return totalScore;
    }
    public User getUser(){
        return user;
    }
}