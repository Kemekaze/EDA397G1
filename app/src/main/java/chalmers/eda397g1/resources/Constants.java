package chalmers.eda397g1.resources;

/**
 * Created by elias on 2017-04-05
 */

public class Constants {
    //localhost only on dev
    public static final String SERVER_IP = "xr-plan.se";
    public static final String SERVER_PORT = "8443";
    public static final String SERVER_TOKEN = "42ig3Psps3oGbQ6H";

    public class StatusCodes{

        public static final int OK = 200;
        public static final int BAD_REQUEST = 400;
        public static final int UNAUTHORIZED = 401;
        public static final int FORBIDDEN = 403;
        public static final int NOT_FOUND = 404;
        public static final int SERVER_ERROR = 500;
        public static final int NOT_IMPLEMENTED = 501;
        public static final int SERVICE_UNAVAILABLE = 503;
    }

    public class SocketEvents {

        public static final String AUTHENTICATE = "authenticate";
        public static final String AUTHORIZED = "authorized";
        public static final String UNAUTHORIZED = "unauthorized";
        public static final String AUTHENTICATE_AUTOLOGIN = "authenticate.autologin";
        public static final String AUTHENTICATE_GITHUB = "authenticate.github";
        public static final String AUTHENTICATE_BITBUCKET = "authenticate.bitbucket";

        public static final String REPOSITORIES = "repositories";
        public static final String REPOSITORY_PROJECTS = "repository.projects";
        public static final String COLUMN_CARDS = "column.cards";
        public static final String PROJECT_COLUMNS = "project.columns";

        public static final String SESSION_CREATE = "session.create";
        public static final String SESSION_CREATED = "session.created";
        public static final String SESSION_JOIN = "session.join";
        public static final String SESSION_START = "session.start";
        public static final String AVAILABLE_SESSIONS = "available.sessions";

        public static final String SESSION_CLIENTS = "session.clients";

        public static final String VOTE_LOWEST = "vote.lowest";
        public static final String VOTE_LOWEST_COMPLETED = "vote.lowest.completed";
        public static final String VOTE = "vote";
        public static final String VOTE_COMPLETED = "vote.completed";
    }
}
