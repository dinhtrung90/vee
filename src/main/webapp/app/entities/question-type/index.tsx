import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import QuestionType from './question-type';
import QuestionTypeDetail from './question-type-detail';
import QuestionTypeUpdate from './question-type-update';
import QuestionTypeDeleteDialog from './question-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={QuestionTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={QuestionTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={QuestionTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={QuestionType} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={QuestionTypeDeleteDialog} />
  </>
);

export default Routes;
