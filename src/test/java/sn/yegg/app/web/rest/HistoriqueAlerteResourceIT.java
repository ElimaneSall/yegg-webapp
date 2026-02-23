package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.HistoriqueAlerteAsserts.*;
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
import sn.yegg.app.domain.Bus;
import sn.yegg.app.domain.HistoriqueAlerte;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.domain.enumeration.ThresholdType;
import sn.yegg.app.repository.HistoriqueAlerteRepository;
import sn.yegg.app.service.dto.HistoriqueAlerteDTO;
import sn.yegg.app.service.mapper.HistoriqueAlerteMapper;

/**
 * Integration tests for the {@link HistoriqueAlerteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoriqueAlerteResourceIT {

    private static final Instant DEFAULT_DATE_DECLENCHEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DECLENCHEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BUS_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_BUS_NUMERO = "BBBBBBBBBB";

    private static final Integer DEFAULT_DISTANCE_REELLE = 1;
    private static final Integer UPDATED_DISTANCE_REELLE = 2;
    private static final Integer SMALLER_DISTANCE_REELLE = 1 - 1;

    private static final Integer DEFAULT_TEMPS_REEL = 1;
    private static final Integer UPDATED_TEMPS_REEL = 2;
    private static final Integer SMALLER_TEMPS_REEL = 1 - 1;

    private static final ThresholdType DEFAULT_TYPE_DECLENCHEMENT = ThresholdType.DISTANCE;
    private static final ThresholdType UPDATED_TYPE_DECLENCHEMENT = ThresholdType.TIME;

    private static final Boolean DEFAULT_NOTIFICATION_ENVOYEE = false;
    private static final Boolean UPDATED_NOTIFICATION_ENVOYEE = true;

    private static final Instant DEFAULT_DATE_LECTURE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_LECTURE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/historique-alertes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoriqueAlerteRepository historiqueAlerteRepository;

    @Autowired
    private HistoriqueAlerteMapper historiqueAlerteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueAlerteMockMvc;

    private HistoriqueAlerte historiqueAlerte;

    private HistoriqueAlerte insertedHistoriqueAlerte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueAlerte createEntity() {
        return new HistoriqueAlerte()
            .dateDeclenchement(DEFAULT_DATE_DECLENCHEMENT)
            .busNumero(DEFAULT_BUS_NUMERO)
            .distanceReelle(DEFAULT_DISTANCE_REELLE)
            .tempsReel(DEFAULT_TEMPS_REEL)
            .typeDeclenchement(DEFAULT_TYPE_DECLENCHEMENT)
            .notificationEnvoyee(DEFAULT_NOTIFICATION_ENVOYEE)
            .dateLecture(DEFAULT_DATE_LECTURE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueAlerte createUpdatedEntity() {
        return new HistoriqueAlerte()
            .dateDeclenchement(UPDATED_DATE_DECLENCHEMENT)
            .busNumero(UPDATED_BUS_NUMERO)
            .distanceReelle(UPDATED_DISTANCE_REELLE)
            .tempsReel(UPDATED_TEMPS_REEL)
            .typeDeclenchement(UPDATED_TYPE_DECLENCHEMENT)
            .notificationEnvoyee(UPDATED_NOTIFICATION_ENVOYEE)
            .dateLecture(UPDATED_DATE_LECTURE);
    }

    @BeforeEach
    void initTest() {
        historiqueAlerte = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHistoriqueAlerte != null) {
            historiqueAlerteRepository.delete(insertedHistoriqueAlerte);
            insertedHistoriqueAlerte = null;
        }
    }

    @Test
    @Transactional
    void createHistoriqueAlerte() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HistoriqueAlerte
        HistoriqueAlerteDTO historiqueAlerteDTO = historiqueAlerteMapper.toDto(historiqueAlerte);
        var returnedHistoriqueAlerteDTO = om.readValue(
            restHistoriqueAlerteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueAlerteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HistoriqueAlerteDTO.class
        );

        // Validate the HistoriqueAlerte in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistoriqueAlerte = historiqueAlerteMapper.toEntity(returnedHistoriqueAlerteDTO);
        assertHistoriqueAlerteUpdatableFieldsEquals(returnedHistoriqueAlerte, getPersistedHistoriqueAlerte(returnedHistoriqueAlerte));

        insertedHistoriqueAlerte = returnedHistoriqueAlerte;
    }

    @Test
    @Transactional
    void createHistoriqueAlerteWithExistingId() throws Exception {
        // Create the HistoriqueAlerte with an existing ID
        historiqueAlerte.setId(1L);
        HistoriqueAlerteDTO historiqueAlerteDTO = historiqueAlerteMapper.toDto(historiqueAlerte);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriqueAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueAlerteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAlerte in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateDeclenchementIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiqueAlerte.setDateDeclenchement(null);

        // Create the HistoriqueAlerte, which fails.
        HistoriqueAlerteDTO historiqueAlerteDTO = historiqueAlerteMapper.toDto(historiqueAlerte);

        restHistoriqueAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueAlerteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotificationEnvoyeeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiqueAlerte.setNotificationEnvoyee(null);

        // Create the HistoriqueAlerte, which fails.
        HistoriqueAlerteDTO historiqueAlerteDTO = historiqueAlerteMapper.toDto(historiqueAlerte);

        restHistoriqueAlerteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueAlerteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertes() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList
        restHistoriqueAlerteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueAlerte.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDeclenchement").value(hasItem(DEFAULT_DATE_DECLENCHEMENT.toString())))
            .andExpect(jsonPath("$.[*].busNumero").value(hasItem(DEFAULT_BUS_NUMERO)))
            .andExpect(jsonPath("$.[*].distanceReelle").value(hasItem(DEFAULT_DISTANCE_REELLE)))
            .andExpect(jsonPath("$.[*].tempsReel").value(hasItem(DEFAULT_TEMPS_REEL)))
            .andExpect(jsonPath("$.[*].typeDeclenchement").value(hasItem(DEFAULT_TYPE_DECLENCHEMENT.toString())))
            .andExpect(jsonPath("$.[*].notificationEnvoyee").value(hasItem(DEFAULT_NOTIFICATION_ENVOYEE)))
            .andExpect(jsonPath("$.[*].dateLecture").value(hasItem(DEFAULT_DATE_LECTURE.toString())));
    }

    @Test
    @Transactional
    void getHistoriqueAlerte() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get the historiqueAlerte
        restHistoriqueAlerteMockMvc
            .perform(get(ENTITY_API_URL_ID, historiqueAlerte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiqueAlerte.getId().intValue()))
            .andExpect(jsonPath("$.dateDeclenchement").value(DEFAULT_DATE_DECLENCHEMENT.toString()))
            .andExpect(jsonPath("$.busNumero").value(DEFAULT_BUS_NUMERO))
            .andExpect(jsonPath("$.distanceReelle").value(DEFAULT_DISTANCE_REELLE))
            .andExpect(jsonPath("$.tempsReel").value(DEFAULT_TEMPS_REEL))
            .andExpect(jsonPath("$.typeDeclenchement").value(DEFAULT_TYPE_DECLENCHEMENT.toString()))
            .andExpect(jsonPath("$.notificationEnvoyee").value(DEFAULT_NOTIFICATION_ENVOYEE))
            .andExpect(jsonPath("$.dateLecture").value(DEFAULT_DATE_LECTURE.toString()));
    }

    @Test
    @Transactional
    void getHistoriqueAlertesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        Long id = historiqueAlerte.getId();

        defaultHistoriqueAlerteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHistoriqueAlerteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHistoriqueAlerteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDateDeclenchementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where dateDeclenchement equals to
        defaultHistoriqueAlerteFiltering(
            "dateDeclenchement.equals=" + DEFAULT_DATE_DECLENCHEMENT,
            "dateDeclenchement.equals=" + UPDATED_DATE_DECLENCHEMENT
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDateDeclenchementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where dateDeclenchement in
        defaultHistoriqueAlerteFiltering(
            "dateDeclenchement.in=" + DEFAULT_DATE_DECLENCHEMENT + "," + UPDATED_DATE_DECLENCHEMENT,
            "dateDeclenchement.in=" + UPDATED_DATE_DECLENCHEMENT
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDateDeclenchementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where dateDeclenchement is not null
        defaultHistoriqueAlerteFiltering("dateDeclenchement.specified=true", "dateDeclenchement.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByBusNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where busNumero equals to
        defaultHistoriqueAlerteFiltering("busNumero.equals=" + DEFAULT_BUS_NUMERO, "busNumero.equals=" + UPDATED_BUS_NUMERO);
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByBusNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where busNumero in
        defaultHistoriqueAlerteFiltering(
            "busNumero.in=" + DEFAULT_BUS_NUMERO + "," + UPDATED_BUS_NUMERO,
            "busNumero.in=" + UPDATED_BUS_NUMERO
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByBusNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where busNumero is not null
        defaultHistoriqueAlerteFiltering("busNumero.specified=true", "busNumero.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByBusNumeroContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where busNumero contains
        defaultHistoriqueAlerteFiltering("busNumero.contains=" + DEFAULT_BUS_NUMERO, "busNumero.contains=" + UPDATED_BUS_NUMERO);
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByBusNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where busNumero does not contain
        defaultHistoriqueAlerteFiltering(
            "busNumero.doesNotContain=" + UPDATED_BUS_NUMERO,
            "busNumero.doesNotContain=" + DEFAULT_BUS_NUMERO
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDistanceReelleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where distanceReelle equals to
        defaultHistoriqueAlerteFiltering(
            "distanceReelle.equals=" + DEFAULT_DISTANCE_REELLE,
            "distanceReelle.equals=" + UPDATED_DISTANCE_REELLE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDistanceReelleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where distanceReelle in
        defaultHistoriqueAlerteFiltering(
            "distanceReelle.in=" + DEFAULT_DISTANCE_REELLE + "," + UPDATED_DISTANCE_REELLE,
            "distanceReelle.in=" + UPDATED_DISTANCE_REELLE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDistanceReelleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where distanceReelle is not null
        defaultHistoriqueAlerteFiltering("distanceReelle.specified=true", "distanceReelle.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDistanceReelleIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where distanceReelle is greater than or equal to
        defaultHistoriqueAlerteFiltering(
            "distanceReelle.greaterThanOrEqual=" + DEFAULT_DISTANCE_REELLE,
            "distanceReelle.greaterThanOrEqual=" + UPDATED_DISTANCE_REELLE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDistanceReelleIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where distanceReelle is less than or equal to
        defaultHistoriqueAlerteFiltering(
            "distanceReelle.lessThanOrEqual=" + DEFAULT_DISTANCE_REELLE,
            "distanceReelle.lessThanOrEqual=" + SMALLER_DISTANCE_REELLE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDistanceReelleIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where distanceReelle is less than
        defaultHistoriqueAlerteFiltering(
            "distanceReelle.lessThan=" + UPDATED_DISTANCE_REELLE,
            "distanceReelle.lessThan=" + DEFAULT_DISTANCE_REELLE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDistanceReelleIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where distanceReelle is greater than
        defaultHistoriqueAlerteFiltering(
            "distanceReelle.greaterThan=" + SMALLER_DISTANCE_REELLE,
            "distanceReelle.greaterThan=" + DEFAULT_DISTANCE_REELLE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByTempsReelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where tempsReel equals to
        defaultHistoriqueAlerteFiltering("tempsReel.equals=" + DEFAULT_TEMPS_REEL, "tempsReel.equals=" + UPDATED_TEMPS_REEL);
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByTempsReelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where tempsReel in
        defaultHistoriqueAlerteFiltering(
            "tempsReel.in=" + DEFAULT_TEMPS_REEL + "," + UPDATED_TEMPS_REEL,
            "tempsReel.in=" + UPDATED_TEMPS_REEL
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByTempsReelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where tempsReel is not null
        defaultHistoriqueAlerteFiltering("tempsReel.specified=true", "tempsReel.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByTempsReelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where tempsReel is greater than or equal to
        defaultHistoriqueAlerteFiltering(
            "tempsReel.greaterThanOrEqual=" + DEFAULT_TEMPS_REEL,
            "tempsReel.greaterThanOrEqual=" + UPDATED_TEMPS_REEL
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByTempsReelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where tempsReel is less than or equal to
        defaultHistoriqueAlerteFiltering(
            "tempsReel.lessThanOrEqual=" + DEFAULT_TEMPS_REEL,
            "tempsReel.lessThanOrEqual=" + SMALLER_TEMPS_REEL
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByTempsReelIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where tempsReel is less than
        defaultHistoriqueAlerteFiltering("tempsReel.lessThan=" + UPDATED_TEMPS_REEL, "tempsReel.lessThan=" + DEFAULT_TEMPS_REEL);
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByTempsReelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where tempsReel is greater than
        defaultHistoriqueAlerteFiltering("tempsReel.greaterThan=" + SMALLER_TEMPS_REEL, "tempsReel.greaterThan=" + DEFAULT_TEMPS_REEL);
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByTypeDeclenchementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where typeDeclenchement equals to
        defaultHistoriqueAlerteFiltering(
            "typeDeclenchement.equals=" + DEFAULT_TYPE_DECLENCHEMENT,
            "typeDeclenchement.equals=" + UPDATED_TYPE_DECLENCHEMENT
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByTypeDeclenchementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where typeDeclenchement in
        defaultHistoriqueAlerteFiltering(
            "typeDeclenchement.in=" + DEFAULT_TYPE_DECLENCHEMENT + "," + UPDATED_TYPE_DECLENCHEMENT,
            "typeDeclenchement.in=" + UPDATED_TYPE_DECLENCHEMENT
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByTypeDeclenchementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where typeDeclenchement is not null
        defaultHistoriqueAlerteFiltering("typeDeclenchement.specified=true", "typeDeclenchement.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByNotificationEnvoyeeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where notificationEnvoyee equals to
        defaultHistoriqueAlerteFiltering(
            "notificationEnvoyee.equals=" + DEFAULT_NOTIFICATION_ENVOYEE,
            "notificationEnvoyee.equals=" + UPDATED_NOTIFICATION_ENVOYEE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByNotificationEnvoyeeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where notificationEnvoyee in
        defaultHistoriqueAlerteFiltering(
            "notificationEnvoyee.in=" + DEFAULT_NOTIFICATION_ENVOYEE + "," + UPDATED_NOTIFICATION_ENVOYEE,
            "notificationEnvoyee.in=" + UPDATED_NOTIFICATION_ENVOYEE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByNotificationEnvoyeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where notificationEnvoyee is not null
        defaultHistoriqueAlerteFiltering("notificationEnvoyee.specified=true", "notificationEnvoyee.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDateLectureIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where dateLecture equals to
        defaultHistoriqueAlerteFiltering("dateLecture.equals=" + DEFAULT_DATE_LECTURE, "dateLecture.equals=" + UPDATED_DATE_LECTURE);
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDateLectureIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where dateLecture in
        defaultHistoriqueAlerteFiltering(
            "dateLecture.in=" + DEFAULT_DATE_LECTURE + "," + UPDATED_DATE_LECTURE,
            "dateLecture.in=" + UPDATED_DATE_LECTURE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByDateLectureIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        // Get all the historiqueAlerteList where dateLecture is not null
        defaultHistoriqueAlerteFiltering("dateLecture.specified=true", "dateLecture.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByBusIsEqualToSomething() throws Exception {
        Bus bus;
        if (TestUtil.findAll(em, Bus.class).isEmpty()) {
            historiqueAlerteRepository.saveAndFlush(historiqueAlerte);
            bus = BusResourceIT.createEntity();
        } else {
            bus = TestUtil.findAll(em, Bus.class).get(0);
        }
        em.persist(bus);
        em.flush();
        historiqueAlerte.setBus(bus);
        historiqueAlerteRepository.saveAndFlush(historiqueAlerte);
        Long busId = bus.getId();
        // Get all the historiqueAlerteList where bus equals to busId
        defaultHistoriqueAlerteShouldBeFound("busId.equals=" + busId);

        // Get all the historiqueAlerteList where bus equals to (busId + 1)
        defaultHistoriqueAlerteShouldNotBeFound("busId.equals=" + (busId + 1));
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByAlerteApprocheIsEqualToSomething() throws Exception {
        AlerteApproche alerteApproche;
        if (TestUtil.findAll(em, AlerteApproche.class).isEmpty()) {
            historiqueAlerteRepository.saveAndFlush(historiqueAlerte);
            alerteApproche = AlerteApprocheResourceIT.createEntity();
        } else {
            alerteApproche = TestUtil.findAll(em, AlerteApproche.class).get(0);
        }
        em.persist(alerteApproche);
        em.flush();
        historiqueAlerte.setAlerteApproche(alerteApproche);
        historiqueAlerteRepository.saveAndFlush(historiqueAlerte);
        Long alerteApprocheId = alerteApproche.getId();
        // Get all the historiqueAlerteList where alerteApproche equals to alerteApprocheId
        defaultHistoriqueAlerteShouldBeFound("alerteApprocheId.equals=" + alerteApprocheId);

        // Get all the historiqueAlerteList where alerteApproche equals to (alerteApprocheId + 1)
        defaultHistoriqueAlerteShouldNotBeFound("alerteApprocheId.equals=" + (alerteApprocheId + 1));
    }

    @Test
    @Transactional
    void getAllHistoriqueAlertesByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            historiqueAlerteRepository.saveAndFlush(historiqueAlerte);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        historiqueAlerte.setUtilisateur(utilisateur);
        historiqueAlerteRepository.saveAndFlush(historiqueAlerte);
        Long utilisateurId = utilisateur.getId();
        // Get all the historiqueAlerteList where utilisateur equals to utilisateurId
        defaultHistoriqueAlerteShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the historiqueAlerteList where utilisateur equals to (utilisateurId + 1)
        defaultHistoriqueAlerteShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultHistoriqueAlerteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHistoriqueAlerteShouldBeFound(shouldBeFound);
        defaultHistoriqueAlerteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoriqueAlerteShouldBeFound(String filter) throws Exception {
        restHistoriqueAlerteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueAlerte.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDeclenchement").value(hasItem(DEFAULT_DATE_DECLENCHEMENT.toString())))
            .andExpect(jsonPath("$.[*].busNumero").value(hasItem(DEFAULT_BUS_NUMERO)))
            .andExpect(jsonPath("$.[*].distanceReelle").value(hasItem(DEFAULT_DISTANCE_REELLE)))
            .andExpect(jsonPath("$.[*].tempsReel").value(hasItem(DEFAULT_TEMPS_REEL)))
            .andExpect(jsonPath("$.[*].typeDeclenchement").value(hasItem(DEFAULT_TYPE_DECLENCHEMENT.toString())))
            .andExpect(jsonPath("$.[*].notificationEnvoyee").value(hasItem(DEFAULT_NOTIFICATION_ENVOYEE)))
            .andExpect(jsonPath("$.[*].dateLecture").value(hasItem(DEFAULT_DATE_LECTURE.toString())));

        // Check, that the count call also returns 1
        restHistoriqueAlerteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoriqueAlerteShouldNotBeFound(String filter) throws Exception {
        restHistoriqueAlerteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoriqueAlerteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHistoriqueAlerte() throws Exception {
        // Get the historiqueAlerte
        restHistoriqueAlerteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoriqueAlerte() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueAlerte
        HistoriqueAlerte updatedHistoriqueAlerte = historiqueAlerteRepository.findById(historiqueAlerte.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHistoriqueAlerte are not directly saved in db
        em.detach(updatedHistoriqueAlerte);
        updatedHistoriqueAlerte
            .dateDeclenchement(UPDATED_DATE_DECLENCHEMENT)
            .busNumero(UPDATED_BUS_NUMERO)
            .distanceReelle(UPDATED_DISTANCE_REELLE)
            .tempsReel(UPDATED_TEMPS_REEL)
            .typeDeclenchement(UPDATED_TYPE_DECLENCHEMENT)
            .notificationEnvoyee(UPDATED_NOTIFICATION_ENVOYEE)
            .dateLecture(UPDATED_DATE_LECTURE);
        HistoriqueAlerteDTO historiqueAlerteDTO = historiqueAlerteMapper.toDto(updatedHistoriqueAlerte);

        restHistoriqueAlerteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueAlerteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueAlerteDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueAlerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistoriqueAlerteToMatchAllProperties(updatedHistoriqueAlerte);
    }

    @Test
    @Transactional
    void putNonExistingHistoriqueAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAlerte.setId(longCount.incrementAndGet());

        // Create the HistoriqueAlerte
        HistoriqueAlerteDTO historiqueAlerteDTO = historiqueAlerteMapper.toDto(historiqueAlerte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueAlerteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueAlerteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueAlerteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAlerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoriqueAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAlerte.setId(longCount.incrementAndGet());

        // Create the HistoriqueAlerte
        HistoriqueAlerteDTO historiqueAlerteDTO = historiqueAlerteMapper.toDto(historiqueAlerte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueAlerteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueAlerteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAlerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoriqueAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAlerte.setId(longCount.incrementAndGet());

        // Create the HistoriqueAlerte
        HistoriqueAlerteDTO historiqueAlerteDTO = historiqueAlerteMapper.toDto(historiqueAlerte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueAlerteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueAlerteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriqueAlerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriqueAlerteWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueAlerte using partial update
        HistoriqueAlerte partialUpdatedHistoriqueAlerte = new HistoriqueAlerte();
        partialUpdatedHistoriqueAlerte.setId(historiqueAlerte.getId());

        partialUpdatedHistoriqueAlerte.distanceReelle(UPDATED_DISTANCE_REELLE).typeDeclenchement(UPDATED_TYPE_DECLENCHEMENT);

        restHistoriqueAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueAlerte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriqueAlerte))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueAlerte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriqueAlerteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistoriqueAlerte, historiqueAlerte),
            getPersistedHistoriqueAlerte(historiqueAlerte)
        );
    }

    @Test
    @Transactional
    void fullUpdateHistoriqueAlerteWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueAlerte using partial update
        HistoriqueAlerte partialUpdatedHistoriqueAlerte = new HistoriqueAlerte();
        partialUpdatedHistoriqueAlerte.setId(historiqueAlerte.getId());

        partialUpdatedHistoriqueAlerte
            .dateDeclenchement(UPDATED_DATE_DECLENCHEMENT)
            .busNumero(UPDATED_BUS_NUMERO)
            .distanceReelle(UPDATED_DISTANCE_REELLE)
            .tempsReel(UPDATED_TEMPS_REEL)
            .typeDeclenchement(UPDATED_TYPE_DECLENCHEMENT)
            .notificationEnvoyee(UPDATED_NOTIFICATION_ENVOYEE)
            .dateLecture(UPDATED_DATE_LECTURE);

        restHistoriqueAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueAlerte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriqueAlerte))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueAlerte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriqueAlerteUpdatableFieldsEquals(
            partialUpdatedHistoriqueAlerte,
            getPersistedHistoriqueAlerte(partialUpdatedHistoriqueAlerte)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHistoriqueAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAlerte.setId(longCount.incrementAndGet());

        // Create the HistoriqueAlerte
        HistoriqueAlerteDTO historiqueAlerteDTO = historiqueAlerteMapper.toDto(historiqueAlerte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historiqueAlerteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueAlerteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAlerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoriqueAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAlerte.setId(longCount.incrementAndGet());

        // Create the HistoriqueAlerte
        HistoriqueAlerteDTO historiqueAlerteDTO = historiqueAlerteMapper.toDto(historiqueAlerte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueAlerteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueAlerteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAlerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoriqueAlerte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAlerte.setId(longCount.incrementAndGet());

        // Create the HistoriqueAlerte
        HistoriqueAlerteDTO historiqueAlerteDTO = historiqueAlerteMapper.toDto(historiqueAlerte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueAlerteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(historiqueAlerteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriqueAlerte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoriqueAlerte() throws Exception {
        // Initialize the database
        insertedHistoriqueAlerte = historiqueAlerteRepository.saveAndFlush(historiqueAlerte);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historiqueAlerte
        restHistoriqueAlerteMockMvc
            .perform(delete(ENTITY_API_URL_ID, historiqueAlerte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historiqueAlerteRepository.count();
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

    protected HistoriqueAlerte getPersistedHistoriqueAlerte(HistoriqueAlerte historiqueAlerte) {
        return historiqueAlerteRepository.findById(historiqueAlerte.getId()).orElseThrow();
    }

    protected void assertPersistedHistoriqueAlerteToMatchAllProperties(HistoriqueAlerte expectedHistoriqueAlerte) {
        assertHistoriqueAlerteAllPropertiesEquals(expectedHistoriqueAlerte, getPersistedHistoriqueAlerte(expectedHistoriqueAlerte));
    }

    protected void assertPersistedHistoriqueAlerteToMatchUpdatableProperties(HistoriqueAlerte expectedHistoriqueAlerte) {
        assertHistoriqueAlerteAllUpdatablePropertiesEquals(
            expectedHistoriqueAlerte,
            getPersistedHistoriqueAlerte(expectedHistoriqueAlerte)
        );
    }
}
