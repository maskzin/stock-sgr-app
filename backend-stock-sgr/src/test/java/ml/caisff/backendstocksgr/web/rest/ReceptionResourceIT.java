package ml.caisff.backendstocksgr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ml.caisff.backendstocksgr.IntegrationTest;
import ml.caisff.backendstocksgr.domain.Reception;
import ml.caisff.backendstocksgr.repository.ReceptionRepository;
import ml.caisff.backendstocksgr.service.ReceptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ReceptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ReceptionResourceIT {

    private static final String DEFAULT_NUM_CONTRAT = "AAAAAAAAAA";
    private static final String UPDATED_NUM_CONTRAT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CONTRAT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CONTRAT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_RECEPTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_RECEPTION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATE_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATE_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/receptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReceptionRepository receptionRepository;

    @Mock
    private ReceptionRepository receptionRepositoryMock;

    @Mock
    private ReceptionService receptionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReceptionMockMvc;

    private Reception reception;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reception createEntity(EntityManager em) {
        Reception reception = new Reception()
            .numContrat(DEFAULT_NUM_CONTRAT)
            .dateContrat(DEFAULT_DATE_CONTRAT)
            .dateReception(DEFAULT_DATE_RECEPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .updateAt(DEFAULT_UPDATE_AT);
        return reception;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reception createUpdatedEntity(EntityManager em) {
        Reception reception = new Reception()
            .numContrat(UPDATED_NUM_CONTRAT)
            .dateContrat(UPDATED_DATE_CONTRAT)
            .dateReception(UPDATED_DATE_RECEPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);
        return reception;
    }

    @BeforeEach
    public void initTest() {
        reception = createEntity(em);
    }

    @Test
    @Transactional
    void createReception() throws Exception {
        int databaseSizeBeforeCreate = receptionRepository.findAll().size();
        // Create the Reception
        restReceptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reception)))
            .andExpect(status().isCreated());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeCreate + 1);
        Reception testReception = receptionList.get(receptionList.size() - 1);
        assertThat(testReception.getNumContrat()).isEqualTo(DEFAULT_NUM_CONTRAT);
        assertThat(testReception.getDateContrat()).isEqualTo(DEFAULT_DATE_CONTRAT);
        assertThat(testReception.getDateReception()).isEqualTo(DEFAULT_DATE_RECEPTION);
        assertThat(testReception.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReception.getUpdateAt()).isEqualTo(DEFAULT_UPDATE_AT);
    }

    @Test
    @Transactional
    void createReceptionWithExistingId() throws Exception {
        // Create the Reception with an existing ID
        reception.setId(1L);

        int databaseSizeBeforeCreate = receptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReceptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reception)))
            .andExpect(status().isBadRequest());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReceptions() throws Exception {
        // Initialize the database
        receptionRepository.saveAndFlush(reception);

        // Get all the receptionList
        restReceptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reception.getId().intValue())))
            .andExpect(jsonPath("$.[*].numContrat").value(hasItem(DEFAULT_NUM_CONTRAT)))
            .andExpect(jsonPath("$.[*].dateContrat").value(hasItem(DEFAULT_DATE_CONTRAT.toString())))
            .andExpect(jsonPath("$.[*].dateReception").value(hasItem(DEFAULT_DATE_RECEPTION.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updateAt").value(hasItem(DEFAULT_UPDATE_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReceptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(receptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReceptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(receptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReceptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(receptionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restReceptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(receptionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getReception() throws Exception {
        // Initialize the database
        receptionRepository.saveAndFlush(reception);

        // Get the reception
        restReceptionMockMvc
            .perform(get(ENTITY_API_URL_ID, reception.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reception.getId().intValue()))
            .andExpect(jsonPath("$.numContrat").value(DEFAULT_NUM_CONTRAT))
            .andExpect(jsonPath("$.dateContrat").value(DEFAULT_DATE_CONTRAT.toString()))
            .andExpect(jsonPath("$.dateReception").value(DEFAULT_DATE_RECEPTION.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updateAt").value(DEFAULT_UPDATE_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingReception() throws Exception {
        // Get the reception
        restReceptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReception() throws Exception {
        // Initialize the database
        receptionRepository.saveAndFlush(reception);

        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();

        // Update the reception
        Reception updatedReception = receptionRepository.findById(reception.getId()).get();
        // Disconnect from session so that the updates on updatedReception are not directly saved in db
        em.detach(updatedReception);
        updatedReception
            .numContrat(UPDATED_NUM_CONTRAT)
            .dateContrat(UPDATED_DATE_CONTRAT)
            .dateReception(UPDATED_DATE_RECEPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        restReceptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedReception.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedReception))
            )
            .andExpect(status().isOk());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
        Reception testReception = receptionList.get(receptionList.size() - 1);
        assertThat(testReception.getNumContrat()).isEqualTo(UPDATED_NUM_CONTRAT);
        assertThat(testReception.getDateContrat()).isEqualTo(UPDATED_DATE_CONTRAT);
        assertThat(testReception.getDateReception()).isEqualTo(UPDATED_DATE_RECEPTION);
        assertThat(testReception.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReception.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void putNonExistingReception() throws Exception {
        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();
        reception.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reception.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reception))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReception() throws Exception {
        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();
        reception.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reception))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReception() throws Exception {
        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();
        reception.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reception)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReceptionWithPatch() throws Exception {
        // Initialize the database
        receptionRepository.saveAndFlush(reception);

        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();

        // Update the reception using partial update
        Reception partialUpdatedReception = new Reception();
        partialUpdatedReception.setId(reception.getId());

        partialUpdatedReception
            .numContrat(UPDATED_NUM_CONTRAT)
            .dateContrat(UPDATED_DATE_CONTRAT)
            .dateReception(UPDATED_DATE_RECEPTION)
            .updateAt(UPDATED_UPDATE_AT);

        restReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReception.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReception))
            )
            .andExpect(status().isOk());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
        Reception testReception = receptionList.get(receptionList.size() - 1);
        assertThat(testReception.getNumContrat()).isEqualTo(UPDATED_NUM_CONTRAT);
        assertThat(testReception.getDateContrat()).isEqualTo(UPDATED_DATE_CONTRAT);
        assertThat(testReception.getDateReception()).isEqualTo(UPDATED_DATE_RECEPTION);
        assertThat(testReception.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testReception.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void fullUpdateReceptionWithPatch() throws Exception {
        // Initialize the database
        receptionRepository.saveAndFlush(reception);

        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();

        // Update the reception using partial update
        Reception partialUpdatedReception = new Reception();
        partialUpdatedReception.setId(reception.getId());

        partialUpdatedReception
            .numContrat(UPDATED_NUM_CONTRAT)
            .dateContrat(UPDATED_DATE_CONTRAT)
            .dateReception(UPDATED_DATE_RECEPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updateAt(UPDATED_UPDATE_AT);

        restReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReception.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReception))
            )
            .andExpect(status().isOk());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
        Reception testReception = receptionList.get(receptionList.size() - 1);
        assertThat(testReception.getNumContrat()).isEqualTo(UPDATED_NUM_CONTRAT);
        assertThat(testReception.getDateContrat()).isEqualTo(UPDATED_DATE_CONTRAT);
        assertThat(testReception.getDateReception()).isEqualTo(UPDATED_DATE_RECEPTION);
        assertThat(testReception.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testReception.getUpdateAt()).isEqualTo(UPDATED_UPDATE_AT);
    }

    @Test
    @Transactional
    void patchNonExistingReception() throws Exception {
        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();
        reception.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reception.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reception))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReception() throws Exception {
        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();
        reception.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reception))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReception() throws Exception {
        int databaseSizeBeforeUpdate = receptionRepository.findAll().size();
        reception.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReceptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reception))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reception in the database
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReception() throws Exception {
        // Initialize the database
        receptionRepository.saveAndFlush(reception);

        int databaseSizeBeforeDelete = receptionRepository.findAll().size();

        // Delete the reception
        restReceptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, reception.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reception> receptionList = receptionRepository.findAll();
        assertThat(receptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
