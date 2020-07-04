// Angular
import { EventEmitter } from '@angular/core';
import { Component, Input, Output, OnInit, OnChanges, ElementRef, ViewChild, ViewChildren, AfterViewInit, ChangeDetectionStrategy, OnDestroy,  ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common'
import { FormGroup, FormControl, Validators } from '@angular/forms';

// Material
import { SelectionModel } from '@angular/cdk/collections';
import { MatPaginator, MatTable, MatSort, MAT_DATE_LOCALE, MatFormFieldModule, DateAdapter, MAT_DATE_FORMATS } from '@angular/material';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
// RXJS
import { Observable, Subscription } from 'rxjs';
// LODASH
import { each, find } from 'lodash';
// NGRX
import { Store, select } from '@ngrx/store';
import { AppState } from '../../../../../core/reducers';
import { Update } from '@ngrx/entity';
//MomentJS datetime picker
import { OWL_DATE_TIME_FORMATS} from 'ng-pick-datetime';

// Services
import { LayoutUtilsService, QueryResultsModel, MessageType  } from '../../../../../core/_base/crud';
import { SubheaderService } from '../../../../../core/_base/layout';
import { DataSetDataService } from '../../../../../core/dashboard/service/dataset_data.service';

//Models
import { DynamicHeader } from '../../../../../core/dashboard/_model/dynamic_dataset_headers-model';
import { DynamicHeadersDataSource } from '../../../../../core/dashboard/_datasource/dynamic_dataset_headers.datasource';
import { DynamicHeaderRequested } from '../../../../../core/dashboard/_actions/dynamic_dataset_headers-actions';
import { DataSetDataItemDeleted, ResetLastModifiedTableFlag,  DataSetDataItemUpdated, DataSetDataItemCreated,DataSetDataResetOperationResult } from '../../../../../core/dashboard/_actions/dataset_data-actions';
import { selectDynamicHeaderName } from '../../../../../core/dashboard/_selector/dynamic_dataset_headers-selector';
import { selectDataSetDataName } from '../../../../../core/dashboard/_selector/dataset_data-selector';
import { DataSetDataModel } from '../../../../../core/dashboard/_model/dataset_data-model';
import { selectLastSelectedModelId } from '../../../../../core/dashboard/_selector/dashboard.selector'

//Action
import { DataSetSelectedFromtheStructure } from '../../../../../core/dashboard/_actions/dataset-list-action';

//Component
import { TableDetailsComponent } from '../../table-details/table-details.component';


//Owl datetime
import { OwlMomentDateTimeModule,  MomentDateTimeAdapter } from 'ng-pick-datetime-moment';
import { DateTimeAdapter, OWL_DATE_TIME_LOCALE } from 'ng-pick-datetime';
import * as moment from 'moment';

//Validator
import { UniquenessValidator } from '../../validator/validator';


export const MY_FORMATS = {
	parse: {
	  dateInput: 'input',
	},
	display: {
	  dateInput: 'DD MMM YYYY h:mm a',
	  monthYearLabel: 'MMM YYYY h:mm a',
	  dateA11yLabel: 'DD MMM YYYY h:mm a',
	  monthYearA11yLabel: 'MMMM YYYY h:mm a',
	},
  };

  export const MY_MOMENT_FORMATS = {
    parseInput: 'DD MMM YYYY h:mm a',
    fullPickerInput: 'DD MMM YYYY h:mm a',
    datePickerInput: 'DD MMM YYYY',
    timePickerInput: 'LT',
    monthYearLabel: 'MMM YYYY h:mm ',
    dateA11yLabel: 'DD MMM YYYY h:mm a',
    monthYearA11yLabel: 'MMMM YYYY h:mm a',
};

@Component({
  selector: 'kt-dynamic-table',
  templateUrl: './dynamic-table.component.html',
  styleUrls: ['./dynamic-table.component.scss'],
  providers: [
    { provide: DateTimeAdapter, useClass: MomentDateTimeAdapter, deps: [OWL_DATE_TIME_LOCALE] },
    { provide: OWL_DATE_TIME_FORMATS, useValue: MY_MOMENT_FORMATS }
    
  ]
})
export class DynamicTableComponent implements OnInit, AfterViewInit{
  @Input() tableName?: string;
  @Input() sequenceNum?: string;
  @Input() formMode?: string;
  @Input() proceed: Observable<boolean>;  
  @Output() generateOptimisedSchedule = new EventEmitter<string>();
  @Output() datasetDataChanged: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() validityChanged: EventEmitter<boolean> = new EventEmitter<boolean>();

  dataSource: DynamicHeadersDataSource;
  displayColumns?: string[]=[];
  columnHeadersResults: DynamicHeader[] = []; 
  loading$: Observable<boolean>;
  displayedColumns: string[];
  displayedColumns_linked: string[];
  dataModel_API: DataSetDataModel[];
  dataModel_API_linked: DataSetDataModel[];
  dataModelDataSource:MatTableDataSource<any[]>;
  dataModel?: any[]=[];
  serviceDDL: any[]=[];
  resourceDDL: any[]=[]; 
  timeWindowDDL: any[]=[]; 
  isDisabled: boolean;

  taset_id:number;
  tableDisplayName:string="";
  jobId:number;
  headersModel?: DynamicHeader[];
  headers_linkedModel?: DynamicHeader[];
  tableName_timewindows:string;
  tableName_skills:string;  
  dataModelMaxID:number;
  dataModelRecordID:number;
  loading:boolean = false;
  model_id: number;
  empty_value:string=null;

  
  header$:         Observable<QueryResultsModel>;
  header_linked$:  Observable<QueryResultsModel>;  
  data$:           Observable<QueryResultsModel>;
  data_linked$:    Observable<QueryResultsModel>;
  serviceDDL_data$:Observable<QueryResultsModel>;
  resourceDDL_data$:Observable<QueryResultsModel>;
  timeWindowDDL_data$:Observable<QueryResultsModel>;
  lastSelectedModel$:  Observable<number>;

  dataModel_linked: any[]=[];
  dataModel_frame: any[]=[];
  frame_object ={};
  frame_object_linked ={};  
  isEditable:boolean=true;
  isEditable_linked:boolean=true;

  // Selection
  selection = new SelectionModel<any>(true, []);

  //Form
  public form: FormGroup;

 @ViewChildren(MatTable) table: MatTable<any>;
 @ViewChildren(MatPaginator) paginator: MatPaginator;
 @ViewChildren(MatSort) sort: MatSort;



  // Subscriptions
  private subscriptions: Subscription[] = [];

  constructor(private activatedRoute: ActivatedRoute,
		private store: Store<AppState>,
		private router: Router,
		private layoutUtilsService: LayoutUtilsService,
    private subheaderService: SubheaderService,
    private DataService:  DataSetDataService,
		private cdr: ChangeDetectorRef,
		public datepipe: DatePipe,
    public dialog: MatDialog    
) {

     }

  ngOnInit() {
          this.isDisabled=false;
          this.tableName_timewindows=this.tableName+"_timewindows"
          this.tableName_skills="skills" 
          if(!this.form){       
            this.form = new FormGroup({});
          }
          const routeSubscription =  this.activatedRoute.params.subscribe(params => {
             if(!this.tableName){
              this.tableName = params['tableName'];
              this.sequenceNum = params['seqNum']; 
              this.tableName_timewindows=this.tableName+"_timewindows" 
             }          
              this.jobId = params['jobid'];  

            });
            this.subscriptions.push(routeSubscription); 
                    

            if(this.proceed){
             const proceedSubscription = this.proceed.subscribe(res => {
                                                                     this.isDisabled=!res})
              this.subscriptions.push(proceedSubscription);
             }

             this.lastSelectedModel$= this.store.pipe(select(selectLastSelectedModelId));
             const lastModelSubscriptions=this.lastSelectedModel$.subscribe(res=>{     
                     this.model_id=res;
                   });		
             this.subscriptions.push(lastModelSubscriptions); 

      if(!this.headersModel){
              this.header$=this.store.pipe(select(selectDynamicHeaderName(this.tableName)));
              const headerSubscriptions=this.header$.subscribe(res=>{
                    if (!res) {
                      return;
                    }

                    this.headersModel=res.items;
                    this.tableDisplayName=this.getHeaderDisplayName(this.headersModel,this.tableName) 
                    if(res.items.length>0)
                    {
                      this.displayColumns=[];
                      this.displayColumns = res.items.map(x => x.columnName);                      
                      this.displayColumns.unshift('index'); 
                      this.displayColumns.push('select'); 
                      this.tableDisplayName=this.getHeaderDisplayName(this.headersModel,this.tableName) 
                      this.frame_object["record_id"]=undefined; 
                      this.frame_object["table_name"]=this.tableName;                                       
                      let ls_placeholder: string="";

                      res.items.forEach(element =>{ 
                        if (element.table_name==="resources"){
                          ls_placeholder="Availability";
                        }
                        if(element.table_name==="skills"){
                          ls_placeholder="Capabilities";
                        }
                       

                        if (element.control_type==="calendar"){                         
                            let ld_today= new Date();  
                            this.frame_object[element.columnName]=ld_today//"2019-08-24 12:00 AM";
                        }
                        if (element.control_type==="textbox"||element.control_type==="dropdownlist"){ 
                            this.frame_object[element.columnName]=undefined;
                        }
                        if (element.control_type==="checkbox"){ 
                          this.frame_object[element.columnName]=false;
                        } 
                         if (element.control_type==="button"){ 

                            this.frame_object[element.columnName]=ls_placeholder;
                        }                                                   
                        this.isEditable=element.is_editable
                      });
                    } 
                  });	
      this.subscriptions.push(headerSubscriptions);       

     
      if(!this.tableDisplayName){
        this.tableDisplayName=this.getHeaderDisplayName(this.headersModel,this.tableName) 
      }     
    }
  }

   convertStringToDate(dateString: string):String{
    let curr = new Date;
    let result_date: string =  moment(dateString).isValid() ? moment(dateString).format('DD MMM YYYY h:mm a') : "";
    
    return result_date 

  }

  getHeaderDisplayName(datasetTable,table){      
      let result;

      if(!datasetTable)
      {
        return null
      }

      if (table==="create_roster")
      {
        result="Create Roster"        
      }
      else{
          if (datasetTable.length>0) 
          {
            result= datasetTable[0]["table_display_name"]
          }
          else
          {
            result=table
          }
      }

      return result;

    }

  generateSchedule(){
    this.isDisabled=false
    this.generateOptimisedSchedule.emit('complete');
  }
  

  ngAfterViewInit(){ 
    if(!this.dataModel||this.dataModel.length===0){        

      this.data$=this.store.pipe(select(selectDataSetDataName(this.tableName)));
      const dataSubscriptions=this.data$.subscribe(res=>{
                  if (!res) {
                    return;
                  }  
            
            this.dataModelMaxID =res.totalCount;
            this.dataModelRecordID=res.recordId;

            if((!res.lastAffectedTable||res.lastAffectedTable===this.tableName)){
                this.dataModel_API  =res.items; 
                this.dataModel      =this.convert_API_dataSetData(res.items);
                             
                 
                this.dataModelDataSource=new MatTableDataSource()
                  

                if(this.paginator){
                  this.dataModelDataSource.paginator=this.paginator["first"];
                }

                if(this.sort){
                  this.dataModelDataSource.sort=this.sort["first"];
                }

                
                this.dataModelDataSource.data=this.dataModel
               

               if(res.lastAffectedTable===this.tableName){
                  this.datasetDataChanged.emit(res.dataModified);
               }

                  //Build dynamic forms
                  let fieldsCtrls = {};                    
                  //
                  for (let f of this.dataModel_API) {
                    let metaDataElement

                    if(this.headersModel){
                      metaDataElement= this.headersModel.find(res => res.table_name === f.table_name && res["columnName"]===f.field_name);
                    }
                    //moment(f.value).format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    fieldsCtrls[f.record_id + f.field_name] = new FormControl((Date.parse(f.value)?new Date(f.value).toISOString():f.value) || "") 
         
                    if((f.field_name==="resourceId"&&f.table_name==="resources")||(f.field_name==="taskId"&&f.table_name==="tasks")
                      ||(f.field_name==="serviceId"&&f.table_name==="services")||(f.field_name==="skillsId"&&f.table_name==="skills")||(f.field_name==="timeWindowId"&&f.table_name==="timewindows")){
                        if (metaDataElement.is_mandatory) {                   
                          fieldsCtrls[f.record_id + f.field_name].setValidators([Validators.required, UniquenessValidator(this.dataModel_API,f.table_name,f.field_name)]);
                        }
                        else{

                          fieldsCtrls[f.record_id + f.field_name].setValidators(UniquenessValidator(this.dataModel_API,f.table_name,f.field_name));                         
                        }
                    }
                    else{
                        if (metaDataElement.is_mandatory) {                   
                          fieldsCtrls[f.record_id + f.field_name].setValidators(Validators.required);
                        }
                    }                     
                  }
                  

                  this.form = new FormGroup(fieldsCtrls);
                  //
                  
                  if(this.form){
                    this.form.statusChanges.subscribe(
                      result => { 
                            this.validityChanged.emit((result==="VALID"?true:false));
                    });
                  }

                  const controls = this.form.controls;
                  Object.keys(controls).forEach(controlName =>                                             
                    controls[controlName].markAsTouched()
                  ); 

                  if (!this.cdr['destroyed']) {
                    this.cdr.detectChanges();
                  }


                  if(this.form&&this.form.status==="INVALID"){
                    this.validityChanged.emit(false);
                  }
                  else{
                    this.validityChanged.emit(true);                      
                  }

              }
                });

    this.subscriptions.push(dataSubscriptions);

    
    this.serviceDDL_data$=this.store.pipe(select(selectDataSetDataName("services")));
    const serviceDDL_Subscription=this.serviceDDL_data$.subscribe(res=>{
      if (!res) {
        return;
      } 

      this.serviceDDL=this.convert_API_dataSetData(res.items);
    
    });

    this.subscriptions.push(serviceDDL_Subscription);

    this.resourceDDL_data$=this.store.pipe(select(selectDataSetDataName("resources")));
    const resourceDDL_Subscription=this.resourceDDL_data$.subscribe(res=>{
      if (!res) {
        return;
      } 

      this.resourceDDL=this.convert_API_dataSetData(res.items);
    
    });

    this.subscriptions.push(resourceDDL_Subscription);


    this.timeWindowDDL_data$=this.store.pipe(select(selectDataSetDataName("timewindows")));
    const timeWindowDDL_Subscription=this.timeWindowDDL_data$.subscribe(res=>{
      if (!res) {
        return;
      } 

      this.timeWindowDDL=this.convert_API_dataSetData(res.items);
    
    });

    this.subscriptions.push(timeWindowDDL_Subscription);
    }
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
        if (e.hasOwnProperty("_createdByApp")){
          result.unshift(this[e.record_id])
        }
        else{
          result.push(this[e.record_id])
        }
        
      } else {
        this[e.record_id][e.field_name] = e.value
      }
    }, {})

    return result
    }
 
