package chalmers.eda397g1.Events;

/**
 * Created by Martin on 2017-04-11.
 */

public class VoteOnLowestEffortEvent extends ParentEvent {
    public VoteOnLowestEffortEvent(String eventName, String[] data) {
        super(eventName, data);
    }

    public VoteOnLowestEffortEvent(String eventName) {
        super(eventName);
    }
}
