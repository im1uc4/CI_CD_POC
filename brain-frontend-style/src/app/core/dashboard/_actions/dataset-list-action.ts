// NGRX
import { Action } from '@ngrx/store';
// CRUD
import { QueryParamsModel } from '../../_base/crud';
// Models
import { DataSetList } from '../_model/dataset-list-model';


export enum DataSetsListActionTypes {
    DataSetListRequested = '[DataSet Requested] DataSet Requested',
    DataSetListLoaded = '[DataSet Loaded] DataSet Loaded',
    DataSetListPageToggleLoading = '[DataSet] DataSet Page Toggle Loading',
    DataSetListActionToggleLoading = '[DataSet] DataSet Action Toggle Loading',
    DataSetListOnServerCreated = '[DataSet] DataSet On Server Created',
    DataSetListCreated = '[DataSet] DataSet Created',  
    DataSetSelectedFromList='[DataSet] DataSet Selected',  
    DataSetSelectedFromtheStructure='[DataSet]  DataSet Selected from Structure',
    DataSetListReset='[DataSet] DatasSet Reset'
}

export class DataSetListOnServerCreated implements Action {
    readonly type = DataSetsListActionTypes.DataSetListOnServerCreated;
    constructor(public payload: { dslist: DataSetList }) { }
}

export class DataSetListCreated implements Action {
    DataSetListCreated = DataSetsListActionTypes.DataSetListCreated;    
    readonly type = DataSetsListActionTypes.DataSetListCreated;
    constructor(public payload: { dslist: DataSetList }) { }
}

export class DataSetListRequested implements Action {
        readonly type = DataSetsListActionTypes.DataSetListRequested;
        constructor(public payload: { id: number, page: QueryParamsModel, selected: number }) { }
    }

export class DataSetListLoaded implements Action {
        readonly type = DataSetsListActionTypes.DataSetListLoaded;
        constructor(public payload: { dslist: DataSetList[], totalCount: number, page: QueryParamsModel, selected: number }) { }
    }

export class DataSetListPageToggleLoading implements Action {
        readonly type = DataSetsListActionTypes.DataSetListPageToggleLoading;
        constructor(public payload: { isLoading: boolean }) { }
    }
    
export class DataSetListActionToggleLoading implements Action {
        readonly type = DataSetsListActionTypes.DataSetListActionToggleLoading
        constructor(public payload: { isLoading: boolean }) { }
    }

export class DataSetSelectedFromList implements Action {
        readonly type = DataSetsListActionTypes.DataSetSelectedFromList
        constructor(public payload: { dslist: number  }) { }
    }

export class DataSetListReset implements Action {
        readonly type = DataSetsListActionTypes.DataSetListReset
        constructor() { }
    }


export class DataSetSelectedFromtheStructure implements Action {
        readonly type = DataSetsListActionTypes.DataSetSelectedFromtheStructure
        constructor(public payload: { newValue: number }) { }
    }

export type  DataSetListActions = DataSetListRequested
                        | DataSetListLoaded
                        | DataSetListPageToggleLoading
                        | DataSetListActionToggleLoading
                        | DataSetListCreated
                        | DataSetListOnServerCreated
                        | DataSetSelectedFromList
                        | DataSetSelectedFromtheStructure
                        | DataSetListReset

