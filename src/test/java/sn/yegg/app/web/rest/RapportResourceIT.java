package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.RapportAsserts.*;
import static sn.yegg.app.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.yegg.app.domain.Operateur;
import sn.yegg.app.domain.Rapport;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.domain.enumeration.ReportFormat;
import sn.yegg.app.domain.enumeration.ReportType;
import sn.yegg.app.repository.RapportRepository;
import sn.yegg.app.service.dto.RapportDTO;
import sn.yegg.app.service.mapper.RapportMapper;

/**
 * Integration tests for the {@link RapportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RapportResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final ReportType DEFAULT_TYPE = ReportType.MONTHLY;
    private static final ReportType UPDATED_TYPE = ReportType.WEEKLY;

    private static final LocalDate DEFAULT_PERIODE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIODE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIODE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_PERIODE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PERIODE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PERIODE_FIN = LocalDate.ofEpochDay(-1L);

    private static final ReportFormat DEFAULT_FORMAT = ReportFormat.PDF;
    private static final ReportFormat UPDATED_FORMAT = ReportFormat.EXCEL;

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_GENERATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_GENERATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_GENERE_PAR = "AAAAAAAAAA";
    private static final String UPDATED_GENERE_PAR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rapports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RapportRepository rapportRepository;

    @Autowired
    private RapportMapper rapportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRapportMockMvc;

    private Rapport rapport;

    private Rapport insertedRapport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rapport createEntity() {
        return new Rapport()
            .nom(DEFAULT_NOM)
            .type(DEFAULT_TYPE)
            .periodeDebut(DEFAULT_PERIODE_DEBUT)
            .periodeFin(DEFAULT_PERIODE_FIN)
            .format(DEFAULT_FORMAT)
            .contenu(DEFAULT_CONTENU)
            .dateGeneration(DEFAULT_DATE_GENERATION)
            .generePar(DEFAULT_GENERE_PAR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rapport createUpdatedEntity() {
        return new Rapport()
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .periodeDebut(UPDATED_PERIODE_DEBUT)
            .periodeFin(UPDATED_PERIODE_FIN)
            .format(UPDATED_FORMAT)
            .contenu(UPDATED_CONTENU)
            .dateGeneration(UPDATED_DATE_GENERATION)
            .generePar(UPDATED_GENERE_PAR);
    }

    @BeforeEach
    void initTest() {
        rapport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRapport != null) {
            rapportRepository.delete(insertedRapport);
            insertedRapport = null;
        }
    }

    @Test
    @Transactional
    void createRapport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);
        var returnedRapportDTO = om.readValue(
            restRapportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RapportDTO.class
        );

        // Validate the Rapport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRapport = rapportMapper.toEntity(returnedRapportDTO);
        assertRapportUpdatableFieldsEquals(returnedRapport, getPersistedRapport(returnedRapport));

        insertedRapport = returnedRapport;
    }

    @Test
    @Transactional
    void createRapportWithExistingId() throws Exception {
        // Create the Rapport with an existing ID
        rapport.setId(1L);
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rapport.setNom(null);

        // Create the Rapport, which fails.
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rapport.setType(null);

        // Create the Rapport, which fails.
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodeDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rapport.setPeriodeDebut(null);

        // Create the Rapport, which fails.
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodeFinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rapport.setPeriodeFin(null);

        // Create the Rapport, which fails.
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateGenerationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rapport.setDateGeneration(null);

        // Create the Rapport, which fails.
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        restRapportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRapports() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList
        restRapportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rapport.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].periodeDebut").value(hasItem(DEFAULT_PERIODE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].periodeFin").value(hasItem(DEFAULT_PERIODE_FIN.toString())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].dateGeneration").value(hasItem(DEFAULT_DATE_GENERATION.toString())))
            .andExpect(jsonPath("$.[*].generePar").value(hasItem(DEFAULT_GENERE_PAR)));
    }

    @Test
    @Transactional
    void getRapport() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get the rapport
        restRapportMockMvc
            .perform(get(ENTITY_API_URL_ID, rapport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rapport.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.periodeDebut").value(DEFAULT_PERIODE_DEBUT.toString()))
            .andExpect(jsonPath("$.periodeFin").value(DEFAULT_PERIODE_FIN.toString()))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU))
            .andExpect(jsonPath("$.dateGeneration").value(DEFAULT_DATE_GENERATION.toString()))
            .andExpect(jsonPath("$.generePar").value(DEFAULT_GENERE_PAR));
    }

    @Test
    @Transactional
    void getRapportsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        Long id = rapport.getId();

        defaultRapportFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRapportFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRapportFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRapportsByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where nom equals to
        defaultRapportFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllRapportsByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where nom in
        defaultRapportFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllRapportsByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where nom is not null
        defaultRapportFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllRapportsByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where nom contains
        defaultRapportFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllRapportsByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where nom does not contain
        defaultRapportFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllRapportsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where type equals to
        defaultRapportFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRapportsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where type in
        defaultRapportFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllRapportsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where type is not null
        defaultRapportFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeDebut equals to
        defaultRapportFiltering("periodeDebut.equals=" + DEFAULT_PERIODE_DEBUT, "periodeDebut.equals=" + UPDATED_PERIODE_DEBUT);
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeDebut in
        defaultRapportFiltering(
            "periodeDebut.in=" + DEFAULT_PERIODE_DEBUT + "," + UPDATED_PERIODE_DEBUT,
            "periodeDebut.in=" + UPDATED_PERIODE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeDebut is not null
        defaultRapportFiltering("periodeDebut.specified=true", "periodeDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeDebut is greater than or equal to
        defaultRapportFiltering(
            "periodeDebut.greaterThanOrEqual=" + DEFAULT_PERIODE_DEBUT,
            "periodeDebut.greaterThanOrEqual=" + UPDATED_PERIODE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeDebut is less than or equal to
        defaultRapportFiltering(
            "periodeDebut.lessThanOrEqual=" + DEFAULT_PERIODE_DEBUT,
            "periodeDebut.lessThanOrEqual=" + SMALLER_PERIODE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeDebut is less than
        defaultRapportFiltering("periodeDebut.lessThan=" + UPDATED_PERIODE_DEBUT, "periodeDebut.lessThan=" + DEFAULT_PERIODE_DEBUT);
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeDebut is greater than
        defaultRapportFiltering("periodeDebut.greaterThan=" + SMALLER_PERIODE_DEBUT, "periodeDebut.greaterThan=" + DEFAULT_PERIODE_DEBUT);
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeFin equals to
        defaultRapportFiltering("periodeFin.equals=" + DEFAULT_PERIODE_FIN, "periodeFin.equals=" + UPDATED_PERIODE_FIN);
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeFin in
        defaultRapportFiltering("periodeFin.in=" + DEFAULT_PERIODE_FIN + "," + UPDATED_PERIODE_FIN, "periodeFin.in=" + UPDATED_PERIODE_FIN);
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeFin is not null
        defaultRapportFiltering("periodeFin.specified=true", "periodeFin.specified=false");
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeFin is greater than or equal to
        defaultRapportFiltering(
            "periodeFin.greaterThanOrEqual=" + DEFAULT_PERIODE_FIN,
            "periodeFin.greaterThanOrEqual=" + UPDATED_PERIODE_FIN
        );
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeFin is less than or equal to
        defaultRapportFiltering("periodeFin.lessThanOrEqual=" + DEFAULT_PERIODE_FIN, "periodeFin.lessThanOrEqual=" + SMALLER_PERIODE_FIN);
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeFinIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeFin is less than
        defaultRapportFiltering("periodeFin.lessThan=" + UPDATED_PERIODE_FIN, "periodeFin.lessThan=" + DEFAULT_PERIODE_FIN);
    }

    @Test
    @Transactional
    void getAllRapportsByPeriodeFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where periodeFin is greater than
        defaultRapportFiltering("periodeFin.greaterThan=" + SMALLER_PERIODE_FIN, "periodeFin.greaterThan=" + DEFAULT_PERIODE_FIN);
    }

    @Test
    @Transactional
    void getAllRapportsByFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where format equals to
        defaultRapportFiltering("format.equals=" + DEFAULT_FORMAT, "format.equals=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllRapportsByFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where format in
        defaultRapportFiltering("format.in=" + DEFAULT_FORMAT + "," + UPDATED_FORMAT, "format.in=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllRapportsByFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where format is not null
        defaultRapportFiltering("format.specified=true", "format.specified=false");
    }

    @Test
    @Transactional
    void getAllRapportsByDateGenerationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where dateGeneration equals to
        defaultRapportFiltering("dateGeneration.equals=" + DEFAULT_DATE_GENERATION, "dateGeneration.equals=" + UPDATED_DATE_GENERATION);
    }

    @Test
    @Transactional
    void getAllRapportsByDateGenerationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where dateGeneration in
        defaultRapportFiltering(
            "dateGeneration.in=" + DEFAULT_DATE_GENERATION + "," + UPDATED_DATE_GENERATION,
            "dateGeneration.in=" + UPDATED_DATE_GENERATION
        );
    }

    @Test
    @Transactional
    void getAllRapportsByDateGenerationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where dateGeneration is not null
        defaultRapportFiltering("dateGeneration.specified=true", "dateGeneration.specified=false");
    }

    @Test
    @Transactional
    void getAllRapportsByGenereParIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where generePar equals to
        defaultRapportFiltering("generePar.equals=" + DEFAULT_GENERE_PAR, "generePar.equals=" + UPDATED_GENERE_PAR);
    }

    @Test
    @Transactional
    void getAllRapportsByGenereParIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where generePar in
        defaultRapportFiltering("generePar.in=" + DEFAULT_GENERE_PAR + "," + UPDATED_GENERE_PAR, "generePar.in=" + UPDATED_GENERE_PAR);
    }

    @Test
    @Transactional
    void getAllRapportsByGenereParIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where generePar is not null
        defaultRapportFiltering("generePar.specified=true", "generePar.specified=false");
    }

    @Test
    @Transactional
    void getAllRapportsByGenereParContainsSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where generePar contains
        defaultRapportFiltering("generePar.contains=" + DEFAULT_GENERE_PAR, "generePar.contains=" + UPDATED_GENERE_PAR);
    }

    @Test
    @Transactional
    void getAllRapportsByGenereParNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        // Get all the rapportList where generePar does not contain
        defaultRapportFiltering("generePar.doesNotContain=" + UPDATED_GENERE_PAR, "generePar.doesNotContain=" + DEFAULT_GENERE_PAR);
    }

    @Test
    @Transactional
    void getAllRapportsByOperateurIsEqualToSomething() throws Exception {
        Operateur operateur;
        if (TestUtil.findAll(em, Operateur.class).isEmpty()) {
            rapportRepository.saveAndFlush(rapport);
            operateur = OperateurResourceIT.createEntity();
        } else {
            operateur = TestUtil.findAll(em, Operateur.class).get(0);
        }
        em.persist(operateur);
        em.flush();
        rapport.setOperateur(operateur);
        rapportRepository.saveAndFlush(rapport);
        Long operateurId = operateur.getId();
        // Get all the rapportList where operateur equals to operateurId
        defaultRapportShouldBeFound("operateurId.equals=" + operateurId);

        // Get all the rapportList where operateur equals to (operateurId + 1)
        defaultRapportShouldNotBeFound("operateurId.equals=" + (operateurId + 1));
    }

    @Test
    @Transactional
    void getAllRapportsByAdminIsEqualToSomething() throws Exception {
        Utilisateur admin;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            rapportRepository.saveAndFlush(rapport);
            admin = UtilisateurResourceIT.createEntity();
        } else {
            admin = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(admin);
        em.flush();
        rapport.setAdmin(admin);
        rapportRepository.saveAndFlush(rapport);
        Long adminId = admin.getId();
        // Get all the rapportList where admin equals to adminId
        defaultRapportShouldBeFound("adminId.equals=" + adminId);

        // Get all the rapportList where admin equals to (adminId + 1)
        defaultRapportShouldNotBeFound("adminId.equals=" + (adminId + 1));
    }

    private void defaultRapportFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRapportShouldBeFound(shouldBeFound);
        defaultRapportShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRapportShouldBeFound(String filter) throws Exception {
        restRapportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rapport.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].periodeDebut").value(hasItem(DEFAULT_PERIODE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].periodeFin").value(hasItem(DEFAULT_PERIODE_FIN.toString())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].dateGeneration").value(hasItem(DEFAULT_DATE_GENERATION.toString())))
            .andExpect(jsonPath("$.[*].generePar").value(hasItem(DEFAULT_GENERE_PAR)));

        // Check, that the count call also returns 1
        restRapportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRapportShouldNotBeFound(String filter) throws Exception {
        restRapportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRapportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRapport() throws Exception {
        // Get the rapport
        restRapportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRapport() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rapport
        Rapport updatedRapport = rapportRepository.findById(rapport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRapport are not directly saved in db
        em.detach(updatedRapport);
        updatedRapport
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .periodeDebut(UPDATED_PERIODE_DEBUT)
            .periodeFin(UPDATED_PERIODE_FIN)
            .format(UPDATED_FORMAT)
            .contenu(UPDATED_CONTENU)
            .dateGeneration(UPDATED_DATE_GENERATION)
            .generePar(UPDATED_GENERE_PAR);
        RapportDTO rapportDTO = rapportMapper.toDto(updatedRapport);

        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rapportDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO))
            )
            .andExpect(status().isOk());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRapportToMatchAllProperties(updatedRapport);
    }

    @Test
    @Transactional
    void putNonExistingRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rapportDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRapportWithPatch() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rapport using partial update
        Rapport partialUpdatedRapport = new Rapport();
        partialUpdatedRapport.setId(rapport.getId());

        partialUpdatedRapport.nom(UPDATED_NOM).type(UPDATED_TYPE).periodeFin(UPDATED_PERIODE_FIN).format(UPDATED_FORMAT);

        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRapport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRapport))
            )
            .andExpect(status().isOk());

        // Validate the Rapport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRapportUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRapport, rapport), getPersistedRapport(rapport));
    }

    @Test
    @Transactional
    void fullUpdateRapportWithPatch() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rapport using partial update
        Rapport partialUpdatedRapport = new Rapport();
        partialUpdatedRapport.setId(rapport.getId());

        partialUpdatedRapport
            .nom(UPDATED_NOM)
            .type(UPDATED_TYPE)
            .periodeDebut(UPDATED_PERIODE_DEBUT)
            .periodeFin(UPDATED_PERIODE_FIN)
            .format(UPDATED_FORMAT)
            .contenu(UPDATED_CONTENU)
            .dateGeneration(UPDATED_DATE_GENERATION)
            .generePar(UPDATED_GENERE_PAR);

        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRapport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRapport))
            )
            .andExpect(status().isOk());

        // Validate the Rapport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRapportUpdatableFieldsEquals(partialUpdatedRapport, getPersistedRapport(partialUpdatedRapport));
    }

    @Test
    @Transactional
    void patchNonExistingRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rapportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rapportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRapport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rapport.setId(longCount.incrementAndGet());

        // Create the Rapport
        RapportDTO rapportDTO = rapportMapper.toDto(rapport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRapportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rapportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rapport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRapport() throws Exception {
        // Initialize the database
        insertedRapport = rapportRepository.saveAndFlush(rapport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rapport
        restRapportMockMvc
            .perform(delete(ENTITY_API_URL_ID, rapport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rapportRepository.count();
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

    protected Rapport getPersistedRapport(Rapport rapport) {
        return rapportRepository.findById(rapport.getId()).orElseThrow();
    }

    protected void assertPersistedRapportToMatchAllProperties(Rapport expectedRapport) {
        assertRapportAllPropertiesEquals(expectedRapport, getPersistedRapport(expectedRapport));
    }

    protected void assertPersistedRapportToMatchUpdatableProperties(Rapport expectedRapport) {
        assertRapportAllUpdatablePropertiesEquals(expectedRapport, getPersistedRapport(expectedRapport));
    }
}
