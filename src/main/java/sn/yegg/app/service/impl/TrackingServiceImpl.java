package sn.yegg.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.Tracking;
import sn.yegg.app.repository.TrackingRepository;
import sn.yegg.app.service.TrackingService;
import sn.yegg.app.service.dto.TrackingDTO;
import sn.yegg.app.service.mapper.TrackingMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.Tracking}.
 */
@Service
@Transactional
public class TrackingServiceImpl implements TrackingService {

    private static final Logger LOG = LoggerFactory.getLogger(TrackingServiceImpl.class);

    private final TrackingRepository trackingRepository;

    private final TrackingMapper trackingMapper;

    public TrackingServiceImpl(TrackingRepository trackingRepository, TrackingMapper trackingMapper) {
        this.trackingRepository = trackingRepository;
        this.trackingMapper = trackingMapper;
    }

    @Override
    public TrackingDTO save(TrackingDTO trackingDTO) {
        LOG.debug("Request to save Tracking : {}", trackingDTO);
        Tracking tracking = trackingMapper.toEntity(trackingDTO);
        tracking = trackingRepository.save(tracking);
        return trackingMapper.toDto(tracking);
    }

    @Override
    public TrackingDTO update(TrackingDTO trackingDTO) {
        LOG.debug("Request to update Tracking : {}", trackingDTO);
        Tracking tracking = trackingMapper.toEntity(trackingDTO);
        tracking = trackingRepository.save(tracking);
        return trackingMapper.toDto(tracking);
    }

    @Override
    public Optional<TrackingDTO> partialUpdate(TrackingDTO trackingDTO) {
        LOG.debug("Request to partially update Tracking : {}", trackingDTO);

        return trackingRepository
            .findById(trackingDTO.getId())
            .map(existingTracking -> {
                trackingMapper.partialUpdate(existingTracking, trackingDTO);

                return existingTracking;
            })
            .map(trackingRepository::save)
            .map(trackingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TrackingDTO> findOne(Long id) {
        LOG.debug("Request to get Tracking : {}", id);
        return trackingRepository.findById(id).map(trackingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Tracking : {}", id);
        trackingRepository.deleteById(id);
    }
}
