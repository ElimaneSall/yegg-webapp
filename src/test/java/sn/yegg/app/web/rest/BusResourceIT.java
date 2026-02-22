package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.BusAsserts.*;
import static sn.yegg.app.web.rest.TestUtil.createUpdateProxyForBean;
import static sn.yegg.app.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
import sn.yegg.app.domain.Bus;
import sn.yegg.app.domain.Ligne;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.repository.BusRepository;
import sn.yegg.app.service.dto.BusDTO;
import sn.yegg.app.service.mapper.BusMapper;

/**
 * Integration tests for the {@link BusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BusResourceIT {

    private static final String DEFAULT_NUMERO_VEHICULE = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_VEHICULE = "BBBBBBBBBB";

    private static final String DEFAULT_PLAQUE = "AAAAAAAAAA";
    private static final String UPDATED_PLAQUE = "BBBBBBBBBB";

    private static final String DEFAULT_MODELE = "AAAAAAAAAA";
    private static final String UPDATED_MODELE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITE = 1;
    private static final Integer UPDATED_CAPACITE = 2;
    private static final Integer SMALLER_CAPACITE = 1 - 1;

    private static final Integer DEFAULT_ANNEE_FABRICATION = 1;
    private static final Integer UPDATED_ANNEE_FABRICATION = 2;
    private static final Integer SMALLER_ANNEE_FABRICATION = 1 - 1;

    private static final String DEFAULT_GPS_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_GPS_DEVICE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_GPS_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_GPS_STATUS = "BBBBBBBBBB";

    private static final Instant DEFAULT_GPS_LAST_PING = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_GPS_LAST_PING = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_GPS_BATTERY_LEVEL = 0;
    private static final Integer UPDATED_GPS_BATTERY_LEVEL = 1;
    private static final Integer SMALLER_GPS_BATTERY_LEVEL = 0 - 1;

    private static final BigDecimal DEFAULT_CURRENT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_LATITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_CURRENT_LATITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CURRENT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_LONGITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_CURRENT_LONGITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_CURRENT_VITESSE = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_VITESSE = new BigDecimal(2);
    private static final BigDecimal SMALLER_CURRENT_VITESSE = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_CURRENT_CAP = 0;
    private static final Integer UPDATED_CURRENT_CAP = 1;
    private static final Integer SMALLER_CURRENT_CAP = 0 - 1;

    private static final Instant DEFAULT_POSITION_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_POSITION_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/buses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private BusMapper busMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBusMockMvc;

    private Bus bus;

    private Bus insertedBus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bus createEntity() {
        return new Bus()
            .numeroVehicule(DEFAULT_NUMERO_VEHICULE)
            .plaque(DEFAULT_PLAQUE)
            .modele(DEFAULT_MODELE)
            .capacite(DEFAULT_CAPACITE)
            .anneeFabrication(DEFAULT_ANNEE_FABRICATION)
            .gpsDeviceId(DEFAULT_GPS_DEVICE_ID)
            .gpsStatus(DEFAULT_GPS_STATUS)
            .gpsLastPing(DEFAULT_GPS_LAST_PING)
            .gpsBatteryLevel(DEFAULT_GPS_BATTERY_LEVEL)
            .currentLatitude(DEFAULT_CURRENT_LATITUDE)
            .currentLongitude(DEFAULT_CURRENT_LONGITUDE)
            .currentVitesse(DEFAULT_CURRENT_VITESSE)
            .currentCap(DEFAULT_CURRENT_CAP)
            .positionUpdatedAt(DEFAULT_POSITION_UPDATED_AT)
            .statut(DEFAULT_STATUT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bus createUpdatedEntity() {
        return new Bus()
            .numeroVehicule(UPDATED_NUMERO_VEHICULE)
            .plaque(UPDATED_PLAQUE)
            .modele(UPDATED_MODELE)
            .capacite(UPDATED_CAPACITE)
            .anneeFabrication(UPDATED_ANNEE_FABRICATION)
            .gpsDeviceId(UPDATED_GPS_DEVICE_ID)
            .gpsStatus(UPDATED_GPS_STATUS)
            .gpsLastPing(UPDATED_GPS_LAST_PING)
            .gpsBatteryLevel(UPDATED_GPS_BATTERY_LEVEL)
            .currentLatitude(UPDATED_CURRENT_LATITUDE)
            .currentLongitude(UPDATED_CURRENT_LONGITUDE)
            .currentVitesse(UPDATED_CURRENT_VITESSE)
            .currentCap(UPDATED_CURRENT_CAP)
            .positionUpdatedAt(UPDATED_POSITION_UPDATED_AT)
            .statut(UPDATED_STATUT);
    }

    @BeforeEach
    void initTest() {
        bus = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBus != null) {
            busRepository.delete(insertedBus);
            insertedBus = null;
        }
    }

    @Test
    @Transactional
    void createBus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);
        var returnedBusDTO = om.readValue(
            restBusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(busDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BusDTO.class
        );

        // Validate the Bus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBus = busMapper.toEntity(returnedBusDTO);
        assertBusUpdatableFieldsEquals(returnedBus, getPersistedBus(returnedBus));

        insertedBus = returnedBus;
    }

    @Test
    @Transactional
    void createBusWithExistingId() throws Exception {
        // Create the Bus with an existing ID
        bus.setId(1L);
        BusDTO busDTO = busMapper.toDto(bus);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(busDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroVehiculeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bus.setNumeroVehicule(null);

        // Create the Bus, which fails.
        BusDTO busDTO = busMapper.toDto(bus);

        restBusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(busDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlaqueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bus.setPlaque(null);

        // Create the Bus, which fails.
        BusDTO busDTO = busMapper.toDto(bus);

        restBusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(busDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCapaciteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bus.setCapacite(null);

        // Create the Bus, which fails.
        BusDTO busDTO = busMapper.toDto(bus);

        restBusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(busDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bus.setStatut(null);

        // Create the Bus, which fails.
        BusDTO busDTO = busMapper.toDto(bus);

        restBusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(busDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBuses() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList
        restBusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bus.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroVehicule").value(hasItem(DEFAULT_NUMERO_VEHICULE)))
            .andExpect(jsonPath("$.[*].plaque").value(hasItem(DEFAULT_PLAQUE)))
            .andExpect(jsonPath("$.[*].modele").value(hasItem(DEFAULT_MODELE)))
            .andExpect(jsonPath("$.[*].capacite").value(hasItem(DEFAULT_CAPACITE)))
            .andExpect(jsonPath("$.[*].anneeFabrication").value(hasItem(DEFAULT_ANNEE_FABRICATION)))
            .andExpect(jsonPath("$.[*].gpsDeviceId").value(hasItem(DEFAULT_GPS_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].gpsStatus").value(hasItem(DEFAULT_GPS_STATUS)))
            .andExpect(jsonPath("$.[*].gpsLastPing").value(hasItem(DEFAULT_GPS_LAST_PING.toString())))
            .andExpect(jsonPath("$.[*].gpsBatteryLevel").value(hasItem(DEFAULT_GPS_BATTERY_LEVEL)))
            .andExpect(jsonPath("$.[*].currentLatitude").value(hasItem(sameNumber(DEFAULT_CURRENT_LATITUDE))))
            .andExpect(jsonPath("$.[*].currentLongitude").value(hasItem(sameNumber(DEFAULT_CURRENT_LONGITUDE))))
            .andExpect(jsonPath("$.[*].currentVitesse").value(hasItem(sameNumber(DEFAULT_CURRENT_VITESSE))))
            .andExpect(jsonPath("$.[*].currentCap").value(hasItem(DEFAULT_CURRENT_CAP)))
            .andExpect(jsonPath("$.[*].positionUpdatedAt").value(hasItem(DEFAULT_POSITION_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)));
    }

    @Test
    @Transactional
    void getBus() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get the bus
        restBusMockMvc
            .perform(get(ENTITY_API_URL_ID, bus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bus.getId().intValue()))
            .andExpect(jsonPath("$.numeroVehicule").value(DEFAULT_NUMERO_VEHICULE))
            .andExpect(jsonPath("$.plaque").value(DEFAULT_PLAQUE))
            .andExpect(jsonPath("$.modele").value(DEFAULT_MODELE))
            .andExpect(jsonPath("$.capacite").value(DEFAULT_CAPACITE))
            .andExpect(jsonPath("$.anneeFabrication").value(DEFAULT_ANNEE_FABRICATION))
            .andExpect(jsonPath("$.gpsDeviceId").value(DEFAULT_GPS_DEVICE_ID))
            .andExpect(jsonPath("$.gpsStatus").value(DEFAULT_GPS_STATUS))
            .andExpect(jsonPath("$.gpsLastPing").value(DEFAULT_GPS_LAST_PING.toString()))
            .andExpect(jsonPath("$.gpsBatteryLevel").value(DEFAULT_GPS_BATTERY_LEVEL))
            .andExpect(jsonPath("$.currentLatitude").value(sameNumber(DEFAULT_CURRENT_LATITUDE)))
            .andExpect(jsonPath("$.currentLongitude").value(sameNumber(DEFAULT_CURRENT_LONGITUDE)))
            .andExpect(jsonPath("$.currentVitesse").value(sameNumber(DEFAULT_CURRENT_VITESSE)))
            .andExpect(jsonPath("$.currentCap").value(DEFAULT_CURRENT_CAP))
            .andExpect(jsonPath("$.positionUpdatedAt").value(DEFAULT_POSITION_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT));
    }

    @Test
    @Transactional
    void getBusesByIdFiltering() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        Long id = bus.getId();

        defaultBusFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBusFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBusFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBusesByNumeroVehiculeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where numeroVehicule equals to
        defaultBusFiltering("numeroVehicule.equals=" + DEFAULT_NUMERO_VEHICULE, "numeroVehicule.equals=" + UPDATED_NUMERO_VEHICULE);
    }

    @Test
    @Transactional
    void getAllBusesByNumeroVehiculeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where numeroVehicule in
        defaultBusFiltering(
            "numeroVehicule.in=" + DEFAULT_NUMERO_VEHICULE + "," + UPDATED_NUMERO_VEHICULE,
            "numeroVehicule.in=" + UPDATED_NUMERO_VEHICULE
        );
    }

    @Test
    @Transactional
    void getAllBusesByNumeroVehiculeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where numeroVehicule is not null
        defaultBusFiltering("numeroVehicule.specified=true", "numeroVehicule.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByNumeroVehiculeContainsSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where numeroVehicule contains
        defaultBusFiltering("numeroVehicule.contains=" + DEFAULT_NUMERO_VEHICULE, "numeroVehicule.contains=" + UPDATED_NUMERO_VEHICULE);
    }

    @Test
    @Transactional
    void getAllBusesByNumeroVehiculeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where numeroVehicule does not contain
        defaultBusFiltering(
            "numeroVehicule.doesNotContain=" + UPDATED_NUMERO_VEHICULE,
            "numeroVehicule.doesNotContain=" + DEFAULT_NUMERO_VEHICULE
        );
    }

    @Test
    @Transactional
    void getAllBusesByPlaqueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where plaque equals to
        defaultBusFiltering("plaque.equals=" + DEFAULT_PLAQUE, "plaque.equals=" + UPDATED_PLAQUE);
    }

    @Test
    @Transactional
    void getAllBusesByPlaqueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where plaque in
        defaultBusFiltering("plaque.in=" + DEFAULT_PLAQUE + "," + UPDATED_PLAQUE, "plaque.in=" + UPDATED_PLAQUE);
    }

    @Test
    @Transactional
    void getAllBusesByPlaqueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where plaque is not null
        defaultBusFiltering("plaque.specified=true", "plaque.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByPlaqueContainsSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where plaque contains
        defaultBusFiltering("plaque.contains=" + DEFAULT_PLAQUE, "plaque.contains=" + UPDATED_PLAQUE);
    }

    @Test
    @Transactional
    void getAllBusesByPlaqueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where plaque does not contain
        defaultBusFiltering("plaque.doesNotContain=" + UPDATED_PLAQUE, "plaque.doesNotContain=" + DEFAULT_PLAQUE);
    }

    @Test
    @Transactional
    void getAllBusesByModeleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where modele equals to
        defaultBusFiltering("modele.equals=" + DEFAULT_MODELE, "modele.equals=" + UPDATED_MODELE);
    }

    @Test
    @Transactional
    void getAllBusesByModeleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where modele in
        defaultBusFiltering("modele.in=" + DEFAULT_MODELE + "," + UPDATED_MODELE, "modele.in=" + UPDATED_MODELE);
    }

    @Test
    @Transactional
    void getAllBusesByModeleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where modele is not null
        defaultBusFiltering("modele.specified=true", "modele.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByModeleContainsSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where modele contains
        defaultBusFiltering("modele.contains=" + DEFAULT_MODELE, "modele.contains=" + UPDATED_MODELE);
    }

    @Test
    @Transactional
    void getAllBusesByModeleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where modele does not contain
        defaultBusFiltering("modele.doesNotContain=" + UPDATED_MODELE, "modele.doesNotContain=" + DEFAULT_MODELE);
    }

    @Test
    @Transactional
    void getAllBusesByCapaciteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where capacite equals to
        defaultBusFiltering("capacite.equals=" + DEFAULT_CAPACITE, "capacite.equals=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void getAllBusesByCapaciteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where capacite in
        defaultBusFiltering("capacite.in=" + DEFAULT_CAPACITE + "," + UPDATED_CAPACITE, "capacite.in=" + UPDATED_CAPACITE);
    }

    @Test
    @Transactional
    void getAllBusesByCapaciteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where capacite is not null
        defaultBusFiltering("capacite.specified=true", "capacite.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByCapaciteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where capacite is greater than or equal to
        defaultBusFiltering("capacite.greaterThanOrEqual=" + DEFAULT_CAPACITE, "capacite.greaterThanOrEqual=" + (DEFAULT_CAPACITE + 1));
    }

    @Test
    @Transactional
    void getAllBusesByCapaciteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where capacite is less than or equal to
        defaultBusFiltering("capacite.lessThanOrEqual=" + DEFAULT_CAPACITE, "capacite.lessThanOrEqual=" + SMALLER_CAPACITE);
    }

    @Test
    @Transactional
    void getAllBusesByCapaciteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where capacite is less than
        defaultBusFiltering("capacite.lessThan=" + (DEFAULT_CAPACITE + 1), "capacite.lessThan=" + DEFAULT_CAPACITE);
    }

    @Test
    @Transactional
    void getAllBusesByCapaciteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where capacite is greater than
        defaultBusFiltering("capacite.greaterThan=" + SMALLER_CAPACITE, "capacite.greaterThan=" + DEFAULT_CAPACITE);
    }

    @Test
    @Transactional
    void getAllBusesByAnneeFabricationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where anneeFabrication equals to
        defaultBusFiltering("anneeFabrication.equals=" + DEFAULT_ANNEE_FABRICATION, "anneeFabrication.equals=" + UPDATED_ANNEE_FABRICATION);
    }

    @Test
    @Transactional
    void getAllBusesByAnneeFabricationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where anneeFabrication in
        defaultBusFiltering(
            "anneeFabrication.in=" + DEFAULT_ANNEE_FABRICATION + "," + UPDATED_ANNEE_FABRICATION,
            "anneeFabrication.in=" + UPDATED_ANNEE_FABRICATION
        );
    }

    @Test
    @Transactional
    void getAllBusesByAnneeFabricationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where anneeFabrication is not null
        defaultBusFiltering("anneeFabrication.specified=true", "anneeFabrication.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByAnneeFabricationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where anneeFabrication is greater than or equal to
        defaultBusFiltering(
            "anneeFabrication.greaterThanOrEqual=" + DEFAULT_ANNEE_FABRICATION,
            "anneeFabrication.greaterThanOrEqual=" + UPDATED_ANNEE_FABRICATION
        );
    }

    @Test
    @Transactional
    void getAllBusesByAnneeFabricationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where anneeFabrication is less than or equal to
        defaultBusFiltering(
            "anneeFabrication.lessThanOrEqual=" + DEFAULT_ANNEE_FABRICATION,
            "anneeFabrication.lessThanOrEqual=" + SMALLER_ANNEE_FABRICATION
        );
    }

    @Test
    @Transactional
    void getAllBusesByAnneeFabricationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where anneeFabrication is less than
        defaultBusFiltering(
            "anneeFabrication.lessThan=" + UPDATED_ANNEE_FABRICATION,
            "anneeFabrication.lessThan=" + DEFAULT_ANNEE_FABRICATION
        );
    }

    @Test
    @Transactional
    void getAllBusesByAnneeFabricationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where anneeFabrication is greater than
        defaultBusFiltering(
            "anneeFabrication.greaterThan=" + SMALLER_ANNEE_FABRICATION,
            "anneeFabrication.greaterThan=" + DEFAULT_ANNEE_FABRICATION
        );
    }

    @Test
    @Transactional
    void getAllBusesByGpsDeviceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsDeviceId equals to
        defaultBusFiltering("gpsDeviceId.equals=" + DEFAULT_GPS_DEVICE_ID, "gpsDeviceId.equals=" + UPDATED_GPS_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllBusesByGpsDeviceIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsDeviceId in
        defaultBusFiltering(
            "gpsDeviceId.in=" + DEFAULT_GPS_DEVICE_ID + "," + UPDATED_GPS_DEVICE_ID,
            "gpsDeviceId.in=" + UPDATED_GPS_DEVICE_ID
        );
    }

    @Test
    @Transactional
    void getAllBusesByGpsDeviceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsDeviceId is not null
        defaultBusFiltering("gpsDeviceId.specified=true", "gpsDeviceId.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByGpsDeviceIdContainsSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsDeviceId contains
        defaultBusFiltering("gpsDeviceId.contains=" + DEFAULT_GPS_DEVICE_ID, "gpsDeviceId.contains=" + UPDATED_GPS_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllBusesByGpsDeviceIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsDeviceId does not contain
        defaultBusFiltering("gpsDeviceId.doesNotContain=" + UPDATED_GPS_DEVICE_ID, "gpsDeviceId.doesNotContain=" + DEFAULT_GPS_DEVICE_ID);
    }

    @Test
    @Transactional
    void getAllBusesByGpsStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsStatus equals to
        defaultBusFiltering("gpsStatus.equals=" + DEFAULT_GPS_STATUS, "gpsStatus.equals=" + UPDATED_GPS_STATUS);
    }

    @Test
    @Transactional
    void getAllBusesByGpsStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsStatus in
        defaultBusFiltering("gpsStatus.in=" + DEFAULT_GPS_STATUS + "," + UPDATED_GPS_STATUS, "gpsStatus.in=" + UPDATED_GPS_STATUS);
    }

    @Test
    @Transactional
    void getAllBusesByGpsStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsStatus is not null
        defaultBusFiltering("gpsStatus.specified=true", "gpsStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByGpsStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsStatus contains
        defaultBusFiltering("gpsStatus.contains=" + DEFAULT_GPS_STATUS, "gpsStatus.contains=" + UPDATED_GPS_STATUS);
    }

    @Test
    @Transactional
    void getAllBusesByGpsStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsStatus does not contain
        defaultBusFiltering("gpsStatus.doesNotContain=" + UPDATED_GPS_STATUS, "gpsStatus.doesNotContain=" + DEFAULT_GPS_STATUS);
    }

    @Test
    @Transactional
    void getAllBusesByGpsLastPingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsLastPing equals to
        defaultBusFiltering("gpsLastPing.equals=" + DEFAULT_GPS_LAST_PING, "gpsLastPing.equals=" + UPDATED_GPS_LAST_PING);
    }

    @Test
    @Transactional
    void getAllBusesByGpsLastPingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsLastPing in
        defaultBusFiltering(
            "gpsLastPing.in=" + DEFAULT_GPS_LAST_PING + "," + UPDATED_GPS_LAST_PING,
            "gpsLastPing.in=" + UPDATED_GPS_LAST_PING
        );
    }

    @Test
    @Transactional
    void getAllBusesByGpsLastPingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsLastPing is not null
        defaultBusFiltering("gpsLastPing.specified=true", "gpsLastPing.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByGpsBatteryLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsBatteryLevel equals to
        defaultBusFiltering("gpsBatteryLevel.equals=" + DEFAULT_GPS_BATTERY_LEVEL, "gpsBatteryLevel.equals=" + UPDATED_GPS_BATTERY_LEVEL);
    }

    @Test
    @Transactional
    void getAllBusesByGpsBatteryLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsBatteryLevel in
        defaultBusFiltering(
            "gpsBatteryLevel.in=" + DEFAULT_GPS_BATTERY_LEVEL + "," + UPDATED_GPS_BATTERY_LEVEL,
            "gpsBatteryLevel.in=" + UPDATED_GPS_BATTERY_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllBusesByGpsBatteryLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsBatteryLevel is not null
        defaultBusFiltering("gpsBatteryLevel.specified=true", "gpsBatteryLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByGpsBatteryLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsBatteryLevel is greater than or equal to
        defaultBusFiltering(
            "gpsBatteryLevel.greaterThanOrEqual=" + DEFAULT_GPS_BATTERY_LEVEL,
            "gpsBatteryLevel.greaterThanOrEqual=" + (DEFAULT_GPS_BATTERY_LEVEL + 1)
        );
    }

    @Test
    @Transactional
    void getAllBusesByGpsBatteryLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsBatteryLevel is less than or equal to
        defaultBusFiltering(
            "gpsBatteryLevel.lessThanOrEqual=" + DEFAULT_GPS_BATTERY_LEVEL,
            "gpsBatteryLevel.lessThanOrEqual=" + SMALLER_GPS_BATTERY_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllBusesByGpsBatteryLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsBatteryLevel is less than
        defaultBusFiltering(
            "gpsBatteryLevel.lessThan=" + (DEFAULT_GPS_BATTERY_LEVEL + 1),
            "gpsBatteryLevel.lessThan=" + DEFAULT_GPS_BATTERY_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllBusesByGpsBatteryLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where gpsBatteryLevel is greater than
        defaultBusFiltering(
            "gpsBatteryLevel.greaterThan=" + SMALLER_GPS_BATTERY_LEVEL,
            "gpsBatteryLevel.greaterThan=" + DEFAULT_GPS_BATTERY_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLatitude equals to
        defaultBusFiltering("currentLatitude.equals=" + DEFAULT_CURRENT_LATITUDE, "currentLatitude.equals=" + UPDATED_CURRENT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLatitude in
        defaultBusFiltering(
            "currentLatitude.in=" + DEFAULT_CURRENT_LATITUDE + "," + UPDATED_CURRENT_LATITUDE,
            "currentLatitude.in=" + UPDATED_CURRENT_LATITUDE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLatitude is not null
        defaultBusFiltering("currentLatitude.specified=true", "currentLatitude.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLatitude is greater than or equal to
        defaultBusFiltering(
            "currentLatitude.greaterThanOrEqual=" + DEFAULT_CURRENT_LATITUDE,
            "currentLatitude.greaterThanOrEqual=" + UPDATED_CURRENT_LATITUDE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLatitude is less than or equal to
        defaultBusFiltering(
            "currentLatitude.lessThanOrEqual=" + DEFAULT_CURRENT_LATITUDE,
            "currentLatitude.lessThanOrEqual=" + SMALLER_CURRENT_LATITUDE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLatitude is less than
        defaultBusFiltering("currentLatitude.lessThan=" + UPDATED_CURRENT_LATITUDE, "currentLatitude.lessThan=" + DEFAULT_CURRENT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLatitude is greater than
        defaultBusFiltering(
            "currentLatitude.greaterThan=" + SMALLER_CURRENT_LATITUDE,
            "currentLatitude.greaterThan=" + DEFAULT_CURRENT_LATITUDE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLongitude equals to
        defaultBusFiltering("currentLongitude.equals=" + DEFAULT_CURRENT_LONGITUDE, "currentLongitude.equals=" + UPDATED_CURRENT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLongitude in
        defaultBusFiltering(
            "currentLongitude.in=" + DEFAULT_CURRENT_LONGITUDE + "," + UPDATED_CURRENT_LONGITUDE,
            "currentLongitude.in=" + UPDATED_CURRENT_LONGITUDE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLongitude is not null
        defaultBusFiltering("currentLongitude.specified=true", "currentLongitude.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLongitude is greater than or equal to
        defaultBusFiltering(
            "currentLongitude.greaterThanOrEqual=" + DEFAULT_CURRENT_LONGITUDE,
            "currentLongitude.greaterThanOrEqual=" + UPDATED_CURRENT_LONGITUDE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLongitude is less than or equal to
        defaultBusFiltering(
            "currentLongitude.lessThanOrEqual=" + DEFAULT_CURRENT_LONGITUDE,
            "currentLongitude.lessThanOrEqual=" + SMALLER_CURRENT_LONGITUDE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLongitude is less than
        defaultBusFiltering(
            "currentLongitude.lessThan=" + UPDATED_CURRENT_LONGITUDE,
            "currentLongitude.lessThan=" + DEFAULT_CURRENT_LONGITUDE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentLongitude is greater than
        defaultBusFiltering(
            "currentLongitude.greaterThan=" + SMALLER_CURRENT_LONGITUDE,
            "currentLongitude.greaterThan=" + DEFAULT_CURRENT_LONGITUDE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentVitesseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentVitesse equals to
        defaultBusFiltering("currentVitesse.equals=" + DEFAULT_CURRENT_VITESSE, "currentVitesse.equals=" + UPDATED_CURRENT_VITESSE);
    }

    @Test
    @Transactional
    void getAllBusesByCurrentVitesseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentVitesse in
        defaultBusFiltering(
            "currentVitesse.in=" + DEFAULT_CURRENT_VITESSE + "," + UPDATED_CURRENT_VITESSE,
            "currentVitesse.in=" + UPDATED_CURRENT_VITESSE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentVitesseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentVitesse is not null
        defaultBusFiltering("currentVitesse.specified=true", "currentVitesse.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByCurrentVitesseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentVitesse is greater than or equal to
        defaultBusFiltering(
            "currentVitesse.greaterThanOrEqual=" + DEFAULT_CURRENT_VITESSE,
            "currentVitesse.greaterThanOrEqual=" + UPDATED_CURRENT_VITESSE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentVitesseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentVitesse is less than or equal to
        defaultBusFiltering(
            "currentVitesse.lessThanOrEqual=" + DEFAULT_CURRENT_VITESSE,
            "currentVitesse.lessThanOrEqual=" + SMALLER_CURRENT_VITESSE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentVitesseIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentVitesse is less than
        defaultBusFiltering("currentVitesse.lessThan=" + UPDATED_CURRENT_VITESSE, "currentVitesse.lessThan=" + DEFAULT_CURRENT_VITESSE);
    }

    @Test
    @Transactional
    void getAllBusesByCurrentVitesseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentVitesse is greater than
        defaultBusFiltering(
            "currentVitesse.greaterThan=" + SMALLER_CURRENT_VITESSE,
            "currentVitesse.greaterThan=" + DEFAULT_CURRENT_VITESSE
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentCapIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentCap equals to
        defaultBusFiltering("currentCap.equals=" + DEFAULT_CURRENT_CAP, "currentCap.equals=" + UPDATED_CURRENT_CAP);
    }

    @Test
    @Transactional
    void getAllBusesByCurrentCapIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentCap in
        defaultBusFiltering("currentCap.in=" + DEFAULT_CURRENT_CAP + "," + UPDATED_CURRENT_CAP, "currentCap.in=" + UPDATED_CURRENT_CAP);
    }

    @Test
    @Transactional
    void getAllBusesByCurrentCapIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentCap is not null
        defaultBusFiltering("currentCap.specified=true", "currentCap.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByCurrentCapIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentCap is greater than or equal to
        defaultBusFiltering(
            "currentCap.greaterThanOrEqual=" + DEFAULT_CURRENT_CAP,
            "currentCap.greaterThanOrEqual=" + (DEFAULT_CURRENT_CAP + 1)
        );
    }

    @Test
    @Transactional
    void getAllBusesByCurrentCapIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentCap is less than or equal to
        defaultBusFiltering("currentCap.lessThanOrEqual=" + DEFAULT_CURRENT_CAP, "currentCap.lessThanOrEqual=" + SMALLER_CURRENT_CAP);
    }

    @Test
    @Transactional
    void getAllBusesByCurrentCapIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentCap is less than
        defaultBusFiltering("currentCap.lessThan=" + (DEFAULT_CURRENT_CAP + 1), "currentCap.lessThan=" + DEFAULT_CURRENT_CAP);
    }

    @Test
    @Transactional
    void getAllBusesByCurrentCapIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where currentCap is greater than
        defaultBusFiltering("currentCap.greaterThan=" + SMALLER_CURRENT_CAP, "currentCap.greaterThan=" + DEFAULT_CURRENT_CAP);
    }

    @Test
    @Transactional
    void getAllBusesByPositionUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where positionUpdatedAt equals to
        defaultBusFiltering(
            "positionUpdatedAt.equals=" + DEFAULT_POSITION_UPDATED_AT,
            "positionUpdatedAt.equals=" + UPDATED_POSITION_UPDATED_AT
        );
    }

    @Test
    @Transactional
    void getAllBusesByPositionUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where positionUpdatedAt in
        defaultBusFiltering(
            "positionUpdatedAt.in=" + DEFAULT_POSITION_UPDATED_AT + "," + UPDATED_POSITION_UPDATED_AT,
            "positionUpdatedAt.in=" + UPDATED_POSITION_UPDATED_AT
        );
    }

    @Test
    @Transactional
    void getAllBusesByPositionUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where positionUpdatedAt is not null
        defaultBusFiltering("positionUpdatedAt.specified=true", "positionUpdatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where statut equals to
        defaultBusFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllBusesByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where statut in
        defaultBusFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllBusesByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where statut is not null
        defaultBusFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllBusesByStatutContainsSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where statut contains
        defaultBusFiltering("statut.contains=" + DEFAULT_STATUT, "statut.contains=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllBusesByStatutNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        // Get all the busList where statut does not contain
        defaultBusFiltering("statut.doesNotContain=" + UPDATED_STATUT, "statut.doesNotContain=" + DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void getAllBusesByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            busRepository.saveAndFlush(bus);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        bus.setUtilisateur(utilisateur);
        busRepository.saveAndFlush(bus);
        Long utilisateurId = utilisateur.getId();
        // Get all the busList where utilisateur equals to utilisateurId
        defaultBusShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the busList where utilisateur equals to (utilisateurId + 1)
        defaultBusShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    @Test
    @Transactional
    void getAllBusesByLigneIsEqualToSomething() throws Exception {
        Ligne ligne;
        if (TestUtil.findAll(em, Ligne.class).isEmpty()) {
            busRepository.saveAndFlush(bus);
            ligne = LigneResourceIT.createEntity();
        } else {
            ligne = TestUtil.findAll(em, Ligne.class).get(0);
        }
        em.persist(ligne);
        em.flush();
        bus.setLigne(ligne);
        busRepository.saveAndFlush(bus);
        Long ligneId = ligne.getId();
        // Get all the busList where ligne equals to ligneId
        defaultBusShouldBeFound("ligneId.equals=" + ligneId);

        // Get all the busList where ligne equals to (ligneId + 1)
        defaultBusShouldNotBeFound("ligneId.equals=" + (ligneId + 1));
    }

    private void defaultBusFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBusShouldBeFound(shouldBeFound);
        defaultBusShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBusShouldBeFound(String filter) throws Exception {
        restBusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bus.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroVehicule").value(hasItem(DEFAULT_NUMERO_VEHICULE)))
            .andExpect(jsonPath("$.[*].plaque").value(hasItem(DEFAULT_PLAQUE)))
            .andExpect(jsonPath("$.[*].modele").value(hasItem(DEFAULT_MODELE)))
            .andExpect(jsonPath("$.[*].capacite").value(hasItem(DEFAULT_CAPACITE)))
            .andExpect(jsonPath("$.[*].anneeFabrication").value(hasItem(DEFAULT_ANNEE_FABRICATION)))
            .andExpect(jsonPath("$.[*].gpsDeviceId").value(hasItem(DEFAULT_GPS_DEVICE_ID)))
            .andExpect(jsonPath("$.[*].gpsStatus").value(hasItem(DEFAULT_GPS_STATUS)))
            .andExpect(jsonPath("$.[*].gpsLastPing").value(hasItem(DEFAULT_GPS_LAST_PING.toString())))
            .andExpect(jsonPath("$.[*].gpsBatteryLevel").value(hasItem(DEFAULT_GPS_BATTERY_LEVEL)))
            .andExpect(jsonPath("$.[*].currentLatitude").value(hasItem(sameNumber(DEFAULT_CURRENT_LATITUDE))))
            .andExpect(jsonPath("$.[*].currentLongitude").value(hasItem(sameNumber(DEFAULT_CURRENT_LONGITUDE))))
            .andExpect(jsonPath("$.[*].currentVitesse").value(hasItem(sameNumber(DEFAULT_CURRENT_VITESSE))))
            .andExpect(jsonPath("$.[*].currentCap").value(hasItem(DEFAULT_CURRENT_CAP)))
            .andExpect(jsonPath("$.[*].positionUpdatedAt").value(hasItem(DEFAULT_POSITION_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)));

        // Check, that the count call also returns 1
        restBusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBusShouldNotBeFound(String filter) throws Exception {
        restBusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBus() throws Exception {
        // Get the bus
        restBusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBus() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bus
        Bus updatedBus = busRepository.findById(bus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBus are not directly saved in db
        em.detach(updatedBus);
        updatedBus
            .numeroVehicule(UPDATED_NUMERO_VEHICULE)
            .plaque(UPDATED_PLAQUE)
            .modele(UPDATED_MODELE)
            .capacite(UPDATED_CAPACITE)
            .anneeFabrication(UPDATED_ANNEE_FABRICATION)
            .gpsDeviceId(UPDATED_GPS_DEVICE_ID)
            .gpsStatus(UPDATED_GPS_STATUS)
            .gpsLastPing(UPDATED_GPS_LAST_PING)
            .gpsBatteryLevel(UPDATED_GPS_BATTERY_LEVEL)
            .currentLatitude(UPDATED_CURRENT_LATITUDE)
            .currentLongitude(UPDATED_CURRENT_LONGITUDE)
            .currentVitesse(UPDATED_CURRENT_VITESSE)
            .currentCap(UPDATED_CURRENT_CAP)
            .positionUpdatedAt(UPDATED_POSITION_UPDATED_AT)
            .statut(UPDATED_STATUT);
        BusDTO busDTO = busMapper.toDto(updatedBus);

        restBusMockMvc
            .perform(put(ENTITY_API_URL_ID, busDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(busDTO)))
            .andExpect(status().isOk());

        // Validate the Bus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBusToMatchAllProperties(updatedBus);
    }

    @Test
    @Transactional
    void putNonExistingBus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bus.setId(longCount.incrementAndGet());

        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusMockMvc
            .perform(put(ENTITY_API_URL_ID, busDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(busDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bus.setId(longCount.incrementAndGet());

        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(busDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bus.setId(longCount.incrementAndGet());

        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(busDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBusWithPatch() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bus using partial update
        Bus partialUpdatedBus = new Bus();
        partialUpdatedBus.setId(bus.getId());

        partialUpdatedBus
            .plaque(UPDATED_PLAQUE)
            .modele(UPDATED_MODELE)
            .capacite(UPDATED_CAPACITE)
            .gpsLastPing(UPDATED_GPS_LAST_PING)
            .gpsBatteryLevel(UPDATED_GPS_BATTERY_LEVEL)
            .currentLatitude(UPDATED_CURRENT_LATITUDE)
            .currentVitesse(UPDATED_CURRENT_VITESSE)
            .positionUpdatedAt(UPDATED_POSITION_UPDATED_AT)
            .statut(UPDATED_STATUT);

        restBusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBus))
            )
            .andExpect(status().isOk());

        // Validate the Bus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBusUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBus, bus), getPersistedBus(bus));
    }

    @Test
    @Transactional
    void fullUpdateBusWithPatch() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bus using partial update
        Bus partialUpdatedBus = new Bus();
        partialUpdatedBus.setId(bus.getId());

        partialUpdatedBus
            .numeroVehicule(UPDATED_NUMERO_VEHICULE)
            .plaque(UPDATED_PLAQUE)
            .modele(UPDATED_MODELE)
            .capacite(UPDATED_CAPACITE)
            .anneeFabrication(UPDATED_ANNEE_FABRICATION)
            .gpsDeviceId(UPDATED_GPS_DEVICE_ID)
            .gpsStatus(UPDATED_GPS_STATUS)
            .gpsLastPing(UPDATED_GPS_LAST_PING)
            .gpsBatteryLevel(UPDATED_GPS_BATTERY_LEVEL)
            .currentLatitude(UPDATED_CURRENT_LATITUDE)
            .currentLongitude(UPDATED_CURRENT_LONGITUDE)
            .currentVitesse(UPDATED_CURRENT_VITESSE)
            .currentCap(UPDATED_CURRENT_CAP)
            .positionUpdatedAt(UPDATED_POSITION_UPDATED_AT)
            .statut(UPDATED_STATUT);

        restBusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBus))
            )
            .andExpect(status().isOk());

        // Validate the Bus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBusUpdatableFieldsEquals(partialUpdatedBus, getPersistedBus(partialUpdatedBus));
    }

    @Test
    @Transactional
    void patchNonExistingBus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bus.setId(longCount.incrementAndGet());

        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, busDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(busDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bus.setId(longCount.incrementAndGet());

        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(busDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bus.setId(longCount.incrementAndGet());

        // Create the Bus
        BusDTO busDTO = busMapper.toDto(bus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(busDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBus() throws Exception {
        // Initialize the database
        insertedBus = busRepository.saveAndFlush(bus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bus
        restBusMockMvc.perform(delete(ENTITY_API_URL_ID, bus.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return busRepository.count();
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

    protected Bus getPersistedBus(Bus bus) {
        return busRepository.findById(bus.getId()).orElseThrow();
    }

    protected void assertPersistedBusToMatchAllProperties(Bus expectedBus) {
        assertBusAllPropertiesEquals(expectedBus, getPersistedBus(expectedBus));
    }

    protected void assertPersistedBusToMatchUpdatableProperties(Bus expectedBus) {
        assertBusAllUpdatablePropertiesEquals(expectedBus, getPersistedBus(expectedBus));
    }
}
