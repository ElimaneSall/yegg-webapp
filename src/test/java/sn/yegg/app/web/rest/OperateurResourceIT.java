package sn.yegg.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static sn.yegg.app.domain.OperateurAsserts.*;
import static sn.yegg.app.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import sn.yegg.app.domain.Operateur;
import sn.yegg.app.repository.OperateurRepository;
import sn.yegg.app.service.dto.OperateurDTO;
import sn.yegg.app.service.mapper.OperateurMapper;

/**
 * Integration tests for the {@link OperateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OperateurResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "18698515548";
    private static final String UPDATED_TELEPHONE = "806000152487427";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LOGO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LOGO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_LOGO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LOGO_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final String ENTITY_API_URL = "/api/operateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OperateurRepository operateurRepository;

    @Autowired
    private OperateurMapper operateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOperateurMockMvc;

    private Operateur operateur;

    private Operateur insertedOperateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operateur createEntity() {
        return new Operateur()
            .nom(DEFAULT_NOM)
            .email(DEFAULT_EMAIL)
            .telephone(DEFAULT_TELEPHONE)
            .adresse(DEFAULT_ADRESSE)
            .logo(DEFAULT_LOGO)
            .logoContentType(DEFAULT_LOGO_CONTENT_TYPE)
            .actif(DEFAULT_ACTIF);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Operateur createUpdatedEntity() {
        return new Operateur()
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .adresse(UPDATED_ADRESSE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .actif(UPDATED_ACTIF);
    }

    @BeforeEach
    void initTest() {
        operateur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOperateur != null) {
            operateurRepository.delete(insertedOperateur);
            insertedOperateur = null;
        }
    }

    @Test
    @Transactional
    void createOperateur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);
        var returnedOperateurDTO = om.readValue(
            restOperateurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operateurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OperateurDTO.class
        );

        // Validate the Operateur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOperateur = operateurMapper.toEntity(returnedOperateurDTO);
        assertOperateurUpdatableFieldsEquals(returnedOperateur, getPersistedOperateur(returnedOperateur));

        insertedOperateur = returnedOperateur;
    }

    @Test
    @Transactional
    void createOperateurWithExistingId() throws Exception {
        // Create the Operateur with an existing ID
        operateur.setId(1L);
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOperateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        operateur.setNom(null);

        // Create the Operateur, which fails.
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        restOperateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        operateur.setEmail(null);

        // Create the Operateur, which fails.
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        restOperateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        operateur.setActif(null);

        // Create the Operateur, which fails.
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        restOperateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOperateurs() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList
        restOperateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));
    }

    @Test
    @Transactional
    void getOperateur() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get the operateur
        restOperateurMockMvc
            .perform(get(ENTITY_API_URL_ID, operateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(operateur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.logoContentType").value(DEFAULT_LOGO_CONTENT_TYPE))
            .andExpect(jsonPath("$.logo").value(Base64.getEncoder().encodeToString(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF));
    }

    @Test
    @Transactional
    void getOperateursByIdFiltering() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        Long id = operateur.getId();

        defaultOperateurFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOperateurFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOperateurFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOperateursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where nom equals to
        defaultOperateurFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllOperateursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where nom in
        defaultOperateurFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllOperateursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where nom is not null
        defaultOperateurFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllOperateursByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where nom contains
        defaultOperateurFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllOperateursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where nom does not contain
        defaultOperateurFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllOperateursByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where email equals to
        defaultOperateurFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOperateursByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where email in
        defaultOperateurFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOperateursByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where email is not null
        defaultOperateurFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllOperateursByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where email contains
        defaultOperateurFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOperateursByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where email does not contain
        defaultOperateurFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllOperateursByTelephoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where telephone equals to
        defaultOperateurFiltering("telephone.equals=" + DEFAULT_TELEPHONE, "telephone.equals=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllOperateursByTelephoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where telephone in
        defaultOperateurFiltering("telephone.in=" + DEFAULT_TELEPHONE + "," + UPDATED_TELEPHONE, "telephone.in=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllOperateursByTelephoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where telephone is not null
        defaultOperateurFiltering("telephone.specified=true", "telephone.specified=false");
    }

    @Test
    @Transactional
    void getAllOperateursByTelephoneContainsSomething() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where telephone contains
        defaultOperateurFiltering("telephone.contains=" + DEFAULT_TELEPHONE, "telephone.contains=" + UPDATED_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllOperateursByTelephoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where telephone does not contain
        defaultOperateurFiltering("telephone.doesNotContain=" + UPDATED_TELEPHONE, "telephone.doesNotContain=" + DEFAULT_TELEPHONE);
    }

    @Test
    @Transactional
    void getAllOperateursByActifIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where actif equals to
        defaultOperateurFiltering("actif.equals=" + DEFAULT_ACTIF, "actif.equals=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllOperateursByActifIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where actif in
        defaultOperateurFiltering("actif.in=" + DEFAULT_ACTIF + "," + UPDATED_ACTIF, "actif.in=" + UPDATED_ACTIF);
    }

    @Test
    @Transactional
    void getAllOperateursByActifIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        // Get all the operateurList where actif is not null
        defaultOperateurFiltering("actif.specified=true", "actif.specified=false");
    }

    private void defaultOperateurFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOperateurShouldBeFound(shouldBeFound);
        defaultOperateurShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOperateurShouldBeFound(String filter) throws Exception {
        restOperateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(operateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].logoContentType").value(hasItem(DEFAULT_LOGO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_LOGO))))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)));

        // Check, that the count call also returns 1
        restOperateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOperateurShouldNotBeFound(String filter) throws Exception {
        restOperateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOperateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOperateur() throws Exception {
        // Get the operateur
        restOperateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOperateur() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the operateur
        Operateur updatedOperateur = operateurRepository.findById(operateur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOperateur are not directly saved in db
        em.detach(updatedOperateur);
        updatedOperateur
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .adresse(UPDATED_ADRESSE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .actif(UPDATED_ACTIF);
        OperateurDTO operateurDTO = operateurMapper.toDto(updatedOperateur);

        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(operateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Operateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOperateurToMatchAllProperties(updatedOperateur);
    }

    @Test
    @Transactional
    void putNonExistingOperateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operateur.setId(longCount.incrementAndGet());

        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, operateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(operateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOperateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operateur.setId(longCount.incrementAndGet());

        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(operateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOperateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operateur.setId(longCount.incrementAndGet());

        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(operateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOperateurWithPatch() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the operateur using partial update
        Operateur partialUpdatedOperateur = new Operateur();
        partialUpdatedOperateur.setId(operateur.getId());

        partialUpdatedOperateur.nom(UPDATED_NOM).email(UPDATED_EMAIL).telephone(UPDATED_TELEPHONE).adresse(UPDATED_ADRESSE);

        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOperateur))
            )
            .andExpect(status().isOk());

        // Validate the Operateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOperateurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOperateur, operateur),
            getPersistedOperateur(operateur)
        );
    }

    @Test
    @Transactional
    void fullUpdateOperateurWithPatch() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the operateur using partial update
        Operateur partialUpdatedOperateur = new Operateur();
        partialUpdatedOperateur.setId(operateur.getId());

        partialUpdatedOperateur
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .telephone(UPDATED_TELEPHONE)
            .adresse(UPDATED_ADRESSE)
            .logo(UPDATED_LOGO)
            .logoContentType(UPDATED_LOGO_CONTENT_TYPE)
            .actif(UPDATED_ACTIF);

        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOperateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOperateur))
            )
            .andExpect(status().isOk());

        // Validate the Operateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOperateurUpdatableFieldsEquals(partialUpdatedOperateur, getPersistedOperateur(partialUpdatedOperateur));
    }

    @Test
    @Transactional
    void patchNonExistingOperateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operateur.setId(longCount.incrementAndGet());

        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, operateurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(operateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOperateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operateur.setId(longCount.incrementAndGet());

        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(operateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Operateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOperateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        operateur.setId(longCount.incrementAndGet());

        // Create the Operateur
        OperateurDTO operateurDTO = operateurMapper.toDto(operateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOperateurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(operateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Operateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOperateur() throws Exception {
        // Initialize the database
        insertedOperateur = operateurRepository.saveAndFlush(operateur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the operateur
        restOperateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, operateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return operateurRepository.count();
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

    protected Operateur getPersistedOperateur(Operateur operateur) {
        return operateurRepository.findById(operateur.getId()).orElseThrow();
    }

    protected void assertPersistedOperateurToMatchAllProperties(Operateur expectedOperateur) {
        assertOperateurAllPropertiesEquals(expectedOperateur, getPersistedOperateur(expectedOperateur));
    }

    protected void assertPersistedOperateurToMatchUpdatableProperties(Operateur expectedOperateur) {
        assertOperateurAllUpdatablePropertiesEquals(expectedOperateur, getPersistedOperateur(expectedOperateur));
    }
}
