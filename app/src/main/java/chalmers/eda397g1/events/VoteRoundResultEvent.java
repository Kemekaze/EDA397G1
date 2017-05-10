package chalmers.eda397g1.events;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import chalmers.eda397g1.models.User;
import chalmers.eda397g1.models.Vote;
import chalmers.eda397g1.models.VoteRoundResult;

/**
 * Created by Martin on 2017-05-02.
 */

public class VoteRoundResultEvent extends Event {
    private VoteRoundResult voteRoundResult;

    /**
     * Event that is fired when all votes for a round have been collected, but not all effort values
     * are equal. Contains the result of the vote.
     * @param args
     */
    public VoteRoundResultEvent(Object... args) {
        super(args);
        ArrayList<Vote> votes = new ArrayList<>();

        JSONArray array = (JSONArray) getData();
        for(int i = 0; i < array.length(); i++) {
            JSONObject element = array.optJSONObject(i);
            JSONObject user = element.optJSONObject("user");

            votes.add(new Vote(element.optInt("vote"),
                    new User(user.optString("login"),
                            user.optString("avatar"))));
        }

        voteRoundResult = new VoteRoundResult(votes);
    }

    public VoteRoundResult getVoteRoundResult() {
        return voteRoundResult;
    }
}
