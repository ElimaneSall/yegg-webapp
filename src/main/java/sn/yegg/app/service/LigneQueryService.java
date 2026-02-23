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
import sn.yegg.app.domain.Ligne;
import sn.yegg.app.repository.LigneRepository;
import sn.yegg.app.service.criteria.LigneCriteria;
import sn.yegg.app.service.dto.LigneDTO;
import sn.yegg.app.service.mapper.LigneMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Ligne} entities in the database.
 * The main input is a {@link LigneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LigneDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LigneQueryService extends QueryService<Ligne> {

    private static final Logger LOG = LoggerFactory.getLogger(LigneQueryService.class);

    private final LigneRepository ligneRepository;

    private final LigneMapper ligneMapper;

    public LigneQueryService(LigneRepository ligneRepository, LigneMapper ligneMapper) {
        this.ligneRepository = ligneRepository;
        this.ligneMapper = ligneMapper;
    }

    /**
     * Return a {@link Page} of {@link LigneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LigneDTO> findByCriteria(LigneCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ligne> specification = createSpecification(criteria);
        return ligneRepository.findAll(specification, page).map(ligneMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LigneCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Ligne> specification = createSpecification(criteria);
        return ligneRepository.count(specification);
    }

    /**
     * Function to convert {@link LigneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ligne> createSpecification(LigneCriteria criteria) {
        Specification<Ligne> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Ligne_.id),
                buildStringSpecification(criteria.getNumero(), Ligne_.numero),
                buildStringSpecification(criteria.getNom(), Ligne_.nom),
                buildStringSpecification(criteria.getDirection(), Ligne_.direction),
                buildStringSpecification(criteria.getCouleur(), Ligne_.couleur),
                buildRangeSpecification(criteria.getDistanceKm(), Ligne_.distanceKm),
                buildRangeSpecification(criteria.getDureeMoyenne(), Ligne_.dureeMoyenne),
                buildRangeSpecification(criteria.getFrequence(), Ligne_.frequence),
                buildSpecification(criteria.getStatut(), Ligne_.statut),
                buildRangeSpecification(criteria.getDateDebut(), Ligne_.dateDebut),
                buildRangeSpecification(criteria.getDateFin(), Ligne_.dateFin),
                buildSpecification(criteria.getActif(), Ligne_.actif),
                buildSpecification(criteria.getLigneArretsId(), root -> root.join(Ligne_.ligneArrets, JoinType.LEFT).get(LigneArret_.id)),
                buildSpecification(criteria.getOperateurId(), root -> root.join(Ligne_.operateur, JoinType.LEFT).get(Operateur_.id))
            );
        }
        return specification;
    }
}
