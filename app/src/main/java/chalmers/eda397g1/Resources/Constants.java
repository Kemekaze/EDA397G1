package chalmers.eda397g1.Resources;

/**
 * Created by elias on 2017-04-05.
 */

public class Constants {
    //localhost only on dev
    public static final String SERVER_IP = "83.185.243.160";
    public static final String SERVER_PORT = "9000";
    public static final String SERVER_TOKEN = "42ig3Psps3oGbQ6H";

    public class StatusCodes{

        public static final int OK = 200;
        public static final int UNAUTHORIZED = 401;
        public static final int NOT_FOUND = 404;
        public static final int SERVER_ERROR = 500;
        public static final int NOT_IMPLEMENTED = 501;
    }

    public class SocketEvents {

        public static final String AUTHENTICATE = "authenticate";
        public static final String AUTHORIZED = "authorized";
        public static final String UNAUTHORIZED = "unauthorized";

        public static final String AUTHENTICATE_GITHUB = "authenticate.github";
        public static final String AUTHENTICATE_BITBUCKET = "authenticate.bitbucket";



    }
}
