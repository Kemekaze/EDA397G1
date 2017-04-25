package chalmers.eda397g1.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.Interfaces.RecyclerViewClickListener;
import chalmers.eda397g1.Objects.Game;
import chalmers.eda397g1.R;
import chalmers.eda397g1.Resources.DownloadImageTask;



/**
 * Created by elias on 2017-04-22.
 */

public class AvailableGamesAdapter extends RecyclerView.Adapter<AvailableGamesAdapter.ViewHolder>{
    private String TAG = "AvailableGamesAdapter";
    private List<Game> games = new ArrayList<>();
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
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
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
        holder.view.setClickable(true);
        holder.view.setOnClickListener(clickListener);
        Game game = games.get(pos);

        holder.mName.setText(game.getName());
        Log.d(TAG, "Name: "+game.getName());
        holder.mHost.setText(game.getHost().getLogin());
        new DownloadImageTask(holder.mAvatar,true)
                .execute(game.getHost().getUri());
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public void addGames(List<Game> games){
        this.games = games;
        this.notifyDataSetChanged();
    }
    public void addGame(Game game){
        this.games.add(game);
        this.notifyDataSetChanged();
    }

}
