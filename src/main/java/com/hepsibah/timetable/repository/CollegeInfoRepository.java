package com.hepsibah.timetable.repository;

import com.hepsibah.timetable.domain.CollegeInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CollegeInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollegeInfoRepository extends JpaRepository<CollegeInfo, Long> {}
