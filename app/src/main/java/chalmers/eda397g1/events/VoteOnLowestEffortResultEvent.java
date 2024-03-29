package chalmers.eda397g1.events;

import android.util.Log;
import org.json.JSONObject;

/**
 * Created by Martin on 2017-04-27.
 */

//This event is fired when the voting on the item with the lowest effort is done.

public class VoteOnLowestEffortResultEvent extends Event {
    private final String TAG = "VoteOnLow..Comp..Event:";
    private String referenceItemId;
    private int lowestEffort;
    private String nextId; // next id to vote on

    public VoteOnLowestEffortResultEvent(Object... args) {
        super(args);
        JSONObject root = (JSONObject) getData();
        Log.d(TAG, "getData:" + root.toString());
        referenceItemId = root.optString("item_id");
        lowestEffort = root.optInt("effort");
        nextId = root.optString("next_id");
    }

    public String getReferenceItemId() {
        return referenceItemId;
    }

    public int getLowestEffort() {
        return lowestEffort;
    }

    public String getNextId() {
        return nextId;
    }
}
