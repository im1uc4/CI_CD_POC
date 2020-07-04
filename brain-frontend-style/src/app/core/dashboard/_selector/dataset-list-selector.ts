// NGRX
import { createFeatureSelector, createSelector } from '@ngrx/store';
// Lodash
import { each } from 'lodash';
// CRUD
import { QueryResultsModel, HttpExtenstionsModel } from '../../_base/crud';
// State
import { DataSetListState } from '../_reducers/dataset-list.reducer';
import { DataSetList } from '../_model/dataset-list-model';

export const selectDatasetListState = createFeatureSelector<DataSetListState>('dataset');

export const selectDatasetListName = (table_name: string) => createSelector(
    selectDatasetListState,
    ModelState => {
                        const items: DataSetList[] = [];
                        each(ModelState.entities, element => {
                            if (element["table_name"]===table_name)
                            {
                                items.push(element);
                            }
                        });
                        const httpExtension = new HttpExtenstionsModel();
                        const result: DataSetList[] = items;
                        return new QueryResultsModel(result, ModelState.totalCount, '');
                    });

export const selectDSListPageLoading = createSelector(
    selectDatasetListState,
    ModelState => ModelState.datasetsLoading
);

export const selectDSListActionLoading = createSelector(
    selectDatasetListState,
    usersState => usersState.datasetsLoading
);

export const selectDSListInitWaitingMessage = createSelector(
    selectDatasetListState,
    ModelState => ModelState.showInitWaitingMessage
);

/*export const selectlastSelectedDataset = createSelector(
    selectDatasetListState,
    ModelState => ModelState.lastSelectedDataset
);*/

export const selectlastSelectedDataset = createSelector(
    selectDatasetListState,
    ModelState => { return { lastQueryWhenDsSelected: ModelState.lastQueryWhenDsSelected, lastSelectedDataset: ModelState.lastSelectedDataset }}
);

export const selectDatasetById = (dataset_id: number) => createSelector(
    selectDatasetListState,
    ModelState =>  ModelState.entities[dataset_id]
);


export const selectDSintheList = createSelector(
    selectDatasetListState,
    ModelState => {
        const items: DataSetList[] = [];
        each(ModelState.entities, element => {
            if (element["isMasterdataset"]){
                items.push(element);
            }
        });
        const httpExtension = new HttpExtenstionsModel();
        const result: DataSetList[] = items;
        return new QueryResultsModel(result, ModelState.totalCount, '');
    }
);
