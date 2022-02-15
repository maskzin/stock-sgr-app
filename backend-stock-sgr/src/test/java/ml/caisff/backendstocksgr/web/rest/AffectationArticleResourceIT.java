package ml.caisff.backendstocksgr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ml.caisff.backendstocksgr.IntegrationTest;
import ml.caisff.backendstocksgr.domain.AffectationArticle;
import ml.caisff.backendstocksgr.repository.AffectationArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AffectationArticleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AffectationArticleResourceIT {

    private static final Boolean DEFAULT_IS_AFFECTATION = false;
    private static final Boolean UPDATED_IS_AFFECTATION = true;

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/affectation-articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AffectationArticleRepository affectationArticleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAffectationArticleMockMvc;

    private AffectationArticle affectationArticle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AffectationArticle createEntity(EntityManager em) {
        AffectationArticle affectationArticle = new AffectationArticle()
            .isAffectation(DEFAULT_IS_AFFECTATION)
            .quantite(DEFAULT_QUANTITE)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return affectationArticle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AffectationArticle createUpdatedEntity(EntityManager em) {
        AffectationArticle affectationArticle = new AffectationArticle()
            .isAffectation(UPDATED_IS_AFFECTATION)
            .quantite(UPDATED_QUANTITE)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);
        return affectationArticle;
    }

    @BeforeEach
    public void initTest() {
        affectationArticle = createEntity(em);
    }

    @Test
    @Transactional
    void createAffectationArticle() throws Exception {
        int databaseSizeBeforeCreate = affectationArticleRepository.findAll().size();
        // Create the AffectationArticle
        restAffectationArticleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(affectationArticle))
            )
            .andExpect(status().isCreated());

        // Validate the AffectationArticle in the database
        List<AffectationArticle> affectationArticleList = affectationArticleRepository.findAll();
        assertThat(affectationArticleList).hasSize(databaseSizeBeforeCreate + 1);
        AffectationArticle testAffectationArticle = affectationArticleList.get(affectationArticleList.size() - 1);
        assertThat(testAffectationArticle.getIsAffectation()).isEqualTo(DEFAULT_IS_AFFECTATION);
        assertThat(testAffectationArticle.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testAffectationArticle.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAffectationArticle.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    void createAffectationArticleWithExistingId() throws Exception {
        // Create the AffectationArticle with an existing ID
        affectationArticle.setId(1L);

        int databaseSizeBeforeCreate = affectationArticleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAffectationArticleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(affectationArticle))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationArticle in the database
        List<AffectationArticle> affectationArticleList = affectationArticleRepository.findAll();
        assertThat(affectationArticleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAffectationArticles() throws Exception {
        // Initialize the database
        affectationArticleRepository.saveAndFlush(affectationArticle);

        // Get all the affectationArticleList
        restAffectationArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affectationArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].isAffectation").value(hasItem(DEFAULT_IS_AFFECTATION.booleanValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    void getAffectationArticle() throws Exception {
        // Initialize the database
        affectationArticleRepository.saveAndFlush(affectationArticle);

        // Get the affectationArticle
        restAffectationArticleMockMvc
            .perform(get(ENTITY_API_URL_ID, affectationArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(affectationArticle.getId().intValue()))
            .andExpect(jsonPath("$.isAffectation").value(DEFAULT_IS_AFFECTATION.booleanValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAffectationArticle() throws Exception {
        // Get the affectationArticle
        restAffectationArticleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAffectationArticle() throws Exception {
        // Initialize the database
        affectationArticleRepository.saveAndFlush(affectationArticle);

        int databaseSizeBeforeUpdate = affectationArticleRepository.findAll().size();

        // Update the affectationArticle
        AffectationArticle updatedAffectationArticle = affectationArticleRepository.findById(affectationArticle.getId()).get();
        // Disconnect from session so that the updates on updatedAffectationArticle are not directly saved in db
        em.detach(updatedAffectationArticle);
        updatedAffectationArticle
            .isAffectation(UPDATED_IS_AFFECTATION)
            .quantite(UPDATED_QUANTITE)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        restAffectationArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAffectationArticle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAffectationArticle))
            )
            .andExpect(status().isOk());

        // Validate the AffectationArticle in the database
        List<AffectationArticle> affectationArticleList = affectationArticleRepository.findAll();
        assertThat(affectationArticleList).hasSize(databaseSizeBeforeUpdate);
        AffectationArticle testAffectationArticle = affectationArticleList.get(affectationArticleList.size() - 1);
        assertThat(testAffectationArticle.getIsAffectation()).isEqualTo(UPDATED_IS_AFFECTATION);
        assertThat(testAffectationArticle.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testAffectationArticle.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAffectationArticle.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void putNonExistingAffectationArticle() throws Exception {
        int databaseSizeBeforeUpdate = affectationArticleRepository.findAll().size();
        affectationArticle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affectationArticle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectationArticle))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationArticle in the database
        List<AffectationArticle> affectationArticleList = affectationArticleRepository.findAll();
        assertThat(affectationArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAffectationArticle() throws Exception {
        int databaseSizeBeforeUpdate = affectationArticleRepository.findAll().size();
        affectationArticle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(affectationArticle))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationArticle in the database
        List<AffectationArticle> affectationArticleList = affectationArticleRepository.findAll();
        assertThat(affectationArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAffectationArticle() throws Exception {
        int databaseSizeBeforeUpdate = affectationArticleRepository.findAll().size();
        affectationArticle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationArticleMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(affectationArticle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AffectationArticle in the database
        List<AffectationArticle> affectationArticleList = affectationArticleRepository.findAll();
        assertThat(affectationArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAffectationArticleWithPatch() throws Exception {
        // Initialize the database
        affectationArticleRepository.saveAndFlush(affectationArticle);

        int databaseSizeBeforeUpdate = affectationArticleRepository.findAll().size();

        // Update the affectationArticle using partial update
        AffectationArticle partialUpdatedAffectationArticle = new AffectationArticle();
        partialUpdatedAffectationArticle.setId(affectationArticle.getId());

        partialUpdatedAffectationArticle.createdAt(UPDATED_CREATED_AT).updateAt(UPDATED_UPDATE_AT);

        restAffectationArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffectationArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffectationArticle))
            )
            .andExpect(status().isOk());

        // Validate the AffectationArticle in the database
        List<AffectationArticle> affectationArticleList = affectationArticleRepository.findAll();
        assertThat(affectationArticleList).hasSize(databaseSizeBeforeUpdate);
        AffectationArticle testAffectationArticle = affectationArticleList.get(affectationArticleList.size() - 1);
        assertThat(testAffectationArticle.getIsAffectation()).isEqualTo(DEFAULT_IS_AFFECTATION);
        assertThat(testAffectationArticle.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testAffectationArticle.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAffectationArticle.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void fullUpdateAffectationArticleWithPatch() throws Exception {
        // Initialize the database
        affectationArticleRepository.saveAndFlush(affectationArticle);

        int databaseSizeBeforeUpdate = affectationArticleRepository.findAll().size();

        // Update the affectationArticle using partial update
        AffectationArticle partialUpdatedAffectationArticle = new AffectationArticle();
        partialUpdatedAffectationArticle.setId(affectationArticle.getId());

        partialUpdatedAffectationArticle
            .isAffectation(UPDATED_IS_AFFECTATION)
            .quantite(UPDATED_QUANTITE)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        restAffectationArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffectationArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAffectationArticle))
            )
            .andExpect(status().isOk());

        // Validate the AffectationArticle in the database
        List<AffectationArticle> affectationArticleList = affectationArticleRepository.findAll();
        assertThat(affectationArticleList).hasSize(databaseSizeBeforeUpdate);
        AffectationArticle testAffectationArticle = affectationArticleList.get(affectationArticleList.size() - 1);
        assertThat(testAffectationArticle.getIsAffectation()).isEqualTo(UPDATED_IS_AFFECTATION);
        assertThat(testAffectationArticle.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testAffectationArticle.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAffectationArticle.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void patchNonExistingAffectationArticle() throws Exception {
        int databaseSizeBeforeUpdate = affectationArticleRepository.findAll().size();
        affectationArticle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, affectationArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affectationArticle))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationArticle in the database
        List<AffectationArticle> affectationArticleList = affectationArticleRepository.findAll();
        assertThat(affectationArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAffectationArticle() throws Exception {
        int databaseSizeBeforeUpdate = affectationArticleRepository.findAll().size();
        affectationArticle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affectationArticle))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationArticle in the database
        List<AffectationArticle> affectationArticleList = affectationArticleRepository.findAll();
        assertThat(affectationArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAffectationArticle() throws Exception {
        int databaseSizeBeforeUpdate = affectationArticleRepository.findAll().size();
        affectationArticle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationArticleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(affectationArticle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AffectationArticle in the database
        List<AffectationArticle> affectationArticleList = affectationArticleRepository.findAll();
        assertThat(affectationArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAffectationArticle() throws Exception {
        // Initialize the database
        affectationArticleRepository.saveAndFlush(affectationArticle);

        int databaseSizeBeforeDelete = affectationArticleRepository.findAll().size();

        // Delete the affectationArticle
        restAffectationArticleMockMvc
            .perform(delete(ENTITY_API_URL_ID, affectationArticle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AffectationArticle> affectationArticleList = affectationArticleRepository.findAll();
        assertThat(affectationArticleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
