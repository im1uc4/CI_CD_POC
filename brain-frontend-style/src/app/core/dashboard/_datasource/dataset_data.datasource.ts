//Selectors
import { selectDataSetDataPageLoading, selectDataSetDataintheList, selectDataSetDataInitWaitingMessage } from '../_selector/dataset_data-selector';
// CRUD
import { QueryResultsModel, BaseDataSource } from '../../_base/crud';
// State
import { Store, select } from '@ngrx/store';
import { AppState } from '../../reducers';

export class DataSetDataDataSource extends BaseDataSource {
	constructor(private store: Store<AppState>) {
		super();
		this.loading$ = this.store.pipe(
			select(selectDataSetDataPageLoading)
        );

        this.isPreloadTextViewed$ = this.store.pipe(
            select(selectDataSetDataInitWaitingMessage)
        );
    
        this.store.pipe(
            select(selectDataSetDataintheList)
        ).subscribe((response: QueryResultsModel) => {
            this.paginatorTotalSubject.next(response.totalCount);
            this.entitySubject.next(response.items);
        });
    }   
}