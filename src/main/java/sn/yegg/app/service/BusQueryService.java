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
import sn.yegg.app.domain.Bus;
import sn.yegg.app.repository.BusRepository;
import sn.yegg.app.service.criteria.BusCriteria;
import sn.yegg.app.service.dto.BusDTO;
import sn.yegg.app.service.mapper.BusMapper;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Bus} entities in the database.
 * The main input is a {@link BusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusQueryService extends QueryService<Bus> {

    private static final Logger LOG = LoggerFactory.getLogger(BusQueryService.class);

    private final BusRepository busRepository;

    private final BusMapper busMapper;

    public BusQueryService(BusRepository busRepository, BusMapper busMapper) {
        this.busRepository = busRepository;
        this.busMapper = busMapper;
    }

    /**
     * Return a {@link Page} of {@link BusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusDTO> findByCriteria(BusCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Bus> specification = createSpecification(criteria);
        return busRepository.findAll(specification, page).map(busMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BusCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Bus> specification = createSpecification(criteria);
        return busRepository.count(specification);
    }

    /**
     * Function to convert {@link BusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Bus> createSpecification(BusCriteria criteria) {
        Specification<Bus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Bus_.id),
                buildStringSpecification(criteria.getNumeroVehicule(), Bus_.numeroVehicule),
                buildStringSpecification(criteria.getPlaque(), Bus_.plaque),
                buildStringSpecification(criteria.getModele(), Bus_.modele),
                buildStringSpecification(criteria.getConstructeur(), Bus_.constructeur),
                buildRangeSpecification(criteria.getCapacite(), Bus_.capacite),
                buildRangeSpecification(criteria.getCapaciteDebout(), Bus_.capaciteDebout),
                buildRangeSpecification(criteria.getAnneeFabrication(), Bus_.anneeFabrication),
                buildSpecification(criteria.getEnergie(), Bus_.energie),
                buildRangeSpecification(criteria.getAutonomieKm(), Bus_.autonomieKm),
                buildStringSpecification(criteria.getGpsDeviceId(), Bus_.gpsDeviceId),
                buildStringSpecification(criteria.getGpsStatus(), Bus_.gpsStatus),
                buildRangeSpecification(criteria.getGpsLastPing(), Bus_.gpsLastPing),
                buildRangeSpecification(criteria.getGpsBatteryLevel(), Bus_.gpsBatteryLevel),
                buildRangeSpecification(criteria.getCurrentLatitude(), Bus_.currentLatitude),
                buildRangeSpecification(criteria.getCurrentLongitude(), Bus_.currentLongitude),
                buildRangeSpecification(criteria.getCurrentVitesse(), Bus_.currentVitesse),
                buildRangeSpecification(criteria.getCurrentCap(), Bus_.currentCap),
                buildRangeSpecification(criteria.getPositionUpdatedAt(), Bus_.positionUpdatedAt),
                buildSpecification(criteria.getStatut(), Bus_.statut),
                buildRangeSpecification(criteria.getDateMiseEnService(), Bus_.dateMiseEnService),
                buildRangeSpecification(criteria.getDateDernierEntretien(), Bus_.dateDernierEntretien),
                buildRangeSpecification(criteria.getProchainEntretienKm(), Bus_.prochainEntretienKm),
                buildSpecification(criteria.getLigneId(), root -> root.join(Bus_.ligne, JoinType.LEFT).get(Ligne_.id)),
                buildSpecification(criteria.getChauffeurId(), root -> root.join(Bus_.chauffeur, JoinType.LEFT).get(Utilisateur_.id))
            );
        }
        return specification;
    }
}
