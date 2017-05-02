package chalmers.eda397g1.events;

import org.json.JSONObject;

/**
 * Created by elias on 2017-04-29.
 */

public class KickedEvent extends Event {
    private String reason;
     public KickedEvent(Object... args) {
        super(args);
         JSONObject data = (JSONObject) getData();
         this.reason = data.optString("reason");
    }

    public String getReason() {
        return reason;
    }
}
