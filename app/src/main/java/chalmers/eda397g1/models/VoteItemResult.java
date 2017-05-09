package chalmers.eda397g1.models;

/**
 * Created by Martin on 2017-04-27.
 */

public class VoteItemResult { // sent when finished
    private int effort;
    private String itemId;
    private String nextId;

    /**
     * Model for the end of a round when the final effort for an item has been found.
     * @param effort Final effort of te current item with @itemIde
     * @param itemId ItemID of current item
     * @param nextId ItemID of next item
     */
    public VoteItemResult(int effort, String itemId, String nextId) {
        this.effort = effort;
        this.itemId = itemId;
        this.nextId = nextId;
    }

    public int getEffort() { return effort; }

    public String getItemId() {
        return itemId;
    }

    public String getNextId() {
        return nextId;
    }
}
