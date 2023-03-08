import { ICourse } from 'app/entities/course/course.model';
import { ISubject } from 'app/entities/subject/subject.model';

export interface ISemister {
  id?: number;
  name?: string;
  courses?: ICourse[] | null;
  subjects?: ISubject[] | null;
}

export class Semister implements ISemister {
  constructor(public id?: number, public name?: string, public courses?: ICourse[] | null, public subjects?: ISubject[] | null) {}
}

export function getSemisterIdentifier(semister: ISemister): number | undefined {
  return semister.id;
}
