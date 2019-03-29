import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Survey from './survey';
import SurveyDetail from './survey-detail';
import SurveyUpdate from './survey-update';
import SurveyDeleteDialog from './survey-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SurveyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SurveyUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SurveyDetail} />
      <ErrorBoundaryRoute path={match.url} component={Survey} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SurveyDeleteDialog} />
  </>
);

export default Routes;
