// Angular
import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {HttpClient} from '@angular/common/http';
// Material
import {DateAdapter, MAT_DATE_LOCALE} from '@angular/material';
import {MomentDateAdapter} from '@angular/material-moment-adapter';
// Service
import {GanttChartService} from '../../../../core/dashboard/service/gantt-chart.service';
import {JobsStatusService} from '../../../../core/dashboard/service/jobs-status.service';
import {HttpUtilsService} from '../../../../core/_base/crud';
// Models
import {GanttChartItemModel} from '../../../../core/dashboard/_model/gantt-chart-item-model';
import {GanttChartGroupModel} from '../../../../core/dashboard/_model/gantt-chart-group-model';
import {GanttChartData} from '../../../../core/dashboard/_model/gantt-chart-data-model';
import {JobsStatusModel} from '../../../../core/dashboard/_model/jobs-status-model';
import Swal from 'sweetalert2';
import * as $ from 'jquery';
import * as moment from 'moment';
import {environment} from '../../../../../environments/environment';
//Pop-up screen
//rxjs
import {Subscription} from 'rxjs';
import * as Excel from "exceljs";
import * as FileSaver from "file-saver";

declare var vis: any;
const API_Update_OptimizedData_URL = '/api/v1/update_optimized_data_for_model';
const API_Delete_OptimizedData_URL = '/api/v1/delete_optimized_data_for_model';

@Component({
  selector: 'gantt-component',
  templateUrl: './gantt-chart.component.html',
  styleUrls: ['./gantt-chart.component.scss'],
  providers: [
    { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE] }
  ]
})


export class GanttChartComponent implements OnInit {
  @Input() JobId?: string;

  constructor(
      private element: ElementRef,
      public dialog: MatDialog,
      public ganttChartService: GanttChartService,
      private jobsStatusService: JobsStatusService,
      private http: HttpClient,
      private httpUtils: HttpUtilsService,
      private matDialog: MatDialog
  ) { }


  eventData: GanttChartItemModel[] = [];      //Gantt-chart Item Model (Row items)
  resourceData: GanttChartGroupModel[] = [];  //Gantt-chart Group Model (Left most Column)
  receivedData: GanttChartData[] = [];        //Gantt-data Model received from api
  groudIdList: string[] = [];
  jobStatusModel: JobsStatusModel;
  sortingID = '2';
  hideGraph = false;
  timeline: any;
  private subscriptions: Subscription[] = [];

  async delay(ms: number) {
    await new Promise(resolve => setTimeout(() => resolve(), ms));
  }

  ngOnInit() {
    // Render Gantt chart
    const getOptimizedJobsSubscriptions= this.ganttChartService.getOptimizedJobs(this.JobId).subscribe(
        responseData => {
          if (responseData.length>0){
            this.receivedData = responseData[0]["optimized_jobs"];
            this.renderGanttChart();
            this.delay(1000).then(any => {
              this.timeline.redraw();
            });
          }
        });
    this.subscriptions.push(getOptimizedJobsSubscriptions);
  }

  ngOnDestroy(){

    this.subscriptions.forEach(el => el.unsubscribe());
  }

  // Method to detect change in radio buttons (Services, Resources)
  changeOption(event) {
    this.sortingID = event.value;
    const getOptimizedJobsSubscriptions_2= this.ganttChartService.getOptimizedJobs(this.JobId).subscribe(
        responseData => {
          if (responseData.length>0){
            this.receivedData = responseData[0]["optimized_jobs"];
            this.renderGanttChart();
            this.delay(1000).then(any => {
              this.timeline.redraw();
            });
          }
        });
    this.subscriptions.push(getOptimizedJobsSubscriptions_2);
  }



