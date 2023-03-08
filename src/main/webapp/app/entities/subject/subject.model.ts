import { ILecturer } from 'app/entities/lecturer/lecturer.model';
import { ICourse } from 'app/entities/course/course.model';
import { ISemister } from 'app/entities/semister/semister.model';

export interface ISubject {
  id?: number;
  name?: string;
  reqHrs?: number | null;
  lecturers?: ILecturer[] | null;
  courses?: ICourse[] | null;
  semisters?: ISemister[] | null;
}

export class Subject implements ISubject {
  constructor(
    public id?: number,
    public name?: string,
    public reqHrs?: number | null,
    public lecturers?: ILecturer[] | null,
    public courses?: ICourse[] | null,
    public semisters?: ISemister[] | null
  ) {}
}

export function getSubjectIdentifier(subject: ISubject): number | undefined {
  return subject.id;
}
