
// NGRX
import { createFeatureSelector } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter, Update } from '@ngrx/entity';
// CRUD
import { QueryParamsModel } from '../../_base/crud';
// Actions
import { DataSetsListActionTypes, DataSetListActions } from '../_actions/dataset-list-action';
// Models
import { DataSetList } from '../_model/dataset-list-model';

export interface  DataSetListState extends EntityState<DataSetList> {
    datasetsLoading: boolean;
    actionsloading: boolean;
    lastQueryWhenDsSelected: QueryParamsModel;
    lastSelectedDataset: number;
    totalCount: number;
    showInitWaitingMessage: boolean;
}

export const adapter: EntityAdapter<DataSetList> = createEntityAdapter<DataSetList>();

export const initialDataSetListState: DataSetListState = adapter.getInitialState({
    datasetsLoading: false,
    actionsloading: false,
    lastQueryWhenDsSelected: new QueryParamsModel({}),
    lastSelectedDataset: undefined,
    totalCount: 0,
    showInitWaitingMessage: true
});

export function DataSetListReducer(state = initialDataSetListState, action:  DataSetListActions): DataSetListState {
    switch  (action.type) {  
        case  DataSetsListActionTypes.DataSetListPageToggleLoading: return {
            ...state, datasetsLoading: action.payload.isLoading
        };
        case  DataSetsListActionTypes.DataSetListActionToggleLoading: return {
            ...state, actionsloading: action.payload.isLoading
        };  
        case  DataSetsListActionTypes.DataSetListLoaded:
            return adapter.addMany(action.payload.dslist, {
                ...initialDataSetListState,
                totalCount: action.payload.totalCount,
                listLoading: false,
                lastQueryWhenDsSelected: action.payload.page,
                lastSelectedDataset: action.payload.selected,
                showInitWaitingMessage: false                
            });
        case DataSetsListActionTypes.DataSetSelectedFromList: return{
            ...state,lastSelectedDataset: (action.payload.dslist?action.payload.dslist:undefined)
        };
        case DataSetsListActionTypes.DataSetSelectedFromtheStructure: return{
            ...state,lastSelectedDataset: (action.payload.newValue?action.payload.newValue:undefined), lastQueryWhenDsSelected: undefined
        };
        case DataSetsListActionTypes.DataSetListReset: return{ ...initialDataSetListState };
        default: return state;
    }
}

export const getDataSetlistState = createFeatureSelector<DataSetList>('dataset');

export const {
    selectAll,
    selectEntities,
    selectIds,
    selectTotal
} = adapter.getSelectors();
