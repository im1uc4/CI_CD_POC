import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt'

import { User } from '../_models/user.model';
import { Permission } from '../_models/permission.model';
import { Role } from '../_models/role.model';
import { QueryParamsModel, QueryResultsModel } from '../../_base/crud';
import { environment } from '../../../../environments/environment';
import { HttpUtilsService } from '../../_base/crud';


const API_USERS_URL = '/api/v1/verify-user' ;
const API_SIGNIN_URL = '/api/v1/signin';
const API_PERMISSION_URL = '/api/v1/permissions';
const API_ROLES_URL = '/api/v1/roles';
const API_GROUPS_URL = '/api/v1/groups';


@Injectable()
export class AuthService {
    constructor(private http: HttpClient,
                private httpUtils: HttpUtilsService) {}
    private jwtHelperService: JwtHelperService=new JwtHelperService();

    public getdecodedToken(jwt_token:any){
        let token: string;
        let isExpired:boolean;

          if (!jwt_token) 
          {
           return null
          }
          else
          {
            token=jwt_token;  
          }

        
        const decodeToken = this.jwtHelperService.decodeToken(token);
             isExpired=  this.jwtHelperService.isTokenExpired(token); 

        if ((!decodeToken)&&(!isExpired)) {            
            return null;
          }
        

        return decodeToken;
    }
        
    // Authentication/Authorization
    login(username: string, password: string): Observable<User> {
        return this.http.post<any>(environment.apiURL+API_SIGNIN_URL, { username, password })
        .pipe(map(user=>{

            let token:string;
            let usr:User=new User();
            if(user["accessToken"] !== null && user["accessToken"] !== '')
            {        
                token=this.getdecodedToken(user["accessToken"]);

                if(token== null)
                {
                    return of(null);                    
                }

                usr.username=token["username"];
                usr.email=token["email"];
                usr.fullname=token["fullname"];
                usr.accessToken=user["accessToken"];
                usr.dislay_title=user["dislay_title"];
                usr.pic=user.pic;                
            } 
            
            return usr;

        }), catchError((err, caught) => {      
           return of(err);           
       }
    )
       );
    }    

    getUserByToken(): Observable<User> {       
        const userToken = localStorage.getItem(environment.authTokenKey);
        //const httpHeaders = new HttpHeaders({'Authorization':userToken}); 
        //httpHeaders.append('Authorization', userToken);
        const httpHeaders = this.httpUtils.getHTTPHeaders(null,null);
        return this.http.get<User>(environment.apiURL+API_USERS_URL, { headers: httpHeaders })
                .pipe(map(result=>{
                    let usr:User =new User();
                    let token: string;
                    
                    token=this.getdecodedToken(userToken);

                    if(token== null)
                    {
                        return of(null);                    
                    }    

                    usr.username=token["username"];
                    usr.email=token["email"];
                    usr.fullname=token["fullname"];;
                    usr.accessToken='Bearer ' + userToken;;
                    usr.pic=result.pic;  

                    return usr;
                }),catchError((err, caught) => {   
                    return of(null);                    
                }
             ));
    }

        // Roles
    getAllRoles(): Observable<Role[]> {
        const httpHeaders = this.httpUtils.getHTTPHeaders(null,null);
        return this.http.get<Role[]>(environment.apiURL+API_ROLES_URL, { headers: httpHeaders });
    }

    // Permission
    getAllPermissions(): Observable<Permission[]> {
		return this.http.get<Permission[]>(API_PERMISSION_URL);
    }

    register(user: User): Observable<any> {
        //const httpHeaders = new HttpHeaders();
        //httpHeaders.set('Content-Type', 'application/json');
        const httpHeaders = this.httpUtils.getHTTPHeaders(null,null);
        return this.http.post<User>(API_USERS_URL, user, { headers: httpHeaders })
            .pipe(
                map(res => {
                    return res;
                }),
                catchError(err => {
                    console.log(err);
                    return null;
                })
            );
    }

