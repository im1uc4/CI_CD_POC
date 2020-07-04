//Angular
import { Component, OnInit, OnDestroy, ChangeDetectionStrategy, Input, ElementRef} from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { MatPaginator, MatSort, MatDialog } from '@angular/material';
import { SelectionModel } from '@angular/cdk/collections';
// NGRX
import { Store, select } from '@ngrx/store';
import { AppState } from '../../../core/reducers';
// RXJS
import { debounceTime, distinctUntilChanged, tap, skip, delay } from 'rxjs/operators';
import { fromEvent, merge, of, Subscription } from 'rxjs';
// UI
import { SubheaderService } from '../../../core/_base/layout';
// CRUD
import { LayoutUtilsService, MessageType, QueryParamsModel } from '../../../core/_base/crud';
//Services
import {BrainDashboard_Service} from '../../../core/dashboard/service/brain-dashboard.service';
import { DashboardElement_Service } from '../../../core/dashboard/service/dashboard-element.service';
import { DashboardElement } from '../../../core/dashboard/element/dashboard-element';
//Services and Models
import { OptModel } from '../../../core/dashboard/_model/opt-model';
import { OptModelsDataSource } from '../../../core/dashboard/_datasource/models.datasource';
import { OptModulesPageRequested, OptModuleReset } from '../../../core/dashboard/_actions/opt-model-actions';
import { DataSetDataModifiedReset } from '../../../core/dashboard/_actions/dataset_data-actions';
import { DataSetListReset } from '../../../core/dashboard/_actions/dataset-list-action';
import { DynamicHeaderReset } from '../../../core/dashboard/_actions/dynamic_dataset_headers-actions';
//import { selectOptModelPageLastQuery } from '../../../core/dashboard/_selector/dashboard.selector';

@Component({
  selector: 'kt-brain-dashboard',
  templateUrl: './brain-dashboard.component.html',
  styleUrls: ['./brain-dashboard.component.scss']
})
export class BrainDashboardComponent implements OnInit, OnDestroy {

  dataSource: OptModelsDataSource; 
  lastQuery: QueryParamsModel;   
  // Selection
  selection = new SelectionModel<OptModel>(true, []);
  optmodelsResult: OptModel[] = [];
  private subscriptions: Subscription[] = [];

  /**
	 * Component constructor
	 *
	 * @param dialog: MatDialog
	 * @param activatedRoute: ActivatedRoute
	 * @param router: Router
	 * @param subheaderService: SubheaderService
	 * @param layoutUtilsService: LayoutUtilsService
	 * @param store: Store<AppState>
	 */

  constructor(private Dashboard_elem_service: DashboardElement_Service,
    private BrainDashBoard_Service: BrainDashboard_Service,
    public dialog: MatDialog,
		private activatedRoute: ActivatedRoute,
		private router: Router,
		private subheaderService: SubheaderService,
		private layoutUtilsService: LayoutUtilsService,
		private store: Store<AppState>
    ) { }
  dashElements: Observable<DashboardElement[]>;
  currentRoot: DashboardElement;
  current_date:Date;
  current_dow:String;
  current_week_day:String;

  ngOnInit() { 

    let child_obj;

    //reset_reducers
    this.store.dispatch(new OptModuleReset())
    this.store.dispatch(new DataSetDataModifiedReset())
    this.store.dispatch(new DataSetListReset())
    this.store.dispatch(new DynamicHeaderReset())

    //Init Datastore
    this.dataSource = new OptModelsDataSource(this.store);  
    const entitiesSubscription = this.dataSource.entitySubject.pipe(
			skip(1),
			distinctUntilChanged()
		).subscribe(res => { 
      this.optmodelsResult = res;
      if (res) {
          Object.keys(res).forEach(k =>{
            if (typeof res[k] === 'object' && res[k] !== null){  
              child_obj=res[k]; 
              if  (child_obj.hasOwnProperty('id')&&child_obj.hasOwnProperty('model_name')&&child_obj.hasOwnProperty('model_description_header')&&child_obj.hasOwnProperty('model_description_details')&&child_obj.hasOwnProperty('person_in_charge'))  
              {      
                this.Dashboard_elem_service.add({id: child_obj["id"], name: child_obj["model_name"], desc_header:child_obj["model_description_header"], desc_details:child_obj["model_description_details"], person_in_charge:child_obj["person_in_charge"], status:true, location:'root'});
                this.updateFileElementQuery(); 
              }
            }
          })
      }
    });
    this.subscriptions.push(entitiesSubscription);



    this.updateFileElementQuery(); 

    this.loadModelsList();

    // Read from URL itemId, for restore previous state
		//const routeSubscription = this.activatedRoute.queryParams.subscribe(params => {			// First load
		//    this.loadModelsList();      
		//});
    //this.subscriptions.push(routeSubscription); 

    this.current_date = new Date();
    this.current_dow  = getDayOfWeek(this.current_date)


    //function to get day of week
    function getDayOfWeek(date) {
      var dayOfWeek = new Date(date).getDay();    
      return isNaN(dayOfWeek) ? null : ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][dayOfWeek];
    }
  }

  ngOnDestroy(){   
    this.subscriptions.forEach(el => el.unsubscribe()); 
  }

  loadModelsList() {
		this.selection.clear();	
		// Call request from server
		this.store.dispatch(new OptModulesPageRequested()); 
		this.selection.clear();
  }
 

  updateFileElementQuery() {
    this.dashElements = this.Dashboard_elem_service.queryInFolder(this.currentRoot ? this.currentRoot.id : 'root');
  }

}
