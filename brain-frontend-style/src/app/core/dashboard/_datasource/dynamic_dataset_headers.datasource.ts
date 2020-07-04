//Selectors
import { selectDynamicHeaderPageLoading, selectDynamicHeaderintheList, selectDynamicHeaderInitWaitingMessage } from '../_selector/dynamic_dataset_headers-selector';
// CRUD
import { QueryResultsModel, BaseDataSource } from '../../_base/crud';
// State
import { Store, select } from '@ngrx/store';
import { AppState } from '../../reducers';

export class DynamicHeadersDataSource extends BaseDataSource {
	constructor(private store: Store<AppState>) {
		super();
		this.loading$ = this.store.pipe(
			select(selectDynamicHeaderPageLoading)
        );

        this.isPreloadTextViewed$ = this.store.pipe(
            select(selectDynamicHeaderInitWaitingMessage)
        );
    
        this.store.pipe(
            select(selectDynamicHeaderintheList)
        ).subscribe((response: QueryResultsModel) => {
            this.paginatorTotalSubject.next(response.totalCount);
            this.entitySubject.next(response.items);
        });
    }   
}