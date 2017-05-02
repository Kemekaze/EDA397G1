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

    public VoteItemResultEvent(Object... args) {
        super(args);
        List<Vote> votes = new ArrayList<>();
        JSONObject obj = (JSONObject) getData();


        JSONArray array = obj.optJSONArray("votes");
        for(int i = 0; i < array.length(); i++) {
            JSONObject vote = array.optJSONObject(i);
            JSONObject user = vote.optJSONObject("user");

            votes.add(new Vote(vote.optInt("effort"),
                    new User(user.optString("login"),
                            user.optString("avatar"))));
        }

        voteItemResult = new VoteItemResult(votes,
                obj.optString("item_id"),
                obj.optString("next_id"));
    }

    public VoteItemResult getVoteItemResult() {
        return voteItemResult;
    }
}
