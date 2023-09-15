package camtrack.cmeet.websocket;

import androidx.lifecycle.MutableLiveData;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

public class webSocketClient extends WebSocketClient {
    public static MutableLiveData<String>  Error =  new MutableLiveData<>();
    public  MutableLiveData<String>  _Message =  new MutableLiveData<>();;

    public webSocketClient(URI serverUri) {
        super(serverUri);
    }
    private static final int CONNECTION_TIMEOUT = 5000; // Set the timeout value in milliseconds
    private Timer connectionTimer;

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to WebSocket");
        // Send a message to the server
        Message message = new Message("Client1", "Hello, Server!");
        send(message.toJson());
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);

        _Message.postValue(message);
        // Handle the received message
        //Message receivedMessage = Message.fromJson(message);
        //_Message.setValue(receivedMessage.toJson());
       // System.out.println("Sender: " + receivedMessage.getSender());
        //System.out.println("Content: " + receivedMessage.getMeetingId());
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        stopConnectionTimer();
        System.out.println("Connection closed. Code: " + code + ", Reason: " + reason);
        Error.postValue(reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("WebSocket error: " + ex.getMessage());
        Error.postValue(ex.getMessage());
    }

    public void connect() {
        System.out.println("***************Connection innitiated******");
        //startConnectionTimer();
        super.connect();
    }

    private void startConnectionTimer() {
        connectionTimer = new Timer();
        connectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Timeout reached, handle the timeout event

               if(isOpen())
               {
                   close();
                   System.out.println("Connection timeout");
               }
            }
        }, CONNECTION_TIMEOUT);
    }

    private void stopConnectionTimer()
    {
        Error.postValue("Timeout Error");
        if (connectionTimer != null) {
            connectionTimer.cancel();
            connectionTimer = null;
        }

    }

    @Override
    public boolean isOpen() {
        return super.isOpen();
    }
}