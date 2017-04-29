package chalmers.eda397g1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.R;
import chalmers.eda397g1.interfaces.RecyclerViewClickListener;
import chalmers.eda397g1.models.User;
import chalmers.eda397g1.resources.DownloadImageTask;


/**
 * Created by elias on 2017-04-22.
 */

public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.ViewHolder>{
    private String TAG = "LobbyAdapter";
    private List<User> users = new ArrayList<>();
    private Context context;
    private static RecyclerViewClickListener itemListener;
    private static boolean isHost;

    public LobbyAdapter(RecyclerViewClickListener itemListener, Context context, boolean isHost) {
        this.itemListener = itemListener;
        this.context = context;
        this.isHost = isHost;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mName;
        public ImageView mAvatar;
        public View view;
        public ViewHolder(View v) {
            super(v);
            this.mName = (TextView) v.findViewById(R.id.name);
            this.mAvatar = (ImageView) v.findViewById(R.id.avatar);
            this.view = v;
            if(isHost){
                this.view.setClickable(true);
                v.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            itemListener.recycleViewListClicked(v, this.getAdapterPosition());
        }
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_lobby_user, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        User user = users.get(pos);

        holder.mName.setText(user.getLogin());
        new DownloadImageTask(holder.mAvatar,context)
                .execute(user.getUri());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public void addUsers(List<User> users){
        this.users = users;
        this.notifyDataSetChanged();
    }
    public void addUser(User user){
        this.users.add(user);
        this.notifyDataSetChanged();
    }

    public User getUser(int index){
        return users.get(index);
    }


}
