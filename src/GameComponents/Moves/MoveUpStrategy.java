package GameComponents.Moves;
import GameComponents.Board;

import java.util.List;

public class MoveUpStrategy implements MoveStrategy{
    private boolean reversed = false;
    private boolean horizontal = false;
    private Board board;
    private Move move;

    public MoveUpStrategy(Board board){
        System.out.println("move UP strategy was reached");
        this.board = board;
    }

    public void move(List<List<Integer>> allTileValues){
        this.move = new Move(board, allTileValues);
        move.assessMovement(reversed, horizontal);
    }
}
