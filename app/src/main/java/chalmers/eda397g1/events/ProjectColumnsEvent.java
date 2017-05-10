package chalmers.eda397g1.events;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.models.Column;

/**
 * Created by elias on 2017-04-17.
 */

public class ProjectColumnsEvent extends Event {
    private List<Column> columns = new ArrayList<>();
    public ProjectColumnsEvent(Object... args) {
        super(args);
        JSONArray columns = (JSONArray) getData();
        if(columns.length() > 0) {
            for (int i = 0; i < columns.length(); i++) {
                JSONObject column = columns.optJSONObject(i);
                this.columns.add(
                        new Column(
                                column.optInt("id"),
                                column.optString("name")
                        )
                );
            }
        }
    }

    public List<Column> getColumns(){
        return columns;
    }
}
