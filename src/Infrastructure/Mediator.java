package Infrastructure;
import GameComponents.GameSession;
import GameComponents.Moves.Move;
import Server.Database.GameDatabase;
import Server.Database.HighscoreDatabase;
import Server.Database.UserDatabase;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Mediator implements Subscriber {
    private List<Subscriber> appSubscribers = new ArrayList<>();
    private List<Subscriber> lowerGameSubscribers = new ArrayList<>();
    private List<Subscriber> upperGameSubscribers = new ArrayList<>();
    private List<Subscriber> dbSubscribers = new ArrayList<>();
    private List<Subscriber> pendingUnsubscribe = new ArrayList<>();
    private final EnumSet<EventType> appEvents = EnumSet.of(
            EventType.RETURN_NEW_USER_SAVED,
            EventType.RETURN_EXISTING_USER,
            EventType.RETURN_NO_SUCH_USER,
            EventType.RETURN_ADD_GAME_PANEL,
            EventType.REQUEST_NEW_GAME,
            EventType.RETURN_ADD_MENU_PANEL,
            EventType.RETURN_GET_SAVED_GAME_TRUE,
            EventType.CONFIRM_SAVED_TRUE,
            EventType.CONFIRM_SAVED_FALSE,
            EventType.RETURN_ALL_HIGHSCORES,
            EventType.RETURN_GET_SAVED_GAME_FALSE,
            EventType.REQUEST_ADD_END_PANEL,
            EventType.RETURN_ADD_END_PANEL,
            EventType.RETURN_HIGHEST_SCORE_TRUE,
            EventType.RETURN_HIGHEST_SCORE_FALSE
    );
    private final EnumSet<EventType> dbEvents = EnumSet.of(
            EventType.REQUEST_VALIDATE_USER,
            EventType.REQUEST_SAVE_NEW_USER,
            EventType.REQUEST_CHECK_SAVE_GAME,
            EventType.REQUEST_GET_SAVED_GAME,
            EventType.REQUEST_SAVE_SCORE,
            EventType.REQUEST_ALL_HIGHSCORES,
            EventType.REQUEST_SAVE_GAME_EXECUTE,
            EventType.RETURN_REMOVE_GAME,
            EventType.REQUEST_HIGHEST_SCORE
    );
    EnumSet<EventType> upperGameEvents = EnumSet.of(
            EventType.RETURN_UPDATE_TILES,
            EventType.RETURN_DISPLAY_SCORE,
            EventType.RETURN_NEW_POINTS,
            EventType.RETURN_HIGHEST_SCORE,
            EventType.RETURN_NEW_HIGHSCORE,
            EventType.RETURN_EMPTY_SCORELIST,
            EventType.REQUEST_CONTINUE_GAME,
            EventType.RETURN_CONTINUE_GAME_TRUE,
            EventType.RETURN_CONTINUE_GAME_FALSE,
            EventType.CONFIRM_FINISHED_SESSION,
            EventType.REQUEST_REMOVE_GAME
    );
    EnumSet<EventType> lowerGameEvents = EnumSet.of(
            EventType.REQUEST_KEY_ACTION,
            EventType.RETURN_UPDATE_VALUES,
            EventType.RETURN_NEW_UNCHECKED_VALUES,
            EventType.REQUEST_SAVE_GAME_INITIATE
    );

    public Mediator(){

    }
    public void subscribe(Subscriber s) {
        if (s == null) return;
        if (s instanceof AppManager) {
            if (!appSubscribers.contains(s)) {
                appSubscribers.add(s);
            }
            return;
        }
        if (s instanceof GameManager) {
            if (!upperGameSubscribers.contains(s)) {
                upperGameSubscribers.add(s);
            }
            return;
        }
        if (s instanceof GameSession || s instanceof Move){
            if (!lowerGameSubscribers.contains(s)) {
                lowerGameSubscribers.add(s);
            }
            return;
        }
        if (s instanceof UserDatabase || s instanceof GameDatabase || s instanceof HighscoreDatabase) {
            if (!dbSubscribers.contains(s)) {
                dbSubscribers.add(s);
            }
            return;
        }
    }
    private void unsubscribe(){
        for (Subscriber s : pendingUnsubscribe){
            lowerGameSubscribers.remove(s);
        }
    }
    public void unsubscribeLowerGame(Subscriber s){
        pendingUnsubscribe.add(s);
    }
    @Override
    public void update(EventType eventType, Object data) {
        List<Subscriber> targetSubscribers = null;

        if (appEvents.contains(eventType)) targetSubscribers = appSubscribers;
        else if (upperGameEvents.contains(eventType)) targetSubscribers = upperGameSubscribers;
        else if (dbEvents.contains(eventType)) targetSubscribers = dbSubscribers;
        else if (lowerGameEvents.contains(eventType)) targetSubscribers = lowerGameSubscribers;

        if (targetSubscribers != null) {
            for (Subscriber s : new ArrayList<>(targetSubscribers)) {
                s.update(eventType, data);
            }
        }
        unsubscribe();
    }
}