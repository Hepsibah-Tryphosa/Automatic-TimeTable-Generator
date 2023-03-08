package com.hepsibah.timetable.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hepsibah.timetable.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LecturerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lecturer.class);
        Lecturer lecturer1 = new Lecturer();
        lecturer1.setId(1L);
        Lecturer lecturer2 = new Lecturer();
        lecturer2.setId(lecturer1.getId());
        assertThat(lecturer1).isEqualTo(lecturer2);
        lecturer2.setId(2L);
        assertThat(lecturer1).isNotEqualTo(lecturer2);
        lecturer1.setId(null);
        assertThat(lecturer1).isNotEqualTo(lecturer2);
    }
}
