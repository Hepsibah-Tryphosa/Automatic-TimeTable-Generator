package com.hepsibah.timetable.domain;

import java.util.ArrayList;
import java.util.List;

public class DayTimeTable {

    String weekName;
    int noOfClassesPerDay;
    List<Period> periods = new ArrayList<>();

    public DayTimeTable() {}

    public DayTimeTable(String weekName, int noOfClassesPerDay, List<Period> periods) {
        this.weekName = weekName;
        this.noOfClassesPerDay = noOfClassesPerDay;
        this.periods = periods;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public int getNoOfClassesPerDay() {
        return noOfClassesPerDay;
    }

    public void setNoOfClassesPerDay(int noOfClassesPerDay) {
        this.noOfClassesPerDay = noOfClassesPerDay;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }
}
