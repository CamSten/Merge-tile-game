package GameComponents;

public interface Subscriber {

    public enum EventType {
        ADD_GAME_PANEL, NEW_SCORE, DISPLAY_SCORE, ADD_END_PANEL, CONTINUE_GAME;
    }
    public void update(EventType e, Object data);
}
