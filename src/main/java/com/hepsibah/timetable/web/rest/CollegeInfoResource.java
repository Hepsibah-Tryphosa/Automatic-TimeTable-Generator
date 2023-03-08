package com.hepsibah.timetable.web.rest;

import com.hepsibah.timetable.domain.CollegeInfo;
import com.hepsibah.timetable.repository.CollegeInfoRepository;
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
 * REST controller for managing {@link com.hepsibah.timetable.domain.CollegeInfo}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CollegeInfoResource {

    private final Logger log = LoggerFactory.getLogger(CollegeInfoResource.class);

    private static final String ENTITY_NAME = "collegeInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollegeInfoRepository collegeInfoRepository;

    public CollegeInfoResource(CollegeInfoRepository collegeInfoRepository) {
        this.collegeInfoRepository = collegeInfoRepository;
    }

    /**
     * {@code POST  /college-infos} : Create a new collegeInfo.
     *
     * @param collegeInfo the collegeInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collegeInfo, or with status {@code 400 (Bad Request)} if the collegeInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/college-infos")
    public ResponseEntity<CollegeInfo> createCollegeInfo(@Valid @RequestBody CollegeInfo collegeInfo) throws URISyntaxException {
        log.debug("REST request to save CollegeInfo : {}", collegeInfo);
        if (collegeInfo.getId() != null) {
            throw new BadRequestAlertException("A new collegeInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CollegeInfo result = collegeInfoRepository.save(collegeInfo);
        return ResponseEntity
            .created(new URI("/api/college-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /college-infos/:id} : Updates an existing collegeInfo.
     *
     * @param id the id of the collegeInfo to save.
     * @param collegeInfo the collegeInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collegeInfo,
     * or with status {@code 400 (Bad Request)} if the collegeInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collegeInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/college-infos/{id}")
    public ResponseEntity<CollegeInfo> updateCollegeInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CollegeInfo collegeInfo
    ) throws URISyntaxException {
        log.debug("REST request to update CollegeInfo : {}, {}", id, collegeInfo);
        if (collegeInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collegeInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collegeInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CollegeInfo result = collegeInfoRepository.save(collegeInfo);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collegeInfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /college-infos/:id} : Partial updates given fields of an existing collegeInfo, field will ignore if it is null
     *
     * @param id the id of the collegeInfo to save.
     * @param collegeInfo the collegeInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collegeInfo,
     * or with status {@code 400 (Bad Request)} if the collegeInfo is not valid,
     * or with status {@code 404 (Not Found)} if the collegeInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the collegeInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/college-infos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CollegeInfo> partialUpdateCollegeInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CollegeInfo collegeInfo
    ) throws URISyntaxException {
        log.debug("REST request to partial update CollegeInfo partially : {}, {}", id, collegeInfo);
        if (collegeInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collegeInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collegeInfoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CollegeInfo> result = collegeInfoRepository
            .findById(collegeInfo.getId())
            .map(existingCollegeInfo -> {
                if (collegeInfo.getName() != null) {
                    existingCollegeInfo.setName(collegeInfo.getName());
                }
                if (collegeInfo.getContactNo() != null) {
                    existingCollegeInfo.setContactNo(collegeInfo.getContactNo());
                }
                if (collegeInfo.getAddress() != null) {
                    existingCollegeInfo.setAddress(collegeInfo.getAddress());
                }
                if (collegeInfo.getCity() != null) {
                    existingCollegeInfo.setCity(collegeInfo.getCity());
                }

                return existingCollegeInfo;
            })
            .map(collegeInfoRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collegeInfo.getId().toString())
        );
    }

    /**
     * {@code GET  /college-infos} : get all the collegeInfos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collegeInfos in body.
     */
    @GetMapping("/college-infos")
    public List<CollegeInfo> getAllCollegeInfos() {
        log.debug("REST request to get all CollegeInfos");
        return collegeInfoRepository.findAll();
    }

    /**
     * {@code GET  /college-infos/:id} : get the "id" collegeInfo.
     *
     * @param id the id of the collegeInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collegeInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/college-infos/{id}")
    public ResponseEntity<CollegeInfo> getCollegeInfo(@PathVariable Long id) {
        log.debug("REST request to get CollegeInfo : {}", id);
        Optional<CollegeInfo> collegeInfo = collegeInfoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(collegeInfo);
    }

    /**
     * {@code DELETE  /college-infos/:id} : delete the "id" collegeInfo.
     *
     * @param id the id of the collegeInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/college-infos/{id}")
    public ResponseEntity<Void> deleteCollegeInfo(@PathVariable Long id) {
        log.debug("REST request to delete CollegeInfo : {}", id);
        collegeInfoRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
