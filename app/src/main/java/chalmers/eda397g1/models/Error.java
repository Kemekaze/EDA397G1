package chalmers.eda397g1.models;

/**
 * Created by elias on 2017-04-06.
 */

public class Error {
    private String error;
    private String message;

    public Error(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
