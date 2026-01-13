package GameComponents.Moves;
import java.util.List;

public class MoveResult {
    private List<List<Integer>> newValues;
    private boolean changedValues;
    private int points;

    public MoveResult(List<List<Integer>> newValues, boolean changedValues, int points){
        this.newValues = newValues;
        this.changedValues = changedValues;
        this.points = points;
    }
    public  List<List<Integer>> getNewValues (){
        return newValues;
    }

    public boolean hasValuesChanged(){
        return changedValues;
    }
    public int getPoints(){
        return points;
    }
}
