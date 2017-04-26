package chalmers.eda397g1.Objects;

/**
 * Created by elias on 2017-04-17.
 */

public class Label {
    private int id;
    private String name;

    public Label(int id, String name) {
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
