package Server.Database;

import java.util.ArrayList;
import java.util.List;

public class HighscorePrintout {
    private List<String[]> scorePrintout;
    private HighscoreDatabase.ScoreValue scoreValue;

    public HighscorePrintout(List<String[]> scorePrintout, HighscoreDatabase.ScoreValue scoreValue) {
        this.scorePrintout = scorePrintout;
        this.scoreValue = scoreValue;
    }

    public List<String[]> getScorePrintout() {
        return scorePrintout;
    }

    public HighscoreDatabase.ScoreValue getScoreValue() {
        return scoreValue;
    }
    public List<String>getScoreSubset(HighscoreDatabase.ScoreValue scoreValue){
        List<String> scoreSubset = new ArrayList<>();
        switch (scoreValue){
            case HighscoreDatabase.ScoreValue.NAME -> {
                scoreSubset = getScorePrintoutName();
                break;
            }
            case HighscoreDatabase.ScoreValue.DATE -> {
                scoreSubset = getScorePrintoutDate();
                break;
            }
            case HighscoreDatabase.ScoreValue.POINTS -> {
                scoreSubset = getScorePrintoutPoints();
                break;
            }
        }
        return scoreSubset;
    }

    private List<String> getScorePrintoutName() {
        List<String> scoreName = new ArrayList<>();
        for (String[] score : scorePrintout) {
            scoreName.add(score[0]);
        }
        return scoreName;
    }
    private List<String> getScorePrintoutPoints() {
        List<String> scorePoints = new ArrayList<>();
        for (String[] score : scorePrintout) {
            scorePoints.add(score[1]);
            System.out.println("________________in getScorePrintoutPoints in HighscorePrintout, score is: " + score[1]);
        }
        return scorePoints;
    }
    private List<String> getScorePrintoutDate() {
        List<String> scoreDate = new ArrayList<>();
        for (String[] score : scorePrintout) {
            scoreDate.add(score[2]);
        }
        return scoreDate;
    }
}
