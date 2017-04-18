package chalmers.eda397g1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.Events.ReposProjectsEvent;
import chalmers.eda397g1.Events.RequestEvent;
import chalmers.eda397g1.Events.UserProjectsEvent;
import chalmers.eda397g1.Objects.Project;
import chalmers.eda397g1.Objects.Repository;
import chalmers.eda397g1.Resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class ChooseRepoProjectActivity extends AppCompatActivity {

    public static final String TAG = "chooserepo";

    private Spinner repoSpinner;
    private Spinner projectSpinner;
    private Button chooseButton;

    private List<Repository> repoList = new ArrayList<>();
    private List<String> repoNames = new ArrayList<>();
    private Repository selectedRepo = null;

    private List<Project> projectList = new ArrayList<>();
    private List<String> projectNames = new ArrayList<>();
    private Project selectedProject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_repo_project);

        // Find Views
        repoSpinner = (Spinner) findViewById(R.id.repoSpinner);
        projectSpinner = (Spinner) findViewById(R.id.projectSpinner);
        chooseButton = (Button) findViewById(R.id.chooseButton);

        //creates the adpters used by the spinners
        final ArrayAdapter<String> repoAdapter = new ArrayAdapter<String>(ChooseRepoProjectActivity.this,
                android.R.layout.simple_spinner_item, repoNames);
        repoAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        final ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(ChooseRepoProjectActivity.this,
                        android.R.layout.simple_spinner_item, projectNames);
        projectAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        // Initialize Repository Spinner
        repoSpinner.setAdapter(repoAdapter);
        repoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(i < repoList.size()){
                    selectedRepo = repoList.get(i);
                    projectList.clear();
                    projectNames.clear();
                    requestProjectsData(selectedRepo.getFullName());
                    projectAdapter.notifyDataSetChanged();
                } else {
                    selectedRepo = null;
                }

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
                if(i < projectList.size()){
                    selectedProject = projectList.get(i);
                } else {
                    selectedProject = null;
                }
                //TODO: This project spinner is unnecessary, it should together with the "choose" button be replaced with a listview where you can choose a project directly.
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Initialize choose Button
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedProject != null ){
                    // Start lobby as hostgit
                    Intent intent = new Intent(ChooseRepoProjectActivity.this, LobbyActivity.class);
                    Bundle b = new Bundle();
                    b.putBoolean("isHost", true);
                    b.putCharSequence("repoName", selectedRepo.getName());
                    b.putCharSequence("projectName", selectedProject.getName());
                    intent.putExtras(b);
                    startActivity(intent);
                    //TODO send relevant data to Lobby activity. Added repo and project name as example.
                }else{
                    Toast.makeText(getApplicationContext(), "No project selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Initiates request for the repository data of this user.
     * Results will later be provided by call-back method receiveRepositoryData(...)
     */
    private void requestRepositoryData() {
        RequestEvent event = new RequestEvent(Constants.SocketEvents.REPOSITORIES);
        EventBus.getDefault().post(event);
    }

    /**
     * Initiates request for the projects data of the selected repo.
     * Results will later be provided by call-back method onReceiveProjectsData(...)
     * @param repoFullName Full name of repository ex: username/reponame
     */
    private void requestProjectsData(String repoFullName) {
        JSONObject query = new JSONObject();
        try {
            query.put("full_name", repoFullName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestEvent event = new RequestEvent(Constants.SocketEvents.REPOSITORY_PROJECTS, query);
        EventBus.getDefault().post(event);
    }

    /**
     * Callback method to receive response from requestRepositoryData() call.
     * @param event from eventbus containing list of repositories.
     */
    @Subscribe (threadMode = ThreadMode.MainThread)
    public void onReceiveRepositoryData(ReposProjectsEvent event){
        repoList = event.getRepositories();
        repoNames.clear();
        projectNames.clear();
        if (repoList.isEmpty()) {
            repoNames.add("No repositories available.");
        } else {
            for(Repository r : repoList){
                repoNames.add(r.getName());
            }
        }
        ( (ArrayAdapter<String>) repoSpinner.getAdapter()).notifyDataSetChanged();
    }

    /**
     * Callback method to receive response from requestProjectsData() call.
     * @param event from eventbus containing list of projects.
     */
    @Subscribe (threadMode = ThreadMode.MainThread)
    public void onReceiveProjectsData(UserProjectsEvent event){
        projectList = event.getProjects();
        projectNames.clear();
        if (projectList.isEmpty()) {
            projectNames.add("No projects available.");
        } else {
            for(Project p : projectList){
                projectNames.add(p.getName());
            }
        }
        ( (ArrayAdapter<String>) projectSpinner.getAdapter()).notifyDataSetChanged();
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
