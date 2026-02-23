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
import sn.yegg.app.domain.Feedback;
import sn.yegg.app.repository.FeedbackRepository;
import sn.yegg.app.service.criteria.FeedbackCriteria;
import sn.yegg.app.service.dto.FeedbackDTO;
import sn.yegg.app.service.mapper.FeedbackMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Feedback} entities in the database.
 * The main input is a {@link FeedbackCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FeedbackDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FeedbackQueryService extends QueryService<Feedback> {

    private static final Logger LOG = LoggerFactory.getLogger(FeedbackQueryService.class);

    private final FeedbackRepository feedbackRepository;

    private final FeedbackMapper feedbackMapper;

    public FeedbackQueryService(FeedbackRepository feedbackRepository, FeedbackMapper feedbackMapper) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
    }

    /**
     * Return a {@link Page} of {@link FeedbackDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FeedbackDTO> findByCriteria(FeedbackCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Feedback> specification = createSpecification(criteria);
        return feedbackRepository.findAll(specification, page).map(feedbackMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FeedbackCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Feedback> specification = createSpecification(criteria);
        return feedbackRepository.count(specification);
    }

    /**
     * Function to convert {@link FeedbackCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Feedback> createSpecification(FeedbackCriteria criteria) {
        Specification<Feedback> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Feedback_.id),
                buildRangeSpecification(criteria.getNote(), Feedback_.note),
                buildStringSpecification(criteria.getTypeObjet(), Feedback_.typeObjet),
                buildRangeSpecification(criteria.getObjetId(), Feedback_.objetId),
                buildRangeSpecification(criteria.getDateCreation(), Feedback_.dateCreation),
                buildSpecification(criteria.getAnonyme(), Feedback_.anonyme),
                buildSpecification(criteria.getUtilisateurId(), root -> root.join(Feedback_.utilisateur, JoinType.LEFT).get(Utilisateur_.id)
                )
            );
        }
        return specification;
    }
}
