package chalmers.eda397g1.events;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.models.User;
import chalmers.eda397g1.models.Vote;
import chalmers.eda397g1.models.VoteItemCompleted;

/**
 * Created by Martin on 2017-04-27.
 */

public class VoteItemCompletedEvent extends Event {
    private VoteItemCompleted voteItemCompleted;

    public VoteItemCompletedEvent(Object... args) {
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

        voteItemCompleted = new VoteItemCompleted(votes,
                obj.optString("item_id"),
                obj.optString("next_id"));
    }

    public VoteItemCompleted getVoteItemCompleted() {
        return voteItemCompleted;
    }
}
