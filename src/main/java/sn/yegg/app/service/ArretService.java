package sn.yegg.app.service;

import java.util.List;
import java.util.Optional;
import sn.yegg.app.service.dto.ArretDTO;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.Arret}.
 */
public interface ArretService {
    /**
     * Save a arret.
     *
     * @param arretDTO the entity to save.
     * @return the persisted entity.
     */
    ArretDTO save(ArretDTO arretDTO);

    /**
     * Updates a arret.
     *
     * @param arretDTO the entity to update.
     * @return the persisted entity.
     */
    ArretDTO update(ArretDTO arretDTO);

    /**
     * Partially updates a arret.
     *
     * @param arretDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ArretDTO> partialUpdate(ArretDTO arretDTO);

    /**
     * Get the "id" arret.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArretDTO> findOne(Long id);

    /**
     * Delete the "id" arret.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<ArretDTO> findNearby(Double lat, Double lng, Double radius);
}
