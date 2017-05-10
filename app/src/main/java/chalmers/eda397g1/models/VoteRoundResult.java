package chalmers.eda397g1.models;

import java.util.ArrayList;

/**
 * Created by Martin on 2017-04-27.
 */

public class VoteRoundResult {
    private ArrayList<Vote> votes;

    public VoteRoundResult(ArrayList<Vote> votes) {
        this.votes = votes;
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }
}
