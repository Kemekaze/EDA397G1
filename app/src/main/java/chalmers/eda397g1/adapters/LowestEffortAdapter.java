package chalmers.eda397g1.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.R;
import chalmers.eda397g1.interfaces.RecyclerViewClickListener;
import chalmers.eda397g1.models.BacklogItem;

/**
 * Created by Jeppe on 2017-05-04.
 */

public class LowestEffortAdapter extends RecyclerView.Adapter<LowestEffortAdapter.ViewHolder>
{
    private String TAG = "LowestEffortAdapter";
    private List<BacklogItem> items = new ArrayList<>();
    private static RecyclerViewClickListener itemListener;

    public LowestEffortAdapter(RecyclerViewClickListener itemListener)
    {
        this.itemListener = itemListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView mBody;
        public TextView mValue;
        public View view;
        public ViewHolder(View v)
        {
            super(v);
            this.mBody = (TextView) v.findViewById(R.id.body);
            this.mValue = (TextView) v.findViewById(R.id.value);
            this.view = v;
            this.view.setClickable(true);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            itemListener.recycleViewListClicked(v, this.getAdapterPosition());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_vote_on_lowest_effort_item,
                                                                  parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        final int pos = position;
        BacklogItem item = items.get(pos);

        holder.mBody.setText(item.getBody());
        holder.mValue.setText(item.getBusinessValue());

    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }


    public void addItems(List<BacklogItem> items)
    {
        this.items = items;
        this.notifyDataSetChanged();
    }
    public void addUser(BacklogItem item)
    {
        this.items.add(item);
        this.notifyDataSetChanged();
    }

    public BacklogItem getItem(int index)
    {
        return items.get(index);
    }


}