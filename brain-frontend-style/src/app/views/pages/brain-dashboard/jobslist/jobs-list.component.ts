import { AfterViewInit, AfterViewChecked } from '@angular/core';
// Angular
import { Component, OnInit, ElementRef, ViewChild, ChangeDetectionStrategy, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common'
// Material
import { SelectionModel } from '@angular/cdk/collections';
import { MatPaginator, MatDatepickerInputEvent, MatSort, MAT_DATE_LOCALE, MatSnackBar, MatFormFieldModule, DateAdapter, MAT_DATE_FORMATS, MatSortable } from '@angular/material';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
// RXJS
import { debounceTime, distinctUntilChanged, tap, skip, take, delay } from 'rxjs/operators';
import { fromEvent, merge, Observable, of, Subscription } from 'rxjs';
// LODASH
import { each, find } from 'lodash';
// NGRX
import { Store, select } from '@ngrx/store';
import { AppState } from '../../../../core/reducers';

// Services
import { LayoutUtilsService, MessageType, QueryParamsModel } from '../../../../core/_base/crud';
import { SubheaderService } from '../../../../core/_base/layout';

//Services and Models
import { JobsList } from '../../../../core/dashboard/_model/jobs-list-model';
import { OptModel } from '../../../../core/dashboard/_model/opt-model';
import { JobsListDataSource } from '../../../../core/dashboard/_datasource/jobs-list.datasource';
import { JobsListRequested } from '../../../../core/dashboard/_actions/jobs-list-actions';
import { selectJobsListActionLoading } from '../../../../core/dashboard/_selector/jobs-list-selector';
import { selectModelById } from '../../../../core/dashboard/_selector/dashboard.selector'
import { DataSetSelectedFromList, DataSetSelectedFromtheStructure } from '../../../../core/dashboard/_actions/dataset-list-action';

export const MY_FORMATS = {
	parse: {
	  dateInput: 'input',
	},
	display: {
	  dateInput: 'DD MMM YYYY',
	  monthYearLabel: 'MMM YYYY',
	  dateA11yLabel: 'DD MMM YYYY',
	  monthYearA11yLabel: 'MMMM YYYY',
	},
  };

@Component({
  selector: 'kt-jobs-list',
  templateUrl: './jobs-list.component.html',
  styleUrls: ['./jobs-list.component.scss'],
  providers: [
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},

    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ]
})
export class JobsListComponent implements OnInit,OnDestroy {

	// Table fields
  dataSource: JobsListDataSource;
  createDateInputSubscription
  jobslistResult: JobsList[] = [];
  loading$: Observable<boolean>;
  model$: Observable<OptModel>;
  selectedModel: OptModel;
  displayedColumns = ['dataset_scheduledatetime', 'dataset_name', 'created_by', 'createdAt'];
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild('a_sort', {static: true}) sort: MatSort;
  // Filter fields
  @ViewChild('scheduleDateInput', {static: true}) scheduleDateInput: ElementRef;
  @ViewChild('createDateInput', {static: true}) createDateInput:  ElementRef;
  //@ViewChild(MatDatepickerInput, {static: true}) matDatepickerinput:  MatDatepickerInput<any>;
  @ViewChild('searchInput', {static: true}) searchInput: ElementRef;
  @ViewChild('runByInput', {static: true}) runByInput: ElementRef;

  lastQuery: QueryParamsModel;
  // Selection
  selection = new SelectionModel<JobsList>(false, []);

  // Subscriptions
  private subscriptions: Subscription[] = [];

  //id of the model
  model_id:number;
  RunByText:string="";


  	/**
	 * Component constructor
	 *
	 * @param activatedRoute: ActivatedRoute
	 * @param router: Router
	 * @param subheaderService: SubheaderService
	 * @param layoutUtilsService: LayoutUtilsService
	 * @param store: Store<AppState>
	 * @param layoutConfigService: LayoutConfigService
	 */

  constructor(private activatedRoute: ActivatedRoute,
		private store: Store<AppState>,
		private router: Router,
		private layoutUtilsService: LayoutUtilsService,
		private subheaderService: SubheaderService,
		private cdr: ChangeDetectorRef,
		public datepipe: DatePipe) {}

