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
import sn.yegg.app.domain.AlerteApproche;
import sn.yegg.app.repository.AlerteApprocheRepository;
import sn.yegg.app.service.criteria.AlerteApprocheCriteria;
import sn.yegg.app.service.dto.AlerteApprocheDTO;
import sn.yegg.app.service.mapper.AlerteApprocheMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link AlerteApproche} entities in the database.
 * The main input is a {@link AlerteApprocheCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AlerteApprocheDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlerteApprocheQueryService extends QueryService<AlerteApproche> {

    private static final Logger LOG = LoggerFactory.getLogger(AlerteApprocheQueryService.class);

    private final AlerteApprocheRepository alerteApprocheRepository;

    private final AlerteApprocheMapper alerteApprocheMapper;

    public AlerteApprocheQueryService(AlerteApprocheRepository alerteApprocheRepository, AlerteApprocheMapper alerteApprocheMapper) {
        this.alerteApprocheRepository = alerteApprocheRepository;
        this.alerteApprocheMapper = alerteApprocheMapper;
    }

    /**
     * Return a {@link Page} of {@link AlerteApprocheDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlerteApprocheDTO> findByCriteria(AlerteApprocheCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AlerteApproche> specification = createSpecification(criteria);
        return alerteApprocheRepository.findAll(specification, page).map(alerteApprocheMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlerteApprocheCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AlerteApproche> specification = createSpecification(criteria);
        return alerteApprocheRepository.count(specification);
    }

    /**
     * Function to convert {@link AlerteApprocheCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AlerteApproche> createSpecification(AlerteApprocheCriteria criteria) {
        Specification<AlerteApproche> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), AlerteApproche_.id),
                buildStringSpecification(criteria.getNom(), AlerteApproche_.nom),
                buildRangeSpecification(criteria.getSeuilDistance(), AlerteApproche_.seuilDistance),
                buildRangeSpecification(criteria.getSeuilTemps(), AlerteApproche_.seuilTemps),
                buildSpecification(criteria.getTypeSeuil(), AlerteApproche_.typeSeuil),
                buildStringSpecification(criteria.getJoursActivation(), AlerteApproche_.joursActivation),
                buildStringSpecification(criteria.getHeureDebut(), AlerteApproche_.heureDebut),
                buildStringSpecification(criteria.getHeureFin(), AlerteApproche_.heureFin),
                buildSpecification(criteria.getStatut(), AlerteApproche_.statut),
                buildRangeSpecification(criteria.getDateCreation(), AlerteApproche_.dateCreation),
                buildRangeSpecification(criteria.getDateModification(), AlerteApproche_.dateModification),
                buildRangeSpecification(criteria.getDernierDeclenchement(), AlerteApproche_.dernierDeclenchement),
                buildRangeSpecification(criteria.getNombreDeclenchements(), AlerteApproche_.nombreDeclenchements),
                buildSpecification(criteria.getAlerteLigneArretId(), root ->
                    root.join(AlerteApproche_.alerteLigneArrets, JoinType.LEFT).get(AlerteLigneArret_.id)
                ),
                buildSpecification(criteria.getHistoriqueAlertesId(), root ->
                    root.join(AlerteApproche_.historiqueAlertes, JoinType.LEFT).get(HistoriqueAlerte_.id)
                ),
                buildSpecification(criteria.getUtilisateurId(), root ->
                    root.join(AlerteApproche_.utilisateur, JoinType.LEFT).get(Utilisateur_.id)
                )
            );
        }
        return specification;
    }
}
