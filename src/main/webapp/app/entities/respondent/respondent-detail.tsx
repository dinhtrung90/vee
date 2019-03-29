import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './respondent.reducer';
import { IRespondent } from 'app/shared/model/respondent.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRespondentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RespondentDetail extends React.Component<IRespondentDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { respondentEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Respondent [<b>{respondentEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="avatarUrl">Avatar Url</span>
            </dt>
            <dd>{respondentEntity.avatarUrl}</dd>
            <dt>
              <span id="email">Email</span>
            </dt>
            <dd>{respondentEntity.email}</dd>
            <dt>
              <span id="birthDay">Birth Day</span>
            </dt>
            <dd>
              <TextFormat value={respondentEntity.birthDay} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="gender">Gender</span>
            </dt>
            <dd>{respondentEntity.gender}</dd>
            <dt>User</dt>
            <dd>{respondentEntity.userId ? respondentEntity.userId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/respondent" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/respondent/${respondentEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ respondent }: IRootState) => ({
  respondentEntity: respondent.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RespondentDetail);
