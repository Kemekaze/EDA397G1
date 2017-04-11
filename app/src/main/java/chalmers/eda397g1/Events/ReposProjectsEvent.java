package chalmers.eda397g1.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ReposProjectsEvent {

    private HashMap<String, List<String>> data = new HashMap<>();

    public ReposProjectsEvent(){
        //TODO: parse this date from JSON object.
        data.put("reposistory1", new ArrayList<String>(Arrays.asList("projectA", "projectB", "projectC")));
        data.put("reposistory2", new ArrayList<String>(Arrays.asList("projectD", "projectE", "projectF")));
        data.put("reposistory3", new ArrayList<String>(Arrays.asList("projectG", "projectH", "projectI")));
    }

    public HashMap<String, List<String>> getData(){
        return data;
    }

}
