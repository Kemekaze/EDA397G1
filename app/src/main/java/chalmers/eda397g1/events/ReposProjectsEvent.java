package chalmers.eda397g1.events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.models.Repository;

public class ReposProjectsEvent extends Event {

    private List<Repository> repositories = new ArrayList<>();

    public ReposProjectsEvent(Object... args) {
        super(args);
        Object obj = getData();
        if(obj instanceof JSONArray){
            JSONArray arr = (JSONArray) obj;
            repositories = parseReposFromJson(arr);
        } else {
            // do nothing give empty list.
        }

    }

    public List<Repository> getRepositories(){
        return repositories;
    }

    private List<Repository> parseReposFromJson(JSONArray arr){
        List<Repository> list = new ArrayList<>();
        for(int i = 0; i < arr.length(); i++){
            try {
                JSONObject obj = arr.getJSONObject(i);
                list.add(new Repository(
                        obj.getInt("id"),
                        obj.getString("name"),
                        obj.getString("full_name"),
                        obj.getBoolean("private")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}












