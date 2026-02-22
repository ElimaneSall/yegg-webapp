package sn.yegg.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.Alerte;
import sn.yegg.app.repository.AlerteRepository;
import sn.yegg.app.service.AlerteService;
import sn.yegg.app.service.dto.AlerteDTO;
import sn.yegg.app.service.mapper.AlerteMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.Alerte}.
 */
@Service
@Transactional
public class AlerteServiceImpl implements AlerteService {

    private static final Logger LOG = LoggerFactory.getLogger(AlerteServiceImpl.class);

    private final AlerteRepository alerteRepository;

    private final AlerteMapper alerteMapper;

    public AlerteServiceImpl(AlerteRepository alerteRepository, AlerteMapper alerteMapper) {
        this.alerteRepository = alerteRepository;
        this.alerteMapper = alerteMapper;
    }

    @Override
    public AlerteDTO save(AlerteDTO alerteDTO) {
        LOG.debug("Request to save Alerte : {}", alerteDTO);
        Alerte alerte = alerteMapper.toEntity(alerteDTO);
        alerte = alerteRepository.save(alerte);
        return alerteMapper.toDto(alerte);
    }

    @Override
    public AlerteDTO update(AlerteDTO alerteDTO) {
        LOG.debug("Request to update Alerte : {}", alerteDTO);
        Alerte alerte = alerteMapper.toEntity(alerteDTO);
        alerte = alerteRepository.save(alerte);
        return alerteMapper.toDto(alerte);
    }

    @Override
    public Optional<AlerteDTO> partialUpdate(AlerteDTO alerteDTO) {
        LOG.debug("Request to partially update Alerte : {}", alerteDTO);

        return alerteRepository
            .findById(alerteDTO.getId())
            .map(existingAlerte -> {
                alerteMapper.partialUpdate(existingAlerte, alerteDTO);

                return existingAlerte;
            })
            .map(alerteRepository::save)
            .map(alerteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlerteDTO> findOne(Long id) {
        LOG.debug("Request to get Alerte : {}", id);
        return alerteRepository.findById(id).map(alerteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Alerte : {}", id);
        alerteRepository.deleteById(id);
    }
}
