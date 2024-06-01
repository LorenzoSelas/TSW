package tsw.ejer.ws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
@Configuration
@EnableWebSocket
public class WSConfigurer implements WebSocketConfigurer{

    @Autowired
    private WSUsuarios wsUsuariosHandler;
    @Autowired
    private WSGames wsGamesHandler;
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(wsGamesHandler, "/wsGames/*")
                .setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
        
        registry.addHandler(wsUsuariosHandler, "/wsUsuarios")
                .setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
	}
}
