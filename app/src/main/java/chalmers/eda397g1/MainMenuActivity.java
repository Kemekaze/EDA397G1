package chalmers.eda397g1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


/**
 * Created by Jesper Kjellqvist 03/04/17
 */
public class MainMenuActivity extends AppCompatActivity
{
    String[] array = {"henriknumes game", "fredrik1337s game", "Kemekazes game", "gustavblides game",
            "barajagmartins game", "lightwalks game", "ziweiSWs game", "blablas game", "blablas game"
            , "blablas game", "blablas game"};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_mainmenulist, array);

        ListView gameListView = (ListView) findViewById(R.id.availabe_games);
        gameListView.setAdapter(adapter);

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

}
