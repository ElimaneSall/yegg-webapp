package sn.yegg.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.AlerteLigneArret;
import sn.yegg.app.repository.AlerteLigneArretRepository;
import sn.yegg.app.service.AlerteLigneArretService;
import sn.yegg.app.service.dto.AlerteLigneArretDTO;
import sn.yegg.app.service.mapper.AlerteLigneArretMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.AlerteLigneArret}.
 */
@Service
@Transactional
public class AlerteLigneArretServiceImpl implements AlerteLigneArretService {

    private static final Logger LOG = LoggerFactory.getLogger(AlerteLigneArretServiceImpl.class);

    private final AlerteLigneArretRepository alerteLigneArretRepository;

    private final AlerteLigneArretMapper alerteLigneArretMapper;

    public AlerteLigneArretServiceImpl(
        AlerteLigneArretRepository alerteLigneArretRepository,
        AlerteLigneArretMapper alerteLigneArretMapper
    ) {
        this.alerteLigneArretRepository = alerteLigneArretRepository;
        this.alerteLigneArretMapper = alerteLigneArretMapper;
    }

    @Override
    public AlerteLigneArretDTO save(AlerteLigneArretDTO alerteLigneArretDTO) {
        LOG.debug("Request to save AlerteLigneArret : {}", alerteLigneArretDTO);
        AlerteLigneArret alerteLigneArret = alerteLigneArretMapper.toEntity(alerteLigneArretDTO);
        alerteLigneArret = alerteLigneArretRepository.save(alerteLigneArret);
        return alerteLigneArretMapper.toDto(alerteLigneArret);
    }

    @Override
    public AlerteLigneArretDTO update(AlerteLigneArretDTO alerteLigneArretDTO) {
        LOG.debug("Request to update AlerteLigneArret : {}", alerteLigneArretDTO);
        AlerteLigneArret alerteLigneArret = alerteLigneArretMapper.toEntity(alerteLigneArretDTO);
        alerteLigneArret = alerteLigneArretRepository.save(alerteLigneArret);
        return alerteLigneArretMapper.toDto(alerteLigneArret);
    }

    @Override
    public Optional<AlerteLigneArretDTO> partialUpdate(AlerteLigneArretDTO alerteLigneArretDTO) {
        LOG.debug("Request to partially update AlerteLigneArret : {}", alerteLigneArretDTO);

        return alerteLigneArretRepository
            .findById(alerteLigneArretDTO.getId())
            .map(existingAlerteLigneArret -> {
                alerteLigneArretMapper.partialUpdate(existingAlerteLigneArret, alerteLigneArretDTO);

                return existingAlerteLigneArret;
            })
            .map(alerteLigneArretRepository::save)
            .map(alerteLigneArretMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlerteLigneArretDTO> findOne(Long id) {
        LOG.debug("Request to get AlerteLigneArret : {}", id);
        return alerteLigneArretRepository.findById(id).map(alerteLigneArretMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AlerteLigneArret : {}", id);
        alerteLigneArretRepository.deleteById(id);
    }
}
