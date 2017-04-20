package chalmers.eda397g1.Objects;

/**
 * Created by nume on 2017-04-20
 */

public class Game {
    private String sessionId;
    private String name;

    public Game(String sessionId, String name) {
        this.sessionId = sessionId;
        this.name = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }
}
