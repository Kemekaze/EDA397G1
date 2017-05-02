package chalmers.eda397g1.ui.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import chalmers.eda397g1.R;
import chalmers.eda397g1.adapters.RoundResultAdapter;
import chalmers.eda397g1.models.RoundVoteResult;


public class ResultsDialogFragment extends DialogFragment {
    // the fragment initialization parameters
    private static final String ROUND_RESULTS = "results";
    private RoundVoteResult[] results;


    public ResultsDialogFragment() {
        // Required empty public constructor
    }

    public static ResultsDialogFragment newInstance(RoundVoteResult[] results) {
        ResultsDialogFragment fragment = new ResultsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ROUND_RESULTS, results);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            results = (RoundVoteResult[]) getArguments().getSerializable(ROUND_RESULTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_results_dialog, container, false);
        ListView list = (ListView) v.findViewById(R.id.resultsList);
        RoundResultAdapter adapter = new RoundResultAdapter(getActivity(), results);
        list.setAdapter(adapter);

        return v;
    }


}
