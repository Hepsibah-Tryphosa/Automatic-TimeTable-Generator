import { ISubject } from 'app/entities/subject/subject.model';
import { ISemister } from 'app/entities/semister/semister.model';

export interface ICourse {
  id?: number;
  name?: string;
  subjects?: ISubject[] | null;
  semisters?: ISemister[] | null;
}

export class Course implements ICourse {
  constructor(public id?: number, public name?: string, public subjects?: ISubject[] | null, public semisters?: ISemister[] | null) {}
}

export function getCourseIdentifier(course: ICourse): number | undefined {
  return course.id;
}
