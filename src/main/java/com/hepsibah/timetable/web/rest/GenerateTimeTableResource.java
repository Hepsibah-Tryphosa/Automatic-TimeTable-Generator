package com.hepsibah.timetable.web.rest;

import com.hepsibah.timetable.domain.*;
import com.hepsibah.timetable.repository.*;
import com.hepsibah.timetable.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import liquibase.pro.packaged.l;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing
 * {@link com.hepsibah.timetable.domain.GenerateTimeTable}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GenerateTimeTableResource {

    private final Logger log = LoggerFactory.getLogger(GenerateTimeTableResource.class);

    private static final String ENTITY_NAME = "generateTimeTable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenerateTimeTableRepository generateTimeTableRepository;
    private final CourseRepository courseRepository;
    private final SemisterRepository semisterRepository;
    private final SubjectRepository subjectRepository;
    private final LecturerRepository lecturerRepository;

    // public GenerateTimeTableResource(GenerateTimeTableRepository
    // generateTimeTableRepository) {
    // this.generateTimeTableRepository = generateTimeTableRepository;
    // }

    public GenerateTimeTableResource(
        GenerateTimeTableRepository generateTimeTableRepository,
        CourseRepository courseRepository,
        SemisterRepository semisterRepository,
        SubjectRepository subjectRepository,
        LecturerRepository lecturerRepository
    ) {
        this.generateTimeTableRepository = generateTimeTableRepository;
        this.courseRepository = courseRepository;
        this.semisterRepository = semisterRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerRepository = lecturerRepository;
    }

    /**
     * {@code POST  /generate-time-tables} : Create a new generateTimeTable.
     *
     * @param generateTimeTable the generateTimeTable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new generateTimeTable, or with status
     *         {@code 400 (Bad Request)} if the generateTimeTable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/generate-time-tables")
    public ResponseEntity<GenerateTimeTable> createGenerateTimeTable(@RequestBody GenerateTimeTable generateTimeTable)
        throws URISyntaxException {
        log.debug("REST request to save GenerateTimeTable : {}", generateTimeTable);
        if (generateTimeTable.getId() != null) {
            throw new BadRequestAlertException("A new generateTimeTable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenerateTimeTable result = generateTimeTableRepository.save(generateTimeTable);
        return ResponseEntity
            .created(new URI("/api/generate-time-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /generate-time-tables/:id} : Updates an existing
     * generateTimeTable.
     *
     * @param id                the id of the generateTimeTable to save.
     * @param generateTimeTable the generateTimeTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated generateTimeTable,
     *         or with status {@code 400 (Bad Request)} if the generateTimeTable is
     *         not valid,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         generateTimeTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/generate-time-tables/{id}")
    public ResponseEntity<GenerateTimeTable> updateGenerateTimeTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GenerateTimeTable generateTimeTable
    ) throws URISyntaxException {
        log.debug("REST request to update GenerateTimeTable : {}, {}", id, generateTimeTable);
        if (generateTimeTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generateTimeTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generateTimeTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GenerateTimeTable result = generateTimeTableRepository.save(generateTimeTable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, generateTimeTable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /generate-time-tables/:id} : Partial updates given fields of an
     * existing generateTimeTable, field will ignore if it is null
     *
     * @param id                the id of the generateTimeTable to save.
     * @param generateTimeTable the generateTimeTable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated generateTimeTable,
     *         or with status {@code 400 (Bad Request)} if the generateTimeTable is
     *         not valid,
     *         or with status {@code 404 (Not Found)} if the generateTimeTable is
     *         not found,
     *         or with status {@code 500 (Internal Server Error)} if the
     *         generateTimeTable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/generate-time-tables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GenerateTimeTable> partialUpdateGenerateTimeTable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody GenerateTimeTable generateTimeTable
    ) throws URISyntaxException {
        log.debug("REST request to partial update GenerateTimeTable partially : {}, {}", id, generateTimeTable);
        if (generateTimeTable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, generateTimeTable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!generateTimeTableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GenerateTimeTable> result = generateTimeTableRepository
            .findById(generateTimeTable.getId())
            .map(existingGenerateTimeTable -> {
                if (generateTimeTable.getWokDays() != null) {
                    existingGenerateTimeTable.setWokDays(generateTimeTable.getWokDays());
                }

                return existingGenerateTimeTable;
            })
            .map(generateTimeTableRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, generateTimeTable.getId().toString())
        );
    }

    /**
     * {@code GET  /generate-time-tables} : get all the generateTimeTables.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of generateTimeTables in body.
     */
    @GetMapping("/generate-time-tables")
    public List<GenerateTimeTable> getAllGenerateTimeTables() {
        log.debug("REST request to get all GenerateTimeTables");
        return generateTimeTableRepository.findAll();
    }

    /**
     * {@code GET  /generate-time-tables/:id} : get the "id" generateTimeTable.
     *
     * @param id the id of the generateTimeTable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the generateTimeTable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/generate-time-tables/{id}")
    public ResponseEntity<GenerateTimeTable> getGenerateTimeTable(@PathVariable Long id) {
        log.debug("REST request to get GenerateTimeTable : {}", id);
        Optional<GenerateTimeTable> generateTimeTable = generateTimeTableRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(generateTimeTable);
    }

    @GetMapping("/generate-time-tables/{course}/{semester}/{periods}")
    public ResponseEntity<List<DayTimeTable>> getGenerateTimeTableBySem(
        @PathVariable String course,
        @PathVariable String semester,
        @PathVariable String periods
    ) {
        List<DayTimeTable> weeeklyTimeTable = generateWeeklyTimeTable(course, semester, Integer.parseInt(periods));
        log.debug("REST request to get GenerateTimeTable : {}", course);

        return ResponseUtil.wrapOrNotFound(Optional.of(weeeklyTimeTable));
    }

    List<DayTimeTable> generateWeeklyTimeTable(String course, String semester, int periods) {
        // private final CourseRepository courseRepository;
        // private final SemisterRepository semisterRepository;
        // private final SubjectRepository subjectRepository;
        // private final LecturerRepository lecturerRepository;

        // Find matched courses from semisterRepository
        List<Semister> semList = semisterRepository
            .findAllWithEagerRelationships()
            .parallelStream()
            .filter(sem -> sem.getName().equals(semester))
            .collect(Collectors.toList());
        System.out.println(semList.get(0).getName());

        Subject subject[] = semList.get(0).getSubjects().toArray(new Subject[] {});

        String weekDaysNames[] = new String[] { "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };

        List<DayTimeTable> weekTimeTable = new ArrayList();

        for (String weekName : weekDaysNames) {
            DayTimeTable dt = new DayTimeTable();
            for (int i = 0; i < periods && i < subject.length; i++) {
                Period p = new Period();
                p.setSubjectName(subject[i].getName());
                p.setPeriodNo(i + 1);
                p.setPeriodType("Class");
                Lecturer subLect[] = subject[i].getLecturers().toArray(new Lecturer[] {});
                if (subLect.length > 0) p.setLecturerName(subLect[0].getName());
                dt.getPeriods().add(p);
                dt.setWeekName(weekName);
                dt.setNoOfClassesPerDay(periods);
            }
            Collections.shuffle(dt.getPeriods());
            weekTimeTable.add(dt);
        }
        return weekTimeTable;
    }

    // WeeeklyTimeTable weeeklyTimeTable = new WeeeklyTimeTable();

    /**
     * {@code DELETE  /generate-time-tables/:id} : delete the "id"
     * generateTimeTable.
     *
     * @param id the id of the generateTimeTable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/generate-time-tables/{id}")
    public ResponseEntity<Void> deleteGenerateTimeTable(@PathVariable Long id) {
        log.debug("REST request to delete GenerateTimeTable : {}", id);
        generateTimeTableRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
