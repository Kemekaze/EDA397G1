package chalmers.eda397g1.models;

/**
 * Created by Julius on 01.05.2017.
 */

public class VoteResult {
    private User user;
    private int vote;

    public VoteResult(User user, int vote) {
        this.user = user;
        this.vote = vote;
    }

    public User getUser() {
        return user;
    }

    public int getVote() {
        return vote;
    }
    
}
