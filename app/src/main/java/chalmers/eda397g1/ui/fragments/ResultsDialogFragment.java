package chalmers.eda397g1.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import chalmers.eda397g1.R;
import chalmers.eda397g1.adapters.RoundResultAdapter;
import chalmers.eda397g1.models.RoundVoteResult;


public class ResultsDialogFragment extends DialogFragment {
    // the fragment initialization parameters
    private static final String TAG = "ResultsDialogFragment";
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
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Log.d(TAG, "onCreateDialog");

        AlertDialog.Builder b  = new AlertDialog.Builder(getActivity())
                .setTitle("TITLE")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //TODO Add functionality
                            }
                        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_results_dialog, null);
        ListView list = (ListView) v.findViewById(R.id.resultsList);
        RoundResultAdapter adapter = new RoundResultAdapter(getActivity(), results);
        list.setAdapter(adapter);

        b.setView(v);
        return b.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            // Make sure the activity handles it correctly when the dialog disappears
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
