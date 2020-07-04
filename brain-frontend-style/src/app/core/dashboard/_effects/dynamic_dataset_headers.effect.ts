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
import { QueryResultsModel, QueryParamsModel } from '../../_base/crud';
// Services
import { DynamicHeaderService } from '../service/dynamic_dataset_headers.service';
// State
import { AppState } from '../../reducers';
// Actions
import { DynamicHeaderActionTypes,
         DynamicHeaderRequested,
         DynamicHeaderLoaded,
         DynamicHeaderActionToggleLoading,
         DynamicHeaderPageToggleLoading  
} from '../_actions/dynamic_dataset_headers-actions';

import { DynamicHeader } from '../_model/dynamic_dataset_headers-model';

@Injectable()
export class DynamicHeaderEffects {
    showPageLoadingDistpatcher = new DynamicHeaderActionToggleLoading({ isLoading: true });
    hidePageLoadingDistpatcher = new DynamicHeaderActionToggleLoading({ isLoading: false });

    showActionLoadingDistpatcher = new DynamicHeaderPageToggleLoading({ isLoading: true });
    hideActionLoadingDistpatcher = new DynamicHeaderPageToggleLoading({ isLoading: false });
  
    @Effect()
    load_dynamicHeadlistPage$ = this.actions$
        .pipe(
            ofType<DynamicHeaderRequested>(DynamicHeaderActionTypes.DynamicHeaderRequested),
            mergeMap(({ payload }) => {
                this.store.dispatch(this.showPageLoadingDistpatcher); 
                const requestToServer = this.dynheadlistService.getHeadersList(payload.dataset_id);
                const lastQuery = of(null);        
                return forkJoin(requestToServer, lastQuery);
            }),
            map(response => { 
                const result: QueryResultsModel = response[0];
                const lastQuery: QueryParamsModel = response[1];
                return new DynamicHeaderLoaded({
                    headers: result.items,
                    totalCount: result.totalCount,
                    page: lastQuery
                });
            }),
        );


    constructor(private actions$: Actions, private dynheadlistService: DynamicHeaderService, private store: Store<AppState>) { }
}
