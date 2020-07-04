// Angular
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule, DatePipe  } from '@angular/common';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule }   from '@angular/forms';
// Core Module
import { CoreModule } from '../../../../core/core.module';
import { PartialsModule } from '../../../partials/partials.module';
// Translate Module
import { TranslateModule } from '@ngx-translate/core';
// NGRX
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
// Auth
import { ModuleGuard } from '../../../../core/auth';
// Core => Services
import { BrainDashboard_Service } from '../../../../core/dashboard/service/brain-dashboard.service';
import { DashboardElement_Service } from '../../../../core/dashboard/service/dashboard-element.service';
import { OptModelConstraintsService } from '../../../../core/dashboard/service/opt-model-constraints.service';
import { JobsListService } from '../../../../core/dashboard/service/jobs-list.service';
import { DataSetListService } from '../../../../core/dashboard/service/dataset-list.service';
import { DynamicHeaderService } from '../../../../core/dashboard/service/dynamic_dataset_headers.service';
import { DataSetDataService } from '../../../../core/dashboard/service/dataset_data.service';
import { OptModelsReducer } from '../../../../core/dashboard/_reducers/dashboard.reducer';
import { OptModelConstraintsReducer } from '../../../../core/dashboard/_reducers/constraints.reducer';
import { JobsListReducer } from '../../../../core/dashboard/_reducers/jobs-list.reducer';
import { DataSetListReducer } from '../../../../core/dashboard/_reducers/dataset-list.reducer';
import { DynamicHeaderReducer } from '../../../../core/dashboard/_reducers/dynamic_dataset_headers-reducer';
import { DataSetDataReducer } from '../../../../core/dashboard/_reducers/dataset_data-reducer';
import { JobsInProgressReducer} from '../../../../core/dashboard/_reducers/jobs-in-progress.reducer';
import { OptModelsEffects } from '../../../../core/dashboard/_effects/model.effects';
import { ConstraintsEffects } from '../../../../core/dashboard/_effects/constraints.effects';
import { JobsListEffects } from '../../../../core/dashboard/_effects/jobs-list.effects';
import { DataSetListEffects } from '../../../../core/dashboard/_effects/dataset-list.effects';
import { DynamicHeaderEffects } from '../../../../core/dashboard/_effects/dynamic_dataset_headers.effects';
import { DataSetDataEffects } from '../../../../core/dashboard/_effects/dataset_data.effects';
import { JobInProgressEffects } from '../../../../core/dashboard/_effects/jobs-in-progress.effects';
//import { Job } from '../../../../core/dashboard/_effects/jobs-in-progress.effects';
import { GanttChartService } from '../../../../core/dashboard/service/gantt-chart.service';
import {JobsStatusService} from '../../../../core/dashboard/service/jobs-status.service';
import {SplashScreenService} from '../../../../core/dashboard/service/splash-screen.service';
import {JobsInProgressService} from '../../../../core/dashboard/service/jobs-in-progress.service';
// Core => Utils
import { HttpUtilsService,
	TypesUtilsService,
	InterceptService,
	LayoutUtilsService
} from '../../../../core/_base/crud';
// Shared
import {
	ActionNotificationComponent,
	DeleteEntityDialogComponent,
	FetchEntityDialogComponent,
	UpdateStatusDialogComponent
} from '../../../partials/content/crud';
// Components
import { BrainDashboardComponent } from '../brain-dashboard.component';
import { CardExplorerComponent } from '../card-explorer/card-explorer.component';
import { ConstraintsListComponent } from '../constraints_list/constraints-list.component';
import { JobsListComponent } from '../jobslist/jobs-list.component';
import { DynamicTableComponent } from '../dataset_maintenance/dynamic-table/dynamic-table.component';
import { CardSortingPipe } from '../pipe/card-sorting-pipe.pipe';
import { GroupByPipe } from '../pipe/group-by.pipe';
import { ListFilterPipe } from '../pipe/myfilter.pipe';
import { DataSetMaintenanceComponent } from '../dataset_maintenance/dataset-maintenance.component';
import { TableDetailsComponent } from '../table-details/table-details.component';
import { GanttChartComponent } from '../gantt_chart/gantt-chart.component';
import { SplashScreenComponent } from '../splash-screen/splash-screen.component';
import { SaveDatasetDialogComponent } from '../save-dataset-dialog/save-dataset-dialog.component';
//ngrx
import { NgbProgressbarModule, NgbProgressbarConfig } from '@ng-bootstrap/ng-bootstrap';
import { NgxPermissionsModule } from 'ngx-permissions';

//MomentJS datetime picker
import { OwlDateTimeModule, OwlNativeDateTimeModule, OWL_DATE_TIME_FORMATS } from 'ng-pick-datetime';
import { OwlMomentDateTimeModule, OWL_MOMENT_DATE_TIME_ADAPTER_OPTIONS } from 'ng-pick-datetime-moment';
//import { OWL_MOMENT_DATE_TIME_ADAPTER_OPTIONS } from 'ng-pick-datetime/date-time/adapter/moment-adapter/moment-date-time-adapter.class';
// Material
import {
	MatInputModule,
	MatPaginatorModule,
	MatProgressSpinnerModule,
	MatSortModule,
	MatTableModule,
	MatSelectModule,
	MatMenuModule,
	MatProgressBarModule,
	MatButtonModule,
	MatCheckboxModule,
	MatDialogModule,
	MatTabsModule,
	MatNativeDateModule,
	MatCardModule,
	MatRadioModule,
	MatIconModule,
	MatDatepickerModule,
	MatAutocompleteModule,
	MAT_DIALOG_DEFAULT_OPTIONS,
	MatSnackBarModule,
	MatTooltipModule,
	MatFormFieldModule,
	MatStepperModule,
	MAT_DATE_FORMATS	
} from '@angular/material';

