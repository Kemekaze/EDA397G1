package chalmers.eda397g1.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import chalmers.eda397g1.R;
import chalmers.eda397g1.models.BacklogItem;

import static chalmers.eda397g1.resources.Constants.BUSINESS_VALUE_COLUMN;
import static chalmers.eda397g1.resources.Constants.EFFORT_COLUMN;
import static chalmers.eda397g1.resources.Constants.ISSUE_NAME_COLUMN;

/**
 * Created by fredrikhansson on 09/05/17.
 */

public class ListViewAdapters extends ArrayAdapter {

    public ArrayList<BacklogItem> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;

    public ListViewAdapters(Activity activity, ArrayList<BacklogItem> list){
        super(activity, R.layout.vote_results_row, list);
        this.activity=activity;
        this.list=list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub


        LayoutInflater inflater = activity.getLayoutInflater();
        View v = inflater.inflate(R.layout.vote_results_row,null);
        TextView businessValue = (TextView) v.findViewById(R.id.businessValue);
        TextView effort = (TextView) v.findViewById(R.id.effort);
        TextView issueName = (TextView) v.findViewById(R.id.issueName);

        BacklogItem b = (BacklogItem) getItem(position);
        businessValue.setText(Integer.toString(b.getBusinessValue()));

return v;
    }
}