  renderGanttChart() {
    if (this.sortingID === '2') { //sort by "Resources"
      this.groudIdList = [];
      this.resourceData = [];
      this.eventData = [];
      for (let receivedDataObject of this.receivedData) {
        if (receivedDataObject.resourceId != null) {  //Ignore ResourceIds with null values
          if (this.resourceData.length === 0 || this.groudIdList.indexOf(receivedDataObject.resourceId) === -1) {
            //Create Group Objects to display on left most column
            let resourceObject = new GanttChartGroupModel();
            resourceObject.id = receivedDataObject.resourceId;
            //Display Name
            resourceObject.content = receivedDataObject.resourceId;
            //Group Style
            resourceObject.style = "color: black; background-color :rgb(245, 245, 245);font-family: sans-serif; padding-top: 25px;" +
                "border-radius: 5px; font: 15px arial";
            this.resourceData.push(resourceObject);
            this.groudIdList.push(receivedDataObject.resourceId);
          }

          //Create Item Objects to display row items
          let eventObject = new GanttChartItemModel();
          eventObject.id = receivedDataObject.taskId;
          //Mapping item to group
          eventObject.group = receivedDataObject.resourceId;
          eventObject.start = receivedDataObject.startDateTime;
          eventObject.end = receivedDataObject.endDateTime;
          //Tooltop
          eventObject.title = receivedDataObject.serviceId;
          //Display Name
          eventObject.content = receivedDataObject.serviceId;
          //Item Style
          eventObject.style = 'color: white; background-color: hsl(185, 100%, 30%); border-color: white; ' +
              'border-radius: 5px; font: 15px arial, sans-serif; height:  60px; text-align: center; padding-top: 15px;';
          eventObject.className = this.sortingID;
          this.eventData.push(eventObject);
        }
      }
    }



    if (this.sortingID === '1') { // sort by "Services"
      this.hideGraph = true;
      this.groudIdList = [];
      this.resourceData = [];
      this.eventData = [];
      for (let receivedDataObject of this.receivedData) {
        if (this.resourceData.length === 0 || this.groudIdList.indexOf(receivedDataObject.serviceId) === -1) {
            //Create Group Objects to display on left most column
            let resourceObject = new GanttChartGroupModel();
            resourceObject.id = receivedDataObject.serviceId;
            resourceObject.content = receivedDataObject.serviceId;
            if (receivedDataObject.resourceId != null) {
              resourceObject.style = 'color: black; background-color :rgb(245, 245, 245);font-family: sans-serif; padding-top: 25px;' +
                  'border-radius: 5px; font: 15px arial';
            } else {
              resourceObject.style = 'color: black; background-color :rgb(245, 245, 245);font-family: sans-serif;s' +
                  'border-radius: 5px; font: 15px arial';
            }
            this.resourceData.push(resourceObject);
            this.groudIdList.push(receivedDataObject.serviceId);
        }
        if (receivedDataObject.resourceId != null) {  //Ignore ResourceIds with null values
          //Create Item Objects to display row items
          let eventObject = new GanttChartItemModel();
          eventObject.id = receivedDataObject.taskId;
          //Mapping item to group
          eventObject.group = receivedDataObject.serviceId;
          eventObject.start = receivedDataObject.startDateTime;
          eventObject.end = receivedDataObject.endDateTime;
          //Tooltop
          eventObject.title = receivedDataObject.resourceId;
          //Display Name
          eventObject.content = receivedDataObject.resourceId;
          //Item Style
          eventObject.style = 'color: white; background-color: hsl(185, 100%, 30%); border-color: white; ' +
              'border-radius: 5px; font: 15px arial, sans-serif; height:  60px; text-align: center; padding-top: 15px;';
          eventObject.className = this.sortingID;
          this.eventData.push(eventObject);
        }
      }
    }



    //Vis js Methods

    //Inserting Item Array
    let items = new vis.DataSet(
        this.eventData
    );
    //Inserting Group Array
    let groups = new vis.DataSet(
        this.resourceData
    );


    // Configuration for the Timeline
    let options = {

      editable: {
        add: false,         // add new items by double tapping
        updateTime: true,  // drag items horizontally
        updateGroup: true, // drag items from one group to another
        remove: false,       // delete an item by tapping the delete button top right
        overrideItems: false,  // allow these options to override item.editable
      },

      timeAxis: { scale: 'hour', step: 1 },
      showTooltips: true,
      tooltip: {
        followMouse: true,
        overflowMethod: 'cap'
      },

      orientation: { axis: 'both' },
      zoomable: false,
      moveable: false,
      hiddenDates: [
        // hide weekends
        // {
        //   start: '2019-09-07 00:00:00',
        //   end: '2019-09-09 00:00:00',
        //   repeat: 'weekly'
        // },

        // hide outside of 8am to 8pm - use any 2 days and repeat daily
        // {
        //   start: '2019-09-10 20:00:00',
        //   end: '2019-09-11 08:00:00',
        //   repeat: 'daily'
        // }
      ],
      margin: { item: { horizontal: -1, vertical: 20 }, axis: 10 }, // align items of same group in same line
      // groupOrder: 'id',


      //Method to update item start and end time by using pop-up
      onUpdate(item, callback) {

        Swal.fire({
          title: 'Modify Event',
          showCancelButton: true,
          confirmButtonText: 'Update',
          html:
              '<br><br>' +
              '<div style="text-align: left">Event start time</div>' +
              '<input id="item-start" class="swal2-input" value=' + item.start + '>' +
              '<div style="text-align: left">Event end time</div>' +
              '<input id="item-end" class="swal2-input" value=' + item.end + '>',
          preConfirm: function () {
            return new Promise(function (resolve) {
              resolve([
                $('#item-start').val(),
                $('#item-end').val()
              ]);
            });
          }
          ,
          onOpen: function () {
            $('#item-start').focus()
          }
        }).then((result) => {
          if (result.value) {
            item.start = result.value[0];
            item.end = result.value[1];
            item.title = '<b>' + 'Start Time: ' + '</b>' +
                moment(new Date(item.start)).format('DD MMM YYYY, h:mm:ss A') +
                '<br><br><b>' + 'End Time:  ' + '</b>' +
                moment(new Date(item.end)).format('DD MMM YYYY, h:mm:ss A  ');
            callback(item);
            const userToken = 'Bearer ' + localStorage.getItem(environment.authTokenKey);
            var request = new XMLHttpRequest();
            request.open('PUT', environment.apiURL + API_Update_OptimizedData_URL, true);
            request.setRequestHeader('Access-Control-Allow-Origin', '*');
            request.setRequestHeader('Content-Type', 'application/json');
            request.setRequestHeader('Authorization', userToken);
            request.send(JSON.stringify(item));
            Swal.fire({
              title: 'Event has been modified!',
              type: 'success',
            });
          }
        });
      },

      //Method to update item start and end time by dragging and dropping
      onMove(item, callback) {
        item.start = moment(item.start).format('YYYY-MM-DD' + 'T' + 'HH:mm:ss');
        item.end = moment(item.end).format('YYYY-MM-DD' + 'T' + 'HH:mm:ss');
        item.title = '<b>' + 'Start Time: ' + '</b>' +
            moment(new Date(item.start)).format('DD MMM YYYY, h:mm:ss A') +
            '<br><br><b>' + 'End Time:  ' + '</b>' +
            moment(new Date(item.end)).format('DD MMM YYYY, h:mm:ss A  ');
        callback(item);
        const userToken = 'Bearer ' + localStorage.getItem(environment.authTokenKey);
        var request = new XMLHttpRequest();
        request.open('PUT', environment.apiURL + API_Update_OptimizedData_URL, true);
        request.setRequestHeader('Access-Control-Allow-Origin', '*');
        request.setRequestHeader('Content-Type', 'application/json');
        request.setRequestHeader('Authorization', userToken);
        request.send(JSON.stringify(item));
      },


      //Method to remove item by clicking on cross
      onRemove(item, callback) {
        callback(item);
        const userToken = 'Bearer ' + localStorage.getItem(environment.authTokenKey);
        var request = new XMLHttpRequest();
        request.open('DELETE', environment.apiURL + API_Delete_OptimizedData_URL, true);
        request.setRequestHeader('Access-Control-Allow-Origin', '*');
        request.setRequestHeader('Content-Type', 'application/json');
        request.setRequestHeader('Authorization', userToken);
        request.send(JSON.stringify(item));
      }
    };


    // Create a Timeline
    if (!this.hideGraph) {
      this.timeline = new vis.Timeline(this.element.nativeElement, items, options, groups);
    } else {
      //re-create timeline if sorting options are changed
      this.timeline.destroy();
      this.timeline = new vis.Timeline(this.element.nativeElement, items, options, groups);
    }
  }

