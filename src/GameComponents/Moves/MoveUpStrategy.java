package GameComponents.Moves;
import GameComponents.GameSession;

import java.util.List;

public class MoveUpStrategy implements MoveStrategy{
    private boolean reversed = false;
    private boolean horizontal = false;
    private GameSession game;
    private Move move;

    public MoveUpStrategy(GameSession game){
        System.out.println("move UP strategy was reached");
        this.game = game;

    }

    public void move(List<List<Integer>> allTileValues){
        this.move = new Move(game, allTileValues);
        move.assessMovement(reversed, horizontal);
    }

}
