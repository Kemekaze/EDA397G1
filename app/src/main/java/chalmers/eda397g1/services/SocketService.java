package chalmers.eda397g1.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.PriorityQueue;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import chalmers.eda397g1.events.AvailableSessionsEvent;
import chalmers.eda397g1.events.CardsEvent;
import chalmers.eda397g1.events.CreateSessionEvent;
import chalmers.eda397g1.events.JoinSessionEvent;
import chalmers.eda397g1.events.KickedEvent;
import chalmers.eda397g1.events.LobbyUpdateEvent;
import chalmers.eda397g1.events.LoginEvent;
import chalmers.eda397g1.events.ProjectColumnsEvent;
import chalmers.eda397g1.events.ReposProjectsEvent;
import chalmers.eda397g1.events.RequestEvent;
import chalmers.eda397g1.events.SignedoutEvent;
import chalmers.eda397g1.events.StartGameEvent;
import chalmers.eda397g1.events.UserProjectsEvent;
import chalmers.eda397g1.events.VoteItemResultEvent;
import chalmers.eda397g1.events.VoteItemEvent;
import chalmers.eda397g1.events.VoteOnLowestEffortResultEvent;
import chalmers.eda397g1.R;
import chalmers.eda397g1.events.VoteOnLowestEffortEvent;
import chalmers.eda397g1.events.VoteRoundResultEvent;
import chalmers.eda397g1.resources.Constants;
import chalmers.eda397g1.resources.Queries;
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
        InputStream in = getApplicationContext().getResources().openRawResource(R.raw.my_ca);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) certificateFactory.generateCertificate(in);
        String alias = cert.getSubjectX500Principal().getName();
        // Create keystore and add to ssl context
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null);
        trustStore.setCertificateEntry(alias, cert);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(trustStore, null);
        KeyManager[] keyManagers = kmf.getKeyManagers();

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(trustStore);

        TrustManager[] managers  = tmf.getTrustManagers();
        SSLContext sc = SSLContext.getInstance("TLS");
        // should use managers above, not working atm
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
        socket.on(Constants.SocketEvents.SIGNOUT, eventSignedout);
        //Thirdparty athentication
        socket.on(Constants.SocketEvents.AUTHENTICATE_AUTOLOGIN, eventAuthenticatedAutoLogin);
        socket.on(Constants.SocketEvents.AUTHENTICATE_GITHUB, eventAuthenticatedGithub);
        socket.on(Constants.SocketEvents.AUTHENTICATE_BITBUCKET, eventAuthenticatedBitbucket);
        //session
        socket.on(Constants.SocketEvents.REPOSITORIES, eventRepositories);
        socket.on(Constants.SocketEvents.REPOSITORY_PROJECTS, eventRepositoryProjects);
        socket.on(Constants.SocketEvents.PROJECT_COLUMNS, eventProjectColumns);
        socket.on(Constants.SocketEvents.COLUMN_CARDS, eventColumnCards);
        socket.on(Constants.SocketEvents.AVAILABLE_SESSIONS, eventAvailableGames);

        socket.on(Constants.SocketEvents.SESSION_KICKED, eventSessionKicked);
        socket.on(Constants.SocketEvents.SESSION_CREATED, eventSessionCreated);
        socket.on(Constants.SocketEvents.SESSION_CREATE, eventCreateSession);
        socket.on(Constants.SocketEvents.SESSION_JOIN, eventJoinSession);
        socket.on(Constants.SocketEvents.SESSION_LEAVE, eventLeaveSession);

        socket.on(Constants.SocketEvents.SESSION_START, eventStartGame);
        socket.on(Constants.SocketEvents.SESSION_STARTED, eventStartedGame);
        socket.on(Constants.SocketEvents.SESSION_CLIENTS, eventSessionClientsEvent);
        socket.on(Constants.SocketEvents.VOTE_LOWEST, eventVoteOnLowest);
        socket.on(Constants.SocketEvents.VOTE_LOWEST_RESULT, eventVoteOnLowestResult);
        socket.on(Constants.SocketEvents.VOTE, eventVoteItem);
        socket.on(Constants.SocketEvents.VOTE_ROUND_RESULT, eventVoteRoundResult);
        socket.on(Constants.SocketEvents.VOTE_RESULT, eventVoteItemResult);
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
            String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            socket.emit(Constants.SocketEvents.AUTHENTICATE_AUTOLOGIN,Queries.query("phone_id", android_id));
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

    private Emitter.Listener eventRepositories = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventRepositories");
            for(int i = 0; i < args.length; i++){
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new ReposProjectsEvent(args));
        }
    };

    private Emitter.Listener eventRepositoryProjects = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventRepositoryProjects()");
            for(int i = 0; i < args.length; i++){
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new UserProjectsEvent(args));
        }
    };
    private Emitter.Listener eventProjectColumns = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventProjectColumns()");
            for(int i = 0; i < args.length; i++){
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new ProjectColumnsEvent(args));
        }
    };
    private Emitter.Listener eventColumnCards = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventColumnCards()");
            for(int i = 0; i < args.length; i++){
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new CardsEvent(args));
        }
    };
    private Emitter.Listener eventAvailableGames = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventAvailableGames()");
            for(int i = 0; i < args.length; i++){
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new AvailableSessionsEvent(args));
        }
    };

    private Emitter.Listener eventCreateSession = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventCreateSession()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new CreateSessionEvent(args));
        }
    };

    private Emitter.Listener eventSessionCreated = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventSessionCreated()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            RequestEvent event = new RequestEvent(Constants.SocketEvents.AVAILABLE_SESSIONS);
            EventBus.getDefault().post(event);
        }
    };

    private Emitter.Listener eventLeaveSession = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventLeaveSession()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }

        }
    };
    private Emitter.Listener eventJoinSession = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventJoinSession()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new JoinSessionEvent(args));
        }
    };

    private Emitter.Listener eventStartGame = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventStartGame()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new StartGameEvent(args));
        }
    };
    private Emitter.Listener eventSessionKicked = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventSessionKicked()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new KickedEvent(args));
        }
    };
    private Emitter.Listener eventStartedGame = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventStartedGame()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new StartGameEvent(args));
        }
    };

    private Emitter.Listener eventSessionClientsEvent = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventSessionClientsEvent()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().postSticky(new LobbyUpdateEvent(args));
        }
    };

    private Emitter.Listener eventVoteOnLowestResult = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventVoteOnLowestResult()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new VoteOnLowestEffortResultEvent(args));
        }
    };

    private Emitter.Listener eventVoteOnLowest = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventVoteOnLowest()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new VoteOnLowestEffortEvent(args));
        }
    };

    private Emitter.Listener eventVoteItem = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventVoteItem()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new VoteItemEvent(args));
        }
    };



    private Emitter.Listener eventVoteRoundResult = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventVoteRoundResult()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new VoteRoundResultEvent(args));
        }
    };

    private Emitter.Listener eventVoteItemResult = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventVoteItemResult()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new VoteItemResultEvent(args));
        }
    };

    private Emitter.Listener eventSignedout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventSignedout()");
            for(int i = 0; i < args.length; i++) {
                Log.i(TAG,  args[i].toString());
            }
            EventBus.getDefault().post(new SignedoutEvent(args));
        }
    };


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
            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
            return hv.verify("xr-plan.se", session);
        }
    };
    private TrustManager[] trustAllCerts = new TrustManager[]{
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }
    };



}
