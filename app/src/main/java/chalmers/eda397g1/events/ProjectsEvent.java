package chalmers.eda397g1.events;

import org.json.JSONArray;
import org.json.JSONObject;

import chalmers.eda397g1.models.Project;

/**
 * Created by elias on 2017-04-17.
 */

public class ProjectsEvent extends Event {

    private Project[] projects = null;

    public ProjectsEvent(Object... args) {
        super(args);
        JSONArray projects = (JSONArray) getData();
        if(projects.length() > 0)
        {
            this.projects = new Project[projects.length()];
            for(int i = 0; i < projects.length(); i++)
            {
                JSONObject repo = projects.optJSONObject(i);
                this.projects[i] = new Project(
                        repo.optInt("id"),
                        repo.optString("name"),
                        repo.optString("body"),
                        repo.optInt("number"),
                        repo.optString("state")
                );
            }
        }else{
            this.projects = new Project[0];
        }

    }
}
