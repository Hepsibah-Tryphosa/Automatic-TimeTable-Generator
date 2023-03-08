export interface IGenerateTimeTable {
  id?: number;
  wokDays?: number | null;
}

export class GenerateTimeTable implements IGenerateTimeTable {
  constructor(public id?: number, public wokDays?: number | null) {}
}

export function getGenerateTimeTableIdentifier(generateTimeTable: IGenerateTimeTable): number | undefined {
  return generateTimeTable.id;
}
