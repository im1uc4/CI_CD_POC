//RXJS
import { of, forkJoin } from 'rxjs';
// Angular
import { Injectable } from '@angular/core';
// RxJS
import { mergeMap, map, tap } from 'rxjs/operators';
// NGRX
import { Effect, Actions, ofType } from '@ngrx/effects';
import { Store, Action } from '@ngrx/store';
// CRUD
import { LayoutUtilsService, QueryResultsModel, QueryParamsModel, MessageType } from '../../_base/crud';
// Services
import { DataSetListService } from '../service/dataset-list.service';
// State
import { AppState } from '../../reducers';
// Actions
import { DataSetsListActionTypes,
         DataSetListRequested,
         DataSetListLoaded,
         DataSetListActionToggleLoading,
         DataSetListPageToggleLoading  
} from '../_actions/dataset-list-action';

import {  
    Logout 
} from '../../auth/_actions/auth.actions';

import { DataSetList } from '../_model/dataset-list-model';

@Injectable()
export class DataSetListEffects {
    showPageLoadingDistpatcher = new DataSetListActionToggleLoading({ isLoading: true });
    hidePageLoadingDistpatcher = new DataSetListActionToggleLoading({ isLoading: false });

    showActionLoadingDistpatcher = new DataSetListPageToggleLoading({ isLoading: true });
    hideActionLoadingDistpatcher = new DataSetListPageToggleLoading({ isLoading: false });
    layoutUtilsService: LayoutUtilsService;
  
    @Effect()
    loaddslistPage$ = this.actions$
        .pipe(
            ofType<DataSetListRequested>(DataSetsListActionTypes.DataSetListRequested),
            mergeMap(({ payload }) => {
                this.store.dispatch(this.showPageLoadingDistpatcher); 
                const requestToServer = this.dslistService.getDataSetList(payload.id, payload.page);
                const lastQuery = of(payload.page);   
                const lastSelectedDs = of(payload.selected);     
                return forkJoin(requestToServer, lastQuery, lastSelectedDs);
            }),
            map(response => { 
                const result: QueryResultsModel = response[0];
                const lastQuery: QueryParamsModel = response[1];
                const selected: number = response[2]; 
                
                if (result["error"]){
                    if(!result["error"]["auth"]&&result["error"]["message"]){
                        this.layoutUtilsService.showActionNotification(result["error"]["message"], MessageType.Create, 5000, true, false); 
                        this.store.dispatch(new Logout());
                    }
                }

                return new DataSetListLoaded({
                    dslist: result.items,
                    totalCount: result.totalCount,
                    page: lastQuery,
                    selected: selected
                });
            }),
        );


    constructor(private actions$: Actions, private dslistService: DataSetListService, private store: Store<AppState>) { }
}
