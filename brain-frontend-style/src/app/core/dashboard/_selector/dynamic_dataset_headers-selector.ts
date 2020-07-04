// NGRX
import { createFeatureSelector, createSelector } from '@ngrx/store';
// Lodash
import { each } from 'lodash';
// CRUD
import { QueryResultsModel, HttpExtenstionsModel } from '../../_base/crud';
// State
import { DynamicHeaderState } from '../_reducers/dynamic_dataset_headers-reducer';
import { DynamicHeader } from '../_model/dynamic_dataset_headers-model';

export const selectDynamicHeaderState = createFeatureSelector<DynamicHeaderState>('dataset_column_headers');

export const selectDynamicHeaderName = (table_name: string) => createSelector(
    selectDynamicHeaderState,
    ModelState => {
                        const items: DynamicHeader[] = [];
                        each(ModelState.entities, element => {
                            if (element["table_name"]===table_name&&element["is_visible"])
                            {
                                items.push(element);
                            }
                        });
                        const httpExtension = new HttpExtenstionsModel();
                        const result: DynamicHeader[] = items;
                        return new QueryResultsModel(result, ModelState.totalCount, '');
                    });

export const selectDynamicHeaderPageLoading = createSelector(
    selectDynamicHeaderState,
    ModelState => ModelState.columnsLoading
);

export const selectDynamicHeaderActionLoading = createSelector(
    selectDynamicHeaderState,
    ModelState => ModelState.columnsLoading
);

export const selectDynamicHeaderInitWaitingMessage = createSelector(
    selectDynamicHeaderState,
    ModelState => ModelState.showInitWaitingMessage
);

export const selectDynamicHeaderintheList = createSelector(
    selectDynamicHeaderState,
    ModelState => {
        const items: DynamicHeader[] = [];
        each(ModelState.entities, element => {
            items.push(element);
        });
        const httpExtension = new HttpExtenstionsModel();
        const result: DynamicHeader[] = items;
        return new QueryResultsModel(result, ModelState.totalCount, '');

    }
);
