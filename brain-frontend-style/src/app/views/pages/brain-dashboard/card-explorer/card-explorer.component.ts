// NGRX
import { Store, select } from '@ngrx/store';

// Angular
import { Component, Input, OnChanges, Output, SimpleChanges, EventEmitter, Inject } from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material/dialog';
//Activated
import { ActivatedRoute, Router } from '@angular/router';
//Services and Actions
import { DashboardElement } from '../../../../core/dashboard/element/dashboard-element';
import { ConstraintsListComponent } from '../constraints_list/constraints-list.component';
import { OptModuleSelectedFromDashboard } from '../../../../core/dashboard/_actions/opt-model-actions';
import { AppState } from '../../../../core/reducers';


@Component({
  selector: 'kt-card-explorer',
  templateUrl: './card-explorer.component.html',
  styleUrls: ['./card-explorer.component.scss']
})
export class CardExplorerComponent  implements OnChanges {
  @Input() dashElements: DashboardElement[]
  @Input() canNavigateUp: string
  @Input() path: string

  @Output() folderAdded = new EventEmitter<{ name: string }>()
  
  @Output() navigatedDown = new EventEmitter<DashboardElement>()
  @Output() navigatedUp = new EventEmitter()

  constructor(public dialog: MatDialog,
              private activatedRoute: ActivatedRoute,		
              private router: Router,
              private store: Store<AppState>) { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {}

  public open(selectedModel: any){
         let id= selectedModel.id

       //to memorise last selected datase
      this.store.dispatch(new OptModuleSelectedFromDashboard({ model: selectedModel }));
      this.router.navigate(['./models',id], { relativeTo: this.activatedRoute });
  }

  openDialog(model_id:number,model_name: string) {
		this.dialog.open(ConstraintsListComponent, {
			data: {
        "model_id": model_id,
        "model_name": model_name
			}
		});
  }
  
}

