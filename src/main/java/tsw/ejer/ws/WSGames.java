package tsw.ejer.ws;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.json.JSONArray;
import org.json.JSONObject;

@Component
public class WSGames extends TextWebSocketHandler {
	private List<WebSocketSession> sessions = new ArrayList<>();
	private Map<String, List<WebSocketSession>> sessionsByRoom = new HashMap<>();
	private Map<String, WebSocketSession> sessionsById = new HashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("Conexión establecida:" + session.getId());

        URI uri = session.getUri();
        String path = uri.getPath();

		Map<String, Object> attributes = session.getAttributes();
        String userId = (String) attributes.get("userId");
		if(userId==null)
			userId = uri.getQuery().split("=")[1];

		sessionsById.put(userId, session);
        // Extraer el roomId de la URI
        String[] pathSegments = path.split("/");
        if (pathSegments.length >= 3) {
            String roomId = pathSegments[2];
            
            // Ahora tienes el roomId y puedes manejar la conexión en consecuencia
            // ...
			try {
				sessionsByRoom.get(roomId).add(session);
			} catch (Exception e) {
				List<WebSocketSession> lista = new ArrayList<>();
				sessionsByRoom.put(roomId, lista);
			}
            this.sessions.add(session);
        } else {
			try {
				sessionsByRoom.get("General").add(session);
			} catch (Exception e) {
				List<WebSocketSession> lista = new ArrayList<>();
				sessionsByRoom.put("General", lista);
			}
        }
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message)
			throws Exception {

		JSONObject jso = new JSONObject(message.getPayload());
		String tipo = jso.getString("tipo");
		String nombre;
		if (tipo.equals("IDENT")) {
			nombre = jso.getString("nombre");
			SessionWS sesionWS = new SessionWS(nombre, session);
			sesionWS.setNombre(nombre);
			this.difundir(session, "tipo", "NUEVO USUARIO", "nombre", nombre);
			this.bienvenida(session);
			return;
		} else if (tipo.equals("TURNO")) {
			String id = jso.getString("userId");
			WebSocketSession ses = sessionsById.get(id);
			TextMessage mes = new TextMessage("your turn");
			ses.sendMessage(mes);
			return;
		}else if (tipo.equals("ACTUALIZACION")) {
			String casillas = jso.getString("userId");
			List<WebSocketSession> ses = sessionsByRoom.get(jso.getString("idTablero"));
			TextMessage mes = new TextMessage(casillas);
			for (WebSocketSession webSocketSession : ses) {
				webSocketSession.sendMessage(mes);	
			}
			return;
		} else {
			try {
				nombre = jso.getString("nombre");
				String mensaje = jso.getString("texto");
				difundir(session, "tipo","MENSAJE","texto",mensaje);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private void bienvenida(WebSocketSession sessionDelTipoQueAcabaDeLegar) {
		JSONObject jso = new JSONObject().put("tipo", "BIENVENIDA");
		JSONArray jsaUsuarios = new JSONArray();
/* 
		Collection<SessionWS> usuariosConectados = this.sessionsByNombre.values();
		for (SessionWS usuarioConectado : usuariosConectados) {
			if (usuarioConectado.getSession() != sessionDelTipoQueAcabaDeLegar) {
				jsaUsuarios.put(usuarioConectado.getNombre());
			}
		}*/
		jso.put("usuarios", jsaUsuarios);
		try {
			sessionDelTipoQueAcabaDeLegar.sendMessage(new TextMessage(jso.toString()));
		} catch (IOException e) {
			this.eliminarSesion(sessionDelTipoQueAcabaDeLegar);
		}

	}

	private void difundir(WebSocketSession remitente, Object... clavesyValores) {
		// tipo, NUEVO USUARIO, nombre, Pepe, edad, 20, curso, 4º
		JSONObject jso = new JSONObject();
		for (int i = 0; i < clavesyValores.length; i += 2) {
			String clave = clavesyValores[i].toString();
			String valor = clavesyValores[i + 1].toString();
			jso.put(clave, valor);
		}
		for (WebSocketSession session : this.sessions) {
			if (session != remitente) {
				try {
					session.sendMessage(new TextMessage(jso.toString()));
				} catch (IOException e) {
					this.eliminarSesion(session);
				}
			}
		}
	}

	private void eliminarSesion(WebSocketSession session) {
		this.sessions.remove(session);
	}

	public void afterConnectionClosed(WebSocketSession sessions, CloseStatus status) throws Exception {
	}

	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception)
			throws Exception {
	}
}
