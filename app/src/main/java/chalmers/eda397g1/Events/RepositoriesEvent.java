package chalmers.eda397g1.Events;

import org.json.JSONArray;
import org.json.JSONObject;

import chalmers.eda397g1.Objects.Repository;

/**
 * Created by elias on 2017-04-17.
 */

public class RepositoriesEvent extends Event {

    private Repository[] repositories = null;
    public RepositoriesEvent(Object... args) {
        super(args);

        JSONArray repos = (JSONArray) getData();
        if(repos.length() > 0)
        {
            this.repositories = new Repository[repos.length()];
            for(int i = 0; i < repos.length(); i++)
            {
                JSONObject repo = repos.optJSONObject(i);
                this.repositories[i] = new Repository(
                        repo.optInt("id"),
                        repo.optString("name"),
                        repo.optString("full_name"),
                        repo.optBoolean("private")
                );
            }
        }
        else{
            this.repositories = new Repository[0];
        }
    }
}
