package com.hepsibah.timetable.repository;

import com.hepsibah.timetable.domain.GenerateTimeTable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the GenerateTimeTable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenerateTimeTableRepository extends JpaRepository<GenerateTimeTable, Long> {}
