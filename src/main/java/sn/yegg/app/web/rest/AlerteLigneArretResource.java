package sn.yegg.app.web.rest;

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
import sn.yegg.app.repository.AlerteLigneArretRepository;
import sn.yegg.app.service.AlerteLigneArretQueryService;
import sn.yegg.app.service.AlerteLigneArretService;
import sn.yegg.app.service.criteria.AlerteLigneArretCriteria;
import sn.yegg.app.service.dto.AlerteLigneArretDTO;
import sn.yegg.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.yegg.app.domain.AlerteLigneArret}.
 */
@RestController
@RequestMapping("/api/alerte-ligne-arrets")
public class AlerteLigneArretResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlerteLigneArretResource.class);

    private static final String ENTITY_NAME = "alerteLigneArret";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlerteLigneArretService alerteLigneArretService;

    private final AlerteLigneArretRepository alerteLigneArretRepository;

    private final AlerteLigneArretQueryService alerteLigneArretQueryService;

    public AlerteLigneArretResource(
        AlerteLigneArretService alerteLigneArretService,
        AlerteLigneArretRepository alerteLigneArretRepository,
        AlerteLigneArretQueryService alerteLigneArretQueryService
    ) {
        this.alerteLigneArretService = alerteLigneArretService;
        this.alerteLigneArretRepository = alerteLigneArretRepository;
        this.alerteLigneArretQueryService = alerteLigneArretQueryService;
    }

    /**
     * {@code POST  /alerte-ligne-arrets} : Create a new alerteLigneArret.
     *
     * @param alerteLigneArretDTO the alerteLigneArretDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alerteLigneArretDTO, or with status {@code 400 (Bad Request)} if the alerteLigneArret has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlerteLigneArretDTO> createAlerteLigneArret(@RequestBody AlerteLigneArretDTO alerteLigneArretDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlerteLigneArret : {}", alerteLigneArretDTO);
        if (alerteLigneArretDTO.getId() != null) {
            throw new BadRequestAlertException("A new alerteLigneArret cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alerteLigneArretDTO = alerteLigneArretService.save(alerteLigneArretDTO);
        return ResponseEntity.created(new URI("/api/alerte-ligne-arrets/" + alerteLigneArretDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alerteLigneArretDTO.getId().toString()))
            .body(alerteLigneArretDTO);
    }

    /**
     * {@code PUT  /alerte-ligne-arrets/:id} : Updates an existing alerteLigneArret.
     *
     * @param id the id of the alerteLigneArretDTO to save.
     * @param alerteLigneArretDTO the alerteLigneArretDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerteLigneArretDTO,
     * or with status {@code 400 (Bad Request)} if the alerteLigneArretDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alerteLigneArretDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlerteLigneArretDTO> updateAlerteLigneArret(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlerteLigneArretDTO alerteLigneArretDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlerteLigneArret : {}, {}", id, alerteLigneArretDTO);
        if (alerteLigneArretDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alerteLigneArretDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alerteLigneArretRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alerteLigneArretDTO = alerteLigneArretService.update(alerteLigneArretDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alerteLigneArretDTO.getId().toString()))
            .body(alerteLigneArretDTO);
    }

    /**
     * {@code PATCH  /alerte-ligne-arrets/:id} : Partial updates given fields of an existing alerteLigneArret, field will ignore if it is null
     *
     * @param id the id of the alerteLigneArretDTO to save.
     * @param alerteLigneArretDTO the alerteLigneArretDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alerteLigneArretDTO,
     * or with status {@code 400 (Bad Request)} if the alerteLigneArretDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alerteLigneArretDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alerteLigneArretDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlerteLigneArretDTO> partialUpdateAlerteLigneArret(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlerteLigneArretDTO alerteLigneArretDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlerteLigneArret partially : {}, {}", id, alerteLigneArretDTO);
        if (alerteLigneArretDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alerteLigneArretDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alerteLigneArretRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlerteLigneArretDTO> result = alerteLigneArretService.partialUpdate(alerteLigneArretDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alerteLigneArretDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /alerte-ligne-arrets} : get all the alerteLigneArrets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alerteLigneArrets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlerteLigneArretDTO>> getAllAlerteLigneArrets(
        AlerteLigneArretCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlerteLigneArrets by criteria: {}", criteria);

        Page<AlerteLigneArretDTO> page = alerteLigneArretQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /alerte-ligne-arrets/count} : count all the alerteLigneArrets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlerteLigneArrets(AlerteLigneArretCriteria criteria) {
        LOG.debug("REST request to count AlerteLigneArrets by criteria: {}", criteria);
        return ResponseEntity.ok().body(alerteLigneArretQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /alerte-ligne-arrets/:id} : get the "id" alerteLigneArret.
     *
     * @param id the id of the alerteLigneArretDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alerteLigneArretDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlerteLigneArretDTO> getAlerteLigneArret(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlerteLigneArret : {}", id);
        Optional<AlerteLigneArretDTO> alerteLigneArretDTO = alerteLigneArretService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alerteLigneArretDTO);
    }

    /**
     * {@code DELETE  /alerte-ligne-arrets/:id} : delete the "id" alerteLigneArret.
     *
     * @param id the id of the alerteLigneArretDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlerteLigneArret(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlerteLigneArret : {}", id);
        alerteLigneArretService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
