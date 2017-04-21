package chalmers.eda397g1.Events;

import org.json.JSONException;
import org.json.JSONObject;

import chalmers.eda397g1.Objects.Game;

/**
 * Created by Martin on 2017-04-21.
 */

public class GameJoinEvent extends Event {
    private Game currentGame;

    public GameJoinEvent(Object... args) {
        super(args);
        Object obj = getData();
        if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) getData();
            try {
                currentGame = new Game(jsonObject.getString("session_id"),
                        jsonObject.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}
