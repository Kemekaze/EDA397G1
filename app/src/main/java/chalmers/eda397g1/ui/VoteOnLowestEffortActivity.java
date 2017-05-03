package chalmers.eda397g1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.R;
import chalmers.eda397g1.models.BacklogItem;
import chalmers.eda397g1.models.Session;

public class VoteOnLowestEffortActivity extends AppCompatActivity {
    private static final String TAG = "VoteOnLow..Activity";


    ListView issueListView;
    private List<String> voteIssues = new ArrayList<>();
    private Session session;
    private BacklogItem selectedBacklogItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_on_lowest_effort);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            session = (Session) b.getSerializable("session");
        } else {
            throw new RuntimeException("No bundle!");
        }

        setBacklogList();

        // Temporary vote button
        final FloatingActionButton voteButton = (FloatingActionButton) findViewById(R.id.fab4);

        //finds the list in the activity, creates an adapter and sets the adapter and the hardcoded data to it
        issueListView = (ListView) findViewById(R.id.issueList);
        issueListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayAdapter<String> voteListAdapter = new ArrayAdapter<String>(VoteOnLowestEffortActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, voteIssues);
        issueListView.setAdapter(voteListAdapter);
        //new ArrayAdapter<String>(this, R.layout.)

//        final ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(ChooseRepoProjectActivity.this,
//                android.R.layout.simple_spinner_item, projectNames);

        // Initialize voteButton
        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Send vote to server and wait for answer to go to next activity
                if(selectedBacklogItem == null){
                    Snackbar alert = Snackbar.make(
                            voteButton,
                            "No item selected!",
                            Snackbar.LENGTH_LONG);
                    alert.show();
                } else {
                    Intent i = new Intent(VoteOnLowestEffortActivity.this, VoteActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("session",session);
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        });


        issueListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "Select BacklogItem: " + i);
                selectedBacklogItem = session.getGithub().getBacklogItems().get(i);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart()");
        //EventBus.getDefault().register(this);
        //requestColumnCardsData();
    }

    @Override
    public void onStop() {
        //EventBus.getDefault().unregister(this);
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    private void setBacklogList(){
        List<BacklogItem> items = session.getGithub().getBacklogItems();
        voteIssues.clear();
        for(BacklogItem i : items){
            String content = i.getTitle();
            content += "\nBusiness Value: " + i.getBusinessValue();
            Log.d(TAG, "Body: " + i.getBody());
            if(!i.getBody().isEmpty())
                content += "\n" + i.getBody();
            voteIssues.add(content);
        }
    }

}
