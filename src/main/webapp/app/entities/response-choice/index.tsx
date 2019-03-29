import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ResponseChoice from './response-choice';
import ResponseChoiceDetail from './response-choice-detail';
import ResponseChoiceUpdate from './response-choice-update';
import ResponseChoiceDeleteDialog from './response-choice-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ResponseChoiceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ResponseChoiceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ResponseChoiceDetail} />
      <ErrorBoundaryRoute path={match.url} component={ResponseChoice} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ResponseChoiceDeleteDialog} />
  </>
);

export default Routes;
