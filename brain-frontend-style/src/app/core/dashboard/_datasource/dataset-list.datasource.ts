//Selectors
import { selectDSListPageLoading, selectDSintheList, selectDSListInitWaitingMessage } from '../_selector/dataset-list-selector';
// CRUD
import { QueryResultsModel, BaseDataSource } from '../../_base/crud';
// State
import { Store, select } from '@ngrx/store';
import { AppState } from '../../reducers';

export class DatasetListDataSource extends BaseDataSource {
	constructor(private store: Store<AppState>) {
		super();
		this.loading$ = this.store.pipe(
			select(selectDSListPageLoading)
        );

        this.isPreloadTextViewed$ = this.store.pipe(
            select(selectDSListInitWaitingMessage)
        );
    
        this.store.pipe(
            select(selectDSintheList)
        ).subscribe((response: QueryResultsModel) => {
            this.paginatorTotalSubject.next(response.totalCount);
            this.entitySubject.next(response.items);
        });
    }   
}