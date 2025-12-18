package Infrastructure;

import GUI.MainPanel;
import GUI.Game.Board;
import GameComponents.GameSession;
import GameComponents.Moves.Move;
import GameComponents.Moves.MoveResult;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class GameMediator implements Subscriber {
    private static GameMediator mediator = new GameMediator();
    private static List<Subscriber> upperSubscribers = new ArrayList<>();
    private static List<Subscriber> lowerSubscribers = new ArrayList<>();
    private final EnumSet<Subscriber.EventType> upperEventTypes = EnumSet.of( EventType.REQUEST_NEW_GAME, EventType.UPDATE_TILES, EventType.ADD_END_PANEL, EventType.ADD_MENU_PANEL, EventType.ADD_GAME_PANEL);
    private final EnumSet<Subscriber.EventType> lowerEventTypes = EnumSet.of(EventType.KEY_ACTION, EventType.UPDATE_TILES, EventType.NEW_SCORE, EventType.CONTINUE_GAME, EventType.DISPLAY_SCORE, EventType.UPDATE_VALUES);

    private GameMediator(){

    }
    public static void subscribe(Subscriber s){
        if( s instanceof GameManager || s instanceof  GameSession){
            upperSubscribers.add(s);
        }
        else if(s instanceof Move || s instanceof MoveResult || s instanceof Board){
            lowerSubscribers.add(s);
        }
        else if(s instanceof MainPanel){
            upperSubscribers.add(s);
            lowerSubscribers.add(s);
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
