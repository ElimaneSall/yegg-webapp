package sn.yegg.app.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.yegg.app.repository.TrackingRepository;
import sn.yegg.app.service.TrackingQueryService;
import sn.yegg.app.service.TrackingService;
import sn.yegg.app.service.criteria.TrackingCriteria;
import sn.yegg.app.service.dto.TrackingDTO;
import sn.yegg.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.yegg.app.domain.Tracking}.
 */
@RestController
@RequestMapping("/api/trackings")
public class TrackingResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrackingResource.class);

    private static final String ENTITY_NAME = "tracking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrackingService trackingService;

    private final TrackingRepository trackingRepository;

    private final TrackingQueryService trackingQueryService;

    public TrackingResource(
        TrackingService trackingService,
        TrackingRepository trackingRepository,
        TrackingQueryService trackingQueryService
    ) {
        this.trackingService = trackingService;
        this.trackingRepository = trackingRepository;
        this.trackingQueryService = trackingQueryService;
    }

    /**
     * {@code POST  /trackings} : Create a new tracking.
     *
     * @param trackingDTO the trackingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trackingDTO, or with status {@code 400 (Bad Request)} if the tracking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrackingDTO> createTracking(@Valid @RequestBody TrackingDTO trackingDTO) throws URISyntaxException {
        LOG.debug("REST request to save Tracking : {}", trackingDTO);
        if (trackingDTO.getId() != null) {
            throw new BadRequestAlertException("A new tracking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trackingDTO = trackingService.save(trackingDTO);
        return ResponseEntity.created(new URI("/api/trackings/" + trackingDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, trackingDTO.getId().toString()))
            .body(trackingDTO);
    }

    /**
     * {@code PUT  /trackings/:id} : Updates an existing tracking.
     *
     * @param id the id of the trackingDTO to save.
     * @param trackingDTO the trackingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackingDTO,
     * or with status {@code 400 (Bad Request)} if the trackingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trackingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrackingDTO> updateTracking(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrackingDTO trackingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Tracking : {}, {}", id, trackingDTO);
        if (trackingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trackingDTO = trackingService.update(trackingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackingDTO.getId().toString()))
            .body(trackingDTO);
    }

    /**
     * {@code PATCH  /trackings/:id} : Partial updates given fields of an existing tracking, field will ignore if it is null
     *
     * @param id the id of the trackingDTO to save.
     * @param trackingDTO the trackingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackingDTO,
     * or with status {@code 400 (Bad Request)} if the trackingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trackingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trackingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrackingDTO> partialUpdateTracking(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrackingDTO trackingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Tracking partially : {}, {}", id, trackingDTO);
        if (trackingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrackingDTO> result = trackingService.partialUpdate(trackingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trackings} : get all the trackings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trackings in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TrackingDTO>> getAllTrackings(
        TrackingCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Trackings by criteria: {}", criteria);

        Page<TrackingDTO> page = trackingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trackings/count} : count all the trackings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTrackings(TrackingCriteria criteria) {
        LOG.debug("REST request to count Trackings by criteria: {}", criteria);
        return ResponseEntity.ok().body(trackingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trackings/:id} : get the "id" tracking.
     *
     * @param id the id of the trackingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trackingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrackingDTO> getTracking(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Tracking : {}", id);
        Optional<TrackingDTO> trackingDTO = trackingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trackingDTO);
    }

    /**
     * {@code DELETE  /trackings/:id} : delete the "id" tracking.
     *
     * @param id the id of the trackingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTracking(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Tracking : {}", id);
        trackingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
