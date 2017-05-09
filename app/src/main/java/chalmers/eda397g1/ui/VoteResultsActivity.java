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

        HashMap<String,String> temp=new HashMap<String, String>();
        temp.put(BUSINESS_VALUE_COLUMN, "BV");
        temp.put(EFFORT_COLUMN, "EFF");
        temp.put(ISSUE_NAME_COLUMN, "IssueName");

        list.add(temp);


        HashMap<String,String> temp2=new HashMap<String, String>();
        temp2.put(BUSINESS_VALUE_COLUMN, "59");
        temp2.put(EFFORT_COLUMN, "20000");
        temp2.put(ISSUE_NAME_COLUMN, "Bajsa jättelite");

        list.add(temp2);

        ListViewAdapters adapter=new ListViewAdapters(this, list);
        listView.setAdapter(adapter);

        /*
        voteResultsView = (ListView) findViewById(R.id.activity_vote_results);

        // hard-coded list for offline try
        List<ArrayList<String>> voteResultsList = new ArrayList<>();
        ArrayList<String> firstIssue = new ArrayList<>();
        firstIssue.add("3");
        firstIssue.add("5");
        firstIssue.add("Title of issue 1");
        ArrayList<String> secondIssue = new ArrayList<>();
        secondIssue.add("3");
        secondIssue.add("5");
        secondIssue.add("Title of issue 2");
        ArrayList<String> thirdIssue = new ArrayList<>();
        thirdIssue.add("3");
        thirdIssue.add("5");
        thirdIssue.add("Title of issue 3");
        voteResultsList.add(firstIssue);
        voteResultsList.add(secondIssue);
        voteResultsList.add(thirdIssue);

        //setting an adapter on voteResultsView
        ArrayAdapter voteResultsAdapter = new ArrayAdapter<>(this, R.layout.activity_vote_results, voteResultsList);
        voteResultsView.setAdapter(voteResultsAdapter);

        for(int i=0; i < voteResultsList.size(); i++) {

            voteResultsRow = (ListView) findViewById(R.id.voteResultsRow);
            ArrayList<String> templist = voteResultsList.get(i);

            //Create inflater to add things to the layout.
            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            voteResultsView = (ListView) inflater.inflate(R.layout.vote_results_row,null);

            //voteResultsView.addView(voteResultsRow);

            businessValueView = (TextView) findViewById(R.id.businessValue);
            effortView = (TextView) findViewById(R.id.effort);
            issueNameView= (TextView) findViewById(R.id.issueName);
            businessValueView.setText(templist.get(0));
            effortView.setText(templist.get(1));
            issueNameView.setText(templist.get(2));


        }*/
    }
}
