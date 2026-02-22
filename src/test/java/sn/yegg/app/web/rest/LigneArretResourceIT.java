package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.LigneArretAsserts.*;
import static sn.yegg.app.web.rest.TestUtil.createUpdateProxyForBean;
import static sn.yegg.app.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import sn.yegg.app.domain.Arret;
import sn.yegg.app.domain.Ligne;
import sn.yegg.app.domain.LigneArret;
import sn.yegg.app.repository.LigneArretRepository;
import sn.yegg.app.service.dto.LigneArretDTO;
import sn.yegg.app.service.mapper.LigneArretMapper;

/**
 * Integration tests for the {@link LigneArretResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LigneArretResourceIT {

    private static final Integer DEFAULT_ORDRE = 1;
    private static final Integer UPDATED_ORDRE = 2;
    private static final Integer SMALLER_ORDRE = 1 - 1;

    private static final Integer DEFAULT_TEMPS_TRAJET_DEPART = 1;
    private static final Integer UPDATED_TEMPS_TRAJET_DEPART = 2;
    private static final Integer SMALLER_TEMPS_TRAJET_DEPART = 1 - 1;

    private static final BigDecimal DEFAULT_DISTANCE_DEPART = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISTANCE_DEPART = new BigDecimal(2);
    private static final BigDecimal SMALLER_DISTANCE_DEPART = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/ligne-arrets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LigneArretRepository ligneArretRepository;

    @Autowired
    private LigneArretMapper ligneArretMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLigneArretMockMvc;

    private LigneArret ligneArret;

    private LigneArret insertedLigneArret;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneArret createEntity() {
        return new LigneArret().ordre(DEFAULT_ORDRE).tempsTrajetDepart(DEFAULT_TEMPS_TRAJET_DEPART).distanceDepart(DEFAULT_DISTANCE_DEPART);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LigneArret createUpdatedEntity() {
        return new LigneArret().ordre(UPDATED_ORDRE).tempsTrajetDepart(UPDATED_TEMPS_TRAJET_DEPART).distanceDepart(UPDATED_DISTANCE_DEPART);
    }

    @BeforeEach
    void initTest() {
        ligneArret = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLigneArret != null) {
            ligneArretRepository.delete(insertedLigneArret);
            insertedLigneArret = null;
        }
    }

    @Test
    @Transactional
    void createLigneArret() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LigneArret
        LigneArretDTO ligneArretDTO = ligneArretMapper.toDto(ligneArret);
        var returnedLigneArretDTO = om.readValue(
            restLigneArretMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneArretDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LigneArretDTO.class
        );

        // Validate the LigneArret in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLigneArret = ligneArretMapper.toEntity(returnedLigneArretDTO);
        assertLigneArretUpdatableFieldsEquals(returnedLigneArret, getPersistedLigneArret(returnedLigneArret));

        insertedLigneArret = returnedLigneArret;
    }

    @Test
    @Transactional
    void createLigneArretWithExistingId() throws Exception {
        // Create the LigneArret with an existing ID
        ligneArret.setId(1L);
        LigneArretDTO ligneArretDTO = ligneArretMapper.toDto(ligneArret);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneArretMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneArretDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrdreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ligneArret.setOrdre(null);

        // Create the LigneArret, which fails.
        LigneArretDTO ligneArretDTO = ligneArretMapper.toDto(ligneArret);

        restLigneArretMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneArretDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLigneArrets() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList
        restLigneArretMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneArret.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].tempsTrajetDepart").value(hasItem(DEFAULT_TEMPS_TRAJET_DEPART)))
            .andExpect(jsonPath("$.[*].distanceDepart").value(hasItem(sameNumber(DEFAULT_DISTANCE_DEPART))));
    }

    @Test
    @Transactional
    void getLigneArret() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get the ligneArret
        restLigneArretMockMvc
            .perform(get(ENTITY_API_URL_ID, ligneArret.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ligneArret.getId().intValue()))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.tempsTrajetDepart").value(DEFAULT_TEMPS_TRAJET_DEPART))
            .andExpect(jsonPath("$.distanceDepart").value(sameNumber(DEFAULT_DISTANCE_DEPART)));
    }

    @Test
    @Transactional
    void getLigneArretsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        Long id = ligneArret.getId();

        defaultLigneArretFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLigneArretFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLigneArretFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLigneArretsByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where ordre equals to
        defaultLigneArretFiltering("ordre.equals=" + DEFAULT_ORDRE, "ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllLigneArretsByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where ordre in
        defaultLigneArretFiltering("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE, "ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllLigneArretsByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where ordre is not null
        defaultLigneArretFiltering("ordre.specified=true", "ordre.specified=false");
    }

    @Test
    @Transactional
    void getAllLigneArretsByOrdreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where ordre is greater than or equal to
        defaultLigneArretFiltering("ordre.greaterThanOrEqual=" + DEFAULT_ORDRE, "ordre.greaterThanOrEqual=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllLigneArretsByOrdreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where ordre is less than or equal to
        defaultLigneArretFiltering("ordre.lessThanOrEqual=" + DEFAULT_ORDRE, "ordre.lessThanOrEqual=" + SMALLER_ORDRE);
    }

    @Test
    @Transactional
    void getAllLigneArretsByOrdreIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where ordre is less than
        defaultLigneArretFiltering("ordre.lessThan=" + UPDATED_ORDRE, "ordre.lessThan=" + DEFAULT_ORDRE);
    }

    @Test
    @Transactional
    void getAllLigneArretsByOrdreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where ordre is greater than
        defaultLigneArretFiltering("ordre.greaterThan=" + SMALLER_ORDRE, "ordre.greaterThan=" + DEFAULT_ORDRE);
    }

    @Test
    @Transactional
    void getAllLigneArretsByTempsTrajetDepartIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where tempsTrajetDepart equals to
        defaultLigneArretFiltering(
            "tempsTrajetDepart.equals=" + DEFAULT_TEMPS_TRAJET_DEPART,
            "tempsTrajetDepart.equals=" + UPDATED_TEMPS_TRAJET_DEPART
        );
    }

    @Test
    @Transactional
    void getAllLigneArretsByTempsTrajetDepartIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where tempsTrajetDepart in
        defaultLigneArretFiltering(
            "tempsTrajetDepart.in=" + DEFAULT_TEMPS_TRAJET_DEPART + "," + UPDATED_TEMPS_TRAJET_DEPART,
            "tempsTrajetDepart.in=" + UPDATED_TEMPS_TRAJET_DEPART
        );
    }

    @Test
    @Transactional
    void getAllLigneArretsByTempsTrajetDepartIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where tempsTrajetDepart is not null
        defaultLigneArretFiltering("tempsTrajetDepart.specified=true", "tempsTrajetDepart.specified=false");
    }

    @Test
    @Transactional
    void getAllLigneArretsByTempsTrajetDepartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where tempsTrajetDepart is greater than or equal to
        defaultLigneArretFiltering(
            "tempsTrajetDepart.greaterThanOrEqual=" + DEFAULT_TEMPS_TRAJET_DEPART,
            "tempsTrajetDepart.greaterThanOrEqual=" + UPDATED_TEMPS_TRAJET_DEPART
        );
    }

    @Test
    @Transactional
    void getAllLigneArretsByTempsTrajetDepartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where tempsTrajetDepart is less than or equal to
        defaultLigneArretFiltering(
            "tempsTrajetDepart.lessThanOrEqual=" + DEFAULT_TEMPS_TRAJET_DEPART,
            "tempsTrajetDepart.lessThanOrEqual=" + SMALLER_TEMPS_TRAJET_DEPART
        );
    }

    @Test
    @Transactional
    void getAllLigneArretsByTempsTrajetDepartIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where tempsTrajetDepart is less than
        defaultLigneArretFiltering(
            "tempsTrajetDepart.lessThan=" + UPDATED_TEMPS_TRAJET_DEPART,
            "tempsTrajetDepart.lessThan=" + DEFAULT_TEMPS_TRAJET_DEPART
        );
    }

    @Test
    @Transactional
    void getAllLigneArretsByTempsTrajetDepartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where tempsTrajetDepart is greater than
        defaultLigneArretFiltering(
            "tempsTrajetDepart.greaterThan=" + SMALLER_TEMPS_TRAJET_DEPART,
            "tempsTrajetDepart.greaterThan=" + DEFAULT_TEMPS_TRAJET_DEPART
        );
    }

    @Test
    @Transactional
    void getAllLigneArretsByDistanceDepartIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where distanceDepart equals to
        defaultLigneArretFiltering("distanceDepart.equals=" + DEFAULT_DISTANCE_DEPART, "distanceDepart.equals=" + UPDATED_DISTANCE_DEPART);
    }

    @Test
    @Transactional
    void getAllLigneArretsByDistanceDepartIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where distanceDepart in
        defaultLigneArretFiltering(
            "distanceDepart.in=" + DEFAULT_DISTANCE_DEPART + "," + UPDATED_DISTANCE_DEPART,
            "distanceDepart.in=" + UPDATED_DISTANCE_DEPART
        );
    }

    @Test
    @Transactional
    void getAllLigneArretsByDistanceDepartIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where distanceDepart is not null
        defaultLigneArretFiltering("distanceDepart.specified=true", "distanceDepart.specified=false");
    }

    @Test
    @Transactional
    void getAllLigneArretsByDistanceDepartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where distanceDepart is greater than or equal to
        defaultLigneArretFiltering(
            "distanceDepart.greaterThanOrEqual=" + DEFAULT_DISTANCE_DEPART,
            "distanceDepart.greaterThanOrEqual=" + UPDATED_DISTANCE_DEPART
        );
    }

    @Test
    @Transactional
    void getAllLigneArretsByDistanceDepartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where distanceDepart is less than or equal to
        defaultLigneArretFiltering(
            "distanceDepart.lessThanOrEqual=" + DEFAULT_DISTANCE_DEPART,
            "distanceDepart.lessThanOrEqual=" + SMALLER_DISTANCE_DEPART
        );
    }

    @Test
    @Transactional
    void getAllLigneArretsByDistanceDepartIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where distanceDepart is less than
        defaultLigneArretFiltering(
            "distanceDepart.lessThan=" + UPDATED_DISTANCE_DEPART,
            "distanceDepart.lessThan=" + DEFAULT_DISTANCE_DEPART
        );
    }

    @Test
    @Transactional
    void getAllLigneArretsByDistanceDepartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        // Get all the ligneArretList where distanceDepart is greater than
        defaultLigneArretFiltering(
            "distanceDepart.greaterThan=" + SMALLER_DISTANCE_DEPART,
            "distanceDepart.greaterThan=" + DEFAULT_DISTANCE_DEPART
        );
    }

    @Test
    @Transactional
    void getAllLigneArretsByLigneIsEqualToSomething() throws Exception {
        Ligne ligne;
        if (TestUtil.findAll(em, Ligne.class).isEmpty()) {
            ligneArretRepository.saveAndFlush(ligneArret);
            ligne = LigneResourceIT.createEntity();
        } else {
            ligne = TestUtil.findAll(em, Ligne.class).get(0);
        }
        em.persist(ligne);
        em.flush();
        ligneArret.setLigne(ligne);
        ligneArretRepository.saveAndFlush(ligneArret);
        Long ligneId = ligne.getId();
        // Get all the ligneArretList where ligne equals to ligneId
        defaultLigneArretShouldBeFound("ligneId.equals=" + ligneId);

        // Get all the ligneArretList where ligne equals to (ligneId + 1)
        defaultLigneArretShouldNotBeFound("ligneId.equals=" + (ligneId + 1));
    }

    @Test
    @Transactional
    void getAllLigneArretsByArretIsEqualToSomething() throws Exception {
        Arret arret;
        if (TestUtil.findAll(em, Arret.class).isEmpty()) {
            ligneArretRepository.saveAndFlush(ligneArret);
            arret = ArretResourceIT.createEntity();
        } else {
            arret = TestUtil.findAll(em, Arret.class).get(0);
        }
        em.persist(arret);
        em.flush();
        ligneArret.setArret(arret);
        ligneArretRepository.saveAndFlush(ligneArret);
        Long arretId = arret.getId();
        // Get all the ligneArretList where arret equals to arretId
        defaultLigneArretShouldBeFound("arretId.equals=" + arretId);

        // Get all the ligneArretList where arret equals to (arretId + 1)
        defaultLigneArretShouldNotBeFound("arretId.equals=" + (arretId + 1));
    }

    private void defaultLigneArretFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLigneArretShouldBeFound(shouldBeFound);
        defaultLigneArretShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLigneArretShouldBeFound(String filter) throws Exception {
        restLigneArretMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligneArret.getId().intValue())))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].tempsTrajetDepart").value(hasItem(DEFAULT_TEMPS_TRAJET_DEPART)))
            .andExpect(jsonPath("$.[*].distanceDepart").value(hasItem(sameNumber(DEFAULT_DISTANCE_DEPART))));

        // Check, that the count call also returns 1
        restLigneArretMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLigneArretShouldNotBeFound(String filter) throws Exception {
        restLigneArretMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLigneArretMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLigneArret() throws Exception {
        // Get the ligneArret
        restLigneArretMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLigneArret() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ligneArret
        LigneArret updatedLigneArret = ligneArretRepository.findById(ligneArret.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLigneArret are not directly saved in db
        em.detach(updatedLigneArret);
        updatedLigneArret.ordre(UPDATED_ORDRE).tempsTrajetDepart(UPDATED_TEMPS_TRAJET_DEPART).distanceDepart(UPDATED_DISTANCE_DEPART);
        LigneArretDTO ligneArretDTO = ligneArretMapper.toDto(updatedLigneArret);

        restLigneArretMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligneArretDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ligneArretDTO))
            )
            .andExpect(status().isOk());

        // Validate the LigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLigneArretToMatchAllProperties(updatedLigneArret);
    }

    @Test
    @Transactional
    void putNonExistingLigneArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligneArret.setId(longCount.incrementAndGet());

        // Create the LigneArret
        LigneArretDTO ligneArretDTO = ligneArretMapper.toDto(ligneArret);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneArretMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligneArretDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ligneArretDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLigneArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligneArret.setId(longCount.incrementAndGet());

        // Create the LigneArret
        LigneArretDTO ligneArretDTO = ligneArretMapper.toDto(ligneArret);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneArretMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ligneArretDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLigneArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligneArret.setId(longCount.incrementAndGet());

        // Create the LigneArret
        LigneArretDTO ligneArretDTO = ligneArretMapper.toDto(ligneArret);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneArretMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneArretDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLigneArretWithPatch() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ligneArret using partial update
        LigneArret partialUpdatedLigneArret = new LigneArret();
        partialUpdatedLigneArret.setId(ligneArret.getId());

        partialUpdatedLigneArret.ordre(UPDATED_ORDRE);

        restLigneArretMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigneArret.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLigneArret))
            )
            .andExpect(status().isOk());

        // Validate the LigneArret in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLigneArretUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLigneArret, ligneArret),
            getPersistedLigneArret(ligneArret)
        );
    }

    @Test
    @Transactional
    void fullUpdateLigneArretWithPatch() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ligneArret using partial update
        LigneArret partialUpdatedLigneArret = new LigneArret();
        partialUpdatedLigneArret.setId(ligneArret.getId());

        partialUpdatedLigneArret
            .ordre(UPDATED_ORDRE)
            .tempsTrajetDepart(UPDATED_TEMPS_TRAJET_DEPART)
            .distanceDepart(UPDATED_DISTANCE_DEPART);

        restLigneArretMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigneArret.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLigneArret))
            )
            .andExpect(status().isOk());

        // Validate the LigneArret in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLigneArretUpdatableFieldsEquals(partialUpdatedLigneArret, getPersistedLigneArret(partialUpdatedLigneArret));
    }

    @Test
    @Transactional
    void patchNonExistingLigneArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligneArret.setId(longCount.incrementAndGet());

        // Create the LigneArret
        LigneArretDTO ligneArretDTO = ligneArretMapper.toDto(ligneArret);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneArretMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ligneArretDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ligneArretDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLigneArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligneArret.setId(longCount.incrementAndGet());

        // Create the LigneArret
        LigneArretDTO ligneArretDTO = ligneArretMapper.toDto(ligneArret);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneArretMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ligneArretDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLigneArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligneArret.setId(longCount.incrementAndGet());

        // Create the LigneArret
        LigneArretDTO ligneArretDTO = ligneArretMapper.toDto(ligneArret);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneArretMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ligneArretDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LigneArret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLigneArret() throws Exception {
        // Initialize the database
        insertedLigneArret = ligneArretRepository.saveAndFlush(ligneArret);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ligneArret
        restLigneArretMockMvc
            .perform(delete(ENTITY_API_URL_ID, ligneArret.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ligneArretRepository.count();
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

    protected LigneArret getPersistedLigneArret(LigneArret ligneArret) {
        return ligneArretRepository.findById(ligneArret.getId()).orElseThrow();
    }

    protected void assertPersistedLigneArretToMatchAllProperties(LigneArret expectedLigneArret) {
        assertLigneArretAllPropertiesEquals(expectedLigneArret, getPersistedLigneArret(expectedLigneArret));
    }

    protected void assertPersistedLigneArretToMatchUpdatableProperties(LigneArret expectedLigneArret) {
        assertLigneArretAllUpdatablePropertiesEquals(expectedLigneArret, getPersistedLigneArret(expectedLigneArret));
    }
}
