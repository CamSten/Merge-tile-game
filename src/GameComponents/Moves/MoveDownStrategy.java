package GameComponents.Moves;
import GameComponents.Board;

import java.util.List;

public class MoveDownStrategy implements MoveStrategy {
    private boolean reversed = true;
    private boolean horizontal = false;
    private Board board;
    private Move move;

    public MoveDownStrategy(Board board) {
        System.out.println("move DOWN strategy was reached");
        this.board = board;
    }
    public void move(List<List<Integer>> allTileValues) {
        this.move = new Move(board, allTileValues);
        move.assessMovement(reversed, horizontal);
    }
}
