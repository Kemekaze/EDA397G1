package chalmers.eda397g1.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 2017-04-27.
 */

public class VoteItemCompleted {
    private ArrayList<Vote> votes;
    private String itemId;
    private String nextId;

    public VoteItemCompleted(ArrayList<Vote> votes, String itemId, String nextId) {
        this.votes = votes;
        this.itemId = itemId;
        this.nextId = nextId;
    }

    public ArrayList<Vote> getVotes() {
        return votes;
    }

    public String getItemId() {
        return itemId;
    }

    public String getNextId() {
        return nextId;
    }
}
