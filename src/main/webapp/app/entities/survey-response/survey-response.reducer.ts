import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISurveyResponse, defaultValue } from 'app/shared/model/survey-response.model';

export const ACTION_TYPES = {
  SEARCH_SURVEYRESPONSES: 'surveyResponse/SEARCH_SURVEYRESPONSES',
  FETCH_SURVEYRESPONSE_LIST: 'surveyResponse/FETCH_SURVEYRESPONSE_LIST',
  FETCH_SURVEYRESPONSE: 'surveyResponse/FETCH_SURVEYRESPONSE',
  CREATE_SURVEYRESPONSE: 'surveyResponse/CREATE_SURVEYRESPONSE',
  UPDATE_SURVEYRESPONSE: 'surveyResponse/UPDATE_SURVEYRESPONSE',
  DELETE_SURVEYRESPONSE: 'surveyResponse/DELETE_SURVEYRESPONSE',
  RESET: 'surveyResponse/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISurveyResponse>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type SurveyResponseState = Readonly<typeof initialState>;

// Reducer

export default (state: SurveyResponseState = initialState, action): SurveyResponseState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SURVEYRESPONSES):
    case REQUEST(ACTION_TYPES.FETCH_SURVEYRESPONSE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SURVEYRESPONSE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SURVEYRESPONSE):
    case REQUEST(ACTION_TYPES.UPDATE_SURVEYRESPONSE):
    case REQUEST(ACTION_TYPES.DELETE_SURVEYRESPONSE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SURVEYRESPONSES):
    case FAILURE(ACTION_TYPES.FETCH_SURVEYRESPONSE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SURVEYRESPONSE):
    case FAILURE(ACTION_TYPES.CREATE_SURVEYRESPONSE):
    case FAILURE(ACTION_TYPES.UPDATE_SURVEYRESPONSE):
    case FAILURE(ACTION_TYPES.DELETE_SURVEYRESPONSE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SURVEYRESPONSES):
    case SUCCESS(ACTION_TYPES.FETCH_SURVEYRESPONSE_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SURVEYRESPONSE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SURVEYRESPONSE):
    case SUCCESS(ACTION_TYPES.UPDATE_SURVEYRESPONSE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SURVEYRESPONSE):
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

const apiUrl = 'api/survey-responses';
const apiSearchUrl = 'api/_search/survey-responses';

// Actions

export const getSearchEntities: ICrudSearchAction<ISurveyResponse> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SURVEYRESPONSES,
  payload: axios.get<ISurveyResponse>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<ISurveyResponse> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SURVEYRESPONSE_LIST,
    payload: axios.get<ISurveyResponse>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ISurveyResponse> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SURVEYRESPONSE,
    payload: axios.get<ISurveyResponse>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISurveyResponse> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SURVEYRESPONSE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISurveyResponse> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SURVEYRESPONSE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISurveyResponse> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SURVEYRESPONSE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
