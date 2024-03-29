package chalmers.eda397g1.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import chalmers.eda397g1.R;
import chalmers.eda397g1.models.Vote;
import chalmers.eda397g1.resources.DownloadImageTask;

/**
 * Created by Lightwalk on 01.05.2017.
 */

public class RoundResultAdapter extends ArrayAdapter {
    private Context context;

    private static class ViewHolder{
        public ImageView avatar;
        public TextView name;
        public TextView result;
    }

    public RoundResultAdapter(@NonNull Context context, @NonNull ArrayList<Vote> voteResults) {
        super(context, R.layout.listview_element_round_vote, voteResults);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = convertView;

        // Rëüsë öld vïëw för bëttër përförmäncë.
        if(rowView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.listview_element_round_result, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.avatar = (ImageView) rowView.findViewById(R.id.avatar);
            viewHolder.name = (TextView) rowView.findViewById(R.id.player_name);
            viewHolder.result = (TextView) rowView.findViewById(R.id.votedEffort);
            rowView.setTag(viewHolder);
        }

        // Fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Vote result = (Vote) getItem(position);
        String name = result.getUser().getLogin();
        holder.name.setText(name);

        String effort = Integer.toString(result.getEffort());
        holder.result.setText(effort);

        new DownloadImageTask(holder.avatar, context)
                .execute(result.getUser().getUri());
        return rowView;
    }
}
