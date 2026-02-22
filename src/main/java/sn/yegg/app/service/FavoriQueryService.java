package sn.yegg.app.service;

import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.*; // for static metamodels
import sn.yegg.app.domain.Favori;
import sn.yegg.app.repository.FavoriRepository;
import sn.yegg.app.service.criteria.FavoriCriteria;
import sn.yegg.app.service.dto.FavoriDTO;
import sn.yegg.app.service.mapper.FavoriMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Favori} entities in the database.
 * The main input is a {@link FavoriCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FavoriDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FavoriQueryService extends QueryService<Favori> {

    private static final Logger LOG = LoggerFactory.getLogger(FavoriQueryService.class);

    private final FavoriRepository favoriRepository;

    private final FavoriMapper favoriMapper;

    public FavoriQueryService(FavoriRepository favoriRepository, FavoriMapper favoriMapper) {
        this.favoriRepository = favoriRepository;
        this.favoriMapper = favoriMapper;
    }

    /**
     * Return a {@link Page} of {@link FavoriDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FavoriDTO> findByCriteria(FavoriCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Favori> specification = createSpecification(criteria);
        return favoriRepository.findAll(specification, page).map(favoriMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FavoriCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Favori> specification = createSpecification(criteria);
        return favoriRepository.count(specification);
    }

    /**
     * Function to convert {@link FavoriCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Favori> createSpecification(FavoriCriteria criteria) {
        Specification<Favori> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Favori_.id),
                buildStringSpecification(criteria.getType(), Favori_.type),
                buildRangeSpecification(criteria.getCibleId(), Favori_.cibleId),
                buildStringSpecification(criteria.getNomPersonnalise(), Favori_.nomPersonnalise),
                buildRangeSpecification(criteria.getOrdre(), Favori_.ordre),
                buildSpecification(criteria.getAlerteActive(), Favori_.alerteActive),
                buildRangeSpecification(criteria.getAlerteSeuil(), Favori_.alerteSeuil),
                buildSpecification(criteria.getUtilisateurId(), root -> root.join(Favori_.utilisateur, JoinType.LEFT).get(Utilisateur_.id))
            );
        }
        return specification;
    }
}
