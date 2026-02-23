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
import sn.yegg.app.domain.Operateur;
import sn.yegg.app.repository.OperateurRepository;
import sn.yegg.app.service.criteria.OperateurCriteria;
import sn.yegg.app.service.dto.OperateurDTO;
import sn.yegg.app.service.mapper.OperateurMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Operateur} entities in the database.
 * The main input is a {@link OperateurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link OperateurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OperateurQueryService extends QueryService<Operateur> {

    private static final Logger LOG = LoggerFactory.getLogger(OperateurQueryService.class);

    private final OperateurRepository operateurRepository;

    private final OperateurMapper operateurMapper;

    public OperateurQueryService(OperateurRepository operateurRepository, OperateurMapper operateurMapper) {
        this.operateurRepository = operateurRepository;
        this.operateurMapper = operateurMapper;
    }

    /**
     * Return a {@link Page} of {@link OperateurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OperateurDTO> findByCriteria(OperateurCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Operateur> specification = createSpecification(criteria);
        return operateurRepository.findAll(specification, page).map(operateurMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OperateurCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Operateur> specification = createSpecification(criteria);
        return operateurRepository.count(specification);
    }

    /**
     * Function to convert {@link OperateurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Operateur> createSpecification(OperateurCriteria criteria) {
        Specification<Operateur> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Operateur_.id),
                buildStringSpecification(criteria.getNom(), Operateur_.nom),
                buildStringSpecification(criteria.getEmail(), Operateur_.email),
                buildStringSpecification(criteria.getTelephone(), Operateur_.telephone),
                buildStringSpecification(criteria.getSiteWeb(), Operateur_.siteWeb),
                buildStringSpecification(criteria.getSiret(), Operateur_.siret),
                buildRangeSpecification(criteria.getDateCreation(), Operateur_.dateCreation),
                buildSpecification(criteria.getActif(), Operateur_.actif),
                buildSpecification(criteria.getLignesId(), root -> root.join(Operateur_.lignes, JoinType.LEFT).get(Ligne_.id))
            );
        }
        return specification;
    }
}
