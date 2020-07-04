//Angular
import { Inject, Component, OnInit, Input, ViewChildren, ViewChild } from '@angular/core';
// Material
import { SelectionModel } from '@angular/cdk/collections';
import { MAT_DIALOG_DATA, MatSort, MatDialog, MatDialogRef, } from '@angular/material';
import { MatPaginator, MatTable, MatDatepickerInputEvent,  MAT_DATE_LOCALE, MatSnackBar, MatFormFieldModule, DateAdapter, MAT_DATE_FORMATS } from '@angular/material';
import { MatTableDataSource } from '@angular/material/table';
//MomentJS datetime picker
import { OWL_DATE_TIME_FORMATS} from 'ng-pick-datetime';
// NGRX
import { Store, select } from '@ngrx/store';
import { AppState } from '../../../../core/reducers';
import { Update } from '@ngrx/entity';
//RxJS
import { Observable, Subscription } from 'rxjs';
//Services and Models
import { DynamicHeader } from '../../../../core/dashboard/_model/dynamic_dataset_headers-model';
import { DataSetDataItemDeleted,  DataSetDataItemUpdated, DataSetDataItemCreated } from '../../../../core/dashboard/_actions/dataset_data-actions';
import { DataSetDataModel } from '../../../../core/dashboard/_model/dataset_data-model';
import { QueryResultsModel } from '../../../../core/_base/crud';
import { selectDynamicHeaderName } from '../../../../core/dashboard/_selector/dynamic_dataset_headers-selector';
import { selectDataSetDataName, selectDataSetLinkedDataforID } from '../../../../core/dashboard/_selector/dataset_data-selector';
//Owl datetime
import { OwlMomentDateTimeModule, MomentDateTimeAdapter } from 'ng-pick-datetime-moment';
import { DateTimeAdapter, OWL_DATE_TIME_LOCALE } from 'ng-pick-datetime';
import * as moment from 'moment';
//Validator
import { UniquenessValidator } from '../validator/validator';
import { FormGroup, FormControl, Validators } from '@angular/forms';

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
  selector: 'kt-table-details',
  templateUrl: './table-details.component.html',
  styleUrls: ['./table-details.component.css'],
  providers: [
    { provide: DateTimeAdapter, useClass: MomentDateTimeAdapter, deps: [OWL_DATE_TIME_LOCALE] },
    { provide: OWL_DATE_TIME_FORMATS, useValue: MY_MOMENT_FORMATS }
  ]
})
export class TableDetailsComponent implements OnInit {
  private shouldSizeUpdate: boolean;
  selection = new SelectionModel<any>(true, []);
  // Subscriptions
  private subscriptions: Subscription[] = [];

  dataModel_API_linked: DataSetDataModel[];
  headers_linkedModel: DynamicHeader[];
  dataModel_linked: any[]=[];
  displayedColumns_linked: string[];
  frame_object_linked ={};
  dataModelMaxID:number=0;
  dataModelRecordID:number=0;
  isEditable_linked:boolean=true;
  tableName_timewindows:string;
  tableName_skills:string;
  serviceDDL: any[]=[];
  resourceDDL: any[]=[];
  timeWindowDDL: any[]=[];
  dataModelDataSource:MatTableDataSource<any[]>;

  header_linked$:  Observable<QueryResultsModel>;
  data_linked$:    Observable<QueryResultsModel>;
  serviceDDL_data$:Observable<QueryResultsModel>;
  resourceDDL_data$:Observable<QueryResultsModel>;
  timeWindowDDL_data$:Observable<QueryResultsModel>;

  //Form
  public form: FormGroup;
  @ViewChildren(MatTable) table: MatTable<any>;
  @ViewChild(MatSort, {static:true}) sort: MatSort;

  constructor(private dialogRef: MatDialogRef<TableDetailsComponent>,
              private store: Store<AppState>,
              @Inject(MAT_DIALOG_DATA) public data: any) {
                this.shouldSizeUpdate = data.shouldSizeUpdate;
                //Prevent dialog from closing when clicking outside the dialog area.
                dialogRef.disableClose = false;
              }