  ngOnInit() {
	  this.sort.sort({
			  id: 'id',
			  start: 'desc'
		  } as MatSortable
	  );

	this.loading$ = this.store.pipe(select(selectJobsListActionLoading));

	const sortSubscription = this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));
	this.subscriptions.push(sortSubscription);

    const routeSubscription =  this.activatedRoute.params.subscribe(params => {
	  const id = params['id'];

      if (id && id > 0) {
		this.model_id=id;

		const paginatorSubscriptions = merge(this.sort.sortChange, this.paginator.page).pipe(
			tap(() => {
			this.loadJobsList(id);
			})
		)
		.subscribe();
		this.subscriptions.push(paginatorSubscriptions);

		// Filtration, bind to searchInput, scheduleDateINput
	   const searchSubscription = fromEvent(this.searchInput.nativeElement, 'keyup').pipe(
								debounceTime(150), // The user can type quite quickly in the input box, and that could trigger a lot of server requests. With this operator, we are limiting the amount of server requests emitted to a maximum of one every 150ms
								distinctUntilChanged(), // This operator will eliminate duplicate values
								tap(() => {
									this.paginator.pageIndex = 0;
									this.loadJobsList(id);
								})
							)
							.subscribe();
		this.subscriptions.push(searchSubscription);

		this.dataSource = new JobsListDataSource(this.store);

		const entitiesSubscription = this.dataSource.entitySubject.pipe(
								skip(1),
								distinctUntilChanged()
								).subscribe(jobslist => {
									this.jobslistResult= jobslist
								});
			this.subscriptions.push(entitiesSubscription);

			this.model$=this.store.pipe(select(selectModelById(id)));

			const modelSubscriptions=this.model$.subscribe(res=>{
				if (!res) {
				return;
				}
				this.selectedModel=res;
			});
			this.subscriptions.push(modelSubscriptions);

			this.loadJobsList(id);

			//Reset selected dataset
			this.store.dispatch(new DataSetSelectedFromList({ dslist: null }));
	}
	else {
        	this.router.navigate(['../../'], { relativeTo: this.activatedRoute });
		}
	});
    this.subscriptions.push(routeSubscription);
  }

  ngOnDestroy(){
	this.subscriptions.forEach(el => el.unsubscribe());
	this.model_id=undefined;
  }

  DatepickerInput_Event(event: MatDatepickerInputEvent<Date>): void {
	if(this.model_id>0)
	{
		this.paginator.pageIndex = 0;
		this.loadJobsList(this.model_id);
	}
  }

  runByInput_event(event): void {
	if(this.model_id>0)
	{
		this.RunByText=event.value;
		this.paginator.pageIndex = 0;
		this.loadJobsList(this.model_id);
	}
}

clickRow(row):void {
    this.selection.toggle(row);
	let selectedDatasetId=this.selection.selected[0]["dataset_id"];
	let selectedModelId=this.selection.selected[0]["model_id"];
	let selectedJobId=this.selection.selected[0]["id"];
	this.store.dispatch(new DataSetSelectedFromtheStructure({ newValue: selectedDatasetId }));
	this.router.navigate(['../../roster',selectedModelId,selectedJobId],{ relativeTo: this.activatedRoute });
}


  loadJobsList(id_parm:number) {
		this.selection.clear();
		const queryParams = new QueryParamsModel(
			this.filterConfiguration(),
			this.sort.direction,
			this.sort.active,
			this.paginator.pageIndex,
			(this.paginator.pageSize===null||this.paginator.pageSize===undefined?this.paginator.pageSize=5:this.paginator.pageSize)
		);
		// Call request from server
		this.store.dispatch(new JobsListRequested({ id : id_parm, page: queryParams }));
		this.selection.clear();
  }

  public createRoster(id){
	this.router.navigate(['../../roster',id], { relativeTo: this.activatedRoute });
}

  	/** FILTRATION */
	filterConfiguration(): any {
		const filter: any = {};
		const searchText: string = this.searchInput.nativeElement.value;
		const RunByText: string = this.RunByText
		const scheduleDateText: string = this.datepipe.transform(this.scheduleDateInput.nativeElement.value, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		const createDateText: string = this.datepipe.transform(this.createDateInput.nativeElement.value, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		filter.dataset_name 			= searchText;
		filter.created_by 				= RunByText;
		filter.dataset_scheduledatetime	= (scheduleDateText===null?"":scheduleDateText);
		filter.createdAt				= (createDateText===null?"":createDateText);

		return filter;
	}

}
