package chalmers.eda397g1;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chalmers.eda397g1.Events.ReposProjectsEvent;
import chalmers.eda397g1.Events.RequestEvent;
import chalmers.eda397g1.Resources.Constants;
import chalmers.eda397g1.Resources.Queries;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class ChooseRepoProjectActivity extends AppCompatActivity {

    public static final String TAG = "chooserepo";

    private Spinner repoSpinner;
    private Spinner projectSpinner;
    private Button chooseButton;

    private HashMap<String, List<String>> repositoriesMap;

    private List<String> repos = new ArrayList<>();
    private List<String> projects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_repo_project);

        // Find Views
        repoSpinner = (Spinner) findViewById(R.id.repoSpinner);
        projectSpinner = (Spinner) findViewById(R.id.projectSpinner);
        chooseButton = (Button) findViewById(R.id.chooseButton);

        //creates the adpters used by the spinners
        ArrayAdapter<String> repoAdapter = new ArrayAdapter<String>(ChooseRepoProjectActivity.this,
                android.R.layout.simple_spinner_item, repos);
        repoAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        final ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(ChooseRepoProjectActivity.this,
                        android.R.layout.simple_spinner_item, projects);
        projectAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        // Initialize Repository Spinner
        repoSpinner.setAdapter(repoAdapter);
        repoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("item", (String) adapterView.getItemAtPosition(i));
                String repoName = repos.get(i);
                projects.clear();
                projects.addAll(repositoriesMap.get(repoName));
                projectAdapter.notifyDataSetChanged();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //TODO
            }
        });

        // Initialize Project Adapter
        projectSpinner.setAdapter(projectAdapter);
        projectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("item", (String) adapterView.getItemAtPosition(i));

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Initialize choose Button
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start lobby as hostgit
                Intent intent = new Intent(ChooseRepoProjectActivity.this, LobbyActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("isHost", true);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }

    /**
     * Initiates request for the repository and project data of this user.
     * Results will later be provided by call-back method receiveRepositoryData(...)
     */
    private void requestRepositoryData() {
        RequestEvent event = new RequestEvent(Constants.SocketEvents.REQUEST_PROJECTS);
        EventBus.getDefault().post(event);
    }

    /**
     * Callback method to receive repository and project data from the server
     * @param event The keys are the repository names, the values the projects
     */
    @Subscribe (threadMode = ThreadMode.MainThread)
    public void onReceiveRepositoryData(ReposProjectsEvent event){
        repositoriesMap = event.getRepos();
        repos.clear();
        repos.addAll(repositoriesMap.keySet());
        projects.clear();
        ( (ArrayAdapter<String>) repoSpinner.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart()");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"onStop()");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        requestRepositoryData();
    }
}
