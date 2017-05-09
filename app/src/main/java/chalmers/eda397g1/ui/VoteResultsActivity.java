package chalmers.eda397g1.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import chalmers.eda397g1.R;

/**
 * Created by Gustav Blide 04/04/17
 */
public class VoteResultsActivity extends AppCompatActivity {

    //intended to display user stories/issues to be solved, and their voted values

    ListView voteResultsView;
    LinearLayout voteResultsRow;
    TextView businessValueView = (TextView) findViewById(R.id.businessValue);
    TextView EffortView = (TextView) findViewById(R.id.effort);
    TextView IssueNameView = (TextView) findViewById(R.id.issueName);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_results);
        voteResultsView = (ListView) findViewById(R.id.activity_vote_results);

        // hard-coded list for offline try
        List<ArrayList<String>> voteResultsList = new ArrayList<>();
        ArrayList<String> firstIssue = new ArrayList<>();
        firstIssue.add("3");
        firstIssue.add("5");
        firstIssue.add("Title of issue 1");
        ArrayList<String> secondIssue = new ArrayList<>();
        secondIssue.add("3");
        secondIssue.add("5");
        secondIssue.add("Title of issue 2");
        ArrayList<String> thirdIssue = new ArrayList<>();
        thirdIssue.add("3");
        thirdIssue.add("5");
        thirdIssue.add("Title of issue 3");
        voteResultsList.add(firstIssue);
        voteResultsList.add(secondIssue);
        voteResultsList.add(thirdIssue);

        //setting an adapter on voteResultsView
        ArrayAdapter voteResultsAdapter = new ArrayAdapter<>(this, R.layout.activity_vote_results, voteResultsList);
        voteResultsView.setAdapter(voteResultsAdapter);

        for(int i=0; i < voteResultsList.size(); i++) {

            voteResultsRow = (LinearLayout) findViewById(R.id.voteResultsRow);
            ArrayList<String> templist = voteResultsList.get(i);


            businessValueView.setText(templist.get(0));
            EffortView.setText(templist.get(1));
            IssueNameView.setText(templist.get(2));

            voteResultsView.addView(voteResultsRow);
        }
    }
}
