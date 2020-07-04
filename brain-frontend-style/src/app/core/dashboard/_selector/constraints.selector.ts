// NGRX
import { createFeatureSelector, createSelector } from '@ngrx/store';
// Lodash
import { each } from 'lodash';
// CRUD
import { QueryResultsModel, HttpExtenstionsModel } from '../../_base/crud';
// State
import { OptModelConstraintsState } from '../_reducers/constraints.reducer';
import { OptModelConstraints } from '../_model/opt-model-constraints';

export const selectOptModelConstraintsState = createFeatureSelector<OptModelConstraintsState>('constraints');

export const selectConstraintsById = (ConstraintId: number) => createSelector(
    selectOptModelConstraintsState,
    ModelState => ModelState.entities[ConstraintId]
);

export const selectOptModelConstraintsPageLoading = createSelector(
    selectOptModelConstraintsState,
    ModelState => ModelState.modelLoading
);

export const selectOptModelConstraintInitWaitingMessage = createSelector(
    selectOptModelConstraintsState,
    ModelState => ModelState.showInitWaitingMessage
);

export const selectConstraintsintheList = createSelector(
    selectOptModelConstraintsState,
    ModelState => {
        const items: OptModelConstraints[] = [];
        each(ModelState.entities, element => {
            items.push(element);
        });
        const httpExtension = new HttpExtenstionsModel();
        const result: OptModelConstraints[] = items;
        return new QueryResultsModel(result, ModelState.totalCount, '');
    }
);

