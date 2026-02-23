package sn.yegg.app.service;

import java.util.Optional;
import sn.yegg.app.service.dto.HistoriqueAlerteDTO;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.HistoriqueAlerte}.
 */
public interface HistoriqueAlerteService {
    /**
     * Save a historiqueAlerte.
     *
     * @param historiqueAlerteDTO the entity to save.
     * @return the persisted entity.
     */
    HistoriqueAlerteDTO save(HistoriqueAlerteDTO historiqueAlerteDTO);

    /**
     * Updates a historiqueAlerte.
     *
     * @param historiqueAlerteDTO the entity to update.
     * @return the persisted entity.
     */
    HistoriqueAlerteDTO update(HistoriqueAlerteDTO historiqueAlerteDTO);

    /**
     * Partially updates a historiqueAlerte.
     *
     * @param historiqueAlerteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistoriqueAlerteDTO> partialUpdate(HistoriqueAlerteDTO historiqueAlerteDTO);

    /**
     * Get the "id" historiqueAlerte.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoriqueAlerteDTO> findOne(Long id);

    /**
     * Delete the "id" historiqueAlerte.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
