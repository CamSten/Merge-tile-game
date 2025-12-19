package Server.Database;

import GameComponents.Score;
import Infrastructure.GameMediator;
import Infrastructure.Subscriber;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Highscores implements Subscriber {
    GameMediator mediator = GameMediator.getInstance();
    static List<String> scoreList = new ArrayList<>();
    private int score;
    private static final int maxSavedScores = 25;
    public enum ScoreValue {NAME, POINTS, DATE }
    public ScoreValue scoreValue;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");

    public Highscores() {
        mediator.subscribe(this);
        System.out.println("Highscores constructor is reached");
    }
    @Override
    public void update(EventType e, Object data) {
        System.out.println("update in Highscores was reached");
        if (e == EventType.REQUEST_SAVE_SCORE){
            Score newScore = (Score) data;
            saveScore(newScore.getUser(), newScore.getTotalScore());
        }
        else if (e == EventType.REQUEST_ALL_HIGHSCORES){
            ScoreValue scoreValue = (ScoreValue) data;
            sendHighscores(scoreValue);
        }
    }

    private void sendHighscores(ScoreValue scoreValue){
        readSaveFile();
        List<String[]> scorePrintout = getScorePrintout();
        HighscorePrintout highscorePrintout = new HighscorePrintout(scorePrintout, scoreValue);
        mediator.update(EventType.RETURN_ALL_HIGHSCORES, highscorePrintout);
    }
    public int getScore() {
        return score;
    }
    public String getScoreString (){
        return Integer.toString(score);
    }
    private static Path getPath() {
        Path path = Paths.get("src/Server/Database/Scores.txt");
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    private List<String> readSaveFile() {
        if (scoreList.isEmpty()){
            scoreList = new ArrayList<>();}
        else {
            scoreList.clear();
        }
        Path saveFile = getPath();
        String score;
        try (BufferedReader reader = new BufferedReader(new FileReader(saveFile.toFile()))) {
            while ((score = reader.readLine()) != null) {
                System.out.print("score in scoreList is: " + score);
                scoreList.add(score);
            }
        } catch (Exception e) {
            System.out.println(errorFileRead);
        }
        for (String s : scoreList){
            System.out.println("saved score: " + s);
        }
        return scoreList;
    }
    public static void saveScore(User user, int score) {
        String userName = user.getUsername();
        if (scoreList.size() >= maxSavedScores) {
            removeScore();
        }
        Path saveTo = getPath();
        try (BufferedWriter saving = Files.newBufferedWriter(saveTo, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            LocalDateTime now = LocalDateTime.now();
            String formatted = now.format(formatter);
            System.out.println("in Highscores, score is: " + score);
            saving.write(userName.trim() + ";" + score + ";" + formatted + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static List<String[]> splitScoreString(){
        List<String[]> savedMoves = new ArrayList<>();
        for (String savedScore : scoreList) {
            String[] scoreValues = savedScore.split(";");
            savedMoves.add(scoreValues);
        }
        return savedMoves;
    }
    public List<String[]> getScorePrintout(){
        List<String[]> result = new ArrayList<>();
        List<String[]> scoreValues = splitScoreString();

        for (String[] s : scoreValues) {
            result.add(new String[]{ s[0], s[1], s[2] });
        }
        return result;
    }

    private static void removeScore(){
        scoreList.remove(scoreList.size() - 1);
        overWriteFile();
    }

    private static void overWriteFile(){
        Path saveTo = getPath();
        try (BufferedWriter overWrite = new BufferedWriter(new FileWriter(saveTo.toFile()))){
            for (String score : scoreList){
                overWrite.write(score + "\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearSavedScores(){
        scoreList.clear();
        Path saveFile = getPath();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile.toFile(), false))) {
        } catch (Exception e) {
            System.out.println(errorFileRead);
        }
    }

    private static final String errorFileRead = "Error reading file";
}


