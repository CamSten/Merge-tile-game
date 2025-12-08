package GameComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Game extends JFrame {
    private Board board;
    private int rows;
    private int cols;
    private JPanel centerPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private Color backgroundColor = Color.darkGray;
    private Color foregroundColor = Color.lightGray;

    public Game (Board board, boolean newGame, int rows, int cols){
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);
        add(centerPanel, BorderLayout.CENTER);

        if (!newGame){
            this.board = board;
            centerPanel.add(board, BorderLayout.CENTER);

        }
        else {
            this.rows = rows;
            this.cols = cols;
            board = new Board(rows, cols);
            centerPanel.add(board, BorderLayout.CENTER);
        }

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                List<Character> actionCharacters = new ArrayList<>();
                actionCharacters.add('w');
                actionCharacters.add('a');
                actionCharacters.add('s');
                actionCharacters.add('d');
                for (char c : actionCharacters){
                    if(e.getKeyChar() == c ) {
                        validateMovement(c);
                    }
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        JTextArea points = new JTextArea();
        points.setVisible(true);
        points.setOpaque(false);
        topPanel = new JPanel();
        topPanel.add(points);
        topPanel.setBackground(backgroundColor);
        topPanel.setVisible(true);
        add(topPanel, BorderLayout.NORTH);

        JButton quitButton = new JButton("Quit game");
        quitButton.setForeground(foregroundColor);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showOptionDialog(null, "Quit game", "Are you sure you want to exit the game?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "No"}, "No");
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setVisible(true);
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.add(quitButton, BorderLayout.EAST);
        repaint();
        revalidate();
        pack();
    }

    private void validateMovement(char c){

    }
}
