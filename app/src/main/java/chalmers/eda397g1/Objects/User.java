package chalmers.eda397g1.Objects;

import java.io.Serializable;

/**
 * Created by Martin on 2017-04-21.
 */

public class User implements Serializable {
    private String login;
    private String uri;

    public User(String login, String uri) {
        this.login = login;
        this.uri = uri;
    }

    public String getLogin() {
        return login;
    }

    public String getUri() {
        return uri;
    }
}
