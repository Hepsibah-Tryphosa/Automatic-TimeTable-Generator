package com.hepsibah.timetable.repository;

import com.hepsibah.timetable.domain.Lecturer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface LecturerRepositoryWithBagRelationships {
    Optional<Lecturer> fetchBagRelationships(Optional<Lecturer> lecturer);

    List<Lecturer> fetchBagRelationships(List<Lecturer> lecturers);

    Page<Lecturer> fetchBagRelationships(Page<Lecturer> lecturers);
}
