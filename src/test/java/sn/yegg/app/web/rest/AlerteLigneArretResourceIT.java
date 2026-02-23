package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.AlerteLigneArretAsserts.*;
import static sn.yegg.app.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.IntegrationTest;
import sn.yegg.app.domain.AlerteApproche;
import sn.yegg.app.domain.AlerteLigneArret;
import sn.yegg.app.domain.Arret;
import sn.yegg.app.domain.Ligne;
import sn.yegg.app.repository.AlerteLigneArretRepository;
import sn.yegg.app.service.dto.AlerteLigneArretDTO;
import sn.yegg.app.service.mapper.AlerteLigneArretMapper;

/**
 * Integration tests for the {@link AlerteLigneArretResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlerteLigneArretResourceIT {

    private static final String DEFAULT_SENS = "AAAAAAAAAA";
    private static final String UPDATED_SENS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/alerte-ligne-arrets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlerteLigneArretRepository alerteLigneArretRepository;

    @Autowired
    private AlerteLigneArretMapper alerteLigneArretMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlerteLigneArretMockMvc;

    private AlerteLigneArret alerteLigneArret;

    private AlerteLigneArret insertedAlerteLigneArret;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlerteLigneArret createEntity() {
        return new AlerteLigneArret().sens(DEFAULT_SENS).actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlerteLigneArret createUpdatedEntity() {
        return new AlerteLigneArret().sens(UPDATED_SENS).actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        alerteLigneArret = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAlerteLigneArret != null) {
            alerteLigneArretRepository.delete(insertedAlerteLigneArret);
            insertedAlerteLigneArret = null;
        }
    }

    @Test
    @Transactional
    void createAlerteLigneArret() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlerteLigneArret
        AlerteLigneArretDTO alerteLigneArretDTO = alerteLigneArretMapper.toDto(alerteLigneArret);
        var returnedAlerteLigneArretDTO = om.readValue(
            restAlerteLigneArretMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteLigneArretDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlerteLigneArretDTO.class
        );

        // Validate the AlerteLigneArret in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlerteLigneArret = alerteLigneArretMapper.toEntity(returnedAlerteLigneArretDTO);
        assertAlerteLigneArretUpdatableFieldsEquals(returnedAlerteLigneArret, getPersistedAlerteLigneArret(returnedAlerteLigneArret));

        insertedAlerteLigneArret = returnedAlerteLigneArret;
    }

    @Test
    @Transactional
    void createAlerteLigneArretWithExistingId() throws Exception {
        // Create the AlerteLigneArret with an existing ID
        alerteLigneArret.setId(1L);
        AlerteLigneArretDTO alerteLigneArretDTO = alerteLigneArretMapper.toDto(alerteLigneArret);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlerteLigneArretMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteLigneArretDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlerteLigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlerteLigneArrets() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        // Get all the alerteLigneArretList
        restAlerteLigneArretMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alerteLigneArret.getId().intValue())))
            .andExpect(jsonPath("$.[*].sens").value(hasItem(DEFAULT_SENS)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getAlerteLigneArret() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        // Get the alerteLigneArret
        restAlerteLigneArretMockMvc
            .perform(get(ENTITY_API_URL_ID, alerteLigneArret.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alerteLigneArret.getId().intValue()))
            .andExpect(jsonPath("$.sens").value(DEFAULT_SENS))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getAlerteLigneArretsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        Long id = alerteLigneArret.getId();

        defaultAlerteLigneArretFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlerteLigneArretFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlerteLigneArretFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlerteLigneArretsBySensIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        // Get all the alerteLigneArretList where sens equals to
        defaultAlerteLigneArretFiltering("sens.equals=" + DEFAULT_SENS, "sens.equals=" + UPDATED_SENS);
    }

    @Test
    @Transactional
    void getAllAlerteLigneArretsBySensIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        // Get all the alerteLigneArretList where sens in
        defaultAlerteLigneArretFiltering("sens.in=" + DEFAULT_SENS + "," + UPDATED_SENS, "sens.in=" + UPDATED_SENS);
    }

    @Test
    @Transactional
    void getAllAlerteLigneArretsBySensIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        // Get all the alerteLigneArretList where sens is not null
        defaultAlerteLigneArretFiltering("sens.specified=true", "sens.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteLigneArretsBySensContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        // Get all the alerteLigneArretList where sens contains
        defaultAlerteLigneArretFiltering("sens.contains=" + DEFAULT_SENS, "sens.contains=" + UPDATED_SENS);
    }

    @Test
    @Transactional
    void getAllAlerteLigneArretsBySensNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        // Get all the alerteLigneArretList where sens does not contain
        defaultAlerteLigneArretFiltering("sens.doesNotContain=" + UPDATED_SENS, "sens.doesNotContain=" + DEFAULT_SENS);
    }

    @Test
    @Transactional
    void getAllAlerteLigneArretsByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        // Get all the alerteLigneArretList where actif equals to
        defaultAlerteLigneArretFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllAlerteLigneArretsByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        // Get all the alerteLigneArretList where actif in
        defaultAlerteLigneArretFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllAlerteLigneArretsByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        // Get all the alerteLigneArretList where actif is not null
        defaultAlerteLigneArretFiltering("actif.specified=true", "actif.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteLigneArretsByLigneIsEqualToSomething() throws Exception {
        Ligne ligne;
        if (TestUtil.findAll(em, Ligne.class).isEmpty()) {
            alerteLigneArretRepository.saveAndFlush(alerteLigneArret);
            ligne = LigneResourceIT.createEntity();
        } else {
            ligne = TestUtil.findAll(em, Ligne.class).get(0);
        }
        em.persist(ligne);
        em.flush();
        alerteLigneArret.setLigne(ligne);
        alerteLigneArretRepository.saveAndFlush(alerteLigneArret);
        Long ligneId = ligne.getId();
        // Get all the alerteLigneArretList where ligne equals to ligneId
        defaultAlerteLigneArretShouldBeFound("ligneId.equals=" + ligneId);

        // Get all the alerteLigneArretList where ligne equals to (ligneId + 1)
        defaultAlerteLigneArretShouldNotBeFound("ligneId.equals=" + (ligneId + 1));
    }

    @Test
    @Transactional
    void getAllAlerteLigneArretsByArretIsEqualToSomething() throws Exception {
        Arret arret;
        if (TestUtil.findAll(em, Arret.class).isEmpty()) {
            alerteLigneArretRepository.saveAndFlush(alerteLigneArret);
            arret = ArretResourceIT.createEntity();
        } else {
            arret = TestUtil.findAll(em, Arret.class).get(0);
        }
        em.persist(arret);
        em.flush();
        alerteLigneArret.setArret(arret);
        alerteLigneArretRepository.saveAndFlush(alerteLigneArret);
        Long arretId = arret.getId();
        // Get all the alerteLigneArretList where arret equals to arretId
        defaultAlerteLigneArretShouldBeFound("arretId.equals=" + arretId);

        // Get all the alerteLigneArretList where arret equals to (arretId + 1)
        defaultAlerteLigneArretShouldNotBeFound("arretId.equals=" + (arretId + 1));
    }

    @Test
    @Transactional
    void getAllAlerteLigneArretsByAlerteApprocheIsEqualToSomething() throws Exception {
        AlerteApproche alerteApproche;
        if (TestUtil.findAll(em, AlerteApproche.class).isEmpty()) {
            alerteLigneArretRepository.saveAndFlush(alerteLigneArret);
            alerteApproche = AlerteApprocheResourceIT.createEntity();
        } else {
            alerteApproche = TestUtil.findAll(em, AlerteApproche.class).get(0);
        }
        em.persist(alerteApproche);
        em.flush();
        alerteLigneArret.setAlerteApproche(alerteApproche);
        alerteLigneArretRepository.saveAndFlush(alerteLigneArret);
        Long alerteApprocheId = alerteApproche.getId();
        // Get all the alerteLigneArretList where alerteApproche equals to alerteApprocheId
        defaultAlerteLigneArretShouldBeFound("alerteApprocheId.equals=" + alerteApprocheId);

        // Get all the alerteLigneArretList where alerteApproche equals to (alerteApprocheId + 1)
        defaultAlerteLigneArretShouldNotBeFound("alerteApprocheId.equals=" + (alerteApprocheId + 1));
    }

    private void defaultAlerteLigneArretFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlerteLigneArretShouldBeFound(shouldBeFound);
        defaultAlerteLigneArretShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlerteLigneArretShouldBeFound(String filter) throws Exception {
        restAlerteLigneArretMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alerteLigneArret.getId().intValue())))
            .andExpect(jsonPath("$.[*].sens").value(hasItem(DEFAULT_SENS)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));

        // Check, that the count call also returns 1
        restAlerteLigneArretMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlerteLigneArretShouldNotBeFound(String filter) throws Exception {
        restAlerteLigneArretMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlerteLigneArretMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlerteLigneArret() throws Exception {
        // Get the alerteLigneArret
        restAlerteLigneArretMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlerteLigneArret() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerteLigneArret
        AlerteLigneArret updatedAlerteLigneArret = alerteLigneArretRepository.findById(alerteLigneArret.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlerteLigneArret are not directly saved in db
        em.detach(updatedAlerteLigneArret);
        updatedAlerteLigneArret.sens(UPDATED_SENS).actif(UPDATED_ACTIF);
        AlerteLigneArretDTO alerteLigneArretDTO = alerteLigneArretMapper.toDto(updatedAlerteLigneArret);

        restAlerteLigneArretMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alerteLigneArretDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alerteLigneArretDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlerteLigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlerteLigneArretToMatchAllProperties(updatedAlerteLigneArret);
    }

    @Test
    @Transactional
    void putNonExistingAlerteLigneArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteLigneArret.setId(longCount.incrementAndGet());

        // Create the AlerteLigneArret
        AlerteLigneArretDTO alerteLigneArretDTO = alerteLigneArretMapper.toDto(alerteLigneArret);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlerteLigneArretMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alerteLigneArretDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alerteLigneArretDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlerteLigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlerteLigneArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteLigneArret.setId(longCount.incrementAndGet());

        // Create the AlerteLigneArret
        AlerteLigneArretDTO alerteLigneArretDTO = alerteLigneArretMapper.toDto(alerteLigneArret);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteLigneArretMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alerteLigneArretDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlerteLigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlerteLigneArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteLigneArret.setId(longCount.incrementAndGet());

        // Create the AlerteLigneArret
        AlerteLigneArretDTO alerteLigneArretDTO = alerteLigneArretMapper.toDto(alerteLigneArret);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteLigneArretMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteLigneArretDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlerteLigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlerteLigneArretWithPatch() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerteLigneArret using partial update
        AlerteLigneArret partialUpdatedAlerteLigneArret = new AlerteLigneArret();
        partialUpdatedAlerteLigneArret.setId(alerteLigneArret.getId());

        partialUpdatedAlerteLigneArret.sens(UPDATED_SENS);

        restAlerteLigneArretMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlerteLigneArret.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlerteLigneArret))
            )
            .andExpect(status().isOk());

        // Validate the AlerteLigneArret in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlerteLigneArretUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlerteLigneArret, alerteLigneArret),
            getPersistedAlerteLigneArret(alerteLigneArret)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlerteLigneArretWithPatch() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerteLigneArret using partial update
        AlerteLigneArret partialUpdatedAlerteLigneArret = new AlerteLigneArret();
        partialUpdatedAlerteLigneArret.setId(alerteLigneArret.getId());

        partialUpdatedAlerteLigneArret.sens(UPDATED_SENS).actif(UPDATED_ACTIF);

        restAlerteLigneArretMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlerteLigneArret.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlerteLigneArret))
            )
            .andExpect(status().isOk());

        // Validate the AlerteLigneArret in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlerteLigneArretUpdatableFieldsEquals(
            partialUpdatedAlerteLigneArret,
            getPersistedAlerteLigneArret(partialUpdatedAlerteLigneArret)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAlerteLigneArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteLigneArret.setId(longCount.incrementAndGet());

        // Create the AlerteLigneArret
        AlerteLigneArretDTO alerteLigneArretDTO = alerteLigneArretMapper.toDto(alerteLigneArret);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlerteLigneArretMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alerteLigneArretDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alerteLigneArretDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlerteLigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlerteLigneArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteLigneArret.setId(longCount.incrementAndGet());

        // Create the AlerteLigneArret
        AlerteLigneArretDTO alerteLigneArretDTO = alerteLigneArretMapper.toDto(alerteLigneArret);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteLigneArretMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alerteLigneArretDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlerteLigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlerteLigneArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteLigneArret.setId(longCount.incrementAndGet());

        // Create the AlerteLigneArret
        AlerteLigneArretDTO alerteLigneArretDTO = alerteLigneArretMapper.toDto(alerteLigneArret);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteLigneArretMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alerteLigneArretDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlerteLigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlerteLigneArret() throws Exception {
        // Initialize the database
        insertedAlerteLigneArret = alerteLigneArretRepository.saveAndFlush(alerteLigneArret);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alerteLigneArret
        restAlerteLigneArretMockMvc
            .perform(delete(ENTITY_API_URL_ID, alerteLigneArret.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alerteLigneArretRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected AlerteLigneArret getPersistedAlerteLigneArret(AlerteLigneArret alerteLigneArret) {
        return alerteLigneArretRepository.findById(alerteLigneArret.getId()).orElseThrow();
    }

    protected void assertPersistedAlerteLigneArretToMatchAllProperties(AlerteLigneArret expectedAlerteLigneArret) {
        assertAlerteLigneArretAllPropertiesEquals(expectedAlerteLigneArret, getPersistedAlerteLigneArret(expectedAlerteLigneArret));
    }

    protected void assertPersistedAlerteLigneArretToMatchUpdatableProperties(AlerteLigneArret expectedAlerteLigneArret) {
        assertAlerteLigneArretAllUpdatablePropertiesEquals(
            expectedAlerteLigneArret,
            getPersistedAlerteLigneArret(expectedAlerteLigneArret)
        );
    }
}
