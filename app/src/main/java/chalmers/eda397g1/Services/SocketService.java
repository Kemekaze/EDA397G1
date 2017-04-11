package chalmers.eda397g1.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import chalmers.eda397g1.Events.RequestEvent;
import chalmers.eda397g1.Resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class SocketService extends Service {
    private static final String TAG = "eda397";

    private static Socket socket;

    public SocketService() {
        Log.i(TAG, "SocketService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        EventBus.getDefault().register(this);
        try {
            setupIO();
            setupListeners();
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate();
    }
    private void setupIO() throws Exception{
        String host = "https://" + Constants.SERVER_IP+":"+Constants.SERVER_PORT;
        Log.i(TAG, "Connecting to: " + host);
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        IO.setDefaultSSLContext(sc);
        IO.setDefaultHostnameVerifier(mHostnameVerifier);

        IO.Options opts = new IO.Options();
        opts.query = "token="+ Constants.SERVER_TOKEN;
        opts.forceNew = true;
        opts.secure = true;
        opts.sslContext = sc;
        opts.reconnection = true;
        opts.hostnameVerifier = mHostnameVerifier;
        socket = IO.socket(host,opts);
    }

    private void setupListeners(){
        //Connection
        socket.on(Socket.EVENT_CONNECT, eventConnected);
        socket.on(Socket.EVENT_RECONNECT, eventReconnected);
        socket.on(Socket.EVENT_DISCONNECT, eventDisconnected);
        socket.on(Socket.EVENT_CONNECT_ERROR, eventError);
        //Authorization
        socket.on(Constants.SocketEvents.AUTHORIZED, eventAuthorized);
        socket.on(Constants.SocketEvents.UNAUTHORIZED, eventUnauthorized);
        //Thirdparty athentication
        socket.on(Constants.SocketEvents.AUTHENTICATE_GITHUB, eventAuthorizedGithub);
        socket.on(Constants.SocketEvents.AUTHENTICATE_BITBUCKET, eventAuthorizedBitbucket);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service destroyed");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**Socket events**/

    //Connection
    private Emitter.Listener eventConnected = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventConnected");
            //testing only
            //JSONObject query = Queries.add(Queries.query("username", ""),"password", "");
            //socket.emit(Constants.SocketEvents.AUTHENTICATE_GITHUB, query);
        }
    };

    private Emitter.Listener eventReconnected = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventReconnected");
        }
    };

    private Emitter.Listener eventDisconnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventDisconnected");
            socket.emit(Socket.EVENT_RECONNECT_ATTEMPT);
        }
    };
    private Emitter.Listener eventError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventError");
            for(int i = 0; i<args.length; i++)
                Log.i(TAG,  args[i].toString());
        }
    };

    //Authentication
    private Emitter.Listener eventAuthorized = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventAuthorized");
        }
    };
    private Emitter.Listener eventUnauthorized = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventUnauthorized");
        }
    };
    private Emitter.Listener eventAuthorizedGithub = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventAuthorizedGithub");
            for(int i = 0; i<args.length; i++)
                Log.i(TAG,  args[i].toString());
        }
    };
    private Emitter.Listener eventAuthorizedBitbucket = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventAuthorizedBitbucket");
            for(int i = 0; i<args.length; i++)
                Log.i(TAG,  args[i].toString());
        }
    };



    //Eventbus events
    @Subscribe
    public void onRequestEvent(RequestEvent event){
        Log.i(TAG, "emit(RequestEvent " + event.getEventName() + " )");
        socket.emit(event.getEventName(),event.getData());
    }
    private HostnameVerifier mHostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.i(TAG, "Verifying host name ::: " + hostname);
            return true;
        }
    };
    private TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
    }};


}
