package com.hepsibah.timetable.repository;

import com.hepsibah.timetable.domain.Lecturer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Lecturer entity.
 */
@Repository
public interface LecturerRepository extends LecturerRepositoryWithBagRelationships, JpaRepository<Lecturer, Long> {
    default Optional<Lecturer> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Lecturer> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Lecturer> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
