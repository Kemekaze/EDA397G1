package chalmers.eda397g1.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.R;
import chalmers.eda397g1.interfaces.RecyclerViewFlipperClickListener;
import chalmers.eda397g1.models.BacklogItem;

/**
 * Created by Jeppe on 2017-05-04.
 */

public class LowestEffortAdapter extends RecyclerView.Adapter<LowestEffortAdapter.ViewHolder>
{
    private String TAG = "LowestEffortAdapter";
    private List<BacklogItem> items = new ArrayList<>();
    private static RecyclerViewFlipperClickListener itemListener;

    public LowestEffortAdapter(RecyclerViewFlipperClickListener itemListener)
    {
        this.itemListener = itemListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView mTitle;
        public TextView mValue;
        public ViewFlipper mViewFlipper;
        public View view;
        public ViewHolder(View v)
        {
            super(v);
            this.mTitle = (TextView) v.findViewById(R.id.backlog_title);
            this.mValue = (TextView) v.findViewById(R.id.backlog_value);
            this.mViewFlipper = (ViewFlipper) v.findViewById(R.id.view_flip);
            this.view = v;
            this.view.setClickable(true);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            itemListener.recycleViewFlipperListClicked(v, this.getAdapterPosition(), mViewFlipper);
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

        holder.mTitle.setText(item.getTitle());
        String string;
        holder.mValue.setText(string = String.valueOf(item.getBusinessValue()));

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
    public void addItem(BacklogItem item)
    {
        this.items.add(item);
        this.notifyDataSetChanged();
    }

    public BacklogItem getItem(int index)
    {
        return items.get(index);
    }


}