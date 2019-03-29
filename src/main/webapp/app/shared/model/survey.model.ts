import { Moment } from 'moment';
import { IQuestionOrder } from 'app/shared/model/question-order.model';
import { ISurveyResponse } from 'app/shared/model/survey-response.model';

export interface ISurvey {
  id?: number;
  name?: string;
  description?: string;
  updated?: Moment;
  openingtime?: Moment;
  closingtime?: Moment;
  questionOrders?: IQuestionOrder[];
  surveyResponses?: ISurveyResponse[];
}

export const defaultValue: Readonly<ISurvey> = {};
