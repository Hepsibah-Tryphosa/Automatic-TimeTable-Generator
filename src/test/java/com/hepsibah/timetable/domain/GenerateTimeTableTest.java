package com.hepsibah.timetable.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hepsibah.timetable.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenerateTimeTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenerateTimeTable.class);
        GenerateTimeTable generateTimeTable1 = new GenerateTimeTable();
        generateTimeTable1.setId(1L);
        GenerateTimeTable generateTimeTable2 = new GenerateTimeTable();
        generateTimeTable2.setId(generateTimeTable1.getId());
        assertThat(generateTimeTable1).isEqualTo(generateTimeTable2);
        generateTimeTable2.setId(2L);
        assertThat(generateTimeTable1).isNotEqualTo(generateTimeTable2);
        generateTimeTable1.setId(null);
        assertThat(generateTimeTable1).isNotEqualTo(generateTimeTable2);
    }
}
