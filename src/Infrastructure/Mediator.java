package Infrastructure;

import GUI.Game.Board;
import GameComponents.Game;
import GameComponents.GameSession;
import GameComponents.Moves.Move;
import GameComponents.Moves.MoveResult;
import Server.Database.Highscores;
import Server.Database.User;
import Server.Database.UserDatabase;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Mediator implements Subscriber {
    private static Mediator mediator = new Mediator();
    private static List<Subscriber> upperSubscribers = new ArrayList<>();
    private static List<Subscriber> lowerSubscribers = new ArrayList<>();
    private static List<Subscriber> applicationSubscribers = new ArrayList<>();
    private final EnumSet<Subscriber.EventType> upperEventTypes = EnumSet.of(EventType.REQUEST_QUIT_GAME, EventType.RETURN_UPDATE_TILES, EventType.REQUEST_STARTING_TILES, EventType.REQUEST_ALL_HIGHSCORES, EventType.REQUEST_NEW_GAME, EventType.NEW_UNCHECKED_VALUES, EventType.REQUEST_KEY_ACTION, EventType.REQUEST_CONTINUE_GAME, EventType.REQUEST_SAVE_SCORE, EventType.REQUEST_SAVE_GAME, EventType.RETURN_SAVED_GAME, EventType.REQUEST_VALIDATE_USER, EventType.REQUEST_SAVE_NEW_USER, EventType.REQUEST_GET_SAVED_GAME, EventType.RETURN_CONTINUE_GAME_TRUE, EventType.RETURN_CONTINUE_GAME_FALSE);
    private final EnumSet<Subscriber.EventType> lowerEventTypes = EnumSet.of(EventType.RETURN_ALL_HIGHSCORES, EventType.RETURN_GET_SAVED_GAME,  EventType.RETURN_ADD_MENU_PANEL, EventType.RETURN_ADD_GAME_PANEL, EventType.RETURN_UPDATE_TILES, EventType.RETURN_NEW_SCORE, EventType.RETURN_DISPLAY_SCORE, EventType.RETURN_UPDATE_VALUES,  EventType.RETURN_ADD_END_PANEL, EventType.RETURN_EXISTING_USER, EventType.RETURN_NO_SUCH_USER, EventType.RETURN_WRONG_PASSWORD, EventType.RETURN_USERNAME_TAKEN, EventType.RETURN_NEW_USER_SAVED);
    private final EnumSet<Subscriber.EventType> applicationEvents = EnumSet.of(EventType.RETURN_ADD_MENU_PANEL, EventType.RETURN_ADD_GAME_PANEL, EventType.RETURN_ADD_END_PANEL, EventType.RETURN_EXISTING_USER, EventType.RETURN_NO_SUCH_USER, EventType.RETURN_WRONG_PASSWORD, EventType.RETURN_USERNAME_TAKEN, EventType.RETURN_NEW_USER_SAVED,EventType.REQUEST_QUIT_GAME, EventType.REQUEST_QUIT_GAME,  EventType.REQUEST_NEW_GAME, EventType.REQUEST_VALIDATE_USER, EventType.REQUEST_SAVE_NEW_USER, EventType.REQUEST_GET_SAVED_GAME);
    private Mediator(){

    }
    public static void subscribe(Subscriber s){
        System.out.println("in GAMEMediator, subscription request from:" + s);
        if(s instanceof GameManager || s instanceof Highscores || s instanceof UserDatabase || s instanceof GameSession) {
            System.out.println("s instance of: " + s.getClass());
            boolean subscribed = false;
            if (applicationSubscribers.isEmpty()){
                applicationSubscribers.add(s);
            }
            else {
                System.out.println("else clause in subscribe is reached");
                for (Subscriber subscriber : applicationSubscribers){
                    if (subscriber.getClass().equals(s.getClass())) {
                        subscribed = true;
                    }
                }
                System.out.println("in else clause, subscribed is: " + subscribed);
                if (!subscribed){
                    applicationSubscribers.add(s);
                }
            }
            if (upperSubscribers.isEmpty()) {
                upperSubscribers.add(s);
            } else {
                System.out.println("else clause in subscribe is reached");
                for (Subscriber subscriber : upperSubscribers){
                    if (subscriber.getClass().equals(s.getClass())) {
                        subscribed = true;
                    }
                }
                System.out.println("in else clause, subscribed is: " + subscribed);
                if (!subscribed){
                    upperSubscribers.add(s);
                }
            }
        }
        else if (s instanceof AppManager){
            if (applicationSubscribers.isEmpty()) {
                applicationSubscribers.add(s);
            } else {
                boolean subscribed = false;
                System.out.println("else clause in subscribe is reached");
                for (Subscriber subscriber : applicationSubscribers) {
                    if (subscriber.getClass().equals(s.getClass())) {
                        subscribed = true;
                    }
                }
                System.out.println("in else clause, subscribed is: " + subscribed);
                if (!subscribed) {
                    applicationSubscribers.add(s);
                }
            }
        }
        else if(s instanceof Move || s instanceof MoveResult || s instanceof Board ){
            lowerSubscribers.add(s);
            System.out.println("in GAMEMEDIATOR, new subscriber is: " + s.getClass());
        }
        for (Subscriber sss : upperSubscribers){
            System.out.println("upper Subscribers are: " + sss.getClass());
        }
    }

    public void unsubscribe(Subscriber s){

    }
    @Override
    public void update(EventType eventType, Object data) {
        System.out.println("update in GameMediator was reached. Eventtype is: " + eventType);

        if (data != null){
            System.out.println("in GameMediator, data is: " + data.getClass());
        }
        List<Subscriber> subscribers = new ArrayList<>();
        if (applicationEvents.contains(eventType)){
            subscribers = applicationSubscribers;
        }
        else if (upperEventTypes.contains(eventType)){
            subscribers = upperSubscribers;
        }
        else if (lowerEventTypes.contains(eventType)){
            subscribers = lowerSubscribers;
        }
        List<Subscriber> copy = new ArrayList<>();
        copy.addAll(subscribers);
        for (Subscriber s : copy){
            System.out.println("____ IN GAMEMEDIATOR, updated subscriber is: " + s.getClass());
            s.update(eventType, data);
        }
    }
    public static Mediator getInstance(){
        if (mediator == null) {
            System.out.println("in getMediator, mediator is null");
            mediator = new Mediator();
        }
        System.out.println("::::: in GameMediator, mediator is returned.");
        return mediator;
    }
}
