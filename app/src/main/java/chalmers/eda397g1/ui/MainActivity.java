package chalmers.eda397g1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import chalmers.eda397g1.adapters.AvailableGamesAdapter;
import chalmers.eda397g1.events.AvailableSessionsEvent;
import chalmers.eda397g1.events.JoinSessionEvent;
import chalmers.eda397g1.events.RequestEvent;
import chalmers.eda397g1.interfaces.RecyclerViewClickListener;
import chalmers.eda397g1.R;
import chalmers.eda397g1.models.AvailSession;
import chalmers.eda397g1.resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MainActivity extends AppCompatActivity{

    private String TAG = "eda397.Main";
    private RecyclerView mRecyclerView;
    private AvailableGamesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mEmptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.availabe_games);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AvailableGamesAdapter(listener, this);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton hostGameButton = (FloatingActionButton) findViewById(R.id.hostGameButton);
        hostGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                startActivity(new Intent(MainActivity.this, ChooseRepoProjectActivity.class));
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        requestAvailGames();
        mRecyclerView.setClickable(true);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void requestAvailGames() {
        RequestEvent event = new RequestEvent(Constants.SocketEvents.AVAILABLE_SESSIONS);
        EventBus.getDefault().post(event);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onReceiveAvailableGames(AvailableSessionsEvent event){
        Log.d("-->", "onReceiveAvailableGames:");
        List<AvailSession> availSessions = event.getAvailableGames();
        if (availSessions.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
        mAdapter.addGames(availSessions);
    }

    private RecyclerViewClickListener listener = new RecyclerViewClickListener() {
        @Override
        public void recycleViewListClicked(View v, int position) {
            Log.i("RecycleViewListClicked", "position: " + position);

            AvailSession availSession = mAdapter.getGame(position);

            JSONObject query = new JSONObject();
            try {
                query.put("game_id", availSession.getSessionId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            EventBus.getDefault().post(new RequestEvent(Constants.SocketEvents.SESSION_JOIN, query));
            mRecyclerView.setClickable(false);
            // wait for response from server. Then response handled in 'onJoinSessionEvent'
        }
    };

    @Subscribe (threadMode = ThreadMode.MainThread)
    public void onJoinSessionEvent(JoinSessionEvent event) {
        Log.i(TAG, "onJoinSessionEvent");
        Intent intent = new Intent(MainActivity.this, LobbyActivity.class);
        Bundle b = new Bundle();
        b.putBoolean("isHost", false);
        b.putSerializable("session", event.getSession());
        intent.putExtras(b);
        startActivity(intent);
    }

}
