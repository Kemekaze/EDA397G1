package chalmers.eda397g1.Events;

/**
 * Created by Martin on 2017-04-11.
 */

public class RequestEvent extends ParentEvent {
    public RequestEvent(String eventName, String data) {
        super(eventName, data);
    }

    public RequestEvent(String eventName) { super(eventName); }
}
