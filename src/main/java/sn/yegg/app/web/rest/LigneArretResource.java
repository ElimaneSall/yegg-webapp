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
import sn.yegg.app.repository.LigneArretRepository;
import sn.yegg.app.service.LigneArretQueryService;
import sn.yegg.app.service.LigneArretService;
import sn.yegg.app.service.criteria.LigneArretCriteria;
import sn.yegg.app.service.dto.LigneArretDTO;
import sn.yegg.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.yegg.app.domain.LigneArret}.
 */
@RestController
@RequestMapping("/api/ligne-arrets")
public class LigneArretResource {

    private static final Logger LOG = LoggerFactory.getLogger(LigneArretResource.class);

    private static final String ENTITY_NAME = "ligneArret";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigneArretService ligneArretService;

    private final LigneArretRepository ligneArretRepository;

    private final LigneArretQueryService ligneArretQueryService;

    public LigneArretResource(
        LigneArretService ligneArretService,
        LigneArretRepository ligneArretRepository,
        LigneArretQueryService ligneArretQueryService
    ) {
        this.ligneArretService = ligneArretService;
        this.ligneArretRepository = ligneArretRepository;
        this.ligneArretQueryService = ligneArretQueryService;
    }

    /**
     * {@code POST  /ligne-arrets} : Create a new ligneArret.
     *
     * @param ligneArretDTO the ligneArretDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ligneArretDTO, or with status {@code 400 (Bad Request)} if the ligneArret has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LigneArretDTO> createLigneArret(@Valid @RequestBody LigneArretDTO ligneArretDTO) throws URISyntaxException {
        LOG.debug("REST request to save LigneArret : {}", ligneArretDTO);
        if (ligneArretDTO.getId() != null) {
            throw new BadRequestAlertException("A new ligneArret cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ligneArretDTO = ligneArretService.save(ligneArretDTO);
        return ResponseEntity.created(new URI("/api/ligne-arrets/" + ligneArretDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ligneArretDTO.getId().toString()))
            .body(ligneArretDTO);
    }

    /**
     * {@code PUT  /ligne-arrets/:id} : Updates an existing ligneArret.
     *
     * @param id the id of the ligneArretDTO to save.
     * @param ligneArretDTO the ligneArretDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneArretDTO,
     * or with status {@code 400 (Bad Request)} if the ligneArretDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ligneArretDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LigneArretDTO> updateLigneArret(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LigneArretDTO ligneArretDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LigneArret : {}, {}", id, ligneArretDTO);
        if (ligneArretDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneArretDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneArretRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ligneArretDTO = ligneArretService.update(ligneArretDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneArretDTO.getId().toString()))
            .body(ligneArretDTO);
    }

    /**
     * {@code PATCH  /ligne-arrets/:id} : Partial updates given fields of an existing ligneArret, field will ignore if it is null
     *
     * @param id the id of the ligneArretDTO to save.
     * @param ligneArretDTO the ligneArretDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneArretDTO,
     * or with status {@code 400 (Bad Request)} if the ligneArretDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ligneArretDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ligneArretDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LigneArretDTO> partialUpdateLigneArret(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LigneArretDTO ligneArretDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LigneArret partially : {}, {}", id, ligneArretDTO);
        if (ligneArretDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneArretDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneArretRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LigneArretDTO> result = ligneArretService.partialUpdate(ligneArretDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneArretDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ligne-arrets} : get all the ligneArrets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ligneArrets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LigneArretDTO>> getAllLigneArrets(
        LigneArretCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get LigneArrets by criteria: {}", criteria);

        Page<LigneArretDTO> page = ligneArretQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ligne-arrets/count} : count all the ligneArrets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLigneArrets(LigneArretCriteria criteria) {
        LOG.debug("REST request to count LigneArrets by criteria: {}", criteria);
        return ResponseEntity.ok().body(ligneArretQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ligne-arrets/:id} : get the "id" ligneArret.
     *
     * @param id the id of the ligneArretDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ligneArretDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LigneArretDTO> getLigneArret(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LigneArret : {}", id);
        Optional<LigneArretDTO> ligneArretDTO = ligneArretService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ligneArretDTO);
    }

    /**
     * {@code DELETE  /ligne-arrets/:id} : delete the "id" ligneArret.
     *
     * @param id the id of the ligneArretDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLigneArret(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LigneArret : {}", id);
        ligneArretService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
