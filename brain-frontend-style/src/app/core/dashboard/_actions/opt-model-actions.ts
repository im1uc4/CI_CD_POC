// NGRX
import { Action } from '@ngrx/store';
// CRUD
import { QueryParamsModel } from '../../_base/crud';
// Models
import {OptModel} from '../_model/opt-model';
import { Update } from '@ngrx/entity';

export enum OptModelActionTypes {
    OptModuleOnServerCreated = '[Edit OptModule Component] OptModules On Server Created',   
    OptModulesStatusUpdated = '[OptModules List Page] OptModules Status Updated',
    OptModulesPageRequested = '[OptModules List Page] OptModules Page Requested',
    OptModulesPageLoaded = '[OptModules API] OptModules Page Loaded',
    OptModulesPageCancelled = '[OptModules API] OptModules Page Cancelled',
    OptModuleSelectedFromDashboard = '[OptModules API] OptModule Selected From DashBoard',  
    OptModuleReset = '[OptModules API] OptModule Reset',    
}

export class OptModuleOnServerCreated  implements Action {
    readonly type = OptModelActionTypes.OptModuleOnServerCreated;
    constructor(public payload: { models: OptModel }) { }
}

export class  OptModuleSelectedFromDashboard implements Action {
    readonly type = OptModelActionTypes. OptModuleSelectedFromDashboard;
    constructor(public payload: { model: OptModel }) { }
}

export class OptModulesStatusUpdated  implements Action {
    readonly type = OptModelActionTypes.OptModulesStatusUpdated;
    constructor(public payload: {
        models: OptModel[],
        status: number
    }) { }
}

export class OptModulesPageRequested implements Action {
        readonly type = OptModelActionTypes.OptModulesPageRequested;
        constructor() { }
    }

export class OptModulesPageLoaded implements Action {
        readonly type = OptModelActionTypes.OptModulesPageLoaded;
        constructor(public payload: { models: OptModel[], totalCount: number, page: QueryParamsModel }) { }
    }

export class OptModuleReset implements Action {
        readonly type = OptModelActionTypes.OptModuleReset;
        constructor() { }
    }
    
export class OptModulesPageCancelled implements Action {
        readonly type = OptModelActionTypes.OptModulesPageCancelled;
    }    


export type  OptModulesActions = OptModuleOnServerCreated
    | OptModulesStatusUpdated
    | OptModulesPageRequested
    | OptModulesPageRequested
    | OptModulesPageLoaded
    | OptModulesPageCancelled
    | OptModuleSelectedFromDashboard
    | OptModuleReset;
