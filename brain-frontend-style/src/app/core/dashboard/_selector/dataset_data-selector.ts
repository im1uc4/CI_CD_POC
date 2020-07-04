// NGRX
import { createFeatureSelector, createSelector } from '@ngrx/store';
// RXJS
import {tap,map } from 'rxjs/operators';
// Lodash
import { each } from 'lodash';
// CRUD
import { QueryResultsModel, HttpExtenstionsModel } from '../../_base/crud';
// State
import { DataSetDataState } from '../_reducers/dataset_data-reducer';
import { DataSetDataModel } from '../_model/dataset_data-model';

export const selectDataSetState = createFeatureSelector<DataSetDataState>('dataset_data');

export const selectDataSetDataName = (table_name: string) => createSelector(
    selectDataSetState,
    ModelState => {
                        const items: DataSetDataModel[] = [];
                        const allItems: DataSetDataModel[] = [];
                        each(ModelState.entities, element => {

                            if(element){                            
                                if (element["table_name"]===table_name)
                                {
                                    items.push(element);
                                }
                                allItems.push(element);                        
                            }
                        });
                        const httpExtension = new HttpExtenstionsModel();
                        const result: DataSetDataModel[] = items;
                        const maxId=Math.max.apply(Math, allItems.map(function(o) { return o.id; }))
                        const recordId=Math.max.apply(Math, allItems.map(function(o) { return o.record_id; }))
                        const dataModified=ModelState.dataModified;
                        const lastTableAffectedByOper=ModelState.lastTableAffectedByOper
                        return new QueryResultsModel(result, maxId, recordId, dataModified, '', lastTableAffectedByOper);
                    });

export const selectDataSetLinkedDataforID = (table_name: string, keyColumn:string, keyValue:string) => createSelector(
    selectDataSetState,
    ModelState => {
                        const items: DataSetDataModel[] = [];
                        const allItems: DataSetDataModel[] = [];
                        each(ModelState.entities, element => {
                            if (element["table_name"]===table_name&&element["value"]===keyValue&&element["field_name"]===keyColumn)
                            {
                                each(ModelState.entities, res_elem => {
                                    if (res_elem["table_name"]===table_name&&res_elem["record_id"]===element["record_id"]){
                                        items.push(res_elem);
                                    }
                                })
                            }
                                allItems.push(element);
                        });
                        const httpExtension = new HttpExtenstionsModel();
                        const result: DataSetDataModel[] = items;
                        const maxId=Math.max.apply(Math, allItems.map(function(o) { return o.id; }))
                        const recordId=Math.max.apply(Math, allItems.map(function(o) { return o.record_id; }))
                        const dataModified=ModelState.dataModified;
                        const lastTableAffectedByOper=ModelState.lastTableAffectedByOper
                        return new QueryResultsModel(result,  maxId, recordId, dataModified, '',lastTableAffectedByOper);
                    });

export const selectDataSetDataPageLoading = createSelector(
    selectDataSetState,
    ModelState => ModelState.columnsLoading
);

export const selectDataSetDataActionLoading = createSelector(
    selectDataSetState,
    ModelState => ModelState.columnsLoading
);

export const selectLastSelectedDatasetId = createSelector(
    selectDataSetState,
    ModelState => ModelState.lastSelectedDatasetId
);

export const selectDataSetDataInitWaitingMessage = createSelector(
    selectDataSetState,
    ModelState => ModelState.showInitWaitingMessage
);

export const selectDataSetDataModifiedFlag = createSelector(
    selectDataSetState,
    ModelState => ModelState.dataModified
);

export const selectDataSetDataintheList = createSelector(
    selectDataSetState,
    ModelState => {
        const items: DataSetDataModel[] = [];
        each(ModelState.entities, element => {
            items.push(element);
        });
        const httpExtension = new HttpExtenstionsModel();
        const result: DataSetDataModel[] = items;
        const lastTableAffectedByOper=ModelState.lastTableAffectedByOper
        return new QueryResultsModel(result, ModelState.totalCount, '',lastTableAffectedByOper);

    }
);

export const selectDataSetDataById = (datasetId: number) => createSelector(
    selectDataSetState,
    ModelState => {
        const items: DataSetDataModel[] = [];
        each(ModelState.entities, element => {
            if (element["dataset_id"] === datasetId)
            {
                items.push(element);
            }
        });
        const httpExtension = new HttpExtenstionsModel();
        const result: DataSetDataModel[] = items;
        return new QueryResultsModel(result, ModelState.totalCount, '');

    }
);

export const selectLastDsSavingStatus =  createSelector(
    selectDataSetState,
    ModelState => {
        return { message:ModelState.lastSavingOperationMessage, succeed:ModelState.isLastSavingOperationSucceed, dataset_id:ModelState.lastSavedDatasetId, jobId:ModelState.lastGeneratedJobId}
    }
);
