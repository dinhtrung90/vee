import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IQuestion } from 'app/shared/model/question.model';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { IRespondent } from 'app/shared/model/respondent.model';
import { getEntities as getRespondents } from 'app/entities/respondent/respondent.reducer';
import { getEntity, updateEntity, createEntity, reset } from './vee-response.reducer';
import { IVeeResponse } from 'app/shared/model/vee-response.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IVeeResponseUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IVeeResponseUpdateState {
  isNew: boolean;
  questionId: string;
  respondentId: string;
}

export class VeeResponseUpdate extends React.Component<IVeeResponseUpdateProps, IVeeResponseUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      questionId: '0',
      respondentId: '0',
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

    this.props.getQuestions();
    this.props.getRespondents();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { veeResponseEntity } = this.props;
      const entity = {
        ...veeResponseEntity,
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
    this.props.history.push('/entity/vee-response');
  };

  render() {
    const { veeResponseEntity, questions, respondents, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="veeApp.veeResponse.home.createOrEditLabel">Create or edit a VeeResponse</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : veeResponseEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="vee-response-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="answerLabel" for="answer">
                    Answer
                  </Label>
                  <AvField id="vee-response-answer" type="text" name="answer" />
                </AvGroup>
                <AvGroup>
                  <Label for="question.id">Question</Label>
                  <AvInput id="vee-response-question" type="select" className="form-control" name="questionId">
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
                <AvGroup>
                  <Label for="respondent.id">Respondent</Label>
                  <AvInput id="vee-response-respondent" type="select" className="form-control" name="respondentId">
                    <option value="" key="0" />
                    {respondents
                      ? respondents.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/vee-response" replace color="info">
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
  questions: storeState.question.entities,
  respondents: storeState.respondent.entities,
  veeResponseEntity: storeState.veeResponse.entity,
  loading: storeState.veeResponse.loading,
  updating: storeState.veeResponse.updating,
  updateSuccess: storeState.veeResponse.updateSuccess
});

const mapDispatchToProps = {
  getQuestions,
  getRespondents,
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
)(VeeResponseUpdate);
