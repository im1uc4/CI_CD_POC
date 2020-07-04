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
import { DataSetDataService } from '../service/dataset_data.service';
// State
import { AppState } from '../../reducers';
// Actions
import { DataSetDataActionTypes,
         DataSetDataRequested,
         DataSetDataLoaded,
         DataSetDataActionToggleLoading,
         DataSetDataPageToggleLoading,
         DataSetDataOnServerCreated,
         DataSetDataOperationResult
} from '../_actions/dataset_data-actions';

import {  
    Logout 
} from '../../auth/_actions/auth.actions';

@Injectable()
export class DataSetDataEffects {
    showPageLoadingDistpatcher = new DataSetDataActionToggleLoading({ isLoading: true });
    hidePageLoadingDistpatcher = new DataSetDataActionToggleLoading({ isLoading: false });

    showActionLoadingDistpatcher = new DataSetDataPageToggleLoading({ isLoading: true });
    hideActionLoadingDistpatcher = new DataSetDataPageToggleLoading({ isLoading: false });
  
    @Effect()
    load_dataSetDataPage$ = this.actions$
        .pipe(
            ofType<DataSetDataRequested>(DataSetDataActionTypes.DataSetDataRequested),
            mergeMap(({ payload }) => {
                this.store.dispatch(this.showPageLoadingDistpatcher); 
                const requestToServer = this.dataSetDataService.getdataSetDataList(payload.dataset_id);
                const lastQuery = of(null);        
                return forkJoin(requestToServer, lastQuery);
            }),
            map(response => { 
                const result: QueryResultsModel = response[0];
                const lastQuery: QueryParamsModel = response[1];
                this.store.dispatch(this.hidePageLoadingDistpatcher);
                return new DataSetDataLoaded({
                    dataset_data: result.items,
                    totalCount: result.totalCount,
                    page: lastQuery
                });
            }),
        );
    
        @Effect()
        SaveDataSet$ = this.actions$
            .pipe(
                ofType<DataSetDataOnServerCreated>(DataSetDataActionTypes.DataSetDataOnServerCreated),
                mergeMap(( { payload } ) => {
                    this.store.dispatch(this.showActionLoadingDistpatcher);
                    return this.dataSetDataService.saveDataSet(payload.model_id, payload.dataset_data, payload.isMaster,payload.dataset_id).pipe(
                        tap(res => {  
                            let res_obj=(res.message.message?res.message.message:undefined)                           
                            let success:boolean =true;
                            let saved_ds_id:number=0; 
                            let gen_job_id:string=""
                            let msg:string="";
                            if(res.type==="error"){
                                success=false
                                msg=res["message"]                               
                              } 
                            else{
                                saved_ds_id=(res_obj?res_obj["createdDs_id"]:0) 
                                msg=(res_obj?res_obj["job"]:"API callback format is wrong")
                                success=(res_obj?success:false) 
                                gen_job_id=(res_obj.job?res_obj.job["jobId"]:"")                     
                            } 
                            this.store.dispatch(new DataSetDataOperationResult({is_success: success, saved_ds_id: saved_ds_id, jobId: gen_job_id, message: msg}));
                            if(res["auth"]===false){
                                this.store.dispatch(new Logout());
                            }
                        }));
                }),
                map(() => {
                    return this.hideActionLoadingDistpatcher;
                })
            );    
       
    constructor(private actions$: Actions, private dataSetDataService: DataSetDataService, private store: Store<AppState>) { }
}
