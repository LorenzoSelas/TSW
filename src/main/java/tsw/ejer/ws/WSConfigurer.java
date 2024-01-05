package tsw.ejer.ws;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
@Configuration
@EnableWebSocket
public class WSConfigurer implements WebSocketConfigurer{
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.
		addHandler(new WSGames(), "/wsGames").
		setAllowedOrigins("*").
		addHandler(new WSUsuarios(), "/wsUusarios").
		setAllowedOrigins("*").
		addInterceptors(new HttpSessionHandshakeInterceptor());
		
		
	}
}
