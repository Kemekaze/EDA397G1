package chalmers.eda397g1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.Events.ProjectColumnsEvent;
import chalmers.eda397g1.Events.ReposProjectsEvent;
import chalmers.eda397g1.Events.RequestEvent;
import chalmers.eda397g1.Events.UserProjectsEvent;
import chalmers.eda397g1.Objects.Column;
import chalmers.eda397g1.Objects.Project;
import chalmers.eda397g1.Objects.Repository;
import chalmers.eda397g1.Resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class ChooseRepoProjectActivity extends AppCompatActivity {

    public static final String TAG = "chooserepo";

    private MaterialBetterSpinner repoSpinner;
    private MaterialBetterSpinner  projectSpinner;
    private MaterialBetterSpinner  columnSpinner;
    private Button chooseButton;

    private List<Repository> repoList = new ArrayList<>();
    private List<String> repoNames = new ArrayList<>();
    private Repository selectedRepo = null;

    private List<Project> projectList = new ArrayList<>();
    private List<String> projectNames = new ArrayList<>();
    private Project selectedProject = null;

    private List<Column> columnList  = new ArrayList<>();
    private List<String> columnNames = new ArrayList<>();
    private Column selectedColumn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_repo_project);

        // Find Spinners
        repoSpinner = (MaterialBetterSpinner) findViewById(R.id.repoSpinner);
        projectSpinner = (MaterialBetterSpinner) findViewById(R.id.projectSpinner);
        columnSpinner = (MaterialBetterSpinner) findViewById(R.id.columnSpinner);

        // Find Button
        chooseButton = (Button) findViewById(R.id.chooseButton);

        // Create the adapters
        final ArrayAdapter<String> repoAdapter = new ArrayAdapter<String>(
                ChooseRepoProjectActivity.this,
                android.R.layout.simple_spinner_item,
                repoNames);

        final ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(
                ChooseRepoProjectActivity.this,
                android.R.layout.simple_spinner_item,
                projectNames);

        final ArrayAdapter<String> columnAdapter = new ArrayAdapter<String>(
                ChooseRepoProjectActivity.this,
                android.R.layout.simple_spinner_item,
                columnNames);

        // Set adapter layouts
        projectAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        repoAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        columnAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        // Initialize Repository Spinner.
        repoSpinner.setAdapter(repoAdapter);
        repoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected repositorySpinner");
                // There may be no repositories.
                if(i < repoList.size()){
                    selectedRepo = repoList.get(i);
                    projectList.clear();
                    projectNames.clear();
                    requestProjectsData(selectedRepo.getFullName());
                    projectAdapter.notifyDataSetChanged();
                } else {
                    selectedRepo = null;
                    // TODO: Show Snackbar when no repositories are available
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
                    columnList.clear();
                    columnNames.clear();
                    requestColumnsData(selectedProject.getId());
                    columnAdapter.notifyDataSetChanged();
                } else {
                    selectedProject = null;
                    // TODO: Show SnackBar that tells the user that no project is selected.
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Initialize Column Adapter
        columnSpinner.setAdapter(columnAdapter);
        columnSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i < columnList.size()){
                    selectedColumn = columnList.get(i);
                } else {
                    selectedColumn = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /**
         * On a press to the choose button it is checked if all necessary selections have been made,
         * and then a new lobby is started
         */
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedRepo == null ) {
                    Snackbar snackbar = Snackbar.make(
                            findViewById(R.id.repoSpinner),
                            "No repository selected!",
                            Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //Toast.makeText(getApplicationContext(), "No repository selected!", Toast.LENGTH_SHORT).show();
                } else if(selectedProject == null) {
                    Snackbar snackbar = Snackbar.make(
                            findViewById(R.id.projectSpinner),
                            "No project selected!",
                            Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //Toast.makeText(getApplicationContext(), "No project selected!", Toast.LENGTH_SHORT).show();
                } else if(selectedColumn == null) {
                    Snackbar snackbar = Snackbar.make(
                            findViewById(R.id.columnSpinner),
                            "No column selected!",
                            Snackbar.LENGTH_LONG);
                    snackbar.show();
                    // Toast.makeText(getApplicationContext(), "No column selected!", Toast.LENGTH_SHORT).show();
                } else {
                    // Tell server to create a game
                    JSONObject query = new JSONObject();
                    try {
                        query.put("full_name", selectedRepo.getFullName());
                        query.put("repo_id", selectedRepo.getId());
                        query.put("project_id", selectedProject.getName());
                        query.put("column_id", selectedColumn.getName());
                        RequestEvent event = new RequestEvent(Constants.SocketEvents.CREATE_GAME, query);
                        EventBus.getDefault().post(event);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Start lobby as host
                    Intent intent = new Intent(ChooseRepoProjectActivity.this, LobbyActivity.class);
                    Bundle b = new Bundle();
                    b.putBoolean("isHost", true);
                    // Put selected repo
                    b.putCharSequence("fullName", selectedRepo.getFullName());
                    b.putInt("repoID", selectedRepo.getId());
                    // Put selected project
                    b.putCharSequence("projectName", selectedProject.getName());
                    b.putInt("projectID", selectedProject.getId());
                    // Put selected Column
                    b.putCharSequence("columnName", selectedColumn.getName());
                    b.putInt("columnID", selectedColumn.getId());
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart()");
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        requestRepositoryData();
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"onStop()");
        EventBus.getDefault().unregister(this);
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
     * Initiates request for the projects data of the selected repo.
     * Results will later be provided by call-back method onReceiveProjectsData(...)
     * @param projectID Full name of repochooserepository ex: username/reponame
     */
    private void requestColumnsData(int projectID) {
        JSONObject query = new JSONObject();
        try {
            query.put("project_id", projectID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestEvent event = new RequestEvent(Constants.SocketEvents.PROJECT_COLUMNS, query);
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
        columnNames.clear();

        if (repoList.isEmpty()) {
            repoNames.add("No repositories available.");

        } else {
            for(Repository r : repoList){
                repoNames.add(r.getName());
            }
            Log.v(TAG, "Call request projects");
            requestProjectsData(repoNames.get(0));
        }
        ( (ArrayAdapter<String>) repoSpinner.getAdapter()).notifyDataSetChanged();
        ( (ArrayAdapter<String>) projectSpinner.getAdapter()).notifyDataSetChanged();
        ( (ArrayAdapter<String>) columnSpinner.getAdapter()).notifyDataSetChanged();
    }

    /**
     * Callback method to receive response from requestProjectsData() call.
     * @param event from eventbus containing list of projects.
     */
    @Subscribe (threadMode = ThreadMode.MainThread)
    public void onReceiveProjectsData(UserProjectsEvent event){
        projectList = event.getProjects();
        projectNames.clear();
        columnNames.clear();
        if (projectList.isEmpty()) {
            projectNames.add("No projects available.");
        } else {
            requestColumnsData(projectList.get(0).getId());
            for(Project p : projectList){
                projectNames.add(p.getName());
            }
        }
        ( (ArrayAdapter<String>) projectSpinner.getAdapter()).notifyDataSetChanged();
        ( (ArrayAdapter<String>) columnSpinner.getAdapter()).notifyDataSetChanged();
    }

    /**
     * Callback method to receive response from requestColumnsData() call.
     * @param event from eventbus containing list of repositories.
     */
    @Subscribe (threadMode = ThreadMode.MainThread)
    public void onReceiveColumnsData(ProjectColumnsEvent event){
        Log.d(TAG, event.getColumns().toString());
        columnList = event.getColumns();
        columnNames.clear();
        if (columnList.isEmpty()) {
            columnNames.add("No columns available.");
        } else {
            for(Column col : columnList){
                Log.d(TAG, col.getName());
                columnNames.add(col.getName());
            }
        }
        ( (ArrayAdapter<String>) columnSpinner.getAdapter()).notifyDataSetChanged();
    }


}
