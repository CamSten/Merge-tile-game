package GameComponents.Moves;
import GameComponents.GameSession;

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

    public void move(List<List<Integer>> allTileValues) {
        this.move = new Move(game, allTileValues);
        move.assessMovement(reversed, horizontal);
    }
}