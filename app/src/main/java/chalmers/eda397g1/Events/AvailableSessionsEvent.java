package chalmers.eda397g1.Events;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.Objects.AvailSession;
import chalmers.eda397g1.Objects.User;

/**
 * Created by nume on 2017-04-20
 */

public class AvailableSessionsEvent extends Event {
    private List<AvailSession> availSessions = new ArrayList<>();

    public AvailableSessionsEvent(Object... args) {
        super(args);
        Object obj = getData();
        if (obj instanceof JSONArray) {
            JSONArray arr = (JSONArray) getData();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject game = arr.optJSONObject(i);
                JSONObject host = game.optJSONObject("host");
                availSessions.add(new AvailSession(
                        game.optString("session_id"),
                        game.optString("full_name"),
                        new User(
                                host.optString("login"),
                                host.optString("avatar")
                        )
                ));

            }
        }
    }

    public List<AvailSession> getAvailableGames() {
        return availSessions;
    }

}