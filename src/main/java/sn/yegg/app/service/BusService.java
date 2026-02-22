package sn.yegg.app.service;

import java.util.Optional;
import sn.yegg.app.service.dto.BusDTO;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.Bus}.
 */
public interface BusService {
    /**
     * Save a bus.
     *
     * @param busDTO the entity to save.
     * @return the persisted entity.
     */
    BusDTO save(BusDTO busDTO);

    /**
     * Updates a bus.
     *
     * @param busDTO the entity to update.
     * @return the persisted entity.
     */
    BusDTO update(BusDTO busDTO);

    /**
     * Partially updates a bus.
     *
     * @param busDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BusDTO> partialUpdate(BusDTO busDTO);

    /**
     * Get the "id" bus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BusDTO> findOne(Long id);

    /**
     * Delete the "id" bus.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
