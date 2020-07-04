// NGRX
import { Action } from '@ngrx/store';
// CRUD
import { QueryParamsModel } from '../../_base/crud';
// Models
import { JobsList } from '../_model/jobs-list-model';


export enum JobsListActionTypes {
    JobsListRequested = '[JobsList Requested] Job List Requested',
    JobsListLoaded = '[JobsList Loaded] Job List Loaded',
    JobsListPageToggleLoading = '[JobsList] Job Lists Page Toggle Loading',
    JobsListActionToggleLoading = '[JobsList] Job Lists Action Toggle Loading',
    JobSelectedFromTheList = '[JobsList] Last Job Selected From the List'
}

export class JobsListRequested implements Action {
        readonly type = JobsListActionTypes.JobsListRequested;
        constructor(public payload: { id: number, page: QueryParamsModel }) { }
    }

export class JobsListLoaded implements Action {
        readonly type = JobsListActionTypes.JobsListLoaded;
        constructor(public payload: { jobslist: JobsList[], totalCount: number, page: QueryParamsModel }) { }
    }

export class JobsListPageToggleLoading implements Action {
        readonly type = JobsListActionTypes.JobsListPageToggleLoading;
        constructor(public payload: { isLoading: boolean }) { }
    }
    
export class JobsListActionToggleLoading implements Action {
        readonly type = JobsListActionTypes.JobsListActionToggleLoading;
        constructor(public payload: { isLoading: boolean }) { }
    }

export class JobSelectedFromTheList implements Action {
        readonly type = JobsListActionTypes.JobSelectedFromTheList
        constructor(public payload: {job:  JobsList }) { }
    }

export type  JobsListActions = JobsListRequested
                                        | JobsListLoaded
                                        | JobsListPageToggleLoading
                                        | JobsListActionToggleLoading
                                        | JobSelectedFromTheList
