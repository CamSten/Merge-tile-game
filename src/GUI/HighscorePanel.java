package GUI;

import GUI.Game.GameColors;
import GUI.Game.GameFont;
import Server.Database.HighscorePrintout;
import Server.Database.Highscores;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HighscorePanel extends JPanel {
    private List<String>scores = new ArrayList<>();
    private static List<String[]>scorePrintout = new ArrayList<>();
    private Highscores.ScoreValue scoreValue;
    HighscorePrintout highscorePrintout;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");

    public HighscorePanel(HighscorePrintout highscorePrintout) {
        this.highscorePrintout = highscorePrintout;
        if (highscorePrintout != null) {
            this.scorePrintout = highscorePrintout.getScorePrintout();
            this.scoreValue = highscorePrintout.getScoreValue();
            System.out.println("highscorePanel constructor is reached");
            this.scores = scores;
            setLayout(new BorderLayout());
            JPanel scorePanel = new JPanel(new BorderLayout());
            scorePanel.setBackground(GameColors.defaultBackground());
//        scorePanel.setOpaque(true);
            scorePanel.setVisible(true);

            JLabel header = new JLabel("Highscores:");
            header.setFont(GameFont.headerFont());
//        header.setPreferredSize(new Dimension(500, 20));

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setVisible(true);

            JPanel scoreHeader = new JPanel(new GridLayout(1, 3));
            JTextArea namesArea = new JTextArea(scorePrintout(Highscores.ScoreValue.NAME));
            JTextArea movesArea = new JTextArea(scorePrintout(Highscores.ScoreValue.POINTS));
            JTextArea datesArea = new JTextArea(scorePrintout(Highscores.ScoreValue.DATE));
            namesArea.setBorder(new BasicBorders.FieldBorder(Color.lightGray, Color.DARK_GRAY, Color.pink, Color.MAGENTA));
            movesArea.setBorder(new BasicBorders.FieldBorder(Color.lightGray, Color.DARK_GRAY, Color.pink, Color.MAGENTA));
            datesArea.setBorder(new BasicBorders.FieldBorder(Color.lightGray, Color.DARK_GRAY, Color.pink, Color.MAGENTA));
            JButton showName = new JButton("Namn:");
            showName.setFont(GameFont.defaultFont());
            showName.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                sortList(Highscores.ScoreValue.NAME);
                    namesArea.setText(scorePrintout(Highscores.ScoreValue.NAME));
                    movesArea.setText(scorePrintout(Highscores.ScoreValue.POINTS));
                    datesArea.setText(scorePrintout(Highscores.ScoreValue.DATE));
                }
            });
            JButton showMoves = new JButton("Antal drag:");
            showMoves.setFont(GameFont.defaultFont());
            showMoves.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                sortList(Highscores.ScoreValue.POINTS);
                    namesArea.setText(scorePrintout(Highscores.ScoreValue.NAME));
                    movesArea.setText(scorePrintout(Highscores.ScoreValue.POINTS));
                    datesArea.setText(scorePrintout(Highscores.ScoreValue.DATE));
                }
            });
            JButton showDate = new JButton("Datum:");
            showDate.setFont(GameFont.defaultFont());
            showDate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                sortList(Highscores.ScoreValue.DATE);
                    namesArea.setText(scorePrintout(Highscores.ScoreValue.NAME));
                    movesArea.setText(scorePrintout(Highscores.ScoreValue.POINTS));
                    datesArea.setText(scorePrintout(Highscores.ScoreValue.DATE));
                }
            });

            scoreHeader.add(showName);
            scoreHeader.add(showMoves);
            scoreHeader.add(showDate);
            centerPanel.add(scoreHeader, BorderLayout.NORTH);

            JPanel showScores = new JPanel(new GridLayout(1, 3));
            showScores.setBackground(GameColors.defaultBackground());
            showScores.setVisible(true);
            showScores.setOpaque(true);

            namesArea.setFont(GameFont.defaultFont());
            movesArea.setFont(GameFont.defaultFont());
            datesArea.setFont(GameFont.defaultFont());
            namesArea.setOpaque(false);
            movesArea.setOpaque(false);
            datesArea.setOpaque(false);
            namesArea.setEditable(false);
            movesArea.setEditable(false);
            datesArea.setEditable(false);
            namesArea.setPreferredSize(new Dimension(150, 200));
            movesArea.setPreferredSize(new Dimension(100, 200));
            datesArea.setPreferredSize(new Dimension(200, 200));
            showScores.add(namesArea);
            showScores.add(movesArea);
            showScores.add(datesArea);
            centerPanel.add(showScores, BorderLayout.CENTER);

            scorePanel.add(header, BorderLayout.NORTH);
            scorePanel.add(centerPanel, BorderLayout.CENTER);
//        scorePanel.setBackground(GameColors.defaultBackground());
            scorePanel.setVisible(true);
            add(scorePanel, BorderLayout.CENTER);
        }
    }

    private String scorePrintout(Highscores.ScoreValue scoreValue){
        List<String> highscorePrintoutScoreSubset = highscorePrintout.getScoreSubset(scoreValue);
        String printout = "";
        System.out.println("in HighscorePanel, scoreValue is: " + scoreValue);
        for (String s : highscorePrintoutScoreSubset){
            System.out.println("in scorePrintout, s is: " + s);
            printout = printout + "\n" + s;
        }
        return printout;
    }
    public static void sortList(Highscores.ScoreValue scoreValue){
        List<String[]> values = scorePrintout;
        boolean changePlace = false;
        if (!values.isEmpty()) {

            for (int pass = 0; pass < values.size() -1; pass++) {
                for (int i = 1; i < values.size() - pass; i++) {
                    switch (scoreValue) {
                        case NAME: {
                            String score1 = values.get(i)[0];
                            String score2 = values.get(i - 1)[0];
                            changePlace = score1.compareToIgnoreCase(score2) < 0;
                            break;
                        }
                        case POINTS: {
                            int score1 = Integer.parseInt(values.get(i)[1].trim());
                            int score2 = Integer.parseInt(values.get(i - 1)[1].trim());
                            changePlace =  score2 < score1;
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
//        scoreList.clear();
//        for (String [] score : values) {
//            StringBuilder sb = new StringBuilder();
//            for (int j = 0; j < score.length; j++) {
//                sb.append(score[j]);
//                if (j < score.length -1){
//                    sb.append(";");
//                }
//            }
//            scoreList.add(sb.toString());
//        }
    }
}
