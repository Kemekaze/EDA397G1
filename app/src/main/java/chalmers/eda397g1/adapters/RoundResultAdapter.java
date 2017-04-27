package chalmers.eda397g1.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import chalmers.eda397g1.R;

public class RoundResultAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] playerNames;
    private final int[] voteResults;

    public RoundResultAdapter(@NonNull Context context, String[] playerNames, int[] voteResults) {
        super(context, R.layout.listview_element_round_vote, playerNames);
        this.context = context;
        this.playerNames = playerNames;
        this.voteResults = voteResults;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.listview_element_round_vote, parent, false);

        TextView playerNameText = (TextView) rowView.findViewById(R.id.playername);
        TextView effortText = (TextView) rowView.findViewById(R.id.effort);

        playerNameText.setText(playerNames[position]);
        effortText.setText(Integer.toString(voteResults[position]));

        return rowView;
    }
}
