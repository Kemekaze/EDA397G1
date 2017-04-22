package chalmers.eda397g1;

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

import java.util.List;

import chalmers.eda397g1.Adapters.AvailableGamesAdapter;
import chalmers.eda397g1.Events.AvailableGamesEvent;
import chalmers.eda397g1.Events.RequestEvent;
import chalmers.eda397g1.Objects.Game;
import chalmers.eda397g1.Resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MainActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private AvailableGamesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.availabe_games);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AvailableGamesAdapter();
        mRecyclerView.setAdapter(mAdapter);



        /*gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Start a lobby as a client
                Intent intent = new Intent(MainActivity.this, LobbyActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("isHost", false);
                intent.putExtras(b);
                startActivity(intent);
            }
        });*/
        FloatingActionButton hostGameButton = (FloatingActionButton) findViewById(R.id.hostGameButton);
        hostGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChooseRepoProjectActivity.class));
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        EventBus.getDefault().post(new RequestEvent(Constants.SocketEvents.AVAILABLE_GAMES));
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
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void requestAvailGames() {
        RequestEvent event = new RequestEvent(Constants.SocketEvents.AVAILABLE_GAMES);
        EventBus.getDefault().post(event);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onReceiveAvailableGames(AvailableGamesEvent event){
        Log.d("-->", "onReceiveAvailableGames:");
        List<Game> games = event.getAvailableGames();
        mAdapter.addGames(games);
    }
}
