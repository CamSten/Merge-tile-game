package GameComponents.Moves;
import GameComponents.GameSession;
import Infrastructure.Mediator;

import java.util.List;

public class MoveLeftStrategy implements MoveStrategy {
    private boolean reversed = false;
    private boolean horizontal = true;
    private GameSession game;
    private Move move;

    public MoveLeftStrategy(GameSession game) {
        System.out.println("move LEFT strategy was reached");
        this.game = game;
    }

    public void move(List<List<Integer>> allTileValues, Mediator mediator) {
        this.move = new Move(game, allTileValues, mediator);
        move.assessMovement(reversed, horizontal);
    }

}