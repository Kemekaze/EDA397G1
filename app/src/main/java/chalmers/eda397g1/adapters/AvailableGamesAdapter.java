package chalmers.eda397g1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.interfaces.RecyclerViewClickListener;
import chalmers.eda397g1.models.AvailSession;
import chalmers.eda397g1.R;
import chalmers.eda397g1.resources.DownloadImageTask;



/**
 * Created by elias on 2017-04-22.
 */

public class AvailableGamesAdapter extends RecyclerView.Adapter<AvailableGamesAdapter.ViewHolder>{
    private String TAG = "AvailableGamesAdapter";
    private List<AvailSession> availSessions = new ArrayList<>();
    private Context context;
    private static RecyclerViewClickListener itemListener;

    public AvailableGamesAdapter(RecyclerViewClickListener itemListener, Context context) {
        this.itemListener = itemListener;
        this.context = context;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mName;
        public TextView mHost;
        public ImageView mAvatar;
        public View view;
        public ViewHolder(View v) {
            super(v);
            this.mName = (TextView) v.findViewById(R.id.name);
            this.mHost = (TextView) v.findViewById(R.id.host);
            this.mAvatar = (ImageView) v.findViewById(R.id.avatar);
            this.view = v;
            this.view.setClickable(true);
            v.setOnClickListener(this);
            Log.i("AvailableGamesAdapter", "ViewName: " + v.toString());
        }

        @Override
        public void onClick(View v) {
            Log.i("AvailableGamesAdapter", "komden hit: " );
            itemListener.recycleViewListClicked(v, this.getAdapterPosition());

        }
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_game, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        AvailSession availSession = availSessions.get(pos);

        holder.mName.setText(availSession.getName());
        Log.d(TAG, "Name: "+ availSession.getName());
        holder.mHost.setText(availSession.getHost().getLogin());
        new DownloadImageTask(holder.mAvatar,context)
                .execute(availSession.getHost().getUri());
    }

    @Override
    public int getItemCount() {
        return availSessions.size();
    }


    public void addGames(List<AvailSession> availSessions){
        this.availSessions = availSessions;
        this.notifyDataSetChanged();
    }
    public void addGame(AvailSession availSession){
        this.availSessions.add(availSession);
        this.notifyDataSetChanged();
    }

    public AvailSession getGame(int index){
        return availSessions.get(index);
    }


}
