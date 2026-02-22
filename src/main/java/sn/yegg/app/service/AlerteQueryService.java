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
import sn.yegg.app.domain.Alerte;
import sn.yegg.app.repository.AlerteRepository;
import sn.yegg.app.service.criteria.AlerteCriteria;
import sn.yegg.app.service.dto.AlerteDTO;
import sn.yegg.app.service.mapper.AlerteMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Alerte} entities in the database.
 * The main input is a {@link AlerteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlerteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlerteQueryService extends QueryService<Alerte> {

    private static final Logger LOG = LoggerFactory.getLogger(AlerteQueryService.class);

    private final AlerteRepository alerteRepository;

    private final AlerteMapper alerteMapper;

    public AlerteQueryService(AlerteRepository alerteRepository, AlerteMapper alerteMapper) {
        this.alerteRepository = alerteRepository;
        this.alerteMapper = alerteMapper;
    }

    /**
     * Return a {@link Page} of {@link AlerteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlerteDTO> findByCriteria(AlerteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Alerte> specification = createSpecification(criteria);
        return alerteRepository.findAll(specification, page).map(alerteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlerteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Alerte> specification = createSpecification(criteria);
        return alerteRepository.count(specification);
    }

    /**
     * Function to convert {@link AlerteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Alerte> createSpecification(AlerteCriteria criteria) {
        Specification<Alerte> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Alerte_.id),
                buildStringSpecification(criteria.getTypeCible(), Alerte_.typeCible),
                buildRangeSpecification(criteria.getCibleId(), Alerte_.cibleId),
                buildRangeSpecification(criteria.getSeuilMinutes(), Alerte_.seuilMinutes),
                buildStringSpecification(criteria.getJoursActivation(), Alerte_.joursActivation),
                buildRangeSpecification(criteria.getHeureDebut(), Alerte_.heureDebut),
                buildRangeSpecification(criteria.getHeureFin(), Alerte_.heureFin),
                buildStringSpecification(criteria.getStatut(), Alerte_.statut),
                buildRangeSpecification(criteria.getDernierDeclenchement(), Alerte_.dernierDeclenchement),
                buildRangeSpecification(criteria.getNombreDeclenchements(), Alerte_.nombreDeclenchements),
                buildSpecification(criteria.getUtilisateurId(), root -> root.join(Alerte_.utilisateur, JoinType.LEFT).get(Utilisateur_.id))
            );
        }
        return specification;
    }
}
