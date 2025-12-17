package GameComponents;

import Infrastructure.Subscriber;

public class Score implements Subscriber {
    private int totalScore = 0;
    private GameSession game;

    public Score(GameSession game){
        this.game = game;
        game.subscribe(this);
    }

    @Override
    public void update(EventType e, Object o) {
        System.out.println(" UPDATE IN SCORE IS REACHED");
        if(e == EventType.NEW_SCORE && o != null){
            calculateScore(o);
            game.update(EventType.DISPLAY_SCORE, totalScore);
            System.out.println("In Score, score is: " + totalScore);
        }
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
}
