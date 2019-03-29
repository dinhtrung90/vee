import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Survey from './survey';
import QuestionOrder from './question-order';
import Question from './question';
import QuestionType from './question-type';
import ResponseChoice from './response-choice';
import VeeResponse from './vee-response';
import SurveyResponse from './survey-response';
import Respondent from './respondent';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/survey`} component={Survey} />
      <ErrorBoundaryRoute path={`${match.url}/question-order`} component={QuestionOrder} />
      <ErrorBoundaryRoute path={`${match.url}/question`} component={Question} />
      <ErrorBoundaryRoute path={`${match.url}/question-type`} component={QuestionType} />
      <ErrorBoundaryRoute path={`${match.url}/response-choice`} component={ResponseChoice} />
      <ErrorBoundaryRoute path={`${match.url}/vee-response`} component={VeeResponse} />
      <ErrorBoundaryRoute path={`${match.url}/survey-response`} component={SurveyResponse} />
      <ErrorBoundaryRoute path={`${match.url}/respondent`} component={Respondent} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
