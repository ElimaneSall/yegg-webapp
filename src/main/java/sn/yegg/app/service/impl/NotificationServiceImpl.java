package sn.yegg.app.service.impl;

import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.Arret;
import sn.yegg.app.domain.Bus;
import sn.yegg.app.domain.Notification;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.domain.enumeration.NotificationStatus;
import sn.yegg.app.domain.enumeration.NotificationType;
import sn.yegg.app.domain.enumeration.Priority;
import sn.yegg.app.repository.NotificationRepository;
import sn.yegg.app.service.AlerteApprocheService;
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

    public void sendAlertNotification(
        Utilisateur user,
        Bus bus,
        Arret arret,
        double distance,
        AlerteApprocheServiceImpl.ThresholdCheckResult result
    ) {
        Notification notification = new Notification();
        notification.setUtilisateur(user);
        notification.setType(NotificationType.BUS_APPROACHING);
        notification.setPriorite(Priority.HIGH);
        notification.setStatut(NotificationStatus.PENDING);
        notification.setDateCreation(Instant.now());
        notification.setLu(false);

        // Titre de la notification
        String titre = String.format("Bus %s en approche", bus.getNumeroVehicule());
        notification.setTitre(titre);

        // Message selon le type de déclenchement
        String message;
        if ("DISTANCE".equals(result.getTriggeredBy())) {
            message = String.format(
                "Le bus %s est à %.0f mètres de l'arrêt %s",
                bus.getNumeroVehicule(),
                result.getDistanceValue(),
                arret.getNom()
            );
        } else if ("TIME".equals(result.getTriggeredBy())) {
            message = String.format(
                "Le bus %s sera à l'arrêt %s dans %d secondes",
                bus.getNumeroVehicule(),
                arret.getNom(),
                result.getTimeValue()
            );
        } else {
            message = String.format(
                "Le bus %s approche de l'arrêt %s (distance: %.0fm, temps: %ds)",
                bus.getNumeroVehicule(),
                arret.getNom(),
                result.getDistanceValue(),
                result.getTimeValue()
            );
        }

        notification.setMessage(message);

        // Données supplémentaires pour l'application mobile
        String donnees;
        donnees = String.format(
            "{\"busId\":%d,\"busNumero\":\"%s\",\"arretId\":%d,\"arretNom\":\"%s\",\"distance\":%.2f,\"temps\":%d}",
            bus.getId(),
            bus.getNumeroVehicule(),
            arret.getId(),
            arret.getNom(),
            result.getDistanceValue(),
            result.getTimeValue() != null ? result.getTimeValue() : 0
        );
        notification.setDonnees(donnees);

        notificationRepository.save(notification);

        // Ici, vous pourriez intégrer Firebase Cloud Messaging pour les notifications push
        // sendPushNotification(user, notification);

        LOG.info("Notification créée pour l'utilisateur {} concernant le bus {}", user.getId(), bus.getNumeroVehicule());
    }
}
