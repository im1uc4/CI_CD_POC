// NGRX
import { Action } from '@ngrx/store';
// CRUD
import { QueryParamsModel } from '../../_base/crud';
// Models
import {OptModelConstraints} from '../_model/opt-model-constraints';


export enum OptModelConstraintsActionTypes {
    OptModelConstraintsListRequested = '[OptModelConstraints List Requested] OptModelConstraints List Requested',
    OptModelConstraintsListLoaded = '[OptModelConstraints API List Loaded] OptModelConstraints List Loaded'
}

export class OptModelConstraintsListRequested implements Action {
        readonly type = OptModelConstraintsActionTypes.OptModelConstraintsListRequested;
        constructor(public payload: { id: number }) { }
    }

export class OptModelConstraintsListLoaded implements Action {
        readonly type = OptModelConstraintsActionTypes.OptModelConstraintsListLoaded;
        constructor(public payload: { constraints: OptModelConstraints[], totalCount: number }) { }
    }

export type  OptModelConstraintsActions = OptModelConstraintsListRequested
                                        | OptModelConstraintsListLoaded;