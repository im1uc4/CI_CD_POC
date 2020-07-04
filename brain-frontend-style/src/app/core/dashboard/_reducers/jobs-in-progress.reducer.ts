
// NGRX
import { createFeatureSelector } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter, Update } from '@ngrx/entity';
// Actions
import { JobsCreated, JobsUpdated, JobsReset, JobsStop, JobsInProgresActions,JobsInProgressActionTypes } from '../_actions/job-in-progress.action';
// Models
import { JobInProgressModel } from '../_model/job-in-progress.model';

export interface JobInProgressState extends EntityState<JobInProgressModel> {
    curentJobId:string;
    isLastSavingOperationSucceed:boolean;
    lastSavingOperationMessage:string;
}

export const adapter: EntityAdapter<JobInProgressModel> = createEntityAdapter<JobInProgressModel>();

export const initialJobInProgressState: JobInProgressState = adapter.getInitialState({
    curentJobId: "",
    isLastSavingOperationSucceed:false,
    lastSavingOperationMessage:undefined,
});

export function JobsInProgressReducer(state = initialJobInProgressState, action:  JobsInProgresActions): JobInProgressState {
    switch  (action.type) {  

        case JobsInProgressActionTypes.JobsCreated: return adapter.addOne(action.payload.jobsInProgress, {
            ...state, curentJobId: action.payload.jobsInProgress.jobId
        });    
        case JobsInProgressActionTypes.JobsUpdated: return  adapter.updateOne(action.payload.partialJob, state);
        case JobsInProgressActionTypes.JobsCompleted: return state;  
        case JobsInProgressActionTypes.JobsStop: return state; 
        case JobsInProgressActionTypes.JobsOperationResult: return{
            ...state,lastSavingOperationMessage: action.payload.message,
                     isLastSavingOperationSucceed: action.payload.is_success
        };
        case JobsInProgressActionTypes.JobsReset: return  initialJobInProgressState
        default: return state;
    }
}

export const getJobsInProgressState = createFeatureSelector<JobInProgressModel>('jobs_in_progress');

export const {
    selectAll,
    selectEntities,
    selectIds,
    selectTotal
} = adapter.getSelectors();