import { MomentDateModule } from '@angular/material-moment-adapter';
//Validator
import { UniquenessValidator } from '../validator/validator';

const routes: Routes = [
	  {
		path: '',
		component: BrainDashboardComponent
	  },
	  { 
		path: 'models',
		children: [{ 
			path: ':id', 
			component: JobsListComponent					
		}]
	  },
	  {
		path: 'roster/:id',	
		component: DataSetMaintenanceComponent		
	  },
	  {
		path: 'rosterm/:id',	
		component: DataSetMaintenanceComponent,
		data : { retrieve : false}	
	  },
	  {
		path: 'roster/:id/:jobid',	
		component: DataSetMaintenanceComponent		
	  },	  	 
	  {
			path: 'roster/edit/:tableName/:seqNum',
			component: DynamicTableComponent
	  },
	  {
		path: 'job-details',	
		component: GanttChartComponent	
	  }	   	 		
];

export const MY_MOMENT_FORMATS = {
    parseInput: 'DD MMM YYYY h:mm a',
    fullPickerInput: 'DD MMM YYYY h:mm a',
    datePickerInput: 'DD MMM YYYY',
    timePickerInput: 'LT',
    monthYearLabel: 'MMM YYYY h:mm ',
    dateA11yLabel: 'DD MMM YYYY h:mm a',
    monthYearA11yLabel: 'MMMM YYYY h:mm a',
};

@NgModule({
	imports: [
		CommonModule,
		PartialsModule,
		CoreModule,
		RouterModule.forChild(routes),
		MatTableModule,
		MatSelectModule,
		MatMenuModule,
		MatProgressBarModule,
		MatSortModule,
		MatButtonModule,
		MatCheckboxModule,
		MatDialogModule,
		MatTabsModule,
		MatNativeDateModule,
		MatCardModule,
		MatRadioModule,
		MatIconModule,
		MatDatepickerModule,
		MatAutocompleteModule,
		NgbProgressbarModule,
		MatSnackBarModule,
		MatTooltipModule,
		MatFormFieldModule,
		MatInputModule,
		MatPaginatorModule,
		MatProgressSpinnerModule,
		MomentDateModule,
		MatStepperModule,
		FormsModule,
		ReactiveFormsModule,
		OwlDateTimeModule, 
		OwlMomentDateTimeModule,
		OwlNativeDateTimeModule,	
    StoreModule.forFeature('models', OptModelsReducer),
		EffectsModule.forFeature([OptModelsEffects]),
	StoreModule.forFeature('constraints', OptModelConstraintsReducer),
		EffectsModule.forFeature([ConstraintsEffects]),	
	StoreModule.forFeature('jobslist', JobsListReducer),
		EffectsModule.forFeature([JobsListEffects]),
	StoreModule.forFeature('dataset', DataSetListReducer),
		EffectsModule.forFeature([DataSetListEffects]),	
	StoreModule.forFeature('dataset_column_headers', DynamicHeaderReducer),
		EffectsModule.forFeature([ DynamicHeaderEffects]),	
	StoreModule.forFeature('dataset_data', DataSetDataReducer),	
		EffectsModule.forFeature([DataSetDataEffects]),	
	StoreModule.forFeature('jobs_in_progress', JobsInProgressReducer),	
		EffectsModule.forFeature([JobInProgressEffects]),				
		NgxPermissionsModule.forChild(),
		TranslateModule.forChild()
	],
	providers: [
		ModuleGuard,
		InterceptService,
      	{
        	provide: HTTP_INTERCEPTORS,
       	 	useClass: InterceptService,
        	multi: true
      	},
        {
          provide: MAT_DIALOG_DEFAULT_OPTIONS,
          useValue: {
            hasBackdrop: true,
            panelClass: 'kt-mat-dialog-container__wrapper',
            height: 'auto',
            width: '900px'
		  },
		},
		{
			provide: OWL_DATE_TIME_FORMATS, 
			useValue: MY_MOMENT_FORMATS
		 },
		 { 
			 provide: OWL_MOMENT_DATE_TIME_ADAPTER_OPTIONS, useValue: { useUtc: false } 
		},
		 {
			provide: MAT_DATE_FORMATS,
			useValue: {
			  parse: {
				dateInput: ['l', 'LL'],
			  },
			  display: {
				dateInput: 'L',
				monthYearLabel: 'MMM YYYY',
				dateA11yLabel: 'LL',
				monthYearA11yLabel: 'MMMM YYYY',
			  },
			},
		  },
        DashboardElement_Service,
		BrainDashboard_Service,
		OptModelConstraintsService,
		JobsListService,
		DataSetListService,
		DynamicHeaderService,
		DataSetDataService,
		JobsInProgressService,
		DatePipe,
		GanttChartService,
		JobsStatusService,
		SplashScreenService,
		TypesUtilsService,
		LayoutUtilsService,
		HttpUtilsService,

  ],
	declarations: [		
    BrainDashboardComponent,
	CardExplorerComponent,
	ConstraintsListComponent,
	JobsListComponent,
	DataSetMaintenanceComponent,
	DynamicTableComponent,
	CardSortingPipe,
	GroupByPipe,
	ListFilterPipe,
	GanttChartComponent,
	TableDetailsComponent,
	SplashScreenComponent,
	SaveDatasetDialogComponent
	],
	entryComponents:[
		ActionNotificationComponent,
		DeleteEntityDialogComponent,
		FetchEntityDialogComponent,
		UpdateStatusDialogComponent,
		ConstraintsListComponent,
		JobsListComponent,
		GanttChartComponent,
		TableDetailsComponent,
		SplashScreenComponent,
		SaveDatasetDialogComponent
	]
})
export class BrainDashboardModule {
}
