package Infrastructure;

public interface Subscriber {

    public enum EventType {
        REQUEST_NEW_SCORE,
        RETURN_ADD_MENU_PANEL,
        RETURN_ADD_GAME_PANEL,
        REQUEST_SAVE_SCORE,
        REQUEST_NEW_GAME,
        REQUEST_KEY_ACTION,
        RETURN_UPDATE_VALUES,
        RETURN_UPDATE_TILES ,
        RETURN_NEW_SCORE,
        RETURN_DISPLAY_SCORE,
        RETURN_ADD_END_PANEL,
        REQUEST_CONTINUE_GAME,
        NEW_UNCHECKED_VALUES,
        REQUEST_ALL_HIGHSCORES,
        RETURN_ALL_HIGHSCORES,
        REQUEST_ADD_MENU_PANEL;
    }
    public void update(EventType e, Object data);
}
