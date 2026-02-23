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
import sn.yegg.app.domain.AlerteLigneArret;
import sn.yegg.app.repository.AlerteLigneArretRepository;
import sn.yegg.app.service.criteria.AlerteLigneArretCriteria;
import sn.yegg.app.service.dto.AlerteLigneArretDTO;
import sn.yegg.app.service.mapper.AlerteLigneArretMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AlerteLigneArret} entities in the database.
 * The main input is a {@link AlerteLigneArretCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlerteLigneArretDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlerteLigneArretQueryService extends QueryService<AlerteLigneArret> {

    private static final Logger LOG = LoggerFactory.getLogger(AlerteLigneArretQueryService.class);

    private final AlerteLigneArretRepository alerteLigneArretRepository;

    private final AlerteLigneArretMapper alerteLigneArretMapper;

    public AlerteLigneArretQueryService(
        AlerteLigneArretRepository alerteLigneArretRepository,
        AlerteLigneArretMapper alerteLigneArretMapper
    ) {
        this.alerteLigneArretRepository = alerteLigneArretRepository;
        this.alerteLigneArretMapper = alerteLigneArretMapper;
    }

    /**
     * Return a {@link Page} of {@link AlerteLigneArretDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlerteLigneArretDTO> findByCriteria(AlerteLigneArretCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlerteLigneArret> specification = createSpecification(criteria);
        return alerteLigneArretRepository.findAll(specification, page).map(alerteLigneArretMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlerteLigneArretCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlerteLigneArret> specification = createSpecification(criteria);
        return alerteLigneArretRepository.count(specification);
    }

    /**
     * Function to convert {@link AlerteLigneArretCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlerteLigneArret> createSpecification(AlerteLigneArretCriteria criteria) {
        Specification<AlerteLigneArret> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AlerteLigneArret_.id),
                buildStringSpecification(criteria.getSens(), AlerteLigneArret_.sens),
                buildSpecification(criteria.getActif(), AlerteLigneArret_.actif),
                buildSpecification(criteria.getLigneId(), root -> root.join(AlerteLigneArret_.ligne, JoinType.LEFT).get(Ligne_.id)),
                buildSpecification(criteria.getArretId(), root -> root.join(AlerteLigneArret_.arret, JoinType.LEFT).get(Arret_.id)),
                buildSpecification(criteria.getAlerteApprocheId(), root ->
                    root.join(AlerteLigneArret_.alerteApproche, JoinType.LEFT).get(AlerteApproche_.id)
                )
            );
        }
        return specification;
    }
}
