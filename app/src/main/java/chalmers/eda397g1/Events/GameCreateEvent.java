package chalmers.eda397g1.Events;

import android.util.Log;

import org.json.JSONObject;

import chalmers.eda397g1.Objects.Game;
import chalmers.eda397g1.Objects.User;

/**
 * Created by Martin on 2017-04-20.
 */

public class GameCreateEvent extends Event {
    private Game currentGame;

    public GameCreateEvent(Object... args) {
        super(args);

        JSONObject game = (JSONObject) getData();
        JSONObject host = game.optJSONObject("host");
        Log.i("GameCreateEvent", game.toString());
        currentGame = new Game(
                game.optString("session_id"),
                game.optString("full_name"),
                new User(
                        host.optString("login"),
                        host.optString("avatar")
                )
        );


    }
    public Game getCurrentGame() {
        return currentGame;
    }
}
