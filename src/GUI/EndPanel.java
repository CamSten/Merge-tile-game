package GUI;

import Infrastructure.AppManager;

import javax.swing.*;
import java.awt.*;

public class EndPanel extends JPanel {
    private AppManager manager;
    private int points;
    private Color backgroundColor = Color.darkGray;


    public EndPanel(AppManager manager, int points){
        System.out.println("EndPanel constructor was reached, points are: " + points);
        this.manager = manager;
        this.points = points;
        setBackground(backgroundColor);
        setLayout(new BorderLayout());

        JLabel pointsHeader = new JLabel("Points:");
        pointsHeader.setBackground(backgroundColor);
        pointsHeader.setForeground(GUI.Game.GameColors.headerText());
        pointsHeader.setFont(GUI.Game.GameFont.headerFont());
        JTextArea displayPoints = new JTextArea(String.valueOf(points));
        displayPoints.setBackground(backgroundColor);
        displayPoints.setForeground(GUI.Game.GameColors.headerText());
        displayPoints.setFont(GUI.Game.GameFont.headerFont());

        JPanel pointsPanel = new JPanel(new GridLayout(1, 2));
        pointsPanel.add(pointsHeader);
        pointsPanel.add(displayPoints);
        pointsPanel.setBackground(backgroundColor);
        pointsPanel.setBorder(
                BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 4, true));
        add(pointsPanel, BorderLayout.CENTER);
        setVisible(true);
        add(endMessage(), BorderLayout.NORTH);
    }

    public JTextArea endMessage (){
        JTextArea endMessage = new JTextArea("Game over");
        endMessage.setFont(new Font("Arial", Font.BOLD, 30));
        endMessage.setForeground(GUI.Game.GameColors.headerText());
        endMessage.setOpaque(false);
        return endMessage;
    }
}
