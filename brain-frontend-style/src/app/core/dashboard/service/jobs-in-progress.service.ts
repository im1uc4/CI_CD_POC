//Angular
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
// RxJS
import { Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { catchError, map } from 'rxjs/operators';
// CRUD
import { HttpUtilsService, QueryParamsModel, QueryResultsModel } from '../../_base/crud';

import { environment } from '../../../../environments/environment';
// Modelss

const API_Submit_URL          = '/api/submit-job';
const API_Query_URL           = '/api/query-job';
const API_Stop_URL            = '/api/stop-job';              

@Injectable()
export class JobsInProgressService {

  constructor(private http: HttpClient,
    private httpUtils: HttpUtilsService) {}

   queryJob(jobId: string): Observable<any> {
     const httpHeaders = this.httpUtils.getHTTPHeaders(null,null);
      return this.http.post<any>(environment.engineAPI+API_Query_URL ,{"jobId": jobId, "includeJobSettings": true, "includeOptimisationModel": true,  "includeBestSolution": true },  { headers: httpHeaders })
                                  .pipe(
                                    map(res => {
                                        return {type:"success", message: res};
                                    }),
                                    catchError(err => {
                                            if(err.error.hasOwnProperty("auth")){
                                               return of({type:"error", message: err.error, auth:err.error["auth"]});
                                            }
                                            else{
                                               return of({type:"error", message: err.error});
                                            }
                                    
                                    })
                                );
    }

    stopJob(jobId: string): Observable<any> {
        const httpHeaders = this.httpUtils.getHTTPHeaders(null,null);
         return this.http.post<any>(environment.engineAPI+API_Stop_URL ,{"jobId": jobId},  { headers: httpHeaders })
                                     .pipe(
                                       map(res => {
                                           return {type:"success", message: res};
                                       }),
                                       catchError(err => {
                                        if(err.error.hasOwnProperty("auth")){
                                            return of({type:"error", message: err.error, auth:err.error["auth"]});
                                         }
                                         else{
                                            return of({type:"error", message: err.error});
                                         }
                                       
                                       })
                                   );
       }


    // Submit
    SubmitJob(sbtMsg: string): Observable<any> {
     const httpHeaders = this.httpUtils.getHTTPHeaders(null,null);
      return this.http.post<any>(environment.engineAPI+API_Submit_URL,sbtMsg, { headers: httpHeaders } )
                                  .pipe(
                                    map(res => {
                                        return {type:"success", message: res};
                                    }),
                                    catchError(err => {
                                        if(err.error.hasOwnProperty("auth")){
                                            return of({type:"error", message: err.error, auth:err.error["auth"]});
                                         }
                                         else{
                                            return of({type:"error", message: err.error});
                                         }
                                    })
                                );
    }

}
