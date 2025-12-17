package Server.Database;

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

public class Highscores {
    static List<String> scoreList = new ArrayList<>();
    private int score;
    private static final int maxSavedScores = 25;
    private enum scoreValue {NAME, MOVES, DATE }
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");

    public Highscores(User user, int score) {

    }
    public int getScore() {
        return score;
    }
    public String getScoreString (){
        return Integer.toString(score);
    }
    private static Path getPath() {
        Path path = Paths.get("src/GameComponents/Scores.txt");
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
        scoreList.clear();
        Path saveFile = getPath();
        String score;
        try (BufferedReader reader = new BufferedReader(new FileReader(saveFile.toFile()))) {
            while ((score = reader.readLine()) != null) {
                scoreList.add(score);
            }
        } catch (Exception e) {
            System.out.println(errorFileRead);
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
    public static List<String> getScorePrintout(){
        List<String>allScorePrintouts = new ArrayList<>();
        List<String[]> scoreValues = splitScoreString();
        String printout = "";
        int start;
        int end;
        int step;
        if (!scoreValues.isEmpty()) {
            for (int type = 0; type <= 2; type++) {
                if (type == 2) {
                    start = scoreValues.size() - 1;
                    end = -1;
                    step = -1;
                } else {
                    start = 0;
                    end = scoreValues.size();
                    step = 1;
                }
                for (int i = start; i != end; i += step) {
                    type = i;
                    String whatToPrint = "";
                    switch (type) {
                        case 0: {
                            whatToPrint = scoreValues.get(i)[0];

                            break;
                        }
                        case 1: {
                            whatToPrint = scoreValues.get(i)[1];
                            break;
                        }
                        case 2: {
                            whatToPrint = scoreValues.get(i)[2];
                            break;
                        }
                    }
                    printout = printout + whatToPrint + "\n";
                    allScorePrintouts.add(whatToPrint);
                }
            }
        }
        return allScorePrintouts;
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
    public void sortList (scoreValue type){
        List<String[]> values = splitScoreString();
        boolean changePlace = false;
        if (!values.isEmpty()) {

            for (int pass = 0; pass < values.size() -1; pass++) {
                for (int i = 1; i < values.size() - pass; i++) {
                    switch (type) {
                        case NAME: {
                            String score1 = values.get(i)[0];
                            String score2 = values.get(i - 1)[0];
                            changePlace = score1.compareToIgnoreCase(score2) < 0;
                            break;
                        }
                        case MOVES: {
                            int score1 = Integer.parseInt(values.get(i)[1].trim());
                            int score2 = Integer.parseInt(values.get(i - 1)[1].trim());
                            changePlace =  score2 > score1;
                            break;
                        }
                        case DATE: {
                            LocalDateTime score1 = LocalDateTime.parse(values.get(i)[2], formatter);
                            LocalDateTime score2 = LocalDateTime.parse(values.get(i - 1)[2], formatter);
                            changePlace = score2.isAfter(score1);
                            break;
                        }
                    }
                    if (changePlace) {
                        String[] temp = values.get(i-1);
                        values.set(i-1, values.get(i));
                        values.set(i, temp);
                    }
                }
            }
        }
        scoreList.clear();
        for (String [] score : values) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < score.length; j++) {
                sb.append(score[j]);
                if (j < score.length -1){
                    sb.append(";");
                }
            }
            scoreList.add(sb.toString());
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


