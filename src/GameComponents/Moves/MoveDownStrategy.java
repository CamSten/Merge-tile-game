package GameComponents.Moves;
import GameComponents.GameSession;

import java.util.List;

public class MoveDownStrategy implements MoveStrategy {
    private boolean reversed = true;
    private boolean horizontal = false;
    private GameSession game;
    private Move move;

    public MoveDownStrategy(GameSession game) {
        System.out.println("move DOWN strategy was reached");
        this.game = game;
    }
    public void move(List<List<Integer>> allTileValues) {
        this.move = new Move(game, allTileValues);
        move.assessMovement(reversed, horizontal);
    }

}
