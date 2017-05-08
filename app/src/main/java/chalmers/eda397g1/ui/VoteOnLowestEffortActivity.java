package chalmers.eda397g1.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import chalmers.eda397g1.R;
import chalmers.eda397g1.adapters.LowestEffortAdapter;
import chalmers.eda397g1.interfaces.RecyclerViewClickListener;
import chalmers.eda397g1.models.BacklogItem;
import chalmers.eda397g1.models.Session;

public class VoteOnLowestEffortActivity extends AppCompatActivity {
    private static final String TAG = "VoteOnLow..Activity";

    private RecyclerView mRecyclerView;
    private LowestEffortAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Session session;
    private BacklogItem selectedBacklogItem;
    private TextView mEmptyView;
    private Context mContext;
    private ViewFlipper mViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_on_lowest_effort);
        mRecyclerView = (RecyclerView) findViewById(R.id.available_items);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LowestEffortAdapter(listener);
        mRecyclerView.setAdapter(mAdapter);
        mContext = this;
        mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flip);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            session = (Session) b.getSerializable("session");
        } else {
            throw new RuntimeException("No bundle!");
        }

        // Temporary
        mAdapter.addItems(session.getGithub().getBacklogItems());

        if(mAdapter.getItemCount() == 0)
        {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }

        final FloatingActionButton voteButton = (FloatingActionButton) findViewById(R.id.button_vote);

        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedBacklogItem == null){
                    Snackbar alert = Snackbar.make(
                            voteButton,
                            "No item selected!",
                            Snackbar.LENGTH_LONG);
                    alert.show();
                } else {
                    startActivity(new Intent(VoteOnLowestEffortActivity.this, VoteActivity.class));
                }
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    private RecyclerViewClickListener listener = new RecyclerViewClickListener() {
        @Override
        public void recycleViewListClicked(View v, int position) {
            Log.i("RecycleViewListClicked", "position: " + position);
            selectedBacklogItem = session.getGithub().getBacklogItems().get(position);
            if(mViewFlipper.getDisplayedChild() == 0)
            {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_left));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.out_from_left));

                mViewFlipper.showNext();
            }

            else
            {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.in_from_right));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.out_from_right));

                mViewFlipper.showPrevious();
            }

        }
    };

}
