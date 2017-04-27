package chalmers.eda397g1.models;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Martin on 2017-04-27.
 */

public class VoteItemCompleted {
    private List<Vote> votes;
    private String itemId;
    private String nextId;

    public VoteItemCompleted(List<Vote> votes, String itemId, String nextId) {
        this.votes = votes;
        this.itemId = itemId;
        this.nextId = nextId;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public String getItemId() {
        return itemId;
    }

    public String getNextId() {
        return nextId;
    }
}
