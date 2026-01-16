package GUI;

import GUI.Game.GameFont;
import Infrastructure.AppManager;

import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel {
    private AppManager manager;
    private int points;
    private boolean highestScore;
    private Color backgroundColor = Color.darkGray;


    public EndPanel(AppManager manager, int points, boolean highestScore){
        this.manager = manager;
        this.points = points;
        this.highestScore = highestScore;
        setBackground(backgroundColor);
        setLayout(new BorderLayout());
        add(endMessage(), BorderLayout.NORTH);
        if(highestScore){
            System.out.println("in EndPanel, highest score is true");
            add(getHighscoreMessage(points), BorderLayout.CENTER);
        }
        else {
            System.out.println("in EndPanel, highest score is false ");
            add(getPointsPanel(), BorderLayout.CENTER);
        }
        setVisible(true);
    }

    private JPanel getHighscoreMessage(int highscore){
        JPanel highscoreDisplay = new JPanel();
        highscoreDisplay.setLayout(new BoxLayout(highscoreDisplay, BoxLayout.Y_AXIS));
        highscoreDisplay.setBackground(backgroundColor);

        JLabel showScore = new JLabel("New Highscore!");
        showScore.setFont(GameFont.topHeaderFont());
        showScore.setBackground(backgroundColor);
        showScore.setForeground(GUI.Game.GameColors.headerText());
        JLabel scoreDisplay = new JLabel(String.valueOf(highscore));
        scoreDisplay.setFont(GameFont.topHeaderFont());
        scoreDisplay.setBackground(backgroundColor);
        scoreDisplay.setForeground(GUI.Game.GameColors.headerText());

        highscoreDisplay.add(showScore);
        highscoreDisplay.add(scoreDisplay);
        return highscoreDisplay;
    }
    private JPanel getPointsPanel(){
        JLabel pointsHeader = new JLabel("Final score:");
        pointsHeader.setBackground(backgroundColor);
        pointsHeader.setForeground(GUI.Game.GameColors.headerText());
        pointsHeader.setFont(GUI.Game.GameFont.topHeaderFont());
        JLabel displayPoints = new JLabel(String.valueOf(points));
        displayPoints.setBackground(backgroundColor);
        displayPoints.setForeground(GUI.Game.GameColors.headerText());
        displayPoints.setFont(GUI.Game.GameFont.topHeaderFont());

        JPanel pointsPanel = new JPanel(new GridLayout(1, 2));
        pointsPanel.add(pointsHeader);
        pointsPanel.add(displayPoints);
        pointsPanel.setBackground(backgroundColor);
        pointsPanel.setBorder(
                BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 4, true));
        return pointsPanel;
    }

    public JTextArea endMessage (){
        JTextArea endMessage = new JTextArea("Game over");
        endMessage.setFont(new Font("Arial", Font.BOLD, 30));
        endMessage.setForeground(GUI.Game.GameColors.headerText());
        endMessage.setOpaque(false);
        return endMessage;
    }
}
