package com.hepsibah.timetable.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hepsibah.timetable.IntegrationTest;
import com.hepsibah.timetable.domain.GenerateTimeTable;
import com.hepsibah.timetable.repository.GenerateTimeTableRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GenerateTimeTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GenerateTimeTableResourceIT {

    private static final Integer DEFAULT_WOK_DAYS = 1;
    private static final Integer UPDATED_WOK_DAYS = 2;

    private static final String ENTITY_API_URL = "/api/generate-time-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GenerateTimeTableRepository generateTimeTableRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGenerateTimeTableMockMvc;

    private GenerateTimeTable generateTimeTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenerateTimeTable createEntity(EntityManager em) {
        GenerateTimeTable generateTimeTable = new GenerateTimeTable().wokDays(DEFAULT_WOK_DAYS);
        return generateTimeTable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GenerateTimeTable createUpdatedEntity(EntityManager em) {
        GenerateTimeTable generateTimeTable = new GenerateTimeTable().wokDays(UPDATED_WOK_DAYS);
        return generateTimeTable;
    }

    @BeforeEach
    public void initTest() {
        generateTimeTable = createEntity(em);
    }

    @Test
    @Transactional
    void createGenerateTimeTable() throws Exception {
        int databaseSizeBeforeCreate = generateTimeTableRepository.findAll().size();
        // Create the GenerateTimeTable
        restGenerateTimeTableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(generateTimeTable))
            )
            .andExpect(status().isCreated());

        // Validate the GenerateTimeTable in the database
        List<GenerateTimeTable> generateTimeTableList = generateTimeTableRepository.findAll();
        assertThat(generateTimeTableList).hasSize(databaseSizeBeforeCreate + 1);
        GenerateTimeTable testGenerateTimeTable = generateTimeTableList.get(generateTimeTableList.size() - 1);
        assertThat(testGenerateTimeTable.getWokDays()).isEqualTo(DEFAULT_WOK_DAYS);
    }

    @Test
    @Transactional
    void createGenerateTimeTableWithExistingId() throws Exception {
        // Create the GenerateTimeTable with an existing ID
        generateTimeTable.setId(1L);

        int databaseSizeBeforeCreate = generateTimeTableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenerateTimeTableMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(generateTimeTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenerateTimeTable in the database
        List<GenerateTimeTable> generateTimeTableList = generateTimeTableRepository.findAll();
        assertThat(generateTimeTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGenerateTimeTables() throws Exception {
        // Initialize the database
        generateTimeTableRepository.saveAndFlush(generateTimeTable);

        // Get all the generateTimeTableList
        restGenerateTimeTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(generateTimeTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].wokDays").value(hasItem(DEFAULT_WOK_DAYS)));
    }

    @Test
    @Transactional
    void getGenerateTimeTable() throws Exception {
        // Initialize the database
        generateTimeTableRepository.saveAndFlush(generateTimeTable);

        // Get the generateTimeTable
        restGenerateTimeTableMockMvc
            .perform(get(ENTITY_API_URL_ID, generateTimeTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(generateTimeTable.getId().intValue()))
            .andExpect(jsonPath("$.wokDays").value(DEFAULT_WOK_DAYS));
    }

    @Test
    @Transactional
    void getNonExistingGenerateTimeTable() throws Exception {
        // Get the generateTimeTable
        restGenerateTimeTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGenerateTimeTable() throws Exception {
        // Initialize the database
        generateTimeTableRepository.saveAndFlush(generateTimeTable);

        int databaseSizeBeforeUpdate = generateTimeTableRepository.findAll().size();

        // Update the generateTimeTable
        GenerateTimeTable updatedGenerateTimeTable = generateTimeTableRepository.findById(generateTimeTable.getId()).get();
        // Disconnect from session so that the updates on updatedGenerateTimeTable are not directly saved in db
        em.detach(updatedGenerateTimeTable);
        updatedGenerateTimeTable.wokDays(UPDATED_WOK_DAYS);

        restGenerateTimeTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGenerateTimeTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGenerateTimeTable))
            )
            .andExpect(status().isOk());

        // Validate the GenerateTimeTable in the database
        List<GenerateTimeTable> generateTimeTableList = generateTimeTableRepository.findAll();
        assertThat(generateTimeTableList).hasSize(databaseSizeBeforeUpdate);
        GenerateTimeTable testGenerateTimeTable = generateTimeTableList.get(generateTimeTableList.size() - 1);
        assertThat(testGenerateTimeTable.getWokDays()).isEqualTo(UPDATED_WOK_DAYS);
    }

    @Test
    @Transactional
    void putNonExistingGenerateTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = generateTimeTableRepository.findAll().size();
        generateTimeTable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenerateTimeTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, generateTimeTable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generateTimeTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenerateTimeTable in the database
        List<GenerateTimeTable> generateTimeTableList = generateTimeTableRepository.findAll();
        assertThat(generateTimeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenerateTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = generateTimeTableRepository.findAll().size();
        generateTimeTable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenerateTimeTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(generateTimeTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenerateTimeTable in the database
        List<GenerateTimeTable> generateTimeTableList = generateTimeTableRepository.findAll();
        assertThat(generateTimeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenerateTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = generateTimeTableRepository.findAll().size();
        generateTimeTable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenerateTimeTableMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(generateTimeTable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GenerateTimeTable in the database
        List<GenerateTimeTable> generateTimeTableList = generateTimeTableRepository.findAll();
        assertThat(generateTimeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGenerateTimeTableWithPatch() throws Exception {
        // Initialize the database
        generateTimeTableRepository.saveAndFlush(generateTimeTable);

        int databaseSizeBeforeUpdate = generateTimeTableRepository.findAll().size();

        // Update the generateTimeTable using partial update
        GenerateTimeTable partialUpdatedGenerateTimeTable = new GenerateTimeTable();
        partialUpdatedGenerateTimeTable.setId(generateTimeTable.getId());

        partialUpdatedGenerateTimeTable.wokDays(UPDATED_WOK_DAYS);

        restGenerateTimeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenerateTimeTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenerateTimeTable))
            )
            .andExpect(status().isOk());

        // Validate the GenerateTimeTable in the database
        List<GenerateTimeTable> generateTimeTableList = generateTimeTableRepository.findAll();
        assertThat(generateTimeTableList).hasSize(databaseSizeBeforeUpdate);
        GenerateTimeTable testGenerateTimeTable = generateTimeTableList.get(generateTimeTableList.size() - 1);
        assertThat(testGenerateTimeTable.getWokDays()).isEqualTo(UPDATED_WOK_DAYS);
    }

    @Test
    @Transactional
    void fullUpdateGenerateTimeTableWithPatch() throws Exception {
        // Initialize the database
        generateTimeTableRepository.saveAndFlush(generateTimeTable);

        int databaseSizeBeforeUpdate = generateTimeTableRepository.findAll().size();

        // Update the generateTimeTable using partial update
        GenerateTimeTable partialUpdatedGenerateTimeTable = new GenerateTimeTable();
        partialUpdatedGenerateTimeTable.setId(generateTimeTable.getId());

        partialUpdatedGenerateTimeTable.wokDays(UPDATED_WOK_DAYS);

        restGenerateTimeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenerateTimeTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenerateTimeTable))
            )
            .andExpect(status().isOk());

        // Validate the GenerateTimeTable in the database
        List<GenerateTimeTable> generateTimeTableList = generateTimeTableRepository.findAll();
        assertThat(generateTimeTableList).hasSize(databaseSizeBeforeUpdate);
        GenerateTimeTable testGenerateTimeTable = generateTimeTableList.get(generateTimeTableList.size() - 1);
        assertThat(testGenerateTimeTable.getWokDays()).isEqualTo(UPDATED_WOK_DAYS);
    }

    @Test
    @Transactional
    void patchNonExistingGenerateTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = generateTimeTableRepository.findAll().size();
        generateTimeTable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenerateTimeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, generateTimeTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generateTimeTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenerateTimeTable in the database
        List<GenerateTimeTable> generateTimeTableList = generateTimeTableRepository.findAll();
        assertThat(generateTimeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenerateTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = generateTimeTableRepository.findAll().size();
        generateTimeTable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenerateTimeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generateTimeTable))
            )
            .andExpect(status().isBadRequest());

        // Validate the GenerateTimeTable in the database
        List<GenerateTimeTable> generateTimeTableList = generateTimeTableRepository.findAll();
        assertThat(generateTimeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenerateTimeTable() throws Exception {
        int databaseSizeBeforeUpdate = generateTimeTableRepository.findAll().size();
        generateTimeTable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenerateTimeTableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(generateTimeTable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the GenerateTimeTable in the database
        List<GenerateTimeTable> generateTimeTableList = generateTimeTableRepository.findAll();
        assertThat(generateTimeTableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGenerateTimeTable() throws Exception {
        // Initialize the database
        generateTimeTableRepository.saveAndFlush(generateTimeTable);

        int databaseSizeBeforeDelete = generateTimeTableRepository.findAll().size();

        // Delete the generateTimeTable
        restGenerateTimeTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, generateTimeTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GenerateTimeTable> generateTimeTableList = generateTimeTableRepository.findAll();
        assertThat(generateTimeTableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
