package chalmers.eda397g1.models;

import java.util.List;

/**
 * Created by Martin on 2017-04-27.
 */

public class VoteRoundResult {
    private List<Vote> votes;

    public VoteRoundResult(List<Vote> votes) {
        this.votes = votes;
    }

    public List<Vote> getVotes() {
        return votes;
    }
}
