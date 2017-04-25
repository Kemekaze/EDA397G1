package chalmers.eda397g1.Events;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.Objects.User;

/**
 * Created by julius on 20.04.17.
 */

public class LobbyUpdateEvent extends Event {
    private List<User> users;

    public LobbyUpdateEvent(Object... args){
        super(args);
        JSONArray data = (JSONArray) getData();
        users = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.optJSONObject(i);
            users.add(new User(
                        obj.optString("login"),
                        obj.optString("avatar")));
        }
    }

    public List<User> getUsers(){
        return users;
    }
}
