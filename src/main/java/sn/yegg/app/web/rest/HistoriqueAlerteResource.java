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
import sn.yegg.app.repository.HistoriqueAlerteRepository;
import sn.yegg.app.service.HistoriqueAlerteQueryService;
import sn.yegg.app.service.HistoriqueAlerteService;
import sn.yegg.app.service.criteria.HistoriqueAlerteCriteria;
import sn.yegg.app.service.dto.HistoriqueAlerteDTO;
import sn.yegg.app.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.yegg.app.domain.HistoriqueAlerte}.
 */
@RestController
@RequestMapping("/api/historique-alertes")
public class HistoriqueAlerteResource {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueAlerteResource.class);

    private static final String ENTITY_NAME = "historiqueAlerte";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriqueAlerteService historiqueAlerteService;

    private final HistoriqueAlerteRepository historiqueAlerteRepository;

    private final HistoriqueAlerteQueryService historiqueAlerteQueryService;

    public HistoriqueAlerteResource(
        HistoriqueAlerteService historiqueAlerteService,
        HistoriqueAlerteRepository historiqueAlerteRepository,
        HistoriqueAlerteQueryService historiqueAlerteQueryService
    ) {
        this.historiqueAlerteService = historiqueAlerteService;
        this.historiqueAlerteRepository = historiqueAlerteRepository;
        this.historiqueAlerteQueryService = historiqueAlerteQueryService;
    }

    /**
     * {@code POST  /historique-alertes} : Create a new historiqueAlerte.
     *
     * @param historiqueAlerteDTO the historiqueAlerteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiqueAlerteDTO, or with status {@code 400 (Bad Request)} if the historiqueAlerte has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HistoriqueAlerteDTO> createHistoriqueAlerte(@Valid @RequestBody HistoriqueAlerteDTO historiqueAlerteDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HistoriqueAlerte : {}", historiqueAlerteDTO);
        if (historiqueAlerteDTO.getId() != null) {
            throw new BadRequestAlertException("A new historiqueAlerte cannot already have an ID", ENTITY_NAME, "idexists");
        }
        historiqueAlerteDTO = historiqueAlerteService.save(historiqueAlerteDTO);
        return ResponseEntity.created(new URI("/api/historique-alertes/" + historiqueAlerteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, historiqueAlerteDTO.getId().toString()))
            .body(historiqueAlerteDTO);
    }

    /**
     * {@code PUT  /historique-alertes/:id} : Updates an existing historiqueAlerte.
     *
     * @param id the id of the historiqueAlerteDTO to save.
     * @param historiqueAlerteDTO the historiqueAlerteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueAlerteDTO,
     * or with status {@code 400 (Bad Request)} if the historiqueAlerteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiqueAlerteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HistoriqueAlerteDTO> updateHistoriqueAlerte(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistoriqueAlerteDTO historiqueAlerteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HistoriqueAlerte : {}, {}", id, historiqueAlerteDTO);
        if (historiqueAlerteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiqueAlerteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiqueAlerteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        historiqueAlerteDTO = historiqueAlerteService.update(historiqueAlerteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueAlerteDTO.getId().toString()))
            .body(historiqueAlerteDTO);
    }

    /**
     * {@code PATCH  /historique-alertes/:id} : Partial updates given fields of an existing historiqueAlerte, field will ignore if it is null
     *
     * @param id the id of the historiqueAlerteDTO to save.
     * @param historiqueAlerteDTO the historiqueAlerteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueAlerteDTO,
     * or with status {@code 400 (Bad Request)} if the historiqueAlerteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historiqueAlerteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historiqueAlerteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HistoriqueAlerteDTO> partialUpdateHistoriqueAlerte(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistoriqueAlerteDTO historiqueAlerteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HistoriqueAlerte partially : {}, {}", id, historiqueAlerteDTO);
        if (historiqueAlerteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiqueAlerteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiqueAlerteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoriqueAlerteDTO> result = historiqueAlerteService.partialUpdate(historiqueAlerteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueAlerteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /historique-alertes} : get all the historiqueAlertes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiqueAlertes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HistoriqueAlerteDTO>> getAllHistoriqueAlertes(
        HistoriqueAlerteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HistoriqueAlertes by criteria: {}", criteria);

        Page<HistoriqueAlerteDTO> page = historiqueAlerteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historique-alertes/count} : count all the historiqueAlertes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHistoriqueAlertes(HistoriqueAlerteCriteria criteria) {
        LOG.debug("REST request to count HistoriqueAlertes by criteria: {}", criteria);
        return ResponseEntity.ok().body(historiqueAlerteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /historique-alertes/:id} : get the "id" historiqueAlerte.
     *
     * @param id the id of the historiqueAlerteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiqueAlerteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueAlerteDTO> getHistoriqueAlerte(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HistoriqueAlerte : {}", id);
        Optional<HistoriqueAlerteDTO> historiqueAlerteDTO = historiqueAlerteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiqueAlerteDTO);
    }

    /**
     * {@code DELETE  /historique-alertes/:id} : delete the "id" historiqueAlerte.
     *
     * @param id the id of the historiqueAlerteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoriqueAlerte(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HistoriqueAlerte : {}", id);
        historiqueAlerteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
