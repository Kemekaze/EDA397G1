package chalmers.eda397g1.Events;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Martin on 2017-04-27.
 */

public class VoteOnLowestEffortCompletedEvent extends Event {
    private final String TAG = "VoteOnLow..Comp..Event:";
    private String itemId;
    private int effort;
    private String nextId;

    public VoteOnLowestEffortCompletedEvent(Object... args) {
        super(args);
        JSONObject root = (JSONObject) getData();
        Log.d(TAG, "getData:" + root.toString());

        itemId = root.optString("item_id");
        effort = root.optInt("effort");
        nextId = root.optString("next_id");
    }

    public String getItemId() {
        return itemId;
    }

    public int getEffort() {
        return effort;
    }

    public String getNextId() {
        return nextId;
    }
}
