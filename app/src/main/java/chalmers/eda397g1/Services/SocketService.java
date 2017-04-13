package chalmers.eda397g1.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.PriorityQueue;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import chalmers.eda397g1.Events.LoginEvent;
import chalmers.eda397g1.Events.ReposProjectsEvent;
import chalmers.eda397g1.Events.RequestEvent;
import chalmers.eda397g1.Events.VoteOnLowestEffortEvent;
import chalmers.eda397g1.Resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class SocketService extends Service {
    private static final String TAG = "eda397.SocketService";

    SocketServiceCallback socketServiceCallback;
    private static Socket socket;

    private static boolean isConnected = false;

    private PriorityQueue<RequestEvent> queue;

    public SocketService() {
        Log.i(TAG, "SocketService");
        queue = new PriorityQueue<RequestEvent>();
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
        socket.on(Constants.SocketEvents.REQUEST_BACKLOG_ITEMS, eventRequestBacklogItems);
        socket.on(Constants.SocketEvents.REQUEST_PROJECTS, eventRequestProjects);

        socket.on(Constants.SocketEvents.AUTHENTICATE_AUTOLOGIN, eventAuthenticatedAutoLogin);
        socket.on(Constants.SocketEvents.AUTHENTICATE_GITHUB, eventAuthenticatedGithub);
        socket.on(Constants.SocketEvents.AUTHENTICATE_BITBUCKET, eventAuthenticatedBitbucket);
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
            emitQueue();
            isConnected = true;
        }
    };

    private Emitter.Listener eventReconnected = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventReconnected");
            emitQueue();
            isConnected = true;
        }
    };

    private Emitter.Listener eventDisconnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventDisconnected");
            isConnected = false;
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

    private Emitter.Listener eventAuthenticatedAutoLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventAuthenticatedAutoLogin");
            for(int i = 0; i<args.length; i++)
                Log.i(TAG,  args[i].toString());
            EventBus.getDefault().post(new LoginEvent(args));
        }
    };

    private Emitter.Listener eventAuthenticatedGithub = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventAuthenticatedGithub");
            for(int i = 0; i<args.length; i++)
                Log.i(TAG,  args[i].toString());
            EventBus.getDefault().post(new LoginEvent(args));
        }
    };
    private Emitter.Listener eventAuthenticatedBitbucket = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventAuthenticatedBitbucket");
            for(int i = 0; i<args.length; i++)
                Log.i(TAG,  args[i].toString());
        }
    };

    private Emitter.Listener eventRequestBacklogItems = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventRequestBacklogItems");
            EventBus.getDefault().post(new String[]{"item1", "item2", "item3"});
        }
    };

    private Emitter.Listener eventRequestProjects = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventRequestProjects");
            for(int i = 0; i < args.length; i++){
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new ReposProjectsEvent(args));
        }
    };
/*

    @Subscribe
    public void onRequestEvent(RequestEvent event){
        Log.i(TAG, "emit(RequestEvent " + event.getEventName() + " )");
        socket.emit(event.getEventName(),event.getData());


        // Mocked server responses // TODO: remove when server is up

        if(event.getEventName().equals(Constants.SocketEvents.REQUEST_PROJECTS)){
            Log.d("","Sending dummy response to ChooserepoActivity.");
            EventBus.getDefault().post(new ReposProjectsEvent());
        }

        if(event.getEventName().equals(Constants.SocketEvents.RESPONSE_BACKLOG_ITEMS)) {
            EventBus.getDefault().post(new VoteOnLowestEffortEvent(
                    Constants.SocketEvents.RESPONSE_BACKLOG_ITEMS,
                    new String[]{"item1", "item2", "item3"}));
        }
    }
*/

    @Subscribe(sticky = true)
    public void emit(RequestEvent event){
        Log.i(TAG, "emit(RequestEvent)");
        Log.i(TAG, event.getEventName());
        if(isConnected)
            socket.emit(event.getEventName(),event.getData());
        else
            queue.add(event);

    }
    private void emitQueue(){
        while(queue.size()>0){
            RequestEvent event = queue.poll();
            socket.emit(event.getEventName(),event.getData());
        }
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
