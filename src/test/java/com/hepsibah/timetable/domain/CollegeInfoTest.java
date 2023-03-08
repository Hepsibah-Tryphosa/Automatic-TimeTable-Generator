package com.hepsibah.timetable.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hepsibah.timetable.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CollegeInfoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollegeInfo.class);
        CollegeInfo collegeInfo1 = new CollegeInfo();
        collegeInfo1.setId(1L);
        CollegeInfo collegeInfo2 = new CollegeInfo();
        collegeInfo2.setId(collegeInfo1.getId());
        assertThat(collegeInfo1).isEqualTo(collegeInfo2);
        collegeInfo2.setId(2L);
        assertThat(collegeInfo1).isNotEqualTo(collegeInfo2);
        collegeInfo1.setId(null);
        assertThat(collegeInfo1).isNotEqualTo(collegeInfo2);
    }
}
