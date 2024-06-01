package tsw.ejer.ws;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import org.json.JSONObject;
@ClientEndpoint
public class WSClient {
    private CountDownLatch latch = new CountDownLatch(1);
    private Session session;
	private String url;
	private IWSListener listener;
 
    public WSClient(String url,IWSListener listener) throws Exception {
    	this.url = url;
    	this.listener=listener;
		this.open();
	}
    
    private void open() throws Exception {
    	WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(this, new URI(url));
        this.latch.await();    	
    }

	@OnOpen
    public void onOpen(Session session) {
        this.session = session;
        latch.countDown();
        System.out.println("conexión del cliente establecida");
        //this.receiver.onResponseReceived(new JSONObject().put("type", "action").put("action", "WS_PREPARED"));
    }
	
	@OnError
	public void onError(Session session, Throwable t) {
		this.session = session;
        t.printStackTrace();
		latch.countDown();
		//this.receiver.onResponseReceived(new JSONObject().put("type", "action").put("action", "WS_ERROR"));
	}
 
    @OnMessage
    public void onText(String message, Session session) {
    	System.out.println(message);
        try {
    	    this.listener.notify(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //JSONObject jso = new JSONObject(message);
        //String type = jso.getString("type");
        //if (type.equals("action"))
        //	this.receiver.onResponseReceived(jso);
    }
 
    @OnClose
    public void onClose(CloseReason reason, Session session) {
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
    }
 
    public CountDownLatch getLatch() {
        return latch;
    }
    
    public void sendMessage(JSONObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
