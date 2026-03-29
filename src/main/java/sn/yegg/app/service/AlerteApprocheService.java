package sn.yegg.app.service;

import java.util.Optional;
import sn.yegg.app.service.dto.*;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.AlerteApproche}.
 */
public interface AlerteApprocheService {
    /**
     * Save a alerteApproche.
     *
     * @param alerteApprocheDTO the entity to save.
     * @return the persisted entity.
     */
    AlerteApprocheDTO save(AlerteApprocheDTO alerteApprocheDTO);

    /**
     * Updates a alerteApproche.
     *
     * @param alerteApprocheDTO the entity to update.
     * @return the persisted entity.
     */
    AlerteApprocheDTO update(AlerteApprocheDTO alerteApprocheDTO);

    /**
     * Partially updates a alerteApproche.
     *
     * @param alerteApprocheDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlerteApprocheDTO> partialUpdate(AlerteApprocheDTO alerteApprocheDTO);

    /**
     * Get the "id" alerteApproche.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlerteApprocheDTO> findOne(Long id);

    AlerteResponseDTO storeCompleteAlert(AlerteRequestDTO request);

    /**
     * Delete the "id" alerteApproche.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    AlertCheckResponse checkAlerts(AlertCheckRequest request);

    AlerteResponseDTO toggleCompleteAlert(ToggleAlerteLigneArretDTO toggleAlerteLigneArretDTO);

    void deleteCompleteAlert(Long alerteLigneArretId);
}
