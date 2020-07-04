//Angular
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
// RxJS
import { Observable, of } from 'rxjs';
import { mergeMap,catchError } from 'rxjs/operators';
// CRUD
import { HttpUtilsService, QueryParamsModel, QueryResultsModel } from '../../_base/crud';

import { environment } from '../../../../environments/environment';
// Models
import { DynamicHeader } from '../_model/dynamic_dataset_headers-model';

const API_Constraints_URL = '/api/v1/get_ds_skeleton_columns_by_ds_id';

@Injectable()
export class DynamicHeaderService {

  constructor(private http: HttpClient,
    private httpUtils: HttpUtilsService) {}

    getHeadersList(dataset_Id: number): Observable<QueryResultsModel> {
      const queryParams= new QueryParamsModel({})
      return this.getHeadersForModel(dataset_Id).pipe(mergeMap(res => {
        const result = this.httpUtils.baseFilter(res["ds_skl"], queryParams, []);
        return of(result);
      }),catchError((err, caught) => {             
        return of(err);           
      })
      ); 
    }

    // READ
    getHeadersForModel(dataset_id: number): Observable<DynamicHeader[]> {
      //const userToken = localStorage.getItem(environment.authTokenKey);
      //let httpHeaders = new HttpHeaders({'Authorization':userToken,"dataset_id": dataset_id.toString(),'Content-Type':'application/json'});    
      const httpHeaders = this.httpUtils.getHTTPHeaders("dataset_id", dataset_id.toString());
      return this.http.get<DynamicHeader[]>(environment.apiURL+API_Constraints_URL, { headers: httpHeaders });
    }
}
