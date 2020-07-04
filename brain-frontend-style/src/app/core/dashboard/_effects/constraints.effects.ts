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
import { OptModelConstraintsService } from '../service/opt-model-constraints.service';
// State
import { AppState } from '../../../core/reducers';
// Actions
import { OptModelConstraintsActionTypes,
         OptModelConstraintsListRequested,
         OptModelConstraintsListLoaded  
} from '../_actions/opt-model-constraints-actions';

import { OptModelConstraints } from '../_model/opt-model-constraints';

@Injectable()
export class ConstraintsEffects {

    @Effect()
    loadConstraintsPage$ = this.actions$
        .pipe(
            ofType<OptModelConstraintsListRequested>(OptModelConstraintsActionTypes.OptModelConstraintsListRequested),
            mergeMap(({ payload }) => {              
                const requestToServer = this.consraintService.getConstraintList(payload.id);
                const lastQuery = of(null);        
                return forkJoin(requestToServer, lastQuery);
            }),
            map(response => { 
                const result: QueryResultsModel = response[0];
                const lastQuery: QueryParamsModel = response[1];
                return new OptModelConstraintsListLoaded({
                    constraints: result.items,
                    totalCount: result.totalCount
                });
            }),
        );


    constructor(private actions$: Actions, private consraintService: OptModelConstraintsService, private store: Store<AppState>) { }
}
