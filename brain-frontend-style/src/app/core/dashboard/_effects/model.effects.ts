import { forkJoin } from 'rxjs';
// Angular
import { Injectable } from '@angular/core';
// RxJS
import { mergeMap, map, tap } from 'rxjs/operators';
// NGRX
import { Effect, Actions, ofType } from '@ngrx/effects';
import { Store, Action } from '@ngrx/store';
// CRUD
import { LayoutUtilsService, QueryResultsModel, QueryParamsModel, MessageType  } from '../../_base/crud';
// Services
import { BrainDashboard_Service } from '../service/brain-dashboard.service';
// State
import { AppState } from '../../../core/reducers';
// Actions
import {
    OptModelActionTypes,
    OptModuleOnServerCreated,
    OptModulesStatusUpdated,
    OptModulesPageRequested,
    OptModulesPageLoaded,
    OptModulesPageCancelled
} from '../_actions/opt-model-actions';

import {  
    Logout 
} from '../../auth/_actions/auth.actions';

import { defer, Observable, of } from 'rxjs';

@Injectable()
export class OptModelsEffects {
    layoutUtilsService: LayoutUtilsService;

    @Effect()
    loadOptModelsPage$ = this.actions$
        .pipe(
            ofType<OptModulesPageRequested>(OptModelActionTypes.OptModulesPageRequested),
            mergeMap(() => {
                //this.store.dispatch(this.showPageLoadingDistpatcher);
                const requestToServer = this.OptModelService.getDashboard();
                //Need to think how to remove this query
                const lastQuery = of(null);           
                return forkJoin(requestToServer, lastQuery);
            }),
            map(response => { 
                const result: QueryResultsModel = response[0];
                const lastQuery: QueryParamsModel = response[1];

                if(result["error"]){
                    this.layoutUtilsService.showActionNotification(result["error"]["message"], MessageType.Create, 5000, true, false); 
                    this.store.dispatch(new Logout());
                }

                return new OptModulesPageLoaded({
                    models: result.items,
                    totalCount: result.totalCount,
                    page: lastQuery
                });
            }),
        );

  

    // @Effect()
    // init$: Observable<Action> = defer(() => {
    //     const queryParams = new QueryParamsModel({});
    //     return of(new ProductsPageRequested({ page: queryParams }));
    // });

    constructor(private actions$: Actions, private OptModelService: BrainDashboard_Service, private store: Store<AppState>) { }
}
