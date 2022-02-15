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
import ml.caisff.backendstocksgr.domain.ReceptionArticle;
import ml.caisff.backendstocksgr.repository.ReceptionArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReceptionArticleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReceptionArticleResourceIT {

    private static final Boolean DEFAULT_IS_RECEPTION = false;
    private static final Boolean UPDATED_IS_RECEPTION = true;

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/reception-articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReceptionArticleRepository receptionArticleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReceptionArticleMockMvc;

    private ReceptionArticle receptionArticle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReceptionArticle createEntity(EntityManager em) {
        ReceptionArticle receptionArticle = new ReceptionArticle()
            .isReception(DEFAULT_IS_RECEPTION)
            .quantite(DEFAULT_QUANTITE)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return receptionArticle;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReceptionArticle createUpdatedEntity(EntityManager em) {
        ReceptionArticle receptionArticle = new ReceptionArticle()
            .isReception(UPDATED_IS_RECEPTION)
            .quantite(UPDATED_QUANTITE)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);
        return receptionArticle;
    }

    @BeforeEach
    public void initTest() {
        receptionArticle = createEntity(em);
    }

    @Test
    @Transactional
    void createReceptionArticle() throws Exception {
        int databaseSizeBeforeCreate = receptionArticleRepository.findAll().size();
        // Create the ReceptionArticle
        restReceptionArticleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionArticle))
            )
            .andExpect(status().isCreated());

        // Validate the ReceptionArticle in the database
        List<ReceptionArticle> receptionArticleList = receptionArticleRepository.findAll();
        assertThat(receptionArticleList).hasSize(databaseSizeBeforeCreate + 1);
        ReceptionArticle testReceptionArticle = receptionArticleList.get(receptionArticleList.size() - 1);
        assertThat(testReceptionArticle.getIsReception()).isEqualTo(DEFAULT_IS_RECEPTION);
        assertThat(testReceptionArticle.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testReceptionArticle.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReceptionArticle.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    void createReceptionArticleWithExistingId() throws Exception {
        // Create the ReceptionArticle with an existing ID
        receptionArticle.setId(1L);

        int databaseSizeBeforeCreate = receptionArticleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReceptionArticleMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionArticle))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceptionArticle in the database
        List<ReceptionArticle> receptionArticleList = receptionArticleRepository.findAll();
        assertThat(receptionArticleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReceptionArticles() throws Exception {
        // Initialize the database
        receptionArticleRepository.saveAndFlush(receptionArticle);

        // Get all the receptionArticleList
        restReceptionArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(receptionArticle.getId().intValue())))
            .andExpect(jsonPath("$.[*].isReception").value(hasItem(DEFAULT_IS_RECEPTION.booleanValue())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @Test
    @Transactional
    void getReceptionArticle() throws Exception {
        // Initialize the database
        receptionArticleRepository.saveAndFlush(receptionArticle);

        // Get the receptionArticle
        restReceptionArticleMockMvc
            .perform(get(ENTITY_API_URL_ID, receptionArticle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(receptionArticle.getId().intValue()))
            .andExpect(jsonPath("$.isReception").value(DEFAULT_IS_RECEPTION.booleanValue()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReceptionArticle() throws Exception {
        // Get the receptionArticle
        restReceptionArticleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReceptionArticle() throws Exception {
        // Initialize the database
        receptionArticleRepository.saveAndFlush(receptionArticle);

        int databaseSizeBeforeUpdate = receptionArticleRepository.findAll().size();

        // Update the receptionArticle
        ReceptionArticle updatedReceptionArticle = receptionArticleRepository.findById(receptionArticle.getId()).get();
        // Disconnect from session so that the updates on updatedReceptionArticle are not directly saved in db
        em.detach(updatedReceptionArticle);
        updatedReceptionArticle
            .isReception(UPDATED_IS_RECEPTION)
            .quantite(UPDATED_QUANTITE)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        restReceptionArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReceptionArticle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReceptionArticle))
            )
            .andExpect(status().isOk());

        // Validate the ReceptionArticle in the database
        List<ReceptionArticle> receptionArticleList = receptionArticleRepository.findAll();
        assertThat(receptionArticleList).hasSize(databaseSizeBeforeUpdate);
        ReceptionArticle testReceptionArticle = receptionArticleList.get(receptionArticleList.size() - 1);
        assertThat(testReceptionArticle.getIsReception()).isEqualTo(UPDATED_IS_RECEPTION);
        assertThat(testReceptionArticle.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testReceptionArticle.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReceptionArticle.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void putNonExistingReceptionArticle() throws Exception {
        int databaseSizeBeforeUpdate = receptionArticleRepository.findAll().size();
        receptionArticle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceptionArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, receptionArticle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receptionArticle))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceptionArticle in the database
        List<ReceptionArticle> receptionArticleList = receptionArticleRepository.findAll();
        assertThat(receptionArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReceptionArticle() throws Exception {
        int databaseSizeBeforeUpdate = receptionArticleRepository.findAll().size();
        receptionArticle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(receptionArticle))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceptionArticle in the database
        List<ReceptionArticle> receptionArticleList = receptionArticleRepository.findAll();
        assertThat(receptionArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReceptionArticle() throws Exception {
        int databaseSizeBeforeUpdate = receptionArticleRepository.findAll().size();
        receptionArticle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionArticleMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(receptionArticle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReceptionArticle in the database
        List<ReceptionArticle> receptionArticleList = receptionArticleRepository.findAll();
        assertThat(receptionArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReceptionArticleWithPatch() throws Exception {
        // Initialize the database
        receptionArticleRepository.saveAndFlush(receptionArticle);

        int databaseSizeBeforeUpdate = receptionArticleRepository.findAll().size();

        // Update the receptionArticle using partial update
        ReceptionArticle partialUpdatedReceptionArticle = new ReceptionArticle();
        partialUpdatedReceptionArticle.setId(receptionArticle.getId());

        partialUpdatedReceptionArticle.isReception(UPDATED_IS_RECEPTION).createdAt(UPDATED_CREATED_AT);

        restReceptionArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceptionArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceptionArticle))
            )
            .andExpect(status().isOk());

        // Validate the ReceptionArticle in the database
        List<ReceptionArticle> receptionArticleList = receptionArticleRepository.findAll();
        assertThat(receptionArticleList).hasSize(databaseSizeBeforeUpdate);
        ReceptionArticle testReceptionArticle = receptionArticleList.get(receptionArticleList.size() - 1);
        assertThat(testReceptionArticle.getIsReception()).isEqualTo(UPDATED_IS_RECEPTION);
        assertThat(testReceptionArticle.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testReceptionArticle.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReceptionArticle.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    void fullUpdateReceptionArticleWithPatch() throws Exception {
        // Initialize the database
        receptionArticleRepository.saveAndFlush(receptionArticle);

        int databaseSizeBeforeUpdate = receptionArticleRepository.findAll().size();

        // Update the receptionArticle using partial update
        ReceptionArticle partialUpdatedReceptionArticle = new ReceptionArticle();
        partialUpdatedReceptionArticle.setId(receptionArticle.getId());

        partialUpdatedReceptionArticle
            .isReception(UPDATED_IS_RECEPTION)
            .quantite(UPDATED_QUANTITE)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        restReceptionArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReceptionArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReceptionArticle))
            )
            .andExpect(status().isOk());

        // Validate the ReceptionArticle in the database
        List<ReceptionArticle> receptionArticleList = receptionArticleRepository.findAll();
        assertThat(receptionArticleList).hasSize(databaseSizeBeforeUpdate);
        ReceptionArticle testReceptionArticle = receptionArticleList.get(receptionArticleList.size() - 1);
        assertThat(testReceptionArticle.getIsReception()).isEqualTo(UPDATED_IS_RECEPTION);
        assertThat(testReceptionArticle.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testReceptionArticle.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReceptionArticle.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void patchNonExistingReceptionArticle() throws Exception {
        int databaseSizeBeforeUpdate = receptionArticleRepository.findAll().size();
        receptionArticle.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceptionArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, receptionArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receptionArticle))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceptionArticle in the database
        List<ReceptionArticle> receptionArticleList = receptionArticleRepository.findAll();
        assertThat(receptionArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReceptionArticle() throws Exception {
        int databaseSizeBeforeUpdate = receptionArticleRepository.findAll().size();
        receptionArticle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receptionArticle))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReceptionArticle in the database
        List<ReceptionArticle> receptionArticleList = receptionArticleRepository.findAll();
        assertThat(receptionArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReceptionArticle() throws Exception {
        int databaseSizeBeforeUpdate = receptionArticleRepository.findAll().size();
        receptionArticle.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionArticleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(receptionArticle))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReceptionArticle in the database
        List<ReceptionArticle> receptionArticleList = receptionArticleRepository.findAll();
        assertThat(receptionArticleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReceptionArticle() throws Exception {
        // Initialize the database
        receptionArticleRepository.saveAndFlush(receptionArticle);

        int databaseSizeBeforeDelete = receptionArticleRepository.findAll().size();

        // Delete the receptionArticle
        restReceptionArticleMockMvc
            .perform(delete(ENTITY_API_URL_ID, receptionArticle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReceptionArticle> receptionArticleList = receptionArticleRepository.findAll();
        assertThat(receptionArticleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
