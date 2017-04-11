package chalmers.eda397g1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class LobbyActivity extends AppCompatActivity {
    ListView playerListView;
    Button startGameButton;
    Boolean isHost;
    TextView projectHeadline;
    TextView repoHeadline;

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
        setContentView(R.layout.activity_lobby);

        String chosenRepo = getIntent().getStringExtra("chosenRepo");
        String chosenProject = getIntent().getStringExtra("chosenProject");

        // Find out if this is a lobby started by a host
        Bundle b = getIntent().getExtras();
        if(b != null)
            isHost = b.getBoolean("isHost");
        repoHeadline = (TextView) findViewById(R.id.repoHeadline);
        projectHeadline = (TextView) findViewById(R.id.projectHeadline);
        playerListView = (ListView) findViewById(R.id.playerList);
        startGameButton = (Button) findViewById(R.id.startGameButton);

        // Add headline to show the chosenRepo and chosenProject
        repoHeadline.setText(chosenRepo);
        projectHeadline.setText(chosenProject);

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
                String chosenProject = getIntent().getStringExtra("chosenProject");

                Intent intent = new Intent(LobbyActivity.this, VoteOnLowestEffortActivity.class);
                intent.putExtra("chosenProject", chosenProject);
                startActivity(intent);
            }
        });
    }

}
