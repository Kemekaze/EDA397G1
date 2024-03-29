package chalmers.eda397g1.models;

/**
 * Created by nume on 2017-04-16
 */

public class Repository {

    private int id;
    private String name;
    private String fullName;
    private boolean privat;

    public Repository(int id, String name, String fullName, boolean privat){
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.privat = privat;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isPrivate(){
        return privat;
    }
}