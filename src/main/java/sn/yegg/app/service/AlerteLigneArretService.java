package sn.yegg.app.service;

import java.util.Optional;
import sn.yegg.app.service.dto.AlerteLigneArretDTO;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.AlerteLigneArret}.
 */
public interface AlerteLigneArretService {
    /**
     * Save a alerteLigneArret.
     *
     * @param alerteLigneArretDTO the entity to save.
     * @return the persisted entity.
     */
    AlerteLigneArretDTO save(AlerteLigneArretDTO alerteLigneArretDTO);

    /**
     * Updates a alerteLigneArret.
     *
     * @param alerteLigneArretDTO the entity to update.
     * @return the persisted entity.
     */
    AlerteLigneArretDTO update(AlerteLigneArretDTO alerteLigneArretDTO);

    /**
     * Partially updates a alerteLigneArret.
     *
     * @param alerteLigneArretDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlerteLigneArretDTO> partialUpdate(AlerteLigneArretDTO alerteLigneArretDTO);

    /**
     * Get the "id" alerteLigneArret.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlerteLigneArretDTO> findOne(Long id);

    /**
     * Delete the "id" alerteLigneArret.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
