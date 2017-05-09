package chalmers.eda397g1.events;

/**
 * Created by Martin on 2017-04-27.
 */
//this is wrong, we mixed it up with VoteonLowerstEffortCompletedEvent
public class VoteItemCompletedEvent extends chalmers.eda397g1.events.Event {
    //private String itemTitle;
    public VoteItemCompletedEvent(Object... args) {
        super(args);
       /* Object obj = (BacklogItem) getData();
        if(obj instanceof BacklogItem) {
            String title = ((BacklogItem) obj).getTitle();
            this.itemTitle = title;
        }
        else{
            //Do nothing
        }
    }
    public String getItemTitle(){
        return itemTitle;
    }*/
    }
}
