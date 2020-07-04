
// NGRX
import { createFeatureSelector } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter, Update } from '@ngrx/entity';
// Actions
import { JobsListActionTypes, JobsListActions } from '../_actions/jobs-list-actions';
// CRUD
import { QueryParamsModel } from '../../_base/crud';
// Models
import { JobsList } from '../_model/jobs-list-model';

export interface  JobsListState extends EntityState<JobsList> {
    jobsLoading: boolean;
    actionsloading: boolean;
    totalCount: number;
    lastQuery: QueryParamsModel;
    lastSelectedJobId: number;
    showInitWaitingMessage: boolean;
}

export const adapter: EntityAdapter<JobsList> = createEntityAdapter<JobsList>();

export const initialJobsListState: JobsListState = adapter.getInitialState({
    jobsLoading: false,
    actionsloading: false,
    totalCount: 0,
    lastQuery:  new QueryParamsModel({}),
    lastSelectedJobId: undefined,
    showInitWaitingMessage: true
});

export function JobsListReducer(state = initialJobsListState, action:  JobsListActions): JobsListState {
    switch  (action.type) {  
        case JobsListActionTypes.JobsListPageToggleLoading: return {
            ...state, jobsLoading: action.payload.isLoading
        };
        case JobsListActionTypes.JobsListActionToggleLoading: return {
            ...state, actionsloading: action.payload.isLoading
        };  
        case JobsListActionTypes.JobsListLoaded:
            return adapter.addMany(action.payload.jobslist, {
                ...initialJobsListState,
                totalCount: action.payload.totalCount,
                listLoading: false,
                lastQuery: action.payload.page,
                showInitWaitingMessage: false                
            });
        case JobsListActionTypes.JobSelectedFromTheList: return{
            ...state,lastSelectedJobId: action.payload.job.id
        };
        default: return state;
    }
}

export const getJobslistState = createFeatureSelector<JobsList>('jobslist');

export const {
    selectAll,
    selectEntities,
    selectIds,
    selectTotal
} = adapter.getSelectors();
