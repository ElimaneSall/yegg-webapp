package sn.yegg.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.Feedback;
import sn.yegg.app.repository.FeedbackRepository;
import sn.yegg.app.service.FeedbackService;
import sn.yegg.app.service.dto.FeedbackDTO;
import sn.yegg.app.service.mapper.FeedbackMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.Feedback}.
 */
@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private static final Logger LOG = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    private final FeedbackRepository feedbackRepository;

    private final FeedbackMapper feedbackMapper;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, FeedbackMapper feedbackMapper) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
    }

    @Override
    public FeedbackDTO save(FeedbackDTO feedbackDTO) {
        LOG.debug("Request to save Feedback : {}", feedbackDTO);
        Feedback feedback = feedbackMapper.toEntity(feedbackDTO);
        feedback = feedbackRepository.save(feedback);
        return feedbackMapper.toDto(feedback);
    }

    @Override
    public FeedbackDTO update(FeedbackDTO feedbackDTO) {
        LOG.debug("Request to update Feedback : {}", feedbackDTO);
        Feedback feedback = feedbackMapper.toEntity(feedbackDTO);
        feedback = feedbackRepository.save(feedback);
        return feedbackMapper.toDto(feedback);
    }

    @Override
    public Optional<FeedbackDTO> partialUpdate(FeedbackDTO feedbackDTO) {
        LOG.debug("Request to partially update Feedback : {}", feedbackDTO);

        return feedbackRepository
            .findById(feedbackDTO.getId())
            .map(existingFeedback -> {
                feedbackMapper.partialUpdate(existingFeedback, feedbackDTO);

                return existingFeedback;
            })
            .map(feedbackRepository::save)
            .map(feedbackMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FeedbackDTO> findOne(Long id) {
        LOG.debug("Request to get Feedback : {}", id);
        return feedbackRepository.findById(id).map(feedbackMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Feedback : {}", id);
        feedbackRepository.deleteById(id);
    }
}
