//Angular
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
// RxJS
import { Observable, of } from 'rxjs';
import { mergeMap,catchError } from 'rxjs/operators';
// CRUD
import { HttpUtilsService, QueryParamsModel, QueryResultsModel } from '../../_base/crud';

import { environment } from '../../../../environments/environment';
// Modelss
import { JobsList } from '../_model/jobs-list-model';

const API_Constraints_URL = '/api/v1/getjobsformodel';

@Injectable()
export class JobsListService {

  constructor(private http: HttpClient,
    private httpUtils: HttpUtilsService) {}

    getJobsList(ModelId: number, queryParams: QueryParamsModel): Observable<QueryResultsModel> {      
      return this.getJobs(ModelId).pipe(mergeMap(res => {
        const result = this.httpUtils.baseFilter(res["jobs"], queryParams, []);
        return of(result);
      }),catchError((err, caught) => {             
          return of(err);           
        })); 
    }

    // READ
    getJobs(model_id: number): Observable<JobsList[]> {
      //const userToken = localStorage.getItem(environment.authTokenKey);
      //let httpHeaders = new HttpHeaders({'Authorization':userToken,"model_id": model_id.toString(),'Content-Type':'application/json'});    
      const httpHeaders = this.httpUtils.getHTTPHeaders("model_id",model_id.toString());
      return this.http.get<JobsList[]>(environment.apiURL+API_Constraints_URL, { headers: httpHeaders });
    }
}
