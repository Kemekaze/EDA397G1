package chalmers.eda397g1.ui;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.widget.TextView;

import chalmers.eda397g1.R;
import chalmers.eda397g1.events.RequestEvent;
import chalmers.eda397g1.events.VoteItemResultEvent;
import chalmers.eda397g1.events.VoteRoundResultEvent;
import chalmers.eda397g1.models.BacklogItem;
import chalmers.eda397g1.models.Session;
import chalmers.eda397g1.models.Vote;
import chalmers.eda397g1.resources.Constants;
import chalmers.eda397g1.ui.fragments.ResultsDialogFragment;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Jesper Kjellqvist 03/04/17
 */

public class VoteActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    final static String TAG = "VoteActivity";
    private NumberPicker effortPicker;
    private FloatingActionButton voteButton;
    private TextView currentItemTextView;
    private int selectedEffort;
    private Session session;
    private BacklogItem currentItem;
    private BacklogItem referenceItem;
    private int referenceEffort;
    private int itemsLeft;

    int[] effortValues = {0, 1, 2, 3, 4, 5, 8, 13, 20, 30, 50, 100, 200};

    // TODO: Remove this when actual data is passed from VoteOnLowestEffortActivity
    private boolean DEBUG = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            session = (Session) b.getSerializable("session");
            String startItem = b.getString("startItemId");
            String referenceId = b.getString("referenceId");
            referenceEffort = b.getInt("referenceEffort");
            currentItem = getBackLogItemById(startItem);
            referenceItem = getBackLogItemById(referenceId);

        } else {
            throw new RuntimeException("No session passed!");
        }

        // Items that are left to vote for minus the reference item.
        itemsLeft = session.getGithub().getBacklogItems().size() - 1;

        currentItemTextView = (TextView) findViewById(R.id.currentItemTitle);
        currentItemTextView.setText(currentItem.getTitle());
        ((TextView) findViewById(R.id.referenceItemTitle)).setText(referenceItem.getTitle());
        ((TextView) findViewById(R.id.refrenceItemBody)).setText(referenceItem.getBody());
        ((TextView) findViewById(R.id.refrenceText)).setText("Effort: "+ referenceEffort);

        setupEffortPicker();
        setupVoteButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onReceiveRoundResults(VoteRoundResultEvent event){
        Log.d(TAG, "Receive Round Result");
        ArrayList<Vote> votes = event.getVoteRoundResult().getVotes();
        displayResults(votes);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onReceiveItemResult(VoteItemResultEvent event){
        Log.d(TAG, "onReceiveItemResult()");
        int effort = event.getVoteItemResult().getEffort();
        String currentId = event.getVoteItemResult().getItemId();
        String nextId = event.getVoteItemResult().getNextId();
        if(!currentId.equals(currentItem.getId()))
            throw new RuntimeException("Wrong itemID!");
        currentItem.setEffortValue(effort);
        itemsLeft--;
        if(itemsLeft > 0) {
            Snackbar.make(voteButton, "Chosen Effort: "+ effort, Snackbar.LENGTH_LONG).show();
            currentItem = getBackLogItemById(nextId);
            voteButton.setEnabled(true);
            effortPicker.setEnabled(true);
            currentItemTextView.setText(currentItem.getTitle());
        } else {
            // TODO: Replace this by the call to show the final result!
            Snackbar sb = Snackbar.make(voteButton, "All Votes Done!", Snackbar.LENGTH_LONG);
            sb.show();
            // Uncomment this when the next activity is started!
            //finish();
        }
    }

    private BacklogItem getBackLogItemById(String id) {
        for(BacklogItem b: session.getGithub().getBacklogItems()){
            if(b.getId().equals(id))
                return b;
        }
        throw new RuntimeException("Could not find next BacklogItem!");
    }

    public void displayResults(ArrayList<Vote> results){
        //Sort votes
        Collections.sort(results, new Comparator<Vote>() {
            @Override
            public int compare(Vote vote, Vote vote2) {
                return vote.getEffort() - vote2.getEffort();
            }
        });
        ResultsDialogFragment frag = ResultsDialogFragment.newInstance(results);
        frag.show(getFragmentManager(), "dialog");
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        // The results dialog has been dismissed
        Log.d(TAG, "onDismiss()");
        voteButton.setEnabled(true);
        effortPicker.setEnabled(true);
        currentItemTextView.setText(currentItem.getTitle());
    }

    /**
     * Initializes the NmmberPicker for effort
     */
    private void setupEffortPicker(){
        effortPicker = (NumberPicker) findViewById(R.id.effort_picker);

        effortPicker.setMinValue(0);
        selectedEffort = 0;
        effortPicker.setMaxValue(effortValues.length - 1);
        String[] effortValueStrings = new String[effortValues.length];
        for (int i = 0; i < effortValues.length; i++)
            effortValueStrings[i] = Integer.toString(effortValues[i]);
        effortPicker.setDisplayedValues(effortValueStrings);
        effortPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        effortPicker.setWrapSelectorWheel(false);
        effortPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldValue, int newValue) {
                selectedEffort = effortValues[newValue];
                Log.d(TAG, "Current effort: " + selectedEffort);
            }
        });
    }


    /**
     * Initializes the vote button.
     */
    private void setupVoteButton(){
        // Initialize voteButton
        voteButton = (FloatingActionButton) findViewById(R.id.votebutton);
        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voteButton.setEnabled(false);
                effortPicker.setEnabled(false);
                JSONObject query = new JSONObject();
                try {
                    query.putOpt("item_id", currentItem.getId());
                    query.putOpt("effort", Integer.toString(selectedEffort));
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                Log.d(TAG, query.toString());

                EventBus.getDefault().post(new RequestEvent(Constants.SocketEvents.VOTE, query));

/*                // DEBUG
                ArrayList<Vote> debugRes = new ArrayList<>();
                debugRes.add( new Vote( 42, new User("Testuser", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 1, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 2, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 3, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 4, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 5, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 7, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 8, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 9, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 10, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser A ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser B ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser C ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser D ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser E ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser F ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser G ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser H ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));

                displayResults(debugRes );*/
            }
        });
    }
}
