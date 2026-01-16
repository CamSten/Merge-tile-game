package GameComponents.Moves;

import Infrastructure.Mediator;

import java.util.List;

public interface MoveStrategy {
    public void move(List<List<Integer>> allTileValues, Mediator mediator);


}
