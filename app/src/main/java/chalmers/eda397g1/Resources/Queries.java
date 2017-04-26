package chalmers.eda397g1.Resources;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * handling the queries to the server for saving different type of json objects.
 */
public class Queries {

    public static <T> JSONObject query(String key, T value){
        try {
            return new JSONObject().put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> JSONObject add(JSONObject obj, String key, T value){
        try {
            return obj.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
