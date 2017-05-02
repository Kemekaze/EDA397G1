package chalmers.eda397g1.models;

import java.util.List;

/**
 * Created by Martin on 2017-04-27.
 */

public class VoteItemResult { // sent when finished
    private int effort;
    private String itemId;
    private String nextId;

    public VoteItemResult(int effort, String itemId, String nextId) {
        this.effort = effort;
        this.itemId = itemId;
        this.nextId = nextId;
    }

    public int getEffort() {
        return effort;
    }

    public String getItemId() {
        return itemId;
    }

    public String getNextId() {
        return nextId;
    }
}
