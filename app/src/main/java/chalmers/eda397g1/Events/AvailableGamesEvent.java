package chalmers.eda397g1.Events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.Objects.Game;
import chalmers.eda397g1.Objects.User;

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
                    JSONObject game = arr.getJSONObject(i);
                    JSONObject host = game.getJSONObject("host");
                    games.add(new Game(
                            game.optString("session_id"),
                            game.optString("name"),
                            new User(
                                    host.optString("login"),
                                    host.optString("avatar")
                            )
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