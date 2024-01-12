package tsw.ejer.ws;
import org.springframework.web.socket.WebSocketSession;

import jakarta.servlet.http.HttpSession;
public class SessionWS {
    private String nombre;
	private WebSocketSession session;
	private HttpSession httpSession;

	public SessionWS(String nombre, WebSocketSession session) {
		this.nombre = nombre;
		this.session = session;
	}

	public SessionWS(WebSocketSession session, HttpSession httpSession) {
		this.session=session;
		this.httpSession=httpSession;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public String getId() {
		return this.session.getId();
	}

	public String getNombre() {
		return nombre;
	}
	
	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public void setNombre(String nombre) {
		this.nombre=nombre;
	} 

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}
}