openFullPageView(tableName,jobId){
  if (jobId > 0){
    this.router.navigate(['../../../roster/edit/',this.tableName,this.sequenceNum], { relativeTo: this.activatedRoute });
  }
  else{
    this.router.navigate(['../../roster/edit/',this.tableName,this.sequenceNum], { relativeTo: this.activatedRoute });
  }  
}

isControlHasError(controlName: string, validationType: string): boolean {

  const control = this.form.controls[controlName];

  if (!control) {
    return false;
  }

  const result = control.hasError(validationType) && (control.dirty || control.touched);
  return result;
}

openDialog(element,columnName) {

  let dialogName:string=""
  let tableName:string=""
  let keyColumnName_linked:string=""
  let keyValue_linked:string=""

  switch(columnName) { 
    case "View Availability":    
        dialogName="Availability";
        tableName=this.tableName_timewindows;        
        keyColumnName_linked=(tableName==="tasks_timewindows"?"taskId":"resourceId");
        keyValue_linked=(tableName==="tasks_timewindows"?element.taskId:element.resourceId);
      break;
    case "View Capabilities":       
        dialogName="Capabilities";
        tableName=this.tableName_skills;
        keyColumnName_linked="resourceId";
        keyValue_linked=element.resourceId;
      break;
    default:
      break;
  }  

    if(keyValue_linked){
      let openDialog= this.dialog.open(TableDetailsComponent, {
          data: {
            "record_id": element.record_id,
            "title": this.tableDisplayName+" "+dialogName,
            "shouldSizeUpdate": true,
            "tableName":tableName,
            "keyColumnName_linked":keyColumnName_linked,
            "keyValue_linked":keyValue_linked
          }          
        });
      }
    else{
      this.layoutUtilsService.showActionNotification("Please provide value for "+keyColumnName_linked, MessageType.Create, 5000, true, false);
      }
}

  addRecord(){
       //Add data into gui table
       let frame_object=  Object.assign({}, this.frame_object);
       let _newObject
       let ldt_now= new Date()
       let headerModel =this.headersModel
       let headerModel_item
       let maxId:number=0
       let maxRecordId:number=0
       let buttonText
       let newDataArray: DataSetDataModel[]=[]
          
       if(frame_object){ 
            maxRecordId =  (this.dataModelRecordID>0?this.dataModelRecordID:0);
            maxId       = (this.dataModelMaxID>0?this.dataModelMaxID:0);

            //
            maxRecordId++;
            for (let key in headerModel) {
              headerModel_item=headerModel[key]
              if(headerModel_item.hasOwnProperty("control_type")){
                  if(headerModel_item["control_type"]==='button'){
                    if(headerModel_item["columnName"]==="IsTimeWindowExists")
                    {
                      buttonText="View Availability"
                    }  
                    else if(headerModel_item["columnName"]==="IsSkillsExists")
                    {
                      buttonText="View Capabilities"
                    }                               
                  }
              } 
              else{
                    continue;
              } 
              maxId++;            

              _newObject=new DataSetDataModel()
              _newObject.id= maxId
              _newObject.record_id=maxRecordId
              _newObject.table_name=(headerModel_item.hasOwnProperty("table_name")?headerModel_item["table_name"]:"")
              _newObject.table_display_name=(headerModel_item.hasOwnProperty("table_display_name")?headerModel_item["table_display_name"]:"")
              _newObject.dataset_id=(headerModel_item.hasOwnProperty("dataset_id")?headerModel_item["dataset_id"]:"")
              _newObject.field_name=(headerModel_item.hasOwnProperty("columnName")?headerModel_item["columnName"]:"")
              _newObject.value=(headerModel_item["control_type"]==='button'?buttonText:"")
              _newObject.status=true
              _newObject.created_by="System"
              _newObject.updated_by="System"
              _newObject.createdAt=ldt_now
              _newObject.updatedAt=ldt_now

              newDataArray.push(_newObject)
           }     
          
          //No need to unshift. Record will be added automaticaly through model bindingthis.dataModel.unshift(frame_object);
          this.store.dispatch(new DataSetDataItemCreated({dataset_data: newDataArray,affected_table:this.tableName}))
          this.table["first"].renderRows()
         }
         
        
  }
  
  removeSelectedRows() {
        var newDataArray: number[]=[]
        this.selection.selected.forEach(item => {   
        let index: number = this.dataModel.findIndex(d => {
                                                            return (d.id === item.id&& d.record_id === item.record_id)});  
        let ln_index:number
        if (index>=0){
          let record_id= this.dataModel[index]["record_id"]
          let table_name= this.dataModel[index]["table_name"]          
              this.dataModel_API.forEach((element, index) => {
                                                                if((element.record_id === record_id)&&(element.table_name===table_name)) {
                                                                   newDataArray.push(element["id"])                                                                   
                                                                }
                                                            });
        }    
      });
      this.store.dispatch(new DataSetDataItemDeleted({ dataset_data: newDataArray,affected_table:this.tableName }));
      this.selection = new SelectionModel<any>(true, []);
      this.table["first"].renderRows()
    }

  ValueChange(event,columnName,columnValue,columnType){    

    let index: number = this.dataModel.findIndex(element=> element=== event);
    let Value = (columnType==="dropdownlist"?columnValue.value:(columnType==="checkbox"?(columnValue.checked?"true":"false"):(columnType==="datetimepicker"?(moment(new Date(columnValue.toLocaleString())).format('DD MMM YYYY h:mm a')):columnValue)))
    
    if (index>=0){
          let record_id= this.dataModel[index]["record_id"]
          let table_name= this.dataModel[index]["table_name"]
          this.dataModel_API.forEach((element, index) => {
                                                                if((element.record_id === record_id)&&(element.table_name===table_name)&&(element.field_name===columnName)) {
                                                                    let element_temp= Object.assign({}, element);                                                                      
                                                                        element_temp["value"]=Value
                                                                    let updateDataItem: Update<DataSetDataModel> = {
                                                                                                            id: element_temp.id,
                                                                                                            changes: element_temp
                                                                                                          };
                                                                    this.store.dispatch(new  DataSetDataItemUpdated({ partialData: updateDataItem, dataset_data: element,affected_table:this.tableName }));
                                                                }
                                                            });
    }
  }

  returnBack(){
  this.router.navigate(['../../../../rosterm',this.model_id], { relativeTo: this.activatedRoute });
}


  isSortingDisabled(columnType: string){
    let result:boolean= false;
    if (columnType==='button'){
      result=true;
    }

    return  result;
  } 

  ngOnDestroy(){    
    this.store.dispatch(new ResetLastModifiedTableFlag())
    this.subscriptions.forEach(el => el.unsubscribe());
  }

  public doFilter = (value: string) => {
    this.dataModelDataSource.filter = value.trim().toLocaleLowerCase();
  }
}
