package sn.yegg.app.service;

import java.util.Optional;
import sn.yegg.app.service.dto.TrackingDTO;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.Tracking}.
 */
public interface TrackingService {
    /**
     * Save a tracking.
     *
     * @param trackingDTO the entity to save.
     * @return the persisted entity.
     */
    TrackingDTO save(TrackingDTO trackingDTO);

    /**
     * Updates a tracking.
     *
     * @param trackingDTO the entity to update.
     * @return the persisted entity.
     */
    TrackingDTO update(TrackingDTO trackingDTO);

    /**
     * Partially updates a tracking.
     *
     * @param trackingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TrackingDTO> partialUpdate(TrackingDTO trackingDTO);

    /**
     * Get the "id" tracking.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TrackingDTO> findOne(Long id);

    /**
     * Delete the "id" tracking.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
