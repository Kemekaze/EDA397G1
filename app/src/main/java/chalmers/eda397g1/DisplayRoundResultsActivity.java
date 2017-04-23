package chalmers.eda397g1;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import chalmers.eda397g1.Adapters.RoundResultAdapter;

public class DisplayRoundResultsActivity extends ListActivity {

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

        final RoundResultAdapter adapter = new RoundResultAdapter(this, dummyPlayers, dummyVotes);

        setListAdapter(adapter);

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
