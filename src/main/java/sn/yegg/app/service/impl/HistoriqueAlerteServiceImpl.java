package sn.yegg.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.HistoriqueAlerte;
import sn.yegg.app.repository.HistoriqueAlerteRepository;
import sn.yegg.app.service.HistoriqueAlerteService;
import sn.yegg.app.service.dto.HistoriqueAlerteDTO;
import sn.yegg.app.service.mapper.HistoriqueAlerteMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.HistoriqueAlerte}.
 */
@Service
@Transactional
public class HistoriqueAlerteServiceImpl implements HistoriqueAlerteService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueAlerteServiceImpl.class);

    private final HistoriqueAlerteRepository historiqueAlerteRepository;

    private final HistoriqueAlerteMapper historiqueAlerteMapper;

    public HistoriqueAlerteServiceImpl(
        HistoriqueAlerteRepository historiqueAlerteRepository,
        HistoriqueAlerteMapper historiqueAlerteMapper
    ) {
        this.historiqueAlerteRepository = historiqueAlerteRepository;
        this.historiqueAlerteMapper = historiqueAlerteMapper;
    }

    @Override
    public HistoriqueAlerteDTO save(HistoriqueAlerteDTO historiqueAlerteDTO) {
        LOG.debug("Request to save HistoriqueAlerte : {}", historiqueAlerteDTO);
        HistoriqueAlerte historiqueAlerte = historiqueAlerteMapper.toEntity(historiqueAlerteDTO);
        historiqueAlerte = historiqueAlerteRepository.save(historiqueAlerte);
        return historiqueAlerteMapper.toDto(historiqueAlerte);
    }

    @Override
    public HistoriqueAlerteDTO update(HistoriqueAlerteDTO historiqueAlerteDTO) {
        LOG.debug("Request to update HistoriqueAlerte : {}", historiqueAlerteDTO);
        HistoriqueAlerte historiqueAlerte = historiqueAlerteMapper.toEntity(historiqueAlerteDTO);
        historiqueAlerte = historiqueAlerteRepository.save(historiqueAlerte);
        return historiqueAlerteMapper.toDto(historiqueAlerte);
    }

    @Override
    public Optional<HistoriqueAlerteDTO> partialUpdate(HistoriqueAlerteDTO historiqueAlerteDTO) {
        LOG.debug("Request to partially update HistoriqueAlerte : {}", historiqueAlerteDTO);

        return historiqueAlerteRepository
            .findById(historiqueAlerteDTO.getId())
            .map(existingHistoriqueAlerte -> {
                historiqueAlerteMapper.partialUpdate(existingHistoriqueAlerte, historiqueAlerteDTO);

                return existingHistoriqueAlerte;
            })
            .map(historiqueAlerteRepository::save)
            .map(historiqueAlerteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HistoriqueAlerteDTO> findOne(Long id) {
        LOG.debug("Request to get HistoriqueAlerte : {}", id);
        return historiqueAlerteRepository.findById(id).map(historiqueAlerteMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete HistoriqueAlerte : {}", id);
        historiqueAlerteRepository.deleteById(id);
    }
}
