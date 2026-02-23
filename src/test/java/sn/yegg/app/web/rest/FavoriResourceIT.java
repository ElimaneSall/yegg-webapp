package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.FavoriAsserts.*;
import static sn.yegg.app.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import sn.yegg.app.domain.enumeration.FavoriteType;
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

    private static final FavoriteType DEFAULT_TYPE = FavoriteType.LINE;
    private static final FavoriteType UPDATED_TYPE = FavoriteType.STOP;

    private static final Long DEFAULT_CIBLE_ID = 1L;
    private static final Long UPDATED_CIBLE_ID = 2L;
    private static final Long SMALLER_CIBLE_ID = 1L - 1L;

    private static final String DEFAULT_NOM_PERSONNALISE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PERSONNALISE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE = 1;
    private static final Integer UPDATED_ORDRE = 2;
    private static final Integer SMALLER_ORDRE = 1 - 1;

    private static final Instant DEFAULT_DATE_AJOUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_AJOUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DERNIER_ACCES = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DERNIER_ACCES = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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
            .dateAjout(DEFAULT_DATE_AJOUT)
            .dernierAcces(DEFAULT_DERNIER_ACCES);
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
            .dateAjout(UPDATED_DATE_AJOUT)
            .dernierAcces(UPDATED_DERNIER_ACCES);
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
    void checkDateAjoutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        favori.setDateAjout(null);

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
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cibleId").value(hasItem(DEFAULT_CIBLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].nomPersonnalise").value(hasItem(DEFAULT_NOM_PERSONNALISE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].dateAjout").value(hasItem(DEFAULT_DATE_AJOUT.toString())))
            .andExpect(jsonPath("$.[*].dernierAcces").value(hasItem(DEFAULT_DERNIER_ACCES.toString())));
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
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.cibleId").value(DEFAULT_CIBLE_ID.intValue()))
            .andExpect(jsonPath("$.nomPersonnalise").value(DEFAULT_NOM_PERSONNALISE))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.dateAjout").value(DEFAULT_DATE_AJOUT.toString()))
            .andExpect(jsonPath("$.dernierAcces").value(DEFAULT_DERNIER_ACCES.toString()));
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
    void getAllFavorisByDateAjoutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where dateAjout equals to
        defaultFavoriFiltering("dateAjout.equals=" + DEFAULT_DATE_AJOUT, "dateAjout.equals=" + UPDATED_DATE_AJOUT);
    }

    @Test
    @Transactional
    void getAllFavorisByDateAjoutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where dateAjout in
        defaultFavoriFiltering("dateAjout.in=" + DEFAULT_DATE_AJOUT + "," + UPDATED_DATE_AJOUT, "dateAjout.in=" + UPDATED_DATE_AJOUT);
    }

    @Test
    @Transactional
    void getAllFavorisByDateAjoutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where dateAjout is not null
        defaultFavoriFiltering("dateAjout.specified=true", "dateAjout.specified=false");
    }

    @Test
    @Transactional
    void getAllFavorisByDernierAccesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where dernierAcces equals to
        defaultFavoriFiltering("dernierAcces.equals=" + DEFAULT_DERNIER_ACCES, "dernierAcces.equals=" + UPDATED_DERNIER_ACCES);
    }

    @Test
    @Transactional
    void getAllFavorisByDernierAccesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where dernierAcces in
        defaultFavoriFiltering(
            "dernierAcces.in=" + DEFAULT_DERNIER_ACCES + "," + UPDATED_DERNIER_ACCES,
            "dernierAcces.in=" + UPDATED_DERNIER_ACCES
        );
    }

    @Test
    @Transactional
    void getAllFavorisByDernierAccesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFavori = favoriRepository.saveAndFlush(favori);

        // Get all the favoriList where dernierAcces is not null
        defaultFavoriFiltering("dernierAcces.specified=true", "dernierAcces.specified=false");
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
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cibleId").value(hasItem(DEFAULT_CIBLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].nomPersonnalise").value(hasItem(DEFAULT_NOM_PERSONNALISE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].dateAjout").value(hasItem(DEFAULT_DATE_AJOUT.toString())))
            .andExpect(jsonPath("$.[*].dernierAcces").value(hasItem(DEFAULT_DERNIER_ACCES.toString())));

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
            .dateAjout(UPDATED_DATE_AJOUT)
            .dernierAcces(UPDATED_DERNIER_ACCES);
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

        partialUpdatedFavori.cibleId(UPDATED_CIBLE_ID).nomPersonnalise(UPDATED_NOM_PERSONNALISE).dateAjout(UPDATED_DATE_AJOUT);

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
            .dateAjout(UPDATED_DATE_AJOUT)
            .dernierAcces(UPDATED_DERNIER_ACCES);

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
