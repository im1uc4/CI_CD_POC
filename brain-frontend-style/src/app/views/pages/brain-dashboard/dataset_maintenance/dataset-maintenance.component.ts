// Angular
import { AfterViewInit, AfterViewChecked, AfterContentChecked } from '@angular/core';
import { Component, OnInit, ElementRef, ViewChild, OnDestroy, ViewContainerRef, QueryList, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common'
import { FormBuilder, FormGroup, Validators, ValidatorFn, ValidationErrors} from '@angular/forms';
import { MatDialog, MatDialogConfig, MatDialogRef } from '@angular/material/dialog';
// Material
import { SelectionModel } from '@angular/cdk/collections';
import { MatPaginator, MatDatepickerInputEvent, MatSort, MatStepper, MAT_DATE_LOCALE, MatRow, MatSnackBar, MatFormFieldModule, DateAdapter, MAT_DATE_FORMATS } from '@angular/material';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
// RXJS
import { debounceTime, distinctUntilChanged, tap, skip, switchMap } from 'rxjs/operators';
import { fromEvent, merge, Observable, Subscription, timer, of, Subject } from 'rxjs';
// NGRX
import { Store, select } from '@ngrx/store';
import { AppState } from '../../../../core/reducers';
// Services
import { LayoutUtilsService, QueryParamsModel, QueryResultsModel, MessageType } from '../../../../core/_base/crud';
import { JobsStatusService } from '../../../../core/dashboard/service/jobs-status.service';
import { SubheaderService } from '../../../../core/_base/layout';
import { SplashScreenService } from '../../../../core/dashboard/service/splash-screen.service';
//Services and Models
import { DataSetList } from '../../../../core/dashboard/_model/dataset-list-model';
import { OptModel } from '../../../../core/dashboard/_model/opt-model';
import { DatasetListDataSource } from '../../../../core/dashboard/_datasource/dataset-list.datasource';
import { DataSetListRequested, DataSetSelectedFromList } from '../../../../core/dashboard/_actions/dataset-list-action';
import { selectDSListActionLoading, selectlastSelectedDataset, selectDatasetById } from '../../../../core/dashboard/_selector/dataset-list-selector';
import { DataSetSelectedFromtheStructure } from '../../../../core/dashboard/_actions/dataset-list-action';
import { JobsReset } from '../../../../core/dashboard/_actions/job-in-progress.action';
import { selectModelById, selectLastSelectedModelId } from '../../../../core/dashboard/_selector/dashboard.selector'
import { selectJobsInProgress } from '../../../../core/dashboard/_selector/jobs-in-progress-selector';
import { DynamicHeader } from '../../../../core/dashboard/_model/dynamic_dataset_headers-model';
//Selectors
import { selectDSintheList } from '../../../../core/dashboard/_selector/dataset-list-selector';
import { DynamicHeaderRequested } from '../../../../core/dashboard/_actions/dynamic_dataset_headers-actions';
//import { selectDynamicHeaderActionLoading } from '../../../../core/dashboard/_selector/dynamic_dataset_headers-selector';
import { DataSetDataModel } from '../../../../core/dashboard/_model/dataset_data-model';
import { DataSetDataRequested, DataSetDataModifiedReset, DataSetDataOnServerCreated, DataSetDataResetOperationResult} from '../../../../core/dashboard/_actions/dataset_data-actions';
import {  selectDataSetDataById, selectLastDsSavingStatus, selectDataSetDataModifiedFlag } from '../../../../core/dashboard/_selector/dataset_data-selector';
import { ConstraintsListComponent } from '../constraints_list/constraints-list.component';
import { SaveDatasetDialogComponent } from '../save-dataset-dialog/save-dataset-dialog.component';
import { SplashScreenComponent } from '../splash-screen/splash-screen.component';
import { JobsStatusModel } from '../../../../core/dashboard/_model/jobs-status-model';
import { IfStmt } from '@angular/compiler';
import { JobsList } from '../../../../core/dashboard/_model/jobs-list-model';
import { selectJobsListId } from '../../../../core/dashboard/_selector/jobs-list-selector';
import { MatTableDataSource } from '@angular/material/table';
import * as moment from 'moment';

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
  selector: 'kt-dataset-maintenance',
  templateUrl: './dataset-maintenance.component.html',
  styleUrls: ['./dataset-maintenance.component.scss'],
  providers: [
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ]
})

export class DataSetMaintenanceComponent implements OnInit, OnDestroy, AfterViewInit {
	
