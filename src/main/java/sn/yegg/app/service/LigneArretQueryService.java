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
import sn.yegg.app.domain.LigneArret;
import sn.yegg.app.repository.LigneArretRepository;
import sn.yegg.app.service.criteria.LigneArretCriteria;
import sn.yegg.app.service.dto.LigneArretDTO;
import sn.yegg.app.service.mapper.LigneArretMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link LigneArret} entities in the database.
 * The main input is a {@link LigneArretCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LigneArretDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LigneArretQueryService extends QueryService<LigneArret> {

    private static final Logger LOG = LoggerFactory.getLogger(LigneArretQueryService.class);

    private final LigneArretRepository ligneArretRepository;

    private final LigneArretMapper ligneArretMapper;

    public LigneArretQueryService(LigneArretRepository ligneArretRepository, LigneArretMapper ligneArretMapper) {
        this.ligneArretRepository = ligneArretRepository;
        this.ligneArretMapper = ligneArretMapper;
    }

    /**
     * Return a {@link Page} of {@link LigneArretDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LigneArretDTO> findByCriteria(LigneArretCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LigneArret> specification = createSpecification(criteria);
        return ligneArretRepository.findAll(specification, page).map(ligneArretMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LigneArretCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<LigneArret> specification = createSpecification(criteria);
        return ligneArretRepository.count(specification);
    }

    /**
     * Function to convert {@link LigneArretCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LigneArret> createSpecification(LigneArretCriteria criteria) {
        Specification<LigneArret> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), LigneArret_.id),
                buildRangeSpecification(criteria.getOrdre(), LigneArret_.ordre),
                buildRangeSpecification(criteria.getTempsTrajetDepart(), LigneArret_.tempsTrajetDepart),
                buildRangeSpecification(criteria.getDistanceDepart(), LigneArret_.distanceDepart),
                buildRangeSpecification(criteria.getTempsArretMoyen(), LigneArret_.tempsArretMoyen),
                buildSpecification(criteria.getArretPhysique(), LigneArret_.arretPhysique),
                buildSpecification(criteria.getLigneId(), root -> root.join(LigneArret_.ligne, JoinType.LEFT).get(Ligne_.id)),
                buildSpecification(criteria.getArretId(), root -> root.join(LigneArret_.arret, JoinType.LEFT).get(Arret_.id))
            );
        }
        return specification;
    }
}
