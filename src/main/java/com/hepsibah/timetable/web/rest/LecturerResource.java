package com.hepsibah.timetable.web.rest;

import com.hepsibah.timetable.domain.Lecturer;
import com.hepsibah.timetable.repository.LecturerRepository;
import com.hepsibah.timetable.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hepsibah.timetable.domain.Lecturer}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LecturerResource {

    private final Logger log = LoggerFactory.getLogger(LecturerResource.class);

    private static final String ENTITY_NAME = "lecturer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LecturerRepository lecturerRepository;

    public LecturerResource(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    /**
     * {@code POST  /lecturers} : Create a new lecturer.
     *
     * @param lecturer the lecturer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lecturer, or with status {@code 400 (Bad Request)} if the lecturer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lecturers")
    public ResponseEntity<Lecturer> createLecturer(@Valid @RequestBody Lecturer lecturer) throws URISyntaxException {
        log.debug("REST request to save Lecturer : {}", lecturer);
        if (lecturer.getId() != null) {
            throw new BadRequestAlertException("A new lecturer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Lecturer result = lecturerRepository.save(lecturer);
        return ResponseEntity
            .created(new URI("/api/lecturers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lecturers/:id} : Updates an existing lecturer.
     *
     * @param id the id of the lecturer to save.
     * @param lecturer the lecturer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lecturer,
     * or with status {@code 400 (Bad Request)} if the lecturer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lecturer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lecturers/{id}")
    public ResponseEntity<Lecturer> updateLecturer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Lecturer lecturer
    ) throws URISyntaxException {
        log.debug("REST request to update Lecturer : {}, {}", id, lecturer);
        if (lecturer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lecturer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lecturerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Lecturer result = lecturerRepository.save(lecturer);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lecturer.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lecturers/:id} : Partial updates given fields of an existing lecturer, field will ignore if it is null
     *
     * @param id the id of the lecturer to save.
     * @param lecturer the lecturer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lecturer,
     * or with status {@code 400 (Bad Request)} if the lecturer is not valid,
     * or with status {@code 404 (Not Found)} if the lecturer is not found,
     * or with status {@code 500 (Internal Server Error)} if the lecturer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lecturers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Lecturer> partialUpdateLecturer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Lecturer lecturer
    ) throws URISyntaxException {
        log.debug("REST request to partial update Lecturer partially : {}, {}", id, lecturer);
        if (lecturer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lecturer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lecturerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Lecturer> result = lecturerRepository
            .findById(lecturer.getId())
            .map(existingLecturer -> {
                if (lecturer.getEmailId() != null) {
                    existingLecturer.setEmailId(lecturer.getEmailId());
                }
                if (lecturer.getName() != null) {
                    existingLecturer.setName(lecturer.getName());
                }
                if (lecturer.getEmpId() != null) {
                    existingLecturer.setEmpId(lecturer.getEmpId());
                }
                if (lecturer.getExpYears() != null) {
                    existingLecturer.setExpYears(lecturer.getExpYears());
                }

                return existingLecturer;
            })
            .map(lecturerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lecturer.getId().toString())
        );
    }

    /**
     * {@code GET  /lecturers} : get all the lecturers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lecturers in body.
     */
    @GetMapping("/lecturers")
    public List<Lecturer> getAllLecturers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Lecturers");
        return lecturerRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /lecturers/:id} : get the "id" lecturer.
     *
     * @param id the id of the lecturer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lecturer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lecturers/{id}")
    public ResponseEntity<Lecturer> getLecturer(@PathVariable Long id) {
        log.debug("REST request to get Lecturer : {}", id);
        Optional<Lecturer> lecturer = lecturerRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(lecturer);
    }

    /**
     * {@code DELETE  /lecturers/:id} : delete the "id" lecturer.
     *
     * @param id the id of the lecturer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lecturers/{id}")
    public ResponseEntity<Void> deleteLecturer(@PathVariable Long id) {
        log.debug("REST request to delete Lecturer : {}", id);
        lecturerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
