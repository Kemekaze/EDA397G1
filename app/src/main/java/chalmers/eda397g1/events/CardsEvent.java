package chalmers.eda397g1.events;

import org.json.JSONArray;
import org.json.JSONObject;

import chalmers.eda397g1.models.Card;
import chalmers.eda397g1.models.Label;

/**
 * Created by elias on 2017-04-17.
 */

public class CardsEvent extends Event {

    private Card[] cards = null;

    public CardsEvent(Object... args) {
        super(args);
        JSONArray cards = (JSONArray) getData();
        if(cards.length() > 0)
        {
            this.cards = new Card[cards.length()];
            for(int i = 0; i < cards.length(); i++)
            {
                JSONObject repo = cards.optJSONObject(i);

                this.cards[i] = new Card(
                        repo.optInt("card_id"),
                        repo.optInt("column_id"),
                        repo.optInt("issue_id"),
                        repo.optInt("number"),
                        repo.optString("title"),
                        repo.optString("body"),
                        repo.optString("state")
                );
                JSONArray labels = (JSONArray) repo.opt("labels");
                if(labels.length() > 0)
                {
                    Label[] lables_arr = new Label[labels.length()];
                    for(int l = 0; l < labels.length(); l++) {
                        JSONObject label = labels.optJSONObject(i);
                        lables_arr[i] = new Label(
                            label.optInt("id"),
                            label.optString("title")
                        );
                    }
                }
            }
        }else{
            this.cards = new Card[0];
        }
    }
}
