//Angular
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
// RxJS
import { Observable, of } from 'rxjs';
import { mergeMap, catchError } from 'rxjs/operators';
// CRUD
import { HttpUtilsService, QueryParamsModel, QueryResultsModel } from '../../_base/crud';

import { environment } from '../../../../environments/environment';
// Models
import { OptModelConstraints } from '../_model/opt-model-constraints';

const API_Constraints_URL = '/api/v1/getconstraints';

@Injectable()
export class OptModelConstraintsService {

  constructor(private http: HttpClient,
    private httpUtils: HttpUtilsService) {}

    getConstraintList(ModelId: number): Observable<QueryResultsModel> {
      const queryParams= new QueryParamsModel({})
      return this.getAllConstraints(ModelId).pipe(mergeMap(res => {
        const result = this.httpUtils.baseFilter(res["constraints"], queryParams, []);
        return of(result);
      }),catchError((err, caught) => {             
        return of(err);           
      })); 
    }

    // READ
    getAllConstraints(model_id: number): Observable<OptModelConstraints[]> {
      //const userToken = localStorage.getItem(environment.authTokenKey);
      //let httpHeaders = new HttpHeaders({'Authorization':userToken,"model_id": model_id.toString(),'Content-Type':'application/json'});  
      const httpHeaders = this.httpUtils.getHTTPHeaders("model_id",model_id.toString());
      return this.http.get<OptModelConstraints[]>(environment.apiURL+API_Constraints_URL, { headers: httpHeaders });
    }
}
