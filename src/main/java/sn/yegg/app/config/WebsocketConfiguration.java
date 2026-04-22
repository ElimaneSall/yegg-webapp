package sn.yegg.app.config;

import java.security.Principal;
import java.util.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.*;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import sn.yegg.app.security.AuthoritiesConstants;
import sn.yegg.app.security.WebsocketAuthInterceptor;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfiguration implements WebSocketMessageBrokerConfigurer {

    public static final String IP_ADDRESS = "IP_ADDRESS";

    private final JHipsterProperties jHipsterProperties;

    public WebsocketConfiguration(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] allowedOrigins = Optional.ofNullable(jHipsterProperties.getCors().getAllowedOrigins())
            .map(origins -> origins.toArray(new String[0]))
            .orElse(new String[0]);
        List<String> allOrigins = new ArrayList<>(Arrays.asList(allowedOrigins));
        allOrigins.add("http://10.0.2.2:*");
        allOrigins.add("http://10.0.2.15:*");
        allOrigins.add("http://localhost:*");
        allOrigins.add("http://127.0.0.1:*");
        allOrigins.add("http://192.168.1.5:*");
        allOrigins.add("https://yegg-app-303905372958.europe-west1.run.app");
        allOrigins.add("https://*.run.app");
        allOrigins.add("https://*.a.run.app");
        registry
            .addEndpoint("/websocket/tracker")
            .setHandshakeHandler(defaultHandshakeHandler())
            .setAllowedOriginPatterns(allOrigins.toArray(new String[0]))
            .addInterceptors(new WebsocketAuthInterceptor(jwtDecoder()))
            .withSockJS();
    }

    // Ajoutez ce bean si nécessaire
    @Bean
    public JwtDecoder jwtDecoder() {
        // 1. On récupère le secret en Base64 depuis le fichier YAML
        String base64Secret = jHipsterProperties.getSecurity().getAuthentication().getJwt().getBase64Secret();

        // 2. On le décode en tableau de bytes
        byte[] keyBytes = Base64.getDecoder().decode(base64Secret);

        // 3. On crée la clé cryptographique (JHipster utilise par défaut HMAC SHA-512)
        SecretKey secretKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA512");

        // 4. On configure et on retourne le décodeur
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

    @Bean
    public HandshakeInterceptor httpSessionHandshakeInterceptor() {
        return new HandshakeInterceptor() {
            @Override
            public boolean beforeHandshake(
                ServerHttpRequest request,
                ServerHttpResponse response,
                WebSocketHandler wsHandler,
                Map<String, Object> attributes
            ) throws Exception {
                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                    attributes.put(IP_ADDRESS, servletRequest.getRemoteAddress());
                }
                return true;
            }

            @Override
            public void afterHandshake(
                ServerHttpRequest request,
                ServerHttpResponse response,
                WebSocketHandler wsHandler,
                Exception exception
            ) {}
        };
    }

    private DefaultHandshakeHandler defaultHandshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                Principal principal = request.getPrincipal();
                if (principal == null) {
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS));
                    principal = new AnonymousAuthenticationToken("WebsocketConfiguration", "anonymous", authorities);
                }
                return principal;
            }
        };
    }
}
