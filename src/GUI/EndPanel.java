package GUI;

import GameComponents.Board;
import GameComponents.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndPanel extends JPanel {
    private Game game;
    public EndPanel(Game game){
        this.game = game;

        setLayout(new BorderLayout());
        JTextArea endMessage = new JTextArea("Game over");
        endMessage.setFont(new Font("Arial", Font.BOLD, 30));
        JButton newGame = new JButton("Start new game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.startNewGame();
            }
        });
        JButton backToMenu = new JButton("Return to main menu");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.backToMainMenu();
            }
        });

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(backToMenu, BorderLayout.EAST);
        buttonPanel.add(newGame, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
        add(endMessage, BorderLayout.CENTER);
    }
}
