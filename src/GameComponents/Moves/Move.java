package GameComponents.Moves;
import GameComponents.GameSession;
import Infrastructure.Mediator;
import Infrastructure.Subscriber;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Move {
    private Mediator mediator;
    private GameSession gameSession;
    private List<List<Integer>> allBoardSubsets;
    private List<Integer> adjustedValues;
    private boolean reversed;
    private boolean horizontal;
    int rows;
    int cols;
    private int points;

    public Move(GameSession game, List<List<Integer>> allBoardSubsets, Mediator mediator) {
        this.mediator = mediator;
        this.gameSession = game;
        this.allBoardSubsets = allBoardSubsets;
        this.rows = game.getRows();
        this.cols = game.getCols();
    }
    public void assessMovement(boolean reversed, boolean horizontal) {
        this.points = 0;
        this.reversed = reversed;
        this.horizontal = horizontal;
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
        if(!horizontal){
            allAdjustedValues = getCorrectOrder(allAdjustedValues);
        }
        MoveResult moveResult = new MoveResult(allAdjustedValues, changedValues, points);
        mediator.update(Subscriber.EventType.RETURN_NEW_UNCHECKED_VALUES, moveResult);
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
            mediator.update(Subscriber.EventType.RETURN_NEW_POINTS, points);
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
        int count;
        if (horizontal) {
            count = rows;
        } else {
            count = cols;
        }
        List<List<Integer>> tileSubsets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tileSubsets.add(getSubset(i, horizontal));
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
    private void setPoints(int value){
        points += value;
    }
}