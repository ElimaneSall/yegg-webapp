package sn.yegg.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/api/**")
            .allowedOrigins(
                "http://localhost:3000",
                "http://127.0.0.1:3000",
                "http://localhost:*", // Accepte tous les ports localhost
                "http://127.0.0.1:*", // Accepte tous les ports 127.0.0.1
                "http://10.0.2.15:*", // Tous les ports sur émulateur
                "http://10.0.2.2:*", // ← Émulateur Android vers host
                "http://192.168.*.*:*", // Réseau local
                "https://yegg-app-303905372958.europe-west1.run.app"
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
