package com.hepsibah.timetable.domain;

public class Period {

    String subjectName;
    String periodType;
    Integer periodNo;
    String lecturerName;

    public Period() {}

    public Period(String subjectName, String periodType, Integer periodNo, String lecturerName) {
        this.subjectName = subjectName;
        this.periodType = periodType;
        this.periodNo = periodNo;
        this.lecturerName = lecturerName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public Integer getPeriodNo() {
        return periodNo;
    }

    public void setPeriodNo(Integer periodNo) {
        this.periodNo = periodNo;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    @Override
    public String toString() {
        return (
            "Period [subjectName=" +
            subjectName +
            ", periodType=" +
            periodType +
            ", periodNo=" +
            periodNo +
            ", lecturerName=" +
            lecturerName +
            "]"
        );
    }
}
