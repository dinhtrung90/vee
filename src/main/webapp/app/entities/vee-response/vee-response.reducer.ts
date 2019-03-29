import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IVeeResponse, defaultValue } from 'app/shared/model/vee-response.model';

export const ACTION_TYPES = {
  SEARCH_VEERESPONSES: 'veeResponse/SEARCH_VEERESPONSES',
  FETCH_VEERESPONSE_LIST: 'veeResponse/FETCH_VEERESPONSE_LIST',
  FETCH_VEERESPONSE: 'veeResponse/FETCH_VEERESPONSE',
  CREATE_VEERESPONSE: 'veeResponse/CREATE_VEERESPONSE',
  UPDATE_VEERESPONSE: 'veeResponse/UPDATE_VEERESPONSE',
  DELETE_VEERESPONSE: 'veeResponse/DELETE_VEERESPONSE',
  RESET: 'veeResponse/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IVeeResponse>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type VeeResponseState = Readonly<typeof initialState>;

// Reducer

export default (state: VeeResponseState = initialState, action): VeeResponseState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_VEERESPONSES):
    case REQUEST(ACTION_TYPES.FETCH_VEERESPONSE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_VEERESPONSE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_VEERESPONSE):
    case REQUEST(ACTION_TYPES.UPDATE_VEERESPONSE):
    case REQUEST(ACTION_TYPES.DELETE_VEERESPONSE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_VEERESPONSES):
    case FAILURE(ACTION_TYPES.FETCH_VEERESPONSE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_VEERESPONSE):
    case FAILURE(ACTION_TYPES.CREATE_VEERESPONSE):
    case FAILURE(ACTION_TYPES.UPDATE_VEERESPONSE):
    case FAILURE(ACTION_TYPES.DELETE_VEERESPONSE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_VEERESPONSES):
    case SUCCESS(ACTION_TYPES.FETCH_VEERESPONSE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_VEERESPONSE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_VEERESPONSE):
    case SUCCESS(ACTION_TYPES.UPDATE_VEERESPONSE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_VEERESPONSE):
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

const apiUrl = 'api/vee-responses';
const apiSearchUrl = 'api/_search/vee-responses';

// Actions

export const getSearchEntities: ICrudSearchAction<IVeeResponse> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_VEERESPONSES,
  payload: axios.get<IVeeResponse>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IVeeResponse> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_VEERESPONSE_LIST,
  payload: axios.get<IVeeResponse>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IVeeResponse> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_VEERESPONSE,
    payload: axios.get<IVeeResponse>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IVeeResponse> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_VEERESPONSE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IVeeResponse> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_VEERESPONSE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IVeeResponse> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_VEERESPONSE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