  // Table fields
  dataSource:  DatasetListDataSource;
  createDateInputSubscription
  dslistResult: DataSetList[] = []; 
  lastSelectedDataSetId:  number;  
  loading$: Observable<boolean>;
  lastSelectedModel$:  Observable<number>;
  lastSavedDataSet$:  Observable<any>;
  dataModiFiedFlag$:  Observable<any>;
  selectDataSetListById$:  Observable<DataSetList>;
  selectJobsInProgressId$:  Observable<any>;
  model$: Observable<OptModel>;  
  selectedJob$:  Observable<JobsList>;
  selectDynamicHeaderintheList;
  selectDatasetById;
  allColumnsHeaders$: Observable<QueryResultsModel>;
  dsTitle:String;
  dataStore_dsList:MatTableDataSource<any[]>=new MatTableDataSource();
  dataModified:boolean=false;
  selectedModel: OptModel;
  tablesHeadersModel: DynamicHeader[];

  displayedColumns = ['dataset_scheduledatetime', 'dataset_name', 'created_by', 'createdAt'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;
  @ViewChild('a_sort', {static: true}) sort: MatSort;
  // Filter fields
  @ViewChild('scheduleDateInput', {static: true}) scheduleDateInput: ElementRef;
  @ViewChild('createDateInput', {static: true}) createDateInput:  ElementRef;
  @ViewChild('searchInput1', {static: true}) searchInput1: ElementRef;
  @ViewChild('runByInput', {static: true}) runByInput: ElementRef;
  //Stepper
  @ViewChild('stepper', {static: true}) private stepper: MatStepper;
 
  lastQuery: QueryParamsModel;
  qParam: QueryParamsModel;

  // Selection
  selection = new SelectionModel<DataSetList>(false, []);

  // Subscriptions
  private subscriptions: Subscription[] = [];
  jobStatusModel: JobsStatusModel;

  //id of the model
  model_id:number;
  RunByText:string="";
  jobid:number;
  jobIdString:string=""
  //
  dataSetDataChange= { isChanged:false };
  isLinear = false;
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  //
  saveDsIsSuccess:boolean=false
  saveDsId:number=null
  genJobId:number=null
  proceedSubject: Subject<boolean> = new Subject<boolean>();
  JobDialog: MatDialogRef<SplashScreenComponent,any>;
  showGantt:boolean=false;
  requestfromAPI:boolean=true;

   tab1_validity:boolean=true
   tab2_validity:boolean=true
   tab3_validity:boolean=true
   tab4_validity:boolean=true
   lb_proceed:boolean=true
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
    public datepipe: DatePipe,
    private _formBuilder: FormBuilder,
    public dialog: MatDialog,
    private jobsStatusService: JobsStatusService,
    private splashScreenService: SplashScreenService) {}