  downloadXlsx() {
    const getOptimizedJobsSubscriptions = this.ganttChartService.getOptimizedJobs(this.JobId).subscribe(
        responseData => {
          if (responseData.length > 0) {
            this.receivedData = responseData[0]['optimized_jobs'];
            const workbook = new Excel.Workbook();

            for (const sheet of ['View by Resources', 'View by Services']) {
              const jsonData = {};
              const timeStringList = [];
              const data = [['']];
              let earliestStartDateTime = new Date(this.receivedData[0]['startDateTime']);
              let latestStartDateTime = new Date(this.receivedData[0]['startDateTime']);

              if (sheet === 'View by Resources') {
                for (const receivedDataObject of this.receivedData) {
                  if (receivedDataObject['resourceId'] != null) {
                    jsonData[receivedDataObject['resourceId']] = {};
                  }
                }

                for (const receivedDataObject of this.receivedData) {
                  if (receivedDataObject['resourceId'] != null) {
                    const date = new Date(receivedDataObject['startDateTime']);
                    jsonData[receivedDataObject['resourceId']][date.toLocaleString('en-US', {hour: 'numeric', hour12: true})] = [];
                  }
                }

                for (const receivedDataObject of this.receivedData) {
                  if (receivedDataObject['resourceId'] != null) {
                    jsonData[receivedDataObject['resourceId']][(new Date(receivedDataObject['startDateTime'])).toLocaleString(
                        'en-US', {hour: 'numeric', hour12: true})].push(receivedDataObject['serviceId']);
                  }
                }
              } else if (sheet === 'View by Services') {
                for (const receivedDataObject of this.receivedData) {
                  jsonData[receivedDataObject['serviceId']] = {};
                }

                for (const receivedDataObject of this.receivedData) {
                  const date = new Date(receivedDataObject['startDateTime']);
                  jsonData[receivedDataObject['serviceId']][date.toLocaleString('en-US', {hour: 'numeric', hour12: true})] = [];
                }

                for (const receivedDataObject of this.receivedData) {
                  if (receivedDataObject['resourceId'] != null) {
                    jsonData[receivedDataObject['serviceId']][(new Date(receivedDataObject['startDateTime'])).toLocaleString(
                        'en-US', {hour: 'numeric', hour12: true})].push(receivedDataObject['resourceId']);
                  }
                }
              }

              for (const receivedDataObject of this.receivedData) {
                const date = new Date(receivedDataObject['startDateTime']);
                if (date < earliestStartDateTime) {
                  earliestStartDateTime = date;
                }
                if (date > latestStartDateTime) {
                  latestStartDateTime = date;
                }
              }

              earliestStartDateTime.setMinutes(0);
              earliestStartDateTime.setSeconds(0);
              latestStartDateTime.setMinutes(0);
              latestStartDateTime.setSeconds(0);

              const time = new Date(earliestStartDateTime.getTime());
              while (time <= latestStartDateTime) {
                const timeString = time.toLocaleString('en-US', {hour: 'numeric', hour12: true});
                timeStringList.push(timeString);
                time.setHours(time.getHours() + 1);
              }

              for (const timeString of timeStringList) {
                data[0].push(timeString);
              }

              for (const key of Object.keys(jsonData)) {
                const item = jsonData[key];
                let numSpots = 1;
                for (const key2 of Object.keys(item)) {
                  if (item[key2].length > numSpots) {
                    numSpots = item[key2].length;
                  }
                }
                for (let i = 0; i < numSpots; i++) {
                  const row = [key];
                  for (const timeString of timeStringList) {
                    try {
                      row.push(item[timeString][i]);
                    } catch (err) {
                      row.push('');
                    }
                  }
                  data.push(row);
                }
              }

              const worksheet = workbook.addWorksheet(sheet);

              for (const row of data) {
                worksheet.addRow(row);
              }

              for (let columnIndex = 1; columnIndex <= worksheet.columnCount; columnIndex++) {
                worksheet.getColumn(columnIndex).width = 20;
                for (let rowIndex = 0; rowIndex <= worksheet.rowCount; rowIndex++) {
                  const cell = worksheet.getRow(rowIndex).getCell(columnIndex);
                  cell.border = {
                    top: {style: 'thin'},
                    left: {style: 'thin'},
                    bottom: {style: 'thin'},
                    right: {style: 'thin'}
                  };
                  cell.alignment = {vertical: 'middle', horizontal: 'center', wrapText: true};
                }
              }
            }

            workbook.xlsx.writeBuffer()
              .then(buffer => FileSaver.saveAs(new Blob([buffer]), 'Roster.xlsx'))
              .catch(err => console.log('Error writing excel export', err));
          }
        });
    this.subscriptions.push(getOptimizedJobsSubscriptions);
  }
}
