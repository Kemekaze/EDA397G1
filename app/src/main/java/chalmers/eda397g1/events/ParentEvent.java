package chalmers.eda397g1.events;

/**
 * Created by Martin on 2017-04-11.
 */

public abstract class ParentEvent {
    private String eventName;
    private Object data = null;

    public ParentEvent(String eventName, Object data) {
        this(eventName);
        this.data = data;
    }
    public ParentEvent(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public Object getData() {
        return data;
    }
}
