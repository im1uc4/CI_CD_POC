// NGRX
import { createFeatureSelector, createSelector } from '@ngrx/store';
// Lodash
import { each } from 'lodash';
// CRUD
import { QueryResultsModel, HttpExtenstionsModel } from '../../_base/crud';
// State
import { JobInProgressState } from '../_reducers/jobs-in-progress.reducer';
import { JobInProgressModel } from '../_model/job-in-progress.model';

export const selectJobsInProgressState = createFeatureSelector<JobInProgressState>('jobs_in_progress');

export const selectJobsInProgress =  createSelector(
    selectJobsInProgressState,
    ModelState => {
        const items: JobInProgressModel[] = [];
        each(ModelState.entities, element => {
            items.push(element);
        });

        return {items: items, lastOperationSucceed: ModelState.isLastSavingOperationSucceed, lastOpertionMessage: ModelState.lastSavingOperationMessage};
    }
);

