package sn.yegg.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.Rapport;
import sn.yegg.app.repository.RapportRepository;
import sn.yegg.app.service.RapportService;
import sn.yegg.app.service.dto.RapportDTO;
import sn.yegg.app.service.mapper.RapportMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.Rapport}.
 */
@Service
@Transactional
public class RapportServiceImpl implements RapportService {

    private static final Logger LOG = LoggerFactory.getLogger(RapportServiceImpl.class);

    private final RapportRepository rapportRepository;

    private final RapportMapper rapportMapper;

    public RapportServiceImpl(RapportRepository rapportRepository, RapportMapper rapportMapper) {
        this.rapportRepository = rapportRepository;
        this.rapportMapper = rapportMapper;
    }

    @Override
    public RapportDTO save(RapportDTO rapportDTO) {
        LOG.debug("Request to save Rapport : {}", rapportDTO);
        Rapport rapport = rapportMapper.toEntity(rapportDTO);
        rapport = rapportRepository.save(rapport);
        return rapportMapper.toDto(rapport);
    }

    @Override
    public RapportDTO update(RapportDTO rapportDTO) {
        LOG.debug("Request to update Rapport : {}", rapportDTO);
        Rapport rapport = rapportMapper.toEntity(rapportDTO);
        rapport = rapportRepository.save(rapport);
        return rapportMapper.toDto(rapport);
    }

    @Override
    public Optional<RapportDTO> partialUpdate(RapportDTO rapportDTO) {
        LOG.debug("Request to partially update Rapport : {}", rapportDTO);

        return rapportRepository
            .findById(rapportDTO.getId())
            .map(existingRapport -> {
                rapportMapper.partialUpdate(existingRapport, rapportDTO);

                return existingRapport;
            })
            .map(rapportRepository::save)
            .map(rapportMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RapportDTO> findOne(Long id) {
        LOG.debug("Request to get Rapport : {}", id);
        return rapportRepository.findById(id).map(rapportMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Rapport : {}", id);
        rapportRepository.deleteById(id);
    }
}
