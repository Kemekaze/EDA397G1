package chalmers.eda397g1.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import chalmers.eda397g1.R;
import chalmers.eda397g1.adapters.ListViewAdapters;
import chalmers.eda397g1.models.Session;

import static chalmers.eda397g1.resources.Constants.BUSINESS_VALUE_COLUMN;
import static chalmers.eda397g1.resources.Constants.EFFORT_COLUMN;
import static chalmers.eda397g1.resources.Constants.ISSUE_NAME_COLUMN;

/**
 * Created by Gustav Blide 04/04/17
 */
public class VoteResultsActivity extends AppCompatActivity {

    //intended to display user stories/issues to be solved, and their voted values

    ListView voteResultsView;
    ListView voteResultsRow;
    TextView businessValueView;
    TextView effortView;
    TextView issueNameView;

    private ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_results);


        ListView listView=(ListView)findViewById(R.id.listView1);

        list=new ArrayList<HashMap<String,String>>();

        HashMap<String,String> headlines=new HashMap<String, String>();
        headlines.put(BUSINESS_VALUE_COLUMN, "BV");
        headlines.put(EFFORT_COLUMN, "EFF");
        headlines.put(ISSUE_NAME_COLUMN, "IssueName");

        list.add(headlines);


        //Get stuff from server

       Bundle b = getIntent().getExtras();
        Session session = (Session) b.get("Session");

        session



        ListViewAdapters adapter=new ListViewAdapters(this, list);
        listView.setAdapter(adapter);


    }
}
