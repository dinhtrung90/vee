import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISurvey } from 'app/shared/model/survey.model';
import { getEntities as getSurveys } from 'app/entities/survey/survey.reducer';
import { IQuestion } from 'app/shared/model/question.model';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { getEntity, updateEntity, createEntity, reset } from './question-order.reducer';
import { IQuestionOrder } from 'app/shared/model/question-order.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IQuestionOrderUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IQuestionOrderUpdateState {
  isNew: boolean;
  surveyId: string;
  questionOrderId: string;
}

export class QuestionOrderUpdate extends React.Component<IQuestionOrderUpdateProps, IQuestionOrderUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      surveyId: '0',
      questionOrderId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getSurveys();
    this.props.getQuestions();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { questionOrderEntity } = this.props;
      const entity = {
        ...questionOrderEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/question-order');
  };

  render() {
    const { questionOrderEntity, surveys, questions, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="veeApp.questionOrder.home.createOrEditLabel">Create or edit a QuestionOrder</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : questionOrderEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="question-order-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="orderLabel" for="order">
                    Order
                  </Label>
                  <AvField id="question-order-order" type="string" className="form-control" name="order" />
                </AvGroup>
                <AvGroup>
                  <Label for="survey.id">Survey</Label>
                  <AvInput id="question-order-survey" type="select" className="form-control" name="survey.id">
                    <option value="" key="0" />
                    {surveys
                      ? surveys.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="questionOrder.id">Question Order</Label>
                  <AvInput id="question-order-questionOrder" type="select" className="form-control" name="questionOrder.id">
                    <option value="" key="0" />
                    {questions
                      ? questions.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/question-order" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  surveys: storeState.survey.entities,
  questions: storeState.question.entities,
  questionOrderEntity: storeState.questionOrder.entity,
  loading: storeState.questionOrder.loading,
  updating: storeState.questionOrder.updating,
  updateSuccess: storeState.questionOrder.updateSuccess
});

const mapDispatchToProps = {
  getSurveys,
  getQuestions,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(QuestionOrderUpdate);
