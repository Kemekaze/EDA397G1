package chalmers.eda397g1.ui;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import chalmers.eda397g1.R;
import chalmers.eda397g1.events.VoteItemCompletedEvent;
import chalmers.eda397g1.models.BacklogItem;
import chalmers.eda397g1.models.Session;
import chalmers.eda397g1.models.User;
import chalmers.eda397g1.models.Vote;
import chalmers.eda397g1.models.VoteItemCompleted;
import chalmers.eda397g1.ui.fragments.ResultsDialogFragment;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by Jesper Kjellqvist 03/04/17
 */

public class VoteActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {
    final static String TAG = "VoteActivity";
    private NumberPicker effortPicker;
    private int selectedEffort;
    private Session session;
    private BacklogItem currentItem;

    int[] effortValues = {0, 1, 2, 3, 4, 5, 8, 13, 20, 30, 50, 100, 200};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            session = (Session) b.getSerializable("session");
        } else {
            throw new RuntimeException("No session passed!");
        }

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
    public void onReceiveRoundResults(VoteItemCompletedEvent event){
        VoteItemCompleted item = event.getVoteItemCompleted();
        //NOTE: This may need to be currentItem.getIssueId()
        if(item.getItemId().equals(currentItem.getCardId())){
            throw new RuntimeException("The current item is not equal to the the currently voted item!");
        }
        currentItem = getBackLogItemById(item.getNextId());
        ArrayList<Vote> votes = item.getVotes();

        displayResults(votes);

    }

    private BacklogItem getBackLogItemById(String id) {
        for(BacklogItem b: session.getGithub().getBacklogItems()){
            if(b.getCardId().equals(id))
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
        //TODO Update the displayed item to show the new item
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
        FloatingActionButton voteButton = (FloatingActionButton) findViewById(R.id.votebutton);
        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                DEBUG
                Insert the next line to make stuff work correctly again!
                startActivity(new Intent(VoteActivity.this, DisplayRoundResultsActivity.class));
                TODO: Send vote to server and disable the button. Then wait for the results and new item to come in.
                */
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
                debugRes.add( new Vote( 6, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));
                debugRes.add( new Vote( 6, new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3")));

                displayResults(debugRes );
            }
        });
    }
}
