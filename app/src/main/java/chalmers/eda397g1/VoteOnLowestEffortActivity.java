package chalmers.eda397g1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class VoteOnLowestEffortActivity extends AppCompatActivity {

    ListView issueListView;
    String[] voteIssues = new String[]{
            "value 10 issue1",
            "value 9 issue2",
            "value 8 issue3",
            "value 7 issue4",
            "value 6 issue5",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_on_lowest_effort);


        //finds the list in the activity, creates an adapter and sets the adapter and the hardcoded data to it
        issueListView = (ListView) findViewById(R.id.issueList);

        ArrayAdapter<String> voteListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, voteIssues);
        issueListView.setAdapter(voteListAdapter);


        issueListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("item", (String) adapterView.getItemAtPosition(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //TODO
            }
        });


    }

}
