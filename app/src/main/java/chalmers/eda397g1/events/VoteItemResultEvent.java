package chalmers.eda397g1.events;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.models.User;
import chalmers.eda397g1.models.Vote;
import chalmers.eda397g1.models.VoteItemResult;

/**
 * Created by Martin on 2017-04-27.
 */

public class VoteItemResultEvent extends Event {
    private VoteItemResult voteItemResult;

    /**
     * Event that is fired when a round for one item is over and the effort for this item has been
     * decided.
     * @param args
     */
    public VoteItemResultEvent(Object... args) {
        super(args);
        JSONObject obj = (JSONObject) getData();

        voteItemResult = new VoteItemResult(
                obj.optInt("effort"),
                obj.optString("item_id"),
                obj.optString("next_id"));
    }

    public VoteItemResult getVoteItemResult() {
        return voteItemResult;
    }
}
