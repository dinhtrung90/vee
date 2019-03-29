export interface IVeeResponse {
  id?: number;
  answer?: string;
  questionId?: number;
  respondentId?: number;
}

export const defaultValue: Readonly<IVeeResponse> = {};
