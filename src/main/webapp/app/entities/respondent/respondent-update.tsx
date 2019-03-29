import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './respondent.reducer';
import { IRespondent } from 'app/shared/model/respondent.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRespondentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRespondentUpdateState {
  isNew: boolean;
  userId: string;
}

export class RespondentUpdate extends React.Component<IRespondentUpdateProps, IRespondentUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      userId: '0',
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

    this.props.getUsers();
  }

  saveEntity = (event, errors, values) => {
    values.birthDay = convertDateTimeToServer(values.birthDay);

    if (errors.length === 0) {
      const { respondentEntity } = this.props;
      const entity = {
        ...respondentEntity,
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
    this.props.history.push('/entity/respondent');
  };

  render() {
    const { respondentEntity, users, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="veeApp.respondent.home.createOrEditLabel">Create or edit a Respondent</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : respondentEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="respondent-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="avatarUrlLabel" for="avatarUrl">
                    Avatar Url
                  </Label>
                  <AvField id="respondent-avatarUrl" type="text" name="avatarUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="emailLabel" for="email">
                    Email
                  </Label>
                  <AvField id="respondent-email" type="text" name="email" />
                </AvGroup>
                <AvGroup>
                  <Label id="birthDayLabel" for="birthDay">
                    Birth Day
                  </Label>
                  <AvInput
                    id="respondent-birthDay"
                    type="datetime-local"
                    className="form-control"
                    name="birthDay"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.respondentEntity.birthDay)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="genderLabel">Gender</Label>
                  <AvInput
                    id="respondent-gender"
                    type="select"
                    className="form-control"
                    name="gender"
                    value={(!isNew && respondentEntity.gender) || 'Male'}
                  >
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="user.id">User</Label>
                  <AvInput id="respondent-user" type="select" className="form-control" name="userId">
                    <option value="" key="0" />
                    {users
                      ? users.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/respondent" replace color="info">
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
  users: storeState.userManagement.users,
  respondentEntity: storeState.respondent.entity,
  loading: storeState.respondent.loading,
  updating: storeState.respondent.updating,
  updateSuccess: storeState.respondent.updateSuccess
});

const mapDispatchToProps = {
  getUsers,
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
)(RespondentUpdate);
