package sn.yegg.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.LigneArret;
import sn.yegg.app.repository.LigneArretRepository;
import sn.yegg.app.service.LigneArretService;
import sn.yegg.app.service.dto.LigneArretDTO;
import sn.yegg.app.service.mapper.LigneArretMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.LigneArret}.
 */
@Service
@Transactional
public class LigneArretServiceImpl implements LigneArretService {

    private static final Logger LOG = LoggerFactory.getLogger(LigneArretServiceImpl.class);

    private final LigneArretRepository ligneArretRepository;

    private final LigneArretMapper ligneArretMapper;

    public LigneArretServiceImpl(LigneArretRepository ligneArretRepository, LigneArretMapper ligneArretMapper) {
        this.ligneArretRepository = ligneArretRepository;
        this.ligneArretMapper = ligneArretMapper;
    }

    @Override
    public LigneArretDTO save(LigneArretDTO ligneArretDTO) {
        LOG.debug("Request to save LigneArret : {}", ligneArretDTO);
        LigneArret ligneArret = ligneArretMapper.toEntity(ligneArretDTO);
        ligneArret = ligneArretRepository.save(ligneArret);
        return ligneArretMapper.toDto(ligneArret);
    }

    @Override
    public LigneArretDTO update(LigneArretDTO ligneArretDTO) {
        LOG.debug("Request to update LigneArret : {}", ligneArretDTO);
        LigneArret ligneArret = ligneArretMapper.toEntity(ligneArretDTO);
        ligneArret = ligneArretRepository.save(ligneArret);
        return ligneArretMapper.toDto(ligneArret);
    }

    @Override
    public Optional<LigneArretDTO> partialUpdate(LigneArretDTO ligneArretDTO) {
        LOG.debug("Request to partially update LigneArret : {}", ligneArretDTO);

        return ligneArretRepository
            .findById(ligneArretDTO.getId())
            .map(existingLigneArret -> {
                ligneArretMapper.partialUpdate(existingLigneArret, ligneArretDTO);

                return existingLigneArret;
            })
            .map(ligneArretRepository::save)
            .map(ligneArretMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LigneArretDTO> findOne(Long id) {
        LOG.debug("Request to get LigneArret : {}", id);
        return ligneArretRepository.findById(id).map(ligneArretMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete LigneArret : {}", id);
        ligneArretRepository.deleteById(id);
    }
}
