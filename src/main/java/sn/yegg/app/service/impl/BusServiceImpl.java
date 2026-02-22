package sn.yegg.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.Bus;
import sn.yegg.app.repository.BusRepository;
import sn.yegg.app.service.BusService;
import sn.yegg.app.service.dto.BusDTO;
import sn.yegg.app.service.mapper.BusMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.Bus}.
 */
@Service
@Transactional
public class BusServiceImpl implements BusService {

    private static final Logger LOG = LoggerFactory.getLogger(BusServiceImpl.class);

    private final BusRepository busRepository;

    private final BusMapper busMapper;

    public BusServiceImpl(BusRepository busRepository, BusMapper busMapper) {
        this.busRepository = busRepository;
        this.busMapper = busMapper;
    }

    @Override
    public BusDTO save(BusDTO busDTO) {
        LOG.debug("Request to save Bus : {}", busDTO);
        Bus bus = busMapper.toEntity(busDTO);
        bus = busRepository.save(bus);
        return busMapper.toDto(bus);
    }

    @Override
    public BusDTO update(BusDTO busDTO) {
        LOG.debug("Request to update Bus : {}", busDTO);
        Bus bus = busMapper.toEntity(busDTO);
        bus = busRepository.save(bus);
        return busMapper.toDto(bus);
    }

    @Override
    public Optional<BusDTO> partialUpdate(BusDTO busDTO) {
        LOG.debug("Request to partially update Bus : {}", busDTO);

        return busRepository
            .findById(busDTO.getId())
            .map(existingBus -> {
                busMapper.partialUpdate(existingBus, busDTO);

                return existingBus;
            })
            .map(busRepository::save)
            .map(busMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BusDTO> findOne(Long id) {
        LOG.debug("Request to get Bus : {}", id);
        return busRepository.findById(id).map(busMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Bus : {}", id);
        busRepository.deleteById(id);
    }
}
