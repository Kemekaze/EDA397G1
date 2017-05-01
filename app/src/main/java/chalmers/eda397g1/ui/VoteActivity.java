package chalmers.eda397g1.ui;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;

import chalmers.eda397g1.R;
import chalmers.eda397g1.models.RoundVoteResult;
import chalmers.eda397g1.models.User;
import chalmers.eda397g1.ui.fragments.ResultsDialogFragment;

/**
 * Created by Jesper Kjellqvist 03/04/17
 */

public class VoteActivity extends AppCompatActivity {
    final static String TAG = "VoteActivity";
    private NumberPicker effortPicker;
    private int selectedEffort;

    int[] effortValues = {0, 1, 2, 3, 4, 5, 8, 13, 20, 30, 50, 100, 200};


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

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


        // Initialize voteButton
        FloatingActionButton voteButton = (FloatingActionButton) findViewById(R.id.votebutton);
        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DEBUG
                // Insert the next line to make stuff work correctly again!
                //startActivity(new Intent(VoteActivity.this, DisplayRoundResultsActivity.class));
                RoundVoteResult[] debugRes = new RoundVoteResult[2];
                debugRes[0] = new RoundVoteResult(new User("Testuser", "https://avatars0.githubusercontent.com/u/20209140?v=3"), 42);
                debugRes[1] = new RoundVoteResult(new User("Testuser 2 ", "https://avatars0.githubusercontent.com/u/20209140?v=3"), 42);
                displayResults(debugRes );
            }
        });
    }

    public void displayResults(RoundVoteResult[] results){
        ResultsDialogFragment frag = ResultsDialogFragment.newInstance(results);
        frag.show(getFragmentManager(), "dialog");
    }
}
