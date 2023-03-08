package com.hepsibah.timetable.repository;

import com.hepsibah.timetable.domain.Semister;
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
public class SemisterRepositoryWithBagRelationshipsImpl implements SemisterRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Semister> fetchBagRelationships(Optional<Semister> semister) {
        return semister.map(this::fetchCourses).map(this::fetchSubjects);
    }

    @Override
    public Page<Semister> fetchBagRelationships(Page<Semister> semisters) {
        return new PageImpl<>(fetchBagRelationships(semisters.getContent()), semisters.getPageable(), semisters.getTotalElements());
    }

    @Override
    public List<Semister> fetchBagRelationships(List<Semister> semisters) {
        return Optional.of(semisters).map(this::fetchCourses).map(this::fetchSubjects).orElse(Collections.emptyList());
    }

    Semister fetchCourses(Semister result) {
        return entityManager
            .createQuery(
                "select semister from Semister semister left join fetch semister.courses where semister is :semister",
                Semister.class
            )
            .setParameter("semister", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Semister> fetchCourses(List<Semister> semisters) {
        return entityManager
            .createQuery(
                "select distinct semister from Semister semister left join fetch semister.courses where semister in :semisters",
                Semister.class
            )
            .setParameter("semisters", semisters)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Semister fetchSubjects(Semister result) {
        return entityManager
            .createQuery(
                "select semister from Semister semister left join fetch semister.subjects where semister is :semister",
                Semister.class
            )
            .setParameter("semister", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Semister> fetchSubjects(List<Semister> semisters) {
        return entityManager
            .createQuery(
                "select distinct semister from Semister semister left join fetch semister.subjects where semister in :semisters",
                Semister.class
            )
            .setParameter("semisters", semisters)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
