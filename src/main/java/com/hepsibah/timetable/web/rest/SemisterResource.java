package com.hepsibah.timetable.web.rest;

import com.hepsibah.timetable.domain.Semister;
import com.hepsibah.timetable.repository.SemisterRepository;
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
 * REST controller for managing {@link com.hepsibah.timetable.domain.Semister}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SemisterResource {

    private final Logger log = LoggerFactory.getLogger(SemisterResource.class);

    private static final String ENTITY_NAME = "semister";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SemisterRepository semisterRepository;

    public SemisterResource(SemisterRepository semisterRepository) {
        this.semisterRepository = semisterRepository;
    }

    /**
     * {@code POST  /semisters} : Create a new semister.
     *
     * @param semister the semister to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new semister, or with status {@code 400 (Bad Request)} if the semister has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/semisters")
    public ResponseEntity<Semister> createSemister(@Valid @RequestBody Semister semister) throws URISyntaxException {
        log.debug("REST request to save Semister : {}", semister);
        if (semister.getId() != null) {
            throw new BadRequestAlertException("A new semister cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Semister result = semisterRepository.save(semister);
        return ResponseEntity
            .created(new URI("/api/semisters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /semisters/:id} : Updates an existing semister.
     *
     * @param id the id of the semister to save.
     * @param semister the semister to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semister,
     * or with status {@code 400 (Bad Request)} if the semister is not valid,
     * or with status {@code 500 (Internal Server Error)} if the semister couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/semisters/{id}")
    public ResponseEntity<Semister> updateSemister(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Semister semister
    ) throws URISyntaxException {
        log.debug("REST request to update Semister : {}, {}", id, semister);
        if (semister.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semister.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!semisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Semister result = semisterRepository.save(semister);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, semister.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /semisters/:id} : Partial updates given fields of an existing semister, field will ignore if it is null
     *
     * @param id the id of the semister to save.
     * @param semister the semister to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semister,
     * or with status {@code 400 (Bad Request)} if the semister is not valid,
     * or with status {@code 404 (Not Found)} if the semister is not found,
     * or with status {@code 500 (Internal Server Error)} if the semister couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/semisters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Semister> partialUpdateSemister(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Semister semister
    ) throws URISyntaxException {
        log.debug("REST request to partial update Semister partially : {}, {}", id, semister);
        if (semister.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semister.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!semisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Semister> result = semisterRepository
            .findById(semister.getId())
            .map(existingSemister -> {
                if (semister.getName() != null) {
                    existingSemister.setName(semister.getName());
                }

                return existingSemister;
            })
            .map(semisterRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, semister.getId().toString())
        );
    }

    /**
     * {@code GET  /semisters} : get all the semisters.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of semisters in body.
     */
    @GetMapping("/semisters")
    public List<Semister> getAllSemisters(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Semisters");
        return semisterRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /semisters/:id} : get the "id" semister.
     *
     * @param id the id of the semister to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the semister, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/semisters/{id}")
    public ResponseEntity<Semister> getSemister(@PathVariable Long id) {
        log.debug("REST request to get Semister : {}", id);
        Optional<Semister> semister = semisterRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(semister);
    }

    /**
     * {@code DELETE  /semisters/:id} : delete the "id" semister.
     *
     * @param id the id of the semister to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/semisters/{id}")
    public ResponseEntity<Void> deleteSemister(@PathVariable Long id) {
        log.debug("REST request to delete Semister : {}", id);
        semisterRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
