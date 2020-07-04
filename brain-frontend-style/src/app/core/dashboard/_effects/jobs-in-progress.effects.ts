//RXJS
import { of, forkJoin } from 'rxjs';
// Angular
import { Injectable } from '@angular/core';
// RxJS
import { mergeMap, map, tap } from 'rxjs/operators';
// NGRX
import { Effect, Actions, ofType } from '@ngrx/effects';
import { Store, Action } from '@ngrx/store';
import { Update } from '@ngrx/entity';
// CRUD
import { QueryResultsModel, QueryParamsModel } from '../../_base/crud';
// Services
import { JobsInProgressService } from '../service/jobs-in-progress.service';
// State
import { AppState } from '../../reducers';
// Actions
import { JobsCreated, JobsUpdated, JobsReset, JobsStop,  JobsInProgressActionTypes, JobsOperationResult } from '../_actions/job-in-progress.action';

import { JobInProgressModel } from '../_model/job-in-progress.model';

@Injectable()
export class JobInProgressEffects {

    @Effect()
    submitJobInProgress$ = this.actions$
        .pipe(
            ofType<JobsCreated>(JobsInProgressActionTypes.JobsCreated),
            mergeMap(({ payload }) => {
                return this.jobInProgressService.SubmitJob(payload.SubmitBody).pipe(
                    tap(res => {
                        if(res["type"]==="success"){
                            let jipModel:JobInProgressModel =new JobInProgressModel()
                            jipModel.jobId=(res.message.job?res.message.job["jobId"]:"")
                            jipModel.jobStatus=(res.message?res.message["jobStatusCode"]:null);

                            const updateJIP: Update<JobInProgressModel> = {
                                id: 1 ,
                                changes: jipModel
                            };
                            this.store.dispatch(new JobsOperationResult({is_success: true, message: res.message}));
                            return this.store.dispatch(new JobsUpdated({ partialJob: updateJIP,jobInProgressModel: jipModel  }));
                        }
                        if(res["type"]==="error"){
                            return this.store.dispatch(new JobsOperationResult({is_success: false,message:res.message}));                            
                        }
                        }))
                    }),            
        );

            @Effect()
            stopJobInProgress$ = this.actions$
                .pipe(
                    ofType<JobsStop>(JobsInProgressActionTypes.JobsStop),
                    mergeMap(({ payload }) => {
                        return this.jobInProgressService.SubmitJob(payload.jobId).pipe(
                            tap(res => {
                               console.log("1")
                               return this.store.dispatch(new JobsReset())
                                }))
                            }),            
                );

    constructor(private actions$: Actions, private jobInProgressService: JobsInProgressService, private store: Store<AppState>) { }
}
