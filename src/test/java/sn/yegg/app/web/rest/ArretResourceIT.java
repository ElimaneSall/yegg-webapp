package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.ArretAsserts.*;
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
import sn.yegg.app.repository.ArretRepository;
import sn.yegg.app.service.dto.ArretDTO;
import sn.yegg.app.service.mapper.ArretMapper;

/**
 * Integration tests for the {@link ArretResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArretResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(-90);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(-89);
    private static final BigDecimal SMALLER_LATITUDE = new BigDecimal(-90 - 1);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(-180);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(-179);
    private static final BigDecimal SMALLER_LONGITUDE = new BigDecimal(-180 - 1);

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_ZONE_TARIFAIRE = "AAAAAAAAAA";
    private static final String UPDATED_ZONE_TARIFAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_EQUIPEMENTS = "AAAAAAAAAA";
    private static final String UPDATED_EQUIPEMENTS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/arrets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArretRepository arretRepository;

    @Autowired
    private ArretMapper arretMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArretMockMvc;

    private Arret arret;

    private Arret insertedArret;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arret createEntity() {
        return new Arret()
            .nom(DEFAULT_NOM)
            .code(DEFAULT_CODE)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .adresse(DEFAULT_ADRESSE)
            .zoneTarifaire(DEFAULT_ZONE_TARIFAIRE)
            .equipements(DEFAULT_EQUIPEMENTS)
            .actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Arret createUpdatedEntity() {
        return new Arret()
            .nom(UPDATED_NOM)
            .code(UPDATED_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .adresse(UPDATED_ADRESSE)
            .zoneTarifaire(UPDATED_ZONE_TARIFAIRE)
            .equipements(UPDATED_EQUIPEMENTS)
            .actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        arret = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedArret != null) {
            arretRepository.delete(insertedArret);
            insertedArret = null;
        }
    }

    @Test
    @Transactional
    void createArret() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Arret
        ArretDTO arretDTO = arretMapper.toDto(arret);
        var returnedArretDTO = om.readValue(
            restArretMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arretDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArretDTO.class
        );

        // Validate the Arret in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArret = arretMapper.toEntity(returnedArretDTO);
        assertArretUpdatableFieldsEquals(returnedArret, getPersistedArret(returnedArret));

        insertedArret = returnedArret;
    }

    @Test
    @Transactional
    void createArretWithExistingId() throws Exception {
        // Create the Arret with an existing ID
        arret.setId(1L);
        ArretDTO arretDTO = arretMapper.toDto(arret);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArretMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arretDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Arret in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        arret.setNom(null);

        // Create the Arret, which fails.
        ArretDTO arretDTO = arretMapper.toDto(arret);

        restArretMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arretDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        arret.setCode(null);

        // Create the Arret, which fails.
        ArretDTO arretDTO = arretMapper.toDto(arret);

        restArretMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arretDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        arret.setLatitude(null);

        // Create the Arret, which fails.
        ArretDTO arretDTO = arretMapper.toDto(arret);

        restArretMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arretDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        arret.setLongitude(null);

        // Create the Arret, which fails.
        ArretDTO arretDTO = arretMapper.toDto(arret);

        restArretMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arretDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        arret.setActif(null);

        // Create the Arret, which fails.
        ArretDTO arretDTO = arretMapper.toDto(arret);

        restArretMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arretDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArrets() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList
        restArretMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arret.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].zoneTarifaire").value(hasItem(DEFAULT_ZONE_TARIFAIRE)))
            .andExpect(jsonPath("$.[*].equipements").value(hasItem(DEFAULT_EQUIPEMENTS)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getArret() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get the arret
        restArretMockMvc
            .perform(get(ENTITY_API_URL_ID, arret.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(arret.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.latitude").value(sameNumber(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.longitude").value(sameNumber(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.zoneTarifaire").value(DEFAULT_ZONE_TARIFAIRE))
            .andExpect(jsonPath("$.equipements").value(DEFAULT_EQUIPEMENTS))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getArretsByIdFiltering() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        Long id = arret.getId();

        defaultArretFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultArretFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultArretFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllArretsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where nom equals to
        defaultArretFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllArretsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where nom in
        defaultArretFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllArretsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where nom is not null
        defaultArretFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllArretsByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where nom contains
        defaultArretFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllArretsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where nom does not contain
        defaultArretFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllArretsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where code equals to
        defaultArretFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllArretsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where code in
        defaultArretFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllArretsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where code is not null
        defaultArretFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllArretsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where code contains
        defaultArretFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllArretsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where code does not contain
        defaultArretFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllArretsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where latitude equals to
        defaultArretFiltering("latitude.equals=" + DEFAULT_LATITUDE, "latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllArretsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where latitude in
        defaultArretFiltering("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE, "latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllArretsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where latitude is not null
        defaultArretFiltering("latitude.specified=true", "latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllArretsByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where latitude is greater than or equal to
        defaultArretFiltering(
            "latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE,
            "latitude.greaterThanOrEqual=" + (DEFAULT_LATITUDE.add(BigDecimal.ONE))
        );
    }

    @Test
    @Transactional
    void getAllArretsByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where latitude is less than or equal to
        defaultArretFiltering("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE, "latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllArretsByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where latitude is less than
        defaultArretFiltering("latitude.lessThan=" + (DEFAULT_LATITUDE.add(BigDecimal.ONE)), "latitude.lessThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllArretsByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where latitude is greater than
        defaultArretFiltering("latitude.greaterThan=" + SMALLER_LATITUDE, "latitude.greaterThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllArretsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where longitude equals to
        defaultArretFiltering("longitude.equals=" + DEFAULT_LONGITUDE, "longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllArretsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where longitude in
        defaultArretFiltering("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE, "longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllArretsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where longitude is not null
        defaultArretFiltering("longitude.specified=true", "longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllArretsByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where longitude is greater than or equal to
        defaultArretFiltering(
            "longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE,
            "longitude.greaterThanOrEqual=" + (DEFAULT_LONGITUDE.add(BigDecimal.ONE))
        );
    }

    @Test
    @Transactional
    void getAllArretsByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where longitude is less than or equal to
        defaultArretFiltering("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllArretsByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where longitude is less than
        defaultArretFiltering("longitude.lessThan=" + (DEFAULT_LONGITUDE.add(BigDecimal.ONE)), "longitude.lessThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllArretsByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where longitude is greater than
        defaultArretFiltering("longitude.greaterThan=" + SMALLER_LONGITUDE, "longitude.greaterThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllArretsByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where adresse equals to
        defaultArretFiltering("adresse.equals=" + DEFAULT_ADRESSE, "adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllArretsByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where adresse in
        defaultArretFiltering("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE, "adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllArretsByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where adresse is not null
        defaultArretFiltering("adresse.specified=true", "adresse.specified=false");
    }

    @Test
    @Transactional
    void getAllArretsByAdresseContainsSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where adresse contains
        defaultArretFiltering("adresse.contains=" + DEFAULT_ADRESSE, "adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    void getAllArretsByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where adresse does not contain
        defaultArretFiltering("adresse.doesNotContain=" + UPDATED_ADRESSE, "adresse.doesNotContain=" + DEFAULT_ADRESSE);
    }

    @Test
    @Transactional
    void getAllArretsByZoneTarifaireIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where zoneTarifaire equals to
        defaultArretFiltering("zoneTarifaire.equals=" + DEFAULT_ZONE_TARIFAIRE, "zoneTarifaire.equals=" + UPDATED_ZONE_TARIFAIRE);
    }

    @Test
    @Transactional
    void getAllArretsByZoneTarifaireIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where zoneTarifaire in
        defaultArretFiltering(
            "zoneTarifaire.in=" + DEFAULT_ZONE_TARIFAIRE + "," + UPDATED_ZONE_TARIFAIRE,
            "zoneTarifaire.in=" + UPDATED_ZONE_TARIFAIRE
        );
    }

    @Test
    @Transactional
    void getAllArretsByZoneTarifaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where zoneTarifaire is not null
        defaultArretFiltering("zoneTarifaire.specified=true", "zoneTarifaire.specified=false");
    }

    @Test
    @Transactional
    void getAllArretsByZoneTarifaireContainsSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where zoneTarifaire contains
        defaultArretFiltering("zoneTarifaire.contains=" + DEFAULT_ZONE_TARIFAIRE, "zoneTarifaire.contains=" + UPDATED_ZONE_TARIFAIRE);
    }

    @Test
    @Transactional
    void getAllArretsByZoneTarifaireNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where zoneTarifaire does not contain
        defaultArretFiltering(
            "zoneTarifaire.doesNotContain=" + UPDATED_ZONE_TARIFAIRE,
            "zoneTarifaire.doesNotContain=" + DEFAULT_ZONE_TARIFAIRE
        );
    }

    @Test
    @Transactional
    void getAllArretsByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where actif equals to
        defaultArretFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllArretsByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where actif in
        defaultArretFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllArretsByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        // Get all the arretList where actif is not null
        defaultArretFiltering("actif.specified=true", "actif.specified=false");
    }

    private void defaultArretFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultArretShouldBeFound(shouldBeFound);
        defaultArretShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArretShouldBeFound(String filter) throws Exception {
        restArretMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(arret.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].zoneTarifaire").value(hasItem(DEFAULT_ZONE_TARIFAIRE)))
            .andExpect(jsonPath("$.[*].equipements").value(hasItem(DEFAULT_EQUIPEMENTS)))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));

        // Check, that the count call also returns 1
        restArretMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArretShouldNotBeFound(String filter) throws Exception {
        restArretMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArretMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingArret() throws Exception {
        // Get the arret
        restArretMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArret() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the arret
        Arret updatedArret = arretRepository.findById(arret.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArret are not directly saved in db
        em.detach(updatedArret);
        updatedArret
            .nom(UPDATED_NOM)
            .code(UPDATED_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .adresse(UPDATED_ADRESSE)
            .zoneTarifaire(UPDATED_ZONE_TARIFAIRE)
            .equipements(UPDATED_EQUIPEMENTS)
            .actif(UPDATED_ACTIF);
        ArretDTO arretDTO = arretMapper.toDto(updatedArret);

        restArretMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arretDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arretDTO))
            )
            .andExpect(status().isOk());

        // Validate the Arret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArretToMatchAllProperties(updatedArret);
    }

    @Test
    @Transactional
    void putNonExistingArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arret.setId(longCount.incrementAndGet());

        // Create the Arret
        ArretDTO arretDTO = arretMapper.toDto(arret);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArretMockMvc
            .perform(
                put(ENTITY_API_URL_ID, arretDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arretDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arret.setId(longCount.incrementAndGet());

        // Create the Arret
        ArretDTO arretDTO = arretMapper.toDto(arret);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArretMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(arretDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arret.setId(longCount.incrementAndGet());

        // Create the Arret
        ArretDTO arretDTO = arretMapper.toDto(arret);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArretMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(arretDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Arret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArretWithPatch() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the arret using partial update
        Arret partialUpdatedArret = new Arret();
        partialUpdatedArret.setId(arret.getId());

        partialUpdatedArret.nom(UPDATED_NOM).actif(UPDATED_ACTIF);

        restArretMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArret.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArret))
            )
            .andExpect(status().isOk());

        // Validate the Arret in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArretUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedArret, arret), getPersistedArret(arret));
    }

    @Test
    @Transactional
    void fullUpdateArretWithPatch() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the arret using partial update
        Arret partialUpdatedArret = new Arret();
        partialUpdatedArret.setId(arret.getId());

        partialUpdatedArret
            .nom(UPDATED_NOM)
            .code(UPDATED_CODE)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .adresse(UPDATED_ADRESSE)
            .zoneTarifaire(UPDATED_ZONE_TARIFAIRE)
            .equipements(UPDATED_EQUIPEMENTS)
            .actif(UPDATED_ACTIF);

        restArretMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArret.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArret))
            )
            .andExpect(status().isOk());

        // Validate the Arret in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArretUpdatableFieldsEquals(partialUpdatedArret, getPersistedArret(partialUpdatedArret));
    }

    @Test
    @Transactional
    void patchNonExistingArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arret.setId(longCount.incrementAndGet());

        // Create the Arret
        ArretDTO arretDTO = arretMapper.toDto(arret);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArretMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, arretDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(arretDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arret.setId(longCount.incrementAndGet());

        // Create the Arret
        ArretDTO arretDTO = arretMapper.toDto(arret);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArretMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(arretDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Arret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArret() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        arret.setId(longCount.incrementAndGet());

        // Create the Arret
        ArretDTO arretDTO = arretMapper.toDto(arret);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArretMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(arretDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Arret in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArret() throws Exception {
        // Initialize the database
        insertedArret = arretRepository.saveAndFlush(arret);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the arret
        restArretMockMvc
            .perform(delete(ENTITY_API_URL_ID, arret.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return arretRepository.count();
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

    protected Arret getPersistedArret(Arret arret) {
        return arretRepository.findById(arret.getId()).orElseThrow();
    }

    protected void assertPersistedArretToMatchAllProperties(Arret expectedArret) {
        assertArretAllPropertiesEquals(expectedArret, getPersistedArret(expectedArret));
    }

    protected void assertPersistedArretToMatchUpdatableProperties(Arret expectedArret) {
        assertArretAllUpdatablePropertiesEquals(expectedArret, getPersistedArret(expectedArret));
    }
}
