import { Moment } from 'moment';
import { IQuestionType } from 'app/shared/model/question-type.model';
import { IResponseChoice } from 'app/shared/model/response-choice.model';
import { IVeeResponse } from 'app/shared/model/vee-response.model';

export interface IQuestion {
  id?: number;
  text?: string;
  updated?: Moment;
  questionTypes?: IQuestionType[];
  responseChoices?: IResponseChoice[];
  res?: IVeeResponse[];
}

export const defaultValue: Readonly<IQuestion> = {};
