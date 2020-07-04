// NGRX
import { createFeatureSelector, createSelector } from '@ngrx/store';
// Lodash
import { each } from 'lodash';
// CRUD
import { QueryResultsModel, HttpExtenstionsModel } from '../../_base/crud';
// State
import { OptModelState } from '../_reducers/dashboard.reducer';
import { OptModel } from '../_model/opt-model';

export const selectOptModelsState = createFeatureSelector<OptModelState>('models');

export const selectModelById = (ModelId: number) => createSelector(
    selectOptModelsState,
    ModelState => ModelState.entities[ModelId]
);

export const selectOptModelsPageLoading = createSelector(
    selectOptModelsState,
    ModelState => ModelState.modelLoading
);

//export const selectOptModelPageLastQuery = createSelector(
 //   selectOptModelsState,
 //   ModelState => ModelState.lastQuery
//);

export const selectOptModelsInitWaitingMessage = createSelector(
    selectOptModelsState,
    ModelState => ModelState.showInitWaitingMessage
);

export const selectLastSelectedModelId = createSelector(
    selectOptModelsState,
    ModelState => ModelState.lastSelectedModelId
);

export const selectModelsonDash = createSelector(
    selectOptModelsState,
    ModelState => {
        const items: OptModel[] = [];
        each(ModelState.entities, element => {
            items.push(element);
        });
        const httpExtension = new HttpExtenstionsModel();
        const result: OptModel[] = items;
        return new QueryResultsModel(result, ModelState.totalCount, '');
    }
);
