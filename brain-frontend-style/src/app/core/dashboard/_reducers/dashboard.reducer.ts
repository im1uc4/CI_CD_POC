
// NGRX
import { createFeatureSelector } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter, Update } from '@ngrx/entity';
// Actions
import { OptModulesActions, OptModelActionTypes } from '../_actions/opt-model-actions';
// CRUD
import { QueryParamsModel } from '../../_base/crud';
// Models
import { OptModel } from '../_model/opt-model';

export interface OptModelState extends EntityState<OptModel> {
    modelLoading: boolean;
    actionsloading: boolean;
    totalCount: number;
    lastSelectedModelId: number;
    lastQuery: QueryParamsModel;
    showInitWaitingMessage: boolean;
}

export const adapter: EntityAdapter<OptModel> = createEntityAdapter<OptModel>();

export const initialOptModelState: OptModelState  = adapter.getInitialState({
    modelLoading: false,
    actionsloading: false,
    totalCount: 0,
    lastSelectedModelId:undefined,
    lastQuery: new QueryParamsModel({}), 
    showInitWaitingMessage: true
});

export function OptModelsReducer(state = initialOptModelState, action: OptModulesActions): OptModelState {
    switch  (action.type) {    
        case OptModelActionTypes.OptModulesPageLoaded:
            return adapter.addMany(action.payload.models, {
                ...initialOptModelState,
                totalCount: action.payload.totalCount,
                listLoading: false,
                lastQuery: action.payload.page,
                showInitWaitingMessage: false
            });
        case OptModelActionTypes.OptModuleSelectedFromDashboard: return{
            ...state,lastSelectedModelId: action.payload.model.id
        };   
        case OptModelActionTypes.OptModuleReset: return { ...initialOptModelState }    
        default: return state;
    }
}

export const getDashboardState = createFeatureSelector<OptModel>('models');

export const {
    selectAll,
    selectEntities,
    selectIds,
    selectTotal
} = adapter.getSelectors();