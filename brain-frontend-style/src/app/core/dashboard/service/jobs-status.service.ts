//Angular
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
// RxJS
import { Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
// CRUD
import { HttpUtilsService, QueryParamsModel, QueryResultsModel } from '../../_base/crud';

import { environment } from '../../../../environments/environment';
// Modelss
import { JobsStatusModel } from '../_model/jobs-status-model';

const API_Jobs_Status_URL = '/api/v1/get_jobs_status';

@Injectable()
export class JobsStatusService {

  constructor(private http: HttpClient,
    private httpUtils: HttpUtilsService) {}

    // READ
    getJobsStatus(jobId: string): Observable<JobsStatusModel> {
      //const userToken = localStorage.getItem(environment.authTokenKey);
      //let httpHeaders = new HttpHeaders({'Authorization':userToken,"model_id": model_id.toString(),'Content-Type':'application/json'});    
      const httpHeaders = this.httpUtils.getHTTPHeaders("jobId",jobId.toString());
      return this.http.get<JobsStatusModel>(environment.apiURL+API_Jobs_Status_URL, { headers: httpHeaders });
    }
}
