package chalmers.eda397g1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Gustav Blide 04/04/17
 */
public class VoteResultsActivity extends AppCompatActivity {

    String[] testArray = {"First story", "Second story", "Third story", "Fourth story",
            "Millionth story"}; //intended to display user stories/issues to be solved, and their voted values

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, testArray);

        ListView voteResultsView = (ListView) findViewById(R.id.voteResultsList);
        voteResultsView.setAdapter(adapter);
    }
}
