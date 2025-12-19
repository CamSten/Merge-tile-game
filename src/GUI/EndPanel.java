package GUI;

import Infrastructure.AppManager;
import Infrastructure.GameManager;
import Infrastructure.GameMediator;
import Infrastructure.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndPanel extends JPanel {
    private AppManager manager;
    private int points;

    public EndPanel(AppManager manager, int points){
        System.out.println("EndPanel constructor was reached");
        this.manager = manager;
        this.points = points;
        setLayout(new BorderLayout());
        JTextArea endMessage = new JTextArea("Game over");
        endMessage.setFont(new Font("Arial", Font.BOLD, 30));
        JButton newGame = new JButton("Start new game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.update(Subscriber.EventType.REQUEST_NEW_GAME, null);
            }
        });
        JButton backToMenu = new JButton("Return to main menu");

        JLabel pointsHeader = new JLabel("Points:");
        JTextArea displayPoints = new JTextArea(String.valueOf(points));
        JPanel pointsPanel = new JPanel(new GridLayout(1, 2));
        pointsPanel.add(pointsHeader);
        pointsPanel.add(displayPoints);
        JPanel buttonPanel = new JPanel(new BorderLayout());
        add(pointsPanel, BorderLayout.NORTH);
        buttonPanel.add(backToMenu, BorderLayout.EAST);
        buttonPanel.add(newGame, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
        add(endMessage, BorderLayout.CENTER);
    }

}
