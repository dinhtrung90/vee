import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IQuestionOrder, defaultValue } from 'app/shared/model/question-order.model';

export const ACTION_TYPES = {
  SEARCH_QUESTIONORDERS: 'questionOrder/SEARCH_QUESTIONORDERS',
  FETCH_QUESTIONORDER_LIST: 'questionOrder/FETCH_QUESTIONORDER_LIST',
  FETCH_QUESTIONORDER: 'questionOrder/FETCH_QUESTIONORDER',
  CREATE_QUESTIONORDER: 'questionOrder/CREATE_QUESTIONORDER',
  UPDATE_QUESTIONORDER: 'questionOrder/UPDATE_QUESTIONORDER',
  DELETE_QUESTIONORDER: 'questionOrder/DELETE_QUESTIONORDER',
  RESET: 'questionOrder/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IQuestionOrder>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type QuestionOrderState = Readonly<typeof initialState>;

// Reducer

export default (state: QuestionOrderState = initialState, action): QuestionOrderState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_QUESTIONORDERS):
    case REQUEST(ACTION_TYPES.FETCH_QUESTIONORDER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_QUESTIONORDER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_QUESTIONORDER):
    case REQUEST(ACTION_TYPES.UPDATE_QUESTIONORDER):
    case REQUEST(ACTION_TYPES.DELETE_QUESTIONORDER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_QUESTIONORDERS):
    case FAILURE(ACTION_TYPES.FETCH_QUESTIONORDER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_QUESTIONORDER):
    case FAILURE(ACTION_TYPES.CREATE_QUESTIONORDER):
    case FAILURE(ACTION_TYPES.UPDATE_QUESTIONORDER):
    case FAILURE(ACTION_TYPES.DELETE_QUESTIONORDER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_QUESTIONORDERS):
    case SUCCESS(ACTION_TYPES.FETCH_QUESTIONORDER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_QUESTIONORDER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_QUESTIONORDER):
    case SUCCESS(ACTION_TYPES.UPDATE_QUESTIONORDER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_QUESTIONORDER):
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

const apiUrl = 'api/question-orders';
const apiSearchUrl = 'api/_search/question-orders';

// Actions

export const getSearchEntities: ICrudSearchAction<IQuestionOrder> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_QUESTIONORDERS,
  payload: axios.get<IQuestionOrder>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IQuestionOrder> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_QUESTIONORDER_LIST,
  payload: axios.get<IQuestionOrder>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IQuestionOrder> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_QUESTIONORDER,
    payload: axios.get<IQuestionOrder>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IQuestionOrder> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_QUESTIONORDER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IQuestionOrder> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_QUESTIONORDER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IQuestionOrder> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_QUESTIONORDER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
