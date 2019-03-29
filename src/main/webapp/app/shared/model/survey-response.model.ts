import { Moment } from 'moment';

export interface ISurveyResponse {
  id?: number;
  updated?: Moment;
  startedat?: Moment;
  completedat?: Moment;
  surveyId?: number;
  respondentId?: number;
}

export const defaultValue: Readonly<ISurveyResponse> = {};
