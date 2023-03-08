package com.hepsibah.timetable.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hepsibah.timetable.IntegrationTest;
import com.hepsibah.timetable.domain.CollegeInfo;
import com.hepsibah.timetable.repository.CollegeInfoRepository;
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
 * Integration tests for the {@link CollegeInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CollegeInfoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NO = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/college-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CollegeInfoRepository collegeInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCollegeInfoMockMvc;

    private CollegeInfo collegeInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollegeInfo createEntity(EntityManager em) {
        CollegeInfo collegeInfo = new CollegeInfo()
            .name(DEFAULT_NAME)
            .contactNo(DEFAULT_CONTACT_NO)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY);
        return collegeInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollegeInfo createUpdatedEntity(EntityManager em) {
        CollegeInfo collegeInfo = new CollegeInfo()
            .name(UPDATED_NAME)
            .contactNo(UPDATED_CONTACT_NO)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY);
        return collegeInfo;
    }

    @BeforeEach
    public void initTest() {
        collegeInfo = createEntity(em);
    }

    @Test
    @Transactional
    void createCollegeInfo() throws Exception {
        int databaseSizeBeforeCreate = collegeInfoRepository.findAll().size();
        // Create the CollegeInfo
        restCollegeInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collegeInfo)))
            .andExpect(status().isCreated());

        // Validate the CollegeInfo in the database
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeCreate + 1);
        CollegeInfo testCollegeInfo = collegeInfoList.get(collegeInfoList.size() - 1);
        assertThat(testCollegeInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCollegeInfo.getContactNo()).isEqualTo(DEFAULT_CONTACT_NO);
        assertThat(testCollegeInfo.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCollegeInfo.getCity()).isEqualTo(DEFAULT_CITY);
    }

    @Test
    @Transactional
    void createCollegeInfoWithExistingId() throws Exception {
        // Create the CollegeInfo with an existing ID
        collegeInfo.setId(1L);

        int databaseSizeBeforeCreate = collegeInfoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollegeInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collegeInfo)))
            .andExpect(status().isBadRequest());

        // Validate the CollegeInfo in the database
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = collegeInfoRepository.findAll().size();
        // set the field null
        collegeInfo.setName(null);

        // Create the CollegeInfo, which fails.

        restCollegeInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collegeInfo)))
            .andExpect(status().isBadRequest());

        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = collegeInfoRepository.findAll().size();
        // set the field null
        collegeInfo.setContactNo(null);

        // Create the CollegeInfo, which fails.

        restCollegeInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collegeInfo)))
            .andExpect(status().isBadRequest());

        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = collegeInfoRepository.findAll().size();
        // set the field null
        collegeInfo.setCity(null);

        // Create the CollegeInfo, which fails.

        restCollegeInfoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collegeInfo)))
            .andExpect(status().isBadRequest());

        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCollegeInfos() throws Exception {
        // Initialize the database
        collegeInfoRepository.saveAndFlush(collegeInfo);

        // Get all the collegeInfoList
        restCollegeInfoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collegeInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].contactNo").value(hasItem(DEFAULT_CONTACT_NO)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)));
    }

    @Test
    @Transactional
    void getCollegeInfo() throws Exception {
        // Initialize the database
        collegeInfoRepository.saveAndFlush(collegeInfo);

        // Get the collegeInfo
        restCollegeInfoMockMvc
            .perform(get(ENTITY_API_URL_ID, collegeInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(collegeInfo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.contactNo").value(DEFAULT_CONTACT_NO))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY));
    }

    @Test
    @Transactional
    void getNonExistingCollegeInfo() throws Exception {
        // Get the collegeInfo
        restCollegeInfoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCollegeInfo() throws Exception {
        // Initialize the database
        collegeInfoRepository.saveAndFlush(collegeInfo);

        int databaseSizeBeforeUpdate = collegeInfoRepository.findAll().size();

        // Update the collegeInfo
        CollegeInfo updatedCollegeInfo = collegeInfoRepository.findById(collegeInfo.getId()).get();
        // Disconnect from session so that the updates on updatedCollegeInfo are not directly saved in db
        em.detach(updatedCollegeInfo);
        updatedCollegeInfo.name(UPDATED_NAME).contactNo(UPDATED_CONTACT_NO).address(UPDATED_ADDRESS).city(UPDATED_CITY);

        restCollegeInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCollegeInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCollegeInfo))
            )
            .andExpect(status().isOk());

        // Validate the CollegeInfo in the database
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeUpdate);
        CollegeInfo testCollegeInfo = collegeInfoList.get(collegeInfoList.size() - 1);
        assertThat(testCollegeInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollegeInfo.getContactNo()).isEqualTo(UPDATED_CONTACT_NO);
        assertThat(testCollegeInfo.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCollegeInfo.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    void putNonExistingCollegeInfo() throws Exception {
        int databaseSizeBeforeUpdate = collegeInfoRepository.findAll().size();
        collegeInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollegeInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collegeInfo.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collegeInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollegeInfo in the database
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCollegeInfo() throws Exception {
        int databaseSizeBeforeUpdate = collegeInfoRepository.findAll().size();
        collegeInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollegeInfoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collegeInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollegeInfo in the database
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCollegeInfo() throws Exception {
        int databaseSizeBeforeUpdate = collegeInfoRepository.findAll().size();
        collegeInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollegeInfoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collegeInfo)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollegeInfo in the database
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCollegeInfoWithPatch() throws Exception {
        // Initialize the database
        collegeInfoRepository.saveAndFlush(collegeInfo);

        int databaseSizeBeforeUpdate = collegeInfoRepository.findAll().size();

        // Update the collegeInfo using partial update
        CollegeInfo partialUpdatedCollegeInfo = new CollegeInfo();
        partialUpdatedCollegeInfo.setId(collegeInfo.getId());

        partialUpdatedCollegeInfo.contactNo(UPDATED_CONTACT_NO).address(UPDATED_ADDRESS).city(UPDATED_CITY);

        restCollegeInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollegeInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollegeInfo))
            )
            .andExpect(status().isOk());

        // Validate the CollegeInfo in the database
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeUpdate);
        CollegeInfo testCollegeInfo = collegeInfoList.get(collegeInfoList.size() - 1);
        assertThat(testCollegeInfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCollegeInfo.getContactNo()).isEqualTo(UPDATED_CONTACT_NO);
        assertThat(testCollegeInfo.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCollegeInfo.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    void fullUpdateCollegeInfoWithPatch() throws Exception {
        // Initialize the database
        collegeInfoRepository.saveAndFlush(collegeInfo);

        int databaseSizeBeforeUpdate = collegeInfoRepository.findAll().size();

        // Update the collegeInfo using partial update
        CollegeInfo partialUpdatedCollegeInfo = new CollegeInfo();
        partialUpdatedCollegeInfo.setId(collegeInfo.getId());

        partialUpdatedCollegeInfo.name(UPDATED_NAME).contactNo(UPDATED_CONTACT_NO).address(UPDATED_ADDRESS).city(UPDATED_CITY);

        restCollegeInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollegeInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollegeInfo))
            )
            .andExpect(status().isOk());

        // Validate the CollegeInfo in the database
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeUpdate);
        CollegeInfo testCollegeInfo = collegeInfoList.get(collegeInfoList.size() - 1);
        assertThat(testCollegeInfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCollegeInfo.getContactNo()).isEqualTo(UPDATED_CONTACT_NO);
        assertThat(testCollegeInfo.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCollegeInfo.getCity()).isEqualTo(UPDATED_CITY);
    }

    @Test
    @Transactional
    void patchNonExistingCollegeInfo() throws Exception {
        int databaseSizeBeforeUpdate = collegeInfoRepository.findAll().size();
        collegeInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollegeInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, collegeInfo.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collegeInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollegeInfo in the database
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCollegeInfo() throws Exception {
        int databaseSizeBeforeUpdate = collegeInfoRepository.findAll().size();
        collegeInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollegeInfoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collegeInfo))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollegeInfo in the database
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCollegeInfo() throws Exception {
        int databaseSizeBeforeUpdate = collegeInfoRepository.findAll().size();
        collegeInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollegeInfoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(collegeInfo))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollegeInfo in the database
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCollegeInfo() throws Exception {
        // Initialize the database
        collegeInfoRepository.saveAndFlush(collegeInfo);

        int databaseSizeBeforeDelete = collegeInfoRepository.findAll().size();

        // Delete the collegeInfo
        restCollegeInfoMockMvc
            .perform(delete(ENTITY_API_URL_ID, collegeInfo.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CollegeInfo> collegeInfoList = collegeInfoRepository.findAll();
        assertThat(collegeInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
