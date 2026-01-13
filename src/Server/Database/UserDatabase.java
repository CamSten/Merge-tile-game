package Server.Database;

import GameComponents.Game;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import Infrastructure.Mediator;
import Infrastructure.Subscriber;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserDatabase implements Subscriber {
    private static final Path userPath = getUserPath();
    private static final Path gamePath = getGamePath();
    private ObjectMapper mapper = new ObjectMapper();
    private static UserDatabase userDatabase = new UserDatabase();
    static Mediator mediator = Mediator.getInstance();
    private List<User> allUsers = new ArrayList<>();
    private List<Game> allGames = new ArrayList<>();

    private UserDatabase(){

    }
    public static UserDatabase getInstance(){
        return userDatabase;
    }
    public void subscribe(){
        mediator.subscribe(this);
    }

    private List<User> retrieveUsersFromFile() {
        List<User> users = new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(userPath.toFile());
            users = mapper.readValue(inputStream, new TypeReference<List<User>>(){});
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return users;
    }
    private List<Game> retrieveGamesFromFile() {
        List<Game> games = new ArrayList<>();
            try {
                InputStream inputStream = new FileInputStream(gamePath.toFile());
                games = mapper.readValue(inputStream, new TypeReference<List<Game>>() {
                });
            } catch (IOException e) {
                System.out.println("error reading file");
                e.printStackTrace();
            }
        return games;
    }

    private void getSavedGame(User user){
        allGames = retrieveGamesFromFile();
        for (Game g : allGames){
            if (g.getUser().getUsername().equalsIgnoreCase(user.getUsername())){
                mediator.update(EventType.RETURN_GET_SAVED_GAME, g);
            }
        }
    }
    public void saveToFile(User user){
        allUsers.add(user);
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(userPath.toFile(), allUsers);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public Game getGame(User user){
        allGames =  retrieveGamesFromFile();
        System.out.println("getGame in UserDatabase is reached. Allgames.size is:" + allGames.size());
        Game game = null;
        for (Game g : allGames){
            if (g.getUser().getUsername().equalsIgnoreCase(user.getUsername())){
                game = g;
            }
        }
        return game;
    }
    private void saveNewUser(String [] userInput){
        allUsers = retrieveUsersFromFile();
        String username = userInput[0];
        boolean existingUser = false;
        for (User user : allUsers){
            if (user.getUsername().equalsIgnoreCase(username)){
                existingUser = true;
            }
        }
        if (!existingUser){
            User user = new User(username);
            user.setPassword(userInput[1]);
            saveToFile(user);
            mediator.update(EventType.RETURN_NEW_USER_SAVED, user);
        }
        else {
            mediator.update(EventType.RETURN_USERNAME_TAKEN, username);
        }
    }

    private void validateUser(String[] userInput){
        System.out.println("validateUser in UserDatabase is reached");
        allUsers = retrieveUsersFromFile();
        String username = userInput[0];
        String password = userInput[1];
        User validUser = null;
        boolean foundUser = false;
        for (User user : allUsers){
            if (user.getUsername().equalsIgnoreCase(username)){
                validUser = user;
               System.out.println("foundUser is true");
                foundUser = true;
            }
        }
        if (foundUser){
            if (password.equals(validUser.getPassword())) {
                mediator.update(EventType.RETURN_EXISTING_USER, validUser);
            }
            else {
                mediator.update(EventType.RETURN_WRONG_PASSWORD, validUser);
            }
        }
        else {
            mediator.update(EventType.RETURN_NO_SUCH_USER, null);
        }
    }

    private static Path getUserPath() {
        Path path = Paths.get("src/Server/Database/Users.txt");
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }
    private static Path getGamePath() {
        Path path = Paths.get("src/Server/Database/SavedGames.txt");
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }
    private void saveGame(Game game){
        System.out.println("UPDATE FILE IN USER DATABASE IS REACHED");
        allGames = retrieveGamesFromFile();
        allGames.removeIf(g ->
                g.getUser().getUsername()
                        .equalsIgnoreCase(game.getUser().getUsername())
        );
        allGames.add(game);
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(getGamePath().toFile(), allGames);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void update(EventType e, Object data) {
        System.out.println("update in UserDatabase is reached, eventType is: " + e);
        if (e == EventType.REQUEST_VALIDATE_USER){
            String[] userInput = (String[]) data;
            validateUser(userInput);
        }
        else if (e == EventType.REQUEST_SAVE_NEW_USER){
            String[] userInput = (String[]) data;
            saveNewUser(userInput);
        }
        else if (e == EventType.RETURN_SAVED_GAME){
            Game game = (Game) data;
            saveGame(game);
        }
        else if (e == EventType.REQUEST_GET_SAVED_GAME){
            User user = (User) data;
            getSavedGame(user);
        }
    }
}
