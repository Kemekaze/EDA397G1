package chalmers.eda397g1.events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.models.Project;

/**
 * Created by nume on 2017-04-16
 */

public class UserProjectsEvent extends Event {
    private List<Project> projects = new ArrayList<>();

    public UserProjectsEvent(Object... args) {
        super(args);
        Object obj = getData();
        if(obj instanceof JSONArray){
            JSONArray arr = (JSONArray) getData();
            projects = parseProjectsFromJson(arr);
        } else {
            // do nothing give empty list.
        }
    }

    public List<Project> getProjects(){
        return projects;
    }

    private List<Project> parseProjectsFromJson(JSONArray arr){
        List<Project> list = new ArrayList<>();
        for(int i = 0; i < arr.length(); i++){
            try {
                JSONObject obj = arr.getJSONObject(i);
                list.add(new Project(
                        obj.getInt("id"),
                        obj.getString("name"),
                        obj.getString("body"),
                        obj.getInt("number"),
                        obj.getString("state")
                        ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
