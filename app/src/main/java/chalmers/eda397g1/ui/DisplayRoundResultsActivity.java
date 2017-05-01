package chalmers.eda397g1.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import chalmers.eda397g1.adapters.RoundResultAdapterOld;
import chalmers.eda397g1.R;

public class DisplayRoundResultsActivity extends AppCompatActivity
{

    String[] dummyPlayers = new String[]{
            "Player 1",
            "Player 2",
            "Player 3",
            "Player 4",
            "Player 5",
            "Player 6",
    };

    int[] dummyVotes = new int[] {10, 20, 5, 11, 10, 10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_round_result);

        final RoundResultAdapterOld adapter = new RoundResultAdapterOld(this, dummyPlayers, dummyVotes);

        ListView voteResult = (ListView) findViewById(R.id.list);
        voteResult.setAdapter(adapter);

        /*
        ListView listView = getListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(DisplayRoundResultsActivity.this, VoteResultsActivity.class));
            }
        });
        */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab5);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DisplayRoundResultsActivity.this, VoteResultsActivity.class));
            }
        });

    }
}