  ngOnInit() {
    if (this.shouldSizeUpdate) this.updateSize();

    this.header_linked$=this.store.pipe(select(selectDynamicHeaderName(this.data.tableName)));
    this.data_linked$=this.store.pipe(select(selectDataSetLinkedDataforID(this.data.tableName,this.data.keyColumnName_linked, this.data.keyValue_linked)))

    const header_linkedSubscriptions=this.header_linked$.subscribe(res=>{
              let ls_placeholder: string="";
              if (!res) {
                return;
              }

              this.headers_linkedModel=res.items;

              if(res.items.length>0)
              {
                this.displayedColumns_linked=[];

                //this.displayedColumns_linked = res.items.map(x =>{
                this.displayedColumns_linked = res.items.filter(x =>x.is_visible&&x.columnName!=this.data.keyColumnName_linked)
                                                        .map(res=>res.columnName);
                this.displayedColumns_linked.unshift('index');
                this.displayedColumns_linked.push('select');


                this.frame_object_linked["record_id"]=undefined;
                this.frame_object_linked["table_name"]=this.data.tableName;

                res.items.forEach(element =>{
                  if (element.table_name==="resources"){
                    ls_placeholder="Availability";
                  }
                  if(element.table_name==="skills"){
                    ls_placeholder="Capabilities";
                  }


                  if (element.control_type==="calendar"){
                      let ld_today= new Date();
                      this.frame_object_linked[element.columnName]=ld_today;
                  }
                  if (element.control_type==="textbox"||element.control_type==="dropdownlist"){
                      this.frame_object_linked[element.columnName]=undefined;
                  }
                  if (element.control_type==="checkbox"){
                    this.frame_object_linked[element.columnName]=false;
                  }
                    if (element.control_type==="button"){

                      this.frame_object_linked[element.columnName]=ls_placeholder;
                  }
                  this.isEditable_linked=element.is_editable
              })
            }
          });

          this.subscriptions.push(header_linkedSubscriptions);

          const data_linkedSubscriptions=this.data_linked$.subscribe(res=>{
            if (!res) {
              return;
            }

            this.dataModelMaxID =res.totalCount;
            this.dataModelRecordID=res.recordId;
            this.dataModel_API_linked=res.items
            this.dataModel_linked=this.convert_API_dataSetData(res.items);

            this.dataModelDataSource=new MatTableDataSource()

            if(this.sort){
              this.dataModelDataSource.sort=this.sort;
            }

            this.dataModelDataSource.data =this.dataModel_linked

                                //Build dynamic forms
                                let fieldsCtrls = {};
                                //
                                for (let f of this.dataModel_API_linked) {
                                  let metaDataElement

                                  if(this.headers_linkedModel){
                                    metaDataElement= this.headers_linkedModel.find(res => res.table_name === f.table_name && res["columnName"]===f.field_name);
                                  }

                                  fieldsCtrls[f.record_id + f.field_name] = new FormControl((Date.parse(f.value)?new Date(f.value).toISOString():f.value) || "")

                                  if(f.field_name==="skillsId"){
                                      if (metaDataElement.is_mandatory) {
                                        fieldsCtrls[f.record_id + f.field_name].setValidators([Validators.required, UniquenessValidator(this.dataModel_API_linked,f.table_name,f.field_name)]);
                                      }
                                      else{

                                        fieldsCtrls[f.record_id + f.field_name].setValidators(UniquenessValidator(this.dataModel_API_linked,f.table_name,f.field_name));
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
                                const controls = this.form.controls;
                                Object.keys(controls).forEach(controlName =>
                                  controls[controlName].markAsTouched()
                                );
          })

          this.subscriptions.push(data_linkedSubscriptions);

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

  updateSize(){
    this.dialogRef.updateSize("auto", "auto");
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

  convertStringToDate(dateString: string):String{
    let curr = new Date;
    let result_date: string =  moment(dateString).isValid() ? moment(dateString).format('DD MMM YYYY h:mm a') : "";

    return result_date

  }

addRecord(tableName = null) {
      if ((tableName === 'resources_timewindows' || tableName === 'tasks_timewindows') && this.dataModelDataSource.data.length === this.timeWindowDDL.length) {
          return;
      }
  //Add data into gui table
  let frame_object=  Object.assign({}, this.frame_object_linked);
  let _newObject
  let ldt_now= new Date()
  let headerModel =this.headers_linkedModel
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
         maxId++;

         _newObject=new DataSetDataModel()
         _newObject.id= maxId
         _newObject.record_id=maxRecordId
         _newObject.table_name=(headerModel_item.hasOwnProperty("table_name")?headerModel_item["table_name"]:"")
         _newObject.table_display_name=(headerModel_item.hasOwnProperty("table_display_name")?headerModel_item["table_display_name"]:"")
         _newObject.dataset_id=(headerModel_item.hasOwnProperty("dataset_id")?headerModel_item["dataset_id"]:"")
         _newObject.field_name=(headerModel_item.hasOwnProperty("columnName")?headerModel_item["columnName"]:"")
         _newObject.value=(headerModel_item["control_type"]==='button'?buttonText:(headerModel_item["columnName"]===this.data.keyColumnName_linked?this.data.keyValue_linked:""))
         _newObject.status=true
         _newObject.created_by="System"
         _newObject.updated_by="System"
         _newObject.createdAt=ldt_now
         _newObject.updatedAt=ldt_now

        newDataArray.push(_newObject)
      }

     //No need to unshift. Record will be added automaticaly through model bindingthis.dataModel.unshift(frame_object);
     this.store.dispatch(new DataSetDataItemCreated({dataset_data: newDataArray, affected_table:this.data.tableName}))
     this.table["first"].renderRows()
    }


}



removeSelectedRows() {
  var newDataArray: number[]=[]
  this.selection.selected.forEach(item => {
  let index: number = this.dataModel_linked.findIndex(d => {
                                                      return (d.id === item.id&& d.record_id === item.record_id)});
  let ln_index:number
  if (index>=0){
    let record_id= this.dataModel_linked[index]["record_id"]
    let table_name= this.dataModel_linked[index]["table_name"]
        this.dataModel_API_linked.forEach((element, index) => {
                                                          if((element.record_id === record_id)&&(element.table_name===table_name)) {
                                                            newDataArray.push(element["id"])
                                                          }
                                                      });
  }
});
this.store.dispatch(new DataSetDataItemDeleted({ dataset_data: newDataArray, affected_table: this.data.tableName }));
this.selection = new SelectionModel<any>(true, []);
this.table["first"].renderRows()
}

  ValueChange(event,columnName,columnValue,columnType){
    let Value = (columnType==="dropdownlist"?columnValue.value:(columnType==="checkbox"?(columnValue.checked?"true":"false"):(columnType==="datetimepicker"?(moment(new Date(columnValue.toLocaleString())).format('DD MMM YYYY h:mm a')):columnValue)))
    let index: number = this.dataModel_linked.findIndex(element=> element=== event);
    this.dataModel_linked[index][columnName]=(columnType==="checkbox"?(Value.checked?"true":"false"):Value)

    if (index>=0){
          let record_id= this.dataModel_linked[index]["record_id"]
          let table_name= this.dataModel_linked[index]["table_name"]

          this.dataModel_API_linked.forEach((element, index) => {
                                                                if((element.record_id === record_id)&&(element.table_name===table_name)&&(element.field_name===columnName)) {
                                                                    let element_temp= Object.assign({}, element);
                                                                        element_temp["value"]=Value
                                                                    let updateDataItem: Update<DataSetDataModel> = {
                                                                                                            id: element_temp.id,
                                                                                                            changes: element_temp
                                                                                                          };
                                                                    this.store.dispatch(new  DataSetDataItemUpdated({ partialData: updateDataItem, dataset_data: element, affected_table:this.data.tableName }));
                                                                }
                                                            });
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

  ngOnDestroy(){
    this.subscriptions.forEach(el => el.unsubscribe());
    }

  closeDialog(){
    this.dialogRef.close();
  }

    isAllSelected() {
        const numSelected = this.selection.selected.length;
        const numRows = this.dataModelDataSource.data.length;
        return numSelected === numRows;
    }

    selectAllToggle() {
        if (this.isAllSelected()) {
            this.dataModelDataSource.data.forEach(row => {
                this.selection.deselect(row);
            });
        } else {
            this.dataModelDataSource.data.forEach(row => {
                this.selection.select(row);
            });
        }
    }

    checkboxLabel(row?: DataSetDataModel): string {
        if (!row) {
            return `${this.isAllSelected() ? 'select' : 'deselect'} all`;
        }
        return `${this.selection.isSelected(row) ? 'deselect' : 'select'} row ${row.id + 1}`;
    }

    unselectedTimeWindows(element) {
      const unselectedTimeWindows = [];
      outer:
      for (const timeWindowDDL of this.timeWindowDDL) {
          if (element.timeWindowId === timeWindowDDL.timeWindowId) {
              unselectedTimeWindows.push(timeWindowDDL);
              continue;
          }
          for (const selectedTimeWindow of this.dataModelDataSource.data) {
              if (timeWindowDDL.timeWindowId === selectedTimeWindow['timeWindowId']) {
                  continue outer;
              }
          }
          unselectedTimeWindows.push(timeWindowDDL);
      }
      return unselectedTimeWindows;
    }

    addDeleteAllRecords() {
      if (this.dataModelDataSource.data.length === this.timeWindowDDL.length) {
          this.selectAllToggle();
          this.removeSelectedRows();
      } else {
          this.selectAllToggle();
          this.removeSelectedRows();
          for (let i = this.timeWindowDDL.length - 1; i >= 0; i--) {
              this.addRecord();
              this.ValueChange(this.dataModelDataSource.data[0], 'timeWindowId',
                  {value: this.timeWindowDDL[i].timeWindowId}, 'dropdownlist');
          }
      }
    }

    addDeleteAllString() {
      return this.dataModelDataSource.data.length === this.timeWindowDDL.length ? 'Delete All' : 'Add All';
    }
}
