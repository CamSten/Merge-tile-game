package GUI;

import Infrastructure.Subscriber;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndPanel extends JPanel implements Subscriber {
    public EndPanel(){

        setLayout(new BorderLayout());
        JTextArea endMessage = new JTextArea("Game over");
        endMessage.setFont(new Font("Arial", Font.BOLD, 30));
        JButton newGame = new JButton("Start new game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update(EventType.REQUEST_NEW_GAME, null);
            }
        });
        JButton backToMenu = new JButton("Return to main menu");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update(EventType.ADD_MENU_PANEL, null);
            }
        });

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(backToMenu, BorderLayout.EAST);
        buttonPanel.add(newGame, BorderLayout.WEST);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
        add(endMessage, BorderLayout.CENTER);
    }

    @Override
    public void update(EventType e, Object data) {

    }
}
