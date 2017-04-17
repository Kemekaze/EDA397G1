package chalmers.eda397g1.Events;

import org.json.JSONArray;
import org.json.JSONObject;

import chalmers.eda397g1.Objects.Column;

/**
 * Created by elias on 2017-04-17.
 */

public class ProjectColumnsEvent extends Event {
    private Column[] columns = null;
    public ProjectColumnsEvent(Object... args) {
        super(args);
        JSONArray columns = (JSONArray) getData();
        if(columns.length() > 0)
        {
            this.columns = new Column[columns.length()];
            for(int i = 0; i < columns.length(); i++) {
                JSONObject column = columns.optJSONObject(i);
                this.columns[i] = new Column(
                    column.optInt("id"),
                    column.optString("title")
                );
            }


        }else{
            this.columns = new Column[0];
        }
    }
}
