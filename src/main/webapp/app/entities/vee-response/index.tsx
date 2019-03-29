import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import VeeResponse from './vee-response';
import VeeResponseDetail from './vee-response-detail';
import VeeResponseUpdate from './vee-response-update';
import VeeResponseDeleteDialog from './vee-response-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VeeResponseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VeeResponseUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VeeResponseDetail} />
      <ErrorBoundaryRoute path={match.url} component={VeeResponse} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={VeeResponseDeleteDialog} />
  </>
);

export default Routes;
