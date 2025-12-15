package GameComponents;

import java.util.ArrayList;
import java.util.List;

public class Score implements Subscriber{
    private int totalScore = 0;
    private final Board board;

    public Score(Board board){
        this.board = board;
        board.subscribe(this);
    }

    @Override
    public void update(EventType e, Object o) {
        if(e == EventType.NEW_SCORE && o != null){
            calculateScore(o);
            board.displayScore(EventType.DISPLAY_SCORE, totalScore);
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
