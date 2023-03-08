package com.hepsibah.timetable.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hepsibah.timetable.IntegrationTest;
import com.hepsibah.timetable.domain.Semister;
import com.hepsibah.timetable.repository.SemisterRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
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
 * Integration tests for the {@link SemisterResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SemisterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/semisters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SemisterRepository semisterRepository;

    @Mock
    private SemisterRepository semisterRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSemisterMockMvc;

    private Semister semister;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semister createEntity(EntityManager em) {
        Semister semister = new Semister().name(DEFAULT_NAME);
        return semister;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semister createUpdatedEntity(EntityManager em) {
        Semister semister = new Semister().name(UPDATED_NAME);
        return semister;
    }

    @BeforeEach
    public void initTest() {
        semister = createEntity(em);
    }

    @Test
    @Transactional
    void createSemister() throws Exception {
        int databaseSizeBeforeCreate = semisterRepository.findAll().size();
        // Create the Semister
        restSemisterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semister)))
            .andExpect(status().isCreated());

        // Validate the Semister in the database
        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeCreate + 1);
        Semister testSemister = semisterList.get(semisterList.size() - 1);
        assertThat(testSemister.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createSemisterWithExistingId() throws Exception {
        // Create the Semister with an existing ID
        semister.setId(1L);

        int databaseSizeBeforeCreate = semisterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSemisterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semister)))
            .andExpect(status().isBadRequest());

        // Validate the Semister in the database
        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = semisterRepository.findAll().size();
        // set the field null
        semister.setName(null);

        // Create the Semister, which fails.

        restSemisterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semister)))
            .andExpect(status().isBadRequest());

        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSemisters() throws Exception {
        // Initialize the database
        semisterRepository.saveAndFlush(semister);

        // Get all the semisterList
        restSemisterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semister.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSemistersWithEagerRelationshipsIsEnabled() throws Exception {
        when(semisterRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSemisterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(semisterRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSemistersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(semisterRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSemisterMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(semisterRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSemister() throws Exception {
        // Initialize the database
        semisterRepository.saveAndFlush(semister);

        // Get the semister
        restSemisterMockMvc
            .perform(get(ENTITY_API_URL_ID, semister.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(semister.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingSemister() throws Exception {
        // Get the semister
        restSemisterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSemister() throws Exception {
        // Initialize the database
        semisterRepository.saveAndFlush(semister);

        int databaseSizeBeforeUpdate = semisterRepository.findAll().size();

        // Update the semister
        Semister updatedSemister = semisterRepository.findById(semister.getId()).get();
        // Disconnect from session so that the updates on updatedSemister are not directly saved in db
        em.detach(updatedSemister);
        updatedSemister.name(UPDATED_NAME);

        restSemisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSemister.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSemister))
            )
            .andExpect(status().isOk());

        // Validate the Semister in the database
        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeUpdate);
        Semister testSemister = semisterList.get(semisterList.size() - 1);
        assertThat(testSemister.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingSemister() throws Exception {
        int databaseSizeBeforeUpdate = semisterRepository.findAll().size();
        semister.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, semister.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(semister))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semister in the database
        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSemister() throws Exception {
        int databaseSizeBeforeUpdate = semisterRepository.findAll().size();
        semister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemisterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(semister))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semister in the database
        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSemister() throws Exception {
        int databaseSizeBeforeUpdate = semisterRepository.findAll().size();
        semister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemisterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semister)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Semister in the database
        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSemisterWithPatch() throws Exception {
        // Initialize the database
        semisterRepository.saveAndFlush(semister);

        int databaseSizeBeforeUpdate = semisterRepository.findAll().size();

        // Update the semister using partial update
        Semister partialUpdatedSemister = new Semister();
        partialUpdatedSemister.setId(semister.getId());

        partialUpdatedSemister.name(UPDATED_NAME);

        restSemisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSemister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSemister))
            )
            .andExpect(status().isOk());

        // Validate the Semister in the database
        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeUpdate);
        Semister testSemister = semisterList.get(semisterList.size() - 1);
        assertThat(testSemister.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateSemisterWithPatch() throws Exception {
        // Initialize the database
        semisterRepository.saveAndFlush(semister);

        int databaseSizeBeforeUpdate = semisterRepository.findAll().size();

        // Update the semister using partial update
        Semister partialUpdatedSemister = new Semister();
        partialUpdatedSemister.setId(semister.getId());

        partialUpdatedSemister.name(UPDATED_NAME);

        restSemisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSemister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSemister))
            )
            .andExpect(status().isOk());

        // Validate the Semister in the database
        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeUpdate);
        Semister testSemister = semisterList.get(semisterList.size() - 1);
        assertThat(testSemister.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingSemister() throws Exception {
        int databaseSizeBeforeUpdate = semisterRepository.findAll().size();
        semister.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, semister.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(semister))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semister in the database
        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSemister() throws Exception {
        int databaseSizeBeforeUpdate = semisterRepository.findAll().size();
        semister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemisterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(semister))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semister in the database
        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSemister() throws Exception {
        int databaseSizeBeforeUpdate = semisterRepository.findAll().size();
        semister.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemisterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(semister)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Semister in the database
        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSemister() throws Exception {
        // Initialize the database
        semisterRepository.saveAndFlush(semister);

        int databaseSizeBeforeDelete = semisterRepository.findAll().size();

        // Delete the semister
        restSemisterMockMvc
            .perform(delete(ENTITY_API_URL_ID, semister.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Semister> semisterList = semisterRepository.findAll();
        assertThat(semisterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
