//core
import { Injectable } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { switchMap } from 'rxjs/operators';
import { timer, Subscription } from 'rxjs';
import { Router, ActivatedRoute } from '@angular/router';
import { Store } from '@ngrx/store';
import { AppState } from '../../reducers';
//Actions
import { DataSetSelectedFromtheStructure } from '../../../core/dashboard/_actions/dataset-list-action';
//Service
import { JobsStatusService } from './jobs-status.service';
//Models
import { JobsStatusModel } from '../_model/jobs-status-model';
//components
import { SplashScreenComponent } from '../../../views/pages/brain-dashboard/splash-screen/splash-screen.component';



@Injectable({
    providedIn: 'root'
  })
  export class SplashScreenService {
 
    
    subscription: Subscription;
    jobStatusModel: JobsStatusModel;
    constructor(
        private activatedRoute: ActivatedRoute,
	    private store: Store<AppState>,
        public dialog: MatDialog,
        private router: Router,
        private jobsStatusService: JobsStatusService
    ) { }
  
  
    closeDialog() {
        this.dialog.closeAll();
      }

  }