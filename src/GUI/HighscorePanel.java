package GUI;

import GUI.Game.GameColors;
import GUI.Game.GameFont;
import Server.Database.HighscorePrintout;
import Server.Database.HighscoreDatabase;

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
    private List<String[]>scorePrintout = new ArrayList<>();
    private HighscoreDatabase.ScoreValue scoreValue;
    HighscorePrintout highscorePrintout;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");

    public HighscorePanel(HighscorePrintout highscorePrintout) {
        this.highscorePrintout = highscorePrintout;
        if (highscorePrintout != null) {
            this.scorePrintout = highscorePrintout.getScorePrintout();
            sortList(HighscoreDatabase.ScoreValue.POINTS);
            this.scoreValue = highscorePrintout.getScoreValue();
            System.out.println("highscorePanel constructor is reached");
            this.scores = scores;
            setLayout(new BorderLayout());
            JPanel scorePanel = new JPanel(new BorderLayout());
            scorePanel.setBackground(Color.DARK_GRAY);
            scorePanel.setVisible(true);

            JLabel header = new JLabel("Highscores:");
            header.setFont(GameFont.topHeaderFont());
            header.setForeground(GUI.Game.GameColors.headerText());;
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.setVisible(true);

            JPanel scoreHeader = new JPanel(new GridLayout(1, 3));
            JTextArea namesArea = new JTextArea(scorePrintout(HighscoreDatabase.ScoreValue.NAME));
            namesArea.setForeground(GUI.Game.GameColors.headerText());
            namesArea.setFont(GUI.Game.GameFont.headerFont());
            JTextArea movesArea = new JTextArea(scorePrintout(HighscoreDatabase.ScoreValue.POINTS));
            movesArea.setForeground(GUI.Game.GameColors.headerText());
            movesArea.setFont(GUI.Game.GameFont.headerFont());
            JTextArea datesArea = new JTextArea(scorePrintout(HighscoreDatabase.ScoreValue.DATE));
            datesArea.setForeground(GUI.Game.GameColors.headerText());
            datesArea.setFont(GUI.Game.GameFont.headerFont());
            namesArea.setBorder(
                    BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 1, true));
            movesArea.setBorder(
                    BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 1, true));
            datesArea.setBorder(
                    BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 1, true));
            JButton showName = new JButton("Name:");
            showName.setBackground(Color.DARK_GRAY);
            showName.setForeground(GUI.Game.GameColors.headerText());
            showName.setFont(GUI.Game.GameFont.headerFont());
            showName.setBorder(
                    BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 2, true));
            showName.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                sortList(HighscoreDatabase.ScoreValue.NAME);
                    namesArea.setText(scorePrintout(HighscoreDatabase.ScoreValue.NAME));
                    movesArea.setText(scorePrintout(HighscoreDatabase.ScoreValue.POINTS));
                    datesArea.setText(scorePrintout(HighscoreDatabase.ScoreValue.DATE));
                }
            });
            JButton showMoves = new JButton("Score:");
            showMoves.setBackground(Color.DARK_GRAY);
            showMoves.setForeground(GUI.Game.GameColors.headerText());
            showMoves.setFont(GUI.Game.GameFont.headerFont());
            showMoves.setBorder(
                    BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 2, true));
            showMoves.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                sortList(HighscoreDatabase.ScoreValue.POINTS);
                    namesArea.setText(scorePrintout(HighscoreDatabase.ScoreValue.NAME));
                    movesArea.setText(scorePrintout(HighscoreDatabase.ScoreValue.POINTS));
                    datesArea.setText(scorePrintout(HighscoreDatabase.ScoreValue.DATE));
                }
            });
            JButton showDate = new JButton("Date:");
            showDate.setBackground(Color.DARK_GRAY);
            showDate.setForeground(GUI.Game.GameColors.headerText());
            showDate.setFont(GUI.Game.GameFont.headerFont());
            showDate.setBorder(
                    BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 2, true));
            showDate.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                sortList(HighscoreDatabase.ScoreValue.DATE);
                    namesArea.setText(scorePrintout(HighscoreDatabase.ScoreValue.NAME));
                    movesArea.setText(scorePrintout(HighscoreDatabase.ScoreValue.POINTS));
                    datesArea.setText(scorePrintout(HighscoreDatabase.ScoreValue.DATE));
                }
            });

            scoreHeader.add(showName);
            scoreHeader.add(showMoves);
            scoreHeader.add(showDate);
            centerPanel.add(scoreHeader, BorderLayout.NORTH);

            JPanel showScores = new JPanel(new GridLayout(1, 3));
            showScores.setBackground(Color.DARK_GRAY);
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
            scorePanel.setBorder(
                    BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 4, true));
            add(scorePanel, BorderLayout.CENTER);
        }
    }

    private String scorePrintout(HighscoreDatabase.ScoreValue scoreValue){
        List<String> highscorePrintoutScoreSubset = highscorePrintout.getScoreSubset(scoreValue);
        String printout = "";
        System.out.println("in HighscorePanel, scoreValue is: " + scoreValue);
        for (String s : highscorePrintoutScoreSubset){
            System.out.println("in scorePrintout, s is: " + s);
            printout = printout + "\n" + s;
        }
        return printout;
    }
    public void sortList(HighscoreDatabase.ScoreValue scoreValue){
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
                            changePlace = score1.isAfter(score2);
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
