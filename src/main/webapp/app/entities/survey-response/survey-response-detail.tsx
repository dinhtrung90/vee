import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './survey-response.reducer';
import { ISurveyResponse } from 'app/shared/model/survey-response.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISurveyResponseDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class SurveyResponseDetail extends React.Component<ISurveyResponseDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { surveyResponseEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            SurveyResponse [<b>{surveyResponseEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="updated">Updated</span>
            </dt>
            <dd>
              <TextFormat value={surveyResponseEntity.updated} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="startedat">Startedat</span>
            </dt>
            <dd>
              <TextFormat value={surveyResponseEntity.startedat} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="completedat">Completedat</span>
            </dt>
            <dd>
              <TextFormat value={surveyResponseEntity.completedat} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Survey</dt>
            <dd>{surveyResponseEntity.surveyId ? surveyResponseEntity.surveyId : ''}</dd>
            <dt>Respondent</dt>
            <dd>{surveyResponseEntity.respondentId ? surveyResponseEntity.respondentId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/survey-response" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/survey-response/${surveyResponseEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ surveyResponse }: IRootState) => ({
  surveyResponseEntity: surveyResponse.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SurveyResponseDetail);
