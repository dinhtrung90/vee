import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISurvey } from 'app/shared/model/survey.model';
import { getEntities as getSurveys } from 'app/entities/survey/survey.reducer';
import { IRespondent } from 'app/shared/model/respondent.model';
import { getEntities as getRespondents } from 'app/entities/respondent/respondent.reducer';
import { getEntity, updateEntity, createEntity, reset } from './survey-response.reducer';
import { ISurveyResponse } from 'app/shared/model/survey-response.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISurveyResponseUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISurveyResponseUpdateState {
  isNew: boolean;
  surveyId: string;
  respondentId: string;
}

export class SurveyResponseUpdate extends React.Component<ISurveyResponseUpdateProps, ISurveyResponseUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      surveyId: '0',
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

    this.props.getSurveys();
    this.props.getRespondents();
  }

  saveEntity = (event, errors, values) => {
    values.updated = convertDateTimeToServer(values.updated);
    values.startedat = convertDateTimeToServer(values.startedat);
    values.completedat = convertDateTimeToServer(values.completedat);

    if (errors.length === 0) {
      const { surveyResponseEntity } = this.props;
      const entity = {
        ...surveyResponseEntity,
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
    this.props.history.push('/entity/survey-response');
  };

  render() {
    const { surveyResponseEntity, surveys, respondents, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="veeApp.surveyResponse.home.createOrEditLabel">Create or edit a SurveyResponse</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : surveyResponseEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="survey-response-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="updatedLabel" for="updated">
                    Updated
                  </Label>
                  <AvInput
                    id="survey-response-updated"
                    type="datetime-local"
                    className="form-control"
                    name="updated"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.surveyResponseEntity.updated)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="startedatLabel" for="startedat">
                    Startedat
                  </Label>
                  <AvInput
                    id="survey-response-startedat"
                    type="datetime-local"
                    className="form-control"
                    name="startedat"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.surveyResponseEntity.startedat)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="completedatLabel" for="completedat">
                    Completedat
                  </Label>
                  <AvInput
                    id="survey-response-completedat"
                    type="datetime-local"
                    className="form-control"
                    name="completedat"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.surveyResponseEntity.completedat)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="survey.id">Survey</Label>
                  <AvInput id="survey-response-survey" type="select" className="form-control" name="surveyId">
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
                  <Label for="respondent.id">Respondent</Label>
                  <AvInput id="survey-response-respondent" type="select" className="form-control" name="respondentId">
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
                <Button tag={Link} id="cancel-save" to="/entity/survey-response" replace color="info">
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
  respondents: storeState.respondent.entities,
  surveyResponseEntity: storeState.surveyResponse.entity,
  loading: storeState.surveyResponse.loading,
  updating: storeState.surveyResponse.updating,
  updateSuccess: storeState.surveyResponse.updateSuccess
});

const mapDispatchToProps = {
  getSurveys,
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
)(SurveyResponseUpdate);
