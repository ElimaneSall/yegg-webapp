package sn.yegg.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path:}")
    private String firebaseConfigPath;

    @Value("${spring.profiles.active:prod}")
    private String activeProfile;

    private final Logger log = LoggerFactory.getLogger(FirebaseConfig.class);

    @PostConstruct
    public void initializeFirebase() {
        // Éviter double initialisation
        if (!FirebaseApp.getApps().isEmpty()) {
            log.info("ℹ️ Firebase déjà initialisé");
            return;
        }

        try {
            FirebaseOptions options;

            // 🎯 MODE CLOUD RUN : Utiliser Application Default Credentials (ADC)
            if ("prod".equals(activeProfile) || isRunningOnCloudRun()) {
                log.info("🔥 Initialisation Firebase avec ADC (Cloud Run)");
                options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setProjectId("senbus-2b9f0")
                    .build();
            }
            // 🎯 MODE LOCAL : Charger le fichier JSON depuis resources
            else if (!firebaseConfigPath.isEmpty()) {
                log.info("🔥 Initialisation Firebase avec fichier JSON: {}", firebaseConfigPath);

                InputStream serviceAccount = new ClassPathResource(firebaseConfigPath).getInputStream();
                if (serviceAccount == null) {
                    log.error("❌ Fichier Firebase introuvable: {}", firebaseConfigPath);
                    return;
                }

                options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId("senbus-2b9f0")
                    .build();
            }
            // ⚠️ Fallback : Firebase optionnel
            else {
                log.warn("⚠️ Firebase non configuré - les notifications push seront désactivées");
                return;
            }

            FirebaseApp.initializeApp(options);
            log.info("✅ Firebase Admin SDK initialisé avec succès (profil: {})", activeProfile);
        } catch (IOException e) {
            log.error("❌ Erreur d'initialisation Firebase: {}", e.getMessage(), e);
            // 🎯 Ne pas faire crasher l'app si Firebase est optionnel
            // throw new RuntimeException("Firebase initialization failed", e);
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        // Vérifier que Firebase est initialisé avant de retourner l'instance
        if (FirebaseApp.getApps().isEmpty()) {
            log.warn("⚠️ Firebase non initialisé - FirebaseMessaging peut échouer");
        }
        return FirebaseMessaging.getInstance();
    }

    // Détecter si on tourne sur Cloud Run
    private boolean isRunningOnCloudRun() {
        return System.getenv("K_SERVICE") != null || System.getenv("CLOUD_RUN_JOB") != null;
    }
}
