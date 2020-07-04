// Angular
import { Component, Output, EventEmitter,Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Store, select } from '@ngrx/store';
import { AppState } from '../../../../core/reducers';
import { Subscription, Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { timer } from 'rxjs';
// Services
import { LayoutUtilsService, QueryParamsModel, QueryResultsModel, MessageType } from '../../../../core/_base/crud';
// Material
import { MAT_DATE_LOCALE, DateAdapter } from '@angular/material';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
// Service
import { HttpUtilsService } from '../../../../core/_base/crud';
import { Update } from '@ngrx/entity';

//Actions
import { DataSetDataOnServerCreated, DataSetDataResetOperationResult} from '../../../../core/dashboard/_actions/dataset_data-actions';
import { JobsReset, JobsCreated, JobsCompleted, JobsUpdated, JobsOperationResult } from '../../../../core/dashboard/_actions/job-in-progress.action';
import { selectJobsInProgress } from '../../../../core/dashboard/_selector/jobs-in-progress-selector';
import { selectDataSetDataById, selectLastDsSavingStatus } from '../../../../core/dashboard/_selector/dataset_data-selector';
import { JobInProgressModel } from '../../../../core/dashboard/_model/job-in-progress.model';
import { JobsStatusService } from '../../../../core/dashboard/service/jobs-status.service';

@Component({
    selector: 'splash-screen-component',
    templateUrl: './splash-screen.component.html',
    styleUrls: ['./splash-screen.component.scss'],
    providers: [
        { provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE] }
    ]
})

export class SplashScreenComponent{

    @Output() onStop = new EventEmitter<any>(true);

    modelId:number=null
    dataSetName:string=null;
    scheduleDate:Date;
    isMasterDs:boolean=false;
    selectedDs:number=null;
    jobId:string=null;
    progress_update:string="";

    selectJobsInProgressId$:  Observable<any>
    selectDataSetDatabyId$: Observable<QueryResultsModel>;

    private subscriptions: Subscription[] = [];

    constructor
    ( 
        @Inject(MAT_DIALOG_DATA) public data: any,
        public dialogRef: MatDialogRef<SplashScreenComponent>,
        private store: Store<AppState>,
        private layoutUtilsService: LayoutUtilsService,
        private jobsStatusService: JobsStatusService
    ){
            //Prevent dialog from closing when clicking outside the dialog area.
            dialogRef.disableClose = true;
    }
   
    ngOnInit() {
        let resCloned;
        this.modelId=this.data.selectedModel;
        this.dataSetName=this.data.dataSetName;
        this.scheduleDate=this.data.scheduleDate;
        this.isMasterDs=this.data.isMasterDs;
        this.selectedDs=this.data.selectedDs;

        this.selectJobsInProgressId$= this.store.pipe(select(selectJobsInProgress));
        const JobsInProgressSubscription=this.selectJobsInProgressId$.subscribe(res=>{
          let resItems=res["items"]
          let lastOperSucceed=res.lastOperationSucceed
          let lastOperMessage=res.lastOpertionMessage
          if(lastOperSucceed){
                if(resItems[0]){
                    if(resItems[0]["jobId"]===this.jobId&&resItems[0]["jobStatus"]==="QUEUING"){
                        this.progress_update="Waiting for Optimized Data" 
                        this.query_JobStatus(this.jobId)
                    }            
                }              
        }
        if(!lastOperSucceed&&lastOperMessage){
          this.layoutUtilsService.showActionNotification(lastOperMessage, MessageType.Create, 5000, true, false);                            
          this.stop()
        }
        })
        this.subscriptions.push(JobsInProgressSubscription);

        this.selectDataSetDatabyId$= this.store.pipe(select(selectDataSetDataById(this.selectedDs)));
        const DataSetDataSubscription=this.selectDataSetDatabyId$.subscribe(res=>{
                if (res) {
                  //SaveDS
                  resCloned = Object.assign({}, res);
                  resCloned["datasetName"]=this.dataSetName
                  resCloned["scheduleDate"]= this.scheduleDate                                 
                }
              });		
      this.subscriptions.push(DataSetDataSubscription);

      if(resCloned)
      {
            this.progress_update="Saving Dataset And Generate Job"
            this.store.dispatch(new DataSetDataOnServerCreated({model_id:this.modelId, dataset_data: resCloned, isMaster: this.isMasterDs ,dataset_id:this.data.selectedDs}));
            const createdDSSubscription = this.store.pipe(select(selectLastDsSavingStatus)).subscribe(res => {
            let msg           =res.message
            let isSuccess     =res.succeed
            let sbtBody       =res.message;
            let savedDataSetId=res.dataset_id
            this.jobId        =res.jobId                        
            let jobsInPrg: JobInProgressModel;
            
            if(!isSuccess&&msg){
                this.layoutUtilsService.showActionNotification(msg, MessageType.Create, 5000, true, false);                            
                this.stop()
            }

            if(this.jobId){
              jobsInPrg=new JobInProgressModel()
              jobsInPrg.id=1
              jobsInPrg.jobId=this.jobId;
              jobsInPrg.jobStatus="CREATED";         
              this.progress_update="Submit Job" 
              this.store.dispatch(new JobsCreated({ jobsInProgress: jobsInPrg, SubmitBody: sbtBody }));
              
            }

            });
            this.subscriptions.push(createdDSSubscription);
        }
    }

    stop() {
        this.store.dispatch(new JobsOperationResult({is_success: false,message:undefined}));
        this.onStop.emit('stop');
    }

    
  ngOnDestroy(){
    this.subscriptions.forEach(el => el.unsubscribe());
  }

  query_JobStatus(jobId:string){    
    const jobDialogSubscription = timer(0, 10000).pipe(
      switchMap(() => this.jobsStatusService.getJobsStatus(jobId))
    ).subscribe(
      result => {
        let res= result[0]["jobs_statuses"];
        if(!res[0]){
          this.layoutUtilsService.showActionNotification("jobsStatusService.getJobsStatus: Object structure error.", MessageType.Create, 5000, true, false); 
          jobDialogSubscription.unsubscribe()
          this.stop()

        }
        if(res[0]["job_status"]==="COMPLETED"){
          this.progress_update="Generating Roster"
          let jipModel:JobInProgressModel =new JobInProgressModel()
          jipModel.jobId=this.jobId
          jipModel.jobStatus=res[0]["job_status"]

          const updateJIP: Update<JobInProgressModel> = {
              id: 1 ,
              changes: jipModel
          };
          jobDialogSubscription.unsubscribe()
          this.store.dispatch(new JobsUpdated({ partialJob: updateJIP,jobInProgressModel: jipModel}));           
          this.stop()
        }        
      }                   
    );
    this.subscriptions.push(jobDialogSubscription); 
  }
    
}
