// NGRX
import { Action } from '@ngrx/store';
// CRUD
import { QueryParamsModel } from '../../_base/crud';
// Models
import {OptModelConstraints_param} from '../_model/opt-model-constraints-param';


export enum OptModelConstraintsParamActions {
    OptModelConstraints_paramOnServerCreated = '[Edit OptModelConstraints Param Dialogue] OptModelConstraints Param On Server Created',   
    OptModelConstraints_paramPageRequested = '[OptModelConstraints Param List Page] OptModelConstraints Param Page Requested',
    OptModelConstraints_paramPageLoaded = '[OptModelConstraints Param API] OptModelConstraints Param Page Loaded',
    OptModelConstraints_paramPageCancelled = '[OptModelConstraints Param API]OptModelConstraints Param Page Cancelled'
}

export class OptModelConstraints_paramOnServerCreated  implements Action {
    readonly type =OptModelConstraintsParamActions.OptModelConstraints_paramOnServerCreated;
    constructor(public payload: { product: OptModelConstraints_param }) { }
}

export class OptModelConstraints_paramPageRequested implements Action {
        readonly type = OptModelConstraintsParamActions.OptModelConstraints_paramPageRequested;
        constructor(public payload: { page: QueryParamsModel, id: number }) { }
    }

export class OptModelConstraints_paramPageLoaded implements Action {
        readonly type = OptModelConstraintsParamActions.OptModelConstraints_paramPageLoaded;
        constructor(public payload: { products: OptModelConstraints_param[], totalCount: number, page: QueryParamsModel }) { }
    }


export type  OptModelConstraintsActions =  OptModelConstraints_paramOnServerCreated
    | OptModelConstraints_paramPageRequested
    | OptModelConstraints_paramPageLoaded
    | OptModelConstraints_paramPageLoaded;
