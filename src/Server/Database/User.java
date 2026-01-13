package Server.Database;

import GameComponents.Game;
import GameComponents.GameSession;

import java.io.Serializable;
import java.util.List;

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
