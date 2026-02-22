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
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.repository.UtilisateurRepository;
import sn.yegg.app.service.criteria.UtilisateurCriteria;
import sn.yegg.app.service.dto.UtilisateurDTO;
import sn.yegg.app.service.mapper.UtilisateurMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Utilisateur} entities in the database.
 * The main input is a {@link UtilisateurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UtilisateurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UtilisateurQueryService extends QueryService<Utilisateur> {

    private static final Logger LOG = LoggerFactory.getLogger(UtilisateurQueryService.class);

    private final UtilisateurRepository utilisateurRepository;

    private final UtilisateurMapper utilisateurMapper;

    public UtilisateurQueryService(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }

    /**
     * Return a {@link Page} of {@link UtilisateurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UtilisateurDTO> findByCriteria(UtilisateurCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Utilisateur> specification = createSpecification(criteria);
        return utilisateurRepository.findAll(specification, page).map(utilisateurMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UtilisateurCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Utilisateur> specification = createSpecification(criteria);
        return utilisateurRepository.count(specification);
    }

    /**
     * Function to convert {@link UtilisateurCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Utilisateur> createSpecification(UtilisateurCriteria criteria) {
        Specification<Utilisateur> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Utilisateur_.id),
                buildStringSpecification(criteria.getMatricule(), Utilisateur_.matricule),
                buildStringSpecification(criteria.getTelephone(), Utilisateur_.telephone),
                buildStringSpecification(criteria.getFcmToken(), Utilisateur_.fcmToken),
                buildSpecification(criteria.getNotificationsPush(), Utilisateur_.notificationsPush),
                buildStringSpecification(criteria.getLangue(), Utilisateur_.langue),
                buildRangeSpecification(criteria.getDateEmbauche(), Utilisateur_.dateEmbauche),
                buildStringSpecification(criteria.getNumeroPermis(), Utilisateur_.numeroPermis),
                buildSpecification(criteria.getBusId(), root -> root.join(Utilisateur_.bus, JoinType.LEFT).get(Bus_.id))
            );
        }
        return specification;
    }
}
