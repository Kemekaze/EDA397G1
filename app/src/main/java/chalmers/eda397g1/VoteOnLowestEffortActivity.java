package chalmers.eda397g1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

public class VoteOnLowestEffortActivity extends AppCompatActivity {

    private Button voteButton;
    ListView issueListView;
    String[] voteIssues1 = new String[]{
            "value 10 issue1",
            "value 9 issue2",
            "value 8 issue3",
            "value 7 issue4",
            "value 6 issue5",
    };
    // Temporal testing the connection between projects and issues
    String[] voteIssues2 = new String[]{
            "value 5 issue6",
            "value 4 issue7",
            "value 3 issue8",
            "value 2 issue9",
            "value 1 issue10",
    };
    Map<String,String[]> projectIssueMap = new HashMap<String,String[]>();
    String chosenProject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_on_lowest_effort);

        // Temperal test the connection between project and issues
        projectIssueMap.put("Project 1", voteIssues1);
        projectIssueMap.put("Project 2", voteIssues2);
        projectIssueMap.put("Project 3", voteIssues2);

        // Temporary vote button
        voteButton = (Button) findViewById(R.id.button1);

        //finds the list in the activity, creates an adapter and sets the adapter and the hardcoded data to it
        issueListView = (ListView) findViewById(R.id.issueList);


        // Temporal test the connection between project and issures
        String chosenProject = getIntent().getStringExtra("chosenProject");
        String[] voteIssues = projectIssueMap.get(chosenProject);

        ArrayAdapter<String> voteListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, voteIssues);
        issueListView.setAdapter(voteListAdapter);

        // Initialize voteButton
        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VoteOnLowestEffortActivity.this, VoteActivity.class));
            }
        });


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
