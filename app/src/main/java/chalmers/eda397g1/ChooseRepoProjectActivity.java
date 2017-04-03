package chalmers.eda397g1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by fredrikhansson on 03/04/17.
 */

public class ChooseRepoProjectActivity extends AppCompatActivity {

    private Spinner repoSpinner;
    private Spinner projectSpinner;

    //Hardcoded repo names and project names. Must find way to get them from git
    private static final String[]repos = {"Repo 1", "Repo 2", "Repo 3"};
    private static final String[]projects = {"Project 1", "Project 2", "Project 3"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_repo_project);


        //creates the adpters used by the spinners
        ArrayAdapter<String> repoAdapter = new ArrayAdapter<String>(ChooseRepoProjectActivity.this,
                android.R.layout.simple_spinner_item,repos);

        ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(ChooseRepoProjectActivity.this,
                        android.R.layout.simple_spinner_item,projects);

        //create the spinners to use for drop down menus
        repoSpinner = (Spinner) findViewById(R.id.repoSpinner);
        projectSpinner = (Spinner) findViewById(R.id.projectSpinner);


        repoSpinner.setAdapter(repoAdapter);
        repoAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        repoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.v("item", (String) adapterView.getItemAtPosition(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



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
    }
}
