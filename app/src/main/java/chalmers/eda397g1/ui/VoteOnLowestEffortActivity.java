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
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;


import chalmers.eda397g1.R;
import chalmers.eda397g1.events.VoteOnLowestEffortEvent;
import chalmers.eda397g1.events.VoteOnLowestEffortResultEvent;
import chalmers.eda397g1.adapters.LowestEffortAdapter;
import chalmers.eda397g1.interfaces.RecyclerViewFlipperClickListener;
import chalmers.eda397g1.models.BacklogItem;
import chalmers.eda397g1.models.Session;
import chalmers.eda397g1.events.RequestEvent;

import chalmers.eda397g1.resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class VoteOnLowestEffortActivity extends AppCompatActivity {
    private static final String TAG = "VoteOnLow..Activity";

    private ProgressBar spinner;
    private RecyclerView mRecyclerView;
    private LowestEffortAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Session session;
    private BacklogItem selectedBacklogItem;
    private TextView mEmptyView;
    private Context mContext;
    private ViewFlipper pViewFlipper;

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
        pViewFlipper = null;

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

                    // TODO: Send the vote to server and wait for response. Refactor start of voteActivity to
                    //    fire when response is received

                    JSONObject lowestEffortItem = new JSONObject();
                    try {
                        lowestEffortItem.put("item_id", selectedBacklogItem.getId());
                        System.out.println(selectedBacklogItem.getId());
                        RequestEvent requestEvent = new RequestEvent(Constants.SocketEvents.VOTE_LOWEST, lowestEffortItem);
                        EventBus.getDefault().post(requestEvent);

                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }
        });

        spinner = (ProgressBar) findViewById(R.id.loadingSpinner);
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart()");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d(TAG, "onStop()");
    }

    private RecyclerViewFlipperClickListener listener = new RecyclerViewFlipperClickListener() {
        @Override
        public void recycleViewFlipperListClicked(View v, int position, ViewFlipper mViewFlipper) {
            Log.i("RecycleViewListClicked", "position: " + position);
            selectedBacklogItem = session.getGithub().getBacklogItems().get(position);

            if(pViewFlipper != null)
            {
                pViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.grow_from_middle));
                pViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shrink_to_middle));

                pViewFlipper.showPrevious();
            }

            if(pViewFlipper == mViewFlipper)
            {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.grow_from_middle));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shrink_to_middle));

                mViewFlipper.showPrevious();
            }

            if(mViewFlipper.getDisplayedChild() == 0)
            {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.grow_from_middle));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shrink_to_middle));

                mViewFlipper.showNext();
            }

            else
            {
                mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.grow_from_middle));
                mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shrink_to_middle));

                mViewFlipper.showPrevious();
            }

            if(pViewFlipper == mViewFlipper)
            {
                pViewFlipper = null;
                selectedBacklogItem = null;
            }

            else
                pViewFlipper = mViewFlipper;

        }
    };

    @Subscribe (threadMode = ThreadMode.MainThread)
    public void onVoteOnLowestEffortResultEvent (VoteOnLowestEffortResultEvent event){
        Log.i(TAG, "onVoteOnLowestEffortCompleted");

        Intent intent = new Intent(VoteOnLowestEffortActivity.this, VoteActivity.class);
        Bundle b = new Bundle();
        String referenceId = event.getReferenceItemId();
        String startItemId = event.getNextId();
        int referenceEffort = event.getLowestEffort();
        b.putSerializable("session", session);
        b.putString("referenceId", referenceId);
        b.putString("startItemId",startItemId);
        b.putInt("referenceEffort",referenceEffort);
        intent.putExtras(b);
        Log.d(TAG, "Spinner gone, mRecyclerView enabled");
        spinner.setVisibility(View.GONE); // reset spinner
        mRecyclerView.setVisibility(View.VISIBLE); // reset list
        startActivity(intent);
        finish();
    }
    //Tell someone when they have voted
    @Subscribe (threadMode = ThreadMode.MainThread)
    public void onEventVoteOnLowest(VoteOnLowestEffortEvent event){
        Log.i(TAG, "onEventVoteOnLowest");

        Log.d(TAG, "Spinner visible, mRecyclerView disabled");
        mRecyclerView.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
    }

}
