package chalmers.eda397g1.models;

/**
 * Created by Martin on 2017-04-27.
 */

public class Vote {
    private int effort;
    private User user;

    public Vote(int effort, User user) {
        this.effort = effort;
        this.user = user;
    }

    public int getEffort() {
        return effort;
    }

    public User getUser() {
        return user;
    }

}
