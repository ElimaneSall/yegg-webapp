package sn.yegg.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.Ligne;
import sn.yegg.app.repository.LigneRepository;
import sn.yegg.app.service.LigneService;
import sn.yegg.app.service.dto.LigneDTO;
import sn.yegg.app.service.mapper.LigneMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.Ligne}.
 */
@Service
@Transactional
public class LigneServiceImpl implements LigneService {

    private static final Logger LOG = LoggerFactory.getLogger(LigneServiceImpl.class);

    private final LigneRepository ligneRepository;

    private final LigneMapper ligneMapper;

    public LigneServiceImpl(LigneRepository ligneRepository, LigneMapper ligneMapper) {
        this.ligneRepository = ligneRepository;
        this.ligneMapper = ligneMapper;
    }

    @Override
    public LigneDTO save(LigneDTO ligneDTO) {
        LOG.debug("Request to save Ligne : {}", ligneDTO);
        Ligne ligne = ligneMapper.toEntity(ligneDTO);
        ligne = ligneRepository.save(ligne);
        return ligneMapper.toDto(ligne);
    }

    @Override
    public LigneDTO update(LigneDTO ligneDTO) {
        LOG.debug("Request to update Ligne : {}", ligneDTO);
        Ligne ligne = ligneMapper.toEntity(ligneDTO);
        ligne = ligneRepository.save(ligne);
        return ligneMapper.toDto(ligne);
    }

    @Override
    public Optional<LigneDTO> partialUpdate(LigneDTO ligneDTO) {
        LOG.debug("Request to partially update Ligne : {}", ligneDTO);

        return ligneRepository
            .findById(ligneDTO.getId())
            .map(existingLigne -> {
                ligneMapper.partialUpdate(existingLigne, ligneDTO);

                return existingLigne;
            })
            .map(ligneRepository::save)
            .map(ligneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LigneDTO> findOne(Long id) {
        LOG.debug("Request to get Ligne : {}", id);
        return ligneRepository.findById(id).map(ligneMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Ligne : {}", id);
        ligneRepository.deleteById(id);
    }
}
