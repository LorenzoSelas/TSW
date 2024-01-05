package tsw.ejer.ws;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.json.JSONObject;
import tsw.ejer.http.UserController;
import tsw.ejer.model.User;

@Component
public class WSUsuarios extends TextWebSocketHandler {
    @Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("conexion estableciad " + session.getId());
	
		
	}
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message)
			throws Exception {
		
		JSONObject jso = new JSONObject(message.getPayload());
		String tipo = jso.getString("tipo");
		if(tipo.equals("TOKEN VALIDO")) {
			String token = jso.getString("token");
			
			User user= UserController.usersByToken.get(token);
			TextMessage respuesta= new TextMessage("404");
			if(user!=null)
				respuesta= new TextMessage(user.getId());
			
			session.sendMessage(respuesta);
			session.close();
		}
		
	}
	
	
	public void afterCnnectionClosed(WebSocketSession sessions, CloseStatus status ) throws Exception {
		
	}
	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception)
			throws Exception {
	}

}
