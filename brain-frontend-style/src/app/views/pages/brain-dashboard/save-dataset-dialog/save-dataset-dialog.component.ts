import { Component, OnInit, Inject} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, } from '@angular/material';
import { MAT_DATE_LOCALE, DateAdapter, MAT_DATE_FORMATS } from '@angular/material';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
// RXJS
//import { debounceTime, distinctUntilChanged, tap } from 'rxjs/operators';
//import { fromEvent, Subscription } from 'rxjs';
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
  selector: 'kt-save-dataset-dialog',
  templateUrl: './save-dataset-dialog.component.html',
  styleUrls: ['./save-dataset-dialog.component.scss'],
  providers: [
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
    {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS},
  ]
})
export class SaveDatasetDialogComponent implements OnInit {
  dataSetName: string="";
  startValue:Date;    
  //private subscriptions: Subscription[] = [];


  //@ViewChild('picker', {static: true}) picker:  ElementRef;
  //@ViewChild('renameDsField', {static: true}) renameDsField: ElementRef;

  constructor
  ( 
      public dialogRef: MatDialogRef<SaveDatasetDialogComponent>,
      @Inject(MAT_DIALOG_DATA) public data: any
  ){
          //Prevent dialog from closing when clicking outside the dialog area.
          dialogRef.disableClose = false;
  }

  ngOnInit() {  
            this.dataSetName =this.getDataSetName()
            this.startValue = new Date();

           /* const searchSubscription = fromEvent(this.renameDsField.nativeElement, 'keyup').pipe(					
              debounceTime(150), // The user can type quite quickly in the input box, and that could trigger a lot of server requests. With this operator, we are limiting the amount of server requests emitted to a maximum of one every 150ms
              distinctUntilChanged(), // This operator will eliminate duplicate values
              tap(() => {
                })
              )
            .subscribe();
        this.subscriptions.push(searchSubscription);*/
      }

  close() {
    this.dialogRef.close({doSave:false,dataSetName: this.dataSetName, scheduleDate: this.startValue});
  }

  save() {
    this.dialogRef.close({doSave:true,dataSetName: this.dataSetName, scheduleDate: this.startValue});
  }

  getDataSetName():string{
    const myMoment: string = moment(new Date()).format('YYYY-MM-DD h:mm:ss A');
    return (this.data.modelName+" - "+myMoment);
  }

}
