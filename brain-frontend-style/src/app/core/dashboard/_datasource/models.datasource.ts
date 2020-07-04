//Selectors
import { selectModelsonDash, selectOptModelsPageLoading, selectOptModelsInitWaitingMessage } from '../_selector/dashboard.selector';
// RxJS
import { delay, distinctUntilChanged } from 'rxjs/operators';
// CRUD
import { QueryResultsModel, BaseDataSource } from '../../_base/crud';
// State
import { Store, select } from '@ngrx/store';
import { AppState } from '../../reducers';

export class OptModelsDataSource extends BaseDataSource {
	constructor(private store: Store<AppState>) {
		super();
		this.loading$ = this.store.pipe(
			select(selectOptModelsPageLoading)
        );

        this.isPreloadTextViewed$ = this.store.pipe(
            select(selectOptModelsInitWaitingMessage)
        );
    
        this.store.pipe(
            select(selectModelsonDash)
        ).subscribe((response: QueryResultsModel) => {
            this.paginatorTotalSubject.next(response.totalCount);
            this.entitySubject.next(response.items);
        });
    }   
}