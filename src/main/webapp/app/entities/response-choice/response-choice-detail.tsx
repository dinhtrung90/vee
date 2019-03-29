import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './response-choice.reducer';
import { IResponseChoice } from 'app/shared/model/response-choice.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IResponseChoiceDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ResponseChoiceDetail extends React.Component<IResponseChoiceDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { responseChoiceEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            ResponseChoice [<b>{responseChoiceEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="text">Text</span>
            </dt>
            <dd>{responseChoiceEntity.text}</dd>
            <dt>Question</dt>
            <dd>{responseChoiceEntity.questionId ? responseChoiceEntity.questionId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/response-choice" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/response-choice/${responseChoiceEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ responseChoice }: IRootState) => ({
  responseChoiceEntity: responseChoice.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ResponseChoiceDetail);
