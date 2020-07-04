//Angular
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
// RxJS
import { Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
// CRUD
import { HttpUtilsService, QueryParamsModel, QueryResultsModel } from '../../_base/crud';

import { environment } from '../../../../environments/environment';
import { GanttChartData } from '../_model/gantt-chart-data-model';
import { GanttChartItemModel } from '../_model/gantt-chart-item-model';


const API_OptimizedData_URL = '/api/v1/get_optimized_data_for_model';
const API_Update_OptimizedData_URL = '/api/v1/update_optimized_data_for_model';
const API_Delete_OptimizedData_URL = '/api/v1/delete_optimized_data_for_model';

@Injectable()
export class GanttChartService {


  constructor(private http: HttpClient,
    private httpUtils: HttpUtilsService) { }

  // getOptimizedJobs(): Observable<GanttChartData[]> {
  //   const httpHeaders = this.httpUtils.getHTTPHeaders(null,null);
  //   return this.http.get<GanttChartData[]>(environment.apiURL+API_OptimizedData_URL, { headers: httpHeaders });
  // }


  //READ
  getOptimizedJobs(genJobId: string): Observable<GanttChartData[]> {
    const httpHeaders = this.httpUtils.getHTTPHeaders("jobId",genJobId.toString());
    return this.http.get<GanttChartData[]>(environment.apiURL + API_OptimizedData_URL, { headers: httpHeaders });
  }

  //Update
  updateOptimizedJobs(ganttItem: GanttChartItemModel): Observable<GanttChartData[]> {
    const userToken = 'Bearer ' + localStorage.getItem(environment.authTokenKey);
    //let httpHeaders = new HttpHeaders({ Authorization: userToken });
    const httpHeaders = this.httpUtils.getHTTPHeaders(null,null);
    return this.http.put<GanttChartData[]>(environment.apiURL + API_Update_OptimizedData_URL, ganttItem,
      { headers: httpHeaders });
  }
}