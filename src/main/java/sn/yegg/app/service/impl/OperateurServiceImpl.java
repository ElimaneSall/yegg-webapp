package sn.yegg.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.Operateur;
import sn.yegg.app.repository.OperateurRepository;
import sn.yegg.app.service.OperateurService;
import sn.yegg.app.service.dto.OperateurDTO;
import sn.yegg.app.service.mapper.OperateurMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.Operateur}.
 */
@Service
@Transactional
public class OperateurServiceImpl implements OperateurService {

    private static final Logger LOG = LoggerFactory.getLogger(OperateurServiceImpl.class);

    private final OperateurRepository operateurRepository;

    private final OperateurMapper operateurMapper;

    public OperateurServiceImpl(OperateurRepository operateurRepository, OperateurMapper operateurMapper) {
        this.operateurRepository = operateurRepository;
        this.operateurMapper = operateurMapper;
    }

    @Override
    public OperateurDTO save(OperateurDTO operateurDTO) {
        LOG.debug("Request to save Operateur : {}", operateurDTO);
        Operateur operateur = operateurMapper.toEntity(operateurDTO);
        operateur = operateurRepository.save(operateur);
        return operateurMapper.toDto(operateur);
    }

    @Override
    public OperateurDTO update(OperateurDTO operateurDTO) {
        LOG.debug("Request to update Operateur : {}", operateurDTO);
        Operateur operateur = operateurMapper.toEntity(operateurDTO);
        operateur = operateurRepository.save(operateur);
        return operateurMapper.toDto(operateur);
    }

    @Override
    public Optional<OperateurDTO> partialUpdate(OperateurDTO operateurDTO) {
        LOG.debug("Request to partially update Operateur : {}", operateurDTO);

        return operateurRepository
            .findById(operateurDTO.getId())
            .map(existingOperateur -> {
                operateurMapper.partialUpdate(existingOperateur, operateurDTO);

                return existingOperateur;
            })
            .map(operateurRepository::save)
            .map(operateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OperateurDTO> findOne(Long id) {
        LOG.debug("Request to get Operateur : {}", id);
        return operateurRepository.findById(id).map(operateurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Operateur : {}", id);
        operateurRepository.deleteById(id);
    }
}
