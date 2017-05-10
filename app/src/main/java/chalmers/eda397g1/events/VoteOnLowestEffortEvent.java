package chalmers.eda397g1.events;

/**
 * Created by Martin on 2017-04-11.
 */

public class VoteOnLowestEffortEvent extends Event {

private String itemTitle;
    public VoteOnLowestEffortEvent(Object... args) {
        super(args);
       /* Object obj =  getData();
        if (obj instanceof BacklogItem) {
            String title = ((BacklogItem) obj).getTitle();
            this.itemTitle = title;
        } else {
            //Do nothing
        }*/
    }

}
