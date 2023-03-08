import { ISubject } from 'app/entities/subject/subject.model';

export interface ILecturer {
  id?: number;
  emailId?: string;
  name?: string;
  empId?: string | null;
  expYears?: number | null;
  subjects?: ISubject[] | null;
}

export class Lecturer implements ILecturer {
  constructor(
    public id?: number,
    public emailId?: string,
    public name?: string,
    public empId?: string | null,
    public expYears?: number | null,
    public subjects?: ISubject[] | null
  ) {}
}

export function getLecturerIdentifier(lecturer: ILecturer): number | undefined {
  return lecturer.id;
}
