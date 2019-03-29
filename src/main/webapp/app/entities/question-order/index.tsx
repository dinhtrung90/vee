import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import QuestionOrder from './question-order';
import QuestionOrderDetail from './question-order-detail';
import QuestionOrderUpdate from './question-order-update';
import QuestionOrderDeleteDialog from './question-order-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={QuestionOrderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={QuestionOrderUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={QuestionOrderDetail} />
      <ErrorBoundaryRoute path={match.url} component={QuestionOrder} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={QuestionOrderDeleteDialog} />
  </>
);

export default Routes;
