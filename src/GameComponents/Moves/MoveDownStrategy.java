package GameComponents.Moves;
import GameComponents.GameSession;
import Infrastructure.Mediator;
import java.util.List;

public class MoveDownStrategy implements MoveStrategy {
    private boolean reversed = true;
    private boolean horizontal = false;
    private GameSession game;
    private Move move;

    public MoveDownStrategy(GameSession game) {
        this.game = game;
    }
    public void move(List<List<Integer>> allTileValues, Mediator mediator) {
        this.move = new Move(game, allTileValues, mediator);
        move.assessMovement(reversed, horizontal);
    }
}