package GameComponents;
import GameComponents.Moves.*;
import Infrastructure.Mediator;
import Infrastructure.Subscriber;
import Server.Database.User;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameSession implements Subscriber {
    Mediator mediator;
    boolean tileAdded = false;
    private User user;
    private MoveStrategy strategy;
    boolean win = false;
    boolean continueAfterWin = false;
    int totalPoints;
    private List<Integer> allTileValues = new ArrayList<>();
    private List<List<Integer>> allTileValueSubsets= new ArrayList<>();
    private List<List<Integer>> allAdjustedValues = new ArrayList<>();
    int rows = 4;
    int cols = 4;
    MoveStrategy moveStrategy = null;
    private boolean hasReached2048;
    private boolean victory = false;
    private int highscore;

    public GameSession(User user, int highscore, Mediator mediator){
        System.out.println("GAME SESSION CONSTRUCTOR WAS REACHED");
        this.mediator = mediator;
        this.user = user;
        this.highscore = highscore;
    }
    public void start(){
        System.out.println("start in GameSession is reached");
        totalPoints = 0;
        allTileValues.clear();
        allTileValueSubsets.clear();
        allAdjustedValues.clear();
        getStartingTiles();
    }

    public void assessKeyAction(char c){
        System.out.println("assessKeyAction in GameSession is reached. c is: " + c);
        tileAdded = false;
        this.moveStrategy = getMoveStrategy(c);
        if (moveStrategy != null) {
            moveStrategy.move(allTileValueSubsets, mediator);
        }
    }

    private void handleMove(MoveResult result) {
        System.out.println("In GameSession, checkUpdatedTileValues is reached");
        this.allAdjustedValues = result.getNewValues();
        this.allTileValueSubsets = allAdjustedValues;
        setTotalPoints(result.getPoints());
        mediator.update(EventType.RETURN_DISPLAY_SCORE, totalPoints);
        if (result.hasValuesChanged()) {
            if (completed(allAdjustedValues)) {
                gameOverActions();
            } else {
                System.out.println("IN GAME SESSION, addTile is called");
                addTile(allAdjustedValues);
                if (completed(allAdjustedValues)){
                    System.out.println("in GAMESESSION, full is true");
                    gameOverActions();
                }
                else {
                    updateTiles();
                }
            }
        }
    }
    private void updateTiles() {
        this.allTileValues = new ArrayList<>();
        for (List<Integer> list : allAdjustedValues) {
            allTileValues.addAll(list);
        }
        EventType eventType = EventType.RETURN_UPDATE_TILES;
        if (check2048()) {
            if (!victory) {
                eventType = EventType.REQUEST_CONTINUE_GAME;
                victory = true;
            }
        }
        mediator.update(eventType, allTileValues);
    }
    public void restoreFromGame(Game game){
        this.allTileValues = game.getAllValues();
        this.totalPoints = game.getPoints();
        getRestoredTiles(allTileValues);
        mediator.update(EventType.RETURN_ADD_GAME_PANEL, allTileValues);
        mediator.update(EventType.RETURN_DISPLAY_SCORE, totalPoints);
    }

    @Override
    public void update(EventType e, Object o) {
        System.out.println("update in GameSession is reached. Eventtype is: " + e);
        if (e != null) {
            switch (e) {
                case RETURN_NEW_SCORE:{
                    int p = (Integer) o;
                    System.out.println("return_Display_score in GameSession is reached. score is: " + p);
                    setTotalPoints(p);
                }
                case RETURN_HIGHEST_SCORE: {
                    this.highscore = (Integer) o;
                }
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
                case RETURN_UPDATE_VALUES: {
                    if (o instanceof MoveResult result) {
                        handleMove(result);
                    }
                }
                case RETURN_NEW_UNCHECKED_VALUES: {
                    if (o instanceof MoveResult result) {
                        handleMove(result);
                    }
                    break;
                }
                case RETURN_CONTINUE_GAME_TRUE: {
                    this.continueAfterWin = true;
                    break;
                }
                case RETURN_CONTINUE_GAME_FALSE: {
                    saveScore();
                    break;
                }
                case REQUEST_SAVE_GAME_INITIATE:{
                    System.out.println("in RequestSaveGame, totalPoints is: " + totalPoints);
                    System.out.println("in GameSession case save_game_initiate is reached.");
                    if (o != null){
                        System.out.println("data is: " + o.getClass());
                    }
                    if (o instanceof User u) {
                        if (user.getUsername() == u.getUsername()) {
                            Game newgame = new Game(user, allTileValues, totalPoints);
                            mediator.update(EventType.REQUEST_SAVE_GAME_EXECUTE, newgame);
                        }
                    }
                    break;
                }
                case RETURN_NEW_POINTS: {
                    int p = (Integer) o;
                    setTotalPoints(p);
                }
            }
        }
    }

    private MoveStrategy getMoveStrategy(char c) {
        return switch (c) {
            case 'a', 'A' -> new MoveLeftStrategy(this);
            case 'd', 'D' -> new MoveRightStrategy(this);
            case 'w', 'W' -> new MoveUpStrategy(this);
            case 's', 'S' -> new MoveDownStrategy(this);
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
            if (emptyIndexes != null) {
                int[] randomIndex = emptyIndexes.get(random.nextInt(emptyIndexes.size()));
                int row = randomIndex[0];
                int col = randomIndex[1];
                int newValue = getStartingValue();
                allAdjustedValues.get(row).set(col, newValue);
                }
                tileAdded = true;
            }
    }

    private void setTotalPoints(int value){
        totalPoints = totalPoints + value;
        mediator.update(EventType.RETURN_NEW_SCORE, totalPoints);
        if (totalPoints > highscore){
            mediator.update(EventType.RETURN_NEW_HIGHSCORE, totalPoints);
        }
    }

    private void getRestoredTiles (List<Integer> tileValues){
        List<List<Integer>> values = new ArrayList<>();
        for (int i = 0; i < rows; i++){
            List<Integer> subset = new ArrayList<>();
            subset = tileValues.subList(i*rows, (rows*(i+1)));
            values.add(subset);
        }
        this.allTileValueSubsets = values;
    }
    private void getStartingTiles() {
        System.out.println("in GameSession, getStartingTiles was reached");
        this.allTileValueSubsets = new ArrayList<>();
        for (int i = 0; i < rows; i++){
            List<Integer> subset = new ArrayList<>();
            for (int j = 0; j < cols; j++){
                subset.add(0);
            }
            allTileValueSubsets.add(subset);
        }

        int startValueOne = getStartingValue();
        int startValueTwo = getStartingValue();
        List<Integer> valueSubsetOne = allTileValueSubsets.getFirst();
        valueSubsetOne.removeFirst();
        valueSubsetOne.add(startValueOne);
        Collections.shuffle(valueSubsetOne);
        List<Integer>valueSubsetTwo = allTileValueSubsets.getLast();
        valueSubsetTwo.removeFirst();
        valueSubsetTwo.add(startValueTwo);
        Collections.shuffle(valueSubsetTwo);
        this.allTileValues = new ArrayList<>();
        for (List<Integer> list : allTileValueSubsets){
            allTileValues.addAll(list);
        }
        mediator.update(EventType.RETURN_ADD_GAME_PANEL, allTileValues);
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
    private boolean check2048(){
        for (List<Integer> l : allAdjustedValues) {
            for (Integer i : l) {
                if (i == 2048) {
                    this.hasReached2048 = true;
                }
            }
        }
        return hasReached2048;
    }
    private void gameOverActions() {
        saveScore();
        if (check2048()){
            win = true;
        }
        if (continueAfterWin) {
            win = false;
        }
        Game game = new Game(user, allTileValues, totalPoints);
        mediator.update(EventType.REQUEST_ADD_END_PANEL, game);
        mediator.update(EventType.CONFIRM_FINISHED_SESSION, this);
    }
    private boolean hasPossibleMoves(List<List<Integer>>allAdjustedValues) {
        Move move = new Move(this, allAdjustedValues, mediator);
        return move.hasMergeableMoves();
    }

    private void setMoveStrategy(MoveStrategy strategy){
        this.strategy = strategy;
    }

    public int getRows(){
        return rows;
    }
    public int getCols(){
        return cols;
    }
    public List<List<Integer>> getAllTileValueSubsets(){
        return allTileValueSubsets;
    }

    private boolean completed(List<List<Integer>> allAdjustedTileValues){
        System.out.println("CHECK IF COMPLETE IN GAME SESSION IS REACHED");
        int emptyTile = 0;
        for(List<Integer> l : allAdjustedTileValues){
            for(int i : l) {
                if (i == 0) {
                    emptyTile += 1;
                }
            }
        }
        if (emptyTile <= 0) {
            if (hasPossibleMoves(allAdjustedTileValues)) {
                return false;
            }
            else {
                return true;
            }
        }
        return false;
    }
    protected void saveScore(){
        Score score = new Score(this);
        mediator.update(EventType.REQUEST_SAVE_SCORE, score);
    }
    public User getUser(){
        return user;
    }
    public void subscribe(){
        mediator.subscribe(this);
    }
    public int getTotalPoints(){
        return totalPoints;
    }
    public void setHighscore(int value){
        this.highscore = value;
    }
}