    /*
     * Submit forgot password request
     *
     * @param {string} email
     * @returns {Observable<any>}
     */
    public requestPassword(email: string): Observable<any> {
        console.log("API3");
    	return this.http.get(API_USERS_URL + '/forgot?=' + email)
    		.pipe(catchError(this.handleError('forgot-password', []))
	    );
    }


    getAllUsers(): Observable<User[]> {
        return this.http.get<User[]>(API_USERS_URL);
    }

    getUserById(userId: number): Observable<User> {
        return this.http.get<User>(API_USERS_URL + `/${userId}`);
	}


    // DELETE => delete the user from the server
	deleteUser(userId: number) {
		const url = `${API_USERS_URL}/${userId}`;
		return this.http.delete(url);
    }

    // UPDATE => PUT: update the user on the server
	updateUser(_user: User): Observable<any> {
        //const httpHeaders = new HttpHeaders();
        //httpHeaders.set('Content-Type', 'application/json');
        const httpHeaders = this.httpUtils.getHTTPHeaders(null,null);
		return this.http.put(API_USERS_URL, _user, { headers: httpHeaders });
	}

    // CREATE =>  POST: add a new user to the server
	createUser(user: User): Observable<User> {
    	const httpHeaders = new HttpHeaders();
        httpHeaders.set('Content-Type', 'application/json');
		return this.http.post<User>(API_USERS_URL, user, { headers: httpHeaders});
	}

    // Method from server should return QueryResultsModel(items: any[], totalsCount: number)
	// items => filtered/sorted result
	findUsers(queryParams: QueryParamsModel): Observable<QueryResultsModel> {
        const httpHeaders = new HttpHeaders();
        httpHeaders.set('Content-Type', 'application/json');
		return this.http.post<QueryResultsModel>(API_USERS_URL + '/findUsers', queryParams, { headers: httpHeaders});
    }
    
    getRolePermissions(roleId: number): Observable<Permission[]> {
        return this.http.get<Permission[]>(API_PERMISSION_URL + '/getRolePermission?=' + roleId);
    }

    getRoleById(roleId: number): Observable<Role> {
		return this.http.get<Role>(API_ROLES_URL + `/${roleId}`);
    }

    // CREATE =>  POST: add a new role to the server
	createRole(role: Role): Observable<Role> {
		// Note: Add headers if needed (tokens/bearer)
        const httpHeaders = new HttpHeaders();
        httpHeaders.set('Content-Type', 'application/json');
		return this.http.post<Role>(API_ROLES_URL, role, { headers: httpHeaders});
	}

    // UPDATE => PUT: update the role on the server
	updateRole(role: Role): Observable<any> {
        const httpHeaders = new HttpHeaders();
        httpHeaders.set('Content-Type', 'application/json');
		return this.http.put(API_ROLES_URL, role, { headers: httpHeaders });
	}

	// DELETE => delete the role from the server
	deleteRole(roleId: number): Observable<Role> {
		const url = `${API_ROLES_URL}/${roleId}`;
		return this.http.delete<Role>(url);
	}

    // Check Role Before deletion
    isRoleAssignedToUsers(roleId: number): Observable<boolean> {
        return this.http.get<boolean>(API_ROLES_URL + '/checkIsRollAssignedToUser?roleId=' + roleId);
    }

    findRoles(queryParams: QueryParamsModel): Observable<QueryResultsModel> {
        // This code imitates server calls
        const httpHeaders = new HttpHeaders();
        httpHeaders.set('Content-Type', 'application/json');
		return this.http.post<QueryResultsModel>(API_ROLES_URL + '/findRoles', queryParams, { headers: httpHeaders});
	}

 	/*
 	 * Handle Http operation that failed.
 	 * Let the app continue.
     *
	 * @param operation - name of the operation that failed
 	 * @param result - optional value to return as the observable result
 	 */
    private handleError<T>(operation = 'operation', result?: any) {
        return (error: any): Observable<any> => {
            // TODO: send the error to remote logging infrastructure
            console.error(error); // log to console instead

            // Let the app keep running by returning an empty result.
            return of(result);
        };
    }
}
