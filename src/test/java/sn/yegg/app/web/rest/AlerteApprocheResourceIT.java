package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.AlerteApprocheAsserts.*;
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
import sn.yegg.app.domain.AlerteApproche;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.domain.enumeration.AlertStatus;
import sn.yegg.app.domain.enumeration.ThresholdType;
import sn.yegg.app.repository.AlerteApprocheRepository;
import sn.yegg.app.service.dto.AlerteApprocheDTO;
import sn.yegg.app.service.mapper.AlerteApprocheMapper;

/**
 * Integration tests for the {@link AlerteApprocheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlerteApprocheResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_SEUIL_DISTANCE = 50;
    private static final Integer UPDATED_SEUIL_DISTANCE = 51;
    private static final Integer SMALLER_SEUIL_DISTANCE = 50 - 1;

    private static final Integer DEFAULT_SEUIL_TEMPS = 1;
    private static final Integer UPDATED_SEUIL_TEMPS = 2;
    private static final Integer SMALLER_SEUIL_TEMPS = 1 - 1;

    private static final ThresholdType DEFAULT_TYPE_SEUIL = ThresholdType.DISTANCE;
    private static final ThresholdType UPDATED_TYPE_SEUIL = ThresholdType.TIME;

    private static final String DEFAULT_JOURS_ACTIVATION = "AAAAAAAAAA";
    private static final String UPDATED_JOURS_ACTIVATION = "BBBBBBBBBB";

    private static final String DEFAULT_HEURE_DEBUT = "AAAAAAAAAA";
    private static final String UPDATED_HEURE_DEBUT = "BBBBBBBBBB";

    private static final String DEFAULT_HEURE_FIN = "AAAAAAAAAA";
    private static final String UPDATED_HEURE_FIN = "BBBBBBBBBB";

    private static final AlertStatus DEFAULT_STATUT = AlertStatus.ACTIVE;
    private static final AlertStatus UPDATED_STATUT = AlertStatus.PAUSED;

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_MODIFICATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MODIFICATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DERNIER_DECLENCHEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DERNIER_DECLENCHEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_NOMBRE_DECLENCHEMENTS = 0;
    private static final Integer UPDATED_NOMBRE_DECLENCHEMENTS = 1;
    private static final Integer SMALLER_NOMBRE_DECLENCHEMENTS = 0 - 1;

    private static final String ENTITY_API_URL = "/api/alerte-approches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlerteApprocheRepository alerteApprocheRepository;

    @Autowired
    private AlerteApprocheMapper alerteApprocheMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlerteApprocheMockMvc;

    private AlerteApproche alerteApproche;

    private AlerteApproche insertedAlerteApproche;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlerteApproche createEntity() {
        return new AlerteApproche()
            .nom(DEFAULT_NOM)
            .seuilDistance(DEFAULT_SEUIL_DISTANCE)
            .seuilTemps(DEFAULT_SEUIL_TEMPS)
            .typeSeuil(DEFAULT_TYPE_SEUIL)
            .joursActivation(DEFAULT_JOURS_ACTIVATION)
            .heureDebut(DEFAULT_HEURE_DEBUT)
            .heureFin(DEFAULT_HEURE_FIN)
            .statut(DEFAULT_STATUT)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateModification(DEFAULT_DATE_MODIFICATION)
            .dernierDeclenchement(DEFAULT_DERNIER_DECLENCHEMENT)
            .nombreDeclenchements(DEFAULT_NOMBRE_DECLENCHEMENTS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlerteApproche createUpdatedEntity() {
        return new AlerteApproche()
            .nom(UPDATED_NOM)
            .seuilDistance(UPDATED_SEUIL_DISTANCE)
            .seuilTemps(UPDATED_SEUIL_TEMPS)
            .typeSeuil(UPDATED_TYPE_SEUIL)
            .joursActivation(UPDATED_JOURS_ACTIVATION)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .statut(UPDATED_STATUT)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dernierDeclenchement(UPDATED_DERNIER_DECLENCHEMENT)
            .nombreDeclenchements(UPDATED_NOMBRE_DECLENCHEMENTS);
    }

    @BeforeEach
    void initTest() {
        alerteApproche = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAlerteApproche != null) {
            alerteApprocheRepository.delete(insertedAlerteApproche);
            insertedAlerteApproche = null;
        }
    }

    @Test
    @Transactional
    void createAlerteApproche() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlerteApproche
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(alerteApproche);
        var returnedAlerteApprocheDTO = om.readValue(
            restAlerteApprocheMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteApprocheDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlerteApprocheDTO.class
        );

        // Validate the AlerteApproche in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlerteApproche = alerteApprocheMapper.toEntity(returnedAlerteApprocheDTO);
        assertAlerteApprocheUpdatableFieldsEquals(returnedAlerteApproche, getPersistedAlerteApproche(returnedAlerteApproche));

        insertedAlerteApproche = returnedAlerteApproche;
    }

    @Test
    @Transactional
    void createAlerteApprocheWithExistingId() throws Exception {
        // Create the AlerteApproche with an existing ID
        alerteApproche.setId(1L);
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(alerteApproche);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlerteApprocheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteApprocheDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlerteApproche in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeSeuilIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alerteApproche.setTypeSeuil(null);

        // Create the AlerteApproche, which fails.
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(alerteApproche);

        restAlerteApprocheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteApprocheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alerteApproche.setStatut(null);

        // Create the AlerteApproche, which fails.
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(alerteApproche);

        restAlerteApprocheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteApprocheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alerteApproche.setDateCreation(null);

        // Create the AlerteApproche, which fails.
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(alerteApproche);

        restAlerteApprocheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteApprocheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlerteApproches() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList
        restAlerteApprocheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alerteApproche.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].seuilDistance").value(hasItem(DEFAULT_SEUIL_DISTANCE)))
            .andExpect(jsonPath("$.[*].seuilTemps").value(hasItem(DEFAULT_SEUIL_TEMPS)))
            .andExpect(jsonPath("$.[*].typeSeuil").value(hasItem(DEFAULT_TYPE_SEUIL.toString())))
            .andExpect(jsonPath("$.[*].joursActivation").value(hasItem(DEFAULT_JOURS_ACTIVATION)))
            .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(DEFAULT_HEURE_DEBUT)))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].dernierDeclenchement").value(hasItem(DEFAULT_DERNIER_DECLENCHEMENT.toString())))
            .andExpect(jsonPath("$.[*].nombreDeclenchements").value(hasItem(DEFAULT_NOMBRE_DECLENCHEMENTS)));
    }

    @Test
    @Transactional
    void getAlerteApproche() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get the alerteApproche
        restAlerteApprocheMockMvc
            .perform(get(ENTITY_API_URL_ID, alerteApproche.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alerteApproche.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.seuilDistance").value(DEFAULT_SEUIL_DISTANCE))
            .andExpect(jsonPath("$.seuilTemps").value(DEFAULT_SEUIL_TEMPS))
            .andExpect(jsonPath("$.typeSeuil").value(DEFAULT_TYPE_SEUIL.toString()))
            .andExpect(jsonPath("$.joursActivation").value(DEFAULT_JOURS_ACTIVATION))
            .andExpect(jsonPath("$.heureDebut").value(DEFAULT_HEURE_DEBUT))
            .andExpect(jsonPath("$.heureFin").value(DEFAULT_HEURE_FIN))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateModification").value(DEFAULT_DATE_MODIFICATION.toString()))
            .andExpect(jsonPath("$.dernierDeclenchement").value(DEFAULT_DERNIER_DECLENCHEMENT.toString()))
            .andExpect(jsonPath("$.nombreDeclenchements").value(DEFAULT_NOMBRE_DECLENCHEMENTS));
    }

    @Test
    @Transactional
    void getAlerteApprochesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        Long id = alerteApproche.getId();

        defaultAlerteApprocheFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlerteApprocheFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlerteApprocheFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where nom equals to
        defaultAlerteApprocheFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where nom in
        defaultAlerteApprocheFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where nom is not null
        defaultAlerteApprocheFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where nom contains
        defaultAlerteApprocheFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where nom does not contain
        defaultAlerteApprocheFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilDistanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilDistance equals to
        defaultAlerteApprocheFiltering("seuilDistance.equals=" + DEFAULT_SEUIL_DISTANCE, "seuilDistance.equals=" + UPDATED_SEUIL_DISTANCE);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilDistanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilDistance in
        defaultAlerteApprocheFiltering(
            "seuilDistance.in=" + DEFAULT_SEUIL_DISTANCE + "," + UPDATED_SEUIL_DISTANCE,
            "seuilDistance.in=" + UPDATED_SEUIL_DISTANCE
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilDistanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilDistance is not null
        defaultAlerteApprocheFiltering("seuilDistance.specified=true", "seuilDistance.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilDistanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilDistance is greater than or equal to
        defaultAlerteApprocheFiltering(
            "seuilDistance.greaterThanOrEqual=" + DEFAULT_SEUIL_DISTANCE,
            "seuilDistance.greaterThanOrEqual=" + (DEFAULT_SEUIL_DISTANCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilDistanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilDistance is less than or equal to
        defaultAlerteApprocheFiltering(
            "seuilDistance.lessThanOrEqual=" + DEFAULT_SEUIL_DISTANCE,
            "seuilDistance.lessThanOrEqual=" + SMALLER_SEUIL_DISTANCE
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilDistanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilDistance is less than
        defaultAlerteApprocheFiltering(
            "seuilDistance.lessThan=" + (DEFAULT_SEUIL_DISTANCE + 1),
            "seuilDistance.lessThan=" + DEFAULT_SEUIL_DISTANCE
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilDistanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilDistance is greater than
        defaultAlerteApprocheFiltering(
            "seuilDistance.greaterThan=" + SMALLER_SEUIL_DISTANCE,
            "seuilDistance.greaterThan=" + DEFAULT_SEUIL_DISTANCE
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilTempsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilTemps equals to
        defaultAlerteApprocheFiltering("seuilTemps.equals=" + DEFAULT_SEUIL_TEMPS, "seuilTemps.equals=" + UPDATED_SEUIL_TEMPS);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilTempsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilTemps in
        defaultAlerteApprocheFiltering(
            "seuilTemps.in=" + DEFAULT_SEUIL_TEMPS + "," + UPDATED_SEUIL_TEMPS,
            "seuilTemps.in=" + UPDATED_SEUIL_TEMPS
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilTempsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilTemps is not null
        defaultAlerteApprocheFiltering("seuilTemps.specified=true", "seuilTemps.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilTempsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilTemps is greater than or equal to
        defaultAlerteApprocheFiltering(
            "seuilTemps.greaterThanOrEqual=" + DEFAULT_SEUIL_TEMPS,
            "seuilTemps.greaterThanOrEqual=" + (DEFAULT_SEUIL_TEMPS + 1)
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilTempsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilTemps is less than or equal to
        defaultAlerteApprocheFiltering(
            "seuilTemps.lessThanOrEqual=" + DEFAULT_SEUIL_TEMPS,
            "seuilTemps.lessThanOrEqual=" + SMALLER_SEUIL_TEMPS
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilTempsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilTemps is less than
        defaultAlerteApprocheFiltering("seuilTemps.lessThan=" + (DEFAULT_SEUIL_TEMPS + 1), "seuilTemps.lessThan=" + DEFAULT_SEUIL_TEMPS);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesBySeuilTempsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where seuilTemps is greater than
        defaultAlerteApprocheFiltering("seuilTemps.greaterThan=" + SMALLER_SEUIL_TEMPS, "seuilTemps.greaterThan=" + DEFAULT_SEUIL_TEMPS);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByTypeSeuilIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where typeSeuil equals to
        defaultAlerteApprocheFiltering("typeSeuil.equals=" + DEFAULT_TYPE_SEUIL, "typeSeuil.equals=" + UPDATED_TYPE_SEUIL);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByTypeSeuilIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where typeSeuil in
        defaultAlerteApprocheFiltering(
            "typeSeuil.in=" + DEFAULT_TYPE_SEUIL + "," + UPDATED_TYPE_SEUIL,
            "typeSeuil.in=" + UPDATED_TYPE_SEUIL
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByTypeSeuilIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where typeSeuil is not null
        defaultAlerteApprocheFiltering("typeSeuil.specified=true", "typeSeuil.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByJoursActivationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where joursActivation equals to
        defaultAlerteApprocheFiltering(
            "joursActivation.equals=" + DEFAULT_JOURS_ACTIVATION,
            "joursActivation.equals=" + UPDATED_JOURS_ACTIVATION
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByJoursActivationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where joursActivation in
        defaultAlerteApprocheFiltering(
            "joursActivation.in=" + DEFAULT_JOURS_ACTIVATION + "," + UPDATED_JOURS_ACTIVATION,
            "joursActivation.in=" + UPDATED_JOURS_ACTIVATION
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByJoursActivationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where joursActivation is not null
        defaultAlerteApprocheFiltering("joursActivation.specified=true", "joursActivation.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByJoursActivationContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where joursActivation contains
        defaultAlerteApprocheFiltering(
            "joursActivation.contains=" + DEFAULT_JOURS_ACTIVATION,
            "joursActivation.contains=" + UPDATED_JOURS_ACTIVATION
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByJoursActivationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where joursActivation does not contain
        defaultAlerteApprocheFiltering(
            "joursActivation.doesNotContain=" + UPDATED_JOURS_ACTIVATION,
            "joursActivation.doesNotContain=" + DEFAULT_JOURS_ACTIVATION
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByHeureDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where heureDebut equals to
        defaultAlerteApprocheFiltering("heureDebut.equals=" + DEFAULT_HEURE_DEBUT, "heureDebut.equals=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByHeureDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where heureDebut in
        defaultAlerteApprocheFiltering(
            "heureDebut.in=" + DEFAULT_HEURE_DEBUT + "," + UPDATED_HEURE_DEBUT,
            "heureDebut.in=" + UPDATED_HEURE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByHeureDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where heureDebut is not null
        defaultAlerteApprocheFiltering("heureDebut.specified=true", "heureDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByHeureDebutContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where heureDebut contains
        defaultAlerteApprocheFiltering("heureDebut.contains=" + DEFAULT_HEURE_DEBUT, "heureDebut.contains=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByHeureDebutNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where heureDebut does not contain
        defaultAlerteApprocheFiltering(
            "heureDebut.doesNotContain=" + UPDATED_HEURE_DEBUT,
            "heureDebut.doesNotContain=" + DEFAULT_HEURE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByHeureFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where heureFin equals to
        defaultAlerteApprocheFiltering("heureFin.equals=" + DEFAULT_HEURE_FIN, "heureFin.equals=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByHeureFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where heureFin in
        defaultAlerteApprocheFiltering("heureFin.in=" + DEFAULT_HEURE_FIN + "," + UPDATED_HEURE_FIN, "heureFin.in=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByHeureFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where heureFin is not null
        defaultAlerteApprocheFiltering("heureFin.specified=true", "heureFin.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByHeureFinContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where heureFin contains
        defaultAlerteApprocheFiltering("heureFin.contains=" + DEFAULT_HEURE_FIN, "heureFin.contains=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByHeureFinNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where heureFin does not contain
        defaultAlerteApprocheFiltering("heureFin.doesNotContain=" + UPDATED_HEURE_FIN, "heureFin.doesNotContain=" + DEFAULT_HEURE_FIN);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where statut equals to
        defaultAlerteApprocheFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where statut in
        defaultAlerteApprocheFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where statut is not null
        defaultAlerteApprocheFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where dateCreation equals to
        defaultAlerteApprocheFiltering("dateCreation.equals=" + DEFAULT_DATE_CREATION, "dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where dateCreation in
        defaultAlerteApprocheFiltering(
            "dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION,
            "dateCreation.in=" + UPDATED_DATE_CREATION
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where dateCreation is not null
        defaultAlerteApprocheFiltering("dateCreation.specified=true", "dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByDateModificationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where dateModification equals to
        defaultAlerteApprocheFiltering(
            "dateModification.equals=" + DEFAULT_DATE_MODIFICATION,
            "dateModification.equals=" + UPDATED_DATE_MODIFICATION
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByDateModificationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where dateModification in
        defaultAlerteApprocheFiltering(
            "dateModification.in=" + DEFAULT_DATE_MODIFICATION + "," + UPDATED_DATE_MODIFICATION,
            "dateModification.in=" + UPDATED_DATE_MODIFICATION
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByDateModificationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where dateModification is not null
        defaultAlerteApprocheFiltering("dateModification.specified=true", "dateModification.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByDernierDeclenchementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where dernierDeclenchement equals to
        defaultAlerteApprocheFiltering(
            "dernierDeclenchement.equals=" + DEFAULT_DERNIER_DECLENCHEMENT,
            "dernierDeclenchement.equals=" + UPDATED_DERNIER_DECLENCHEMENT
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByDernierDeclenchementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where dernierDeclenchement in
        defaultAlerteApprocheFiltering(
            "dernierDeclenchement.in=" + DEFAULT_DERNIER_DECLENCHEMENT + "," + UPDATED_DERNIER_DECLENCHEMENT,
            "dernierDeclenchement.in=" + UPDATED_DERNIER_DECLENCHEMENT
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByDernierDeclenchementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where dernierDeclenchement is not null
        defaultAlerteApprocheFiltering("dernierDeclenchement.specified=true", "dernierDeclenchement.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByNombreDeclenchementsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where nombreDeclenchements equals to
        defaultAlerteApprocheFiltering(
            "nombreDeclenchements.equals=" + DEFAULT_NOMBRE_DECLENCHEMENTS,
            "nombreDeclenchements.equals=" + UPDATED_NOMBRE_DECLENCHEMENTS
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByNombreDeclenchementsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where nombreDeclenchements in
        defaultAlerteApprocheFiltering(
            "nombreDeclenchements.in=" + DEFAULT_NOMBRE_DECLENCHEMENTS + "," + UPDATED_NOMBRE_DECLENCHEMENTS,
            "nombreDeclenchements.in=" + UPDATED_NOMBRE_DECLENCHEMENTS
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByNombreDeclenchementsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where nombreDeclenchements is not null
        defaultAlerteApprocheFiltering("nombreDeclenchements.specified=true", "nombreDeclenchements.specified=false");
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByNombreDeclenchementsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where nombreDeclenchements is greater than or equal to
        defaultAlerteApprocheFiltering(
            "nombreDeclenchements.greaterThanOrEqual=" + DEFAULT_NOMBRE_DECLENCHEMENTS,
            "nombreDeclenchements.greaterThanOrEqual=" + UPDATED_NOMBRE_DECLENCHEMENTS
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByNombreDeclenchementsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where nombreDeclenchements is less than or equal to
        defaultAlerteApprocheFiltering(
            "nombreDeclenchements.lessThanOrEqual=" + DEFAULT_NOMBRE_DECLENCHEMENTS,
            "nombreDeclenchements.lessThanOrEqual=" + SMALLER_NOMBRE_DECLENCHEMENTS
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByNombreDeclenchementsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where nombreDeclenchements is less than
        defaultAlerteApprocheFiltering(
            "nombreDeclenchements.lessThan=" + UPDATED_NOMBRE_DECLENCHEMENTS,
            "nombreDeclenchements.lessThan=" + DEFAULT_NOMBRE_DECLENCHEMENTS
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByNombreDeclenchementsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        // Get all the alerteApprocheList where nombreDeclenchements is greater than
        defaultAlerteApprocheFiltering(
            "nombreDeclenchements.greaterThan=" + SMALLER_NOMBRE_DECLENCHEMENTS,
            "nombreDeclenchements.greaterThan=" + DEFAULT_NOMBRE_DECLENCHEMENTS
        );
    }

    @Test
    @Transactional
    void getAllAlerteApprochesByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            alerteApprocheRepository.saveAndFlush(alerteApproche);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        alerteApproche.setUtilisateur(utilisateur);
        alerteApprocheRepository.saveAndFlush(alerteApproche);
        Long utilisateurId = utilisateur.getId();
        // Get all the alerteApprocheList where utilisateur equals to utilisateurId
        defaultAlerteApprocheShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the alerteApprocheList where utilisateur equals to (utilisateurId + 1)
        defaultAlerteApprocheShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultAlerteApprocheFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlerteApprocheShouldBeFound(shouldBeFound);
        defaultAlerteApprocheShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlerteApprocheShouldBeFound(String filter) throws Exception {
        restAlerteApprocheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alerteApproche.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].seuilDistance").value(hasItem(DEFAULT_SEUIL_DISTANCE)))
            .andExpect(jsonPath("$.[*].seuilTemps").value(hasItem(DEFAULT_SEUIL_TEMPS)))
            .andExpect(jsonPath("$.[*].typeSeuil").value(hasItem(DEFAULT_TYPE_SEUIL.toString())))
            .andExpect(jsonPath("$.[*].joursActivation").value(hasItem(DEFAULT_JOURS_ACTIVATION)))
            .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(DEFAULT_HEURE_DEBUT)))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateModification").value(hasItem(DEFAULT_DATE_MODIFICATION.toString())))
            .andExpect(jsonPath("$.[*].dernierDeclenchement").value(hasItem(DEFAULT_DERNIER_DECLENCHEMENT.toString())))
            .andExpect(jsonPath("$.[*].nombreDeclenchements").value(hasItem(DEFAULT_NOMBRE_DECLENCHEMENTS)));

        // Check, that the count call also returns 1
        restAlerteApprocheMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlerteApprocheShouldNotBeFound(String filter) throws Exception {
        restAlerteApprocheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlerteApprocheMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlerteApproche() throws Exception {
        // Get the alerteApproche
        restAlerteApprocheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlerteApproche() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerteApproche
        AlerteApproche updatedAlerteApproche = alerteApprocheRepository.findById(alerteApproche.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlerteApproche are not directly saved in db
        em.detach(updatedAlerteApproche);
        updatedAlerteApproche
            .nom(UPDATED_NOM)
            .seuilDistance(UPDATED_SEUIL_DISTANCE)
            .seuilTemps(UPDATED_SEUIL_TEMPS)
            .typeSeuil(UPDATED_TYPE_SEUIL)
            .joursActivation(UPDATED_JOURS_ACTIVATION)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .statut(UPDATED_STATUT)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dernierDeclenchement(UPDATED_DERNIER_DECLENCHEMENT)
            .nombreDeclenchements(UPDATED_NOMBRE_DECLENCHEMENTS);
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(updatedAlerteApproche);

        restAlerteApprocheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alerteApprocheDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alerteApprocheDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlerteApproche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlerteApprocheToMatchAllProperties(updatedAlerteApproche);
    }

    @Test
    @Transactional
    void putNonExistingAlerteApproche() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteApproche.setId(longCount.incrementAndGet());

        // Create the AlerteApproche
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(alerteApproche);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlerteApprocheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alerteApprocheDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alerteApprocheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlerteApproche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlerteApproche() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteApproche.setId(longCount.incrementAndGet());

        // Create the AlerteApproche
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(alerteApproche);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteApprocheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alerteApprocheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlerteApproche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlerteApproche() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteApproche.setId(longCount.incrementAndGet());

        // Create the AlerteApproche
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(alerteApproche);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteApprocheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alerteApprocheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlerteApproche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlerteApprocheWithPatch() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerteApproche using partial update
        AlerteApproche partialUpdatedAlerteApproche = new AlerteApproche();
        partialUpdatedAlerteApproche.setId(alerteApproche.getId());

        partialUpdatedAlerteApproche
            .seuilTemps(UPDATED_SEUIL_TEMPS)
            .dernierDeclenchement(UPDATED_DERNIER_DECLENCHEMENT)
            .nombreDeclenchements(UPDATED_NOMBRE_DECLENCHEMENTS);

        restAlerteApprocheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlerteApproche.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlerteApproche))
            )
            .andExpect(status().isOk());

        // Validate the AlerteApproche in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlerteApprocheUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlerteApproche, alerteApproche),
            getPersistedAlerteApproche(alerteApproche)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlerteApprocheWithPatch() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alerteApproche using partial update
        AlerteApproche partialUpdatedAlerteApproche = new AlerteApproche();
        partialUpdatedAlerteApproche.setId(alerteApproche.getId());

        partialUpdatedAlerteApproche
            .nom(UPDATED_NOM)
            .seuilDistance(UPDATED_SEUIL_DISTANCE)
            .seuilTemps(UPDATED_SEUIL_TEMPS)
            .typeSeuil(UPDATED_TYPE_SEUIL)
            .joursActivation(UPDATED_JOURS_ACTIVATION)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .statut(UPDATED_STATUT)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateModification(UPDATED_DATE_MODIFICATION)
            .dernierDeclenchement(UPDATED_DERNIER_DECLENCHEMENT)
            .nombreDeclenchements(UPDATED_NOMBRE_DECLENCHEMENTS);

        restAlerteApprocheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlerteApproche.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlerteApproche))
            )
            .andExpect(status().isOk());

        // Validate the AlerteApproche in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlerteApprocheUpdatableFieldsEquals(partialUpdatedAlerteApproche, getPersistedAlerteApproche(partialUpdatedAlerteApproche));
    }

    @Test
    @Transactional
    void patchNonExistingAlerteApproche() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteApproche.setId(longCount.incrementAndGet());

        // Create the AlerteApproche
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(alerteApproche);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlerteApprocheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alerteApprocheDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alerteApprocheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlerteApproche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlerteApproche() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteApproche.setId(longCount.incrementAndGet());

        // Create the AlerteApproche
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(alerteApproche);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteApprocheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alerteApprocheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlerteApproche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlerteApproche() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alerteApproche.setId(longCount.incrementAndGet());

        // Create the AlerteApproche
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(alerteApproche);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlerteApprocheMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alerteApprocheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlerteApproche in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlerteApproche() throws Exception {
        // Initialize the database
        insertedAlerteApproche = alerteApprocheRepository.saveAndFlush(alerteApproche);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alerteApproche
        restAlerteApprocheMockMvc
            .perform(delete(ENTITY_API_URL_ID, alerteApproche.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alerteApprocheRepository.count();
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

    protected AlerteApproche getPersistedAlerteApproche(AlerteApproche alerteApproche) {
        return alerteApprocheRepository.findById(alerteApproche.getId()).orElseThrow();
    }

    protected void assertPersistedAlerteApprocheToMatchAllProperties(AlerteApproche expectedAlerteApproche) {
        assertAlerteApprocheAllPropertiesEquals(expectedAlerteApproche, getPersistedAlerteApproche(expectedAlerteApproche));
    }

    protected void assertPersistedAlerteApprocheToMatchUpdatableProperties(AlerteApproche expectedAlerteApproche) {
        assertAlerteApprocheAllUpdatablePropertiesEquals(expectedAlerteApproche, getPersistedAlerteApproche(expectedAlerteApproche));
    }
}
