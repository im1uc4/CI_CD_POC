// Angular
import { Injectable } from '@angular/core';
import { HttpParams, HttpHeaders } from '@angular/common/http';
// CRUD
import { QueryResultsModel } from '../models/query-models/query-results.model';
import { QueryParamsModel } from '../models/query-models/query-params.model';
import { HttpExtenstionsModel } from '../../crud/models/http-extentsions-model';
import { environment } from '../../../../../environments/environment';

@Injectable()
export class HttpUtilsService {
	/**
	 * Prepare query http params
	 * @param queryParams: QueryParamsModel
	 */
	getFindHTTPParams(queryParams): HttpParams {
		const params = new HttpParams()
			.set('lastNamefilter', queryParams.filter)
			.set('sortOrder', queryParams.sortOrder)
			.set('sortField', queryParams.sortField)
			.set('pageNumber', queryParams.pageNumber.toString())
			.set('pageSize', queryParams.pageSize.toString());

		return params;
	}

	/**
	 * get standard content-type
	 
		getHTTPHeaders(): HttpHeaders {
			const userToken = localStorage.getItem(environment.authTokenKey);
			const result = new HttpHeaders();
			result.set('Content-Type', 'application/json');
			result.set('Authorization', userToken);
			return result;
		}*/

	    getHTTPHeaders(key_val: string, value: string): HttpHeaders {
		const userToken = 'Bearer '+localStorage.getItem(environment.authTokenKey);
		let result;

		if(key_val&&value){	

			result = new HttpHeaders({'Access-Control-Allow-Origin': '*','Content-Type': 'application/json','Authorization': userToken});	
			result=result.append( ''+`${key_val}`+'', value)				
		}
		else{
			result = new HttpHeaders({'Access-Control-Allow-Origin': '*','Content-Type': 'application/json','Authorization': userToken});					
		}

		return result;
	}

	baseFilter(_entities: any[], _queryParams: QueryParamsModel, _filtrationFields: string[] = []): QueryResultsModel {
		const httpExtention = new HttpExtenstionsModel();
		return httpExtention.baseFilter(_entities, _queryParams, _filtrationFields);
	}

	sortArray(_incomingArray: any[], _sortField: string = '', _sortOrder: string = 'asc'): any[] {
		const httpExtention = new HttpExtenstionsModel();
		return httpExtention.sortArray(_incomingArray, _sortField, _sortOrder);
	}

	searchInArray(_incomingArray: any[], _queryObj: any, _filtrationFields: string[] = []): any[] {
		const httpExtention = new HttpExtenstionsModel();
		return httpExtention.searchInArray(_incomingArray, _queryObj, _filtrationFields);
	}
}
