import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRespondent, defaultValue } from 'app/shared/model/respondent.model';

export const ACTION_TYPES = {
  SEARCH_RESPONDENTS: 'respondent/SEARCH_RESPONDENTS',
  FETCH_RESPONDENT_LIST: 'respondent/FETCH_RESPONDENT_LIST',
  FETCH_RESPONDENT: 'respondent/FETCH_RESPONDENT',
  CREATE_RESPONDENT: 'respondent/CREATE_RESPONDENT',
  UPDATE_RESPONDENT: 'respondent/UPDATE_RESPONDENT',
  DELETE_RESPONDENT: 'respondent/DELETE_RESPONDENT',
  RESET: 'respondent/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRespondent>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type RespondentState = Readonly<typeof initialState>;

// Reducer

export default (state: RespondentState = initialState, action): RespondentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RESPONDENTS):
    case REQUEST(ACTION_TYPES.FETCH_RESPONDENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RESPONDENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RESPONDENT):
    case REQUEST(ACTION_TYPES.UPDATE_RESPONDENT):
    case REQUEST(ACTION_TYPES.DELETE_RESPONDENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RESPONDENTS):
    case FAILURE(ACTION_TYPES.FETCH_RESPONDENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RESPONDENT):
    case FAILURE(ACTION_TYPES.CREATE_RESPONDENT):
    case FAILURE(ACTION_TYPES.UPDATE_RESPONDENT):
    case FAILURE(ACTION_TYPES.DELETE_RESPONDENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RESPONDENTS):
    case SUCCESS(ACTION_TYPES.FETCH_RESPONDENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESPONDENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RESPONDENT):
    case SUCCESS(ACTION_TYPES.UPDATE_RESPONDENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RESPONDENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/respondents';
const apiSearchUrl = 'api/_search/respondents';

// Actions

export const getSearchEntities: ICrudSearchAction<IRespondent> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RESPONDENTS,
  payload: axios.get<IRespondent>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IRespondent> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RESPONDENT_LIST,
  payload: axios.get<IRespondent>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IRespondent> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RESPONDENT,
    payload: axios.get<IRespondent>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRespondent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RESPONDENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRespondent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RESPONDENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRespondent> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RESPONDENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
