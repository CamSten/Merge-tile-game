package Infrastructure;

public interface Subscriber {

    public enum EventType {
        START, ADD_MENU_PANEL,ADD_GAME_PANEL, NEW_GAME, KEY_ACTION, UPDATE_VALUES, UPDATE_TILES , NEW_SCORE, DISPLAY_SCORE, ADD_END_PANEL, CONTINUE_GAME;
    }
    public void update(EventType e, Object data);
}
