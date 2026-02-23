package sn.yegg.app.service;

import java.util.Optional;
import sn.yegg.app.domain.Arret;
import sn.yegg.app.domain.Bus;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.service.dto.NotificationDTO;
import sn.yegg.app.service.impl.AlerteApprocheServiceImpl;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.Notification}.
 */
public interface NotificationService {
    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    NotificationDTO save(NotificationDTO notificationDTO);

    /**
     * Updates a notification.
     *
     * @param notificationDTO the entity to update.
     * @return the persisted entity.
     */
    NotificationDTO update(NotificationDTO notificationDTO);

    /**
     * Partially updates a notification.
     *
     * @param notificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NotificationDTO> partialUpdate(NotificationDTO notificationDTO);

    /**
     * Get the "id" notification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotificationDTO> findOne(Long id);

    /**
     * Delete the "id" notification.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void sendAlertNotification(
        Utilisateur utilisateur,
        Bus bus,
        Arret arretCible,
        double distance,
        AlerteApprocheServiceImpl.ThresholdCheckResult thresholdResult
    );
}
