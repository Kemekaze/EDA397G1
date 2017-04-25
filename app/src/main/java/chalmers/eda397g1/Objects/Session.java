package chalmers.eda397g1.Objects;

/**
 * Created by nume on 2017-04-25.
 */

public class Session {
    private String id;
    private Github github;
    private User host;

    public Session(String id, Github github, User host){
        this.id = id;
        this.github = github;
        this.host = host;
    }

    public String getId() {
        return id;
    }

    public Github getGithub(){
        return github;
    }

    public User getHost(){
        return host;
    }
}
