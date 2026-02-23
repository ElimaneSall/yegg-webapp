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
import sn.yegg.app.domain.Arret;
import sn.yegg.app.repository.ArretRepository;
import sn.yegg.app.service.criteria.ArretCriteria;
import sn.yegg.app.service.dto.ArretDTO;
import sn.yegg.app.service.mapper.ArretMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Arret} entities in the database.
 * The main input is a {@link ArretCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ArretDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArretQueryService extends QueryService<Arret> {

    private static final Logger LOG = LoggerFactory.getLogger(ArretQueryService.class);

    private final ArretRepository arretRepository;

    private final ArretMapper arretMapper;

    public ArretQueryService(ArretRepository arretRepository, ArretMapper arretMapper) {
        this.arretRepository = arretRepository;
        this.arretMapper = arretMapper;
    }

    /**
     * Return a {@link Page} of {@link ArretDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArretDTO> findByCriteria(ArretCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Arret> specification = createSpecification(criteria);
        return arretRepository.findAll(specification, page).map(arretMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArretCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Arret> specification = createSpecification(criteria);
        return arretRepository.count(specification);
    }

    /**
     * Function to convert {@link ArretCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Arret> createSpecification(ArretCriteria criteria) {
        Specification<Arret> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Arret_.id),
                buildStringSpecification(criteria.getNom(), Arret_.nom),
                buildStringSpecification(criteria.getCode(), Arret_.code),
                buildRangeSpecification(criteria.getLatitude(), Arret_.latitude),
                buildRangeSpecification(criteria.getLongitude(), Arret_.longitude),
                buildRangeSpecification(criteria.getAltitude(), Arret_.altitude),
                buildStringSpecification(criteria.getAdresse(), Arret_.adresse),
                buildStringSpecification(criteria.getVille(), Arret_.ville),
                buildStringSpecification(criteria.getCodePostal(), Arret_.codePostal),
                buildStringSpecification(criteria.getZoneTarifaire(), Arret_.zoneTarifaire),
                buildSpecification(criteria.getAccessiblePMR(), Arret_.accessiblePMR),
                buildSpecification(criteria.getActif(), Arret_.actif),
                buildSpecification(criteria.getLigneArretsId(), root -> root.join(Arret_.ligneArrets, JoinType.LEFT).get(LigneArret_.id))
            );
        }
        return specification;
    }
}
