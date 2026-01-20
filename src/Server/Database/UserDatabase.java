package Server.Database;
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
    private ObjectMapper mapper = new ObjectMapper();
    private Mediator mediator;
    private static UserDatabase userDatabase = new UserDatabase();
    private List<User> allUsers = new ArrayList<>();

    private UserDatabase(){

    }
    public static UserDatabase getInstance(){
        return userDatabase;
    }
    public void subscribe(Mediator mediator) {
        this.mediator = mediator;
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
    public void saveToFile(User user){
        allUsers.add(user);
        try{
            mapper.writerWithDefaultPrettyPrinter().writeValue(userPath.toFile(), allUsers);
        }
        catch (IOException e){
            e.printStackTrace();
        }
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
        allUsers = retrieveUsersFromFile();
        String username = userInput[0];
        String password = userInput[1];
        User validUser = null;
        boolean foundUser = false;
        for (User user : allUsers){
            if (user.getUsername().equalsIgnoreCase(username)){
                validUser = user;
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
    @Override
    public void update(EventType e, Object data) {
        if (e == EventType.REQUEST_VALIDATE_USER){
            String[] userInput = (String[]) data;
            validateUser(userInput);
        }
        else if (e == EventType.REQUEST_SAVE_NEW_USER){
            String[] userInput = (String[]) data;
            saveNewUser(userInput);
        }
    }
}