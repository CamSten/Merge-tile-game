package GUI;

import GUI.Game.Board;
import GameComponents.GameSession;
import Infrastructure.AppManager;
import Infrastructure.Mediator;
import Infrastructure.Subscriber;
import Server.Database.HighscoreDatabase;
import Server.Database.HighscoreEntry;
import Server.Database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class MainFrame extends JFrame {
    private static int rows = 4;
    private static int cols = 4;
    private static JPanel centerPanel;
    private static JPanel highscorePanel;
    private Board board;
    private JPanel topPanel;
    private MenuPanel menuPanel;
    private JPanel bottomPanel;
    private Color backgroundColor = Color.darkGray;
    private Color foregroundColor = Color.lightGray;
    private User user;
    private GameSession game;
    java.util.List<Subscriber> subscribers;
    private AppManager manager;
    private Mediator mediator;
    HighscoreDatabase.ScoreValue scoreValue = HighscoreDatabase.ScoreValue.DATE;
    JButton backToMenu;

    public MainFrame(Mediator mediator, AppManager manager) {
        if (bottomPanel != null ){
            bottomPanel.removeAll();
        }
        System.out.println("mainPanel constructor is reached");
        this.manager = manager;
        this.mediator = mediator;
        this.rows = rows;
        this.cols = cols;

        setVisible(true);
        setLayout(new BorderLayout());
        setEnabled(true);
        setMinimumSize(new Dimension(500, 600));
        setBackground(backgroundColor);
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);
        add(centerPanel, BorderLayout.CENTER);
        showLoginPanel();
        JTextArea points = new JTextArea();
        points.setVisible(true);
        points.setOpaque(false);
        topPanel = new JPanel();
        topPanel.add(points);
        topPanel.setBackground(backgroundColor);
        topPanel.setVisible(true);
        add(topPanel, BorderLayout.NORTH);
        bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(backgroundColor);
        add(bottomPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                manager.assessQuit(0);
                dispose();
                System.exit(0);
            }
        });
        repaint();
        revalidate();
        pack();
    }
    private void showLoginPanel(){
        centerPanel.removeAll();
        LoginPanel loginPanel = new LoginPanel(manager);
        centerPanel.add(loginPanel, BorderLayout.CENTER);
        repaint();
        revalidate();
        pack();
    }
    public void showMenuPanel(User user, boolean hasSavedGame){
        System.out.println("showMenuPanel in MainFrame is reached, hasSavedGame is: " + hasSavedGame);
        if (bottomPanel != null){
            bottomPanel.removeAll();
        }
        centerPanel.removeAll();
        this.menuPanel = new MenuPanel(user, manager, hasSavedGame);
        centerPanel.add(menuPanel);
        repaint();
        revalidate();
        pack();
    }

    public void showEndPanel(int points, boolean highestScore) {
        centerPanel.removeAll();
        System.out.println("updateCenterPanel was reached");
        EndPanel endPanel = new EndPanel(manager, points, highestScore);
        centerPanel.add(endPanel, BorderLayout.CENTER);
        JButton newGame = new JButton("Start new game");
        newGame.setBackground(backgroundColor);
        newGame.setForeground(GUI.Game.GameColors.headerText());
        newGame.setFont(GUI.Game.GameFont.headerFont());
        newGame.setBorder(
                BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 4, true));
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.update(Subscriber.EventType.REQUEST_NEW_GAME, user);
            }
        });

        bottomPanel.add(newGame, BorderLayout.WEST);
        addReturnButton();
        repaint();
        revalidate();
        pack();
    }
    private void addReturnButton() {
        bottomPanel.removeAll();
        if (backToMenu == null) {
            this.backToMenu = new JButton("Return to menu");
            backToMenu.setBackground(backgroundColor);
            backToMenu.setForeground(GUI.Game.GameColors.headerText());
            backToMenu.setFont(GUI.Game.GameFont.headerFont());
            backToMenu.setBorder(
                    BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 4, true));
            backToMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("from endPanel, return_add_menu_panel is called");
                    manager.update(Subscriber.EventType.REQUEST_ADD_MENU_PANEL, null);
                }
            });
        }
        bottomPanel.add(backToMenu, BorderLayout.WEST);
        bottomPanel.setVisible(true);
        bottomPanel.setBackground(backgroundColor);

        repaint();
        revalidate();
        pack();
    }

    public void showHighscorePanel(List<HighscoreEntry> entries) {
        centerPanel.removeAll();
        this.highscorePanel = new HighscorePanel(entries);
        if (entries != null) {
            addReturnButton();
            centerPanel.add(highscorePanel, BorderLayout.CENTER);
            highscorePanel.setFocusable(true);
            highscorePanel.setVisible(true);
            highscorePanel.setEnabled(true);
            repaint();
            revalidate();
            pack();
        }
    }
    public void showGameBoard(List<Integer> values, int highscore) {
        System.out.println("in mainPanel, showGameBoard is reached");
        centerPanel.removeAll();
        repaint();
        this.board = new Board(values, highscore, mediator, this);

        if (bottomPanel != null){
            bottomPanel.removeAll();
        }
        addReturnButton();
        JButton quitGame = new JButton("Quit game");
        quitGame.setBackground(backgroundColor);
        quitGame.setForeground(GUI.Game.GameColors.headerText());
        quitGame.setFont(GUI.Game.GameFont.headerFont());
        quitGame.setBorder(
                BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 4, true));
        bottomPanel.add(quitGame, BorderLayout.EAST);
        quitGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showOptionDialog(null, "Are you sure you want to quit playing?", "Quit game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "No"}, "No");
                manager.assessQuit(choice);
            }
        });
        centerPanel.add(board, BorderLayout.CENTER);
        board.setEnabled(true);
        SwingUtilities.invokeLater(() -> {
            board.setFocusable(true);
            board.requestFocusInWindow();
        });
        repaint();
        revalidate();
        pack();
    }
    public void update(Subscriber.EventType eventType, Object data){
        mediator.update(eventType, data);
    }
    public void updateBoard(List<Integer> values){
        board.updateTileBoard(values);
    }
    public void updateScoreDisplay(int value){
        board.updateScoreDisplay(value);
    }
    public void updateHighscoreDisplay(int value) {
        if (board != null) {
            board.updateHighscoreDisplay(value);
        }
    }
    public void showWin(){
        int choice = JOptionPane.showOptionDialog(null, "Congratulations, you've reached 2048! Do you want to continue playing?", "Keep playing", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "No"}, "Yes");
        manager.assessContinue(choice);
    }
}

