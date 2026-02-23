package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.FeedbackAsserts.*;
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
import sn.yegg.app.domain.Feedback;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.repository.FeedbackRepository;
import sn.yegg.app.service.dto.FeedbackDTO;
import sn.yegg.app.service.mapper.FeedbackMapper;

/**
 * Integration tests for the {@link FeedbackResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeedbackResourceIT {

    private static final Integer DEFAULT_NOTE = 1;
    private static final Integer UPDATED_NOTE = 2;
    private static final Integer SMALLER_NOTE = 1 - 1;

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_OBJET = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_OBJET = "BBBBBBBBBB";

    private static final Long DEFAULT_OBJET_ID = 1L;
    private static final Long UPDATED_OBJET_ID = 2L;
    private static final Long SMALLER_OBJET_ID = 1L - 1L;

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ANONYME = false;
    private static final Boolean UPDATED_ANONYME = true;

    private static final String ENTITY_API_URL = "/api/feedbacks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackMockMvc;

    private Feedback feedback;

    private Feedback insertedFeedback;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedback createEntity() {
        return new Feedback()
            .note(DEFAULT_NOTE)
            .commentaire(DEFAULT_COMMENTAIRE)
            .typeObjet(DEFAULT_TYPE_OBJET)
            .objetId(DEFAULT_OBJET_ID)
            .dateCreation(DEFAULT_DATE_CREATION)
            .anonyme(DEFAULT_ANONYME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedback createUpdatedEntity() {
        return new Feedback()
            .note(UPDATED_NOTE)
            .commentaire(UPDATED_COMMENTAIRE)
            .typeObjet(UPDATED_TYPE_OBJET)
            .objetId(UPDATED_OBJET_ID)
            .dateCreation(UPDATED_DATE_CREATION)
            .anonyme(UPDATED_ANONYME);
    }

    @BeforeEach
    void initTest() {
        feedback = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFeedback != null) {
            feedbackRepository.delete(insertedFeedback);
            insertedFeedback = null;
        }
    }

    @Test
    @Transactional
    void createFeedback() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);
        var returnedFeedbackDTO = om.readValue(
            restFeedbackMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeedbackDTO.class
        );

        // Validate the Feedback in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFeedback = feedbackMapper.toEntity(returnedFeedbackDTO);
        assertFeedbackUpdatableFieldsEquals(returnedFeedback, getPersistedFeedback(returnedFeedback));

        insertedFeedback = returnedFeedback;
    }

    @Test
    @Transactional
    void createFeedbackWithExistingId() throws Exception {
        // Create the Feedback with an existing ID
        feedback.setId(1L);
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedback.setNote(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeObjetIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedback.setTypeObjet(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkObjetIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedback.setObjetId(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateCreationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedback.setDateCreation(null);

        // Create the Feedback, which fails.
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeedbacks() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedback.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].typeObjet").value(hasItem(DEFAULT_TYPE_OBJET)))
            .andExpect(jsonPath("$.[*].objetId").value(hasItem(DEFAULT_OBJET_ID.intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].anonyme").value(hasItem(DEFAULT_ANONYME)));
    }

    @Test
    @Transactional
    void getFeedback() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get the feedback
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL_ID, feedback.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedback.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.typeObjet").value(DEFAULT_TYPE_OBJET))
            .andExpect(jsonPath("$.objetId").value(DEFAULT_OBJET_ID.intValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.anonyme").value(DEFAULT_ANONYME));
    }

    @Test
    @Transactional
    void getFeedbacksByIdFiltering() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        Long id = feedback.getId();

        defaultFeedbackFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFeedbackFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFeedbackFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFeedbacksByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where note equals to
        defaultFeedbackFiltering("note.equals=" + DEFAULT_NOTE, "note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllFeedbacksByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where note in
        defaultFeedbackFiltering("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE, "note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    void getAllFeedbacksByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where note is not null
        defaultFeedbackFiltering("note.specified=true", "note.specified=false");
    }

    @Test
    @Transactional
    void getAllFeedbacksByNoteIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where note is greater than or equal to
        defaultFeedbackFiltering("note.greaterThanOrEqual=" + DEFAULT_NOTE, "note.greaterThanOrEqual=" + (DEFAULT_NOTE + 1));
    }

    @Test
    @Transactional
    void getAllFeedbacksByNoteIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where note is less than or equal to
        defaultFeedbackFiltering("note.lessThanOrEqual=" + DEFAULT_NOTE, "note.lessThanOrEqual=" + SMALLER_NOTE);
    }

    @Test
    @Transactional
    void getAllFeedbacksByNoteIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where note is less than
        defaultFeedbackFiltering("note.lessThan=" + (DEFAULT_NOTE + 1), "note.lessThan=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllFeedbacksByNoteIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where note is greater than
        defaultFeedbackFiltering("note.greaterThan=" + SMALLER_NOTE, "note.greaterThan=" + DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void getAllFeedbacksByTypeObjetIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where typeObjet equals to
        defaultFeedbackFiltering("typeObjet.equals=" + DEFAULT_TYPE_OBJET, "typeObjet.equals=" + UPDATED_TYPE_OBJET);
    }

    @Test
    @Transactional
    void getAllFeedbacksByTypeObjetIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where typeObjet in
        defaultFeedbackFiltering("typeObjet.in=" + DEFAULT_TYPE_OBJET + "," + UPDATED_TYPE_OBJET, "typeObjet.in=" + UPDATED_TYPE_OBJET);
    }

    @Test
    @Transactional
    void getAllFeedbacksByTypeObjetIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where typeObjet is not null
        defaultFeedbackFiltering("typeObjet.specified=true", "typeObjet.specified=false");
    }

    @Test
    @Transactional
    void getAllFeedbacksByTypeObjetContainsSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where typeObjet contains
        defaultFeedbackFiltering("typeObjet.contains=" + DEFAULT_TYPE_OBJET, "typeObjet.contains=" + UPDATED_TYPE_OBJET);
    }

    @Test
    @Transactional
    void getAllFeedbacksByTypeObjetNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where typeObjet does not contain
        defaultFeedbackFiltering("typeObjet.doesNotContain=" + UPDATED_TYPE_OBJET, "typeObjet.doesNotContain=" + DEFAULT_TYPE_OBJET);
    }

    @Test
    @Transactional
    void getAllFeedbacksByObjetIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where objetId equals to
        defaultFeedbackFiltering("objetId.equals=" + DEFAULT_OBJET_ID, "objetId.equals=" + UPDATED_OBJET_ID);
    }

    @Test
    @Transactional
    void getAllFeedbacksByObjetIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where objetId in
        defaultFeedbackFiltering("objetId.in=" + DEFAULT_OBJET_ID + "," + UPDATED_OBJET_ID, "objetId.in=" + UPDATED_OBJET_ID);
    }

    @Test
    @Transactional
    void getAllFeedbacksByObjetIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where objetId is not null
        defaultFeedbackFiltering("objetId.specified=true", "objetId.specified=false");
    }

    @Test
    @Transactional
    void getAllFeedbacksByObjetIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where objetId is greater than or equal to
        defaultFeedbackFiltering("objetId.greaterThanOrEqual=" + DEFAULT_OBJET_ID, "objetId.greaterThanOrEqual=" + UPDATED_OBJET_ID);
    }

    @Test
    @Transactional
    void getAllFeedbacksByObjetIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where objetId is less than or equal to
        defaultFeedbackFiltering("objetId.lessThanOrEqual=" + DEFAULT_OBJET_ID, "objetId.lessThanOrEqual=" + SMALLER_OBJET_ID);
    }

    @Test
    @Transactional
    void getAllFeedbacksByObjetIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where objetId is less than
        defaultFeedbackFiltering("objetId.lessThan=" + UPDATED_OBJET_ID, "objetId.lessThan=" + DEFAULT_OBJET_ID);
    }

    @Test
    @Transactional
    void getAllFeedbacksByObjetIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where objetId is greater than
        defaultFeedbackFiltering("objetId.greaterThan=" + SMALLER_OBJET_ID, "objetId.greaterThan=" + DEFAULT_OBJET_ID);
    }

    @Test
    @Transactional
    void getAllFeedbacksByDateCreationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where dateCreation equals to
        defaultFeedbackFiltering("dateCreation.equals=" + DEFAULT_DATE_CREATION, "dateCreation.equals=" + UPDATED_DATE_CREATION);
    }

    @Test
    @Transactional
    void getAllFeedbacksByDateCreationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where dateCreation in
        defaultFeedbackFiltering(
            "dateCreation.in=" + DEFAULT_DATE_CREATION + "," + UPDATED_DATE_CREATION,
            "dateCreation.in=" + UPDATED_DATE_CREATION
        );
    }

    @Test
    @Transactional
    void getAllFeedbacksByDateCreationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where dateCreation is not null
        defaultFeedbackFiltering("dateCreation.specified=true", "dateCreation.specified=false");
    }

    @Test
    @Transactional
    void getAllFeedbacksByAnonymeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where anonyme equals to
        defaultFeedbackFiltering("anonyme.equals=" + DEFAULT_ANONYME, "anonyme.equals=" + UPDATED_ANONYME);
    }

    @Test
    @Transactional
    void getAllFeedbacksByAnonymeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where anonyme in
        defaultFeedbackFiltering("anonyme.in=" + DEFAULT_ANONYME + "," + UPDATED_ANONYME, "anonyme.in=" + UPDATED_ANONYME);
    }

    @Test
    @Transactional
    void getAllFeedbacksByAnonymeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList where anonyme is not null
        defaultFeedbackFiltering("anonyme.specified=true", "anonyme.specified=false");
    }

    @Test
    @Transactional
    void getAllFeedbacksByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            feedbackRepository.saveAndFlush(feedback);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        feedback.setUtilisateur(utilisateur);
        feedbackRepository.saveAndFlush(feedback);
        Long utilisateurId = utilisateur.getId();
        // Get all the feedbackList where utilisateur equals to utilisateurId
        defaultFeedbackShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the feedbackList where utilisateur equals to (utilisateurId + 1)
        defaultFeedbackShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultFeedbackFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFeedbackShouldBeFound(shouldBeFound);
        defaultFeedbackShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFeedbackShouldBeFound(String filter) throws Exception {
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedback.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].typeObjet").value(hasItem(DEFAULT_TYPE_OBJET)))
            .andExpect(jsonPath("$.[*].objetId").value(hasItem(DEFAULT_OBJET_ID.intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].anonyme").value(hasItem(DEFAULT_ANONYME)));

        // Check, that the count call also returns 1
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFeedbackShouldNotBeFound(String filter) throws Exception {
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFeedback() throws Exception {
        // Get the feedback
        restFeedbackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedback() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedback
        Feedback updatedFeedback = feedbackRepository.findById(feedback.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeedback are not directly saved in db
        em.detach(updatedFeedback);
        updatedFeedback
            .note(UPDATED_NOTE)
            .commentaire(UPDATED_COMMENTAIRE)
            .typeObjet(UPDATED_TYPE_OBJET)
            .objetId(UPDATED_OBJET_ID)
            .dateCreation(UPDATED_DATE_CREATION)
            .anonyme(UPDATED_ANONYME);
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(updatedFeedback);

        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackDTO))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeedbackToMatchAllProperties(updatedFeedback);
    }

    @Test
    @Transactional
    void putNonExistingFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackWithPatch() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedback using partial update
        Feedback partialUpdatedFeedback = new Feedback();
        partialUpdatedFeedback.setId(feedback.getId());

        partialUpdatedFeedback.commentaire(UPDATED_COMMENTAIRE).typeObjet(UPDATED_TYPE_OBJET);

        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedback.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedback))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFeedback, feedback), getPersistedFeedback(feedback));
    }

    @Test
    @Transactional
    void fullUpdateFeedbackWithPatch() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedback using partial update
        Feedback partialUpdatedFeedback = new Feedback();
        partialUpdatedFeedback.setId(feedback.getId());

        partialUpdatedFeedback
            .note(UPDATED_NOTE)
            .commentaire(UPDATED_COMMENTAIRE)
            .typeObjet(UPDATED_TYPE_OBJET)
            .objetId(UPDATED_OBJET_ID)
            .dateCreation(UPDATED_DATE_CREATION)
            .anonyme(UPDATED_ANONYME);

        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedback.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedback))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackUpdatableFieldsEquals(partialUpdatedFeedback, getPersistedFeedback(partialUpdatedFeedback));
    }

    @Test
    @Transactional
    void patchNonExistingFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedbackDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // Create the Feedback
        FeedbackDTO feedbackDTO = feedbackMapper.toDto(feedback);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feedbackDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedback() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feedback
        restFeedbackMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedback.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feedbackRepository.count();
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

    protected Feedback getPersistedFeedback(Feedback feedback) {
        return feedbackRepository.findById(feedback.getId()).orElseThrow();
    }

    protected void assertPersistedFeedbackToMatchAllProperties(Feedback expectedFeedback) {
        assertFeedbackAllPropertiesEquals(expectedFeedback, getPersistedFeedback(expectedFeedback));
    }

    protected void assertPersistedFeedbackToMatchUpdatableProperties(Feedback expectedFeedback) {
        assertFeedbackAllUpdatablePropertiesEquals(expectedFeedback, getPersistedFeedback(expectedFeedback));
    }
}
