package Server.Database;
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    public User(){
    }
    public User(String username){
        this.username = username;
    }
    public String getUsername (){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String input){
        this.password = input;
    }
}