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
import sn.yegg.app.repository.ArretRepository;
import sn.yegg.app.service.ArretQueryService;
import sn.yegg.app.service.ArretService;
import sn.yegg.app.service.criteria.ArretCriteria;
import sn.yegg.app.service.dto.ArretDTO;
import sn.yegg.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.yegg.app.domain.Arret}.
 */
@RestController
@RequestMapping("/api/arrets")
public class ArretResource {

    private static final Logger LOG = LoggerFactory.getLogger(ArretResource.class);

    private static final String ENTITY_NAME = "arret";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArretService arretService;

    private final ArretRepository arretRepository;

    private final ArretQueryService arretQueryService;

    public ArretResource(ArretService arretService, ArretRepository arretRepository, ArretQueryService arretQueryService) {
        this.arretService = arretService;
        this.arretRepository = arretRepository;
        this.arretQueryService = arretQueryService;
    }

    /**
     * {@code POST  /arrets} : Create a new arret.
     *
     * @param arretDTO the arretDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arretDTO, or with status {@code 400 (Bad Request)} if the arret has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ArretDTO> createArret(@Valid @RequestBody ArretDTO arretDTO) throws URISyntaxException {
        LOG.debug("REST request to save Arret : {}", arretDTO);
        if (arretDTO.getId() != null) {
            throw new BadRequestAlertException("A new arret cannot already have an ID", ENTITY_NAME, "idexists");
        }
        arretDTO = arretService.save(arretDTO);
        return ResponseEntity.created(new URI("/api/arrets/" + arretDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, arretDTO.getId().toString()))
            .body(arretDTO);
    }

    /**
     * {@code PUT  /arrets/:id} : Updates an existing arret.
     *
     * @param id the id of the arretDTO to save.
     * @param arretDTO the arretDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arretDTO,
     * or with status {@code 400 (Bad Request)} if the arretDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arretDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ArretDTO> updateArret(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ArretDTO arretDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Arret : {}, {}", id, arretDTO);
        if (arretDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arretDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arretRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        arretDTO = arretService.update(arretDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arretDTO.getId().toString()))
            .body(arretDTO);
    }

    /**
     * {@code PATCH  /arrets/:id} : Partial updates given fields of an existing arret, field will ignore if it is null
     *
     * @param id the id of the arretDTO to save.
     * @param arretDTO the arretDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arretDTO,
     * or with status {@code 400 (Bad Request)} if the arretDTO is not valid,
     * or with status {@code 404 (Not Found)} if the arretDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the arretDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArretDTO> partialUpdateArret(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ArretDTO arretDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Arret partially : {}, {}", id, arretDTO);
        if (arretDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, arretDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!arretRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArretDTO> result = arretService.partialUpdate(arretDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arretDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /arrets} : get all the arrets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arrets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ArretDTO>> getAllArrets(
        ArretCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Arrets by criteria: {}", criteria);

        Page<ArretDTO> page = arretQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /arrets/count} : count all the arrets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countArrets(ArretCriteria criteria) {
        LOG.debug("REST request to count Arrets by criteria: {}", criteria);
        return ResponseEntity.ok().body(arretQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /arrets/:id} : get the "id" arret.
     *
     * @param id the id of the arretDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arretDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArretDTO> getArret(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Arret : {}", id);
        Optional<ArretDTO> arretDTO = arretService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arretDTO);
    }

    /**
     * {@code DELETE  /arrets/:id} : delete the "id" arret.
     *
     * @param id the id of the arretDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArret(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Arret : {}", id);
        arretService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
