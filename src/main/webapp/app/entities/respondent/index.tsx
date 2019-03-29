import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Respondent from './respondent';
import RespondentDetail from './respondent-detail';
import RespondentUpdate from './respondent-update';
import RespondentDeleteDialog from './respondent-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RespondentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RespondentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RespondentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Respondent} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RespondentDeleteDialog} />
  </>
);

export default Routes;
