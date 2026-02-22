package sn.yegg.app.service;

import java.util.Optional;
import sn.yegg.app.service.dto.OperateurDTO;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.Operateur}.
 */
public interface OperateurService {
    /**
     * Save a operateur.
     *
     * @param operateurDTO the entity to save.
     * @return the persisted entity.
     */
    OperateurDTO save(OperateurDTO operateurDTO);

    /**
     * Updates a operateur.
     *
     * @param operateurDTO the entity to update.
     * @return the persisted entity.
     */
    OperateurDTO update(OperateurDTO operateurDTO);

    /**
     * Partially updates a operateur.
     *
     * @param operateurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OperateurDTO> partialUpdate(OperateurDTO operateurDTO);

    /**
     * Get the "id" operateur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OperateurDTO> findOne(Long id);

    /**
     * Delete the "id" operateur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
