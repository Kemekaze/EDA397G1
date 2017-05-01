package chalmers.eda397g1.models;

/**
 * Created by Julius on 01.05.2017.
 */

public class RoundVoteResult {
    private User user;
    private int vote;

    public RoundVoteResult(User user, int vote) {
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
