package chalmers.eda397g1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import chalmers.eda397g1.Objects.Card;
import chalmers.eda397g1.Objects.Label;
import chalmers.eda397g1.Resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class VoteOnLowestEffortActivity extends AppCompatActivity {
    private static final String TAG = "VoteOnLow..Activity";


    private Button voteButton;
    ListView issueListView;
    List<Card> cards;
    private List<String> voteIssues = new ArrayList<>();
    private String fullName;
    private int projectID;
    private int columnID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_on_lowest_effort);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            fullName = b.getString("fullName");
            projectID = b.getInt("projectID");
            columnID = b.getInt("columnID");
        } else {
            throw new RuntimeException("No bundle!");
        }



        // Temporary vote button
        voteButton = (Button) findViewById(R.id.button1);

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
                startActivity(new Intent(VoteOnLowestEffortActivity.this, VoteActivity.class));
            }
        });


        issueListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //adapterView.setSelection(i);
                //view.setBackgroundColor(Color.parseColor("#FF0000"));
                Log.v("item", (String) adapterView.getItemAtPosition(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //TODO
            }
        });


    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart()");
        EventBus.getDefault().register(this);
        requestColumnCardsData();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onReceiveBacklogItemsData(CardsEvent event){
        Log.i(TAG, String.valueOf(event.getStatus()));
        Log.i(TAG, event.getData().toString());
        JSONArray data = (JSONArray) event.getData();
        cards = new ArrayList<>();
        LinkedList<Label> labels = new LinkedList<Label>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonCard = data.optJSONObject(i);
            labels.clear();
            try {
                JSONArray jsonLabels = jsonCard.getJSONArray("labels");
                for (int j = 0; j < jsonLabels.length(); j++) {
                    JSONObject jsonLabel = jsonLabels.optJSONObject(j);
                    labels.addLast(new Label(jsonLabel.getInt("id"),
                            jsonLabel.getString("name")));
                }
                Card card = new Card(jsonCard.getInt("card_id"),
                        jsonCard.getInt("column_id"),
                        jsonCard.getInt("issue_id"),
                        jsonCard.getInt("number"),
                        jsonCard.getString("title"),
                        jsonCard.getString("body"),
                        jsonCard.getString("state")
                );
                Label[] labelsArray = new Label[labels.size()];
                labels.toArray(labelsArray);
                card.setLabels(labelsArray);
                cards.add(card);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        List<String> tmp = new ArrayList<String>(cards.size());
        for (int i = 0; i < cards.size(); i++) {
            int businessValue = cards.size() - i;
            tmp.add(cards.get(i).getTitle() +
                    "\n- " + getString(R.string.business_value) + ": " + businessValue +
                    "\n- " + getString(R.string.comment) + ": "+ cards.get(i).getBody());
        }

        voteIssues.clear();
        voteIssues.addAll(tmp);
        ( (ArrayAdapter<String>) issueListView.getAdapter()).notifyDataSetChanged();

    }

    private void requestColumnCardsData() {
        JSONObject query = new JSONObject();
        try {
            query.put("full_name", fullName);
            query.put("project_id", projectID);
            query.put("column_id", columnID);
            Log.i(TAG, ""+fullName);
            Log.i(TAG, ""+projectID);
            Log.i(TAG, ""+columnID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestEvent event = new RequestEvent(Constants.SocketEvents.COLUMN_CARDS, query);
        EventBus.getDefault().post(event);
    }

}
