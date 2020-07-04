// NGRX
import { Action } from '@ngrx/store';
import { Update } from '@ngrx/entity';
// CRUD
import { QueryParamsModel } from '../../_base/crud';
// Models
import { JobInProgressModel } from '../_model/job-in-progress.model';


export enum JobsInProgressActionTypes {
    JobsCreated     ='[Jobs In Progress] Job in progress created',
    JobsUpdated     ='[Jobs In Progress] Job in progress updated',
    JobsCompleted     ='[Jobs In Progress] Job in progress completed',
    JobsStop       ='[Jobs In Progress] Job in progress stop',   
    JobsReset       ='[Jobs In Progress] Job in progress reset',
    JobsOperationResult       ='[Jobs In Progress] Job in progress operation result',
}

export class JobsCreated implements Action {
        readonly type =  JobsInProgressActionTypes.JobsCreated;
        constructor(public payload: { jobsInProgress: JobInProgressModel, SubmitBody: string }) { }
    }

export class JobsUpdated implements Action {
        readonly type = JobsInProgressActionTypes.JobsUpdated;
        constructor(public payload: { partialJob: Update<JobInProgressModel>,jobInProgressModel: JobInProgressModel  }) { }
    }

    export class  JobsStop  implements Action {
        readonly type = JobsInProgressActionTypes.JobsStop;
        constructor(public payload: { jobId:string }) { }
    }
    
    export class JobsCompleted implements Action {
        readonly type = JobsInProgressActionTypes.JobsCompleted;
        constructor(public payload: { partialJob: Update<JobInProgressModel>,jobInProgressModel: JobInProgressModel, SubmitBody: string  }) { }
    }

export class JobsReset implements Action {
        readonly type = JobsInProgressActionTypes.JobsReset;
        constructor() { }
    }

export class JobsOperationResult implements Action {
    readonly type = JobsInProgressActionTypes.JobsOperationResult;
    constructor(public payload: { is_success: boolean, message: string }) { }
}
 
export type  JobsInProgresActions = JobsCreated
                                | JobsUpdated
                                | JobsCompleted
                                | JobsStop
                                | JobsReset
                                | JobsOperationResult 
