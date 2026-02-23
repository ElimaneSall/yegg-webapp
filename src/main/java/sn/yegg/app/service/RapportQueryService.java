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
import sn.yegg.app.domain.Rapport;
import sn.yegg.app.repository.RapportRepository;
import sn.yegg.app.service.criteria.RapportCriteria;
import sn.yegg.app.service.dto.RapportDTO;
import sn.yegg.app.service.mapper.RapportMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Rapport} entities in the database.
 * The main input is a {@link RapportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RapportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RapportQueryService extends QueryService<Rapport> {

    private static final Logger LOG = LoggerFactory.getLogger(RapportQueryService.class);

    private final RapportRepository rapportRepository;

    private final RapportMapper rapportMapper;

    public RapportQueryService(RapportRepository rapportRepository, RapportMapper rapportMapper) {
        this.rapportRepository = rapportRepository;
        this.rapportMapper = rapportMapper;
    }

    /**
     * Return a {@link Page} of {@link RapportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RapportDTO> findByCriteria(RapportCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Rapport> specification = createSpecification(criteria);
        return rapportRepository.findAll(specification, page).map(rapportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RapportCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Rapport> specification = createSpecification(criteria);
        return rapportRepository.count(specification);
    }

    /**
     * Function to convert {@link RapportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Rapport> createSpecification(RapportCriteria criteria) {
        Specification<Rapport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Rapport_.id),
                buildStringSpecification(criteria.getNom(), Rapport_.nom),
                buildSpecification(criteria.getType(), Rapport_.type),
                buildRangeSpecification(criteria.getPeriodeDebut(), Rapport_.periodeDebut),
                buildRangeSpecification(criteria.getPeriodeFin(), Rapport_.periodeFin),
                buildSpecification(criteria.getFormat(), Rapport_.format),
                buildRangeSpecification(criteria.getDateGeneration(), Rapport_.dateGeneration),
                buildStringSpecification(criteria.getGenerePar(), Rapport_.generePar),
                buildSpecification(criteria.getOperateurId(), root -> root.join(Rapport_.operateur, JoinType.LEFT).get(Operateur_.id)),
                buildSpecification(criteria.getAdminId(), root -> root.join(Rapport_.admin, JoinType.LEFT).get(Utilisateur_.id))
            );
        }
        return specification;
    }
}
