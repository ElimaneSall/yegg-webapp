package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.LigneAsserts.*;
import static sn.yegg.app.web.rest.TestUtil.createUpdateProxyForBean;
import static sn.yegg.app.web.rest.TestUtil.sameNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
import sn.yegg.app.domain.Ligne;
import sn.yegg.app.domain.Operateur;
import sn.yegg.app.domain.enumeration.LineStatus;
import sn.yegg.app.repository.LigneRepository;
import sn.yegg.app.service.dto.LigneDTO;
import sn.yegg.app.service.mapper.LigneMapper;

/**
 * Integration tests for the {@link LigneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LigneResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COULEUR = "AAAAAAAAAA";
    private static final String UPDATED_COULEUR = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DISTANCE_KM = new BigDecimal(0);
    private static final BigDecimal UPDATED_DISTANCE_KM = new BigDecimal(1);
    private static final BigDecimal SMALLER_DISTANCE_KM = new BigDecimal(0 - 1);

    private static final Integer DEFAULT_DUREE_MOYENNE = 0;
    private static final Integer UPDATED_DUREE_MOYENNE = 1;
    private static final Integer SMALLER_DUREE_MOYENNE = 0 - 1;

    private static final Integer DEFAULT_FREQUENCE = 1;
    private static final Integer UPDATED_FREQUENCE = 2;
    private static final Integer SMALLER_FREQUENCE = 1 - 1;

    private static final LineStatus DEFAULT_STATUT = LineStatus.ACTIVE;
    private static final LineStatus UPDATED_STATUT = LineStatus.SUSPENDED;

    private static final String DEFAULT_JOURS_FERIES = "AAAAAAAAAA";
    private static final String UPDATED_JOURS_FERIES = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/lignes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LigneRepository ligneRepository;

    @Autowired
    private LigneMapper ligneMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLigneMockMvc;

    private Ligne ligne;

    private Ligne insertedLigne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ligne createEntity() {
        return new Ligne()
            .numero(DEFAULT_NUMERO)
            .nom(DEFAULT_NOM)
            .direction(DEFAULT_DIRECTION)
            .description(DEFAULT_DESCRIPTION)
            .couleur(DEFAULT_COULEUR)
            .distanceKm(DEFAULT_DISTANCE_KM)
            .dureeMoyenne(DEFAULT_DUREE_MOYENNE)
            .frequence(DEFAULT_FREQUENCE)
            .statut(DEFAULT_STATUT)
            .joursFeries(DEFAULT_JOURS_FERIES)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ligne createUpdatedEntity() {
        return new Ligne()
            .numero(UPDATED_NUMERO)
            .nom(UPDATED_NOM)
            .direction(UPDATED_DIRECTION)
            .description(UPDATED_DESCRIPTION)
            .couleur(UPDATED_COULEUR)
            .distanceKm(UPDATED_DISTANCE_KM)
            .dureeMoyenne(UPDATED_DUREE_MOYENNE)
            .frequence(UPDATED_FREQUENCE)
            .statut(UPDATED_STATUT)
            .joursFeries(UPDATED_JOURS_FERIES)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        ligne = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLigne != null) {
            ligneRepository.delete(insertedLigne);
            insertedLigne = null;
        }
    }

    @Test
    @Transactional
    void createLigne() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Ligne
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);
        var returnedLigneDTO = om.readValue(
            restLigneMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LigneDTO.class
        );

        // Validate the Ligne in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLigne = ligneMapper.toEntity(returnedLigneDTO);
        assertLigneUpdatableFieldsEquals(returnedLigne, getPersistedLigne(returnedLigne));

        insertedLigne = returnedLigne;
    }

    @Test
    @Transactional
    void createLigneWithExistingId() throws Exception {
        // Create the Ligne with an existing ID
        ligne.setId(1L);
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLigneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ligne in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ligne.setNumero(null);

        // Create the Ligne, which fails.
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);

        restLigneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ligne.setNom(null);

        // Create the Ligne, which fails.
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);

        restLigneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDirectionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ligne.setDirection(null);

        // Create the Ligne, which fails.
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);

        restLigneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ligne.setStatut(null);

        // Create the Ligne, which fails.
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);

        restLigneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ligne.setActif(null);

        // Create the Ligne, which fails.
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);

        restLigneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLignes() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList
        restLigneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligne.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].couleur").value(hasItem(DEFAULT_COULEUR)))
            .andExpect(jsonPath("$.[*].distanceKm").value(hasItem(sameNumber(DEFAULT_DISTANCE_KM))))
            .andExpect(jsonPath("$.[*].dureeMoyenne").value(hasItem(DEFAULT_DUREE_MOYENNE)))
            .andExpect(jsonPath("$.[*].frequence").value(hasItem(DEFAULT_FREQUENCE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].joursFeries").value(hasItem(DEFAULT_JOURS_FERIES)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getLigne() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get the ligne
        restLigneMockMvc
            .perform(get(ENTITY_API_URL_ID, ligne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ligne.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.direction").value(DEFAULT_DIRECTION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.couleur").value(DEFAULT_COULEUR))
            .andExpect(jsonPath("$.distanceKm").value(sameNumber(DEFAULT_DISTANCE_KM)))
            .andExpect(jsonPath("$.dureeMoyenne").value(DEFAULT_DUREE_MOYENNE))
            .andExpect(jsonPath("$.frequence").value(DEFAULT_FREQUENCE))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.joursFeries").value(DEFAULT_JOURS_FERIES))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getLignesByIdFiltering() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        Long id = ligne.getId();

        defaultLigneFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLigneFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLigneFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLignesByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where numero equals to
        defaultLigneFiltering("numero.equals=" + DEFAULT_NUMERO, "numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllLignesByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where numero in
        defaultLigneFiltering("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO, "numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllLignesByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where numero is not null
        defaultLigneFiltering("numero.specified=true", "numero.specified=false");
    }

    @Test
    @Transactional
    void getAllLignesByNumeroContainsSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where numero contains
        defaultLigneFiltering("numero.contains=" + DEFAULT_NUMERO, "numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllLignesByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where numero does not contain
        defaultLigneFiltering("numero.doesNotContain=" + UPDATED_NUMERO, "numero.doesNotContain=" + DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void getAllLignesByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where nom equals to
        defaultLigneFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllLignesByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where nom in
        defaultLigneFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllLignesByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where nom is not null
        defaultLigneFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllLignesByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where nom contains
        defaultLigneFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllLignesByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where nom does not contain
        defaultLigneFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllLignesByDirectionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where direction equals to
        defaultLigneFiltering("direction.equals=" + DEFAULT_DIRECTION, "direction.equals=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllLignesByDirectionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where direction in
        defaultLigneFiltering("direction.in=" + DEFAULT_DIRECTION + "," + UPDATED_DIRECTION, "direction.in=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllLignesByDirectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where direction is not null
        defaultLigneFiltering("direction.specified=true", "direction.specified=false");
    }

    @Test
    @Transactional
    void getAllLignesByDirectionContainsSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where direction contains
        defaultLigneFiltering("direction.contains=" + DEFAULT_DIRECTION, "direction.contains=" + UPDATED_DIRECTION);
    }

    @Test
    @Transactional
    void getAllLignesByDirectionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where direction does not contain
        defaultLigneFiltering("direction.doesNotContain=" + UPDATED_DIRECTION, "direction.doesNotContain=" + DEFAULT_DIRECTION);
    }

    @Test
    @Transactional
    void getAllLignesByCouleurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where couleur equals to
        defaultLigneFiltering("couleur.equals=" + DEFAULT_COULEUR, "couleur.equals=" + UPDATED_COULEUR);
    }

    @Test
    @Transactional
    void getAllLignesByCouleurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where couleur in
        defaultLigneFiltering("couleur.in=" + DEFAULT_COULEUR + "," + UPDATED_COULEUR, "couleur.in=" + UPDATED_COULEUR);
    }

    @Test
    @Transactional
    void getAllLignesByCouleurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where couleur is not null
        defaultLigneFiltering("couleur.specified=true", "couleur.specified=false");
    }

    @Test
    @Transactional
    void getAllLignesByCouleurContainsSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where couleur contains
        defaultLigneFiltering("couleur.contains=" + DEFAULT_COULEUR, "couleur.contains=" + UPDATED_COULEUR);
    }

    @Test
    @Transactional
    void getAllLignesByCouleurNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where couleur does not contain
        defaultLigneFiltering("couleur.doesNotContain=" + UPDATED_COULEUR, "couleur.doesNotContain=" + DEFAULT_COULEUR);
    }

    @Test
    @Transactional
    void getAllLignesByDistanceKmIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where distanceKm equals to
        defaultLigneFiltering("distanceKm.equals=" + DEFAULT_DISTANCE_KM, "distanceKm.equals=" + UPDATED_DISTANCE_KM);
    }

    @Test
    @Transactional
    void getAllLignesByDistanceKmIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where distanceKm in
        defaultLigneFiltering("distanceKm.in=" + DEFAULT_DISTANCE_KM + "," + UPDATED_DISTANCE_KM, "distanceKm.in=" + UPDATED_DISTANCE_KM);
    }

    @Test
    @Transactional
    void getAllLignesByDistanceKmIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where distanceKm is not null
        defaultLigneFiltering("distanceKm.specified=true", "distanceKm.specified=false");
    }

    @Test
    @Transactional
    void getAllLignesByDistanceKmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where distanceKm is greater than or equal to
        defaultLigneFiltering(
            "distanceKm.greaterThanOrEqual=" + DEFAULT_DISTANCE_KM,
            "distanceKm.greaterThanOrEqual=" + UPDATED_DISTANCE_KM
        );
    }

    @Test
    @Transactional
    void getAllLignesByDistanceKmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where distanceKm is less than or equal to
        defaultLigneFiltering("distanceKm.lessThanOrEqual=" + DEFAULT_DISTANCE_KM, "distanceKm.lessThanOrEqual=" + SMALLER_DISTANCE_KM);
    }

    @Test
    @Transactional
    void getAllLignesByDistanceKmIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where distanceKm is less than
        defaultLigneFiltering("distanceKm.lessThan=" + UPDATED_DISTANCE_KM, "distanceKm.lessThan=" + DEFAULT_DISTANCE_KM);
    }

    @Test
    @Transactional
    void getAllLignesByDistanceKmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where distanceKm is greater than
        defaultLigneFiltering("distanceKm.greaterThan=" + SMALLER_DISTANCE_KM, "distanceKm.greaterThan=" + DEFAULT_DISTANCE_KM);
    }

    @Test
    @Transactional
    void getAllLignesByDureeMoyenneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dureeMoyenne equals to
        defaultLigneFiltering("dureeMoyenne.equals=" + DEFAULT_DUREE_MOYENNE, "dureeMoyenne.equals=" + UPDATED_DUREE_MOYENNE);
    }

    @Test
    @Transactional
    void getAllLignesByDureeMoyenneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dureeMoyenne in
        defaultLigneFiltering(
            "dureeMoyenne.in=" + DEFAULT_DUREE_MOYENNE + "," + UPDATED_DUREE_MOYENNE,
            "dureeMoyenne.in=" + UPDATED_DUREE_MOYENNE
        );
    }

    @Test
    @Transactional
    void getAllLignesByDureeMoyenneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dureeMoyenne is not null
        defaultLigneFiltering("dureeMoyenne.specified=true", "dureeMoyenne.specified=false");
    }

    @Test
    @Transactional
    void getAllLignesByDureeMoyenneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dureeMoyenne is greater than or equal to
        defaultLigneFiltering(
            "dureeMoyenne.greaterThanOrEqual=" + DEFAULT_DUREE_MOYENNE,
            "dureeMoyenne.greaterThanOrEqual=" + UPDATED_DUREE_MOYENNE
        );
    }

    @Test
    @Transactional
    void getAllLignesByDureeMoyenneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dureeMoyenne is less than or equal to
        defaultLigneFiltering(
            "dureeMoyenne.lessThanOrEqual=" + DEFAULT_DUREE_MOYENNE,
            "dureeMoyenne.lessThanOrEqual=" + SMALLER_DUREE_MOYENNE
        );
    }

    @Test
    @Transactional
    void getAllLignesByDureeMoyenneIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dureeMoyenne is less than
        defaultLigneFiltering("dureeMoyenne.lessThan=" + UPDATED_DUREE_MOYENNE, "dureeMoyenne.lessThan=" + DEFAULT_DUREE_MOYENNE);
    }

    @Test
    @Transactional
    void getAllLignesByDureeMoyenneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dureeMoyenne is greater than
        defaultLigneFiltering("dureeMoyenne.greaterThan=" + SMALLER_DUREE_MOYENNE, "dureeMoyenne.greaterThan=" + DEFAULT_DUREE_MOYENNE);
    }

    @Test
    @Transactional
    void getAllLignesByFrequenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where frequence equals to
        defaultLigneFiltering("frequence.equals=" + DEFAULT_FREQUENCE, "frequence.equals=" + UPDATED_FREQUENCE);
    }

    @Test
    @Transactional
    void getAllLignesByFrequenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where frequence in
        defaultLigneFiltering("frequence.in=" + DEFAULT_FREQUENCE + "," + UPDATED_FREQUENCE, "frequence.in=" + UPDATED_FREQUENCE);
    }

    @Test
    @Transactional
    void getAllLignesByFrequenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where frequence is not null
        defaultLigneFiltering("frequence.specified=true", "frequence.specified=false");
    }

    @Test
    @Transactional
    void getAllLignesByFrequenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where frequence is greater than or equal to
        defaultLigneFiltering(
            "frequence.greaterThanOrEqual=" + DEFAULT_FREQUENCE,
            "frequence.greaterThanOrEqual=" + (DEFAULT_FREQUENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllLignesByFrequenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where frequence is less than or equal to
        defaultLigneFiltering("frequence.lessThanOrEqual=" + DEFAULT_FREQUENCE, "frequence.lessThanOrEqual=" + SMALLER_FREQUENCE);
    }

    @Test
    @Transactional
    void getAllLignesByFrequenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where frequence is less than
        defaultLigneFiltering("frequence.lessThan=" + (DEFAULT_FREQUENCE + 1), "frequence.lessThan=" + DEFAULT_FREQUENCE);
    }

    @Test
    @Transactional
    void getAllLignesByFrequenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where frequence is greater than
        defaultLigneFiltering("frequence.greaterThan=" + SMALLER_FREQUENCE, "frequence.greaterThan=" + DEFAULT_FREQUENCE);
    }

    @Test
    @Transactional
    void getAllLignesByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where statut equals to
        defaultLigneFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllLignesByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where statut in
        defaultLigneFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllLignesByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where statut is not null
        defaultLigneFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllLignesByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateDebut equals to
        defaultLigneFiltering("dateDebut.equals=" + DEFAULT_DATE_DEBUT, "dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllLignesByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateDebut in
        defaultLigneFiltering("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT, "dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllLignesByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateDebut is not null
        defaultLigneFiltering("dateDebut.specified=true", "dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllLignesByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateDebut is greater than or equal to
        defaultLigneFiltering("dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT, "dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllLignesByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateDebut is less than or equal to
        defaultLigneFiltering("dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT, "dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllLignesByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateDebut is less than
        defaultLigneFiltering("dateDebut.lessThan=" + UPDATED_DATE_DEBUT, "dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllLignesByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateDebut is greater than
        defaultLigneFiltering("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT, "dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllLignesByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateFin equals to
        defaultLigneFiltering("dateFin.equals=" + DEFAULT_DATE_FIN, "dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllLignesByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateFin in
        defaultLigneFiltering("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN, "dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllLignesByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateFin is not null
        defaultLigneFiltering("dateFin.specified=true", "dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllLignesByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateFin is greater than or equal to
        defaultLigneFiltering("dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN, "dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllLignesByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateFin is less than or equal to
        defaultLigneFiltering("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN, "dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllLignesByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateFin is less than
        defaultLigneFiltering("dateFin.lessThan=" + UPDATED_DATE_FIN, "dateFin.lessThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllLignesByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where dateFin is greater than
        defaultLigneFiltering("dateFin.greaterThan=" + SMALLER_DATE_FIN, "dateFin.greaterThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllLignesByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where actif equals to
        defaultLigneFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllLignesByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where actif in
        defaultLigneFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllLignesByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        // Get all the ligneList where actif is not null
        defaultLigneFiltering("actif.specified=true", "actif.specified=false");
    }

    @Test
    @Transactional
    void getAllLignesByOperateurIsEqualToSomething() throws Exception {
        Operateur operateur;
        if (TestUtil.findAll(em, Operateur.class).isEmpty()) {
            ligneRepository.saveAndFlush(ligne);
            operateur = OperateurResourceIT.createEntity();
        } else {
            operateur = TestUtil.findAll(em, Operateur.class).get(0);
        }
        em.persist(operateur);
        em.flush();
        ligne.setOperateur(operateur);
        ligneRepository.saveAndFlush(ligne);
        Long operateurId = operateur.getId();
        // Get all the ligneList where operateur equals to operateurId
        defaultLigneShouldBeFound("operateurId.equals=" + operateurId);

        // Get all the ligneList where operateur equals to (operateurId + 1)
        defaultLigneShouldNotBeFound("operateurId.equals=" + (operateurId + 1));
    }

    private void defaultLigneFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLigneShouldBeFound(shouldBeFound);
        defaultLigneShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLigneShouldBeFound(String filter) throws Exception {
        restLigneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ligne.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].direction").value(hasItem(DEFAULT_DIRECTION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].couleur").value(hasItem(DEFAULT_COULEUR)))
            .andExpect(jsonPath("$.[*].distanceKm").value(hasItem(sameNumber(DEFAULT_DISTANCE_KM))))
            .andExpect(jsonPath("$.[*].dureeMoyenne").value(hasItem(DEFAULT_DUREE_MOYENNE)))
            .andExpect(jsonPath("$.[*].frequence").value(hasItem(DEFAULT_FREQUENCE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].joursFeries").value(hasItem(DEFAULT_JOURS_FERIES)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));

        // Check, that the count call also returns 1
        restLigneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLigneShouldNotBeFound(String filter) throws Exception {
        restLigneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLigneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLigne() throws Exception {
        // Get the ligne
        restLigneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLigne() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ligne
        Ligne updatedLigne = ligneRepository.findById(ligne.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLigne are not directly saved in db
        em.detach(updatedLigne);
        updatedLigne
            .numero(UPDATED_NUMERO)
            .nom(UPDATED_NOM)
            .direction(UPDATED_DIRECTION)
            .description(UPDATED_DESCRIPTION)
            .couleur(UPDATED_COULEUR)
            .distanceKm(UPDATED_DISTANCE_KM)
            .dureeMoyenne(UPDATED_DUREE_MOYENNE)
            .frequence(UPDATED_FREQUENCE)
            .statut(UPDATED_STATUT)
            .joursFeries(UPDATED_JOURS_FERIES)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .actif(UPDATED_ACTIF);
        LigneDTO ligneDTO = ligneMapper.toDto(updatedLigne);

        restLigneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligneDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ligne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLigneToMatchAllProperties(updatedLigne);
    }

    @Test
    @Transactional
    void putNonExistingLigne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligne.setId(longCount.incrementAndGet());

        // Create the Ligne
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ligneDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ligne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLigne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligne.setId(longCount.incrementAndGet());

        // Create the Ligne
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ligneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ligne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLigne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligne.setId(longCount.incrementAndGet());

        // Create the Ligne
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ligneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ligne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLigneWithPatch() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ligne using partial update
        Ligne partialUpdatedLigne = new Ligne();
        partialUpdatedLigne.setId(ligne.getId());

        partialUpdatedLigne
            .direction(UPDATED_DIRECTION)
            .description(UPDATED_DESCRIPTION)
            .couleur(UPDATED_COULEUR)
            .distanceKm(UPDATED_DISTANCE_KM)
            .dureeMoyenne(UPDATED_DUREE_MOYENNE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restLigneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigne.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLigne))
            )
            .andExpect(status().isOk());

        // Validate the Ligne in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLigneUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLigne, ligne), getPersistedLigne(ligne));
    }

    @Test
    @Transactional
    void fullUpdateLigneWithPatch() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ligne using partial update
        Ligne partialUpdatedLigne = new Ligne();
        partialUpdatedLigne.setId(ligne.getId());

        partialUpdatedLigne
            .numero(UPDATED_NUMERO)
            .nom(UPDATED_NOM)
            .direction(UPDATED_DIRECTION)
            .description(UPDATED_DESCRIPTION)
            .couleur(UPDATED_COULEUR)
            .distanceKm(UPDATED_DISTANCE_KM)
            .dureeMoyenne(UPDATED_DUREE_MOYENNE)
            .frequence(UPDATED_FREQUENCE)
            .statut(UPDATED_STATUT)
            .joursFeries(UPDATED_JOURS_FERIES)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .actif(UPDATED_ACTIF);

        restLigneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLigne.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLigne))
            )
            .andExpect(status().isOk());

        // Validate the Ligne in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLigneUpdatableFieldsEquals(partialUpdatedLigne, getPersistedLigne(partialUpdatedLigne));
    }

    @Test
    @Transactional
    void patchNonExistingLigne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligne.setId(longCount.incrementAndGet());

        // Create the Ligne
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLigneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ligneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ligneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ligne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLigne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligne.setId(longCount.incrementAndGet());

        // Create the Ligne
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ligneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ligne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLigne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ligne.setId(longCount.incrementAndGet());

        // Create the Ligne
        LigneDTO ligneDTO = ligneMapper.toDto(ligne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLigneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ligneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ligne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLigne() throws Exception {
        // Initialize the database
        insertedLigne = ligneRepository.saveAndFlush(ligne);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ligne
        restLigneMockMvc
            .perform(delete(ENTITY_API_URL_ID, ligne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ligneRepository.count();
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

    protected Ligne getPersistedLigne(Ligne ligne) {
        return ligneRepository.findById(ligne.getId()).orElseThrow();
    }

    protected void assertPersistedLigneToMatchAllProperties(Ligne expectedLigne) {
        assertLigneAllPropertiesEquals(expectedLigne, getPersistedLigne(expectedLigne));
    }

    protected void assertPersistedLigneToMatchUpdatableProperties(Ligne expectedLigne) {
        assertLigneAllUpdatablePropertiesEquals(expectedLigne, getPersistedLigne(expectedLigne));
    }
}
