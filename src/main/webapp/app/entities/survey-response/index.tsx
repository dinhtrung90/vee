import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SurveyResponse from './survey-response';
import SurveyResponseDetail from './survey-response-detail';
import SurveyResponseUpdate from './survey-response-update';
import SurveyResponseDeleteDialog from './survey-response-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SurveyResponseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SurveyResponseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SurveyResponseDetail} />
      <ErrorBoundaryRoute path={match.url} component={SurveyResponse} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SurveyResponseDeleteDialog} />
  </>
);

export default Routes;
