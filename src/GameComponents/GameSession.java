package GameComponents;
import GUI.Game.Board;
import GUI.MainPanel;
import GUI.Game.Tile;
import GameComponents.Moves.*;
import Infrastructure.GameMediator;
import Infrastructure.Subscriber;
import Server.Database.Highscores;
import Server.Database.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameSession implements Subscriber {
    private User user;
    private Board board;
    private MoveStrategy strategy;
    boolean win = false;
    boolean continueAfterWin = false;
    private int points;
    List<Tile> tiles = new ArrayList<>();
    List<List<Integer>> allTileValues = new ArrayList<>();
    List<Subscriber> subscribers;
    int rows = 4;
    int cols = 4;
    MoveStrategy moveStrategy = null;

    public GameSession(User user){
        this.user = user;
        board = new Board();
        subscribe(board);
        board.subscribe(this);
        getStartingTiles();
        GameMediator mediator = GameMediator.getInstance();
        mediator.update(EventType.ADD_GAME_PANEL, board);
        mediator.update(EventType.UPDATE_TILES, allTileValues);
    }
    private void start(){
        System.out.println("GameSession start was reached");


        System.out.println("getStartingTiles was reached");
    }

    public void assessKeyAction(char c){
        this.moveStrategy = getMoveStrategy(c);
        if (moveStrategy != null) {
            moveStrategy.move(allTileValues);
        }
    }

    public void checkUpdatedTileValues(MoveResult result) {
        System.out.println("In GameSession, checkUpdatedTileValues is reached");
        List<List<Integer>> allAdjustedValues = result.getNewValues();
        if (result.hasValuesChanged()) {
            updateTiles(allAdjustedValues);
            if ((result.hasReached2048() && !result.doContinueAfterWin()) || result.isFull()) {
                gameOverActions(result.hasReached2048());
            } else {
                System.out.println("IN BOARD, addTile is called");
                addTile();
                if (checkIfFull()){
                    System.out.println("in GAMESESSION, full is true");
                    gameOverActions(result.hasReached2048());
                }
            }
        }
        else {
            System.out.println("in Board checkUpdatedTileValues, values haven't changed");
        }
    }
    public void updateTiles(List<List<Integer>> allAdjustedValues) {
        allTileValues = allAdjustedValues;
        notifySubscribers(EventType.UPDATE_TILES, allAdjustedValues);
    }

    @Override
    public void update(EventType e, Object o) {
        System.out.println("update in GameSession is reached. Eventtype is: " + e);
        if (e != null){
            switch (e){
                case REQUEST_NEW_GAME: {
                    start();
                }
                case KEY_ACTION: {
                    if (o instanceof Character c) {
                        assessKeyAction(c);
                    }
                }
                case UPDATE_VALUES: {
                    if(o instanceof MoveResult result){
                        checkUpdatedTileValues(result);
                    }
                }
                case UPDATE_TILES: {

                }
            }
        }
    }
    public void subscribe(Subscriber s){
        System.out.println("subscription request from: " + s.getClass());
        if(subscribers== null)
        {
            this.subscribers = new ArrayList<>();
        }
        subscribers.add(s);
        System.out.println("Subscriber added: " + s.getClass());
    }
    public void unSubscribe(Subscriber s){
        subscribers.remove(s);
    }

    private void notifySubscribers(EventType e, Object o){
        System.out.println("subscribers notified from GameSession: " + e);
        for (Subscriber s : subscribers) {
            System.out.println(s.getClass());
            s.update(e, o);
        }
    }
    private MoveStrategy getMoveStrategy(char c) {
        return switch (c) {
            case 'a' -> new MoveLeftStrategy(this);
            case 'd' -> new MoveRightStrategy(this);
            case 'w' -> new MoveUpStrategy(this);
            case 's' -> new MoveDownStrategy(this);
            default -> null;
        };
    }
    private void addTile() {
        Random random = new Random();
        List<int[]> indexes = new ArrayList<>();
        for (List<Integer> l : allTileValues) {
            for (int i : l) {
                int[] index = new int[2];
                if (i == 0) {
                    index[0] = allTileValues.indexOf(l);
                    index[1] = l.indexOf(i);
                    indexes.add(index);
                }
            }
        }
            System.out.println("indexes.size is: " + indexes.size());
            if (indexes != null){

            random.nextInt(indexes.size());
                int[]randomIndex = indexes.get(random.nextInt(indexes.size()));
                int valueSublist = randomIndex[0];
                int sublistIndex = randomIndex[1];
                int newValue = getStartingValue();
                int index = allTileValues.get(valueSublist).get(sublistIndex);
                allTileValues.get(valueSublist).set(sublistIndex, newValue);
                System.out.println("in addTile, newvalue is: " + newValue);
                notifySubscribers(EventType.UPDATE_TILES, allTileValues);
            }
    }
    private void getStartingTiles() {
        System.out.println("in GameSession, getStartingTiles was reached");
        setCoordinates();
        List<List<Integer>> allTileValues = new ArrayList<>();
        for (int i = 0; i < rows; i++){
            List<Integer> subset = new ArrayList<>();
            for (int j = 0; j < cols; j++){
                subset.add(0);
            }
            allTileValues.add(subset);
        }
        this.allTileValues = allTileValues;
        int startValueOne = getStartingValue();
        int startValueTwo = getStartingValue();
        List<Integer> valueSubsetOne = allTileValues.getFirst();
        valueSubsetOne.removeFirst();
        valueSubsetOne.add(startValueOne);
        Collections.shuffle(valueSubsetOne);
        List<Integer>valueSubsetTwo = allTileValues.getLast();
        valueSubsetTwo.removeFirst();
        valueSubsetTwo.add(startValueTwo);
        Collections.shuffle(valueSubsetTwo);

        update(EventType.UPDATE_TILES, allTileValues);
    }

    private void setCoordinates() {
        for (Tile t : tiles) {
            int index = tiles.indexOf(t);
            t.setRow(index / rows);
            t.setCol(index % cols);
        }
    }

    private int getStartingValue() {
        Random random = new Random();
        List<Integer> values = new ArrayList<>();
        values.add(2);
        values.add(4);
        int randomValue = random.nextInt(2);
        if (randomValue == 0) {
            return values.getFirst();
        } else {
            return values.getLast();
        }
    }
    private void gameOverActions(boolean hasReached2048) {
        System.out.println("in Board, score is: " + points);
        saveScore(points);
        if (hasReached2048){
            win = true;
        }
        if (continueAfterWin) {
            win = false;
        }
        notifySubscribers(EventType.ADD_END_PANEL, points);
    }

    protected void updateContinueGame(Subscriber.EventType e){
        if (e == Subscriber.EventType.CONTINUE_GAME){
            continueAfterWin = true;
        }
    }
    private void setMoveStrategy(MoveStrategy strategy){
        this.strategy = strategy;
    }
    public List<Tile> getTiles(){
        return tiles;
    }
    public int getRows(){
        return rows;
    }
    public int getCols(){
        return cols;
    }
    public void setScore(int points){
        System.out.println("in setScore, score is: " + points);
        this.points = points;
    }
    private boolean checkIfFull(){
        int emptyTile = 0;
        for(List<Integer> l : allTileValues){
            for(int i : l) {
                if (i == 0) {
                    emptyTile += 1;
                }
            }
        }
        return emptyTile <= 0;
    }
    protected void saveScore(int score){
        System.out.println("In Game, score is: " + score);
        Highscores.saveScore(user, score);
    }

    public Board  getBoard() {
        return board;
    }
}