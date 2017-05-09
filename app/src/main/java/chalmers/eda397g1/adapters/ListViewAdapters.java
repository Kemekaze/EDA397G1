package chalmers.eda397g1.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import chalmers.eda397g1.R;

import static chalmers.eda397g1.resources.Constants.BUSINESS_VALUE_COLUMN;
import static chalmers.eda397g1.resources.Constants.EFFORT_COLUMN;
import static chalmers.eda397g1.resources.Constants.ISSUE_NAME_COLUMN;

/**
 * Created by fredrikhansson on 09/05/17.
 */

public class ListViewAdapters extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;

    public ListViewAdapters(Activity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub



        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.vote_results_row, null);

            txtFirst=(TextView) convertView.findViewById(R.id.businessValue);
            txtSecond=(TextView) convertView.findViewById(R.id.effort);
            txtThird=(TextView) convertView.findViewById(R.id.issueName);


        }

        HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get(BUSINESS_VALUE_COLUMN));
        txtSecond.setText(map.get(EFFORT_COLUMN));
        txtThird.setText(map.get(ISSUE_NAME_COLUMN));


        return convertView;
    }

}

