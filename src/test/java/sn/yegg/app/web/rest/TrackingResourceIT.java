package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.TrackingAsserts.*;
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
import sn.yegg.app.domain.Tracking;
import sn.yegg.app.repository.TrackingRepository;
import sn.yegg.app.service.dto.TrackingDTO;
import sn.yegg.app.service.mapper.TrackingMapper;

/**
 * Integration tests for the {@link TrackingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrackingResourceIT {

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LATITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);
    private static final BigDecimal SMALLER_LONGITUDE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_VITESSE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VITESSE = new BigDecimal(2);
    private static final BigDecimal SMALLER_VITESSE = new BigDecimal(1 - 1);

    private static final Integer DEFAULT_CAP = 0;
    private static final Integer UPDATED_CAP = 1;
    private static final Integer SMALLER_CAP = 0 - 1;

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trackings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrackingRepository trackingRepository;

    @Autowired
    private TrackingMapper trackingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrackingMockMvc;

    private Tracking tracking;

    private Tracking insertedTracking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tracking createEntity() {
        return new Tracking()
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE)
            .vitesse(DEFAULT_VITESSE)
            .cap(DEFAULT_CAP)
            .timestamp(DEFAULT_TIMESTAMP)
            .source(DEFAULT_SOURCE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tracking createUpdatedEntity() {
        return new Tracking()
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .vitesse(UPDATED_VITESSE)
            .cap(UPDATED_CAP)
            .timestamp(UPDATED_TIMESTAMP)
            .source(UPDATED_SOURCE);
    }

    @BeforeEach
    void initTest() {
        tracking = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTracking != null) {
            trackingRepository.delete(insertedTracking);
            insertedTracking = null;
        }
    }

    @Test
    @Transactional
    void createTracking() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Tracking
        TrackingDTO trackingDTO = trackingMapper.toDto(tracking);
        var returnedTrackingDTO = om.readValue(
            restTrackingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trackingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrackingDTO.class
        );

        // Validate the Tracking in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTracking = trackingMapper.toEntity(returnedTrackingDTO);
        assertTrackingUpdatableFieldsEquals(returnedTracking, getPersistedTracking(returnedTracking));

        insertedTracking = returnedTracking;
    }

    @Test
    @Transactional
    void createTrackingWithExistingId() throws Exception {
        // Create the Tracking with an existing ID
        tracking.setId(1L);
        TrackingDTO trackingDTO = trackingMapper.toDto(tracking);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrackingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trackingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tracking in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLatitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tracking.setLatitude(null);

        // Create the Tracking, which fails.
        TrackingDTO trackingDTO = trackingMapper.toDto(tracking);

        restTrackingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trackingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tracking.setLongitude(null);

        // Create the Tracking, which fails.
        TrackingDTO trackingDTO = trackingMapper.toDto(tracking);

        restTrackingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trackingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tracking.setTimestamp(null);

        // Create the Tracking, which fails.
        TrackingDTO trackingDTO = trackingMapper.toDto(tracking);

        restTrackingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trackingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrackings() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList
        restTrackingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))))
            .andExpect(jsonPath("$.[*].vitesse").value(hasItem(sameNumber(DEFAULT_VITESSE))))
            .andExpect(jsonPath("$.[*].cap").value(hasItem(DEFAULT_CAP)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)));
    }

    @Test
    @Transactional
    void getTracking() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get the tracking
        restTrackingMockMvc
            .perform(get(ENTITY_API_URL_ID, tracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tracking.getId().intValue()))
            .andExpect(jsonPath("$.latitude").value(sameNumber(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.longitude").value(sameNumber(DEFAULT_LONGITUDE)))
            .andExpect(jsonPath("$.vitesse").value(sameNumber(DEFAULT_VITESSE)))
            .andExpect(jsonPath("$.cap").value(DEFAULT_CAP))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE));
    }

    @Test
    @Transactional
    void getTrackingsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        Long id = tracking.getId();

        defaultTrackingFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTrackingFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTrackingFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTrackingsByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where latitude equals to
        defaultTrackingFiltering("latitude.equals=" + DEFAULT_LATITUDE, "latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTrackingsByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where latitude in
        defaultTrackingFiltering("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE, "latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTrackingsByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where latitude is not null
        defaultTrackingFiltering("latitude.specified=true", "latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllTrackingsByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where latitude is greater than or equal to
        defaultTrackingFiltering("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE, "latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTrackingsByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where latitude is less than or equal to
        defaultTrackingFiltering("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE, "latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTrackingsByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where latitude is less than
        defaultTrackingFiltering("latitude.lessThan=" + UPDATED_LATITUDE, "latitude.lessThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTrackingsByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where latitude is greater than
        defaultTrackingFiltering("latitude.greaterThan=" + SMALLER_LATITUDE, "latitude.greaterThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllTrackingsByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where longitude equals to
        defaultTrackingFiltering("longitude.equals=" + DEFAULT_LONGITUDE, "longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTrackingsByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where longitude in
        defaultTrackingFiltering("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE, "longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTrackingsByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where longitude is not null
        defaultTrackingFiltering("longitude.specified=true", "longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllTrackingsByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where longitude is greater than or equal to
        defaultTrackingFiltering("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTrackingsByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where longitude is less than or equal to
        defaultTrackingFiltering("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTrackingsByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where longitude is less than
        defaultTrackingFiltering("longitude.lessThan=" + UPDATED_LONGITUDE, "longitude.lessThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTrackingsByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where longitude is greater than
        defaultTrackingFiltering("longitude.greaterThan=" + SMALLER_LONGITUDE, "longitude.greaterThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllTrackingsByVitesseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where vitesse equals to
        defaultTrackingFiltering("vitesse.equals=" + DEFAULT_VITESSE, "vitesse.equals=" + UPDATED_VITESSE);
    }

    @Test
    @Transactional
    void getAllTrackingsByVitesseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where vitesse in
        defaultTrackingFiltering("vitesse.in=" + DEFAULT_VITESSE + "," + UPDATED_VITESSE, "vitesse.in=" + UPDATED_VITESSE);
    }

    @Test
    @Transactional
    void getAllTrackingsByVitesseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where vitesse is not null
        defaultTrackingFiltering("vitesse.specified=true", "vitesse.specified=false");
    }

    @Test
    @Transactional
    void getAllTrackingsByVitesseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where vitesse is greater than or equal to
        defaultTrackingFiltering("vitesse.greaterThanOrEqual=" + DEFAULT_VITESSE, "vitesse.greaterThanOrEqual=" + UPDATED_VITESSE);
    }

    @Test
    @Transactional
    void getAllTrackingsByVitesseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where vitesse is less than or equal to
        defaultTrackingFiltering("vitesse.lessThanOrEqual=" + DEFAULT_VITESSE, "vitesse.lessThanOrEqual=" + SMALLER_VITESSE);
    }

    @Test
    @Transactional
    void getAllTrackingsByVitesseIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where vitesse is less than
        defaultTrackingFiltering("vitesse.lessThan=" + UPDATED_VITESSE, "vitesse.lessThan=" + DEFAULT_VITESSE);
    }

    @Test
    @Transactional
    void getAllTrackingsByVitesseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where vitesse is greater than
        defaultTrackingFiltering("vitesse.greaterThan=" + SMALLER_VITESSE, "vitesse.greaterThan=" + DEFAULT_VITESSE);
    }

    @Test
    @Transactional
    void getAllTrackingsByCapIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where cap equals to
        defaultTrackingFiltering("cap.equals=" + DEFAULT_CAP, "cap.equals=" + UPDATED_CAP);
    }

    @Test
    @Transactional
    void getAllTrackingsByCapIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where cap in
        defaultTrackingFiltering("cap.in=" + DEFAULT_CAP + "," + UPDATED_CAP, "cap.in=" + UPDATED_CAP);
    }

    @Test
    @Transactional
    void getAllTrackingsByCapIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where cap is not null
        defaultTrackingFiltering("cap.specified=true", "cap.specified=false");
    }

    @Test
    @Transactional
    void getAllTrackingsByCapIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where cap is greater than or equal to
        defaultTrackingFiltering("cap.greaterThanOrEqual=" + DEFAULT_CAP, "cap.greaterThanOrEqual=" + (DEFAULT_CAP + 1));
    }

    @Test
    @Transactional
    void getAllTrackingsByCapIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where cap is less than or equal to
        defaultTrackingFiltering("cap.lessThanOrEqual=" + DEFAULT_CAP, "cap.lessThanOrEqual=" + SMALLER_CAP);
    }

    @Test
    @Transactional
    void getAllTrackingsByCapIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where cap is less than
        defaultTrackingFiltering("cap.lessThan=" + (DEFAULT_CAP + 1), "cap.lessThan=" + DEFAULT_CAP);
    }

    @Test
    @Transactional
    void getAllTrackingsByCapIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where cap is greater than
        defaultTrackingFiltering("cap.greaterThan=" + SMALLER_CAP, "cap.greaterThan=" + DEFAULT_CAP);
    }

    @Test
    @Transactional
    void getAllTrackingsByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where timestamp equals to
        defaultTrackingFiltering("timestamp.equals=" + DEFAULT_TIMESTAMP, "timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllTrackingsByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where timestamp in
        defaultTrackingFiltering("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP, "timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllTrackingsByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where timestamp is not null
        defaultTrackingFiltering("timestamp.specified=true", "timestamp.specified=false");
    }

    @Test
    @Transactional
    void getAllTrackingsBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where source equals to
        defaultTrackingFiltering("source.equals=" + DEFAULT_SOURCE, "source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllTrackingsBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where source in
        defaultTrackingFiltering("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE, "source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllTrackingsBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where source is not null
        defaultTrackingFiltering("source.specified=true", "source.specified=false");
    }

    @Test
    @Transactional
    void getAllTrackingsBySourceContainsSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where source contains
        defaultTrackingFiltering("source.contains=" + DEFAULT_SOURCE, "source.contains=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllTrackingsBySourceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        // Get all the trackingList where source does not contain
        defaultTrackingFiltering("source.doesNotContain=" + UPDATED_SOURCE, "source.doesNotContain=" + DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    void getAllTrackingsByBusIsEqualToSomething() throws Exception {
        Bus bus;
        if (TestUtil.findAll(em, Bus.class).isEmpty()) {
            trackingRepository.saveAndFlush(tracking);
            bus = BusResourceIT.createEntity();
        } else {
            bus = TestUtil.findAll(em, Bus.class).get(0);
        }
        em.persist(bus);
        em.flush();
        tracking.setBus(bus);
        trackingRepository.saveAndFlush(tracking);
        Long busId = bus.getId();
        // Get all the trackingList where bus equals to busId
        defaultTrackingShouldBeFound("busId.equals=" + busId);

        // Get all the trackingList where bus equals to (busId + 1)
        defaultTrackingShouldNotBeFound("busId.equals=" + (busId + 1));
    }

    private void defaultTrackingFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTrackingShouldBeFound(shouldBeFound);
        defaultTrackingShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrackingShouldBeFound(String filter) throws Exception {
        restTrackingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))))
            .andExpect(jsonPath("$.[*].vitesse").value(hasItem(sameNumber(DEFAULT_VITESSE))))
            .andExpect(jsonPath("$.[*].cap").value(hasItem(DEFAULT_CAP)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)));

        // Check, that the count call also returns 1
        restTrackingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrackingShouldNotBeFound(String filter) throws Exception {
        restTrackingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrackingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTracking() throws Exception {
        // Get the tracking
        restTrackingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTracking() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tracking
        Tracking updatedTracking = trackingRepository.findById(tracking.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTracking are not directly saved in db
        em.detach(updatedTracking);
        updatedTracking
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .vitesse(UPDATED_VITESSE)
            .cap(UPDATED_CAP)
            .timestamp(UPDATED_TIMESTAMP)
            .source(UPDATED_SOURCE);
        TrackingDTO trackingDTO = trackingMapper.toDto(updatedTracking);

        restTrackingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trackingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrackingToMatchAllProperties(updatedTracking);
    }

    @Test
    @Transactional
    void putNonExistingTracking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tracking.setId(longCount.incrementAndGet());

        // Create the Tracking
        TrackingDTO trackingDTO = trackingMapper.toDto(tracking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trackingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTracking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tracking.setId(longCount.incrementAndGet());

        // Create the Tracking
        TrackingDTO trackingDTO = trackingMapper.toDto(tracking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trackingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTracking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tracking.setId(longCount.incrementAndGet());

        // Create the Tracking
        TrackingDTO trackingDTO = trackingMapper.toDto(tracking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trackingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrackingWithPatch() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tracking using partial update
        Tracking partialUpdatedTracking = new Tracking();
        partialUpdatedTracking.setId(tracking.getId());

        partialUpdatedTracking.cap(UPDATED_CAP).timestamp(UPDATED_TIMESTAMP).source(UPDATED_SOURCE);

        restTrackingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTracking.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTracking))
            )
            .andExpect(status().isOk());

        // Validate the Tracking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrackingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTracking, tracking), getPersistedTracking(tracking));
    }

    @Test
    @Transactional
    void fullUpdateTrackingWithPatch() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tracking using partial update
        Tracking partialUpdatedTracking = new Tracking();
        partialUpdatedTracking.setId(tracking.getId());

        partialUpdatedTracking
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE)
            .vitesse(UPDATED_VITESSE)
            .cap(UPDATED_CAP)
            .timestamp(UPDATED_TIMESTAMP)
            .source(UPDATED_SOURCE);

        restTrackingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTracking.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTracking))
            )
            .andExpect(status().isOk());

        // Validate the Tracking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrackingUpdatableFieldsEquals(partialUpdatedTracking, getPersistedTracking(partialUpdatedTracking));
    }

    @Test
    @Transactional
    void patchNonExistingTracking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tracking.setId(longCount.incrementAndGet());

        // Create the Tracking
        TrackingDTO trackingDTO = trackingMapper.toDto(tracking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trackingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trackingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTracking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tracking.setId(longCount.incrementAndGet());

        // Create the Tracking
        TrackingDTO trackingDTO = trackingMapper.toDto(tracking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trackingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTracking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tracking.setId(longCount.incrementAndGet());

        // Create the Tracking
        TrackingDTO trackingDTO = trackingMapper.toDto(tracking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trackingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTracking() throws Exception {
        // Initialize the database
        insertedTracking = trackingRepository.saveAndFlush(tracking);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tracking
        restTrackingMockMvc
            .perform(delete(ENTITY_API_URL_ID, tracking.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trackingRepository.count();
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

    protected Tracking getPersistedTracking(Tracking tracking) {
        return trackingRepository.findById(tracking.getId()).orElseThrow();
    }

    protected void assertPersistedTrackingToMatchAllProperties(Tracking expectedTracking) {
        assertTrackingAllPropertiesEquals(expectedTracking, getPersistedTracking(expectedTracking));
    }

    protected void assertPersistedTrackingToMatchUpdatableProperties(Tracking expectedTracking) {
        assertTrackingAllUpdatablePropertiesEquals(expectedTracking, getPersistedTracking(expectedTracking));
    }
}
