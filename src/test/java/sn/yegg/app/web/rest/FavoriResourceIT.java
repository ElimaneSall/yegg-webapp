package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.FavoriAsserts.*;
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
import sn.yegg.app.domain.Favori;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.repository.FavoriRepository;
import sn.yegg.app.service.dto.FavoriDTO;
import sn.yegg.app.service.mapper.FavoriMapper;

/**
 * Integration tests for the {@link FavoriResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FavoriResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_CIBLE_ID = 1L;
    private static final Long UPDATED_CIBLE_ID = 2L;
    private static final Long SMALLER_CIBLE_ID = 1L - 1L;

    private static final String DEFAULT_NOM_PERSONNALISE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PERSONNALISE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE = 1;
    private static final Integer UPDATED_ORDRE = 2;
    private static final Integer SMALLER_ORDRE = 1 - 1;

    private static final Boolean DEFAULT_ALERTE_ACTIVE = false;
    private static final Boolean UPDATED_ALERTE_ACTIVE = true;

    private static final Integer DEFAULT_ALERTE_SEUIL = 1;
    private static final Integer UPDATED_ALERTE_SEUIL = 2;
    private static final Integer SMALLER_ALERTE_SEUIL = 1 - 1;

    private static final String ENTITY_API_URL = "/api/favoris";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FavoriRepository favoriRepository;

    @Autowired
    private FavoriMapper favoriMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFavoriMockMvc;

    private Favori favori;

    private Favori insertedFavori;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Favori createEntity() {
        return new Favori()
            .type(DEFAULT_TYPE)
            .cibleId(DEFAULT_CIBLE_ID)
            .nomPersonnalise(DEFAULT_NOM_PERSONNALISE)
            .ordre(DEFAULT_ORDRE)
            .alerteActive(DEFAULT_ALERTE_ACTIVE)
            .alerteSeuil(DEFAULT_ALERTE_SEUIL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Favori createUpdatedEntity() {
        return new Favori()
            .type(UPDATED_TYPE)
            .cibleId(UPDATED_CIBLE_ID)
            .nomPersonnalise(UPDATED_NOM_PERSONNALISE)
            .ordre(UPDATED_ORDRE)
            .alerteActive(UPDATED_ALERTE_ACTIVE)
            .alerteSeuil(UPDATED_ALERTE_SEUIL);
    }

    @BeforeEach
    void initTest() {
        favori = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFavori != null) {
            favoriRepository.delete(insertedFavori);
            insertedFavori = null;
        }
    }

    @Test
    @Transactional
    void createFavori() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Favori
        FavoriDTO favoriDTO = favoriMapper.toDto(favori);
        var returnedFavoriDTO = om.readValue(
            restFavoriMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(favoriDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FavoriDTO.class
        );

        // Validate the Favori in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFavori = favoriMapper.toEntity(returnedFavoriDTO);
        assertFavoriUpdatableFieldsEquals(returnedFavori, getPersistedFavori(returnedFavori));

        insertedFavori = returnedFavori;
    }

    @Test
    @Transactional
    void createFavoriWithExistingId() throws Exception {
        // Create the Favori with an existing ID
        favori.setId(1L);
        FavoriDTO favoriDTO = favoriMapper.toDto(favori);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavoriMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(favoriDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Favori in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        favori.setType(null);

        // Create the Favori, which fails.
        FavoriDTO favoriDTO = favoriMapper.toDto(favori);

        restFavoriMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(favoriDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCibleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        favori.setCibleId(null);

        // Create the Favori, which fails.
        FavoriDTO favoriDTO = favoriMapper.toDto(favori);

        restFavoriMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(favoriDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFavoris() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList
        restFavoriMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favori.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].cibleId").value(hasItem(DEFAULT_CIBLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].nomPersonnalise").value(hasItem(DEFAULT_NOM_PERSONNALISE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].alerteActive").value(hasItem(DEFAULT_ALERTE_ACTIVE)))
            .andExpect(jsonPath("$.[*].alerteSeuil").value(hasItem(DEFAULT_ALERTE_SEUIL)));
    }

    @Test
    @Transactional
    void getFavori() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get the favori
        restFavoriMockMvc
            .perform(get(ENTITY_API_URL_ID, favori.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(favori.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.cibleId").value(DEFAULT_CIBLE_ID.intValue()))
            .andExpect(jsonPath("$.nomPersonnalise").value(DEFAULT_NOM_PERSONNALISE))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.alerteActive").value(DEFAULT_ALERTE_ACTIVE))
            .andExpect(jsonPath("$.alerteSeuil").value(DEFAULT_ALERTE_SEUIL));
    }

    @Test
    @Transactional
    void getFavorisByIdFiltering() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        Long id = favori.getId();

        defaultFavoriFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFavoriFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFavoriFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFavorisByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where type equals to
        defaultFavoriFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFavorisByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where type in
        defaultFavoriFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFavorisByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where type is not null
        defaultFavoriFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllFavorisByTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where type contains
        defaultFavoriFiltering("type.contains=" + DEFAULT_TYPE, "type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllFavorisByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where type does not contain
        defaultFavoriFiltering("type.doesNotContain=" + UPDATED_TYPE, "type.doesNotContain=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllFavorisByCibleIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where cibleId equals to
        defaultFavoriFiltering("cibleId.equals=" + DEFAULT_CIBLE_ID, "cibleId.equals=" + UPDATED_CIBLE_ID);
    }

    @Test
    @Transactional
    void getAllFavorisByCibleIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where cibleId in
        defaultFavoriFiltering("cibleId.in=" + DEFAULT_CIBLE_ID + "," + UPDATED_CIBLE_ID, "cibleId.in=" + UPDATED_CIBLE_ID);
    }

    @Test
    @Transactional
    void getAllFavorisByCibleIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where cibleId is not null
        defaultFavoriFiltering("cibleId.specified=true", "cibleId.specified=false");
    }

    @Test
    @Transactional
    void getAllFavorisByCibleIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where cibleId is greater than or equal to
        defaultFavoriFiltering("cibleId.greaterThanOrEqual=" + DEFAULT_CIBLE_ID, "cibleId.greaterThanOrEqual=" + UPDATED_CIBLE_ID);
    }

    @Test
    @Transactional
    void getAllFavorisByCibleIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where cibleId is less than or equal to
        defaultFavoriFiltering("cibleId.lessThanOrEqual=" + DEFAULT_CIBLE_ID, "cibleId.lessThanOrEqual=" + SMALLER_CIBLE_ID);
    }

    @Test
    @Transactional
    void getAllFavorisByCibleIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where cibleId is less than
        defaultFavoriFiltering("cibleId.lessThan=" + UPDATED_CIBLE_ID, "cibleId.lessThan=" + DEFAULT_CIBLE_ID);
    }

    @Test
    @Transactional
    void getAllFavorisByCibleIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where cibleId is greater than
        defaultFavoriFiltering("cibleId.greaterThan=" + SMALLER_CIBLE_ID, "cibleId.greaterThan=" + DEFAULT_CIBLE_ID);
    }

    @Test
    @Transactional
    void getAllFavorisByNomPersonnaliseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where nomPersonnalise equals to
        defaultFavoriFiltering("nomPersonnalise.equals=" + DEFAULT_NOM_PERSONNALISE, "nomPersonnalise.equals=" + UPDATED_NOM_PERSONNALISE);
    }

    @Test
    @Transactional
    void getAllFavorisByNomPersonnaliseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where nomPersonnalise in
        defaultFavoriFiltering(
            "nomPersonnalise.in=" + DEFAULT_NOM_PERSONNALISE + "," + UPDATED_NOM_PERSONNALISE,
            "nomPersonnalise.in=" + UPDATED_NOM_PERSONNALISE
        );
    }

    @Test
    @Transactional
    void getAllFavorisByNomPersonnaliseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where nomPersonnalise is not null
        defaultFavoriFiltering("nomPersonnalise.specified=true", "nomPersonnalise.specified=false");
    }

    @Test
    @Transactional
    void getAllFavorisByNomPersonnaliseContainsSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where nomPersonnalise contains
        defaultFavoriFiltering(
            "nomPersonnalise.contains=" + DEFAULT_NOM_PERSONNALISE,
            "nomPersonnalise.contains=" + UPDATED_NOM_PERSONNALISE
        );
    }

    @Test
    @Transactional
    void getAllFavorisByNomPersonnaliseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where nomPersonnalise does not contain
        defaultFavoriFiltering(
            "nomPersonnalise.doesNotContain=" + UPDATED_NOM_PERSONNALISE,
            "nomPersonnalise.doesNotContain=" + DEFAULT_NOM_PERSONNALISE
        );
    }

    @Test
    @Transactional
    void getAllFavorisByOrdreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where ordre equals to
        defaultFavoriFiltering("ordre.equals=" + DEFAULT_ORDRE, "ordre.equals=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllFavorisByOrdreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where ordre in
        defaultFavoriFiltering("ordre.in=" + DEFAULT_ORDRE + "," + UPDATED_ORDRE, "ordre.in=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllFavorisByOrdreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where ordre is not null
        defaultFavoriFiltering("ordre.specified=true", "ordre.specified=false");
    }

    @Test
    @Transactional
    void getAllFavorisByOrdreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where ordre is greater than or equal to
        defaultFavoriFiltering("ordre.greaterThanOrEqual=" + DEFAULT_ORDRE, "ordre.greaterThanOrEqual=" + UPDATED_ORDRE);
    }

    @Test
    @Transactional
    void getAllFavorisByOrdreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where ordre is less than or equal to
        defaultFavoriFiltering("ordre.lessThanOrEqual=" + DEFAULT_ORDRE, "ordre.lessThanOrEqual=" + SMALLER_ORDRE);
    }

    @Test
    @Transactional
    void getAllFavorisByOrdreIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where ordre is less than
        defaultFavoriFiltering("ordre.lessThan=" + UPDATED_ORDRE, "ordre.lessThan=" + DEFAULT_ORDRE);
    }

    @Test
    @Transactional
    void getAllFavorisByOrdreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where ordre is greater than
        defaultFavoriFiltering("ordre.greaterThan=" + SMALLER_ORDRE, "ordre.greaterThan=" + DEFAULT_ORDRE);
    }

    @Test
    @Transactional
    void getAllFavorisByAlerteActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where alerteActive equals to
        defaultFavoriFiltering("alerteActive.equals=" + DEFAULT_ALERTE_ACTIVE, "alerteActive.equals=" + UPDATED_ALERTE_ACTIVE);
    }

    @Test
    @Transactional
    void getAllFavorisByAlerteActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where alerteActive in
        defaultFavoriFiltering(
            "alerteActive.in=" + DEFAULT_ALERTE_ACTIVE + "," + UPDATED_ALERTE_ACTIVE,
            "alerteActive.in=" + UPDATED_ALERTE_ACTIVE
        );
    }

    @Test
    @Transactional
    void getAllFavorisByAlerteActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where alerteActive is not null
        defaultFavoriFiltering("alerteActive.specified=true", "alerteActive.specified=false");
    }

    @Test
    @Transactional
    void getAllFavorisByAlerteSeuilIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where alerteSeuil equals to
        defaultFavoriFiltering("alerteSeuil.equals=" + DEFAULT_ALERTE_SEUIL, "alerteSeuil.equals=" + UPDATED_ALERTE_SEUIL);
    }

    @Test
    @Transactional
    void getAllFavorisByAlerteSeuilIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where alerteSeuil in
        defaultFavoriFiltering(
            "alerteSeuil.in=" + DEFAULT_ALERTE_SEUIL + "," + UPDATED_ALERTE_SEUIL,
            "alerteSeuil.in=" + UPDATED_ALERTE_SEUIL
        );
    }

    @Test
    @Transactional
    void getAllFavorisByAlerteSeuilIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where alerteSeuil is not null
        defaultFavoriFiltering("alerteSeuil.specified=true", "alerteSeuil.specified=false");
    }

    @Test
    @Transactional
    void getAllFavorisByAlerteSeuilIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where alerteSeuil is greater than or equal to
        defaultFavoriFiltering(
            "alerteSeuil.greaterThanOrEqual=" + DEFAULT_ALERTE_SEUIL,
            "alerteSeuil.greaterThanOrEqual=" + (DEFAULT_ALERTE_SEUIL + 1)
        );
    }

    @Test
    @Transactional
    void getAllFavorisByAlerteSeuilIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where alerteSeuil is less than or equal to
        defaultFavoriFiltering(
            "alerteSeuil.lessThanOrEqual=" + DEFAULT_ALERTE_SEUIL,
            "alerteSeuil.lessThanOrEqual=" + SMALLER_ALERTE_SEUIL
        );
    }

    @Test
    @Transactional
    void getAllFavorisByAlerteSeuilIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where alerteSeuil is less than
        defaultFavoriFiltering("alerteSeuil.lessThan=" + (DEFAULT_ALERTE_SEUIL + 1), "alerteSeuil.lessThan=" + DEFAULT_ALERTE_SEUIL);
    }

    @Test
    @Transactional
    void getAllFavorisByAlerteSeuilIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where alerteSeuil is greater than
        defaultFavoriFiltering("alerteSeuil.greaterThan=" + SMALLER_ALERTE_SEUIL, "alerteSeuil.greaterThan=" + DEFAULT_ALERTE_SEUIL);
    }

    @Test
    @Transactional
    void getAllFavorisByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            favoriRepository.saveAndFlush(favori);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        favori.setUtilisateur(utilisateur);
        favoriRepository.saveAndFlush(favori);
        Long utilisateurId = utilisateur.getId();
        // Get all the favoriList where utilisateur equals to utilisateurId
        defaultFavoriShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the favoriList where utilisateur equals to (utilisateurId + 1)
        defaultFavoriShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultFavoriFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFavoriShouldBeFound(shouldBeFound);
        defaultFavoriShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFavoriShouldBeFound(String filter) throws Exception {
        restFavoriMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favori.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].cibleId").value(hasItem(DEFAULT_CIBLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].nomPersonnalise").value(hasItem(DEFAULT_NOM_PERSONNALISE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].alerteActive").value(hasItem(DEFAULT_ALERTE_ACTIVE)))
            .andExpect(jsonPath("$.[*].alerteSeuil").value(hasItem(DEFAULT_ALERTE_SEUIL)));

        // Check, that the count call also returns 1
        restFavoriMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFavoriShouldNotBeFound(String filter) throws Exception {
        restFavoriMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFavoriMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFavori() throws Exception {
        // Get the favori
        restFavoriMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFavori() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the favori
        Favori updatedFavori = favoriRepository.findById(favori.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFavori are not directly saved in db
        em.detach(updatedFavori);
        updatedFavori
            .type(UPDATED_TYPE)
            .cibleId(UPDATED_CIBLE_ID)
            .nomPersonnalise(UPDATED_NOM_PERSONNALISE)
            .ordre(UPDATED_ORDRE)
            .alerteActive(UPDATED_ALERTE_ACTIVE)
            .alerteSeuil(UPDATED_ALERTE_SEUIL);
        FavoriDTO favoriDTO = favoriMapper.toDto(updatedFavori);

        restFavoriMockMvc
            .perform(
                put(ENTITY_API_URL_ID, favoriDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(favoriDTO))
            )
            .andExpect(status().isOk());

        // Validate the Favori in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFavoriToMatchAllProperties(updatedFavori);
    }

    @Test
    @Transactional
    void putNonExistingFavori() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favori.setId(longCount.incrementAndGet());

        // Create the Favori
        FavoriDTO favoriDTO = favoriMapper.toDto(favori);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoriMockMvc
            .perform(
                put(ENTITY_API_URL_ID, favoriDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(favoriDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favori in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFavori() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favori.setId(longCount.incrementAndGet());

        // Create the Favori
        FavoriDTO favoriDTO = favoriMapper.toDto(favori);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoriMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(favoriDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favori in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFavori() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favori.setId(longCount.incrementAndGet());

        // Create the Favori
        FavoriDTO favoriDTO = favoriMapper.toDto(favori);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoriMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(favoriDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Favori in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFavoriWithPatch() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the favori using partial update
        Favori partialUpdatedFavori = new Favori();
        partialUpdatedFavori.setId(favori.getId());

        partialUpdatedFavori.type(UPDATED_TYPE).nomPersonnalise(UPDATED_NOM_PERSONNALISE);

        restFavoriMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFavori.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFavori))
            )
            .andExpect(status().isOk());

        // Validate the Favori in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFavoriUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFavori, favori), getPersistedFavori(favori));
    }

    @Test
    @Transactional
    void fullUpdateFavoriWithPatch() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the favori using partial update
        Favori partialUpdatedFavori = new Favori();
        partialUpdatedFavori.setId(favori.getId());

        partialUpdatedFavori
            .type(UPDATED_TYPE)
            .cibleId(UPDATED_CIBLE_ID)
            .nomPersonnalise(UPDATED_NOM_PERSONNALISE)
            .ordre(UPDATED_ORDRE)
            .alerteActive(UPDATED_ALERTE_ACTIVE)
            .alerteSeuil(UPDATED_ALERTE_SEUIL);

        restFavoriMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFavori.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFavori))
            )
            .andExpect(status().isOk());

        // Validate the Favori in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFavoriUpdatableFieldsEquals(partialUpdatedFavori, getPersistedFavori(partialUpdatedFavori));
    }

    @Test
    @Transactional
    void patchNonExistingFavori() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favori.setId(longCount.incrementAndGet());

        // Create the Favori
        FavoriDTO favoriDTO = favoriMapper.toDto(favori);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoriMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, favoriDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(favoriDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favori in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFavori() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favori.setId(longCount.incrementAndGet());

        // Create the Favori
        FavoriDTO favoriDTO = favoriMapper.toDto(favori);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoriMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(favoriDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favori in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFavori() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        favori.setId(longCount.incrementAndGet());

        // Create the Favori
        FavoriDTO favoriDTO = favoriMapper.toDto(favori);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoriMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(favoriDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Favori in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFavori() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the favori
        restFavoriMockMvc
            .perform(delete(ENTITY_API_URL_ID, favori.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return favoriRepository.count();
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

    protected Favori getPersistedFavori(Favori favori) {
        return favoriRepository.findById(favori.getId()).orElseThrow();
    }

    protected void assertPersistedFavoriToMatchAllProperties(Favori expectedFavori) {
        assertFavoriAllPropertiesEquals(expectedFavori, getPersistedFavori(expectedFavori));
    }

    protected void assertPersistedFavoriToMatchUpdatableProperties(Favori expectedFavori) {
        assertFavoriAllUpdatablePropertiesEquals(expectedFavori, getPersistedFavori(expectedFavori));
    }
}
