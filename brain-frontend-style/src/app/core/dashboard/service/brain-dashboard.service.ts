//Angular
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
// RxJS
import { Observable, of } from 'rxjs';
import { mergeMap,catchError } from 'rxjs/operators';
// CRUD
import { HttpUtilsService, QueryParamsModel, QueryResultsModel } from '../../_base/crud';

import { environment } from '../../../../environments/environment';
// Models
import {OptModel} from '../_model/opt-model';

const API_DASHBRD_URL = '/api/v1/getmodels';

@Injectable()
export class BrainDashboard_Service {

  constructor(private http: HttpClient,
    private httpUtils: HttpUtilsService) { }

  getDashboard(): Observable<QueryResultsModel> {
    const queryParams= new QueryParamsModel({})
    return this.getAllModels().pipe(mergeMap(res => {
      const result = this.httpUtils.baseFilter(res, queryParams, []);
      return of(result);
    }),catchError((err, caught) => {             
                        return of(err);           
      })
    
    ); 
  }

  	// READ
	getAllModels(): Observable<OptModel[]> {
    const httpHeaders = this.httpUtils.getHTTPHeaders(null,null);
		return this.http.get<OptModel[]>(environment.apiURL+API_DASHBRD_URL, { headers: httpHeaders });
	}
}
