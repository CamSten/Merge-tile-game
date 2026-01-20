package GameComponents;
import Server.Database.User;
import java.io.Serializable;
import java.util.List;

public class Game implements Serializable {
    private User user;
    private List<Integer> allValues;
    private int points;
    public Game(){
    }
    public Game(User user, List<Integer> allValues, int points){
        this.user = user;
        this.allValues = allValues;
        this.points = points;
    }
    public User getUser(){
        return user;
    }
    public List<Integer> getAllValues(){
        return allValues;
    }
    public int getPoints(){
        return points;
    }
}