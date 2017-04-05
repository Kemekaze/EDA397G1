package chalmers.eda397g1;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import chalmers.eda397g1.adapter.RoundResultAdapter;

public class DisplayRoundResultsActivity extends ListActivity {
    ListView listView;

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

        ListView listView = getListView();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(DisplayRoundResultsActivity.this, VoteResultsActivity.class));
            }
        });

    }
}
