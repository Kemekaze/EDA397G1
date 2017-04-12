package chalmers.eda397g1.Events;

import org.json.JSONObject;

/**
 * Created by elias on 2017-04-05.
 */

public class RequestEvent{

    private String eventName;
    private JSONObject data = null;
    public RequestEvent(String eventName, JSONObject data) {
        this.eventName = eventName;
        this.data = data;
    }
    public RequestEvent(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public JSONObject getData() {
        return data;
    }
}