  ngOnInit() {  

      let id = 0;
      this.firstFormGroup = this._formBuilder.group({
      firstCtrl: [null,  Validators.requiredTrue]
        });
      this.secondFormGroup= this._formBuilder.group({
        });
      
      const routedataSubscription =  this.activatedRoute.data.subscribe(data => { 
                                      if(Object.keys(data).length>0){
                                          this.requestfromAPI=(!data.retrieve?false:true)
                                      }
                                    })
      this.subscriptions.push(routedataSubscription);
            
      const routeSubscription =  this.activatedRoute.params.subscribe(params => {
      const jobid_temp = params['jobid'];
      
        if (jobid_temp && jobid_temp > 0) {
          this.selectedJob$= this.store.pipe(select(selectJobsListId(jobid_temp)));
          const selectedJobSubscriptions=this.selectedJob$.subscribe(res=>{
                  this.jobid=jobid_temp;
                  this.genJobId=jobid_temp;
                  this.jobIdString=res.jobId;   
                  this.showGantt=true;                
                });		
          this.subscriptions.push(selectedJobSubscriptions);

        }        
      })
      this.subscriptions.push(routeSubscription);

      this.dataModiFiedFlag$= this.store.pipe(select(selectLastDsSavingStatus));
      const lastDataModifiedFlag=this.dataModiFiedFlag$.subscribe(res=>{
            this.saveDsIsSuccess=res["succeed"]
            this.saveDsId=res["dataset_id"]
            this.genJobId=res["jobId"]
        });		
      this.subscriptions.push(lastDataModifiedFlag); 

      // Filtration, bind to searchInput, scheduleDateINput
      const searchSubscription = fromEvent(this.searchInput1.nativeElement, 'keyup').pipe(					
      debounceTime(150), // The user can type quite quickly in the input box, and that could trigger a lot of server requests. With this operator, we are limiting the amount of server requests emitted to a maximum of one every 150ms
      distinctUntilChanged(), // This operator will eliminate duplicate values
      tap(() => {
        let filters= this.filterConfiguration()
        this.dataStore_dsList.filterPredicate = function(data, filter: string): boolean {
          return data["dataset_name"].toLowerCase().includes(filter);
        };

        this.dataStore_dsList.filter=filters.dataset_name
        })
      ).subscribe()
      this.subscriptions.push(searchSubscription);

      this.loading$ = this.store.pipe(select(selectDSListActionLoading));

      const sortSubscription = this.sort.sortChange.subscribe(() => (this.paginator.pageIndex = 0));
      this.subscriptions.push(sortSubscription);

      this.lastSelectedModel$= this.store.pipe(select(selectLastSelectedModelId));
      const lastModelSubscriptions=this.lastSelectedModel$.subscribe(res=>{
              if (!res) {
                this.router.navigate(['../../'], { relativeTo: this.activatedRoute });
              }
              id=res;
            });		
      this.subscriptions.push(lastModelSubscriptions); 

      this.lastSavedDataSet$= this.store.pipe(select(selectDataSetDataModifiedFlag));
      const lastSavedDataSetSubscriptions=this.lastSavedDataSet$.subscribe(res=>{
            this.dataModified=res
        });		
      this.subscriptions.push(lastSavedDataSetSubscriptions);   
      
      this.selectJobsInProgressId$= this.store.pipe(select(selectJobsInProgress));
      const JobsInProgressSubscription=this.selectJobsInProgressId$.subscribe(res=>{
        let resItems=res["items"]
        let lastOperSucceed=res.lastOperationSucceed
        if(lastOperSucceed){
           if(resItems[0]){
              if(resItems[0]["jobId"]===this.genJobId&&resItems[0]["jobStatus"]==="COMPLETED"){              
                      this.jobIdString=res["items"][0]["jobId"];
                      this.showGantt=true;                         
                      this.stepper.next(); 
                      this.store.dispatch(new DataSetDataResetOperationResult());
                      this.JobDialog.close();           
              }
            }
          }
      })
      this.subscriptions.push(JobsInProgressSubscription);
      
      if (id && id > 0) {
        this.model_id=id;

        // Load last query from store
        const lastSelectedDsSubscription = this.store.pipe(select(selectlastSelectedDataset)).subscribe(res => { 
                this.lastSelectedDataSetId = res.lastSelectedDataset; 
                
                if(this.requestfromAPI&&this.lastSelectedDataSetId>0){
                  this.firstFormGroup.setValue({firstCtrl: true});
                  this.store.dispatch(new DynamicHeaderRequested({ dataset_id : this.lastSelectedDataSetId, page: this.qParam })); 
                  this.store.dispatch(new DataSetDataRequested({ dataset_id : this.lastSelectedDataSetId, page: this.qParam })); 
                }

                if(!this.jobid&&!this.lastSelectedDataSetId){
                  this.jumpToStep(0)
                }                
                if(this.jobid>0&&this.lastSelectedDataSetId>0){
                  this.jumpToStep(2)
                }
                if(!this.jobid&&this.lastSelectedDataSetId>0){               
                  this.jumpToStep(1);
                }
              });
        this.subscriptions.push(lastSelectedDsSubscription);

                         
        this.selectDataSetListById$= this.store.pipe(select(selectDatasetById(this.lastSelectedDataSetId)));
        const selectDataSetListByIdSubscriptions=this.selectDataSetListById$.subscribe(res=>{
          if(res){            
            this.dslistResult.push(res)
          }               
        });		
        this.subscriptions.push(selectDataSetListByIdSubscriptions);

       
        this.dataSource = new DatasetListDataSource(this.store); 
                      
        const entitiesSubscription = this.dataSource.entitySubject.pipe(
              skip(1),
              distinctUntilChanged()
              ).subscribe(dslist => {
                this.dslistResult= dslist  
                this.dsTitle=dslist.filter(x=>(x.id===this.lastSelectedDataSetId))["dataset_name"]

                this.dataStore_dsList=new MatTableDataSource(dslist)
    
                  if(this.paginator){
                    this.dataStore_dsList.paginator=this.paginator
                  }

                  if(this.sort){
                    this.dataStore_dsList.sort=this.sort         
                  }

                  //this.dataStore_dsList.data=dslist
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

        this.loadDSList(id, this.lastQuery);
    } 
    else {
        this.router.navigate(['../../'], { relativeTo: this.activatedRoute });
      }  
       
  }

  ngAfterViewInit(){}   

  ngAfterViewChecked(){
    if ((!this.selection.selected[0])&&(this.lastSelectedDataSetId))
    {
      if(this.jobid){
        this.dslistResult=this.dslistResult.filter(row =>row.id===this.lastSelectedDataSetId)
      }

      this.dslistResult.forEach(row => {
        if(row.id===this.lastSelectedDataSetId)
        {
          this.clickRow(row);       
        }
      });
    }    
  }  
  
  emitProceedforGenerateScheduleSpinner() {
    this.proceedSubject.next(this.lb_proceed)
  }

   //Convert table data into convenient format
    convert_API_dataSetData<Obect>(input_ds:DataSetDataModel[]){
      var result = []
      input_ds.forEach(function(e, i) {
      if (!this[e.record_id]) {
        this[e.record_id] = {
          record_id: e.record_id,
          table_name:e.table_name,
          [e.field_name]: e.value
        }
        result.push(this[e.record_id])
      } else {
        this[e.record_id][e.field_name] = e.value
      }
    }, {})

   return result
    } 
  

  matchingInputsValidator(): ValidatorFn {
    return (control: FormGroup): ValidationErrors | null => {
       return (!this.selection.isEmpty()) ? null : { SelectionError: false };
     } 
    }

  ngOnDestroy(){
    this.subscriptions.forEach(el => el.unsubscribe());
    this.model_id=undefined;
  }


  DatepickerInput_Event_Schedule(event: MatDatepickerInputEvent<Date>): void {
	if(this.model_id>0)
	{
    let filters= this.filterConfiguration()
    this.dataStore_dsList.filterPredicate = function(data, filter: string): boolean {
      console.log( moment(data["dataset_scheduledatetime"].toLowerCase()).format("YYYY-MM-DDT00:00:00.000[Z]"))
      return moment(data["dataset_scheduledatetime"].toLowerCase()).format("YYYY-MM-DDT00:00:00.000[Z]").includes(filter);
    };

    if(filters.dataset_scheduledatetime===""||filters.dataset_scheduledatetime===null){
      this.dataStore_dsList.filter="";
    }
    else{
    this.dataStore_dsList.filter=moment(filters.dataset_scheduledatetime.toLowerCase()).format("YYYY-MM-DDT00:00:00.000[Z]")
    }
  }
  }

  DatepickerInput_Event_CreateAt(event: MatDatepickerInputEvent<Date>): void {
    if(this.model_id>0)
    {
      let filters= this.filterConfiguration()
      this.dataStore_dsList.filterPredicate = function(data, filter: string): boolean {
        return moment(data["createdAt"].toLowerCase()).format("YYYY-MM-DDT00:00:00.000[Z]").includes(filter);
      };
      if(filters.createdAt===""||filters.createdAt===null){
        this.dataStore_dsList.filter="";
      }
      else{
        this.dataStore_dsList.filter=moment(filters.createdAt.toLowerCase()).format("YYYY-MM-DDT00:00:00.000[Z]")
      }
      }
    }

    runByInput_event(event): void {
      if(this.model_id>0)
      {
        this.RunByText=event.value;
        let filters= this.filterConfiguration()
        this.dataStore_dsList.filterPredicate = function(data, filter: string): boolean {
          return data["created_by"].toLowerCase().includes(filter);
        };
        this.dataStore_dsList.filter=filters.created_by
      }
    }
  
  loadDSList(id_parm:number, lastQuery: QueryParamsModel) {    
        this.selection.clear();
        if((!lastQuery)||lastQuery.pageSize===1000){
            lastQuery = new QueryParamsModel(
              this.filterConfiguration(),
              this.sort.direction,
              this.sort.active,
              this.paginator.pageIndex,
              (this.paginator.pageSize===null||this.paginator.pageSize===undefined?this.paginator.pageSize=5:this.paginator.pageSize)
            );
        }
        // Call request from server
        if(this.requestfromAPI){
          this.store.dispatch(new DataSetListRequested({ id : id_parm, page: lastQuery, selected : this.lastSelectedDataSetId }));  
        }
        this.selection.clear();     
  } 

  jumpToStepperTab(index: number) {
    this.stepper.selectedIndex = index;
  }

  selectDistinctTableNames(model:DynamicHeader[], filterByPropertyName:string){
    var flags = [], output = [], l = model.length, i;
    for( i=0; i<l; i++) {
        if( flags[model[i][filterByPropertyName]]) continue;
        flags[model[i][filterByPropertyName]] = true;
        output.push(model[i][filterByPropertyName]);
    }

    return output 
  }

  openDialog() {
		this.dialog.open(ConstraintsListComponent, {
			data: {
        "model_id": this.model_id,
        "model_name": this.selectedModel.model_name
			}
    });
  }

  clickRow(row):void {
    this.selection.toggle(row); 
    if(this.selection.selected[0]){
        this.store.dispatch(new DataSetSelectedFromList({ dslist: this.selection.selected[0]["dataset_id"] }));
    }
  }

  jumpToStep(index: number) {
    this.stepper.selectedIndex = index;
  }

  generateOptimizedSchedule(event,_dataSetDataChange){    
    let doSave:boolean
    let dataSetName:string
    let scheduleDate:Date
    let savedData:boolean
    const dialogConfig = new MatDialogConfig();    
    dialogConfig.width = "80%";
    dialogConfig.height = "80%";
    this.showGantt=false;
    this.store.dispatch(new JobsReset());

      if(this.dataModified){
          //Open Dialogue
          let openDialog= this.dialog.open(SaveDatasetDialogComponent, {
            data: {
              "modelName": this.selectedModel.model_name
            }          
          });

          let aferDialogClose=openDialog.afterClosed().subscribe(result => {
            if (result){
              doSave=result.doSave              
              dataSetName=result.dataSetName
              scheduleDate=result.scheduleDate
              savedData=false
              dialogConfig.data = { dataSetName: dataSetName, scheduleDate:scheduleDate, isMasterDs: doSave, selectedDs:this.lastSelectedDataSetId, selectedModel:this.model_id};
              this.JobDialog = this.dialog.open(SplashScreenComponent, dialogConfig);

              const sub = this.JobDialog.componentInstance.onStop.subscribe(() => {                
              this.store.dispatch(new DataSetDataResetOperationResult());
              this.JobDialog.close();
                });
              this.subscriptions.push(sub);  
          }
         });            
         this.subscriptions.push(aferDialogClose);


      }
      else{
        doSave=false;
        dialogConfig.data = { dataSetName: dataSetName, scheduleDate:scheduleDate, isMasterDs: doSave, selectedDs:this.lastSelectedDataSetId, selectedModel:this.model_id};
        this.JobDialog = this.dialog.open(SplashScreenComponent, dialogConfig);
      }

      if(this.JobDialog){
        const sub = this.JobDialog.componentInstance.onStop.subscribe(() => {
          this.store.dispatch(new DataSetDataResetOperationResult());
          this.JobDialog.close();
          });
        this.subscriptions.push(sub);  
      }
  }

 

  closeDialog() {
    this.dialog.closeAll();
  }
  

	filterConfiguration(): any {
		const filter: any = {};
		const searchText: string = this.searchInput1.nativeElement.value;
		const RunByText: string = this.RunByText
		const scheduleDateText: string = this.datepipe.transform(this.scheduleDateInput.nativeElement.value, 'yyyy-MM-dd hh:mm:ss');
		const createDateText: string = this.datepipe.transform(this.createDateInput.nativeElement.value, 'yyyy-MM-dd hh:mm:ss');

		filter.dataset_name 			= searchText.toLowerCase();
		filter.created_by 				= RunByText.toLowerCase();
		filter.dataset_scheduledatetime	= (scheduleDateText===null?"":new Date(scheduleDateText).toISOString());
		filter.createdAt				= (createDateText===null?"":new Date(createDateText).toISOString());

		return filter;
  }

  goBack(stepper: MatStepper){
    this.showGantt=false;
    stepper.previous();
  }

  childFormValidity($event,formNum:number){
    switch(formNum) { 
      case 1: { 
         this.tab1_validity=$event
         break; 
      } 
      case 2: { 
        this.tab2_validity=$event
         break; 
      } 
      case 3: { 
        this.tab3_validity=$event 
        break; 
      }
      case 4: { 
        this.tab4_validity=$event
        break; 
    }     
   } 

   this.lb_proceed=(this.tab1_validity&&this.tab2_validity&&this.tab3_validity&&this.tab4_validity?true:false)
   this.emitProceedforGenerateScheduleSpinner()
  }
  
  DataSetDataStatus($event,_dataSetDataChange){
    if(_dataSetDataChange.hasOwnProperty("isChanged")){
      if(_dataSetDataChange["isChanged"]!=$event){
        _dataSetDataChange["isChanged"]=$event;
      }
    }
  }

}
