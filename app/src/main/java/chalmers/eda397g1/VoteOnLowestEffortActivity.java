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
import android.widget.Toast;

import chalmers.eda397g1.Events.RequestEvent;
import chalmers.eda397g1.Resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.socket.emitter.Emitter;

public class VoteOnLowestEffortActivity extends AppCompatActivity {
    private static final String TAG = "eda397 - VoteOnLowestEffortActivity";


    private Button voteButton;
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
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_vote_on_lowest_effort);

        EventBus.getDefault().post(new RequestEvent(Constants.SocketEvents.REQUEST_BACKLOG_ITEMS));


        // Temporary vote button
        voteButton = (Button) findViewById(R.id.button1);

        //finds the list in the activity, creates an adapter and sets the adapter and the hardcoded data to it
        issueListView = (ListView) findViewById(R.id.issueList);

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

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onResponseEvent(String[] event){
        Log.i(TAG, "onResponseEvent");
        voteIssues = event;
    }

}
