//Angular
import { Inject, Component, OnInit, OnDestroy, ChangeDetectionStrategy, Input, ElementRef} from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { MAT_DIALOG_DATA, MatPaginator, MatSort, MatDialog } from '@angular/material';
import { SelectionModel } from '@angular/cdk/collections';
// NGRX
import { Store, select } from '@ngrx/store';
import { AppState } from '../../../../core/reducers';
// RXJS
import { debounceTime, distinctUntilChanged, tap, skip, delay } from 'rxjs/operators';
import { fromEvent, merge, of, Subscription } from 'rxjs';
// UI
import { SubheaderService } from '../../../../core/_base/layout';
// CRUD
import { LayoutUtilsService, MessageType, QueryParamsModel } from '../../../../core/_base/crud';
//Services
import { OptModelConstraints } from '../../../../core/dashboard/_model/opt-model-constraints';
import { OptModelConstraintsService } from '../../../../core/dashboard/service/opt-model-constraints.service';
import { ConstraintsDataSource } from '../../../../core/dashboard/_datasource/constraints.datasource';
import {  OptModelConstraintsListRequested } from '../../../../core/dashboard/_actions/opt-model-constraints-actions';

@Component({
  selector: 'kt-constraints-list',
  templateUrl: './constraints-list.component.html',
  styleUrls: ['./constraints-list.component.scss']
})
export class ConstraintsListComponent implements OnInit {

/**
	 * Component constructor
	 *
	 * @param activatedRoute: ActivatedRoute
	 * @param router: Router
	 * @param subheaderService: SubheaderService
	 * @param layoutUtilsService: LayoutUtilsService
	 * @param store: Store<AppState>
	 */

  dataSource:ConstraintsDataSource;
  optmodelsResult: OptModelConstraints[] = []; 
  constraint: OptModelConstraints; 
  private subscriptions: Subscription[] = [];

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
    private ConstraintsService: OptModelConstraintsService,
		private activatedRoute: ActivatedRoute,
		private router: Router,
		private subheaderService: SubheaderService,
		private layoutUtilsService: LayoutUtilsService,
    private store: Store<AppState>) { }
    
  ngOnInit() {

    this.dataSource = new ConstraintsDataSource(this.store);     
    const entitiesSubscription = this.dataSource.entitySubject.pipe(
			skip(1),
			distinctUntilChanged()
    ).subscribe(constraint => {
          this.optmodelsResult= constraint
    });

    this.subscriptions.push(entitiesSubscription);


    this.store.dispatch(new OptModelConstraintsListRequested({ id : this.data.model_id }));  

  }

  ngOnDestroy() {
    this.subscriptions.forEach(el => el.unsubscribe()); 
		}

}


