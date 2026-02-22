package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.AlerteAsserts.*;
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
import sn.yegg.app.domain.Alerte;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.repository.AlerteRepository;
import sn.yegg.app.service.dto.AlerteDTO;
import sn.yegg.app.service.mapper.AlerteMapper;

/**
 * Integration tests for the {@link AlerteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlerteResourceIT {

    private static final String DEFAULT_TYPE_CIBLE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_CIBLE = "BBBBBBBBBB";

    private static final Long DEFAULT_CIBLE_ID = 1L;
    private static final Long UPDATED_CIBLE_ID = 2L;
    private static final Long SMALLER_CIBLE_ID = 1L - 1L;

    private static final Integer DEFAULT_SEUIL_MINUTES = 1;
    private static final Integer UPDATED_SEUIL_MINUTES = 2;
    private static final Integer SMALLER_SEUIL_MINUTES = 1 - 1;

    private static final String DEFAULT_JOURS_ACTIVATION = "AAAAAAAAAA";
    private static final String UPDATED_JOURS_ACTIVATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_HEURE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HEURE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_HEURE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HEURE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

    private static final Instant DEFAULT_DERNIER_DECLENCHEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DERNIER_DECLENCHEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_NOMBRE_DECLENCHEMENTS = 1;
    private static final Integer UPDATED_NOMBRE_DECLENCHEMENTS = 2;
    private static final Integer SMALLER_NOMBRE_DECLENCHEMENTS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/alertes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlerteRepository alerteRepository;

    @Autowired
    private AlerteMapper alerteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlerteMockMvc;

    private Alerte alerte;

    private Alerte insertedAlerte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alerte createEntity() {
        return new Alerte()
            .typeCible(DEFAULT_TYPE_CIBLE)
            .cibleId(DEFAULT_CIBLE_ID)
            .seuilMinutes(DEFAULT_SEUIL_MINUTES)
            .joursActivation(DEFAULT_JOURS_ACTIVATION)
            .heureDebut(DEFAULT_HEURE_DEBUT)
            .heureFin(DEFAULT_HEURE_FIN)
            .statut(DEFAULT_STATUT)
            .dernierDeclenchement(DEFAULT_DERNIER_DECLENCHEMENT)
            .nombreDeclenchements(DEFAULT_NOMBRE_DECLENCHEMENTS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alerte createUpdatedEntity() {
        return new Alerte()
            .typeCible(UPDATED_TYPE_CIBLE)
            .cibleId(UPDATED_CIBLE_ID)
            .seuilMinutes(UPDATED_SEUIL_MINUTES)
            .joursActivation(UPDATED_JOURS_ACTIVATION)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .statut(UPDATED_STATUT)
            .dernierDeclenchement(UPDATED_DERNIER_DECLENCHEMENT)
            .nombreDeclenchements(UPDATED_NOMBRE_DECLENCHEMENTS);
    }

    @BeforeEach
    void initTest() {
        alerte = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAlerte != null) {
            alerteRepository.delete(insertedAlerte);
            insertedAlerte = null;
        }
    }

    @Test
    @Transactional
    void createAlerte() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Alerte
        AlerteDTO alerteDTO = alerteMapper.toDto(alerte);
        var returnedAlerteDTO = om.readValue(
            restAlerteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlerteDTO.class
        );

        // Validate the Alerte in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlerte = alerteMapper.toEntity(returnedAlerteDTO);
        assertAlerteUpdatableFieldsEquals(returnedAlerte, getPersistedAlerte(returnedAlerte));

        insertedAlerte = returnedAlerte;
    }

    @Test
    @Transactional
    void createAlerteWithExistingId() throws Exception {
        // Create the Alerte with an existing ID
        alerte.setId(1L);
        AlerteDTO alerteDTO = alerteMapper.toDto(alerte);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeCibleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alerte.setTypeCible(null);

        // Create the Alerte, which fails.
        AlerteDTO alerteDTO = alerteMapper.toDto(alerte);

        restAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCibleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alerte.setCibleId(null);

        // Create the Alerte, which fails.
        AlerteDTO alerteDTO = alerteMapper.toDto(alerte);

        restAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeuilMinutesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alerte.setSeuilMinutes(null);

        // Create the Alerte, which fails.
        AlerteDTO alerteDTO = alerteMapper.toDto(alerte);

        restAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alerte.setStatut(null);

        // Create the Alerte, which fails.
        AlerteDTO alerteDTO = alerteMapper.toDto(alerte);

        restAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlertes() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList
        restAlerteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alerte.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeCible").value(hasItem(DEFAULT_TYPE_CIBLE)))
            .andExpect(jsonPath("$.[*].cibleId").value(hasItem(DEFAULT_CIBLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].seuilMinutes").value(hasItem(DEFAULT_SEUIL_MINUTES)))
            .andExpect(jsonPath("$.[*].joursActivation").value(hasItem(DEFAULT_JOURS_ACTIVATION)))
            .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(DEFAULT_HEURE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].dernierDeclenchement").value(hasItem(DEFAULT_DERNIER_DECLENCHEMENT.toString())))
            .andExpect(jsonPath("$.[*].nombreDeclenchements").value(hasItem(DEFAULT_NOMBRE_DECLENCHEMENTS)));
    }

    @Test
    @Transactional
    void getAlerte() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get the alerte
        restAlerteMockMvc
            .perform(get(ENTITY_API_URL_ID, alerte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alerte.getId().intValue()))
            .andExpect(jsonPath("$.typeCible").value(DEFAULT_TYPE_CIBLE))
            .andExpect(jsonPath("$.cibleId").value(DEFAULT_CIBLE_ID.intValue()))
            .andExpect(jsonPath("$.seuilMinutes").value(DEFAULT_SEUIL_MINUTES))
            .andExpect(jsonPath("$.joursActivation").value(DEFAULT_JOURS_ACTIVATION))
            .andExpect(jsonPath("$.heureDebut").value(DEFAULT_HEURE_DEBUT.toString()))
            .andExpect(jsonPath("$.heureFin").value(DEFAULT_HEURE_FIN.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT))
            .andExpect(jsonPath("$.dernierDeclenchement").value(DEFAULT_DERNIER_DECLENCHEMENT.toString()))
            .andExpect(jsonPath("$.nombreDeclenchements").value(DEFAULT_NOMBRE_DECLENCHEMENTS));
    }

    @Test
    @Transactional
    void getAlertesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        Long id = alerte.getId();

        defaultAlerteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlerteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlerteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlertesByTypeCibleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where typeCible equals to
        defaultAlerteFiltering("typeCible.equals=" + DEFAULT_TYPE_CIBLE, "typeCible.equals=" + UPDATED_TYPE_CIBLE);
    }

    @Test
    @Transactional
    void getAllAlertesByTypeCibleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where typeCible in
        defaultAlerteFiltering("typeCible.in=" + DEFAULT_TYPE_CIBLE + "," + UPDATED_TYPE_CIBLE, "typeCible.in=" + UPDATED_TYPE_CIBLE);
    }

    @Test
    @Transactional
    void getAllAlertesByTypeCibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where typeCible is not null
        defaultAlerteFiltering("typeCible.specified=true", "typeCible.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertesByTypeCibleContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where typeCible contains
        defaultAlerteFiltering("typeCible.contains=" + DEFAULT_TYPE_CIBLE, "typeCible.contains=" + UPDATED_TYPE_CIBLE);
    }

    @Test
    @Transactional
    void getAllAlertesByTypeCibleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where typeCible does not contain
        defaultAlerteFiltering("typeCible.doesNotContain=" + UPDATED_TYPE_CIBLE, "typeCible.doesNotContain=" + DEFAULT_TYPE_CIBLE);
    }

    @Test
    @Transactional
    void getAllAlertesByCibleIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where cibleId equals to
        defaultAlerteFiltering("cibleId.equals=" + DEFAULT_CIBLE_ID, "cibleId.equals=" + UPDATED_CIBLE_ID);
    }

    @Test
    @Transactional
    void getAllAlertesByCibleIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where cibleId in
        defaultAlerteFiltering("cibleId.in=" + DEFAULT_CIBLE_ID + "," + UPDATED_CIBLE_ID, "cibleId.in=" + UPDATED_CIBLE_ID);
    }

    @Test
    @Transactional
    void getAllAlertesByCibleIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where cibleId is not null
        defaultAlerteFiltering("cibleId.specified=true", "cibleId.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertesByCibleIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where cibleId is greater than or equal to
        defaultAlerteFiltering("cibleId.greaterThanOrEqual=" + DEFAULT_CIBLE_ID, "cibleId.greaterThanOrEqual=" + UPDATED_CIBLE_ID);
    }

    @Test
    @Transactional
    void getAllAlertesByCibleIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where cibleId is less than or equal to
        defaultAlerteFiltering("cibleId.lessThanOrEqual=" + DEFAULT_CIBLE_ID, "cibleId.lessThanOrEqual=" + SMALLER_CIBLE_ID);
    }

    @Test
    @Transactional
    void getAllAlertesByCibleIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where cibleId is less than
        defaultAlerteFiltering("cibleId.lessThan=" + UPDATED_CIBLE_ID, "cibleId.lessThan=" + DEFAULT_CIBLE_ID);
    }

    @Test
    @Transactional
    void getAllAlertesByCibleIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where cibleId is greater than
        defaultAlerteFiltering("cibleId.greaterThan=" + SMALLER_CIBLE_ID, "cibleId.greaterThan=" + DEFAULT_CIBLE_ID);
    }

    @Test
    @Transactional
    void getAllAlertesBySeuilMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where seuilMinutes equals to
        defaultAlerteFiltering("seuilMinutes.equals=" + DEFAULT_SEUIL_MINUTES, "seuilMinutes.equals=" + UPDATED_SEUIL_MINUTES);
    }

    @Test
    @Transactional
    void getAllAlertesBySeuilMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where seuilMinutes in
        defaultAlerteFiltering(
            "seuilMinutes.in=" + DEFAULT_SEUIL_MINUTES + "," + UPDATED_SEUIL_MINUTES,
            "seuilMinutes.in=" + UPDATED_SEUIL_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlertesBySeuilMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where seuilMinutes is not null
        defaultAlerteFiltering("seuilMinutes.specified=true", "seuilMinutes.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertesBySeuilMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where seuilMinutes is greater than or equal to
        defaultAlerteFiltering(
            "seuilMinutes.greaterThanOrEqual=" + DEFAULT_SEUIL_MINUTES,
            "seuilMinutes.greaterThanOrEqual=" + (DEFAULT_SEUIL_MINUTES + 1)
        );
    }

    @Test
    @Transactional
    void getAllAlertesBySeuilMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where seuilMinutes is less than or equal to
        defaultAlerteFiltering(
            "seuilMinutes.lessThanOrEqual=" + DEFAULT_SEUIL_MINUTES,
            "seuilMinutes.lessThanOrEqual=" + SMALLER_SEUIL_MINUTES
        );
    }

    @Test
    @Transactional
    void getAllAlertesBySeuilMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where seuilMinutes is less than
        defaultAlerteFiltering("seuilMinutes.lessThan=" + (DEFAULT_SEUIL_MINUTES + 1), "seuilMinutes.lessThan=" + DEFAULT_SEUIL_MINUTES);
    }

    @Test
    @Transactional
    void getAllAlertesBySeuilMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where seuilMinutes is greater than
        defaultAlerteFiltering("seuilMinutes.greaterThan=" + SMALLER_SEUIL_MINUTES, "seuilMinutes.greaterThan=" + DEFAULT_SEUIL_MINUTES);
    }

    @Test
    @Transactional
    void getAllAlertesByJoursActivationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where joursActivation equals to
        defaultAlerteFiltering("joursActivation.equals=" + DEFAULT_JOURS_ACTIVATION, "joursActivation.equals=" + UPDATED_JOURS_ACTIVATION);
    }

    @Test
    @Transactional
    void getAllAlertesByJoursActivationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where joursActivation in
        defaultAlerteFiltering(
            "joursActivation.in=" + DEFAULT_JOURS_ACTIVATION + "," + UPDATED_JOURS_ACTIVATION,
            "joursActivation.in=" + UPDATED_JOURS_ACTIVATION
        );
    }

    @Test
    @Transactional
    void getAllAlertesByJoursActivationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where joursActivation is not null
        defaultAlerteFiltering("joursActivation.specified=true", "joursActivation.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertesByJoursActivationContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where joursActivation contains
        defaultAlerteFiltering(
            "joursActivation.contains=" + DEFAULT_JOURS_ACTIVATION,
            "joursActivation.contains=" + UPDATED_JOURS_ACTIVATION
        );
    }

    @Test
    @Transactional
    void getAllAlertesByJoursActivationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where joursActivation does not contain
        defaultAlerteFiltering(
            "joursActivation.doesNotContain=" + UPDATED_JOURS_ACTIVATION,
            "joursActivation.doesNotContain=" + DEFAULT_JOURS_ACTIVATION
        );
    }

    @Test
    @Transactional
    void getAllAlertesByHeureDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where heureDebut equals to
        defaultAlerteFiltering("heureDebut.equals=" + DEFAULT_HEURE_DEBUT, "heureDebut.equals=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    void getAllAlertesByHeureDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where heureDebut in
        defaultAlerteFiltering("heureDebut.in=" + DEFAULT_HEURE_DEBUT + "," + UPDATED_HEURE_DEBUT, "heureDebut.in=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    void getAllAlertesByHeureDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where heureDebut is not null
        defaultAlerteFiltering("heureDebut.specified=true", "heureDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertesByHeureFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where heureFin equals to
        defaultAlerteFiltering("heureFin.equals=" + DEFAULT_HEURE_FIN, "heureFin.equals=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    void getAllAlertesByHeureFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where heureFin in
        defaultAlerteFiltering("heureFin.in=" + DEFAULT_HEURE_FIN + "," + UPDATED_HEURE_FIN, "heureFin.in=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    void getAllAlertesByHeureFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where heureFin is not null
        defaultAlerteFiltering("heureFin.specified=true", "heureFin.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertesByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where statut equals to
        defaultAlerteFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllAlertesByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where statut in
        defaultAlerteFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllAlertesByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where statut is not null
        defaultAlerteFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertesByStatutContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where statut contains
        defaultAlerteFiltering("statut.contains=" + DEFAULT_STATUT, "statut.contains=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllAlertesByStatutNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where statut does not contain
        defaultAlerteFiltering("statut.doesNotContain=" + UPDATED_STATUT, "statut.doesNotContain=" + DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void getAllAlertesByDernierDeclenchementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where dernierDeclenchement equals to
        defaultAlerteFiltering(
            "dernierDeclenchement.equals=" + DEFAULT_DERNIER_DECLENCHEMENT,
            "dernierDeclenchement.equals=" + UPDATED_DERNIER_DECLENCHEMENT
        );
    }

    @Test
    @Transactional
    void getAllAlertesByDernierDeclenchementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where dernierDeclenchement in
        defaultAlerteFiltering(
            "dernierDeclenchement.in=" + DEFAULT_DERNIER_DECLENCHEMENT + "," + UPDATED_DERNIER_DECLENCHEMENT,
            "dernierDeclenchement.in=" + UPDATED_DERNIER_DECLENCHEMENT
        );
    }

    @Test
    @Transactional
    void getAllAlertesByDernierDeclenchementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where dernierDeclenchement is not null
        defaultAlerteFiltering("dernierDeclenchement.specified=true", "dernierDeclenchement.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertesByNombreDeclenchementsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where nombreDeclenchements equals to
        defaultAlerteFiltering(
            "nombreDeclenchements.equals=" + DEFAULT_NOMBRE_DECLENCHEMENTS,
            "nombreDeclenchements.equals=" + UPDATED_NOMBRE_DECLENCHEMENTS
        );
    }

    @Test
    @Transactional
    void getAllAlertesByNombreDeclenchementsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where nombreDeclenchements in
        defaultAlerteFiltering(
            "nombreDeclenchements.in=" + DEFAULT_NOMBRE_DECLENCHEMENTS + "," + UPDATED_NOMBRE_DECLENCHEMENTS,
            "nombreDeclenchements.in=" + UPDATED_NOMBRE_DECLENCHEMENTS
        );
    }

    @Test
    @Transactional
    void getAllAlertesByNombreDeclenchementsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where nombreDeclenchements is not null
        defaultAlerteFiltering("nombreDeclenchements.specified=true", "nombreDeclenchements.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertesByNombreDeclenchementsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where nombreDeclenchements is greater than or equal to
        defaultAlerteFiltering(
            "nombreDeclenchements.greaterThanOrEqual=" + DEFAULT_NOMBRE_DECLENCHEMENTS,
            "nombreDeclenchements.greaterThanOrEqual=" + UPDATED_NOMBRE_DECLENCHEMENTS
        );
    }

    @Test
    @Transactional
    void getAllAlertesByNombreDeclenchementsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where nombreDeclenchements is less than or equal to
        defaultAlerteFiltering(
            "nombreDeclenchements.lessThanOrEqual=" + DEFAULT_NOMBRE_DECLENCHEMENTS,
            "nombreDeclenchements.lessThanOrEqual=" + SMALLER_NOMBRE_DECLENCHEMENTS
        );
    }

    @Test
    @Transactional
    void getAllAlertesByNombreDeclenchementsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where nombreDeclenchements is less than
        defaultAlerteFiltering(
            "nombreDeclenchements.lessThan=" + UPDATED_NOMBRE_DECLENCHEMENTS,
            "nombreDeclenchements.lessThan=" + DEFAULT_NOMBRE_DECLENCHEMENTS
        );
    }

    @Test
    @Transactional
    void getAllAlertesByNombreDeclenchementsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        // Get all the alerteList where nombreDeclenchements is greater than
        defaultAlerteFiltering(
            "nombreDeclenchements.greaterThan=" + SMALLER_NOMBRE_DECLENCHEMENTS,
            "nombreDeclenchements.greaterThan=" + DEFAULT_NOMBRE_DECLENCHEMENTS
        );
    }

    @Test
    @Transactional
    void getAllAlertesByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            alerteRepository.saveAndFlush(alerte);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        alerte.setUtilisateur(utilisateur);
        alerteRepository.saveAndFlush(alerte);
        Long utilisateurId = utilisateur.getId();
        // Get all the alerteList where utilisateur equals to utilisateurId
        defaultAlerteShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the alerteList where utilisateur equals to (utilisateurId + 1)
        defaultAlerteShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultAlerteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlerteShouldBeFound(shouldBeFound);
        defaultAlerteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlerteShouldBeFound(String filter) throws Exception {
        restAlerteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alerte.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeCible").value(hasItem(DEFAULT_TYPE_CIBLE)))
            .andExpect(jsonPath("$.[*].cibleId").value(hasItem(DEFAULT_CIBLE_ID.intValue())))
            .andExpect(jsonPath("$.[*].seuilMinutes").value(hasItem(DEFAULT_SEUIL_MINUTES)))
            .andExpect(jsonPath("$.[*].joursActivation").value(hasItem(DEFAULT_JOURS_ACTIVATION)))
            .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(DEFAULT_HEURE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].dernierDeclenchement").value(hasItem(DEFAULT_DERNIER_DECLENCHEMENT.toString())))
            .andExpect(jsonPath("$.[*].nombreDeclenchements").value(hasItem(DEFAULT_NOMBRE_DECLENCHEMENTS)));

        // Check, that the count call also returns 1
        restAlerteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlerteShouldNotBeFound(String filter) throws Exception {
        restAlerteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlerteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlerte() throws Exception {
        // Get the alerte
        restAlerteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlerte() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerte
        Alerte updatedAlerte = alerteRepository.findById(alerte.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlerte are not directly saved in db
        em.detach(updatedAlerte);
        updatedAlerte
            .typeCible(UPDATED_TYPE_CIBLE)
            .cibleId(UPDATED_CIBLE_ID)
            .seuilMinutes(UPDATED_SEUIL_MINUTES)
            .joursActivation(UPDATED_JOURS_ACTIVATION)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .statut(UPDATED_STATUT)
            .dernierDeclenchement(UPDATED_DERNIER_DECLENCHEMENT)
            .nombreDeclenchements(UPDATED_NOMBRE_DECLENCHEMENTS);
        AlerteDTO alerteDTO = alerteMapper.toDto(updatedAlerte);

        restAlerteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alerteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlerteToMatchAllProperties(updatedAlerte);
    }

    @Test
    @Transactional
    void putNonExistingAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerte.setId(longCount.incrementAndGet());

        // Create the Alerte
        AlerteDTO alerteDTO = alerteMapper.toDto(alerte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alerteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerte.setId(longCount.incrementAndGet());

        // Create the Alerte
        AlerteDTO alerteDTO = alerteMapper.toDto(alerte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alerteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerte.setId(longCount.incrementAndGet());

        // Create the Alerte
        AlerteDTO alerteDTO = alerteMapper.toDto(alerte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlerteWithPatch() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerte using partial update
        Alerte partialUpdatedAlerte = new Alerte();
        partialUpdatedAlerte.setId(alerte.getId());

        partialUpdatedAlerte
            .typeCible(UPDATED_TYPE_CIBLE)
            .cibleId(UPDATED_CIBLE_ID)
            .joursActivation(UPDATED_JOURS_ACTIVATION)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .nombreDeclenchements(UPDATED_NOMBRE_DECLENCHEMENTS);

        restAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlerte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlerte))
            )
            .andExpect(status().isOk());

        // Validate the Alerte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlerteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlerte, alerte), getPersistedAlerte(alerte));
    }

    @Test
    @Transactional
    void fullUpdateAlerteWithPatch() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerte using partial update
        Alerte partialUpdatedAlerte = new Alerte();
        partialUpdatedAlerte.setId(alerte.getId());

        partialUpdatedAlerte
            .typeCible(UPDATED_TYPE_CIBLE)
            .cibleId(UPDATED_CIBLE_ID)
            .seuilMinutes(UPDATED_SEUIL_MINUTES)
            .joursActivation(UPDATED_JOURS_ACTIVATION)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .statut(UPDATED_STATUT)
            .dernierDeclenchement(UPDATED_DERNIER_DECLENCHEMENT)
            .nombreDeclenchements(UPDATED_NOMBRE_DECLENCHEMENTS);

        restAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlerte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlerte))
            )
            .andExpect(status().isOk());

        // Validate the Alerte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlerteUpdatableFieldsEquals(partialUpdatedAlerte, getPersistedAlerte(partialUpdatedAlerte));
    }

    @Test
    @Transactional
    void patchNonExistingAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerte.setId(longCount.incrementAndGet());

        // Create the Alerte
        AlerteDTO alerteDTO = alerteMapper.toDto(alerte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alerteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alerteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerte.setId(longCount.incrementAndGet());

        // Create the Alerte
        AlerteDTO alerteDTO = alerteMapper.toDto(alerte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alerteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerte.setId(longCount.incrementAndGet());

        // Create the Alerte
        AlerteDTO alerteDTO = alerteMapper.toDto(alerte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alerteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlerte() throws Exception {
        // Initialize the database
        insertedAlerte = alerteRepository.saveAndFlush(alerte);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alerte
        restAlerteMockMvc
            .perform(delete(ENTITY_API_URL_ID, alerte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alerteRepository.count();
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

    protected Alerte getPersistedAlerte(Alerte alerte) {
        return alerteRepository.findById(alerte.getId()).orElseThrow();
    }

    protected void assertPersistedAlerteToMatchAllProperties(Alerte expectedAlerte) {
        assertAlerteAllPropertiesEquals(expectedAlerte, getPersistedAlerte(expectedAlerte));
    }

    protected void assertPersistedAlerteToMatchUpdatableProperties(Alerte expectedAlerte) {
        assertAlerteAllUpdatablePropertiesEquals(expectedAlerte, getPersistedAlerte(expectedAlerte));
    }
}
