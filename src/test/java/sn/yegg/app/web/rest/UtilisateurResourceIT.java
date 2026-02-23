package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.UtilisateurAsserts.*;
import static sn.yegg.app.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
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
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.domain.enumeration.UserRole;
import sn.yegg.app.repository.UtilisateurRepository;
import sn.yegg.app.service.dto.UtilisateurDTO;
import sn.yegg.app.service.mapper.UtilisateurMapper;

/**
 * Integration tests for the {@link UtilisateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UtilisateurResourceIT {

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_MOT_DE_PASSE = "AAAAAAAAAA";
    private static final String UPDATED_MOT_DE_PASSE = "BBBBBBBBBB";

    private static final UserRole DEFAULT_ROLE = UserRole.PASSENGER;
    private static final UserRole UPDATED_ROLE = UserRole.TOURIST;

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String DEFAULT_FCM_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_FCM_TOKEN = "BBBBBBBBBB";

    private static final Boolean DEFAULT_NOTIFICATIONS_PUSH = false;
    private static final Boolean UPDATED_NOTIFICATIONS_PUSH = true;

    private static final Boolean DEFAULT_NOTIFICATIONS_SMS = false;
    private static final Boolean UPDATED_NOTIFICATIONS_SMS = true;

    private static final String DEFAULT_LANGUE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DERNIERE_CONNEXION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DERNIERE_CONNEXION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final LocalDate DEFAULT_DATE_EMBAUCHE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EMBAUCHE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_EMBAUCHE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NUMERO_PERMIS = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_PERMIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/utilisateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurMapper utilisateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtilisateurMockMvc;

    private Utilisateur utilisateur;

    private Utilisateur insertedUtilisateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createEntity() {
        return new Utilisateur()
            .prenom(DEFAULT_PRENOM)
            .nom(DEFAULT_NOM)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .motDePasse(DEFAULT_MOT_DE_PASSE)
            .role(DEFAULT_ROLE)
            .matricule(DEFAULT_MATRICULE)
            .fcmToken(DEFAULT_FCM_TOKEN)
            .notificationsPush(DEFAULT_NOTIFICATIONS_PUSH)
            .notificationsSms(DEFAULT_NOTIFICATIONS_SMS)
            .langue(DEFAULT_LANGUE)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .derniereConnexion(DEFAULT_DERNIERE_CONNEXION)
            .actif(DEFAULT_ACTIF)
            .dateEmbauche(DEFAULT_DATE_EMBAUCHE)
            .numeroPermis(DEFAULT_NUMERO_PERMIS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createUpdatedEntity() {
        return new Utilisateur()
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .role(UPDATED_ROLE)
            .matricule(UPDATED_MATRICULE)
            .fcmToken(UPDATED_FCM_TOKEN)
            .notificationsPush(UPDATED_NOTIFICATIONS_PUSH)
            .notificationsSms(UPDATED_NOTIFICATIONS_SMS)
            .langue(UPDATED_LANGUE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .dateCreation(UPDATED_DATE_CREATION)
            .derniereConnexion(UPDATED_DERNIERE_CONNEXION)
            .actif(UPDATED_ACTIF)
            .dateEmbauche(UPDATED_DATE_EMBAUCHE)
            .numeroPermis(UPDATED_NUMERO_PERMIS);
    }

    @BeforeEach
    void initTest() {
        utilisateur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUtilisateur != null) {
            utilisateurRepository.delete(insertedUtilisateur);
            insertedUtilisateur = null;
        }
    }

    @Test
    @Transactional
    void createUtilisateur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);
        var returnedUtilisateurDTO = om.readValue(
            restUtilisateurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UtilisateurDTO.class
        );

        // Validate the Utilisateur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUtilisateur = utilisateurMapper.toEntity(returnedUtilisateurDTO);
        assertUtilisateurUpdatableFieldsEquals(returnedUtilisateur, getPersistedUtilisateur(returnedUtilisateur));

        insertedUtilisateur = returnedUtilisateur;
    }

    @Test
    @Transactional
    void createUtilisateurWithExistingId() throws Exception {
        // Create the Utilisateur with an existing ID
        utilisateur.setId(1L);
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setRole(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setDateCreation(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setActif(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUtilisateurs() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].motDePasse").value(hasItem(DEFAULT_MOT_DE_PASSE)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].fcmToken").value(hasItem(DEFAULT_FCM_TOKEN)))
            .andExpect(jsonPath("$.[*].notificationsPush").value(hasItem(DEFAULT_NOTIFICATIONS_PUSH)))
            .andExpect(jsonPath("$.[*].notificationsSms").value(hasItem(DEFAULT_NOTIFICATIONS_SMS)))
            .andExpect(jsonPath("$.[*].langue").value(hasItem(DEFAULT_LANGUE)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].derniereConnexion").value(hasItem(DEFAULT_DERNIERE_CONNEXION.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)))
            .andExpect(jsonPath("$.[*].dateEmbauche").value(hasItem(DEFAULT_DATE_EMBAUCHE.toString())))
            .andExpect(jsonPath("$.[*].numeroPermis").value(hasItem(DEFAULT_NUMERO_PERMIS)));
    }

    @Test
    @Transactional
    void getUtilisateur() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get the utilisateur
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL_ID, utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utilisateur.getId().intValue()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.motDePasse").value(DEFAULT_MOT_DE_PASSE))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
            .andExpect(jsonPath("$.fcmToken").value(DEFAULT_FCM_TOKEN))
            .andExpect(jsonPath("$.notificationsPush").value(DEFAULT_NOTIFICATIONS_PUSH))
            .andExpect(jsonPath("$.notificationsSms").value(DEFAULT_NOTIFICATIONS_SMS))
            .andExpect(jsonPath("$.langue").value(DEFAULT_LANGUE))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64.getEncoder().encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.derniereConnexion").value(DEFAULT_DERNIERE_CONNEXION.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF))
            .andExpect(jsonPath("$.dateEmbauche").value(DEFAULT_DATE_EMBAUCHE.toString()))
            .andExpect(jsonPath("$.numeroPermis").value(DEFAULT_NUMERO_PERMIS));
    }

    @Test
    @Transactional
    void getUtilisateursByIdFiltering() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        Long id = utilisateur.getId();

        defaultUtilisateurFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUtilisateurFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUtilisateurFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom equals to
        defaultUtilisateurFiltering("prenom.equals=" + DEFAULT_PRENOM, "prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom in
        defaultUtilisateurFiltering("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM, "prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom is not null
        defaultUtilisateurFiltering("prenom.specified=true", "prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom contains
        defaultUtilisateurFiltering("prenom.contains=" + DEFAULT_PRENOM, "prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom does not contain
        defaultUtilisateurFiltering("prenom.doesNotContain=" + UPDATED_PRENOM, "prenom.doesNotContain=" + DEFAULT_PRENOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom equals to
        defaultUtilisateurFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom in
        defaultUtilisateurFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom is not null
        defaultUtilisateurFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom contains
        defaultUtilisateurFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom does not contain
        defaultUtilisateurFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email equals to
        defaultUtilisateurFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email in
        defaultUtilisateurFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email is not null
        defaultUtilisateurFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email contains
        defaultUtilisateurFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email does not contain
        defaultUtilisateurFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllUtilisateursByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where telephone equals to
        defaultUtilisateurFiltering("telephone.equals=" + DEFAULT_TELEPHONE, "telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where telephone in
        defaultUtilisateurFiltering("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE, "telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where telephone is not null
        defaultUtilisateurFiltering("telephone.specified=true", "telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where telephone contains
        defaultUtilisateurFiltering("telephone.contains=" + DEFAULT_TELEPHONE, "telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where telephone does not contain
        defaultUtilisateurFiltering("telephone.doesNotContain=" + UPDATED_TELEPHONE, "telephone.doesNotContain=" + DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByMotDePasseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where motDePasse equals to
        defaultUtilisateurFiltering("motDePasse.equals=" + DEFAULT_MOT_DE_PASSE, "motDePasse.equals=" + UPDATED_MOT_DE_PASSE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByMotDePasseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where motDePasse in
        defaultUtilisateurFiltering(
            "motDePasse.in=" + DEFAULT_MOT_DE_PASSE + "," + UPDATED_MOT_DE_PASSE,
            "motDePasse.in=" + UPDATED_MOT_DE_PASSE
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByMotDePasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where motDePasse is not null
        defaultUtilisateurFiltering("motDePasse.specified=true", "motDePasse.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByMotDePasseContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where motDePasse contains
        defaultUtilisateurFiltering("motDePasse.contains=" + DEFAULT_MOT_DE_PASSE, "motDePasse.contains=" + UPDATED_MOT_DE_PASSE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByMotDePasseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where motDePasse does not contain
        defaultUtilisateurFiltering(
            "motDePasse.doesNotContain=" + UPDATED_MOT_DE_PASSE,
            "motDePasse.doesNotContain=" + DEFAULT_MOT_DE_PASSE
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where role equals to
        defaultUtilisateurFiltering("role.equals=" + DEFAULT_ROLE, "role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where role in
        defaultUtilisateurFiltering("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE, "role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where role is not null
        defaultUtilisateurFiltering("role.specified=true", "role.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByMatriculeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where matricule equals to
        defaultUtilisateurFiltering("matricule.equals=" + DEFAULT_MATRICULE, "matricule.equals=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByMatriculeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where matricule in
        defaultUtilisateurFiltering("matricule.in=" + DEFAULT_MATRICULE + "," + UPDATED_MATRICULE, "matricule.in=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByMatriculeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where matricule is not null
        defaultUtilisateurFiltering("matricule.specified=true", "matricule.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByMatriculeContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where matricule contains
        defaultUtilisateurFiltering("matricule.contains=" + DEFAULT_MATRICULE, "matricule.contains=" + UPDATED_MATRICULE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByMatriculeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where matricule does not contain
        defaultUtilisateurFiltering("matricule.doesNotContain=" + UPDATED_MATRICULE, "matricule.doesNotContain=" + DEFAULT_MATRICULE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByFcmTokenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where fcmToken equals to
        defaultUtilisateurFiltering("fcmToken.equals=" + DEFAULT_FCM_TOKEN, "fcmToken.equals=" + UPDATED_FCM_TOKEN);
    }

    @Test
    @Transactional
    void getAllUtilisateursByFcmTokenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where fcmToken in
        defaultUtilisateurFiltering("fcmToken.in=" + DEFAULT_FCM_TOKEN + "," + UPDATED_FCM_TOKEN, "fcmToken.in=" + UPDATED_FCM_TOKEN);
    }

    @Test
    @Transactional
    void getAllUtilisateursByFcmTokenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where fcmToken is not null
        defaultUtilisateurFiltering("fcmToken.specified=true", "fcmToken.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByFcmTokenContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where fcmToken contains
        defaultUtilisateurFiltering("fcmToken.contains=" + DEFAULT_FCM_TOKEN, "fcmToken.contains=" + UPDATED_FCM_TOKEN);
    }

    @Test
    @Transactional
    void getAllUtilisateursByFcmTokenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where fcmToken does not contain
        defaultUtilisateurFiltering("fcmToken.doesNotContain=" + UPDATED_FCM_TOKEN, "fcmToken.doesNotContain=" + DEFAULT_FCM_TOKEN);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNotificationsPushIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where notificationsPush equals to
        defaultUtilisateurFiltering(
            "notificationsPush.equals=" + DEFAULT_NOTIFICATIONS_PUSH,
            "notificationsPush.equals=" + UPDATED_NOTIFICATIONS_PUSH
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByNotificationsPushIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where notificationsPush in
        defaultUtilisateurFiltering(
            "notificationsPush.in=" + DEFAULT_NOTIFICATIONS_PUSH + "," + UPDATED_NOTIFICATIONS_PUSH,
            "notificationsPush.in=" + UPDATED_NOTIFICATIONS_PUSH
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByNotificationsPushIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where notificationsPush is not null
        defaultUtilisateurFiltering("notificationsPush.specified=true", "notificationsPush.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByNotificationsSmsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where notificationsSms equals to
        defaultUtilisateurFiltering(
            "notificationsSms.equals=" + DEFAULT_NOTIFICATIONS_SMS,
            "notificationsSms.equals=" + UPDATED_NOTIFICATIONS_SMS
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByNotificationsSmsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where notificationsSms in
        defaultUtilisateurFiltering(
            "notificationsSms.in=" + DEFAULT_NOTIFICATIONS_SMS + "," + UPDATED_NOTIFICATIONS_SMS,
            "notificationsSms.in=" + UPDATED_NOTIFICATIONS_SMS
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByNotificationsSmsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where notificationsSms is not null
        defaultUtilisateurFiltering("notificationsSms.specified=true", "notificationsSms.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByLangueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where langue equals to
        defaultUtilisateurFiltering("langue.equals=" + DEFAULT_LANGUE, "langue.equals=" + UPDATED_LANGUE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByLangueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where langue in
        defaultUtilisateurFiltering("langue.in=" + DEFAULT_LANGUE + "," + UPDATED_LANGUE, "langue.in=" + UPDATED_LANGUE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByLangueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where langue is not null
        defaultUtilisateurFiltering("langue.specified=true", "langue.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByLangueContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where langue contains
        defaultUtilisateurFiltering("langue.contains=" + DEFAULT_LANGUE, "langue.contains=" + UPDATED_LANGUE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByLangueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where langue does not contain
        defaultUtilisateurFiltering("langue.doesNotContain=" + UPDATED_LANGUE, "langue.doesNotContain=" + DEFAULT_LANGUE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateCreation equals to
        defaultUtilisateurFiltering("dateCreation.equals=" + DEFAULT_DATE_CREATION, "dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateCreation in
        defaultUtilisateurFiltering(
            "dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION,
            "dateCreation.in=" + UPDATED_DATE_CREATION
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateCreation is not null
        defaultUtilisateurFiltering("dateCreation.specified=true", "dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByDerniereConnexionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where derniereConnexion equals to
        defaultUtilisateurFiltering(
            "derniereConnexion.equals=" + DEFAULT_DERNIERE_CONNEXION,
            "derniereConnexion.equals=" + UPDATED_DERNIERE_CONNEXION
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByDerniereConnexionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where derniereConnexion in
        defaultUtilisateurFiltering(
            "derniereConnexion.in=" + DEFAULT_DERNIERE_CONNEXION + "," + UPDATED_DERNIERE_CONNEXION,
            "derniereConnexion.in=" + UPDATED_DERNIERE_CONNEXION
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByDerniereConnexionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where derniereConnexion is not null
        defaultUtilisateurFiltering("derniereConnexion.specified=true", "derniereConnexion.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where actif equals to
        defaultUtilisateurFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllUtilisateursByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where actif in
        defaultUtilisateurFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllUtilisateursByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where actif is not null
        defaultUtilisateurFiltering("actif.specified=true", "actif.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateEmbaucheIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateEmbauche equals to
        defaultUtilisateurFiltering("dateEmbauche.equals=" + DEFAULT_DATE_EMBAUCHE, "dateEmbauche.equals=" + UPDATED_DATE_EMBAUCHE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateEmbaucheIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateEmbauche in
        defaultUtilisateurFiltering(
            "dateEmbauche.in=" + DEFAULT_DATE_EMBAUCHE + "," + UPDATED_DATE_EMBAUCHE,
            "dateEmbauche.in=" + UPDATED_DATE_EMBAUCHE
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateEmbaucheIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateEmbauche is not null
        defaultUtilisateurFiltering("dateEmbauche.specified=true", "dateEmbauche.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateEmbaucheIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateEmbauche is greater than or equal to
        defaultUtilisateurFiltering(
            "dateEmbauche.greaterThanOrEqual=" + DEFAULT_DATE_EMBAUCHE,
            "dateEmbauche.greaterThanOrEqual=" + UPDATED_DATE_EMBAUCHE
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateEmbaucheIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateEmbauche is less than or equal to
        defaultUtilisateurFiltering(
            "dateEmbauche.lessThanOrEqual=" + DEFAULT_DATE_EMBAUCHE,
            "dateEmbauche.lessThanOrEqual=" + SMALLER_DATE_EMBAUCHE
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateEmbaucheIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateEmbauche is less than
        defaultUtilisateurFiltering("dateEmbauche.lessThan=" + UPDATED_DATE_EMBAUCHE, "dateEmbauche.lessThan=" + DEFAULT_DATE_EMBAUCHE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByDateEmbaucheIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where dateEmbauche is greater than
        defaultUtilisateurFiltering(
            "dateEmbauche.greaterThan=" + SMALLER_DATE_EMBAUCHE,
            "dateEmbauche.greaterThan=" + DEFAULT_DATE_EMBAUCHE
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByNumeroPermisIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where numeroPermis equals to
        defaultUtilisateurFiltering("numeroPermis.equals=" + DEFAULT_NUMERO_PERMIS, "numeroPermis.equals=" + UPDATED_NUMERO_PERMIS);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNumeroPermisIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where numeroPermis in
        defaultUtilisateurFiltering(
            "numeroPermis.in=" + DEFAULT_NUMERO_PERMIS + "," + UPDATED_NUMERO_PERMIS,
            "numeroPermis.in=" + UPDATED_NUMERO_PERMIS
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByNumeroPermisIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where numeroPermis is not null
        defaultUtilisateurFiltering("numeroPermis.specified=true", "numeroPermis.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByNumeroPermisContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where numeroPermis contains
        defaultUtilisateurFiltering("numeroPermis.contains=" + DEFAULT_NUMERO_PERMIS, "numeroPermis.contains=" + UPDATED_NUMERO_PERMIS);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNumeroPermisNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where numeroPermis does not contain
        defaultUtilisateurFiltering(
            "numeroPermis.doesNotContain=" + UPDATED_NUMERO_PERMIS,
            "numeroPermis.doesNotContain=" + DEFAULT_NUMERO_PERMIS
        );
    }

    private void defaultUtilisateurFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUtilisateurShouldBeFound(shouldBeFound);
        defaultUtilisateurShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUtilisateurShouldBeFound(String filter) throws Exception {
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].motDePasse").value(hasItem(DEFAULT_MOT_DE_PASSE)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].fcmToken").value(hasItem(DEFAULT_FCM_TOKEN)))
            .andExpect(jsonPath("$.[*].notificationsPush").value(hasItem(DEFAULT_NOTIFICATIONS_PUSH)))
            .andExpect(jsonPath("$.[*].notificationsSms").value(hasItem(DEFAULT_NOTIFICATIONS_SMS)))
            .andExpect(jsonPath("$.[*].langue").value(hasItem(DEFAULT_LANGUE)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].derniereConnexion").value(hasItem(DEFAULT_DERNIERE_CONNEXION.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)))
            .andExpect(jsonPath("$.[*].dateEmbauche").value(hasItem(DEFAULT_DATE_EMBAUCHE.toString())))
            .andExpect(jsonPath("$.[*].numeroPermis").value(hasItem(DEFAULT_NUMERO_PERMIS)));

        // Check, that the count call also returns 1
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUtilisateurShouldNotBeFound(String filter) throws Exception {
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUtilisateur() throws Exception {
        // Get the utilisateur
        restUtilisateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUtilisateur() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateur
        Utilisateur updatedUtilisateur = utilisateurRepository.findById(utilisateur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUtilisateur are not directly saved in db
        em.detach(updatedUtilisateur);
        updatedUtilisateur
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .role(UPDATED_ROLE)
            .matricule(UPDATED_MATRICULE)
            .fcmToken(UPDATED_FCM_TOKEN)
            .notificationsPush(UPDATED_NOTIFICATIONS_PUSH)
            .notificationsSms(UPDATED_NOTIFICATIONS_SMS)
            .langue(UPDATED_LANGUE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .dateCreation(UPDATED_DATE_CREATION)
            .derniereConnexion(UPDATED_DERNIERE_CONNEXION)
            .actif(UPDATED_ACTIF)
            .dateEmbauche(UPDATED_DATE_EMBAUCHE)
            .numeroPermis(UPDATED_NUMERO_PERMIS);
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(updatedUtilisateur);

        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUtilisateurToMatchAllProperties(updatedUtilisateur);
    }

    @Test
    @Transactional
    void putNonExistingUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur
            .telephone(UPDATED_TELEPHONE)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .matricule(UPDATED_MATRICULE)
            .notificationsPush(UPDATED_NOTIFICATIONS_PUSH)
            .notificationsSms(UPDATED_NOTIFICATIONS_SMS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .dateCreation(UPDATED_DATE_CREATION)
            .numeroPermis(UPDATED_NUMERO_PERMIS);

        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtilisateurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUtilisateur, utilisateur),
            getPersistedUtilisateur(utilisateur)
        );
    }

    @Test
    @Transactional
    void fullUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .role(UPDATED_ROLE)
            .matricule(UPDATED_MATRICULE)
            .fcmToken(UPDATED_FCM_TOKEN)
            .notificationsPush(UPDATED_NOTIFICATIONS_PUSH)
            .notificationsSms(UPDATED_NOTIFICATIONS_SMS)
            .langue(UPDATED_LANGUE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .dateCreation(UPDATED_DATE_CREATION)
            .derniereConnexion(UPDATED_DERNIERE_CONNEXION)
            .actif(UPDATED_ACTIF)
            .dateEmbauche(UPDATED_DATE_EMBAUCHE)
            .numeroPermis(UPDATED_NUMERO_PERMIS);

        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtilisateurUpdatableFieldsEquals(partialUpdatedUtilisateur, getPersistedUtilisateur(partialUpdatedUtilisateur));
    }

    @Test
    @Transactional
    void patchNonExistingUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utilisateurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtilisateur() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the utilisateur
        restUtilisateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, utilisateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return utilisateurRepository.count();
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

    protected Utilisateur getPersistedUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.findById(utilisateur.getId()).orElseThrow();
    }

    protected void assertPersistedUtilisateurToMatchAllProperties(Utilisateur expectedUtilisateur) {
        assertUtilisateurAllPropertiesEquals(expectedUtilisateur, getPersistedUtilisateur(expectedUtilisateur));
    }

    protected void assertPersistedUtilisateurToMatchUpdatableProperties(Utilisateur expectedUtilisateur) {
        assertUtilisateurAllUpdatablePropertiesEquals(expectedUtilisateur, getPersistedUtilisateur(expectedUtilisateur));
    }
}
