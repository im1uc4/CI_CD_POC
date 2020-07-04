// Angular
import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse, HttpErrorResponse } from '@angular/common/http';
// RxJS
import { Observable } from 'rxjs';
import { tap, map } from 'rxjs/operators';
//User
import { User } from '../../../auth/_models/user.model';
// Debug since it's not being used import { debug } from 'util';

/**
 * More information there => https://medium.com/@MetonymyQT/angular-http-interceptors-what-are-they-and-how-to-use-them-52e060321088
 */
@Injectable()
export class InterceptService implements HttpInterceptor {
	// intercept request and add token
	intercept(
		request: HttpRequest<any>,
		next: HttpHandler
	): Observable<HttpEvent<any>> {
		const { url, method, headers, body } = request;     
		var self = this;
		switch (true) {
            case url.endsWith('/api/v1/signin') && method === 'POST':
				return authenticate();
			case url.endsWith('/api/v1/verify-user') && method === 'GET':
				return getUserDetails()
			case url.endsWith('/api/v1/verify-user') && method === 'GET':
				return getDashboardElements()				
            default:
                return next.handle(request);
		}  

		//Get details by token
			function getUserDetails(){
				return next.handle(request).pipe(map((event: HttpEvent<any>)=>{ 
					if (event instanceof HttpResponse) { 
						if(!event.body.auth){
							throw (new HttpErrorResponse({ status: 200,  error: { message: event.body.err}}));                  
						}						
						return ok({
							auth:'true',
							pic:'./assets/media/users/default.jpg'							
						})
				}
			}));
		}

		//Get available cards for dashboard
		function getDashboardElements(){           
            return next.handle(request);
		}
		
		function checkTokenValidity(){
			
		}

		
		//Authentication
		function authenticate() { 
            return next.handle(request).pipe(map((event: HttpEvent<any>)=>{ 
                if (event instanceof HttpResponse) {        
                        if(!event.body.auth){
                            throw (new HttpErrorResponse({ status: 200,  error: { message: event.body.reason}}));                     
						} 
                        if(event.body.jwt_token)
                        {                                      
							return ok({
								accessToken : event.body.jwt_token,
								pic:'./assets/media/users/default.jpg'    
							})
								
                        }
                        else
                        {                  
                            throw (new HttpErrorResponse({ status: 200,  error: { message: "Token issue."}})); 
                        }

                        
                }    
                },(err: any) => {
                    if (err instanceof HttpErrorResponse) { 
                        throw new HttpErrorResponse({ status: 200,  error: { message: err }});                  
                    }
                  }
                ));  
		}   
		
		function ok(body?) {
            return (new HttpResponse({ status: 200, body }));
        }

		return next.handle(request).pipe(
			tap(
				event => {
					 if (event instanceof HttpResponse) {
						console.log('all looks good');
						// http response status code
						// console.log(event.status);
					}
				},
				error => {
					// http response status code
					// console.log('----response----');
					// console.error('status code:');
					// tslint:disable-next-line:no-debugger
					console.error(error.status);
					console.error(error.message);
					// console.log('--- end of response---');
				}
			)
		);
	}
}
