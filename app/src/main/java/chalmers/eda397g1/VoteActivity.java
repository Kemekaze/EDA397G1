package chalmers.eda397g1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Jesper Kjellqvist 03/04/17
 */
public class VoteActivity extends AppCompatActivity
{
    private Button voteButton;

    String[] array = {"0", "1", "2", "3", "4", "5", "8", "13", "20", "30", "50", "100", "200"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        voteButton = (Button) findViewById(R.id.votebutton);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_mainmenulist, array);
        ListView listView = (ListView) findViewById(R.id.priorities);
        listView.setAdapter(adapter);

        // Initialize voteButton
        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VoteActivity.this, LoginActivity.class));
            }
        });
    }
}
