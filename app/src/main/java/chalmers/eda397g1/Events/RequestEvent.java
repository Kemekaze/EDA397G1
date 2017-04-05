package chalmers.eda397g1.Events;

/**
 * Created by elias on 2017-04-05.
 */

public class RequestEvent {

    private String eventName;
    private Object data = null;
    public RequestEvent(String eventName, Object data) {
        this.eventName = eventName;
        this.data = data;
    }
    public RequestEvent(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public Object getData() {
        return data;
    }
}
