package chalmers.eda397g1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import chalmers.eda397g1.Events.AvailableGamesEvent;
import chalmers.eda397g1.Events.RequestEvent;
import chalmers.eda397g1.Events.UserProjectsEvent;
import chalmers.eda397g1.Objects.Game;
import chalmers.eda397g1.Objects.Project;
import chalmers.eda397g1.Resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


/**
 * Created by Jesper Kjellqvist 03/04/17
 */
public class MainMenuActivity extends AppCompatActivity
{
    List<Game> gameList = new ArrayList<>();
    List<String> gameNames = new ArrayList<>();
    Game selectedGame = null;

    ArrayAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         listAdapter = new ArrayAdapter<String>(this, R.layout.activity_mainmenulist, gameNames);

        ListView gameListView = (ListView) findViewById(R.id.availabe_games);
        gameListView.setAdapter(listAdapter);

        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Start a lobby as a client
                Intent intent = new Intent(MainMenuActivity.this, LobbyActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("isHost", false);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        Button hostGameButton = (Button) findViewById(R.id.hostGameButton);
        hostGameButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainMenuActivity.this, ChooseRepoProjectActivity.class));

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        gameList = event.getAvailableGames();
        gameNames.clear();
        if (gameList.isEmpty()) {
            gameNames.add("No Games available.");
        } else {
            for(Game g : gameList){
                gameNames.add(g.getName());
            }
            listAdapter.notifyDataSetChanged();
        }

    }

}
