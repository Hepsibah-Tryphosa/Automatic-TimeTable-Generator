package com.hepsibah.timetable.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hepsibah.timetable.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SemisterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Semister.class);
        Semister semister1 = new Semister();
        semister1.setId(1L);
        Semister semister2 = new Semister();
        semister2.setId(semister1.getId());
        assertThat(semister1).isEqualTo(semister2);
        semister2.setId(2L);
        assertThat(semister1).isNotEqualTo(semister2);
        semister1.setId(null);
        assertThat(semister1).isNotEqualTo(semister2);
    }
}
