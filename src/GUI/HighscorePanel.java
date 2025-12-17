package GUI;

import GUI.Game.GameColors;
import GUI.Game.GameFont;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class HighscorePanel extends JPanel {
    private List<String>scores = new ArrayList<>();
    private enum scoreValue {NAME, MOVES, DATE }

    public HighscorePanel(List<String> scores) {
        this.scores = scores;
        JPanel scorePanel = new JPanel(new BorderLayout());
        scorePanel.setBackground(GameColors.defaultBackground());
        scorePanel.setOpaque(true);

        JLabel header = new JLabel("Highscores:");
        header.setFont(GameFont.headerFont());
        header.setPreferredSize(new Dimension(500, 20));

        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel scoreHeader = new JPanel(new GridLayout(1, 3));
        JTextArea namesArea = new JTextArea(scorePrintout(scoreValue.NAME));
        JTextArea movesArea = new JTextArea(scorePrintout(scoreValue.MOVES));
        JTextArea datesArea = new JTextArea(scorePrintout(scoreValue.DATE));
        namesArea.setBorder(new BasicBorders.FieldBorder(Color.lightGray, Color.DARK_GRAY, Color.pink, Color.MAGENTA));
        movesArea.setBorder(new BasicBorders.FieldBorder(Color.lightGray, Color.DARK_GRAY, Color.pink, Color.MAGENTA));
        ;
        datesArea.setBorder(new BasicBorders.FieldBorder(Color.lightGray, Color.DARK_GRAY, Color.pink, Color.MAGENTA));
        JButton showName = new JButton("Namn:");
        showName.setFont(GameFont.defaultFont());
        showName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                sortList(Highscores.scoreValue.NAME);
                namesArea.setText(scorePrintout(scoreValue.NAME));
                movesArea.setText(scorePrintout(scoreValue.MOVES));
                datesArea.setText(scorePrintout(scoreValue.DATE));
            }
        });
        JButton showMoves = new JButton("Antal drag:");
        showMoves.setFont(GameFont.defaultFont());
        showMoves.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                sortList(Highscores.scoreValue.MOVES);
                namesArea.setText(scorePrintout(scoreValue.NAME));
                movesArea.setText(scorePrintout(scoreValue.MOVES));
                datesArea.setText(scorePrintout(scoreValue.DATE));
            }
        });
        JButton showDate = new JButton("Datum:");
        showDate.setFont(GameFont.defaultFont());
        showDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                sortList(Highscores.scoreValue.DATE);
                namesArea.setText(scorePrintout(scoreValue.NAME));
                movesArea.setText(scorePrintout(scoreValue.MOVES));
                datesArea.setText(scorePrintout(scoreValue.DATE));
            }
        });

        scoreHeader.add(showName);
        scoreHeader.add(showMoves);
        scoreHeader.add(showDate);
        centerPanel.add(scoreHeader, BorderLayout.NORTH);

        JPanel showScores = new JPanel(new GridLayout(1, 3));
        showScores.setBackground(GameColors.defaultBackground());
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
    }

    private String scorePrintout(scoreValue scoreValue){
        String temp = "";
        System.out.println("in HighscorePanel, scoreValue is: " + scores.getFirst());
        for (String s : scores){

        }
        return temp;
    }
}
