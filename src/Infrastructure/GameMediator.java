package Infrastructure;

import GUI.MainPanel;
import GUI.Game.Board;
import GameComponents.Moves.Move;
import GameComponents.Moves.MoveResult;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class GameMediator implements Subscriber {
    private List<Subscriber> upperSubscribers = new ArrayList<>();
    private List<Subscriber> lowerSubscribers = new ArrayList<>();
    private final EnumSet<Subscriber.EventType> upperEventTypes = EnumSet.of(EventType.START, EventType.NEW_GAME, EventType.ADD_END_PANEL, EventType.ADD_MENU_PANEL, EventType.ADD_GAME_PANEL);
    private final EnumSet<Subscriber.EventType> lowerEventTypes = EnumSet.of(EventType.KEY_ACTION, EventType.UPDATE_TILES, EventType.NEW_SCORE, EventType.CONTINUE_GAME, EventType.DISPLAY_SCORE, EventType.UPDATE_VALUES);

    public void subscribe(Subscriber s){
        if(s instanceof MainPanel || s instanceof GameManager){
            upperSubscribers.add(s);
        }
        else if(s instanceof Move || s instanceof MoveResult || s instanceof Board){
            lowerSubscribers.add(s);
        }
    }

    @Override
    public void update(EventType eventType, Object data) {
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
}
