package chalmers.eda397g1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.HashMap;

import de.greenrobot.event.EventBus;


public class ChooseRepoProjectActivity extends AppCompatActivity {

    private Spinner repoSpinner;
    private Spinner projectSpinner;
    private Button chooseButton;

    //Hardcoded repo names and project names. Must find way to get them from git
    private static final String[]repos = {"Repo 1", "Repo 2", "Repo 3"};
    private static final String[]projects = {"Project 1", "Project 2", "Project 3"};

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

        ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(ChooseRepoProjectActivity.this,
                        android.R.layout.simple_spinner_item, projects);
        projectAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        // Initialize Repository Spinner
        repoSpinner.setAdapter(repoAdapter);
        repoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("item", (String) adapterView.getItemAtPosition(i));
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

        requestRepositoryData();
    }

    /**
     * Initiates request for the repository and project data of this user.
     * Results will later be provided by call-back method receiveRepositoryData(...)
     */
    private void requestRepositoryData() {
        // TODO: Request data from server
    }

    /**
     * Callback method to receive repository and project data from the server
     * @param data The keys are the repository names, the values the projects
     */
    public void onReceiveRepositoryData(HashMap<String,String> data){
        // TODO: receive and display data
    }

    @Override
    public void onStart(){
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
