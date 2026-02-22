package sn.yegg.app.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.Favori;
import sn.yegg.app.repository.FavoriRepository;
import sn.yegg.app.service.FavoriService;
import sn.yegg.app.service.dto.FavoriDTO;
import sn.yegg.app.service.mapper.FavoriMapper;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.Favori}.
 */
@Service
@Transactional
public class FavoriServiceImpl implements FavoriService {

    private static final Logger LOG = LoggerFactory.getLogger(FavoriServiceImpl.class);

    private final FavoriRepository favoriRepository;

    private final FavoriMapper favoriMapper;

    public FavoriServiceImpl(FavoriRepository favoriRepository, FavoriMapper favoriMapper) {
        this.favoriRepository = favoriRepository;
        this.favoriMapper = favoriMapper;
    }

    @Override
    public FavoriDTO save(FavoriDTO favoriDTO) {
        LOG.debug("Request to save Favori : {}", favoriDTO);
        Favori favori = favoriMapper.toEntity(favoriDTO);
        favori = favoriRepository.save(favori);
        return favoriMapper.toDto(favori);
    }

    @Override
    public FavoriDTO update(FavoriDTO favoriDTO) {
        LOG.debug("Request to update Favori : {}", favoriDTO);
        Favori favori = favoriMapper.toEntity(favoriDTO);
        favori = favoriRepository.save(favori);
        return favoriMapper.toDto(favori);
    }

    @Override
    public Optional<FavoriDTO> partialUpdate(FavoriDTO favoriDTO) {
        LOG.debug("Request to partially update Favori : {}", favoriDTO);

        return favoriRepository
            .findById(favoriDTO.getId())
            .map(existingFavori -> {
                favoriMapper.partialUpdate(existingFavori, favoriDTO);

                return existingFavori;
            })
            .map(favoriRepository::save)
            .map(favoriMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FavoriDTO> findOne(Long id) {
        LOG.debug("Request to get Favori : {}", id);
        return favoriRepository.findById(id).map(favoriMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Favori : {}", id);
        favoriRepository.deleteById(id);
    }
}
