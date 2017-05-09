package chalmers.eda397g1.ui;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import chalmers.eda397g1.R;
import chalmers.eda397g1.adapters.ListViewAdapters;
import chalmers.eda397g1.models.BacklogItem;
import chalmers.eda397g1.models.Session;

import static chalmers.eda397g1.resources.Constants.BUSINESS_VALUE_COLUMN;
import static chalmers.eda397g1.resources.Constants.EFFORT_COLUMN;
import static chalmers.eda397g1.resources.Constants.ISSUE_NAME_COLUMN;

/**
 * Created by Gustav Blide 04/04/17
 */
public class VoteResultsActivity extends AppCompatActivity {

    //intended to display user stories/issues to be solved, and their voted values


    private ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_results);

        ListView listView=(ListView)findViewById(R.id.listView1);

        //Get stuff from server

       Bundle b = getIntent().getExtras();
        Session session = (Session) b.get("Session");

        ArrayList<BacklogItem> backlogItemList = session.getGithub().getBacklogItems();

        ListViewAdapters adapter=new ListViewAdapters(this, backlogItemList);
        listView.setAdapter(adapter);

        FloatingActionButton restartButton = (FloatingActionButton) findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                //Temporary
                startActivity(new Intent(VoteResultsActivity.this, LoginActivity.class));
            }
        });






    }
}
