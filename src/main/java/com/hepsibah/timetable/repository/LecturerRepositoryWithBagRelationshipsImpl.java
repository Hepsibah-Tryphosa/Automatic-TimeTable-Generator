package com.hepsibah.timetable.repository;

import com.hepsibah.timetable.domain.Lecturer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class LecturerRepositoryWithBagRelationshipsImpl implements LecturerRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Lecturer> fetchBagRelationships(Optional<Lecturer> lecturer) {
        return lecturer.map(this::fetchSubjects);
    }

    @Override
    public Page<Lecturer> fetchBagRelationships(Page<Lecturer> lecturers) {
        return new PageImpl<>(fetchBagRelationships(lecturers.getContent()), lecturers.getPageable(), lecturers.getTotalElements());
    }

    @Override
    public List<Lecturer> fetchBagRelationships(List<Lecturer> lecturers) {
        return Optional.of(lecturers).map(this::fetchSubjects).orElse(Collections.emptyList());
    }

    Lecturer fetchSubjects(Lecturer result) {
        return entityManager
            .createQuery(
                "select lecturer from Lecturer lecturer left join fetch lecturer.subjects where lecturer is :lecturer",
                Lecturer.class
            )
            .setParameter("lecturer", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Lecturer> fetchSubjects(List<Lecturer> lecturers) {
        return entityManager
            .createQuery(
                "select distinct lecturer from Lecturer lecturer left join fetch lecturer.subjects where lecturer in :lecturers",
                Lecturer.class
            )
            .setParameter("lecturers", lecturers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
