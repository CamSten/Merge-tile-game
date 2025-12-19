package GameComponents.Moves;
import GameComponents.GameSession;
import Infrastructure.GameMediator;
import Infrastructure.Subscriber;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Move {
    private GameMediator mediator = GameMediator.getInstance();
    private GameSession game;
    private List<List<Integer>> allBoardSubsets;
    private List<Integer> adjustedValues;
    int rows;
    int cols;
    private int points;

    public Move(GameSession game, List<List<Integer>> allBoardSubsets) {
        this.game = game;
        this.allBoardSubsets = allBoardSubsets;
        this.rows = game.getRows();
        this.cols = game.getCols();
    }

    public void assessMovement(boolean reversed, boolean horizontal) {
        List<List<Integer>> allSubsetValues = getAllBoardSubsets(horizontal);
        List<List<Integer>> allAdjustedValues = new ArrayList<>();
        for (List<Integer> l : allSubsetValues) {
            List<Integer> line = new ArrayList<>(l);
            if (reversed) {
                Collections.reverse(line);
            }
            List<Integer> adjustedValues = getAdjustedValues(line);
            List<Integer> mergedValues = new ArrayList<>(adjustedValues);
            if (reversed) {
                Collections.reverse(mergedValues);
            }
            allAdjustedValues.add(mergedValues);
        }
        boolean changedValues = hasValuesChanged(allSubsetValues, allAdjustedValues);
        boolean completed = isCompleted();
        boolean hasReached2048 = checkFor2048(horizontal);
        boolean continueAfter2048 = false;
        if(!horizontal){
            allAdjustedValues = getCorrectOrder(allAdjustedValues);
        }
        MoveResult moveResult = new MoveResult(allAdjustedValues, changedValues, completed, hasReached2048, continueAfter2048, points);
        mediator.update(Subscriber.EventType.NEW_UNCHECKED_VALUES, moveResult);
    }
    private List<Integer> getAdjustedValues(List<Integer> subset) {
        List<Integer> adjustedValues = new ArrayList<>();
        List<Integer> nonZeroValues = new ArrayList<>();
        for (int i = 0; i < subset.size(); i++) {
            if (subset.get(i) != 0) {
                nonZeroValues.add(subset.get(i));
            }
        }
        if (nonZeroValues.size() < 2) {
            adjustedValues.addAll(nonZeroValues);
        } else {
            for (int i = 0; i < nonZeroValues.size(); i++) {
                if (i < nonZeroValues.size() - 1 && nonZeroValues.get(i).equals(nonZeroValues.get(i + 1))) {
                    adjustedValues.add((nonZeroValues.get(i)) * 2);
                    setPoints(nonZeroValues.get(i) * 2);
                    i++;
                } else {
                    adjustedValues.add(nonZeroValues.get(i));
                }
            }
            mediator.update(Subscriber.EventType.RETURN_NEW_SCORE, points);
            System.out.println("in Move, points is: " + points);
        }
        while (adjustedValues.size() < subset.size()) {
            adjustedValues.add(0);
        }
        this.adjustedValues = adjustedValues;
        return adjustedValues;
    }
    private List<List<Integer>> getCorrectOrder(List<List<Integer>> adjustedValues){
        List<List<Integer>> valuesInOrder = new ArrayList<>();
        for(int i = 0; i < rows; i++){
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                row.add(adjustedValues.get(j).get(i));
            }
            valuesInOrder.add(row);
        }
        return valuesInOrder;
    }

    private List<List<Integer>> getAllBoardSubsets(boolean horizontal) {
        int count = rows;
        if (horizontal) {
            count = cols;
        }
        List<List<Integer>> tileSubsets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<Integer> subset = getSubset(i, horizontal);
            tileSubsets.add(subset);
        }
        return tileSubsets;
    }

    private static boolean hasValuesChanged(List<List<Integer>> allSubsetValues, List<List<Integer>> allAdjustedValues) {
        for (int i = 0; i < allSubsetValues.size(); i++){
            List<Integer> valueSubset = allSubsetValues.get(i);
            List<Integer> adjustedValueSubset = allAdjustedValues.get(i);
            for (int j = 0; j < valueSubset.size(); j++){
                if (!(valueSubset.get(j).equals(adjustedValueSubset.get(j)))){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkFor2048(boolean horizontal) {
        boolean hasReached2048 = false;
        List<List<Integer>> allTileValues = getAllBoardSubsets(horizontal);
        for (List<Integer> l : allTileValues) {
            for (Integer i : l) {
                if (i == 2048) {
                    hasReached2048 = true;
                }
            }
        }
        return hasReached2048;
    }

    public List<Integer> getSubset(int index, boolean horizontal) {
        List<Integer> values = new ArrayList<>();
        for (List<Integer> l : allBoardSubsets) {
            values.addAll(l);
        }
        List<Integer> subset = new ArrayList<>();

        if (horizontal) {
            int start = index * cols;
            return new ArrayList<>(values.subList(start, start + cols));
        } else {
            for (int i = 0; i < rows; i++) {
                subset.add(values.get(i * cols + index));
            }
            return subset;
        }
    }

    private boolean isCompleted(){
        if (isFull()) {
            System.out.println("----------------------in Move, full is true");
            for (int i = 0; i < adjustedValues.size(); i++) {
                if (i < adjustedValues.size() - 1 && adjustedValues.get(i).equals(adjustedValues.get(i + 1))) {
                    return true;
                }
            }
        }
        return false;
    }
    private boolean isFull() {
        System.out.println("______________ isFull in Move is reached");
        int numberOfValueTiles = 0;
        for (List<Integer> l : allBoardSubsets) {
            for (Integer i : l) {
                if (i > 0) {
                    numberOfValueTiles += 1;
                }
            }
        }
        return numberOfValueTiles == rows * cols;
    }
    private void setPoints(int value){
        this.points += value;
    }
}
