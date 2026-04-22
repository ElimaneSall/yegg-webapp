package sn.yegg.app.security;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class WebsocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtDecoder jwtDecoder;

    private final Logger LOG = LoggerFactory.getLogger(WebsocketAuthInterceptor.class);

    public WebsocketAuthInterceptor(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public boolean beforeHandshake(
        ServerHttpRequest request,
        ServerHttpResponse response,
        WebSocketHandler wsHandler,
        Map<String, Object> attributes
    ) {
        // 1. Extraire le token (query param OU header)
        String token = null;
        if (request.getURI().getQuery() != null) {
            String[] params = request.getURI().getQuery().split("&");
            for (String param : params) {
                if (param.startsWith("access_token=")) {
                    token = param.split("=")[1];
                    break;
                }
            }
        }

        // 2. Valider le token JWT
        if (token != null) {
            LOG.info("🔑 Token reçu pour WebSocket: {}", token.substring(0, 20) + "...");

            try {
                Jwt jwt = jwtDecoder.decode(token);
                String username = jwt.getSubject();

                // 3. Créer l'authentification et la stocker dans la session WebSocket
                var authorities = jwt.getClaimAsStringList("auth").stream().map(SimpleGrantedAuthority::new).toList();

                var auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                attributes.put("user", auth); // Utilisé par Spring pour résoudre /user/{id}/queue
                return true;
            } catch (Exception e) {
                System.err.println("❌ Token invalide: " + e.getMessage());
                return false; // Rejette la connexion
            }
        }
        LOG.warn("⚠️ Aucun access_token dans l'URL WebSocket");

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {}
}
