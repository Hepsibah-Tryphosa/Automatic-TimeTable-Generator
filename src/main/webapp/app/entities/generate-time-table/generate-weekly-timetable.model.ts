export interface IWeeklyTimeTable {
  DailyTimeTable?: IDailyTimeTable[] | null;
}

export interface IDailyTimeTable {
  weekName?: string;
  noOfClassesPerDay?: number;
  periods?: IPeriod[] | null;
}

export interface IPeriod {
  subjectName?: string;
  periodType?: string;
  periodNo?: number;
  lecturerName?: string;
}

export class WeeklyTimeTable implements IWeeklyTimeTable {
  constructor(public DailyTimeTable?: IDailyTimeTable[] | null) {}
}

export class DailyTimeTable implements IDailyTimeTable {
  constructor(public weekName?: string, public noOfClassesPerDay?: number, public periods?: IPeriod[] | null) {}
}

export class Period implements IPeriod {
  constructor(public subjectName?: string, public periodType?: string, public periodNo?: number, public lecturerName?: string) {}
}
