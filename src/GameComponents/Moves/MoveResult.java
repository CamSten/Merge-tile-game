package GameComponents.Moves;
import java.util.List;

public class MoveResult {
    private List<List<Integer>> newValues;
    private boolean changedValues;
    private boolean full;
    private boolean reached2048;
    private boolean continueAfterWin;

    public MoveResult(List<List<Integer>> newValues, boolean changedValues, boolean full, boolean reached2048, boolean continueAfterWin){
        this.newValues = newValues;
        this.changedValues = changedValues;
        this.full = full;
        this.reached2048 = reached2048;
        this.continueAfterWin = continueAfterWin;
    }
    public  List<List<Integer>> getNewValues (){
        return newValues;
    }

    public boolean hasValuesChanged(){
        return changedValues;
    }

    public boolean isFull(){
        return full;
    }
    public boolean hasReached2048(){
        return reached2048;
    }
    public boolean doContinueAfterWin(){
        return continueAfterWin;
    }
}
