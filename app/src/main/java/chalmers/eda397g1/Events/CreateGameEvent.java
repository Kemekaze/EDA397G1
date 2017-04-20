package chalmers.eda397g1.Events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.Objects.Game;

/**
 * Created by Martin on 2017-04-20.
 */

public class CreateGameEvent extends Event {
    private Game currentGame;

    public CreateGameEvent(Object... args) {
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

    public Game getCurrentGames() {
        return currentGame;
    }
}
