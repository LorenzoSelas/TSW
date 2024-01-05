import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import tsw.ejer.http.UserController;
import tsw.ejer.model.User;
import jakarta.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
@Component
public class WSGames extends TextWebSocketHandler{
    private List<WebSocketSession> sessions = new ArrayList<>();
	private Map<String, SessionWS> sessionsByNombre= new HashMap<>();
	private Map<String, SessionWS> sessionsById= new HashMap<>();
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		HttpHeaders headers = session.getHandshakeHeaders();

		Collection<List<String>> values = headers.values();
		String httpId = null;
		for (List<String> value : values) {
			for (String cadena : value) {
				if (cadena.startsWith("JSESSIONID")) {
					httpId = cadena.substring(11);
					break;
				}
			}
			if (httpId!=null)
				break;
		}


//		String httpId=session.getUri().getQuery();
//		 httpId= httpId.substring(7);
		 HttpSession httpSession =UserControler.httpSessions.get(httpId);
		 
		 SessionWS sessionWS = new SessionWS(session,httpSession);
		 User user=(User) httpSession.getAttribute("user");
		 sessionWS.setNombre(user.getNombre());
		 user.setSessionWS(sessionWS);
		 
		 this.sessionsById.put(session.getId(), sessionWS);
		 
		this.sessions.add(session);
		
	}
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message)
			throws Exception {
		
		JSONObject jso = new JSONObject(message.getPayload());
		String tipo = jso.getString("tipo");
		if(tipo.equals("IDENT")) {
			String nombre = jso.getString("nombre");
			SessionWS sesionWS = this.sessionsById.get(session.getId());
			sesionWS.setNombre(nombre);
			this.sessionsByNombre.put(nombre, sesionWS);
			this.sessionsById.put(session.getId(), sesionWS);
			this.difundir(session,"tipo", "NUEVO USUARIO", "nombre", nombre);
			this.bienvenida(session);
			return;
		}
		if(tipo.equals("MENSAJE PRIVADO")) {
			String destinatario = jso.getString("destinatario");
			String texto = jso.getString("texto");
			String remitente = this.sessionsById.get(session.getId()).getNombre();
			JSONObject respuesta = new JSONObject().
					put("tipo", "MENSAJE PRIVADO").
					put("texto",texto).
					put("remitente", remitente);
			
			SessionWS sesionDestinatario= this.sessionsByNombre.get(destinatario);
			
			if(sesionDestinatario == null) {
				respuesta.put("tipo", "SE FUE");
				TextMessage messageRespuesta = new TextMessage(respuesta.toString());
				session.sendMessage(messageRespuesta);
			} else {
				WebSocketSession sessionDestinatario = (this.sessionsByNombre.get(destinatario)).getSession();
				TextMessage messageRespuesta = new TextMessage(respuesta.toString());
				sessionDestinatario.sendMessage(messageRespuesta);
			}
			return;
		}
		/*for(WebSocketSession s: this.sessions)
			s.sendMessage(message);*/
	}
	
	private void bienvenida(WebSocketSession sessionDelTipoQueAcabaDeLegar) {
		JSONObject jso = new JSONObject().put("tipo", "BIENVENIDA");
		JSONArray jsaUsuarios = new JSONArray();
		
		Collection<SessionWS> usuariosConectados = this.sessionsByNombre.values();
		for(SessionWS usuarioConectado: usuariosConectados) {
			if(usuarioConectado.getSession()!= sessionDelTipoQueAcabaDeLegar) {
				jsaUsuarios.put(usuarioConectado.getNombre());
			}
		}
		jso.put("usuarios", jsaUsuarios);
		try {
			sessionDelTipoQueAcabaDeLegar.sendMessage(new TextMessage(jso.toString()));
		}catch (IOException e){
			this.elimiarSesion(sessionDelTipoQueAcabaDeLegar);
		}
		
	}
	private void difundir(WebSocketSession remitente, Object... clavesyValores) {
		//tipo, NUEVO USUARIO, nombre, Pepe, edad, 20, curso, 4º
		JSONObject jso = new JSONObject();
		for(int i=0; i< clavesyValores.length; i+=2) {
			String clave = clavesyValores[i].toString();
			String valor = clavesyValores[i+1].toString();
			jso.put(clave, valor);
		}
		for(WebSocketSession session: this.sessions) {
			if(session!= remitente) {
				try {
					session.sendMessage(new TextMessage(jso.toString()));
				} catch (IOException e) {
					this.elimiarSesion(session);
				}
			}
		}
	}
	private void elimiarSesion(WebSocketSession session) {
		this.sessions.remove(session);
		SessionWS sessionWS= this.sessionsById.remove(session.getId());
		this.sessionsByNombre.remove(sessionWS.getNombre());
	}
	public void afterCnnectionClosed(WebSocketSession sessions, CloseStatus status ) throws Exception {
		this.sessionsById.remove(sessions.getId());
		this.sessionsByNombre.remove(sessions.getId()).getNombre();
	}
	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception)
			throws Exception {
	}
}
