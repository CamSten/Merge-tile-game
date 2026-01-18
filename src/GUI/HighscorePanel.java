package GUI;

import GUI.Game.GameFont;
import Server.Database.HighscoreDatabase;
import Server.Database.HighscoreEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HighscorePanel extends JPanel {
    List<HighscoreEntry> highscoreEntries;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy HH:mm");

    public HighscorePanel(List<HighscoreEntry> highscoreEntries) {
        this.highscoreEntries = highscoreEntries;
        if (highscoreEntries != null) {
            sortList(HighscoreDatabase.ScoreValue.POINTS);
            System.out.println("highscorePanel constructor is reached");
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
            scorePanel.setVisible(true);
            scorePanel.setBorder(
                    BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 4, true));
            add(scorePanel, BorderLayout.CENTER);
        }
    }

    private String scorePrintout(HighscoreDatabase.ScoreValue scoreValue){
        String printout = "";
        for (HighscoreEntry entry : highscoreEntries) {
            switch (scoreValue) {
                case NAME -> {
                    printout += entry.getName() + "\n";
                    break;
                }
                case POINTS -> {
                    printout += entry.getPoints() + "\n";
                    break;
                }
                case DATE -> {
                    printout += entry.getFormattedDate() + "\n";
                    break;

                }
            }
        }
        return printout;
    }
    public void sortList(HighscoreDatabase.ScoreValue scoreValue){
        switch (scoreValue){
            case NAME -> {
                highscoreEntries.sort(
                        Comparator.comparing(HighscoreEntry::getName).reversed()
                );
                break;
            }
            case POINTS -> {
                highscoreEntries.sort(
                        Comparator.comparingInt(HighscoreEntry::getPoints).reversed()
                );
                break;
            }
            case DATE -> {
                highscoreEntries.sort(
                        Comparator.comparing(HighscoreEntry::getDate).reversed()
                );
                break;
            }
        }
    }
}
