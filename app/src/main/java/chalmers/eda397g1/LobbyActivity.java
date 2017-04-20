package chalmers.eda397g1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import chalmers.eda397g1.Events.PlayerJoinedLobbyEvent;
import chalmers.eda397g1.Events.StartGameEvent;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class LobbyActivity extends AppCompatActivity {
    ListView playerListView;
    Button startGameButton;
    Boolean isHost;

    // Dummy Players
    String[] players = new String[] {"Player 1",
            "Player 2",
            "Player 3",
            "Player 4",
            "Player 5",
            "Player 6",
            "Player 7",
            "Player 8",
            "Player 9",
            "Player 10",
            "Player 11",
            "Player 12",
            "Player 13",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_lobby);

        // Find out if this is a lobby started by a host
        Bundle b = getIntent().getExtras();
        if(b != null)
            isHost = b.getBoolean("isHost");

        playerListView = (ListView) findViewById(R.id.playerList);
        startGameButton = (Button) findViewById(R.id.startGameButton);

        // If this is not a host make the startGame button invisible
        if( !isHost )
            startGameButton.setVisibility(View.GONE);


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
                startActivity(intent);
            }
        });
    }

    @Subscribe
    public void onPlayerJoinedEvent(PlayerJoinedLobbyEvent event){
        // TODO: Add player
    }

    @Subscribe
    public void onStartGameEvent(StartGameEvent event){
        Intent intent = new Intent(LobbyActivity.this, VoteOnLowestEffortActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }



}
