//Angular
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
// RxJS
import { Observable, of } from 'rxjs';
import { mergeMap, catchError } from 'rxjs/operators';
// CRUD
import { HttpUtilsService, QueryParamsModel, QueryResultsModel } from '../../_base/crud';

import { environment } from '../../../../environments/environment';
// Modelss
import { DataSetList } from '../_model/dataset-list-model';


const API_Constraints_URL = '/api/v1/getdsformodel';

@Injectable()
export class DataSetListService {

  constructor(private http: HttpClient,
    private httpUtils: HttpUtilsService) {}

    getDataSetList(ModelId: number, queryParams: QueryParamsModel): Observable<QueryResultsModel> {      
      return this.getDataSets(ModelId).pipe(mergeMap(res => {
        const result = new QueryResultsModel(res["ds_lists"],res["ds_lists"].length)
        return of(result);
      }),catchError((err, caught) => {             
            return of(err);           
          })      
      ); 
    }

    // READ
    getDataSets(model_id: number): Observable<DataSetList[]> {
      const httpHeaders = this.httpUtils.getHTTPHeaders("model_id",model_id.toString());
      //const userToken = localStorage.getItem(environment.authTokenKey);
      //let httpHeaders = new HttpHeaders({'Authorization':userToken,"model_id": model_id.toString(),'Content-Type':'application/json'});  
      return this.http.get<DataSetList[]>(environment.apiURL+API_Constraints_URL, { headers: httpHeaders });
    }
}