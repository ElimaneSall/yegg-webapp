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
import sn.yegg.app.domain.HistoriqueAlerte;
import sn.yegg.app.repository.HistoriqueAlerteRepository;
import sn.yegg.app.service.criteria.HistoriqueAlerteCriteria;
import sn.yegg.app.service.dto.HistoriqueAlerteDTO;
import sn.yegg.app.service.mapper.HistoriqueAlerteMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HistoriqueAlerte} entities in the database.
 * The main input is a {@link HistoriqueAlerteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HistoriqueAlerteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoriqueAlerteQueryService extends QueryService<HistoriqueAlerte> {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueAlerteQueryService.class);

    private final HistoriqueAlerteRepository historiqueAlerteRepository;

    private final HistoriqueAlerteMapper historiqueAlerteMapper;

    public HistoriqueAlerteQueryService(
        HistoriqueAlerteRepository historiqueAlerteRepository,
        HistoriqueAlerteMapper historiqueAlerteMapper
    ) {
        this.historiqueAlerteRepository = historiqueAlerteRepository;
        this.historiqueAlerteMapper = historiqueAlerteMapper;
    }

    /**
     * Return a {@link Page} of {@link HistoriqueAlerteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoriqueAlerteDTO> findByCriteria(HistoriqueAlerteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistoriqueAlerte> specification = createSpecification(criteria);
        return historiqueAlerteRepository.findAll(specification, page).map(historiqueAlerteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoriqueAlerteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HistoriqueAlerte> specification = createSpecification(criteria);
        return historiqueAlerteRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoriqueAlerteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HistoriqueAlerte> createSpecification(HistoriqueAlerteCriteria criteria) {
        Specification<HistoriqueAlerte> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), HistoriqueAlerte_.id),
                buildRangeSpecification(criteria.getDateDeclenchement(), HistoriqueAlerte_.dateDeclenchement),
                buildStringSpecification(criteria.getBusNumero(), HistoriqueAlerte_.busNumero),
                buildRangeSpecification(criteria.getDistanceReelle(), HistoriqueAlerte_.distanceReelle),
                buildRangeSpecification(criteria.getTempsReel(), HistoriqueAlerte_.tempsReel),
                buildSpecification(criteria.getTypeDeclenchement(), HistoriqueAlerte_.typeDeclenchement),
                buildSpecification(criteria.getNotificationEnvoyee(), HistoriqueAlerte_.notificationEnvoyee),
                buildRangeSpecification(criteria.getDateLecture(), HistoriqueAlerte_.dateLecture),
                buildSpecification(criteria.getBusId(), root -> root.join(HistoriqueAlerte_.bus, JoinType.LEFT).get(Bus_.id)),
                buildSpecification(criteria.getAlerteApprocheId(), root ->
                    root.join(HistoriqueAlerte_.alerteApproche, JoinType.LEFT).get(AlerteApproche_.id)
                ),
                buildSpecification(criteria.getUtilisateurId(), root ->
                    root.join(HistoriqueAlerte_.utilisateur, JoinType.LEFT).get(Utilisateur_.id)
                )
            );
        }
        return specification;
    }
}
