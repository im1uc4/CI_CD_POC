//Angular
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
// RxJS
import { Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { catchError, map } from 'rxjs/operators';
// CRUD
import { HttpUtilsService, QueryParamsModel, QueryResultsModel } from '../../_base/crud';

import { environment } from '../../../../environments/environment';
// Models
import { DataSetDataModel } from '../_model/dataset_data-model';

const API_Constraints_URL = '/api/v1/get_raw_ds_data_by_id';
const API_Get_Running_Num_URL = '/api/v1/get_running_no';
const API_SAVEDATASET_URL = '/api/v1/save_dataset';
const API_GENJOBID_URL= '/api/v1/create_job';

@Injectable()
export class DataSetDataService {

  constructor(private http: HttpClient,
    private httpUtils: HttpUtilsService) {}

    getdataSetDataList(dataset_Id: number): Observable<QueryResultsModel> {
      const queryParams= new QueryParamsModel({})
      return this.getDataSetData(dataset_Id).pipe(mergeMap(res => {
        //const interim_result=this.convert_API_dataSetData(res["dataset"]);
        const result = this.httpUtils.baseFilter(res["dataset"], queryParams, []);
        return of(result);
      })); 
    }

    // READ
    getDataSetData(dataset_id: number): Observable<DataSetDataModel[]> {
      const httpHeaders = this.httpUtils.getHTTPHeaders("dataset_id",dataset_id.toString());
      //const userToken = localStorage.getItem(environment.authTokenKey);
      //let httpHeaders = new HttpHeaders({'Authorization':userToken,"dataset_id": dataset_id.toString(),'Content-Type':'application/json'});    
      return this.http.get<DataSetDataModel[]>(environment.apiURL+API_Constraints_URL, { headers: httpHeaders });
    }

    // Retrieve running number for record
    getNextRunningNumber(counterName:string): Observable<any> {    
      const httpHeaders = this.httpUtils.getHTTPHeaders("counter_name",counterName); 
      return this.http.get<any>(environment.apiURL+API_Get_Running_Num_URL, { headers: httpHeaders });
    }

    saveDataSet(model_id: number,dataset_data: DataSetDataModel, isMaster: boolean,dataset_id: number): Observable<any> {
      const httpHeaders = this.httpUtils.getHTTPHeaders(null,null);
      return this.http.post<any>(environment.apiURL+API_SAVEDATASET_URL,{model_id:model_id, dataset: dataset_data, Master: isMaster,dataset_id:dataset_id},{ headers: httpHeaders })
                                  .pipe(
                                    map(res => {
                                        return {type:"success", message: res};
                                    }),
                                    catchError(err => {
                                        if (err.error.hasOwnProperty("auth")){
                                            return of({type:"error", auth: err.error["auth"] ,message: err.error["message"]});
                                         }                                                                           
                                        if (err.error.hasOwnProperty("message")){
                                            return of({type:"error", message:  err.error["message"]});
                                        }
                                        else{
                                            return of({type:"error", message: err.message})
                                        }
                                    })
                                );
    }

    GenerateJobId(modelId: number,datasetId: number): Observable<any> {
      const httpHeaders = this.httpUtils.getHTTPHeaders(null,null);
      return this.http.post<any>(environment.apiURL+API_GENJOBID_URL,{model_id:modelId, dataset_id: datasetId},{ headers: httpHeaders })
                                  .pipe(
                                    map(res => {
                                        return {type:"success", message: res};
                                    }),
                                    catchError(err => {
                                           if (err.error.hasOwnProperty("message")){
                                            return of({type:"error", message: err.error["message"]});
                                        }
                                        else{
                                            return of({type:"error", message: err.message})
                                        }
                                    })
                                );
    }

  }
