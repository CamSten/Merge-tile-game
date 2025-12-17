package Server.Database;

public class User {
    String username;
    String password;

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
