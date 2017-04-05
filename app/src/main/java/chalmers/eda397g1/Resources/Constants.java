package chalmers.eda397g1.Resources;

/**
 * Created by elias on 2017-04-05.
 */

public class Constants {
    //localhost only on dev
    public static final String SERVER_IP = "192.168.10.1";
    public static final String SERVER_PORT = "9000";
    public static final String SERVER_TOKEN = "42ig3Psps3oGbQ6H";


    public class SocketEvents {

        public static final String AUTHENTICATE = "authenticate";
        public static final String AUTHORIZED = "authorized";
        public static final String UNAUTHORIZED = "unauthorized";

        public static final String AUTHENTICATE_GITHUB = "authenticate.github";
        public static final String AUTHENTICATE_BITBUCKET = "authenticate.bitbucket";



    }
}
