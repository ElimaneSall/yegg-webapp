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
import sn.yegg.app.repository.AlerteApprocheRepository;
import sn.yegg.app.service.AlerteApprocheQueryService;
import sn.yegg.app.service.AlerteApprocheService;
import sn.yegg.app.service.criteria.AlerteApprocheCriteria;
import sn.yegg.app.service.dto.AlertCheckRequest;
import sn.yegg.app.service.dto.AlertCheckResponse;
import sn.yegg.app.service.dto.AlerteApprocheDTO;
import sn.yegg.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.yegg.app.domain.AlerteApproche}.
 */
@RestController
@RequestMapping("/api/alerte-approches")
public class AlerteApprocheResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlerteApprocheResource.class);

    private static final String ENTITY_NAME = "alerteApproche";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlerteApprocheService alerteApprocheService;

    private final AlerteApprocheRepository alerteApprocheRepository;

    private final AlerteApprocheQueryService alerteApprocheQueryService;

    public AlerteApprocheResource(
        AlerteApprocheService alerteApprocheService,
        AlerteApprocheRepository alerteApprocheRepository,
        AlerteApprocheQueryService alerteApprocheQueryService
    ) {
        this.alerteApprocheService = alerteApprocheService;
        this.alerteApprocheRepository = alerteApprocheRepository;
        this.alerteApprocheQueryService = alerteApprocheQueryService;
    }

    /**
     * {@code POST  /alerte-approches} : Create a new alerteApproche.
     *
     * @param alerteApprocheDTO the alerteApprocheDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alerteApprocheDTO, or with status {@code 400 (Bad Request)} if the alerteApproche has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlerteApprocheDTO> createAlerteApproche(@Valid @RequestBody AlerteApprocheDTO alerteApprocheDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlerteApproche : {}", alerteApprocheDTO);
        if (alerteApprocheDTO.getId() != null) {
            throw new BadRequestAlertException("A new alerteApproche cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alerteApprocheDTO = alerteApprocheService.save(alerteApprocheDTO);
        return ResponseEntity.created(new URI("/api/alerte-approches/" + alerteApprocheDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alerteApprocheDTO.getId().toString()))
            .body(alerteApprocheDTO);
    }

    /**
     * {@code PUT  /alerte-approches/:id} : Updates an existing alerteApproche.
     *
     * @param id the id of the alerteApprocheDTO to save.
     * @param alerteApprocheDTO the alerteApprocheDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerteApprocheDTO,
     * or with status {@code 400 (Bad Request)} if the alerteApprocheDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alerteApprocheDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlerteApprocheDTO> updateAlerteApproche(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlerteApprocheDTO alerteApprocheDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlerteApproche : {}, {}", id, alerteApprocheDTO);
        if (alerteApprocheDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alerteApprocheDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alerteApprocheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alerteApprocheDTO = alerteApprocheService.update(alerteApprocheDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alerteApprocheDTO.getId().toString()))
            .body(alerteApprocheDTO);
    }

    /**
     * {@code PATCH  /alerte-approches/:id} : Partial updates given fields of an existing alerteApproche, field will ignore if it is null
     *
     * @param id the id of the alerteApprocheDTO to save.
     * @param alerteApprocheDTO the alerteApprocheDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerteApprocheDTO,
     * or with status {@code 400 (Bad Request)} if the alerteApprocheDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alerteApprocheDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alerteApprocheDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlerteApprocheDTO> partialUpdateAlerteApproche(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlerteApprocheDTO alerteApprocheDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlerteApproche partially : {}, {}", id, alerteApprocheDTO);
        if (alerteApprocheDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alerteApprocheDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alerteApprocheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlerteApprocheDTO> result = alerteApprocheService.partialUpdate(alerteApprocheDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alerteApprocheDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /alerte-approches} : get all the alerteApproches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alerteApproches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlerteApprocheDTO>> getAllAlerteApproches(
        AlerteApprocheCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlerteApproches by criteria: {}", criteria);

        Page<AlerteApprocheDTO> page = alerteApprocheQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alerte-approches/count} : count all the alerteApproches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlerteApproches(AlerteApprocheCriteria criteria) {
        LOG.debug("REST request to count AlerteApproches by criteria: {}", criteria);
        return ResponseEntity.ok().body(alerteApprocheQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /alerte-approches/:id} : get the "id" alerteApproche.
     *
     * @param id the id of the alerteApprocheDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alerteApprocheDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlerteApprocheDTO> getAlerteApproche(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlerteApproche : {}", id);
        Optional<AlerteApprocheDTO> alerteApprocheDTO = alerteApprocheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alerteApprocheDTO);
    }

    /**
     * {@code DELETE  /alerte-approches/:id} : delete the "id" alerteApproche.
     *
     * @param id the id of the alerteApprocheDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlerteApproche(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlerteApproche : {}", id);
        alerteApprocheService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * POST /alerts/check : Vérifie si un bus déclenche des alertes
     */
    @PostMapping("/alerts/check")
    public ResponseEntity<AlertCheckResponse> checkAlerts(@Valid @RequestBody AlertCheckRequest request) {
        LOG.debug("REST request to check alerts: {}", request);

        AlertCheckResponse response = alerteApprocheService.checkAlerts(request);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /alerts/bus/{busId}/check : Vérifie les alertes pour un bus spécifique
     */
    @GetMapping("/alerts/bus/{busId}/check")
    public ResponseEntity<AlertCheckResponse> checkAlertsForBus(
        @PathVariable Long busId,
        @RequestParam Double latitude,
        @RequestParam Double longitude,
        @RequestParam(required = false) Double vitesse,
        @RequestParam(required = false) Integer cap
    ) {
        LOG.debug("REST request to check alerts for bus {} at ({}, {})", busId, latitude, longitude);

        AlertCheckRequest request = new AlertCheckRequest();
        request.setBusId(busId);
        request.setLatitude(latitude);
        request.setLongitude(longitude);
        request.setVitesse(vitesse);
        request.setCap(cap);
        request.setTimestamp(java.time.Instant.now());

        AlertCheckResponse response = alerteApprocheService.checkAlerts(request);

        return ResponseEntity.ok(response);
    }
}
