import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import survey, {
  SurveyState
} from 'app/entities/survey/survey.reducer';
// prettier-ignore
import questionOrder, {
  QuestionOrderState
} from 'app/entities/question-order/question-order.reducer';
// prettier-ignore
import question, {
  QuestionState
} from 'app/entities/question/question.reducer';
// prettier-ignore
import questionType, {
  QuestionTypeState
} from 'app/entities/question-type/question-type.reducer';
// prettier-ignore
import responseChoice, {
  ResponseChoiceState
} from 'app/entities/response-choice/response-choice.reducer';
// prettier-ignore
import veeResponse, {
  VeeResponseState
} from 'app/entities/vee-response/vee-response.reducer';
// prettier-ignore
import surveyResponse, {
  SurveyResponseState
} from 'app/entities/survey-response/survey-response.reducer';
// prettier-ignore
import respondent, {
  RespondentState
} from 'app/entities/respondent/respondent.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly survey: SurveyState;
  readonly questionOrder: QuestionOrderState;
  readonly question: QuestionState;
  readonly questionType: QuestionTypeState;
  readonly responseChoice: ResponseChoiceState;
  readonly veeResponse: VeeResponseState;
  readonly surveyResponse: SurveyResponseState;
  readonly respondent: RespondentState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  survey,
  questionOrder,
  question,
  questionType,
  responseChoice,
  veeResponse,
  surveyResponse,
  respondent,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
