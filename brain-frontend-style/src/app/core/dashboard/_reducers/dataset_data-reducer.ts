// NGRX
import { createFeatureSelector } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter, Update } from '@ngrx/entity';
// Actions
import { DataSetDataActions, DataSetDataModifiedReset, DataSetDataActionTypes, DataSetDataOperationResult } from '../_actions/dataset_data-actions';
// Models
import { DataSetDataModel } from '../_model/dataset_data-model';

export interface DataSetDataState extends EntityState<DataSetDataModel> {
    columnsLoading: boolean;
    actionsloading: boolean;
    totalCount: number;
    lastSelectedDatasetId: number;
    isLastSavingOperationSucceed:boolean;
    lastSavingOperationMessage:string;
    lastTableAffectedByOper:string;
    lastSavedDatasetId:number;
    lastGeneratedJobId:string;    
    showInitWaitingMessage: boolean;
    dataModified:boolean;
}

export const adapter: EntityAdapter<DataSetDataModel> = createEntityAdapter<DataSetDataModel>();

export const initialDataSetDataState: DataSetDataState  = adapter.getInitialState({
    columnsLoading: false,
    actionsloading: false,
    totalCount: 0,
    lastSavingOperationMessage: undefined,
    lastSelectedDatasetId: undefined,
    isLastSavingOperationSucceed: false,
    lastTableAffectedByOper:undefined,
    lastSavedDatasetId: undefined,
    lastGeneratedJobId: undefined,
    showInitWaitingMessage: true,
    dataModified:false
});

export function DataSetDataReducer(state = initialDataSetDataState, action: DataSetDataActions): DataSetDataState {
    switch  (action.type) {    
        case DataSetDataActionTypes.DataSetDataLoaded:
            return adapter.addMany(action.payload.dataset_data, {
                ...initialDataSetDataState, 
                listLoading: false,
                showInitWaitingMessage: false,
                dataModified:false
            });
            case DataSetDataActionTypes.DataSetDataOnServerCreated: return {
                ...state
            };
            case DataSetDataActionTypes.DataSetDataCreated: return adapter.addOne(action.payload.dataset_data, {
                ...state, lastCreatedDataId: action.payload.dataset_data.record_id
            });
            case DataSetDataActionTypes.DataSetDataOperationResult: return{
                ...state,lastSavedDatasetId: (action.payload.saved_ds_id?action.payload.saved_ds_id:undefined),
                         lastSavingOperationMessage: (action.payload.message?action.payload.message:undefined),
                         isLastSavingOperationSucceed: (action.payload.is_success?action.payload.is_success:undefined),
                         lastGeneratedJobId: (action.payload.jobId?action.payload.jobId:undefined)
            };
            case DataSetDataActionTypes.DataSetDataResetOperationResult: return{
                ...state,isLastSavingOperationSucceed:false,lastSavedDatasetId:undefined,lastSavingOperationMessage:undefined, lastGeneratedJobId:undefined, lastTableAffectedByOper:undefined 
            };
            case DataSetDataActionTypes.DataSetDataUpdated: return adapter.updateOne(action.payload.partialData, state);
            case DataSetDataActionTypes.DataSetDataItemUpdated: return adapter.updateOne(action.payload.partialData, {...state, dataModified: true, lastTableAffectedByOper:action.payload.affected_table});
            case DataSetDataActionTypes.DataSetDataItemDeleted: return adapter.removeMany(action.payload.dataset_data, {...state, dataModified: true, lastTableAffectedByOper:action.payload.affected_table});
            case DataSetDataActionTypes.DataSetDataItemCreated: return adapter.addMany(action.payload.dataset_data, {...state, dataModified: true, lastTableAffectedByOper:action.payload.affected_table});
            case DataSetDataActionTypes.DataSetDataModifiedReset: return {...initialDataSetDataState, dataModified: false,lastTableAffectedByOper: undefined}
            case DataSetDataActionTypes.ResetLastModifiedTableFlag: return { ...state,lastTableAffectedByOper: undefined }
            default: return state;
    }
}

export const getDataSetDataState = createFeatureSelector<DataSetDataModel>('dataset_data');

export const {
    selectAll,
    selectEntities,
    selectIds,
    selectTotal
} = adapter.getSelectors();