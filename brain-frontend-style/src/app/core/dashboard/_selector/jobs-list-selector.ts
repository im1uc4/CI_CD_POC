// NGRX
import { createFeatureSelector, createSelector } from '@ngrx/store';
// Lodash
import { each } from 'lodash';
// CRUD
import { QueryResultsModel, HttpExtenstionsModel } from '../../_base/crud';
// State
import { JobsListState } from '../_reducers/jobs-list.reducer';
import { JobsList } from '../_model/jobs-list-model';

export const selectJobsListState = createFeatureSelector<JobsListState>('jobslist');

export const selectJobsListId = (jobsId: number) => createSelector(
    selectJobsListState,
    ModelState => ModelState.entities[jobsId]
);

export const selectJobsListPageLoading = createSelector(
    selectJobsListState,
    ModelState => ModelState.jobsLoading
);

export const selectJobsListActionLoading = createSelector(
    selectJobsListState,
    ModelState => ModelState.jobsLoading
);

export const selectJobsListInitWaitingMessage = createSelector(
    selectJobsListState,
    ModelState => ModelState.showInitWaitingMessage
);


export const selectLastSelectedJobFromTheList = createSelector(
    selectJobsListState,
    ModelState => ModelState.lastSelectedJobId
);

export const selectJobsintheList = createSelector(
    selectJobsListState,
    ModelState => {
        const items: JobsList[] = [];
        each(ModelState.entities, element => {
            items.push(element);
        });
        const httpExtension = new HttpExtenstionsModel();
        const result: JobsList[] = httpExtension.sortArray(items, ModelState.lastQuery.sortField, ModelState.lastQuery.sortOrder);
        //const result: JobsList[] = items;
        return new QueryResultsModel(result, ModelState.totalCount, '');
    }
);
