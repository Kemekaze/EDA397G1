package chalmers.eda397g1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import chalmers.eda397g1.R;
import chalmers.eda397g1.adapters.LobbyAdapter;
import chalmers.eda397g1.events.KickedEvent;
import chalmers.eda397g1.events.LobbyUpdateEvent;
import chalmers.eda397g1.events.RequestEvent;
import chalmers.eda397g1.events.StartGameEvent;
import chalmers.eda397g1.interfaces.RecyclerViewClickListener;
import chalmers.eda397g1.models.Session;
import chalmers.eda397g1.resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class LobbyActivity extends AppCompatActivity {
    private Boolean isHost;
    private Boolean isKicked = false;
    private static Boolean onkickedPressed = false;
    private final String TAG = "eda397.Lobby";
    private Session session;
    private int timesBackPressed = 0;

    private RecyclerView mRecyclerView;
    private LobbyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_lobby);

        // Find out if this is a lobby started by a host
        Bundle b = getIntent().getExtras();
        if(b != null) {
            isHost = b.getBoolean("isHost");
            session = (Session) b.getSerializable("session");
        } else {
            throw new RuntimeException("No bundle!");
        }
        setTitle(session.getGithub().getFullName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.startGameButton);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.users);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LobbyAdapter(listener, this, isHost);
        mRecyclerView.setAdapter(mAdapter);

        // If this is not a host make the startGame button invisible
        if( !isHost )
            fab.setVisibility(View.GONE);

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

    @Subscribe (threadMode = ThreadMode.MainThread, sticky = true)
    public void onLobbyUpdateEvent(LobbyUpdateEvent event) {
        Log.i(TAG, "onLobbyUpdateEvent");
        mAdapter.addUsers(event.getUsers());
    }

    // Start game for people in the lobby when leader starts the game
    @Subscribe (threadMode = ThreadMode.MainThread)
    public void onStartGame(StartGameEvent event) {
        Log.i(TAG, "onStartGame");
        if (event.getStatus() == 200) {
            startGame();
        } else {
            Snackbar.make(mRecyclerView, event.getStatus() + " " + event.getErrors()[0].getError() + ": " + event.getErrors()[0].getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    @Subscribe (threadMode = ThreadMode.MainThread)
    public void onKicked(KickedEvent event) {
        isKicked = true;
        Log.i(TAG, "onKicked");
        final Snackbar snackbar = Snackbar.make(mRecyclerView, event.getReason(), Snackbar.LENGTH_INDEFINITE).setAction("Exit", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onkickedPressed = true;
                finish();
            }
        });
        snackbar.show();
        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    if(!onkickedPressed) {
                        snackbar.dismiss();
                        finish();
                    }
                }
            },
        5000);

    }

    private void startGame() {
        Intent intent = new Intent(LobbyActivity.this, VoteOnLowestEffortActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("session", session);
        intent.putExtras(b);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        timesBackPressed++;
        if(timesBackPressed > 1){
            EventBus.getDefault().post(new RequestEvent(Constants.SocketEvents.SESSION_LEAVE));
            finish();
        }else{
            Snackbar.make(mRecyclerView, "Press one more time to leave", Snackbar.LENGTH_LONG).show();
        }

    }
    private RecyclerViewClickListener listener = new RecyclerViewClickListener() {
        @Override
        public void recycleViewListClicked(View v, int position) {
            Log.i("RecycleViewListClicked", "position: " + position);

        }
    };

}
