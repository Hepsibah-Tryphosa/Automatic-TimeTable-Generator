package com.hepsibah.timetable.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hepsibah.timetable.IntegrationTest;
import com.hepsibah.timetable.domain.Lecturer;
import com.hepsibah.timetable.repository.LecturerRepository;
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
 * Integration tests for the {@link LecturerResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LecturerResourceIT {

    private static final String DEFAULT_EMAIL_ID = "36N@kbA.Awr";
    private static final String UPDATED_EMAIL_ID = "KGs@ddtN3.Qjv";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMP_ID = "AAAAAAAAAA";
    private static final String UPDATED_EMP_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_EXP_YEARS = 1;
    private static final Integer UPDATED_EXP_YEARS = 2;

    private static final String ENTITY_API_URL = "/api/lecturers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LecturerRepository lecturerRepository;

    @Mock
    private LecturerRepository lecturerRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLecturerMockMvc;

    private Lecturer lecturer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lecturer createEntity(EntityManager em) {
        Lecturer lecturer = new Lecturer().emailId(DEFAULT_EMAIL_ID).name(DEFAULT_NAME).empId(DEFAULT_EMP_ID).expYears(DEFAULT_EXP_YEARS);
        return lecturer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lecturer createUpdatedEntity(EntityManager em) {
        Lecturer lecturer = new Lecturer().emailId(UPDATED_EMAIL_ID).name(UPDATED_NAME).empId(UPDATED_EMP_ID).expYears(UPDATED_EXP_YEARS);
        return lecturer;
    }

    @BeforeEach
    public void initTest() {
        lecturer = createEntity(em);
    }

    @Test
    @Transactional
    void createLecturer() throws Exception {
        int databaseSizeBeforeCreate = lecturerRepository.findAll().size();
        // Create the Lecturer
        restLecturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isCreated());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeCreate + 1);
        Lecturer testLecturer = lecturerList.get(lecturerList.size() - 1);
        assertThat(testLecturer.getEmailId()).isEqualTo(DEFAULT_EMAIL_ID);
        assertThat(testLecturer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLecturer.getEmpId()).isEqualTo(DEFAULT_EMP_ID);
        assertThat(testLecturer.getExpYears()).isEqualTo(DEFAULT_EXP_YEARS);
    }

    @Test
    @Transactional
    void createLecturerWithExistingId() throws Exception {
        // Create the Lecturer with an existing ID
        lecturer.setId(1L);

        int databaseSizeBeforeCreate = lecturerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLecturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isBadRequest());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = lecturerRepository.findAll().size();
        // set the field null
        lecturer.setEmailId(null);

        // Create the Lecturer, which fails.

        restLecturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isBadRequest());

        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lecturerRepository.findAll().size();
        // set the field null
        lecturer.setName(null);

        // Create the Lecturer, which fails.

        restLecturerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isBadRequest());

        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLecturers() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        // Get all the lecturerList
        restLecturerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lecturer.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailId").value(hasItem(DEFAULT_EMAIL_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].empId").value(hasItem(DEFAULT_EMP_ID)))
            .andExpect(jsonPath("$.[*].expYears").value(hasItem(DEFAULT_EXP_YEARS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLecturersWithEagerRelationshipsIsEnabled() throws Exception {
        when(lecturerRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLecturerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lecturerRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLecturersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(lecturerRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLecturerMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lecturerRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLecturer() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        // Get the lecturer
        restLecturerMockMvc
            .perform(get(ENTITY_API_URL_ID, lecturer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lecturer.getId().intValue()))
            .andExpect(jsonPath("$.emailId").value(DEFAULT_EMAIL_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.empId").value(DEFAULT_EMP_ID))
            .andExpect(jsonPath("$.expYears").value(DEFAULT_EXP_YEARS));
    }

    @Test
    @Transactional
    void getNonExistingLecturer() throws Exception {
        // Get the lecturer
        restLecturerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLecturer() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();

        // Update the lecturer
        Lecturer updatedLecturer = lecturerRepository.findById(lecturer.getId()).get();
        // Disconnect from session so that the updates on updatedLecturer are not directly saved in db
        em.detach(updatedLecturer);
        updatedLecturer.emailId(UPDATED_EMAIL_ID).name(UPDATED_NAME).empId(UPDATED_EMP_ID).expYears(UPDATED_EXP_YEARS);

        restLecturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLecturer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLecturer))
            )
            .andExpect(status().isOk());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
        Lecturer testLecturer = lecturerList.get(lecturerList.size() - 1);
        assertThat(testLecturer.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testLecturer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLecturer.getEmpId()).isEqualTo(UPDATED_EMP_ID);
        assertThat(testLecturer.getExpYears()).isEqualTo(UPDATED_EXP_YEARS);
    }

    @Test
    @Transactional
    void putNonExistingLecturer() throws Exception {
        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();
        lecturer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLecturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lecturer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lecturer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLecturer() throws Exception {
        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();
        lecturer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLecturerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lecturer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLecturer() throws Exception {
        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();
        lecturer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLecturerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLecturerWithPatch() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();

        // Update the lecturer using partial update
        Lecturer partialUpdatedLecturer = new Lecturer();
        partialUpdatedLecturer.setId(lecturer.getId());

        partialUpdatedLecturer.emailId(UPDATED_EMAIL_ID).expYears(UPDATED_EXP_YEARS);

        restLecturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLecturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLecturer))
            )
            .andExpect(status().isOk());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
        Lecturer testLecturer = lecturerList.get(lecturerList.size() - 1);
        assertThat(testLecturer.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testLecturer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLecturer.getEmpId()).isEqualTo(DEFAULT_EMP_ID);
        assertThat(testLecturer.getExpYears()).isEqualTo(UPDATED_EXP_YEARS);
    }

    @Test
    @Transactional
    void fullUpdateLecturerWithPatch() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();

        // Update the lecturer using partial update
        Lecturer partialUpdatedLecturer = new Lecturer();
        partialUpdatedLecturer.setId(lecturer.getId());

        partialUpdatedLecturer.emailId(UPDATED_EMAIL_ID).name(UPDATED_NAME).empId(UPDATED_EMP_ID).expYears(UPDATED_EXP_YEARS);

        restLecturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLecturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLecturer))
            )
            .andExpect(status().isOk());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
        Lecturer testLecturer = lecturerList.get(lecturerList.size() - 1);
        assertThat(testLecturer.getEmailId()).isEqualTo(UPDATED_EMAIL_ID);
        assertThat(testLecturer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLecturer.getEmpId()).isEqualTo(UPDATED_EMP_ID);
        assertThat(testLecturer.getExpYears()).isEqualTo(UPDATED_EXP_YEARS);
    }

    @Test
    @Transactional
    void patchNonExistingLecturer() throws Exception {
        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();
        lecturer.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLecturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lecturer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lecturer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLecturer() throws Exception {
        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();
        lecturer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLecturerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lecturer))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLecturer() throws Exception {
        int databaseSizeBeforeUpdate = lecturerRepository.findAll().size();
        lecturer.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLecturerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lecturer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lecturer in the database
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLecturer() throws Exception {
        // Initialize the database
        lecturerRepository.saveAndFlush(lecturer);

        int databaseSizeBeforeDelete = lecturerRepository.findAll().size();

        // Delete the lecturer
        restLecturerMockMvc
            .perform(delete(ENTITY_API_URL_ID, lecturer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Lecturer> lecturerList = lecturerRepository.findAll();
        assertThat(lecturerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
