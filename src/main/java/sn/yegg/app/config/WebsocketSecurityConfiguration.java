package sn.yegg.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import sn.yegg.app.security.AuthoritiesConstants;

@Configuration
public class WebsocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
            .nullDestMatcher()
            .permitAll()
            .simpDestMatchers("/topic/bus-positions")
            .permitAll()
            .simpDestMatchers("/topic/bus-alerts")
            .permitAll()
            .simpDestMatchers("/topic/activity")
            .authenticated()
            .simpTypeMatchers(SimpMessageType.CONNECT, SimpMessageType.SUBSCRIBE, SimpMessageType.DISCONNECT)
            .permitAll()
            .simpTypeMatchers(SimpMessageType.MESSAGE)
            .authenticated()
            .anyMessage()
            .denyAll();
    }

    /**
     * Disables CSRF for Websockets.
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
