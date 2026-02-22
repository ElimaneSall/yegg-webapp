package sn.yegg.app.service;

import java.util.Optional;
import sn.yegg.app.service.dto.AlerteDTO;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.Alerte}.
 */
public interface AlerteService {
    /**
     * Save a alerte.
     *
     * @param alerteDTO the entity to save.
     * @return the persisted entity.
     */
    AlerteDTO save(AlerteDTO alerteDTO);

    /**
     * Updates a alerte.
     *
     * @param alerteDTO the entity to update.
     * @return the persisted entity.
     */
    AlerteDTO update(AlerteDTO alerteDTO);

    /**
     * Partially updates a alerte.
     *
     * @param alerteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlerteDTO> partialUpdate(AlerteDTO alerteDTO);

    /**
     * Get the "id" alerte.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlerteDTO> findOne(Long id);

    /**
     * Delete the "id" alerte.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
