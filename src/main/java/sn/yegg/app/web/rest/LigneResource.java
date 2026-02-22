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
import sn.yegg.app.repository.LigneRepository;
import sn.yegg.app.service.LigneQueryService;
import sn.yegg.app.service.LigneService;
import sn.yegg.app.service.criteria.LigneCriteria;
import sn.yegg.app.service.dto.LigneDTO;
import sn.yegg.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.yegg.app.domain.Ligne}.
 */
@RestController
@RequestMapping("/api/lignes")
public class LigneResource {

    private static final Logger LOG = LoggerFactory.getLogger(LigneResource.class);

    private static final String ENTITY_NAME = "ligne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LigneService ligneService;

    private final LigneRepository ligneRepository;

    private final LigneQueryService ligneQueryService;

    public LigneResource(LigneService ligneService, LigneRepository ligneRepository, LigneQueryService ligneQueryService) {
        this.ligneService = ligneService;
        this.ligneRepository = ligneRepository;
        this.ligneQueryService = ligneQueryService;
    }

    /**
     * {@code POST  /lignes} : Create a new ligne.
     *
     * @param ligneDTO the ligneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ligneDTO, or with status {@code 400 (Bad Request)} if the ligne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LigneDTO> createLigne(@Valid @RequestBody LigneDTO ligneDTO) throws URISyntaxException {
        LOG.debug("REST request to save Ligne : {}", ligneDTO);
        if (ligneDTO.getId() != null) {
            throw new BadRequestAlertException("A new ligne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ligneDTO = ligneService.save(ligneDTO);
        return ResponseEntity.created(new URI("/api/lignes/" + ligneDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ligneDTO.getId().toString()))
            .body(ligneDTO);
    }

    /**
     * {@code PUT  /lignes/:id} : Updates an existing ligne.
     *
     * @param id the id of the ligneDTO to save.
     * @param ligneDTO the ligneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneDTO,
     * or with status {@code 400 (Bad Request)} if the ligneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ligneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LigneDTO> updateLigne(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LigneDTO ligneDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Ligne : {}, {}", id, ligneDTO);
        if (ligneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ligneDTO = ligneService.update(ligneDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneDTO.getId().toString()))
            .body(ligneDTO);
    }

    /**
     * {@code PATCH  /lignes/:id} : Partial updates given fields of an existing ligne, field will ignore if it is null
     *
     * @param id the id of the ligneDTO to save.
     * @param ligneDTO the ligneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ligneDTO,
     * or with status {@code 400 (Bad Request)} if the ligneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ligneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ligneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LigneDTO> partialUpdateLigne(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LigneDTO ligneDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Ligne partially : {}, {}", id, ligneDTO);
        if (ligneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ligneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ligneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LigneDTO> result = ligneService.partialUpdate(ligneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ligneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lignes} : get all the lignes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lignes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LigneDTO>> getAllLignes(
        LigneCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Lignes by criteria: {}", criteria);

        Page<LigneDTO> page = ligneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lignes/count} : count all the lignes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLignes(LigneCriteria criteria) {
        LOG.debug("REST request to count Lignes by criteria: {}", criteria);
        return ResponseEntity.ok().body(ligneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lignes/:id} : get the "id" ligne.
     *
     * @param id the id of the ligneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ligneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LigneDTO> getLigne(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Ligne : {}", id);
        Optional<LigneDTO> ligneDTO = ligneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ligneDTO);
    }

    /**
     * {@code DELETE  /lignes/:id} : delete the "id" ligne.
     *
     * @param id the id of the ligneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLigne(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Ligne : {}", id);
        ligneService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
