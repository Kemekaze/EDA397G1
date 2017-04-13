package chalmers.eda397g1.Events;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ReposProjectsEvent extends Event {

    private HashMap<String, List<String>> reposmap = new HashMap<>();

    public ReposProjectsEvent(Object... args) {
        super(args);

        JSONObject obj = (JSONObject) getData();
        try {
            JSONArray arr = obj.getJSONArray("repos");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject repoObj = arr.getJSONObject(i);
                String repositoryname = repoObj.getString("name");

                JSONArray arr2 = repoObj.getJSONArray("projects");
                List<String> projectlist = new ArrayList<>();
                for (int j = 0; j < arr2.length(); j++) {
                    String projectname = arr2.getJSONObject(j).getString("name");
                    projectlist.add(projectname);
                }
                reposmap.put(repositoryname, projectlist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, List<String>> getRepos(){
        return reposmap;
    }

}

/* Parsing this schema:
{
	"repos": [{
		"id": "REPOID1",
		"name": "Reponame1",
		"full_name": "dummyname/Reponame1",
		"private": true,
		"projects": [{
			"pid": "PROJECTID1A",
			"name": "ProjectAInRepo1"
		}, {
			"pid": "PROJECTID1B",
			"name": "ProjectBInRepo1"
		}, {
			"pid": "PROJECTID1C",
			"name": "ProjectCInRepo1"
		}]
	}, {
		"id": "REPOID2",
		"name": "Reponame2",
		"full_name": "otherdummyname/Reponame2",
		"private": false,
		"projects": [{
			"pid": "PROJECTID2A",
			"name": "ProjectAInRepo2"
		}, {
			"pid": "PROJECTID2B",
			"name": "ProjectBInRepo2"
		}]
	}]
}




*/