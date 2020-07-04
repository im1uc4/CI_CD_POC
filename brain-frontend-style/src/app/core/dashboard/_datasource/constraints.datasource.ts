//Selectors
import { selectConstraintsintheList, selectOptModelConstraintsPageLoading, selectOptModelConstraintInitWaitingMessage } from '../_selector/constraints.selector';
// CRUD
import { QueryResultsModel, BaseDataSource } from '../../_base/crud';
// State
import { Store, select } from '@ngrx/store';
import { AppState } from '../../reducers';

export class ConstraintsDataSource extends BaseDataSource {
	constructor(private store: Store<AppState>) {
		super();
		this.loading$ = this.store.pipe(
			select(selectOptModelConstraintsPageLoading)
        );

        this.isPreloadTextViewed$ = this.store.pipe(
            select(selectOptModelConstraintInitWaitingMessage)
        );
    
        this.store.pipe(
            select(selectConstraintsintheList)
        ).subscribe((response: QueryResultsModel) => {
            this.paginatorTotalSubject.next(response.totalCount);
            this.entitySubject.next(response.items);
        });
    }   
}