export interface ICollegeInfo {
  id?: number;
  name?: string;
  contactNo?: string;
  address?: string | null;
  city?: string;
}

export class CollegeInfo implements ICollegeInfo {
  constructor(public id?: number, public name?: string, public contactNo?: string, public address?: string | null, public city?: string) {}
}

export function getCollegeInfoIdentifier(collegeInfo: ICollegeInfo): number | undefined {
  return collegeInfo.id;
}
