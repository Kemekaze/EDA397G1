package chalmers.eda397g1.events;

import org.json.JSONArray;
import org.json.JSONObject;
import chalmers.eda397g1.models.Error;

/**
 * Created by elias on 2017-04-06.
 */

public class Event {
    private int status;
    private Error[] errors = null;
    private Object data;

    public Event(Object... args) {
        JSONObject obj = (JSONObject)args[0];
        this.status = obj.optInt("status");
        JSONArray errors = (JSONArray)obj.opt("errors");
        if(errors.length() > 0)
        {
            this.errors = new Error[errors.length()];
            for(int i = 0; i < errors.length(); i++)
            {
                JSONObject error = errors.optJSONObject(i);
                this.errors[i] = new Error(error.optString("error"), error.optString("message"));
            }
        }
        this.data = obj.opt("data");

    }

    public Object getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public Error[] getErrors() {
        return errors;
    }

}
