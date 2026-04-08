package sn.yegg.app.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.Notification;
import sn.yegg.app.repository.NotificationRepository;
import sn.yegg.app.service.NotificationService;
import sn.yegg.app.service.dto.NotificationDTO;
import sn.yegg.app.service.mapper.NotificationMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.Notification}.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public NotificationDTO save(NotificationDTO notificationDTO) {
        LOG.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    @Override
    public NotificationDTO update(NotificationDTO notificationDTO) {
        LOG.debug("Request to update Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    @Override
    public Optional<NotificationDTO> partialUpdate(NotificationDTO notificationDTO) {
        LOG.debug("Request to partially update Notification : {}", notificationDTO);

        return notificationRepository
            .findById(notificationDTO.getId())
            .map(existingNotification -> {
                notificationMapper.partialUpdate(existingNotification, notificationDTO);

                return existingNotification;
            })
            .map(notificationRepository::save)
            .map(notificationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        LOG.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id).map(notificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }

    @Override
    public void sendPushNotification(String token, String title, String body) {}

    /**
     * Envoie une notification push FCM avec données de navigation
     */
    public void sendPushNotificationWithNavigation(String token, String title, String body, Map<String, String> data) {
        try {
            // 🧹 Nettoyer le token : supprimer TOUT whitespace (newline, espace, tab)
            String cleanToken = token == null ? null : token.replaceAll("[\\s\\n\\r\\t]+", "").trim();

            // Validation minimale du format
            if (cleanToken == null || cleanToken.length() < 100) {
                LOG.error("Token FCM invalide ou trop court: length={}", cleanToken == null ? 0 : cleanToken.length());
                return;
            }

            com.google.firebase.messaging.Notification notification = com.google.firebase.messaging.Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

            Message message = Message.builder()
                .setToken(cleanToken)
                .setNotification(notification)
                .putAllData(data)
                .putData("click_action", "OPEN_ALERT")
                .build();

            String response = firebaseMessaging.send(message);
            LOG.info("FCM envoyé: {} | Token: {}", response, cleanToken.substring(0, 20) + "...");
        } catch (FirebaseMessagingException e) {
            LOG.error("Erreur FCM (code: {}): {}", e.getErrorCode(), e.getMessage(), e);

            // Supprimer les tokens définitivement invalides
            if ("UNREGISTERED".equals(e.getErrorCode().name()) || "INVALID_ARGUMENT".equals(e.getErrorCode().name())) {
                LOG.warn("Token invalide - À supprimer de la DB: {}", token.substring(0, 20) + "...");
            }
        } catch (Exception e) {
            LOG.error("Erreur inattendue FCM: {}", e.getMessage(), e);
        }
    }
}
