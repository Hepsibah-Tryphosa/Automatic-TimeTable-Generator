package com.hepsibah.timetable.repository;

import com.hepsibah.timetable.domain.Semister;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SemisterRepositoryWithBagRelationships {
    Optional<Semister> fetchBagRelationships(Optional<Semister> semister);

    List<Semister> fetchBagRelationships(List<Semister> semisters);

    Page<Semister> fetchBagRelationships(Page<Semister> semisters);
}
