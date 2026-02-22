package sn.yegg.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.Arret;
import sn.yegg.app.repository.ArretRepository;
import sn.yegg.app.service.ArretService;
import sn.yegg.app.service.dto.ArretDTO;
import sn.yegg.app.service.mapper.ArretMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.Arret}.
 */
@Service
@Transactional
public class ArretServiceImpl implements ArretService {

    private static final Logger LOG = LoggerFactory.getLogger(ArretServiceImpl.class);

    private final ArretRepository arretRepository;

    private final ArretMapper arretMapper;

    public ArretServiceImpl(ArretRepository arretRepository, ArretMapper arretMapper) {
        this.arretRepository = arretRepository;
        this.arretMapper = arretMapper;
    }

    @Override
    public ArretDTO save(ArretDTO arretDTO) {
        LOG.debug("Request to save Arret : {}", arretDTO);
        Arret arret = arretMapper.toEntity(arretDTO);
        arret = arretRepository.save(arret);
        return arretMapper.toDto(arret);
    }

    @Override
    public ArretDTO update(ArretDTO arretDTO) {
        LOG.debug("Request to update Arret : {}", arretDTO);
        Arret arret = arretMapper.toEntity(arretDTO);
        arret = arretRepository.save(arret);
        return arretMapper.toDto(arret);
    }

    @Override
    public Optional<ArretDTO> partialUpdate(ArretDTO arretDTO) {
        LOG.debug("Request to partially update Arret : {}", arretDTO);

        return arretRepository
            .findById(arretDTO.getId())
            .map(existingArret -> {
                arretMapper.partialUpdate(existingArret, arretDTO);

                return existingArret;
            })
            .map(arretRepository::save)
            .map(arretMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArretDTO> findOne(Long id) {
        LOG.debug("Request to get Arret : {}", id);
        return arretRepository.findById(id).map(arretMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Arret : {}", id);
        arretRepository.deleteById(id);
    }
}
