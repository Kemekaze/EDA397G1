package chalmers.eda397g1.Objects;

/**
 * Created by nume on 2017-04-20
 */

public class AvailSession {
    private String session_id;
    private String full_name;
    private User host;

    public AvailSession(String session_id, String full_name, User host) {
        this.session_id = session_id;
        this.full_name = full_name;
        this.host = host;
    }

    public String getSessionId() {
        return session_id;
    }

    public String getName() {
        return full_name;
    }

    public User getHost() {
        return host;
    }
}
