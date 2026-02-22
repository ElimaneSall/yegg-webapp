package sn.yegg.app.service;

import java.util.Optional;
import sn.yegg.app.service.dto.FavoriDTO;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.Favori}.
 */
public interface FavoriService {
    /**
     * Save a favori.
     *
     * @param favoriDTO the entity to save.
     * @return the persisted entity.
     */
    FavoriDTO save(FavoriDTO favoriDTO);

    /**
     * Updates a favori.
     *
     * @param favoriDTO the entity to update.
     * @return the persisted entity.
     */
    FavoriDTO update(FavoriDTO favoriDTO);

    /**
     * Partially updates a favori.
     *
     * @param favoriDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FavoriDTO> partialUpdate(FavoriDTO favoriDTO);

    /**
     * Get the "id" favori.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FavoriDTO> findOne(Long id);

    /**
     * Delete the "id" favori.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
