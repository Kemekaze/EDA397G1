package chalmers.eda397g1.events;

import org.json.JSONObject;

import chalmers.eda397g1.models.User;

/**
 * Created by elias on 2017-04-06.
 */

public class LoginEvent extends Event {
    private User user;

    public LoginEvent(Object... args) {
        super(args);

        JSONObject obj = (JSONObject) getData();
        user = new User(obj.optString("login"), obj.optString("avatar_url"));
    }

    public User getUser() {
        return user;
    }
}
