package chalmers.eda397g1.Objects;

/**
 * Created by nume on 2017-04-16
 */

public class Project {
    private int id;
    private String name;
    private String body;
    private int number;
    private String state;

    public Project(int id, String name, String body, int number, String state) {
        this.id = id;
        this.name = name;
        this.body = body;
        this.number = number;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public int getNumber(){
        return number;
    }

    public String getState() {
        return state;
    }
}
