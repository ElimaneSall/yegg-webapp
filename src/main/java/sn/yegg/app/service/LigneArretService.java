package sn.yegg.app.service;

import java.util.Optional;
import sn.yegg.app.service.dto.LigneArretDTO;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.LigneArret}.
 */
public interface LigneArretService {
    /**
     * Save a ligneArret.
     *
     * @param ligneArretDTO the entity to save.
     * @return the persisted entity.
     */
    LigneArretDTO save(LigneArretDTO ligneArretDTO);

    /**
     * Updates a ligneArret.
     *
     * @param ligneArretDTO the entity to update.
     * @return the persisted entity.
     */
    LigneArretDTO update(LigneArretDTO ligneArretDTO);

    /**
     * Partially updates a ligneArret.
     *
     * @param ligneArretDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LigneArretDTO> partialUpdate(LigneArretDTO ligneArretDTO);

    /**
     * Get the "id" ligneArret.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LigneArretDTO> findOne(Long id);

    /**
     * Delete the "id" ligneArret.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
