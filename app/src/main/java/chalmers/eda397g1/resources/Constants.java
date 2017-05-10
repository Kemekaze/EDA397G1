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
        public static final String SIGNOUT = "signout";
        /********* LOGIN *********/
        public static final String AUTHENTICATE_AUTOLOGIN = "authenticate.autologin";
        public static final String AUTHENTICATE_GITHUB = "authenticate.github";
        public static final String AUTHENTICATE_BITBUCKET = "authenticate.bitbucket";
        /********* GITHUB INFO *********/
        public static final String REPOSITORIES = "repositories";
        public static final String REPOSITORY_PROJECTS = "repository.projects";
        public static final String COLUMN_CARDS = "column.cards";
        public static final String PROJECT_COLUMNS = "project.columns";

        /********* SESSION *********/
        public static final String SESSION_CREATE = "session.create";
        public static final String SESSION_JOIN = "session.join";
        public static final String SESSION_LEAVE = "session.leave";
        public static final String SESSION_KICK = "session.kick";
        public static final String SESSION_START = "session.start";
        public static final String AVAILABLE_SESSIONS = "available.sessions";

        /********* VOTEING *********/
        public static final String VOTE_LOWEST = "vote.lowest";
        public static final String VOTE = "vote";

        /********* BEGIN FROM SERVER ONLY *********/
        /********* SESSION *********/
        //When someone has joined the session
        public static final String SESSION_JOINED = "session.joined";
        //Client list update when someone joins or leaves
        public static final String SESSION_CLIENTS = "session.clients";
        //When the host has stated the session
        public static final String SESSION_STARTED = "session.started";
        //When the host has kicked you from the session
        public static final String SESSION_KICKED = "session.kicked";
        //When a session is created
        public static final String SESSION_CREATED = "session.created";
        //When someone left the session
        public static final String SESSION_LEFT = "session.left";
        /********* VOTEING *********/
        //When everyone has voted on the lowest item
        public static final String VOTE_LOWEST_RESULT = "vote.lowest.result";
        //When everyone has selected an effort for the current item, the round result
        public static final String VOTE_ROUND_RESULT = "vote.round.result";
        //When all the rounds are completed and an effort for the item is set
        public static final String VOTE_RESULT = "vote.result";
        /********* END FROM SERVER ONLY ********/


    }
    //Constants for the final result screen columns
    public static final String BUSINESS_VALUE_COLUMN="Businessvalue column";
    public static final String EFFORT_COLUMN="Effort column";
    public static final String ISSUE_NAME_COLUMN="Issuename column";

}
