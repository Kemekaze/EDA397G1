package chalmers.eda397g1.Events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.Objects.Game;

/**
 * Created by nume on 2017-04-20
 */

public class AvailableGamesEvent extends Event {
    private List<Game> games = new ArrayList<>();

    public AvailableGamesEvent(Object... args) {
        super(args);
        Object obj = getData();
        if (obj instanceof JSONArray) {
            JSONArray arr = (JSONArray) getData();
            for (int i = 0; i < arr.length(); i++) {
                try {
                    JSONObject obj2 = arr.getJSONObject(i);
                    games.add(new Game(
                            obj2.getString("session_id"),
                            obj2.getString("name")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Game> getAvailableGames() {
        return games;
    }

}