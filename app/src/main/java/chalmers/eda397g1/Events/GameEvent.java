package chalmers.eda397g1.Events;

import org.json.JSONObject;

import chalmers.eda397g1.Objects.Game;
import chalmers.eda397g1.Objects.User;

/**
 * Created by Martin on 2017-04-20.
 */

public class GameEvent extends Event {
    private Game currentGame;

    public GameEvent(Object... args) {
        super(args);
        Object obj = getData();
        if (obj instanceof JSONObject) {
            JSONObject game = (JSONObject) getData();
            JSONObject host = game.optJSONObject("host");
            currentGame = new Game(
                    game.optString("session_id"),
                    game.optString("name"),
                    new User(
                            host.optString("login"),
                            host.optString("avatar")
                    )
            );

        }
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}
