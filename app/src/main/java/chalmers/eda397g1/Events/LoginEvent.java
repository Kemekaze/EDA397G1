package chalmers.eda397g1.Events;

import org.json.JSONObject;

/**
 * Created by elias on 2017-04-06.
 */

public class LoginEvent extends Event {

    private String login;
    private String uri;

    public LoginEvent(Object... args) {
        super(args);

        JSONObject obj = (JSONObject) getData();
        this.login = obj.optString("login");
        this.uri = obj.optString("avatar_url");

    }
}
