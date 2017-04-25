package chalmers.eda397g1.Objects;

/**
 * Created by julius on 25.04.17.
 */

public class BacklogItem {

    private int businessValue;
    private String cardId;
    private int effortValue;
    private String issueId;
    private int number;
    private String state;
    private String title;

    public BacklogItem(int businessValue, String cardId, int effortValue, String issueId, int number, String state, String title) {
        this.businessValue = businessValue;
        this.cardId = cardId;
        this.effortValue = effortValue;
        this.issueId = issueId;
        this.number = number;
        this.state = state;
        this.title = title;
    }

    public void setBusinessValue(int businessValue) {
        this.businessValue = businessValue;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setEffortValue(int effortValue) {
        this.effortValue = effortValue;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBusinessValue() {
        return businessValue;
    }

    public String getCardId() {
        return cardId;
    }

    public int getEffortValue() {
        return effortValue;
    }

    public String getIssueId() {
        return issueId;
    }

    public int getNumber() {
        return number;
    }

    public String getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

}
