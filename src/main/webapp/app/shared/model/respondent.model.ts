import { Moment } from 'moment';
import { IVeeResponse } from 'app/shared/model/vee-response.model';
import { ISurveyResponse } from 'app/shared/model/survey-response.model';

export const enum Gender {
  Male = 'Male',
  Female = 'Female'
}

export interface IRespondent {
  id?: number;
  avatarUrl?: string;
  email?: string;
  birthDay?: Moment;
  gender?: Gender;
  userId?: number;
  res?: IVeeResponse[];
  surveyResponses?: ISurveyResponse[];
}

export const defaultValue: Readonly<IRespondent> = {};
