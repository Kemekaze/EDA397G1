package chalmers.eda397g1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import chalmers.eda397g1.R;
import chalmers.eda397g1.models.BacklogItem;
import chalmers.eda397g1.models.Session;

public class VoteOnLowestEffortActivity extends AppCompatActivity {
    private static final String TAG = "VoteOnLow..Activity";


    private RecyclerView mRecyclerView;
    // private SomeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Session session;
    private BacklogItem selectedBacklogItem;
    private TextView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_on_lowest_effort);
        mRecyclerView = (RecyclerView) findViewById(R.id.available_items);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mAdapter = new SomeAdapter;
        //mRecyclerView.setAdapter(mAdapter);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            session = (Session) b.getSerializable("session");
        } else {
            throw new RuntimeException("No bundle!");
        }

        setBacklogList();

        final FloatingActionButton voteButton = (FloatingActionButton) findViewById(R.id.button_vote);
        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedBacklogItem == null){
                    Snackbar alert = Snackbar.make(
                            voteButton,
                            "No item selected!",
                            Snackbar.LENGTH_LONG);
                    alert.show();
                } else {
                    startActivity(new Intent(VoteOnLowestEffortActivity.this, VoteActivity.class));
                }
            }
        });

        //finds the list in the activity, creates an adapter and sets the adapter and the hardcoded data to it
        issueListView = (ListView) findViewById(R.id.issueList);
        issueListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayAdapter<String> voteListAdapter = new ArrayAdapter<String>(VoteOnLowestEffortActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, voteIssues);
        issueListView.setAdapter(voteListAdapter);
        //new ArrayAdapter<String>(this, R.layout.)

//        final ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(ChooseRepoProjectActivity.this,
//                android.R.layout.simple_spinner_item, projectNames);


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
