// NGRX
import { createFeatureSelector } from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter, Update } from '@ngrx/entity';
// Actions
import { DynamicHeaderActions, DynamicHeaderActionTypes, DynamicHeaderReset } from '../_actions/dynamic_dataset_headers-actions';
// Models
import { DynamicHeader } from '../_model/dynamic_dataset_headers-model';

export interface DynamicHeaderState extends EntityState<DynamicHeader> {
    columnsLoading: boolean;
    actionsloading: boolean;
    totalCount: number;
    showInitWaitingMessage: boolean;
}

export const adapter: EntityAdapter<DynamicHeader> = createEntityAdapter<DynamicHeader>();

export const initialDynamicHeaderState: DynamicHeaderState  = adapter.getInitialState({
    columnsLoading: false,
    actionsloading: false,
    totalCount: 0,
    showInitWaitingMessage: true
});

export function DynamicHeaderReducer(state = initialDynamicHeaderState, action: DynamicHeaderActions): DynamicHeaderState {
    switch  (action.type) {    
        case DynamicHeaderActionTypes.DynamicHeaderLoaded:
            return adapter.addMany(action.payload.headers, {
                ...initialDynamicHeaderState,            
                listLoading: false,
                showInitWaitingMessage: false
            });
        case DynamicHeaderActionTypes.DynamicHeaderReset: return {...initialDynamicHeaderState }
        default: return state;
    }
}

export const getDynamicHeaderState = createFeatureSelector<DynamicHeader>('dataset_column_headers');

export const {
    selectAll,
    selectEntities,
    selectIds,
    selectTotal
} = adapter.getSelectors();