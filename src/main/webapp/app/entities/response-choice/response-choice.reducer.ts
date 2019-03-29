import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IResponseChoice, defaultValue } from 'app/shared/model/response-choice.model';

export const ACTION_TYPES = {
  SEARCH_RESPONSECHOICES: 'responseChoice/SEARCH_RESPONSECHOICES',
  FETCH_RESPONSECHOICE_LIST: 'responseChoice/FETCH_RESPONSECHOICE_LIST',
  FETCH_RESPONSECHOICE: 'responseChoice/FETCH_RESPONSECHOICE',
  CREATE_RESPONSECHOICE: 'responseChoice/CREATE_RESPONSECHOICE',
  UPDATE_RESPONSECHOICE: 'responseChoice/UPDATE_RESPONSECHOICE',
  DELETE_RESPONSECHOICE: 'responseChoice/DELETE_RESPONSECHOICE',
  RESET: 'responseChoice/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IResponseChoice>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ResponseChoiceState = Readonly<typeof initialState>;

// Reducer

export default (state: ResponseChoiceState = initialState, action): ResponseChoiceState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_RESPONSECHOICES):
    case REQUEST(ACTION_TYPES.FETCH_RESPONSECHOICE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RESPONSECHOICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RESPONSECHOICE):
    case REQUEST(ACTION_TYPES.UPDATE_RESPONSECHOICE):
    case REQUEST(ACTION_TYPES.DELETE_RESPONSECHOICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_RESPONSECHOICES):
    case FAILURE(ACTION_TYPES.FETCH_RESPONSECHOICE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RESPONSECHOICE):
    case FAILURE(ACTION_TYPES.CREATE_RESPONSECHOICE):
    case FAILURE(ACTION_TYPES.UPDATE_RESPONSECHOICE):
    case FAILURE(ACTION_TYPES.DELETE_RESPONSECHOICE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_RESPONSECHOICES):
    case SUCCESS(ACTION_TYPES.FETCH_RESPONSECHOICE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESPONSECHOICE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RESPONSECHOICE):
    case SUCCESS(ACTION_TYPES.UPDATE_RESPONSECHOICE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RESPONSECHOICE):
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

const apiUrl = 'api/response-choices';
const apiSearchUrl = 'api/_search/response-choices';

// Actions

export const getSearchEntities: ICrudSearchAction<IResponseChoice> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_RESPONSECHOICES,
  payload: axios.get<IResponseChoice>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IResponseChoice> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_RESPONSECHOICE_LIST,
  payload: axios.get<IResponseChoice>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IResponseChoice> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RESPONSECHOICE,
    payload: axios.get<IResponseChoice>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IResponseChoice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RESPONSECHOICE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IResponseChoice> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RESPONSECHOICE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IResponseChoice> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RESPONSECHOICE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
