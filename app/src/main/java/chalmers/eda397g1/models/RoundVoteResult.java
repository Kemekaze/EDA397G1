package chalmers.eda397g1.models;

import java.io.Serializable;

/**
 * Created by Julius on 01.05.2017.
 */

public class RoundVoteResult implements Serializable{
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
