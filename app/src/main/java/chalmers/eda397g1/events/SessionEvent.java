package chalmers.eda397g1.events;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chalmers.eda397g1.models.BacklogItem;
import chalmers.eda397g1.models.Github;
import chalmers.eda397g1.models.Session;
import chalmers.eda397g1.models.User;

/**
 * Created by nume on 2017-04-25.
 */

public class SessionEvent extends Event{
    private String TAG = "SessionEvent:";
    private Session session;

    public SessionEvent(Object... args) {
        super(args);
        try {
            JSONObject root = (JSONObject) getData();
            Log.d(TAG, "getdata:" + root.toString());

            JSONObject github = root.getJSONObject("github");
            JSONObject user = root.getJSONObject("host");

            List<BacklogItem> backlogItems = new ArrayList<>();

            JSONArray backlogArr = github.getJSONArray("backlog_items");

            for(int i = 0; i < backlogArr.length(); i++){
                    JSONObject obj = backlogArr.getJSONObject(i);
                    backlogItems.add(new BacklogItem(
                            obj.getInt("business_value"),
                            obj.getString("card_id"),
                            obj.getInt("effort_value"),
                            obj.getString("issue_id"),
                            obj.getInt("number"),
                            obj.getString("state"),
                            obj.getString("title"),
                            obj.getString("body")
                    ));
            }

            Github githubObj = new Github(backlogItems,
                    github.getString("column_id"),
                    github.getString("full_name"),
                    github.getString("project_id"),
                    github.getString("repo_id"));


            session = new Session(
                    root.optString("_id"), githubObj,
                    new User(
                            user.getString("login"),
                            user.getString("avatar")
                    )
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Session getSession() {
        return session;
    }
}

/**
 * Joins a game
 * @param {Number} data.game_id
 * @return {Object} rtn
 *  Example:
 *  {
 *      _id: 58fa6333b4e1c63204c6a734,
 *     github:{
 *      backlog_items: [
 *          {
 *            business_value:17,
 *            card_id: 2388608,
 *            effort_value: -1,
 *            issue_id: 219890979,
 *            number: 57,
 *            state: "open",
 *            title: "As a user i would like to automatically log in on app start so that the user dont have to reenter credentials",
 *            votes: []
 *          },
....
 *      ],
 *      column_id:"824127"
 *      full_name:"Kemekaze/EDA397G1"
 *      project_id:"482637"
 *      repo_id:"86072010"
 *    },
 *    host:{
 *    	 "login": "Kemekaze",
 *       "avatar_url": "https://avatars3.githubusercontent.com/u/5463135?v=3"
 *    }
 *  }
 */
