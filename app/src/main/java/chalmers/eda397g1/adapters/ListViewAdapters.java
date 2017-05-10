package chalmers.eda397g1.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
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

    private static class ViewHolder{
        TextView businessValue;
        TextView effort;
        TextView issueName;
    }
    public ListViewAdapters(Activity activity, ArrayList<BacklogItem> list){
        super(activity, R.layout.vote_results_row, list);
        this.activity=activity;
        this.list=list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView ==  null){
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.vote_results_row,null);
            ViewHolder holder = new ViewHolder();
            holder.businessValue = (TextView) rowView.findViewById(R.id.businessValue);
            holder.effort = (TextView) rowView.findViewById(R.id.effort);
            holder.issueName = (TextView) rowView.findViewById(R.id.issueName);
            rowView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        BacklogItem b = (BacklogItem) getItem(position);
        holder.businessValue.setText(Integer.toString(b.getBusinessValue()));
        holder.issueName.setText(b.getTitle());
        holder.effort.setText(Integer.toString(b.getEffortValue()));
        return rowView;
    }
}

