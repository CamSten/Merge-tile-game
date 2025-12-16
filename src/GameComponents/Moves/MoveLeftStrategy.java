package GameComponents.Moves;
import GameComponents.Board;

import java.util.List;

public class MoveLeftStrategy implements MoveStrategy {
    private boolean reversed = false;
    private boolean horizontal = true;
    private Board board;
    private Move move;

    public MoveLeftStrategy(Board board) {
        System.out.println("move LEFT strategy was reached");
        this.board = board;
    }

    public void move(List<List<Integer>> allTileValues) {
        this.move = new Move(board, allTileValues);
        move.assessMovement(reversed, horizontal);
    }
}