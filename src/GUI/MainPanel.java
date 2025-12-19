package GUI;

import GUI.Game.Board;
import GameComponents.GameSession;
import Infrastructure.AppManager;
import Server.Database.HighscorePrintout;
import Server.Database.Highscores;
import Infrastructure.Subscriber;
import Server.Database.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainPanel extends JFrame{
    private static int rows = 4;
    private static int cols = 4;
    private static JPanel centerPanel;
    private static JPanel highscorePanel;
    private Board board;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private Color backgroundColor = Color.darkGray;
    private Color foregroundColor = Color.lightGray;
    private User user;
    private GameSession game;
    List<Subscriber> subscribers;
    private AppManager manager;
    Highscores.ScoreValue scoreValue = Highscores.ScoreValue.DATE;
    JButton backToMenu;

    public MainPanel (User user, AppManager manager) {
        System.out.println("mainPanel constructor is reached");
        this.manager = manager;
        this.user = user;
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

        showMainMenu();

        JTextArea points = new JTextArea();
        points.setVisible(true);
        points.setOpaque(false);
        topPanel = new JPanel();
        topPanel.add(points);
        topPanel.setBackground(backgroundColor);
        topPanel.setVisible(true);
        add(topPanel, BorderLayout.NORTH);

        repaint();
        revalidate();
        pack();
    }
    public void showMainMenu() {
        System.out.println("showMainMenu in MainPanel is reached");
        if (centerPanel != null) {
            centerPanel.removeAll();
        }

        JButton startGame = new JButton("Start new game");
        startGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manager.startGame(user);
            }
        });

        JButton seeHighscores = new JButton("Highscores");
        seeHighscores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("showHighscorePanel is called from main Panel");

                manager.update(Subscriber.EventType.REQUEST_ALL_HIGHSCORES, scoreValue);
            }
        });

        JButton savedGame = new JButton("SavedGame");
        savedGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        JPanel menuButtons = new JPanel(new GridLayout(3, 1));
        menuButtons.add(startGame);
        menuButtons.add(savedGame);
        menuButtons.add(seeHighscores);
        centerPanel.add(menuButtons, BorderLayout.CENTER);
        add(centerPanel);
        revalidate();
        repaint();
        pack();
    }

    public void showEndPanel(int points){
        centerPanel.removeAll();
        System.out.println("updateCenterPanel was reached");
        EndPanel endPanel = new EndPanel(manager, points);
        centerPanel.add(endPanel, BorderLayout.CENTER);
        addReturnButton();
    }

    private void addReturnButton(){
        if (backToMenu == null) {
            this.backToMenu = new JButton("Return to menu");
        }
        backToMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("from endPanel, return_add_menu_panel is called");
                manager.update(Subscriber.EventType.REQUEST_ADD_MENU_PANEL, null);
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setVisible(true);
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.add(backToMenu, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
        repaint();
        revalidate();
        pack();
    }

    public void showHighscorePanel (HighscorePrintout highscorePrintout){
        centerPanel.removeAll();
        this.highscorePanel = new HighscorePanel(highscorePrintout);
        if (highscorePrintout != null) {
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

    public void showGameBoard(List<List<Integer>> values){
            System.out.println("in mainPanel, showGameBoard is reached");
            centerPanel.removeAll();
            this.board = new Board(values, manager);
            System.out.println("in MainPanel, board is not null");
            centerPanel.add(board, BorderLayout.CENTER);
            board.setEnabled(true);
            board.setFocusable(true);
            repaint();
            revalidate();
            pack();
            SwingUtilities.invokeLater(board::requestFocusInWindow);
    }
    public void updateGameBoard(List<List<Integer>> values){
        board.updateTileBoard(values);
    }
    public void updatePoints(int points){
        System.out.println("updatePoints in MainPanel was reached");
        board.updateScoreDisplay(points);
    }
}

//
//
//        System.out.println("Game constructor was reached");
//
//
//
////        if (!newGame){
////            this.board = board;
////            centerPanel.add(board, BorderLayout.CENTER);
////        }
////
//
//
//
//
//
//    protected static void validateMovement(char c){
//        System.out.println("validateMovement was reached");
//        if (!checkIfCompleted()) {
//            boolean doneMoving = false;
//            switch (c) {
//                case 'w': {
//                    System.out.println("case W was reached");
//                    while (!doneMoving) {
//                        for (int i = 0; i < rows-1; i++) {
//                            for (Tile tile : board.getRow(i)) {
//                                Tile adjacent = board.getAdjacent(tile, Board.Direction.UP);
//                                if (adjacent != null) {
//                                    if (checkIfMergeable(tile, adjacent)) {
//                                        mergeTiles(tile, adjacent, Board.Direction.UP);
//                                        doneMoving = true;
//                                    } else if (tile.getValue() > 0 && tile.getRow() < rows && adjacent.getValue() == 0) {
//                                        tile.setRow(adjacent.getRow());
//                                        tile.adjustTile(tile);
//
//                                        adjacent = board.getAdjacent(tile, Board.Direction.UP);
//                                        if (tile.getRow() == 0 || (adjacent.getRow() == 0 && adjacent.getValue() > 0)) {
//                                            doneMoving = true;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    break;
//                }
//                case 'a': {
//                    while (!doneMoving) {
//                        for (int i = 1; i <= cols; i++){
//                            for (int j = 0; j < board.getColumn(i).size(); j++) {
//                                Tile tile = board.getColumn(i).get(j);
//                                Tile adjacent = board.getAdjacent(tile, Board.Direction.LEFT);
//                                if (adjacent != null) {
//                                    if (checkIfMergeable(tile, adjacent)) {
//                                        mergeTiles(tile, adjacent, Board.Direction.LEFT);
//                                        System.out.println("doneMoving is True (done merging)");
//                                        doneMoving = true;
//                                    }
//                                    else if (!checkIfMergeable(tile, adjacent) && tile.getValue() > 0 && tile.getCol() > 0 && adjacent.getValue() == 0) {
//                                       boolean keepMoving = true;
//                                        while (keepMoving) {
//                                            System.out.println("---move is possible. tileValue is: " + tile.getValue() + " positions are, tile and adjacent (col, row): " + tile.getCol() + " " + tile.getRow() + "|" + adjacent.getCol() + " " + adjacent.getRow());
//                                            Tile tempTile = new Tile(0, true, Color.lightGray, adjacent.getRow(),adjacent.getCol());
//                                            adjacent.setCol(tile.getCol());
//                                            adjacent.setRow(tile.getCol());
//                                            adjacent.adjustTile(adjacent);
//
//                                            tile.setCol(tempTile.getCol());
//                                            tile.setRow(tempTile.getRow());
//                                            tile.adjustTile(tempTile);
//
//                                            tile = adjacent;
//                                            if (tile.getCol() == 0 || (adjacent.getCol() == 0 && adjacent.getValue() > 0)) {
//                                                System.out.println("doneMoving is True (no possible moves left)");
//                                                keepMoving = false;
//                                            }
//                                        }
//                                    } else if (i == cols - 1 && !doneMoving) {
//                                        System.out.println("doneMoving is True (no possible move)");
//                                        doneMoving = true;
//
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    break;
//                }
//                case 's': {
//                    while (!doneMoving) {
//                        for (int i = rows; i > 1; i--) {
//                            for (Tile tile : board.getRow(i)) {
//                                Tile adjacent = board.getAdjacent(tile, Board.Direction.DOWN);
//                                if (adjacent != null) {
//                                    if (checkIfMergeable(tile, adjacent)) {
//                                        mergeTiles(tile, adjacent, Board.Direction.DOWN);
//                                        doneMoving = true;
//                                    } else if (tile.getValue() > 0 && tile.getRow() > 1 && adjacent.getValue() == 0) {
//                                        tile.setRow(adjacent.getRow());
//                                        adjacent = board.getAdjacent(tile, Board.Direction.DOWN);
//                                        if (tile.getRow() == 0 || (adjacent.getRow() == 0) && adjacent.getValue() > 0) {
//                                            doneMoving = true;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    break;
//                }
//                case 'd': {
//                    while (!doneMoving){
//                        for (int i = cols; i > 1; i--){
//                            for (Tile tile : board.getColumn(i)) {
//                                Tile adjacent = board.getAdjacent(tile, Board.Direction.RIGHT);
//                                if (adjacent != null) {
//                                    if (checkIfMergeable(tile, adjacent)) {
//                                        mergeTiles(tile, adjacent, Board.Direction.RIGHT);
//                                        doneMoving = true;
//                                    } else if (tile.getValue() > 0 && tile.getCol() < cols && adjacent.getValue() == 0) {
//                                        tile.setCol(adjacent.getCol());
//                                        adjacent = board.getAdjacent(tile, Board.Direction.RIGHT);
//                                        if (tile.getCol() == cols || (adjacent.getCol() == cols && adjacent.getValue() > 0)) {
//                                            doneMoving = true;
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    break;
//                }
//            }
//        }
//    }
//    private static boolean checkIfCompleted(){
//        return false;
//    }
//    private static boolean checkIfMergeable(Tile tile, Tile adjacent){
//        System.out.println("checkIfMergeable was reached. row and col is: " + tile.getCol() + " " + tile.getRow() + " vs " + adjacent.getCol() + " " + adjacent.getRow());
//        if (tile.getValue() > 0  && tile.getValue() == adjacent.getValue()){
//            System.out.println("checkIfMergeable is true");
//            return true;
//        }
//        else {
//            System.out.println("checkIfMergeable is false");
//            return false;
//        }
//    }
//    private static void mergeTiles(Tile tile, Tile adjacent, Board.Direction direction){
//        System.out.println("mergeTiles was reached");
//        tile.setValue(tile.value+adjacent.value);
//        switch (direction) {
//            case UP, DOWN: {
//                tile.setRow(adjacent.getRow());
//                tile.adjustTile(tile);
//                break;
//            }
//            case LEFT, RIGHT: {
//                tile.setCol(adjacent.getCol());
//                tile.adjustTile(tile);
//                break;
//            }
//        }
//    }
//
//}
