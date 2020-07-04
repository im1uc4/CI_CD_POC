//Selectors
import { selectJobsListPageLoading, selectJobsintheList, selectJobsListInitWaitingMessage } from '../_selector/jobs-list-selector';
// CRUD
import { QueryResultsModel, BaseDataSource } from '../../_base/crud';
// State
import { Store, select } from '@ngrx/store';
import { AppState } from '../../reducers';

export class JobsListDataSource extends BaseDataSource {
	constructor(private store: Store<AppState>) {
		super();
		this.loading$ = this.store.pipe(
			select(selectJobsListPageLoading)
        );

        this.isPreloadTextViewed$ = this.store.pipe(
            select(selectJobsListInitWaitingMessage)
        );
    
        this.store.pipe(
            select(selectJobsintheList)
        ).subscribe((response: QueryResultsModel) => {
            this.paginatorTotalSubject.next(response.totalCount);
            this.entitySubject.next(response.items);
        });
    }   
}