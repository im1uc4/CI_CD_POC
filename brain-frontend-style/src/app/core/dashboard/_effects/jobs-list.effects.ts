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
import { JobsListService } from '../service/jobs-list.service';
// State
import { AppState } from '../../../core/reducers';
// Actions
import { JobsListActionTypes,
         JobsListRequested,
         JobsListLoaded,
         JobsListActionToggleLoading,
         JobsListPageToggleLoading  
} from '../_actions/jobs-list-actions';

import {  
    Logout 
} from '../../auth/_actions/auth.actions';

import { JobsList } from '../_model/jobs-list-model';

@Injectable()
export class JobsListEffects {
    showPageLoadingDistpatcher = new JobsListActionToggleLoading({ isLoading: true });
    hidePageLoadingDistpatcher = new JobsListActionToggleLoading({ isLoading: false });

    showActionLoadingDistpatcher = new JobsListPageToggleLoading({ isLoading: true });
    hideActionLoadingDistpatcher = new JobsListPageToggleLoading({ isLoading: false });

    layoutUtilsService: LayoutUtilsService;
  
    @Effect()
    loadjobslistPage$ = this.actions$
        .pipe(
            ofType<JobsListRequested>(JobsListActionTypes.JobsListRequested),
            mergeMap(({ payload }) => {
                this.store.dispatch(this.showPageLoadingDistpatcher); 
                const requestToServer = this.joblistService.getJobsList(payload.id, payload.page);
                const lastQuery = of(payload.page);        
                return forkJoin(requestToServer, lastQuery);
            }),
            map(response => { 
                const result: QueryResultsModel = response[0];
                const lastQuery: QueryParamsModel = response[1];

                if(result["error"]){
                    this.layoutUtilsService.showActionNotification(result["error"]["message"], MessageType.Create, 5000, true, false); 
                    this.store.dispatch(new Logout());
                }

                return new JobsListLoaded({
                    jobslist: result.items,
                    totalCount: result.totalCount,
                    page: lastQuery
                });
            }),
        );


    constructor(private actions$: Actions, private joblistService: JobsListService, private store: Store<AppState>) { }
}
