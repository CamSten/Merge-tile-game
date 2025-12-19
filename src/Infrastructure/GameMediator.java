package Infrastructure;

import GUI.MainPanel;
import GUI.Game.Board;
import GameComponents.GameSession;
import GameComponents.Moves.Move;
import GameComponents.Moves.MoveResult;
import Server.Database.Highscores;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class GameMediator implements Subscriber {
    private static GameMediator mediator = new GameMediator();
    private static List<Subscriber> upperSubscribers = new ArrayList<>();
    private static List<Subscriber> lowerSubscribers = new ArrayList<>();
    private final EnumSet<Subscriber.EventType> upperEventTypes = EnumSet.of(EventType.REQUEST_ALL_HIGHSCORES, EventType.NEW_UNCHECKED_VALUES, EventType.REQUEST_NEW_GAME, EventType.REQUEST_KEY_ACTION, EventType.REQUEST_CONTINUE_GAME, EventType.REQUEST_SAVE_SCORE);
    private final EnumSet<Subscriber.EventType> lowerEventTypes = EnumSet.of(EventType.RETURN_ALL_HIGHSCORES, EventType.RETURN_ADD_MENU_PANEL, EventType.RETURN_ADD_GAME_PANEL, EventType.RETURN_UPDATE_TILES, EventType.RETURN_NEW_SCORE, EventType.RETURN_DISPLAY_SCORE, EventType.RETURN_UPDATE_VALUES,  EventType.RETURN_ADD_END_PANEL);

    private GameMediator(){

    }
    public static void subscribe(Subscriber s){
        for (Subscriber sss : upperSubscribers){
            System.out.println("upper Subscribers are: " + sss.getClass());
        }

        System.out.println("in GAMEMediagor, subscription request from:" + s);
        if(s instanceof GameManager || s instanceof  GameSession || s instanceof Highscores) {
            System.out.println("s instance of: " + s.getClass());
            boolean subscribed = false;
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
        else if(s instanceof Move || s instanceof MoveResult || s instanceof Board || s instanceof AppManager){
            lowerSubscribers.add(s);
            System.out.println("in GAMEMEDIATOR, new subscriber is: " + s.getClass());
        }
        for (Subscriber sss : upperSubscribers){
            System.out.println("upper Subscribers are: " + sss.getClass());
        }
    }

    @Override
    public void update(EventType eventType, Object data) {
        System.out.println("update in GameMediator was reached. Eventtype is: " + eventType);

        if (data != null){
            System.out.println("in GameMediator, data is: " + data.getClass());
        }
        List<Subscriber> subscribers = new ArrayList<>();
        if (upperEventTypes.contains(eventType)){
            subscribers = upperSubscribers;
        }
        else if (lowerEventTypes.contains(eventType)){
            subscribers = lowerSubscribers;
        }
        for (Subscriber s : subscribers){
            System.out.println("____ IN GAMEMEDIATOR, updated subscriber is: " + s.getClass());
            s.update(eventType, data);
        }
    }
    public static GameMediator getInstance(){
        if (mediator == null) {
            System.out.println("in getMediator, mediator is null");
            mediator = new GameMediator();
        }
        System.out.println("::::: in GameMediator, mediator is returned.");
        return mediator;
    }
}
