package chalmers.eda397g1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.Events.LobbyUpdateEvent;
import chalmers.eda397g1.Events.RequestEvent;
import chalmers.eda397g1.Events.StartGameEvent;
import chalmers.eda397g1.Objects.User;
import chalmers.eda397g1.Resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class LobbyActivity extends AppCompatActivity {
    private ListView playerListView;
    private Button startGameButton;
    private Boolean isHost;
    private String fullName;
    private int repoID;
    private int projectID;
    private int columnID;
    private final String TAG = "Lobby";
    private List<String> players;
    private String joinedSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        players = new ArrayList<>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");

        // Find out if this is a lobby started by a host
        Bundle b = getIntent().getExtras();
        if(b != null) {
            isHost = b.getBoolean("isHost");
            fullName = b.getString("fullName");
            repoID = b.getInt("repoID");
            projectID = b.getInt("projectID");
            columnID = b.getInt("columnID");
        } else {
            throw new RuntimeException("No bundle!");
        }

        playerListView = (ListView) findViewById(R.id.playerList);
        startGameButton = (Button) findViewById(R.id.startGameButton);

        // If this is not a host make the startGame button invisible
        if( !isHost ) {
            startGameButton.setVisibility(View.GONE);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, players);

        playerListView.setAdapter(adapter);

        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

        });

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LobbyActivity.this, VoteOnLowestEffortActivity.class);
                Bundle b = new Bundle();
                b.putString("fullName", fullName);
                b.putInt("repoID", repoID);
                b.putInt("projectID", projectID);
                b.putInt("columnID", columnID);
                intent.putExtras(b);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart()");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop()");
        EventBus.getDefault().unregister(this);
    }

    /*@Subscribe (threadMode = ThreadMode.MainThread, sticky = true) // TODO: not sure about sticky, just want to make sure it catches it
    public void onGameJoinEvent(GameJoinEvent event) {
        Log.i(TAG, "onGameJoinEvent");
        joinedSessionId = event.getCurrentGame().getSessionId();
    }*/

    @Subscribe (threadMode = ThreadMode.MainThread)
    public void onLobbyUpdateEvent(LobbyUpdateEvent event) {
        Log.i(TAG, "onLobbyUpdateEvent");

        players.clear();
        for (User user : event.getUsers()) {
            players.add(user.getLogin());
        }

        ( (ArrayAdapter<String>) playerListView.getAdapter()).notifyDataSetChanged();
    }

    // Start game for people in the lobby when leader starts the game
    @Subscribe (threadMode = ThreadMode.MainThread)
    public void onStartGame(StartGameEvent event) {
        Log.i(TAG, "onStartGame");
        if (!isHost) {
            // if game that was started is equal to the one of the lobby, then switch activity
            if (event.getCurrentGame().getSessionId().equals(joinedSessionId)) {
                Intent intent = new Intent(LobbyActivity.this, VoteOnLowestEffortActivity.class);
                startActivity(intent);
            }
        }
    }
}
