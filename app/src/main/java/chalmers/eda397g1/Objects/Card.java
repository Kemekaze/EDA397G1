package chalmers.eda397g1.Objects;

/**
 * Created by elias on 2017-04-17.
 */

public class Card {
    private int card_id;
    private int column_id;
    private int issue_id;
    private int number;
    private String title;
    private String body;
    private String state;
    private Label[] labels;

    public Card(int card_id, int column_id, int issue_id, int number, String title, String body, String state) {
        this.card_id = card_id;
        this.column_id = column_id;
        this.issue_id = issue_id;
        this.number = number;
        this.title = title;
        this.body = body;
        this.state = state;
        this.labels = new Label[0];
    }

    public int getCard_id() {
        return card_id;
    }

    public int getColumn_id() {
        return column_id;
    }

    public int getIssue_id() {
        return issue_id;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getState() {
        return state;
    }

    public Label[] getLabels() {
        return labels;
    }

    public void setLabels(Label[] labels) {
        this.labels = labels;
    }
}
