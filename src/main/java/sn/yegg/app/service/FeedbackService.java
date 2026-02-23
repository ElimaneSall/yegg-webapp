package sn.yegg.app.service;

import java.util.Optional;
import sn.yegg.app.service.dto.FeedbackDTO;

/**
 * Service Interface for managing {@link sn.yegg.app.domain.Feedback}.
 */
public interface FeedbackService {
    /**
     * Save a feedback.
     *
     * @param feedbackDTO the entity to save.
     * @return the persisted entity.
     */
    FeedbackDTO save(FeedbackDTO feedbackDTO);

    /**
     * Updates a feedback.
     *
     * @param feedbackDTO the entity to update.
     * @return the persisted entity.
     */
    FeedbackDTO update(FeedbackDTO feedbackDTO);

    /**
     * Partially updates a feedback.
     *
     * @param feedbackDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FeedbackDTO> partialUpdate(FeedbackDTO feedbackDTO);

    /**
     * Get the "id" feedback.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FeedbackDTO> findOne(Long id);

    /**
     * Delete the "id" feedback.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
