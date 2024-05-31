package tsw.ejer.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import jakarta.servlet.http.HttpSession;
import tsw.ejer.dao.UserDAO;
import tsw.ejer.http.UserController;
import tsw.ejer.model.User;

@Component
public class WSUsuarios extends TextWebSocketHandler {
	private List<SessionWS> sessions = new ArrayList<>();
	private Map<String, SessionWS> sessionsByNombre = new HashMap<>();
	private Map<String, SessionWS> sessionsById = new HashMap<>();

	@Autowired
	private UserDAO userDAO;
    @Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("conexion establecida en WSUsuarios" + session.getId());
		
		Map<String, Object> attributes = session.getAttributes();
        String userId = (String) attributes.get("userId");

        System.out.println("User ID: " + userId);

        User user;
        try {
            user = this.userDAO.findById(userId).orElseThrow();
            SessionWS sessionWS = new SessionWS(user.getNombre(), session);
			user.setSessionWS(sessionWS);
            this.sessions.add(sessionWS);
            this.sessionsByNombre.put(user.getNombre(), sessionWS);
        } catch (Exception e) {
            SessionWS sessionWS = new SessionWS(null, session);
            this.sessions.add(sessionWS);
        }
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
		}else if (tipo.equals("MENSAJE PRIVADO")) {
			try {
				String nombre = jso.getString("destinatario");
				User user = UserController.usersByName.get(nombre);
				user.getSessionWS().getSession().sendMessage(message);
			} catch (Exception e) {
				// TODO: handle exception
			}
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
