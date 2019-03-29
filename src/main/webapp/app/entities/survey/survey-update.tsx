import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './survey.reducer';
import { ISurvey } from 'app/shared/model/survey.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISurveyUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISurveyUpdateState {
  isNew: boolean;
}

export class SurveyUpdate extends React.Component<ISurveyUpdateProps, ISurveyUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    values.updated = convertDateTimeToServer(values.updated);
    values.openingtime = convertDateTimeToServer(values.openingtime);
    values.closingtime = convertDateTimeToServer(values.closingtime);

    if (errors.length === 0) {
      const { surveyEntity } = this.props;
      const entity = {
        ...surveyEntity,
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
    this.props.history.push('/entity/survey');
  };

  render() {
    const { surveyEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="veeApp.survey.home.createOrEditLabel">Create or edit a Survey</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : surveyEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="survey-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField id="survey-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="descriptionLabel" for="description">
                    Description
                  </Label>
                  <AvField id="survey-description" type="text" name="description" />
                </AvGroup>
                <AvGroup>
                  <Label id="updatedLabel" for="updated">
                    Updated
                  </Label>
                  <AvInput
                    id="survey-updated"
                    type="datetime-local"
                    className="form-control"
                    name="updated"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.surveyEntity.updated)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="openingtimeLabel" for="openingtime">
                    Openingtime
                  </Label>
                  <AvInput
                    id="survey-openingtime"
                    type="datetime-local"
                    className="form-control"
                    name="openingtime"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.surveyEntity.openingtime)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="closingtimeLabel" for="closingtime">
                    Closingtime
                  </Label>
                  <AvInput
                    id="survey-closingtime"
                    type="datetime-local"
                    className="form-control"
                    name="closingtime"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.surveyEntity.closingtime)}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/survey" replace color="info">
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
  surveyEntity: storeState.survey.entity,
  loading: storeState.survey.loading,
  updating: storeState.survey.updating,
  updateSuccess: storeState.survey.updateSuccess
});

const mapDispatchToProps = {
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
)(SurveyUpdate);
