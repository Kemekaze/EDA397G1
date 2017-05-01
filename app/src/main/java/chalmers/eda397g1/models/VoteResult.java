package chalmers.eda397g1.models;

/**
 * Created by Julius on 01.05.2017.
 */

public class VoteResult {
    private User user;
    private int vote;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
