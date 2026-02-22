package sn.yegg.app.service;

import java.util.Optional;
import sn.yegg.app.service.dto.LigneDTO;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.Ligne}.
 */
public interface LigneService {
    /**
     * Save a ligne.
     *
     * @param ligneDTO the entity to save.
     * @return the persisted entity.
     */
    LigneDTO save(LigneDTO ligneDTO);

    /**
     * Updates a ligne.
     *
     * @param ligneDTO the entity to update.
     * @return the persisted entity.
     */
    LigneDTO update(LigneDTO ligneDTO);

    /**
     * Partially updates a ligne.
     *
     * @param ligneDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LigneDTO> partialUpdate(LigneDTO ligneDTO);

    /**
     * Get the "id" ligne.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LigneDTO> findOne(Long id);

    /**
     * Delete the "id" ligne.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
