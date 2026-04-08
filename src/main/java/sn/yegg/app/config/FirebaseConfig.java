package sn.yegg.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() {
        try {
            // Charger le fichier de service account depuis resources
            InputStream serviceAccount = new ClassPathResource("firebase/senbus-adminsdk.json").getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId("senbus-2b9f0") // ✅ Votre projet
                .build();

            // Initialiser Firebase (vérifier qu'il n'est pas déjà initialisé)
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase Admin SDK initialisé avec succès");
            } else {
                System.out.println("ℹ️ Firebase déjà initialisé");
            }
        } catch (IOException e) {
            System.err.println("❌ Erreur d'initialisation Firebase: " + e.getMessage());
            throw new RuntimeException("Firebase initialization failed", e);
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        return FirebaseMessaging.getInstance();
    }
}
