package chalmers.eda397g1.Objects;

/**
 * Created by elias on 2017-04-17.
 */

public class Column {
    private int id;
    private String name;

    public Column(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
