package GameComponents;
import GUI.Game.Board;
import GUI.Game.Tile;
import GameComponents.Moves.*;
import Infrastructure.GameMediator;
import Infrastructure.Subscriber;
import Server.Database.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameSession implements Subscriber {
    GameMediator mediator = GameMediator.getInstance();
    boolean tileAdded = false;
    private User user;
    private Board board;
    private MoveStrategy strategy;
    boolean win = false;
    boolean continueAfterWin = false;
    int totalPoints = 0;
    private int points;
    List<Tile> tiles = new ArrayList<>();
    List<List<Integer>> allTileValues = new ArrayList<>();
    int rows = 4;
    int cols = 4;
    MoveStrategy moveStrategy = null;

    public GameSession(User user){
        mediator.subscribe(this);
        this.user = user;
    }
    private void start(){
    getStartingTiles();
        mediator.update(EventType.RETURN_ADD_GAME_PANEL, allTileValues);
    }

    public void assessKeyAction(char c){
        System.out.println("assessKeyAction in GameSession is reached. c is: " + c);
        tileAdded = false;
        this.moveStrategy = getMoveStrategy(c);
        if (moveStrategy != null) {
            moveStrategy.move(allTileValues);
        }
    }

    public void checkUpdatedTileValues(MoveResult result) {
        setTotalPoints(result.getPoints());
        System.out.println("In GameSession, checkUpdatedTileValues is reached");
        List<List<Integer>> allAdjustedValues = result.getNewValues();
        if (result.hasValuesChanged()) {
            if ((result.hasReached2048() && !result.doContinueAfterWin()) || result.isFull()) {
                gameOverActions(result.hasReached2048());
            } else {
                System.out.println("IN GAME SESSION, addTile is called");
                addTile(allAdjustedValues);
                if (checkIfFull()){
                    System.out.println("in GAMESESSION, full is true");
                    gameOverActions(result.hasReached2048());
                }
                else {
                    updateTiles(allAdjustedValues);
                }
            }
        }
        else {
            System.out.println("in Board checkUpdatedTileValues, values haven't changed");
        }
    }
    public void updateTiles(List<List<Integer>> allAdjustedValues) {
        allTileValues = allAdjustedValues;
        mediator.update(EventType.RETURN_NEW_SCORE, allAdjustedValues);
        mediator.update(EventType.RETURN_UPDATE_TILES, allAdjustedValues);
    }

    @Override
    public void update(EventType e, Object o) {
        System.out.println("update in GameSession is reached. Eventtype is: " + e);
        if (e != null) {
            switch (e) {

                case REQUEST_NEW_GAME: {
                    start();
                    break;
                }
                case REQUEST_KEY_ACTION: {
                    if (o instanceof Character c) {
                        assessKeyAction(c);
                    }
                    break;
                }
//                case RETURN_UPDATE_VALUES: {
//                    if (o instanceof MoveResult result) {
//                        checkUpdatedTileValues(result);
//                    }
//                }
                case NEW_UNCHECKED_VALUES: {
                    if (o instanceof MoveResult result) {
                        checkUpdatedTileValues(result);
                    }
                    break;
                }
            }
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
    private void addTile(List<List<Integer>> allAdjustedValues) {
        if (tileAdded == false) {
            Random random = new Random();
            List<int[]> emptyIndexes = new ArrayList<>();
            for (int row = 0; row < allAdjustedValues.size(); row++) {
                List<Integer> currentRow = allAdjustedValues.get(row);
                for (int col = 0; col < currentRow.size(); col++) {
                    if (currentRow.get(col) == 0) {
                        emptyIndexes.add(new int[]{row, col});
                    }
                }
            }
            System.out.println("indexes.size is: " + emptyIndexes.size());
            if (emptyIndexes != null) {
                int[] randomIndex = emptyIndexes.get(random.nextInt(emptyIndexes.size()));
                int row = randomIndex[0];
                int col = randomIndex[1];
                int newValue = getStartingValue();
                allAdjustedValues.get(row).set(col, newValue);
                System.out.println("in addTile, newvalue is: " + newValue);
                tileAdded = true;
            }
        }
    }

    private void setTotalPoints(int value){
        this.totalPoints = totalPoints + value;
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
        System.out.println("gameOverActions in GameSession is reached");
        System.out.println("in Board, score is: " + totalPoints);
        saveScore();
        if (hasReached2048){
            win = true;
        }
        if (continueAfterWin) {
            win = false;
        }
        mediator.update(EventType.RETURN_ADD_END_PANEL, totalPoints);
    }

    protected void updateContinueGame(Subscriber.EventType e){
        if (e == EventType.REQUEST_CONTINUE_GAME){
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
    protected void saveScore(){
        Score score = new Score(this);
        System.out.println("in GameSession saveScore, score is: " + score.getTotalScore());
        mediator.update(EventType.REQUEST_SAVE_SCORE, score);
    }
    public User getUser(){
        return user;
    }
    int getTotalPoints(){
        return totalPoints;
    }
}