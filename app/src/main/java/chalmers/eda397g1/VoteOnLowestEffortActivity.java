package chalmers.eda397g1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import chalmers.eda397g1.Events.CardsEvent;
import chalmers.eda397g1.Events.RequestEvent;
import chalmers.eda397g1.Objects.BacklogItem;
import chalmers.eda397g1.Objects.Card;
import chalmers.eda397g1.Objects.Label;
import chalmers.eda397g1.Objects.Session;
import chalmers.eda397g1.Resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import io.socket.emitter.Emitter;

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
                    startActivity(new Intent(VoteOnLowestEffortActivity.this, VoteActivity.class));
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
            voteIssues.add(
                    i.getTitle() + "\nBusiness Value: " + i.getBusinessValue()
            );
        }
    }

}
