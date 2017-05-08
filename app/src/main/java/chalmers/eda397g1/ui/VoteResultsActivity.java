package chalmers.eda397g1.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import chalmers.eda397g1.R;

/**
 * Created by Gustav Blide 04/04/17
 */
public class VoteResultsActivity extends AppCompatActivity {

    //intended to display user stories/issues to be solved, and their voted values
    String[] testArray = {"First story", "Second story", "Third story", "Fourth story"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_results); // for now, a view with hard-coded text views


       /* ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, testArray);

        ListView voteResultsView = (ListView) findViewById(R.id.voteResultsList);
        voteResultsView.setAdapter(adapter); */
    }
}

/* An idea for later development: have a 'votedUserStory' object that has the variables businessValue, votedEffort and name.
   An array with many of these stories could then be used to display the info about the stories,
   visually similar to what is dummy-created in the view now.
 */
