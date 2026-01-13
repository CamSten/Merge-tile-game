package GameComponents;

import Infrastructure.Mediator;
import Infrastructure.Subscriber;
import Server.Database.User;

public class Score implements Subscriber {
    private Mediator mediator = Mediator.getInstance();
    private int totalScore = 0;
    private GameSession game;
    User user;

    public Score(GameSession game){
        this.game = game;
        this.user = game.getUser();
        this.totalScore = game.getTotalPoints();

    }

    @Override
    public void update(EventType e, Object o) {
//        System.out.println(" UPDATE IN SCORE IS REACHED");
//        if(e == EventType.REQUEST_NEW_SCORE && o != null){
//            calculateScore(o);
//            mediator.update(EventType.RETURN_DISPLAY_SCORE, totalScore);
//            System.out.println("In Score, score is: " + totalScore);
//        }
    }
    private void calculateScore(Object o){
        if (o instanceof Integer i) {
            totalScore+= i;
        }
    }

    private void setTotalScore(int score){
        totalScore = totalScore+(totalScore-score);
    }

    public int getTotalScore() {
        return totalScore;
    }
    public User getUser(){
        return user;
    }
}
