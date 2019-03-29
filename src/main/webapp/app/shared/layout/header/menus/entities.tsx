import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name="Entities" id="entity-menu">
    <DropdownItem tag={Link} to="/entity/survey">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;Survey
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/question-order">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;Question Order
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/question">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;Question
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/question-type">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;Question Type
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/response-choice">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;Response Choice
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/vee-response">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;Vee Response
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/survey-response">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;Survey Response
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/respondent">
      <FontAwesomeIcon icon="asterisk" fixedWidth />&nbsp;Respondent
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
