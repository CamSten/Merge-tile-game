package GUI;

import GUI.Game.Board;
import GameComponents.GameSession;
import Infrastructure.AppManager;
import Server.Database.Highscores;
import Infrastructure.Subscriber;
import Server.Database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MenuPanel extends JPanel{
    private static int rows = 4;
    private static int cols = 4;
    private static JPanel centerPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private Color backgroundColor = Color.darkGray;
    private User user;
    private AppManager manager;
    Highscores.ScoreValue scoreValue = Highscores.ScoreValue.DATE;

    public MenuPanel(User user, AppManager manager, boolean hasSavedGame) {
        if (bottomPanel != null ){
            bottomPanel.removeAll();
        }
        System.out.println("menuPanel constructor is reached");

        this.manager = manager;
        this.user = user;

        setVisible(true);
        setLayout(new BorderLayout());
        setEnabled(true);
        setMinimumSize(new Dimension(500, 600));
        setBackground(backgroundColor);
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);
        add(centerPanel, BorderLayout.CENTER);

        showMainMenu(hasSavedGame);
        bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        repaint();
        revalidate();
    }
    public void showMainMenu(boolean hasSavedGame) {
        topPanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getUsername());
        welcomeLabel.setFont(GUI.Game.GameFont.topHeaderFont());
        welcomeLabel.setForeground(GUI.Game.GameColors.headerText());
//        topPanel.add(points);
        topPanel.add(welcomeLabel);
        topPanel.setBackground(backgroundColor);
        topPanel.setVisible(true);
        add(topPanel, BorderLayout.NORTH);
        System.out.println("showMainMenu in MenuPanel is reached");
        if (centerPanel != null) {
            centerPanel.removeAll();
        }
        JPanel menuButtons = new JPanel(new GridLayout(3, 1));
        menuButtons.setBackground(backgroundColor);

        JButton startGame = new JButton("Start new game");
        startGame.setBackground(backgroundColor);
        startGame.setFont(GUI.Game.GameFont.topHeaderFont());
        startGame.setForeground(GUI.Game.GameColors.headerText());
        startGame.setBorder(
                BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 4, true));
        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.requestGameValues();
            }
        });
        menuButtons.add(startGame);

        if (hasSavedGame) {
            JButton savedGame = new JButton("Continue game");
            savedGame.setBackground(backgroundColor);
            savedGame.setFont(GUI.Game.GameFont.topHeaderFont());
            savedGame.setForeground(GUI.Game.GameColors.headerText());
            savedGame.setBorder(
                    BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 4, true));
            savedGame.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    manager.requestSavedGame(user);
                }
            });
            menuButtons.add(savedGame);
        }

        JButton seeHighscores = new JButton("See highscores");
        seeHighscores.setBackground(backgroundColor);
        seeHighscores.setFont(GUI.Game.GameFont.topHeaderFont());
        seeHighscores.setForeground(GUI.Game.GameColors.headerText());
        seeHighscores.setBorder(
                BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 4, true));
        seeHighscores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("showHighscorePanel is called from main Panel");
                manager.update(Subscriber.EventType.REQUEST_ALL_HIGHSCORES, scoreValue);
            }
        });
        menuButtons.add(seeHighscores);
        centerPanel.add(menuButtons, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
