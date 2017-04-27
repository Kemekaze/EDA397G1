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

import chalmers.eda397g1.events.CreateSessionEvent;
import chalmers.eda397g1.events.JoinSessionEvent;
import chalmers.eda397g1.events.LobbyUpdateEvent;
import chalmers.eda397g1.events.RequestEvent;
import chalmers.eda397g1.events.StartGameEvent;
import chalmers.eda397g1.R;
import chalmers.eda397g1.models.Session;
import chalmers.eda397g1.models.User;
import chalmers.eda397g1.resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class LobbyActivity extends AppCompatActivity {
    private ListView playerListView;
    private Boolean isHost;
    private final String TAG = "eda397.Lobby";
    private List<String> players;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_lobby);

        players = new ArrayList<>();

        // Find out if this is a lobby started by a host
        Bundle b = getIntent().getExtras();
        if(b != null) {
            isHost = b.getBoolean("isHost");
        } else {
            throw new RuntimeException("No bundle!");
        }

        playerListView = (ListView) findViewById(R.id.playerList);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);

        // If this is not a host make the startGame button invisible
        if( !isHost )
            fab.setVisibility(View.GONE);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, players);

        playerListView.setAdapter(adapter);

        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new RequestEvent(Constants.SocketEvents.SESSION_START));
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
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop()");
        EventBus.getDefault().unregister(this);
    }

    @Subscribe (sticky = true)
    public void onCreateSessionEvent(CreateSessionEvent event) {
        Log.i(TAG, "onCreateSessionEvent");
        session = event.getSession();
        EventBus.getDefault().post(new RequestEvent(Constants.SocketEvents.SESSION_CLIENTS)); // TODO: remove when server is working correcly
    }

    @Subscribe (sticky = true)
    public void onJoinSessionEvent(JoinSessionEvent event) {
        Log.i(TAG, "onJoinSessionEvent");
        session = event.getSession();
        EventBus.getDefault().post(new RequestEvent(Constants.SocketEvents.SESSION_CLIENTS)); // TODO: remove when server is working correcly
    }

    @Subscribe (threadMode = ThreadMode.MainThread, sticky = true)
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
        if (event.getStatus() == 200) {
            startGame();
        } else {
            Snackbar.make(playerListView, event.getStatus() + " " + event.getErrors()[0].getError() + ": " + event.getErrors()[0].getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    private void startGame() {
        Intent intent = new Intent(LobbyActivity.this, VoteOnLowestEffortActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("session", session);
        intent.putExtras(b);
        startActivity(intent);
    }

}
