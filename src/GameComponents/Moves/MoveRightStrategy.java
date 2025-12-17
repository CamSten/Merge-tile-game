package GameComponents.Moves;
import GameComponents.GameSession;

import java.util.List;

public class MoveRightStrategy implements MoveStrategy {
    private boolean reversed = true;
    private boolean horizontal = true;
    private GameSession game;
    private Move move;

    public MoveRightStrategy(GameSession game) {
        System.out.println("move RIGHT strategy was reached");
        this.game = game;
    }

    public void move(List<List<Integer>> allTileValues) {
        this.move = new Move(game, allTileValues);
        move.assessMovement(reversed, horizontal);
    }
}
