import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './question-order.reducer';
import { IQuestionOrder } from 'app/shared/model/question-order.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IQuestionOrderDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class QuestionOrderDetail extends React.Component<IQuestionOrderDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { questionOrderEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            QuestionOrder [<b>{questionOrderEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="order">Order</span>
            </dt>
            <dd>{questionOrderEntity.order}</dd>
            <dt>Survey</dt>
            <dd>{questionOrderEntity.survey ? questionOrderEntity.survey.id : ''}</dd>
            <dt>Question Order</dt>
            <dd>{questionOrderEntity.questionOrder ? questionOrderEntity.questionOrder.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/question-order" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/question-order/${questionOrderEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ questionOrder }: IRootState) => ({
  questionOrderEntity: questionOrder.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(QuestionOrderDetail);
