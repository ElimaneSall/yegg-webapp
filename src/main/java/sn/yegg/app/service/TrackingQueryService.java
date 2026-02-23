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
import sn.yegg.app.domain.Tracking;
import sn.yegg.app.repository.TrackingRepository;
import sn.yegg.app.service.criteria.TrackingCriteria;
import sn.yegg.app.service.dto.TrackingDTO;
import sn.yegg.app.service.mapper.TrackingMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Tracking} entities in the database.
 * The main input is a {@link TrackingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TrackingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrackingQueryService extends QueryService<Tracking> {

    private static final Logger LOG = LoggerFactory.getLogger(TrackingQueryService.class);

    private final TrackingRepository trackingRepository;

    private final TrackingMapper trackingMapper;

    public TrackingQueryService(TrackingRepository trackingRepository, TrackingMapper trackingMapper) {
        this.trackingRepository = trackingRepository;
        this.trackingMapper = trackingMapper;
    }

    /**
     * Return a {@link Page} of {@link TrackingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrackingDTO> findByCriteria(TrackingCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Tracking> specification = createSpecification(criteria);
        return trackingRepository.findAll(specification, page).map(trackingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrackingCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Tracking> specification = createSpecification(criteria);
        return trackingRepository.count(specification);
    }

    /**
     * Function to convert {@link TrackingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Tracking> createSpecification(TrackingCriteria criteria) {
        Specification<Tracking> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Tracking_.id),
                buildRangeSpecification(criteria.getLatitude(), Tracking_.latitude),
                buildRangeSpecification(criteria.getLongitude(), Tracking_.longitude),
                buildRangeSpecification(criteria.getVitesse(), Tracking_.vitesse),
                buildRangeSpecification(criteria.getCap(), Tracking_.cap),
                buildRangeSpecification(criteria.getPrecision(), Tracking_.precision),
                buildRangeSpecification(criteria.getTimestamp(), Tracking_.timestamp),
                buildSpecification(criteria.getSource(), Tracking_.source),
                buildStringSpecification(criteria.getEvenement(), Tracking_.evenement),
                buildSpecification(criteria.getBusId(), root -> root.join(Tracking_.bus, JoinType.LEFT).get(Bus_.id))
            );
        }
        return specification;
    }
}
