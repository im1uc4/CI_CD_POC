// NGRX
import { Action } from '@ngrx/store';
// CRUD
import { QueryParamsModel } from '../../_base/crud';
// Models
import { DynamicHeader } from '../_model/dynamic_dataset_headers-model';


export enum DynamicHeaderActionTypes {
    DynamicHeaderRequested = '[Dynamic Header Requested] Dynamic Header Requested',
    DynamicHeaderLoaded = '[Dynamic Header Loaded] Dynamic Header Loaded',
    DynamicHeaderPageToggleLoading = '[Dynamic Header Action Toggle] Dynamic Header Page Toggle Loading',
    DynamicHeaderActionToggleLoading = '[Dynamic Header Page Toggl] Dynamic Header Action Toggle Loading',
    DynamicHeaderReset = '[Dynamic Header ] Reset State'
}

export class DynamicHeaderRequested implements Action {
        readonly type = DynamicHeaderActionTypes.DynamicHeaderRequested;
        constructor(public payload: { dataset_id: number, page: QueryParamsModel }) { }
    }

export class DynamicHeaderLoaded implements Action {
        readonly type = DynamicHeaderActionTypes.DynamicHeaderLoaded;
        constructor(public payload: { headers: DynamicHeader[], totalCount: number, page: QueryParamsModel }) { }
    }

    export class DynamicHeaderPageToggleLoading implements Action {
        readonly type = DynamicHeaderActionTypes.DynamicHeaderPageToggleLoading;
        constructor(public payload: { isLoading: boolean }) { }
    }

    export class DynamicHeaderReset implements Action {
        readonly type = DynamicHeaderActionTypes.DynamicHeaderReset;
        constructor() { }
    }
    
export class DynamicHeaderActionToggleLoading implements Action {
        readonly type = DynamicHeaderActionTypes.DynamicHeaderActionToggleLoading;
        constructor(public payload: { isLoading: boolean }) { }
    }

export type  DynamicHeaderActions = DynamicHeaderRequested
                                    | DynamicHeaderLoaded
                                    | DynamicHeaderPageToggleLoading
                                    | DynamicHeaderActionToggleLoading
                                    | DynamicHeaderReset;