// NGRX
import { Action } from '@ngrx/store';
import { Update } from '@ngrx/entity';
// CRUD
import { QueryParamsModel } from '../../_base/crud';
// Models
import { DataSetDataModel } from '../_model/dataset_data-model';


export enum DataSetDataActionTypes {
    DataSetDataOnServerCreated = '[Dataset Maintenance Screen] Dataset Record On the Server Created',
    DataSetDataRequested = '[Dataset Maintenance Screen Requested] Dynamic Header Requested',
    DataSetDataLoaded = '[Dataset Maintenance Screen Loaded] Dynamic Header Loaded',
    DataSetDataPageToggleLoading = '[Dataset Maintenance Screen Action Toggle] Dynamic Header Page Toggle Loading',
    DataSetDataActionToggleLoading = '[Dataset Maintenance Screen Page Toggl] Dynamic Header Action Toggle Loading',
    DataSetDataCreated = '[Dataset Maintenance Screen] Record Created',
    DataSetDataUpdated = '[Dataset Maintenance Screen] Record Updated',
    DataSetDataOperationResult = '[Dataset Maintenance Screen] DataSetData Operation Result',
    DataSetDataResetOperationResult = '[Dataset Maintenance Screen] Reset DataSetData Operation Result',    
    DataSetDataItemDeleted='[Dataset Items Operation] Dataset Item Deleted',
    DataSetDataItemUpdated='[Dataset Items Operation] Dataset Item Updated',
    DataSetDataItemCreated='[Dataset Items Operation] Dataset Item Created',
    DataSetDataModifiedReset='[Dataset Maintenance Screen] DataSet Modified Data Reset',
    ResetLastModifiedTableFlag='[Dataset Maintenance Screen] Reset Last Modified Table Flagt'   
}

export class DataSetDataRequested implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataRequested;
        constructor(public payload: { dataset_id: number, page: QueryParamsModel }) { }
    }

export class DataSetDataLoaded implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataLoaded;
        constructor(public payload: { dataset_data: DataSetDataModel[], totalCount: number, page: QueryParamsModel }) { }
    }

    export class DataSetDataPageToggleLoading implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataPageToggleLoading;
        constructor(public payload: { isLoading: boolean }) { }
    }
    
export class DataSetDataActionToggleLoading implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataActionToggleLoading;
        constructor(public payload: { isLoading: boolean }) { }
    }

    export class DataSetDataCreated implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataCreated;
        constructor(public payload: { dataset_data: DataSetDataModel }) { }
    }

    export class DataSetDataOnServerCreated implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataOnServerCreated;
        constructor(public payload: { model_id: number, dataset_data: any, isMaster: boolean ,dataset_id: number }) { }
    }

    export class DataSetDataOperationResult implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataOperationResult;
        constructor(public payload: { is_success: boolean, saved_ds_id: number, jobId:string, message: string }) { }
    }
    export class DataSetDataResetOperationResult implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataResetOperationResult;
        constructor() { }
    }

    export class ResetLastModifiedTableFlag implements Action {
        readonly type = DataSetDataActionTypes.ResetLastModifiedTableFlag;
        constructor() { }
    }
    
    export class DataSetDataUpdated implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataUpdated;
        constructor(public payload: {
            partialData: Update<DataSetDataModel>,
            dataset_data: DataSetDataModel
            
        }) { }
    } 

     export class DataSetDataItemUpdated implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataItemUpdated;
        constructor(public payload: {
            partialData: Update<DataSetDataModel>,
            dataset_data: DataSetDataModel,
            affected_table: string
        }) { }
    } 

    export class DataSetDataItemDeleted implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataItemDeleted;
        constructor(public payload: {dataset_data: number[],affected_table: string}) { }        
    } 
    
    export class DataSetDataModifiedReset implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataModifiedReset;
        constructor() { }        
    }  
    
    export class DataSetDataItemCreated implements Action {
        readonly type = DataSetDataActionTypes.DataSetDataItemCreated;
        constructor(public payload: {dataset_data: DataSetDataModel[],affected_table: string}) { }        
    } 

export type  DataSetDataActions = DataSetDataOnServerCreated
                                |DataSetDataRequested
                                |DataSetDataLoaded
                                |DataSetDataPageToggleLoading
                                |DataSetDataActionToggleLoading
                                |DataSetDataCreated
                                |DataSetDataUpdated
                                |DataSetDataOperationResult
                                |DataSetDataResetOperationResult
                                |DataSetDataItemDeleted
                                |DataSetDataItemUpdated
                                |DataSetDataItemCreated
                                |DataSetDataModifiedReset
                                |ResetLastModifiedTableFlag;